package mrkto.mvoice.api.Voice.data;

import java.util.ArrayList;
import java.util.UUID;

public class PlayerData {
    private UUID uuid = new UUID(1, 1);
    private boolean isMuted = false;
    private boolean isGroupMuted = false;
    private ArrayList<String> localMutedList = new ArrayList<>();
    private String wave = "";
    public PlayerData(UUID uuid, boolean muted, boolean isGroupMuted, ArrayList<String> localMutedList){
        this.uuid = uuid;
        this.isMuted = muted;
        this.isGroupMuted = isGroupMuted;
        this.localMutedList = localMutedList;

    }
    public PlayerData(UUID uuid, boolean muted, boolean isGroupMuted, ArrayList<String> localMutedList, String wave){
        this.uuid = uuid;
        this.isMuted = muted;
        this.isGroupMuted = isGroupMuted;
        this.localMutedList = localMutedList;
        this.wave = wave;
    }
    public PlayerData(){

    }
    public PlayerData(UUID uuid){
        this.uuid = uuid;
    }

    public UUID getUUID() {
        return this.uuid;
    }
    public String getWave(){
        return this.wave;
    }
    public void setWave(String wave){
        this.wave = wave;
    }

    public boolean isMuted() {
        return isMuted;
    }
    public void setMuted(boolean value) {
        isMuted = value;
    }
    public ArrayList<String> getLocalMutedList() {
        return localMutedList;
    }
}
