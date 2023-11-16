package mrkto.mvoice.client.audio;

import mrkto.mvoice.network.common.EventPacket;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.utils.PacketUtils;

import javax.sound.sampled.*;

import static java.lang.Thread.sleep;
import static mrkto.mvoice.client.audio.speakerWriter.SPEAKER_INFO;


public class microReader {
    public static final DataLine.Info MICRO_INFO = new DataLine.Info(TargetDataLine.class, AudioUtils.FORMATM);
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
    //для ламы
    public static void main(String[] args) throws LineUnavailableException {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        Mixer omixer = null;
        // Перебираем аудиоустройства и ищем микрофоны
        for (Mixer.Info info : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(MICRO_INFO)) {
                omixer = mixer;
                break;
            }
        }



        microphone = (TargetDataLine) omixer.getLine(MICRO_INFO); // получаем data line у миксера
        microphone.open(AudioUtils.FORMATM); //FORMATS нужен для только для "реалистичного звука"
        microphone.start(); // старутем? типо хз зачем насамом деле

        byte[] data = new byte[1920000]; // ~20 секунд

        microphone.read(data, 0, data.length);


        Mixer omixerSpeaker = null;
        // Перебираем аудиоустройства и ищем микрофоны
        for (Mixer.Info info : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(SPEAKER_INFO)) {
                omixerSpeaker = mixer;
                break;
            }
        }

        SourceDataLine speaker = (SourceDataLine) omixerSpeaker.getLine(SPEAKER_INFO);
        speaker.open(AudioUtils.FORMATM);
        speaker.start();
        speaker.write(data, 0, data.length);

    }
}
