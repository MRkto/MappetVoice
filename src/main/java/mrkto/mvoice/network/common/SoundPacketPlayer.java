package mrkto.mvoice.network.common;

import mrkto.mvoice.utils.PacketUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;



public class SoundPacketPlayer implements IMessage {
    private byte[] sound;
    private BlockPos pos;
    private String name;
    private double distance;
    private float balance;

    public SoundPacketPlayer() {}
    public SoundPacketPlayer(byte[] sound, BlockPos position, String name, double distance) {
        this.sound = sound;
        this.pos = position;
        this.name = name;
        this.distance = distance;
        this.balance = 0f;
    }
    public SoundPacketPlayer(byte[] sound, BlockPos position, String name, double distance, float balance) {
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
        name = PacketUtils.readString(buf);
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
        PacketUtils.writeString(buf, name);
        buf.writeDouble(distance);
        buf.writeFloat(balance);
    }

    public byte[] getSound() {
        return sound;
    }
    public BlockPos getSourcePosition(){return pos;}
    public String getName(){return name;}
    public double getDistance(){return distance;}
    public float getBalance(){return balance;}
}
