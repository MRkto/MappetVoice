package mrkto.mvoice.capability;

import mrkto.mvoice.network.Dispatcher;
import mrkto.mvoice.network.packets.SoundPacketClient;
import mrkto.mvoice.network.packets.SyncCapabilityPacket;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.*;
import net.minecraftforge.fml.relauncher.Side;

import java.util.ArrayList;

public class Profile implements IProfile{
    private EntityPlayer player;
    private boolean isMuted = false;
    private String wave = "";
    private final ArrayList<String> MutedList = new ArrayList<>();
    @Override
    public void setMuted(boolean value) {
        isMuted = value;
        if(Side.SERVER.isServer())
            sync();
    }
    @Override
    public void setWave(String value) {
        wave = value;
        if(Side.SERVER.isServer())
            sync();
    }
    @Override
    public String getWave() {
        return wave;
    }

    @Override
    public boolean getMuted() {
        return isMuted;
    }

    @Override
    public void addMuted(String name) {
        MutedList.add(name);
        if(Side.SERVER.isServer())
            sync();
    }

    @Override
    public ArrayList<String> getMutedList() {
        return MutedList;
    }

    @Override
    public void removeMuted(String name) {
        MutedList.remove(name);
        if(Side.SERVER.isServer())
            sync();
    }
    public static Profile get(EntityPlayer player)
    {
        IProfile profileCapability = player == null ? null : player.getCapability(ProfileProvider.MAIN, null);

        if (profileCapability instanceof Profile)
        {
            Profile profile = (Profile) profileCapability;
            profile.player = player;

            return profile;
        }
        return null;
    }
    private void sync()
    {
        Dispatcher.sendTo(new SyncCapabilityPacket(serializeNBT()), (EntityPlayerMP) player);
    }
    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound nbtTagCompound = new NBTTagCompound();
        nbtTagCompound.setBoolean("isMuted", this.isMuted);
        NBTTagList list = new NBTTagList();
        for(int i = 0; i < this.MutedList.size(); i++){
            list.set(i, new NBTTagString(this.MutedList.get(i)));
        }
        nbtTagCompound.setTag("List", list);
        return nbtTagCompound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound tag) {
        if (tag.hasKey("isMuted")) {
            this.isMuted = tag.getBoolean("isMuted");
        }
        if (tag.hasKey("List")) {
            for (NBTBase i : (NBTTagList) tag.getTag("List")) {
                this.MutedList.add(i.toString());
            }
        }
    }
}
