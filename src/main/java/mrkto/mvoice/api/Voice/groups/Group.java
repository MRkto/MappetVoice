package mrkto.mvoice.api.Voice.groups;

import mchorse.mappet.api.scripts.code.entities.ScriptPlayer;
import mchorse.mappet.api.scripts.code.nbt.ScriptNBTCompound;
import mrkto.mvoice.MappetVoice;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import java.util.ArrayList;
import java.util.UUID;

public class Group{
    private String name;
    private ArrayList<UUID> PlayerList;
    private NBTTagCompound customData;
    private boolean isGlobal;
    private boolean isMuted;
    public Group(String name){
        this.name = name;
        this.customData = new NBTTagCompound();
        this.PlayerList = new ArrayList<>();
        this.isGlobal = true;
        this.isMuted = true;
    }
    public Group(String name, NBTTagCompound customData){
        this.name = name;
        this.PlayerList = new ArrayList<>();
        this.customData = customData;
        this.isGlobal = true;
        this.isMuted = true;
    }

    public String getName(){
        return this.name;
    }
    public void setName(String name){
        this.name = name;
    }
    public NBTTagCompound getData(){
        return this.customData;
    }
    public void setData(NBTTagCompound customData){
        this.customData = customData;
    }
    public void setData(ScriptNBTCompound customData){
        this.customData = customData.getNBTTagCompound();
    }
    public EntityPlayerMP[] getMinecraftPlayerList(){
        EntityPlayerMP[] list = new EntityPlayerMP[this.PlayerList.size()];
        for(int i = 0; i < this.PlayerList.size(); i++){
            list[i] = MappetVoice.server.getPlayerList().getPlayerByUUID(this.PlayerList.get(i));
        }
        return list;
    }
    public ScriptPlayer[] getPlayerList(){
        ScriptPlayer[] list = new ScriptPlayer[this.PlayerList.size()];
        for(int i = 0; i < this.PlayerList.size(); i++){
            list[i] = new ScriptPlayer(MappetVoice.server.getPlayerList().getPlayerByUUID(this.PlayerList.get(i)));
        }
        return list;

    }
    public boolean hasPlayer(EntityPlayerMP player){
        return PlayerList.contains(player.getUniqueID());
    }
    public boolean hasPlayer(ScriptPlayer player){
        return PlayerList.contains(player.getMinecraftPlayer().getUniqueID());
    }
    public boolean isEmpty(){
        return PlayerList.isEmpty();
    }
    public void setGlobal(boolean is){
        this.isGlobal = is;
    }
    public boolean isGlobal(){
        return this.isGlobal;
    }
    public void setMuted(boolean is){
        this.isMuted = is;
    }
    public boolean isMuted(){
        return this.isMuted;
    }
    public void join(EntityPlayerMP player){
        this.PlayerList.add(player.getUniqueID());
    }
    public void leave(EntityPlayerMP player){
        this.PlayerList.remove(player.getUniqueID());
    }
    public void join(ScriptPlayer player){
        this.PlayerList.add(player.getMinecraftPlayer().getUniqueID());
    }
    public void leave(ScriptPlayer player){
        this.PlayerList.remove(player.getMinecraftPlayer().getUniqueID());
    }
    public boolean isNullGroup(){
        return false;
    }
}
