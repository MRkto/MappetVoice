package mrkto.mvoice.utils;

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
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
    public static void sendSoundToAllInRadius(byte[] data, BlockPos DataPos, int x, int y, int z, int radius, byte world, String name, double distance){
        MappetVoice.NETWORK.sendToAllAround(new SoundPacketPlayer(data, DataPos, name, distance), new NetworkRegistry.TargetPoint(world, x, y, z, radius));
    }
    public static void serverSoundProcessor(byte[] data, EntityPlayerMP player, boolean isRadio){
        if(PLayersData.get().getPlayerData(player).isMuted()){
            MappetVoice.NETWORK.sendTo(new CancelSpeakPacket(), player);
            player.sendStatusMessage(new TextComponentString(I18n.format("mvoice.send.muted")), true);
        }else if(!isRadio){
            BlockPos position = player.getPosition();
            String name = player.getName();
            List<EntityPlayerMP> playerList = player.getServer().getPlayerList().getPlayers();
            for (EntityPlayerMP Listedplayer : playerList) {
                if (player.getDistance(Listedplayer) < MappetVoice.range.get().intValue() && !Listedplayer.equals(player) && !PLayersData.get().getPlayerData(player).getLocalMutedList().contains(Listedplayer.getName()) && !MappetVoice.voice.getGroup(MappetVoice.voice.PlayerGroup(player)).hasPlayer(Listedplayer)) {
                    sendSoundToClient(data, position, Listedplayer, name, MappetVoice.range.get().intValue());
                }
            }
            //sendSoundToAllInRadius(data, position, position.getX(), position.getY(), position.getZ(), MappetVoice.range.get().intValue(), (byte)player.getEntityWorld().getWorldType().getId(), name, MappetVoice.range.get().doubleValue());
        }

        if(!PLayersData.get().getPlayerData(player).isGroupMuted() && MappetVoice.voice.PlayerInGroup(player) && (!isRadio || MappetVoice.hearOther.get())){
            List<EntityPlayerMP> playerList = player.getServer().getPlayerList().getPlayers();
            for (EntityPlayerMP Listedplayer : playerList) {
                if (!Listedplayer.equals(player) && PLayersData.get().getPlayerData(player).getLocalMutedList().contains(Listedplayer.getName()) && !MappetVoice.voice.getGroup(MappetVoice.voice.PlayerGroup(player)).hasPlayer(Listedplayer)) {
                    BlockPos position = Listedplayer.getPosition();
                    String name = player.getName();
                    sendSoundToClient(data, position, Listedplayer, name, 999);
                }
            }
        }
        if(isRadio){
            List<EntityPlayerMP> playerList = player.getServer().getPlayerList().getPlayers();
            String wave = PLayersData.get().getPlayerData(player).getWave();
            for (EntityPlayerMP Listedplayer : playerList) {
                if (!Listedplayer.equals(player) && !PLayersData.get().getPlayerData(player).getLocalMutedList().contains(Listedplayer.getName()) && PLayersData.get().getPlayerData(Listedplayer).getWave().equals(wave)) {
                    float balance = 0.5f;
                    sendRadioSoundToClient(data, Listedplayer.getPosition(), Listedplayer, player.getName(), 999, balance);
                }
            }
        }
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
