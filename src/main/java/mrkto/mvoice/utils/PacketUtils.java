package mrkto.mvoice.utils;

import mrkto.mvoice.api.Events.OnPlayerSpeaking;
import mrkto.mvoice.capability.Profile;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.network.Dispatcher;
import mrkto.mvoice.proxy.ServerProxy;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.MinecraftForge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PacketUtils {
    public static void serverSoundProcessor(byte[] data, EntityPlayerMP player, boolean isRadio, float volume){
        if(Profile.get(player).getMuted()){
            return;
        }
        BlockPos position = player.getPosition();
        String name = player.getName();
        List<EntityPlayerMP> playerList = player.getServer().getPlayerList().getPlayers();
        ArrayList<String> MutedList = Profile.get(player).getMutedList();
        if(isRadio){
            String wave = PlayerUtils.getWave(player);
            for (EntityPlayerMP Listedplayer : playerList) {
                if (!Listedplayer.equals(player) && !MutedList.contains(Listedplayer.getName()) && Objects.equals(PlayerUtils.getWave(Listedplayer), wave)) {
                    float balance = MappetVoice.minNoise.get();
                    Dispatcher.sendSoundTo(data, Listedplayer.getPosition(), Listedplayer, player.getName(), 999, balance);
                }
            }
            if(!MappetVoice.hearOther.get()){
                return;
            }
        }
        for (EntityPlayerMP Listedplayer : playerList) {
            if (player.getDistance(Listedplayer) < MappetVoice.range.get() && !Listedplayer.equals(player) && !MutedList.contains(Listedplayer.getName()) && !MappetVoice.voice.getGroup(MappetVoice.voice.PlayerGroup(player)).hasPlayer(Listedplayer)) {
                Dispatcher.sendSoundTo(data, position, Listedplayer, name, MappetVoice.range.get());
            }
        }

        sendToGroup(data, player);
        Event(data, player, isRadio, volume);
    }
    private static void sendToGroup(byte[] data, EntityPlayerMP player){
        if(!MappetVoice.voice.PlayerInGroup(player)){
            return;
        }
        List<EntityPlayerMP> playerList = player.getServer().getPlayerList().getPlayers();
        for (EntityPlayerMP Listedplayer : playerList) {
            if (!Listedplayer.equals(player) && Profile.get(player).getMutedList().contains(Listedplayer.getName()) && !MappetVoice.voice.getGroup(MappetVoice.voice.PlayerGroup(player)).hasPlayer(Listedplayer)) {
                BlockPos position = Listedplayer.getPosition();
                Dispatcher.sendSoundTo(data, position, Listedplayer, player.getName(), 999);
            }
        }
    }
    private static void Event(byte[] bytes, EntityPlayerMP player, boolean isRadio, float volumeClient){

            final byte[] data = ServerProxy.isLoaded() ? ServerProxy.decode(bytes) : new byte[1920];
            float volume = ServerProxy.isLoaded() ? AudioUtils.calcVolume(data) : volumeClient;

            MinecraftForge.EVENT_BUS.post(new OnPlayerSpeaking(player, isRadio, volume, data));
    }
}
