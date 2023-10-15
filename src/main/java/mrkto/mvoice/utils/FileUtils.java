package mrkto.mvoice.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.Voice.client.ClientData;
import mrkto.mvoice.api.Voice.data.PLayersData;
import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.audio.speaker.speakerWriter;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

public class FileUtils {
    public static Gson gson = new GsonBuilder()
                                  .setPrettyPrinting()
                                  .create();
    private static void createDefaultWorld(){
        if(!Files.exists(voiceFolder())) {
            voiceFolder().toFile().mkdir();
        }
        if(!Files.exists(getFilePath("PlayerData.json"))) {
            try {
                getFilePath("PlayerData.json").toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (getPlayersData() == null){
            setPlayersData(new PLayersData());
        }

    }
    private static void createDefaultConfig(){
        if(!Files.exists(getConfigFilePath("Settings.json"))) {
            try {
                getConfigFilePath("Settings.json").toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(FileUtils.getClientData() == null){
            microReader.setMixer("");
            speakerWriter.setMixer("");
            FileUtils.setClientData(new ClientData(microReader.getMixer(), speakerWriter.getMixer()));
        }

    }
    public static Path getFilePath(String fileName) {
        return Objects.requireNonNull(voiceFolder()).resolve(fileName);
    }
    public static Path getConfigFilePath(String fileName) {
        return Objects.requireNonNull(MappetVoice.config.toPath()).resolve("mvoice").resolve(fileName);
    }

    private static Path voiceFolder() {
        return Objects.requireNonNull(DimensionManager.getCurrentSaveRootDirectory()).toPath().resolve("mappet").resolve("voice");
    }
    public static PLayersData getPlayersData() {
        if(!Files.exists(getFilePath("PlayerData.json"))) {
            createDefaultWorld();
        }
        try (BufferedReader reader = Files.newBufferedReader(getFilePath("PlayerData.json"))) {
            PLayersData data = gson.fromJson(reader, PLayersData.class);
            if(data == null){
                setPlayersData(new PLayersData());
                data = gson.fromJson(reader, PLayersData.class);
            }
            return data;
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
        
    }
    public static void setPlayersData(PLayersData data) {
        try (BufferedWriter writer = Files.newBufferedWriter(getFilePath("PlayerData.json"))) {
             gson.toJson(data, writer);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SideOnly(Side.CLIENT)
    public static ClientData getClientData(){
        if(!Files.exists(getConfigFilePath("Settings.json"))) {
            createDefaultWorld();
        }
        try (BufferedReader reader = Files.newBufferedReader(getConfigFilePath("Settings.json"))) {
            return gson.fromJson(reader, ClientData.class);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    @SideOnly(Side.CLIENT)
    public static void setClientData(ClientData data){
        try (BufferedWriter writer = Files.newBufferedWriter(getConfigFilePath("Settings.json"))) {
            gson.toJson(data, writer);
        }catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
