package mrkto.mvoice.network.server;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.network.common.RadioPacket;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class RadioChangePacketS implements IMessageHandler<RadioPacket, IMessage> {
    @Override
    public IMessage onMessage(RadioPacket packet, MessageContext ctx) {
        String data = packet.getData();
        EntityPlayerMP player = ctx.getServerHandler().player;
        ItemStack stack = player.getHeldItemMainhand();
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("wave", data);
        stack.setTagCompound(tag);
        if(!MappetVoice.switchRadioSound.get())
            return null;

        if(!MappetVoice.hearOther.get()) {
            player.connection.sendPacket(new SPacketCustomSound("mvoice:RadioSwitch", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 1f, 1f));
            return null;
        }

        for(EntityPlayerMP ListedPlayer : MappetVoice.server.getPlayerList().getPlayers())
            ListedPlayer.connection.sendPacket(new SPacketCustomSound("mvoice:RadioSwitch", SoundCategory.PLAYERS, player.posX, player.posY, player.posZ, 0.1f, 1f));

        return null;
    }



}
