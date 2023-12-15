package mrkto.mvoice.network.packets;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.ServerMessageHandler;
import mrkto.mvoice.utils.PacketUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class SoundPacket implements IMessage {
    byte[] sound;
    boolean isRadio;
    float volume;

    public SoundPacket() {
    }

    public SoundPacket(byte[] sound, boolean isRadio, float volume) {
        this.sound = sound;
        this.isRadio = isRadio;
        this.volume = volume;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        sound = new byte[length];
        for (int i = 0; i < length; i++) {
            sound[i] = buf.readByte();
        }
        isRadio = buf.readBoolean();
        volume = buf.readFloat();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sound.length);
        for (byte b : sound) {
            buf.writeByte(b);
        }
        buf.writeBoolean(isRadio);
        buf.writeFloat(volume);
    }
    public static class ServerHandler extends ServerMessageHandler<SoundPacket> {
        @Override
        public void run(EntityPlayerMP entityPlayerMP, SoundPacket packet) {
            PacketUtils.serverSoundProcessor(packet.sound, entityPlayerMP, packet.isRadio, packet.volume);
        }
    }
}
