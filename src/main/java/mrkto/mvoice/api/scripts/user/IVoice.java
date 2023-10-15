package mrkto.mvoice.api.scripts.user;

import mchorse.mappet.api.scripts.code.entities.ScriptPlayer;
import mrkto.mvoice.api.Voice.groups.Group;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;

public interface IVoice {
    public LinkedList<Group> getGroups();
    public Group getGroup(String name);
    public boolean createGroup(String name);
    public boolean createGroupWithCustomData(String name, NBTTagCompound data);
    public boolean deleteGroup(String name);
    public boolean PlayerInGroup(ScriptPlayer player);
    public String PlayerGroup(ScriptPlayer player);


}

