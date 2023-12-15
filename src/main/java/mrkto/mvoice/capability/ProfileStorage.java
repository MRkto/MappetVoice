package mrkto.mvoice.capability;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

public class ProfileStorage implements Capability.IStorage<IProfile> {
    @Override
    public NBTBase writeNBT(Capability<IProfile> capability, IProfile instance, EnumFacing side) {
        return instance.serializeNBT();
    }

    @Override
    public void readNBT(Capability<IProfile> capability, IProfile instance, EnumFacing side, NBTBase nbt) {
        if (nbt instanceof NBTTagCompound)
        {
            instance.deserializeNBT((NBTTagCompound) nbt);
        }
    }
}
