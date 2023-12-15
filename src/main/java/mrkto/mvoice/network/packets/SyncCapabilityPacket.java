package mrkto.mvoice.network.packets;

import io.netty.buffer.ByteBuf;
import mchorse.mclib.network.ClientMessageHandler;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.capability.Profile;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.utils.other.Sounds;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class SyncCapabilityPacket implements IMessage {
    private NBTTagCompound compound;
    public SyncCapabilityPacket() {
    }
    public SyncCapabilityPacket(NBTTagCompound compound) {
        this.compound = compound;

    }
    @Override
    public void fromBytes(ByteBuf buf) {
        compound = ByteBufUtils.readTag(buf);
    }

    @Override
    public void toBytes(ByteBuf buf) {
        ByteBufUtils.writeTag(buf, compound);
    }
    public static class ClientHandler extends ClientMessageHandler<SyncCapabilityPacket> {
        @SideOnly(Side.CLIENT)
        @Override
        public void run(EntityPlayerSP entityPlayerSP, SyncCapabilityPacket packet) {
            Profile.get(entityPlayerSP).deserializeNBT(packet.compound);
        }
    }
}
