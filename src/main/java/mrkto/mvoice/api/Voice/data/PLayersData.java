package mrkto.mvoice.api.Voice.data;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.utils.FileUtils;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.UUID;

public class PLayersData {
    private final ArrayList<PlayerData> data = new ArrayList<>();
    public static PLayersData get(){
        return FileUtils.getPlayersData();
    }
    public void createNew(UUID uuid){
        data.add(new PlayerData(uuid));
    }
    public PlayerData getPlayerData(UUID uuid){
        ArrayList<PlayerData> data = this.data;
        for (PlayerData playerdata : data) {
            if (playerdata.getUUID().equals(uuid)) {
                return playerdata;
            }
        }
        this.data.add(new PlayerData(uuid));
        return new PlayerData(uuid);
    }
    public PlayerData getPlayerData(String name){
        EntityPlayerMP p = MappetVoice.server.getPlayerList().getPlayerByUsername(name);
        UUID uuid = null;
        if(p != null)
            uuid = p.getUniqueID();
        else
            return null;
        ArrayList<PlayerData> data = this.data;
        for (PlayerData playerdata : data) {
            if (playerdata.getUUID().equals(uuid)) {
                return playerdata;
            }
        }
        this.data.add(new PlayerData(uuid));
        return new PlayerData(uuid);
    }
    public PlayerData getPlayerData(EntityPlayerMP player){
        UUID uuid = player.getUniqueID();
        ArrayList<PlayerData> data = this.data;
        for (PlayerData playerdata : data) {
            if (playerdata.getUUID().equals(uuid)) {
                return playerdata;
            }
        }
        this.data.add(new PlayerData(uuid));
        return new PlayerData(uuid);
    }
    public void save(){
        FileUtils.setPlayersData(this);
    }
}

