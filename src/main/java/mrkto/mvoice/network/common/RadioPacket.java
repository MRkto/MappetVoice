package mrkto.mvoice.network.common;

import io.netty.buffer.ByteBuf;
import mrkto.mvoice.utils.PacketUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RadioPacket  implements IMessage {
    private String data;

    public RadioPacket() {}

    public RadioPacket(String data) {
        this.data = data;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        data = PacketUtils.readString(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        PacketUtils.writeString(buf, data);
    }


    public String getData() {
        return data;
    }
}