package mrkto.mvoice.network.common;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class SoundPacket implements IMessage {
    private byte[] sound;
    private boolean isRadio;

    public SoundPacket() {}

    public SoundPacket(byte[] sound) {
        this.sound = sound;
    }
    public SoundPacket(byte[] sound, boolean isRadio) {
        this.sound = sound;
        this.isRadio = isRadio;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        int length = buf.readInt();
        sound = new byte[length];
        for (int i = 0; i < length; i++) {
            sound[i] = buf.readByte();
        }
        isRadio = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeInt(sound.length);
        for (int i = 0; i < sound.length; i++) {
            buf.writeByte(sound[i]);
        }
        buf.writeBoolean(isRadio);
    }

    public byte[] getSound() {
        return sound;
    }
    public boolean isRadio() {
        return isRadio;
    }
}
