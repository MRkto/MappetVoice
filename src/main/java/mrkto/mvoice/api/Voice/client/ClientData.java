package mrkto.mvoice.api.Voice.client;

import mrkto.mvoice.utils.FileUtils;

import java.util.HashMap;


public class ClientData {
    private String microName;
    private String speakerName;
    private HashMap<String, Double> data;
    public ClientData(String microName, String speakerName){
        this.microName = microName;
        this.speakerName = speakerName;
        this.data = new HashMap<String, Double>();
    }
    public static ClientData get(){
        return FileUtils.getClientData();
    }
    public void save(){
        FileUtils.setClientData(this);
    }

    public String getMicroName() {
        return microName;
    }

    public String getSpeakerName() {
        return speakerName;
    }
    public HashMap<String, Double> getData() {
        return data;
    }
    public void setMicroName(String microName) {
        this.microName = microName;
    }

    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }
    public void setData(HashMap<String, Double> data) {
        this.data = data;
    }
}
