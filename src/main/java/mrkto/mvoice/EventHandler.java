package mrkto.mvoice;

import mrkto.mvoice.api.Events.OnPlayerSpeaking;
import mrkto.mvoice.api.Events.OnPlayerStartSpeak;
import mrkto.mvoice.api.Events.OnPlayerStopSpeak;
import mrkto.mvoice.capability.Profile;
import mrkto.mvoice.capability.ProfileProvider;
import mrkto.mvoice.network.Dispatcher;
import mrkto.mvoice.network.packets.SyncCapabilityPacket;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;


public class EventHandler {
    private static int i = 0;
    public static Map<String, Byte> list = new HashMap<>();

    public static final ResourceLocation PROFILE = new ResourceLocation(MappetVoice.MOD_ID, "profile");
    @SubscribeEvent
    public void onPlayerLogIn(PlayerEvent.PlayerLoggedInEvent event){
        Dispatcher.sendTo(new SyncCapabilityPacket(Profile.get(event.player).serializeNBT()), (EntityPlayerMP) event.player);
    }
    @SubscribeEvent
    public void onStartSpeak(OnPlayerStartSpeak e){
        list.put(e.getPlayer().getName(), e.getType());
        if(e.getType() == 2){
            EntityPlayerMP player = e.getPlayer();
            if(!MappetVoice.onRadioSound.get()) return;

            player.connection.sendPacket(new SPacketCustomSound("mvoice:RadioOn", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 1f, 1f));

            if(!MappetVoice.hearOther.get()) return;

            for(EntityPlayerMP ListedPlayer : MappetVoice.server.getPlayerList().getPlayers()) {
                if(!ListedPlayer.equals(player)) {
                    ListedPlayer.connection.sendPacket(new SPacketCustomSound("mvoice:RadioOn", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 0.1f, 1f));
                }
            }
        }
    }
    @SubscribeEvent
    public void onStopSpeak(OnPlayerStopSpeak e) {
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
    @SubscribeEvent
    public void attachCapabilities(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof EntityPlayer){
            event.addCapability(PROFILE, new ProfileProvider());
        }
    }
    @SubscribeEvent
    public void onSpeaking(OnPlayerSpeaking e){

    }
}




