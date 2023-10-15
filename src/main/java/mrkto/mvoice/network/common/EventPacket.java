package mrkto.mvoice.network.common;

import mrkto.mvoice.utils.PacketUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import io.netty.buffer.ByteBuf;

public class EventPacket implements IMessage {
    private String event;
    private byte info;

    public EventPacket() {}

    public EventPacket(String eventName) {
        this.event = eventName;
        this.info = 0;
    }
    public EventPacket(String eventName, byte info) {
        this.event = eventName;
        this.info = info;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        event = PacketUtils.readString(buf);
        info = buf.readByte();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketUtils.writeString(buf, event);
        buf.writeByte(info);
    }


    public String getEvent() {
        return event;
    }
    public byte getInfo() {
        return info;
    }
}
