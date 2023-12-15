package mrkto.mvoice.client.audio;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.audio.interfaces.IAudioSystemManager;

import java.util.HashMap;
import java.util.Map;

public class AudioEngineLoader {
    public static final String defaultEngine = "kostil";
    public static final Map<String, IAudioSystemManager> registeredEngines = new HashMap<>();
    public static String selectedEngine = "kostil";

    public static void loadEngine(String engineName){
        if(MappetVoice.AudioManager != null){
            MappetVoice.AudioManager.terminate();
        }
        IAudioSystemManager audioSystemManager = registeredEngines.get(engineName);
        selectedEngine = engineName;
        if(audioSystemManager == null){
            audioSystemManager = registeredEngines.get(defaultEngine);
            selectedEngine = defaultEngine;
        }
        MappetVoice.AudioManager = audioSystemManager.getNewInstance();
        MappetVoice.AudioManager.preInitiate();
    }
    public static void reloadEngine(){
        if(MappetVoice.AudioManager == null){
            MappetVoice.logger.error("the engine cannot be restarted because it is not loaded");
        }
        loadEngine(selectedEngine);
        MappetVoice.AudioManager.initiate();
    }
    public static void stopEngineLoader(){

    }
}
