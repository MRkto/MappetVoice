package mrkto.mvoice.api.Voice.client;

import java.util.HashMap;


public class ClientData {
    public String microName;
    public String speakerName;
    public HashMap<String, Double> data;
    public ClientData(String microName, String speakerName){
        this.microName = microName;
        this.speakerName = speakerName;
        this.data = new HashMap<String, Double>();
    }
}
