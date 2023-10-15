package mrkto.mvoice.api;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.Events.OnPlayerStartSpeak;
import mrkto.mvoice.api.Events.OnPlayerStopSpeak;
import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.audio.speaker.speakerWriter;
import mrkto.mvoice.utils.FileUtils;
import mrkto.mvoice.api.Voice.data.PLayersData;
import mrkto.mvoice.api.Voice.data.PlayerData;
import mrkto.mvoice.utils.other.mclib.MVIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;


public class EventHandler {
    private static int i = 0;
    public static Map<String, Byte> list = new HashMap<>();
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void PlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event){
        speakerWriter.deleteAllChanels();
    }
    @SubscribeEvent
    public void PlayerLogIn(PlayerEvent.PlayerLoggedInEvent event){
        if(Side.CLIENT.isClient())
            speakerWriter.createDefault();
        PLayersData data = FileUtils.getPlayersData();
        if(data.getPlayerData(event.player.getUniqueID()) == null)
            data.createNew(event.player.getUniqueID());
        FileUtils.setPlayersData(data);
    }
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if(i > 5){
            i = 0;
            speakerWriter.tickDel();
        }
        i++;
    }
    public float alpha = 0.3f;
    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post e){
        if (e.getType() == RenderGameOverlayEvent.ElementType.ALL) {
            this.alpha = Math.max(0f, Math.min(1, microReader.IsRecording() ? this.alpha + MappetVoice.fadetime.get() : this.alpha - MappetVoice.fadetime.get() / 2));
            if (alpha > 0f) {
                ScaledResolution resolution = e.getResolution();
                GlStateManager.pushMatrix();
                GlStateManager.translate(resolution.getScaledWidth() - 32, resolution.getScaledHeight() - 32, 0);
                GlStateManager.color(1, 1f, 1, alpha);
                GlStateManager.scale(1f, 1f, 1f);
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MappetVoice.MOD_ID, "textures/voice.png"));
                Gui.drawModalRectWithCustomSizedTexture(0, 0, MVIcons.Micro.x, MVIcons.Micro.y, MVIcons.Micro.w, MVIcons.Micro.h, MVIcons.Micro.textureW, MVIcons.Micro.textureH);
                GlStateManager.popMatrix();
                GlStateManager.color(1, 1, 1, 1F);
            }
        }
    }
    @SubscribeEvent
    public void startSpeak(OnPlayerStartSpeak e){
        list.put(e.getPlayer().getName(), e.getType());
        if(e.getType() == 2){
            EntityPlayerMP player = e.getPlayer();
            if(MappetVoice.onRadioSound.get())
                player.connection.sendPacket(new SPacketCustomSound("mvoice:RadioOn", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 1f, 1f));
            if(MappetVoice.onRadioSound.get() && MappetVoice.hearOther.get())
                for(EntityPlayerMP ListedPlayer : MappetVoice.server.getPlayerList().getPlayers())
                    if(!ListedPlayer.equals(player))
                        ListedPlayer.connection.sendPacket(new SPacketCustomSound("mvoice:RadioOn", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 0.1f, 1f));
        }
    }
    @SubscribeEvent
    public void stopSpeak(OnPlayerStopSpeak e) {
        byte type = list.get(e.getPlayer().getName());
        list.put(e.getPlayer().getName(), (byte) 0);
        if (type == 2) {
            EntityPlayerMP player = e.getPlayer();
            if (MappetVoice.offRadioSound.get())
                player.connection.sendPacket(new SPacketCustomSound("mvoice:RadioOff", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 1f, 1f));
            if (MappetVoice.offRadioSound.get() && MappetVoice.hearOther.get())
                for (EntityPlayerMP ListedPlayer : MappetVoice.server.getPlayerList().getPlayers())
                    if (!ListedPlayer.equals(player))
                        ListedPlayer.connection.sendPacket(new SPacketCustomSound("mvoice:RadioOff", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 0.1f, 1f));
        }
    }
}




