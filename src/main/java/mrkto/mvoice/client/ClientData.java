package mrkto.mvoice.client;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mchorse.mappet.api.huds.HUDManager;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.audio.AudioEngineLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;


public class ClientData {
    private String microName;
    private String speakerName;
    private String engineName;
    private HashMap<String, Double> data;
    public ClientData(String microName, String speakerName){
        this.microName = microName;
        this.speakerName = speakerName;
        this.engineName = AudioEngineLoader.selectedEngine;
        this.data = new HashMap<>();
    }
    public static ClientData getInstance(){
        return getClientData();
    }
    public void save(){
        setClientData(this);
    }
    public String getMicroName() {
        return microName;
    }
    public String getSpeakerName() {
        return speakerName;
    }
    public String getEngineName() {
        return engineName;
    }
    public HashMap<String, Double> getData() {
        return data;
    }
    public double getVolume(String name){
        HashMap<String, Double> data = getData();
        if(data != null && data.containsKey(name))
            return data.get(name);
        return 100d;
    }
    public void setMicroName(String microName) {
        this.microName = microName;
    }
    public void setSpeakerName(String speakerName) {
        this.speakerName = speakerName;
    }
    public void setEngineName(String engineName) {
        this.engineName = engineName;
    }
    public void setData(HashMap<String, Double> data) {
        this.data = data;
    }
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();
    public static Path getConfigFilePath(String fileName) {
        MappetVoice.config.toPath().resolve("mvoice").toFile().mkdir();
        return MappetVoice.config.toPath().resolve("mvoice").resolve(fileName);
    }

    @SideOnly(Side.CLIENT)
    private static ClientData getClientData(){
        Path path = getConfigFilePath("settings.json");
        if(!Files.exists(path)) {
            try {
                Files.createFile(path);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        try (BufferedReader reader = Files.newBufferedReader(path)) {
            ClientData data = gson.fromJson(reader, ClientData.class);
            if(data == null) {
                data = new ClientData("", "");
                setClientData(data);
            }
            return data;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SideOnly(Side.CLIENT)
    private static void setClientData(ClientData data){
        try (BufferedWriter writer = Files.newBufferedWriter(getConfigFilePath("settings.json"))) {
            gson.toJson(data, writer);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
