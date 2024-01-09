package mrkto.mvoice.api.scripts.code;

import mchorse.mappet.api.scripts.code.entities.ScriptPlayer;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mrkto.mvoice.api.Voice.groups.Group;
import mrkto.mvoice.api.scripts.user.IVoice;
import mrkto.mvoice.MappetVoice;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;

public class Voice implements IVoice {
    @Override
    public Group getGroup(String name) {
        return MappetVoice.voice.getGroup(name);
    }

    @Override
    public LinkedList<Group> getGroups() {
        return MappetVoice.voice.getGroups();
    }
    public boolean createGroup(String name){
        return MappetVoice.voice.createGroup(name);
    }
    public boolean createGroupWithCustomData(String name, ScriptNBTCompound data){
        return MappetVoice.voice.createGroupWithCustomData(name, data.getNBTTagCompound());
    }
    public boolean createGroupWithCustomData(String name, NBTTagCompound data){
        return MappetVoice.voice.createGroupWithCustomData(name, data);
    }
    public boolean deleteGroup(String name){
        return MappetVoice.voice.deleteGroup(name);
    };
    public boolean PlayerInGroup(ScriptPlayer player){
        return MappetVoice.voice.playerInGroup(player.getMinecraftPlayer());
    };
    public String PlayerGroup(ScriptPlayer player){
        return MappetVoice.voice.playerGroup(player.getMinecraftPlayer());
    };


}
