package mrkto.mvoice.network.packets;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.ClientMessageHandler;
import mchorse.mclib.network.ServerMessageHandler;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.utils.other.Sounds;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.play.server.SPacketCustomSound;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;

public class RadioPacket  implements IMessage {
    String wave;

    public RadioPacket() {
    }

    public RadioPacket(String wave) {
        this.wave = wave;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        wave = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeUTF8String(buf, wave);
    }

    public static class ServerHandler extends ServerMessageHandler<RadioPacket> {
        @Override
        public void run(EntityPlayerMP entityPlayerMP, RadioPacket packet) {
            ItemStack stack = entityPlayerMP.getHeldItemMainhand();
            NBTTagCompound tag = new NBTTagCompound();
            tag.setString("wave", packet.wave);
            stack.setTagCompound(tag);
            if(!MappetVoice.switchRadioSound.get())
                return;

            if(!MappetVoice.hearOther.get()) {
                entityPlayerMP.connection.sendPacket(new SPacketCustomSound("mvoice:RadioSwitch", SoundCategory.PLAYERS, entityPlayerMP.posX, entityPlayerMP.posY, entityPlayerMP.posZ, 1f, 1f));
                return;
            }

            for(EntityPlayerMP ListedPlayer : MappetVoice.server.getPlayerList().getPlayers())
                ListedPlayer.connection.sendPacket(new SPacketCustomSound("mvoice:RadioSwitch", SoundCategory.PLAYERS, entityPlayerMP.posX, entityPlayerMP.posY, entityPlayerMP.posZ, 0.1f, 1f));
        }
    }
}
