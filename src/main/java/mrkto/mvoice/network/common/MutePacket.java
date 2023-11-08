package mrkto.mvoice.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class MutePacket implements IMessage {
    private boolean canSpeak;
    private boolean canSpeakRadio;
    private int changeType;

    public MutePacket() {}

    public MutePacket(boolean canSpeak, boolean canSpeakRadio, int changeType) {
        this.canSpeak = canSpeak;
        this.canSpeakRadio = canSpeakRadio;
        this.changeType = changeType;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        canSpeakRadio = buf.readBoolean();
        canSpeak = buf.readBoolean();
        changeType = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(canSpeakRadio);
        buf.writeBoolean(canSpeak);
        buf.writeByte(changeType);
    }


    public boolean canSpeak() {
        return canSpeak;
    }
    public boolean canSpeakRadio() {
        return canSpeakRadio;
    }

    public int getChangeType() {
        return changeType;
    }
}
