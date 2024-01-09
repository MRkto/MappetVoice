package mrkto.mvoice.client.audio;

import mrkto.mvoice.client.audio.interfaces.IAudioInput;
import mrkto.mvoice.network.Dispatcher;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;

import javax.sound.sampled.*;
import java.util.ArrayList;

public class DefaultRecorder implements IAudioInput {
    public final DataLine.Info MICRO_INFO = new DataLine.Info(TargetDataLine.class, AudioUtils.FORMATM);
    private boolean isRecording = false;
    private TargetDataLine microphone;
    private Mixer mixer;
    private boolean isRadio;

    public void startRecording(boolean isRadio) {
        if(isRecording)
            return;
        try {
            AudioFormat format = AudioUtils.FORMATM;
            if(mixer == null){
                setMixer("");
            }
            this.isRadio = isRadio;
            isRecording = true;
            if(microphone != null && microphone.isOpen())
                if(isRecording) {
                    startRecording(isRadio);
                    return;
                }else{return;}

            microphone = (TargetDataLine) mixer.getLine(MICRO_INFO);

            microphone.open(format);
            Dispatcher.postEventOnServer("StartSpeak", this.isRadio);

            new Thread(() -> {
                try {
                    byte[] data = new byte[1920];
                    microphone.start();

                    while (isRecording && isAvailable()) {
                        microphone.read(data, 0, data.length);
                        byte[] dataVolumed = AudioUtils.adjustVolume(data, (double) MappetVoice.volumem.get() / 100);
                        Dispatcher.sendSoundToServer(dataVolumed, this.isRadio);
                    }
                    data = null;
                    Dispatcher.postEventOnServer("StopSpeak");
                } catch (Exception e) {
                    e.printStackTrace();
                    Dispatcher.postEventOnServer("StopSpeak");
                    stopRecording();
                }
            }).start();
        } catch (LineUnavailableException e) {
            MappetVoice.logger.error("Error in microReader if after this message minecraft crashed or freeze this is bug error message: " + e.getMessage());
            stopRecording();
            setMixer(getMixer());
        }
    }

    public void stopRecording() {
        new Thread(() -> {
            isRecording = false;
            microphone.stop();
            microphone.close();
        }).start();
    }
    public void setRadio(boolean value){
        isRadio = value;
    }
    public void setMixer(String name){
        if(mixer != null){
            mixer.close();
        }
        mixer = AudioUtils.findMixer(name, MICRO_INFO);
        if(microphone != null) {
            stopRecording();
        }
    }
    public String getMixer(){
        if(mixer != null){
            return mixer.getMixerInfo().getName();
        }
        return null;
    }

    @Override
    public ArrayList<String> getAudioDevices() {
        return AudioUtils.findAudioDevices(MICRO_INFO);
    }

    public boolean isAvailable(){
        return microphone.isOpen();
    }
    public boolean isRecording(){return isRecording;}
}
