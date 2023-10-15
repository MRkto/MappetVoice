package mrkto.mvoice.network.common;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class CancelSpeakPacket implements IMessage {

    private boolean isMuted;

    public CancelSpeakPacket() {
        isMuted = false;
    }
    public CancelSpeakPacket(boolean isMuted) {
        this.isMuted = isMuted;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        isMuted = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(isMuted);
    }

}
