package mrkto.mvoice.api.Voice.groups;

import mchorse.mappet.api.scripts.code.entities.ScriptPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;

public class nullGroup extends Group{

    public nullGroup(){
        super("");

    }

    public String getName(){
        return "";
    }
    public void setName(String name){

    }
    public NBTTagCompound getData(){
        return new NBTTagCompound();
    }
    public void setData(NBTTagCompound customData){

    }
    public ScriptPlayer[] getPlayerList(){
        return new ScriptPlayer[1];
    }
    public EntityPlayerMP[] getMinecraftPlayerList(){
        return new EntityPlayerMP[1];
    }
    public boolean hasPlayer(EntityPlayerMP player){
        return false;
    }
    public boolean isEmpty(){
        return true;
    }
    public void join(EntityPlayerMP player){

    }
    public void leave(EntityPlayerMP player){

    }
    public boolean isNullGroup(){
        return true;
    }
}
