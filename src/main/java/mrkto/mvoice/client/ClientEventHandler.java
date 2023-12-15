package mrkto.mvoice.client;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.Events.OnEngineRegistry;
import mrkto.mvoice.capability.Profile;
import mrkto.mvoice.client.audio.AudioEngineLoader;
import mrkto.mvoice.client.audio.DefaultAudioSystemManager;
import mrkto.mvoice.network.Dispatcher;
import mrkto.mvoice.utils.other.UnableInitiateEngineException;
import mrkto.mvoice.utils.other.mclib.MVIcons;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
@SideOnly(Side.CLIENT)
public class ClientEventHandler {
    private static int i = 0;
    public boolean canSpeak = true;

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        Profile profile = Profile.get(Minecraft.getMinecraft().player);
        if(profile != null) {
            canSpeak = !profile.getMuted();
        }
        if(i++ < 5){
            return;
        }
        MappetVoice.AudioManager.getOutput().update();
        if(MappetVoice.voiceaction.get() && !MappetVoice.AudioManager.getInput().isRecording()){
            MappetVoice.AudioManager.getInput().startRecording(false);
        }
        if(Minecraft.getMinecraft().player == null){
            return;
        }
//        if(client == null){
//            client = new Client(Minecraft.getMinecraft().player.getName());
//        }
//        if(Minecraft.getMinecraft().getCurrentServerData() != null){
//
//            if(!client.isConnected()){
//                if(ConnectServerPacket.serverPort != 0){
//
//                        if(!client.connect(Minecraft.getMinecraft().getCurrentServerData().serverIP, ConnectServerPacket.serverPort)){
//                            client.connect("localhost", ConnectServerPacket.serverPort);
//                        }
//                }else{
//                    Dispatcher.sendToServer(new ConnectServerPacket());
//                }
//            }
//        }else{
//            ConnectServerPacket.serverPort = 0;
//            if(client.isConnected()){
//                client.disconnect();
//            }
//        }

    }
    public float alpha = 0.0f;
    @SubscribeEvent
    public void onOverlayRender(RenderGameOverlayEvent.Post e){
        if (e.getType() != RenderGameOverlayEvent.ElementType.ALL)
            return;
        boolean condition = !MappetVoice.voiceaction.get() ? MappetVoice.AudioManager.getInput().isRecording() : Dispatcher.sending;
        this.alpha = Math.max(0f, Math.min(1, condition ? this.alpha + MappetVoice.fadetime.get() : this.alpha - MappetVoice.fadetime.get() / 2));
        if (alpha > 0f) {
            ScaledResolution resolution = e.getResolution();
            GlStateManager.pushMatrix();
            GlStateManager.scale(1f, 1f, 1f);
            GlStateManager.translate(resolution.getScaledWidth() - 32, resolution.getScaledHeight() - 32, 0);
            float color = canSpeak ? 1f : 0.3f;
            GlStateManager.color(1f, color, color, alpha);
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(MappetVoice.MOD_ID, "textures/voice.png"));
            Gui.drawModalRectWithCustomSizedTexture(0, 0, MVIcons.Micro.x, MVIcons.Micro.y, MVIcons.Micro.w, MVIcons.Micro.h, MVIcons.Micro.textureW, MVIcons.Micro.textureH);
            GlStateManager.popMatrix();
            GlStateManager.color(1f, 1f, 1f, 1f);
        }
    }
    @SubscribeEvent
    public void PlayerLogOut(PlayerEvent.PlayerLoggedOutEvent event){
        MappetVoice.AudioManager.terminate();
    }
    @SubscribeEvent
    public void PlayerLogIn(PlayerEvent.PlayerLoggedInEvent event) throws UnableInitiateEngineException {
        if(!MappetVoice.AudioManager.initiate()){
            throw new UnableInitiateEngineException();
        }
    }
    @SubscribeEvent
    public void OnEngineRegister(OnEngineRegistry event){
        event.registry(AudioEngineLoader.defaultEngine, new DefaultAudioSystemManager());
    }

}
