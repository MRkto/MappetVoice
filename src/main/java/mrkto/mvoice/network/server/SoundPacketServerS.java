package mrkto.mvoice.network.server;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.network.common.SoundPacket;
import mrkto.mvoice.utils.PacketUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.*;



public class SoundPacketServerS implements IMessageHandler<SoundPacket, IMessage>{
    @Override
    public IMessage onMessage(SoundPacket packet, MessageContext ctx) {
        byte[] data = packet.getSound();
        // Получаем игрока, который прислал нам пакет.
        EntityPlayerMP player = ctx.getServerHandler().player;
        // Отправляем сообщение игроку
//        for(EntityPlayerMP players : MappetVoice.server.getPlayerList().getPlayers())
//            PacketUtils.sendSoundToClient(data, player.getPosition(), players, player.getName(), 15);
        PacketUtils.serverSoundProcessor(data, player, packet.isRadio());


        return null; // В ответ ничего не отправляем
    }


}
