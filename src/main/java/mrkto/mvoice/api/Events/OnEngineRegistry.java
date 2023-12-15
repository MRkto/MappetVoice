package mrkto.mvoice.api.Events;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.audio.AudioEngineLoader;
import mrkto.mvoice.client.audio.interfaces.IAudioSystemManager;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnEngineRegistry extends Event {
    public OnEngineRegistry() {

    }
    public void registry(String EngineName, IAudioSystemManager AudioSystemManager){
        AudioEngineLoader.registeredEngines.put(EngineName, AudioSystemManager);
        MappetVoice.logger.info("registered " + EngineName + " sound engine");
    }
}
