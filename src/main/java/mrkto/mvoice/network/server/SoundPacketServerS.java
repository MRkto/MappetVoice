package mrkto.mvoice.network.server;

import mrkto.mvoice.network.common.SoundPacket;
import mrkto.mvoice.utils.PacketUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.network.simpleimpl.*;



public class SoundPacketServerS implements IMessageHandler<SoundPacket, IMessage>{
    @Override
    public IMessage onMessage(SoundPacket packet, MessageContext ctx) {
        byte[] data = packet.getSound();
        // Получаем игрока, который прислал нам пакет.
        EntityPlayerMP player = ctx.getServerHandler().player;
        // Отправляем сообщение игроку
        PacketUtils.serverSoundProcessor(data, player, packet.isRadio());


        return null; // В ответ ничего не отправляем
    }


}
