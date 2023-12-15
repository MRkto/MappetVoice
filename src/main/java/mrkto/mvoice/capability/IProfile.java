package mrkto.mvoice.capability;

import net.minecraft.nbt.NBTTagCompound;

import java.util.ArrayList;

public interface IProfile {
    void setMuted(boolean value);
    boolean getMuted();
    String getWave();
    void addMuted(String name);
    ArrayList<String> getMutedList();
    void removeMuted(String name);
    void setWave(String value);
    public NBTTagCompound serializeNBT();
    public void deserializeNBT(NBTTagCompound tag);
}
