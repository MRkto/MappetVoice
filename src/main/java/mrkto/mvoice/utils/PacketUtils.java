package mrkto.mvoice.utils;

import mrkto.mvoice.api.Events.OnPlayerSpeaking;
import mrkto.mvoice.api.Voice.data.PLayersData;
import mrkto.mvoice.audio.speaker.speakerWriter;
import mrkto.mvoice.network.client.CancelSpeakPacketC;
import mrkto.mvoice.network.client.SoundPacketClientC;
import mrkto.mvoice.network.common.*;
import mrkto.mvoice.network.server.EventPacketServerS;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.network.server.RadioChangePacketS;
import mrkto.mvoice.network.server.SoundPacketServerS;
import io.netty.buffer.ByteBuf;
import mrkto.mvoice.utils.other.Sounds;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.EventBus;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.tools.nsc.doc.model.diagram.ObjectNode;

import java.util.List;

public class PacketUtils {
    public static void register(){
        int id = 0;
        MappetVoice.NETWORK.registerMessage(EventPacketServerS.class, EventPacket.class, id++, Side.SERVER);
        MappetVoice.NETWORK.registerMessage(SoundPacketServerS.class, SoundPacket.class, id++, Side.SERVER);
        MappetVoice.NETWORK.registerMessage(SoundPacketClientC.class, SoundPacketPlayer.class, id++, Side.CLIENT);
        MappetVoice.NETWORK.registerMessage(RadioChangePacketS.class, RadioPacket.class, id++, Side.SERVER);
        MappetVoice.NETWORK.registerMessage(CancelSpeakPacketC.class, CancelSpeakPacket.class, id++, Side.CLIENT);
    }
    public static void sendSoundToServer(byte[] data, boolean isRadio){
        MappetVoice.NETWORK.sendToServer(new SoundPacket(data, isRadio));
    }
    public static void sendSoundToClient(byte[] data, BlockPos DataPos,  EntityPlayerMP player, String name, double distance){
        MappetVoice.NETWORK.sendTo(new SoundPacketPlayer(data, DataPos, name, distance), player);
    }

    public static void sendRadioSoundToClient(byte[] data, BlockPos DataPos,  EntityPlayerMP player, String name, double distance, float balance){
        MappetVoice.NETWORK.sendTo(new SoundPacketPlayer(data, DataPos, name, distance, balance), player);
    }
    public static void serverSoundProcessor(byte[] data, EntityPlayerMP player, boolean isRadio){
        if(PLayersData.get().getPlayerData(player).isMuted()){
            MappetVoice.NETWORK.sendTo(new CancelSpeakPacket(), player);
            player.sendStatusMessage(new TextComponentString(I18n.format("mvoice.send.muted")), true);
            return;
        }
        BlockPos position = player.getPosition();
        String name = player.getName();
        List<EntityPlayerMP> playerList = player.getServer().getPlayerList().getPlayers();
        if(isRadio){
            String wave = PLayersData.get().getPlayerData(player).getWave();
            for (EntityPlayerMP Listedplayer : playerList) {
                if (!Listedplayer.equals(player) && !PLayersData.get().getPlayerData(player).getLocalMutedList().contains(Listedplayer.getName()) && PLayersData.get().getPlayerData(Listedplayer).getWave().equals(wave)) {
                    float balance = MappetVoice.minNoise.get();
                    sendRadioSoundToClient(data, Listedplayer.getPosition(), Listedplayer, player.getName(), 999, balance);
                }
            }
            if(!MappetVoice.hearOther.get()){
                return;
            }
        }
        for (EntityPlayerMP Listedplayer : playerList) {
            if (player.getDistance(Listedplayer) < MappetVoice.range.get() && !Listedplayer.equals(player) && !PLayersData.get().getPlayerData(player).getLocalMutedList().contains(Listedplayer.getName()) && !MappetVoice.voice.getGroup(MappetVoice.voice.PlayerGroup(player)).hasPlayer(Listedplayer)) {
                sendSoundToClient(data, position, Listedplayer, name, MappetVoice.range.get());
            }
        }

        sendToGroup(data, player);
        Event(data, player, isRadio);


    }
    private static void sendToGroup(byte[] data, EntityPlayerMP player){
        if(!MappetVoice.voice.PlayerInGroup(player)){
            return;
        }
        List<EntityPlayerMP> playerList = player.getServer().getPlayerList().getPlayers();
        for (EntityPlayerMP Listedplayer : playerList) {
            if (!Listedplayer.equals(player) && PLayersData.get().getPlayerData(player).getLocalMutedList().contains(Listedplayer.getName()) && !MappetVoice.voice.getGroup(MappetVoice.voice.PlayerGroup(player)).hasPlayer(Listedplayer)) {
                BlockPos position = Listedplayer.getPosition();
                sendSoundToClient(data, position, Listedplayer, player.getName(), 999);
            }
        }
    }
    private static void Event(byte[] bytes, EntityPlayerMP player, boolean isRadio){
        byte[] finalBytes = AudioUtils.decode(bytes);
        new Thread(new Runnable() {
            @Override
            public void run() {
                float volume = AudioUtils.calcVolume(finalBytes);

                MinecraftForge.EVENT_BUS.post(new OnPlayerSpeaking(player, isRadio, volume, finalBytes));
            }
            }).start();
    }
    public static void clientSoundSender(byte[] data, boolean isRadio){


        byte[] encodedData = AudioUtils.encode(data);
        sendSoundToServer(encodedData, isRadio);

    }
    @SideOnly(Side.CLIENT)
    public static void clientSoundProcessor(byte[] data, BlockPos position, double distance, String name, float balance){
        byte[] decodedData = AudioUtils.decode(data);
        if(balance != 0f){
            byte[] arr = Sounds.getNoise().createArray(0, decodedData.length);
            decodedData = AudioUtils.mergeSounds(decodedData, arr, 0.8f);
        }
        EntityPlayerSP player = Minecraft.getMinecraft().player;
        speakerWriter.PlaySound(decodedData, position.getX(), position.getY(), position.getZ(), player.posX, player.posY, player.posZ, player.rotationPitch, player.rotationYaw, distance, name);
    }
    public static void writeString(ByteBuf buf, String string) {
        byte[] bytes = string.getBytes();
        buf.writeInt(bytes.length);
        buf.writeBytes(bytes);
    }

    public static String readString(ByteBuf buf) {
        int length = buf.readInt();
        byte[] bytes = new byte[length];
        buf.readBytes(bytes);
        return new String(bytes);
    }
}
