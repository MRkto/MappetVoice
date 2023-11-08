package mrkto.mvoice.api.Voice.data;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.utils.FileUtils;
import net.minecraft.entity.player.EntityPlayerMP;

import java.util.ArrayList;
import java.util.UUID;

public class PlayersData {
    private final ArrayList<PlayerData> data = new ArrayList<>();
    public static PlayersData get(){
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
        PlayerData PlayerData = new PlayerData(uuid);
        this.data.add(PlayerData);
        return PlayerData;
    }
    public PlayerData getPlayerData(String name){
        EntityPlayerMP uuid = MappetVoice.server.getPlayerList().getPlayerByUsername(name);
        if(uuid != null)
            return getPlayerData(uuid);
        else
            return new PlayerData(new UUID(123, 321));
    }
    public PlayerData getPlayerData(EntityPlayerMP player){
        UUID uuid = player.getUniqueID();
        return getPlayerData(uuid);
    }
    public void save(){
        FileUtils.setPlayersData(this);
    }
}

