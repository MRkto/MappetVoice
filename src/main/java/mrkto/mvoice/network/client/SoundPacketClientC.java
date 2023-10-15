package mrkto.mvoice.network.client;

import mrkto.mvoice.network.common.SoundPacketPlayer;
import mrkto.mvoice.utils.PacketUtils;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class SoundPacketClientC implements IMessageHandler<SoundPacketPlayer, IMessage> {
    public IMessage onMessage(SoundPacketPlayer packet, MessageContext ctx) {
        byte[] sound = packet.getSound(); // получаем закодированый звук
        BlockPos position = packet.getSourcePosition(); // позиция того кто говорил
        String name = packet.getName();
        float balance = packet.getBalance();
        double distance = packet.getDistance();
        PacketUtils.clientSoundProcessor(sound, position, distance, name, balance); // отправляем звук на обработку
        return null; // В ответ ничего не отправляем
    }
}
