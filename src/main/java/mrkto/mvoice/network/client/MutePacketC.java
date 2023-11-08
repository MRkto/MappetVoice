package mrkto.mvoice.network.client;

import mrkto.mvoice.network.common.MutePacket;
import mrkto.mvoice.utils.other.KeyHandler;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MutePacketC implements IMessageHandler<MutePacket, IMessage> {
    public IMessage onMessage(MutePacket packet, MessageContext ctx) {
        if(packet.getChangeType() == 0 || packet.getChangeType() == 1)
            KeyHandler.canSpeak = packet.canSpeak();
        if(packet.getChangeType() == 0 || packet.getChangeType() == 2)
            KeyHandler.canSpeakRadio = packet.canSpeakRadio();
        return null; // В ответ ничего не отправляем
    }
}