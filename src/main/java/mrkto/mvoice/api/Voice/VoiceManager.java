package mrkto.mvoice.api.Voice;

import mrkto.mvoice.api.Voice.groups.Group;
import mrkto.mvoice.api.Voice.groups.nullGroup;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

import java.util.LinkedList;

public class VoiceManager {
    private final LinkedList<Group> groups = new LinkedList<>();

    public LinkedList<Group> getGroups(){
        return this.groups;
    }
    public Group getGroup(String name){
        for(Group group : getGroups()){
            if(group.getName().equals(name)){
                return group;
            }
        }
        return new nullGroup();
    }
    public boolean createGroup(String name){
        if(getGroup(name).getName().equals(new nullGroup().getName()) && !name.equals("")) {
            groups.add(new Group(name));
            return true;
        }
        return false;
    }
    public boolean createGroupWithCustomData(String name, NBTTagCompound data){
        if(getGroup(name).getName().equals(new nullGroup().getName()) && !name.equals("")) {
            groups.add(new Group(name, data));
            return true;
        }
        return false;
    }
    public boolean deleteGroup(String name){
        for(Group group : groups){
            if(group.getName().equals(name)){
                groups.remove(group);
                return true;
            }
        }
        return false;
    }
    public boolean playerInGroup(EntityPlayerMP player){
        for(Group group : getGroups()){
            if(group.hasPlayer(player)){
                return true;
            }
        }
        return false;
    }
    public String playerGroup(EntityPlayerMP player){
        for(Group group : getGroups()){
            if(group.hasPlayer(player)){
                return group.getName();
            }
        }
        return "";
    }
}

