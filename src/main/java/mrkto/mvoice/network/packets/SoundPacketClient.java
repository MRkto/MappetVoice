package mrkto.mvoice.network.packets;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.ClientMessageHandler;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;

import mrkto.mvoice.utils.PacketUtils;
import mrkto.mvoice.utils.other.Sounds;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SoundPacketClient implements IMessage {
    private byte[] sound;
    private BlockPos pos;
    private String name;
    private double distance;
    private float balance;
    public SoundPacketClient() {
    }
    public SoundPacketClient(byte[] sound, BlockPos position, String name, double distance, float balance) {
        this.sound = sound;
        this.pos = position;
        this.name = name;
        this.distance = distance;
        this.balance = balance;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        sound = new byte[length];
        for (int i = 0; i < length; i++) {
            sound[i] = buf.readByte();
        }
        double x = buf.readDouble();
        double y = buf.readDouble();
        double z = buf.readDouble();
        pos = new BlockPos(x, y, z);
        name = ByteBufUtils.readUTF8String(buf);
        distance = buf.readDouble();
        balance = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sound.length);
        for (byte b : sound) {
            buf.writeByte(b);
        }
        buf.writeDouble(pos.getX());
        buf.writeDouble(pos.getY());
        buf.writeDouble(pos.getZ());
        ByteBufUtils.writeUTF8String(buf, name);
        buf.writeDouble(distance);
        buf.writeFloat(balance);
    }
    public static class ClientHandler extends ClientMessageHandler<SoundPacketClient> {
        @SideOnly(Side.CLIENT)
        @Override
        public void run(EntityPlayerSP entityPlayerSP, SoundPacketClient packet) {
            byte[] decodedData = AudioUtils.decode(packet.sound);
            if(packet.balance != 0f){
                packet.name += "radio";
                byte[] arr = Sounds.getNoise().createArray(0, decodedData.length);
                decodedData = AudioUtils.mergeSounds(decodedData, arr, 0.8f);
            }
            MappetVoice.AudioManager.processSound(decodedData, packet.pos, entityPlayerSP.posX, entityPlayerSP.posY, entityPlayerSP.posZ, entityPlayerSP.rotationPitch, entityPlayerSP.rotationYawHead, packet.distance, packet.name, packet.balance);
        }
    }
}
