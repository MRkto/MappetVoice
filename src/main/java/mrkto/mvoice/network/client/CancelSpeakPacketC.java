package mrkto.mvoice.network.client;

import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.network.common.CancelSpeakPacket;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CancelSpeakPacketC implements IMessageHandler<CancelSpeakPacket, IMessage> {
    public IMessage onMessage(CancelSpeakPacket packet, MessageContext ctx) {
        microReader.stopRecording();
        return null; // В ответ ничего не отправляем
    }
}
