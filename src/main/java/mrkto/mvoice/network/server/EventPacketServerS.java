package mrkto.mvoice.network.server;

import mrkto.mvoice.network.common.EventPacket;
import mrkto.mvoice.api.Events.OnPlayerStartSpeak;
import mrkto.mvoice.api.Events.OnPlayerStopSpeak;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.network.simpleimpl.*;

import java.util.HashMap;
import java.util.Map;


public class EventPacketServerS implements IMessageHandler<EventPacket, IMessage>{
    @Override
    public IMessage onMessage(EventPacket packet, MessageContext ctx) {
        String event = packet.getEvent();
        // Получаем игрока, который прислал нам пакет.
        EntityPlayerMP player = ctx.getServerHandler().player;
        byte info = packet.getInfo();
        Map<String, Event> dictionary = new HashMap<>();
        dictionary.put("StartSpeak", new OnPlayerStartSpeak(player, info));
        dictionary.put("StopSpeak", new OnPlayerStopSpeak(player));
        Event value = dictionary.get(event);
        if(value != null){
            MinecraftForge.EVENT_BUS.post(value);
        }
        return null; // В ответ ничего не отправляем
    }


}
