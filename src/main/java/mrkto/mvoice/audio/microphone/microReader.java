package mrkto.mvoice.audio.microphone;

import mrkto.mvoice.audio.speaker.speakerWriter;
import mrkto.mvoice.network.common.EventPacket;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.utils.AudioUtils;
import mrkto.mvoice.utils.PacketUtils;

import javax.sound.sampled.*;

import java.util.Random;

import static java.lang.Thread.sleep;


public class microReader {
    private static final DataLine.Info MICRO_INFO = new DataLine.Info(TargetDataLine.class, AudioUtils.FORMATM);
    private static boolean isRecording = false;
    public static boolean isMuted = false;
    private static TargetDataLine microphone;
    private static Mixer mixer;


    public static void startRecording(boolean isRadio) {
        if(isRecording)
            return;
        try {
            AudioFormat format = AudioUtils.FORMATM;
            DataLine.Info info = MICRO_INFO;
            if(mixer == null){
                setMixer("");
            }
            isRecording = true;
            if(microphone != null && microphone.isOpen())
                if(isRecording) {
                    startRecording(isRadio);
                    System.out.println(1);
                    return;
                }else{return;}

            microphone = (TargetDataLine) mixer.getLine(info);

            microphone.open(format);
            MappetVoice.NETWORK.sendToServer(new EventPacket("StartSpeak", (byte)(isRadio ? 2 : 1)));

            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        byte[] data = new byte[1920];
                        microphone.start();

                        while (isRecording && isAvalible()) {
                            microphone.read(data, 0, data.length);
                            byte[] dataVolumed = speakerWriter.adjustVolume(data, (double) MappetVoice.volumem.get() / 100);
                            PacketUtils.clientSoundSender(dataVolumed, isRadio);
                        }
                        data = null;
                        MappetVoice.NETWORK.sendToServer(new EventPacket("StopSpeak"));
                    } catch (Exception e) {
                        e.printStackTrace();
                        MappetVoice.NETWORK.sendToServer(new EventPacket("StopSpeak"));
                        stopRecording();
                    }
                }
            }).start();
        } catch (LineUnavailableException e) {
            MappetVoice.logger.error("Error in microReader if after this message minecraft crashed or freeze this is bug error message: " + e.getMessage());
            stopRecording();
            setMixer(getMixer());
        }
    }

    public static void stopRecording() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                isRecording = false;
                microphone.stop();
                microphone.close();
            }
        }).start();

    }
    public static void setMixer(String name){
        if(mixer != null){
            mixer.close();
        }
        mixer = AudioUtils.findMixer(name, MICRO_INFO);
        if(microphone != null) {
            stopRecording();
        }
    }
    public static String getMixer(){
        if(mixer != null){
            return mixer.getMixerInfo().getName();
        }
        return null;
    }
    public static DataLine.Info getFromat(){
        return MICRO_INFO;
    }
    public static boolean isAvalible(){
        return microphone.isOpen();
    }
    public static boolean IsRecording(){return isRecording;}

    public static void main(String[] args) throws InterruptedException {
        AudioUtils.loadOpus();
        startRecording(false);
        sleep(10000);
        stopRecording();
    }
}
