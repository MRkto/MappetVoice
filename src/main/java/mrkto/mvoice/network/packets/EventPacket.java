package mrkto.mvoice.network.packets;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.ServerMessageHandler;
import mrkto.mvoice.api.Events.OnPlayerStartSpeak;
import mrkto.mvoice.api.Events.OnPlayerStopSpeak;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

import java.util.HashMap;
import java.util.Map;

public class EventPacket  implements IMessage {
    private String event;
    private boolean isRadio;
    public EventPacket() {
    }

    public EventPacket(String event, boolean isRadio) {
        this.event = event;
        this.isRadio = isRadio;
    }
    @Override
    public void fromBytes(ByteBuf buf) {
        event = ByteBufUtils.readUTF8String(buf);
        isRadio = buf.readBoolean();
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, event);
        buf.writeBoolean(isRadio);
    }
    public static class ServerHandler extends ServerMessageHandler<EventPacket> {
        @Override
        public void run(EntityPlayerMP entityPlayerMP, EventPacket packet) {
            Map<String, Event> eventMap = new HashMap<>();
            eventMap.put("StartSpeak", new OnPlayerStartSpeak(entityPlayerMP, (byte) (packet.isRadio ? 2 : 1)));
            eventMap.put("StopSpeak", new OnPlayerStopSpeak(entityPlayerMP));
            Event value = eventMap.get(packet.event);
            if(value != null){
                MinecraftForge.EVENT_BUS.post(value);
            }
        }
    }
}