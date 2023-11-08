package mrkto.mvoice.audio.speaker;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.utils.AudioUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;

import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class speakerWriter {
    public static final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, AudioUtils.FORMATS);
    private static Mixer mixer;
    private static int volume;
    private static Map<String, SourceDataLine> chanels = new HashMap<String, SourceDataLine>() {{
        try {
            put("main", (SourceDataLine) AudioUtils.findMixer("", SPEAKER_INFO).getLine(SPEAKER_INFO));
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }};
    private static ArrayList delList = new ArrayList<String>();

    public static void playSound(byte[] data, float leftVolume, float rightVolume, String chanel) {
        try {
            AudioFormat format = AudioUtils.FORMATS;
            DataLine.Info info = SPEAKER_INFO;
            if (mixer == null) {
                setMixer("");
            }
            if (chanels.get(chanel) == null) {
                chanels.put(chanel, (SourceDataLine) mixer.getLine(info));
            }
            SourceDataLine DataLine = chanels.get(chanel);
            //monoToStereo(data, leftVolume  * ((float) MappetVoice.volumes.get() / 100)  * (PlayerUtils.getVolume(chanel) / 100), rightVolume * ((float) MappetVoice.volumes.get() / 100) * (PlayerUtils.getVolume(chanel) / 100));
            byte[] stereoData = monoToStereo(data, 1, 1);
            DataLine.open(format);
            DataLine.start();
            DataLine.write(stereoData, 0, stereoData.length);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            MappetVoice.logger.error("DataLine == null (do not report unless the message is repeated frequently)");
        }
    }
    public static void PlaySound(byte[] data, double sourceX, double sourceY, double sourceZ, double listenerX, double listenerY, double listenerZ, double pitch, double yaw, double Maxdistance, String chanel){
        double directionX = sourceX - listenerX;
        double directionY = sourceY - listenerY;
        double directionZ = sourceZ - listenerZ;

        double distance = Math.sqrt(Math.pow(directionX, 2) + Math.pow(directionY, 2) + Math.pow(directionZ, 2));
        double listenerLookAtSourceX = directionX / distance;
        double listenerLookAtSourceY = directionY / distance;
        double listenerLookAtSourceZ = directionZ / distance;

        distance = Math.max(distance, 0.01);

        double listenerLookAtX = -Math.sin(yaw / 180 * Math.PI);
        double listenerLookAtZ = Math.cos(yaw / 180 * Math.PI);

        double listenerLookLeftX = listenerLookAtZ;
        double listenerLookLeftZ = -listenerLookAtX;
        double listenerLookRightX = -listenerLookAtZ;
        double listenerLookRightZ = listenerLookAtX;

        double gain = Math.min(distance <= Maxdistance ? 1.0 - (distance / Maxdistance) : 0.0, 1.0);

        double dotProduct1 = listenerLookAtSourceX * listenerLookLeftX + listenerLookAtSourceY * 0 + listenerLookAtSourceZ * listenerLookLeftZ;
        double dotProduct2 = listenerLookAtSourceX * listenerLookRightX + listenerLookAtSourceY * 0 + listenerLookAtSourceZ * listenerLookRightZ;
        double leftVolume = Math.min((dotProduct1 + 1) / 2 + 0.3, 1.0) * gain;
        double rightVolume = Math.min((dotProduct2 + 1) / 2 + 0.3, 1.0) * gain;

        playSound(data, (float)leftVolume * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER), (float)rightVolume * Minecraft.getMinecraft().gameSettings.getSoundLevel(SoundCategory.MASTER), chanel);
    }

    public static void stopSound(String chanel) {
        if (chanels.get(chanel) == null) {
            chanels.get(chanel).flush();
            chanels.get(chanel).stop();
            chanels.get(chanel).close();

        }

    }
    public static DataLine.Info getFromat(){
        return SPEAKER_INFO;
    }
    public static void stopAllSound() {
        for (Map.Entry<String, SourceDataLine> entry : chanels.entrySet()) {
            entry.getValue().flush();
            entry.getValue().stop();
            entry.getValue().close();
        }
    }

    public static void deleteAllChanels() {
        Map<String, String> toDel = new HashMap<>();
        for (Map.Entry<String, SourceDataLine> entry : chanels.entrySet()) {
            entry.getValue().flush();
            entry.getValue().stop();
            entry.getValue().close();
            toDel.put(entry.getKey(), entry.getKey());
        }
        for (Map.Entry<String, String> entry : toDel.entrySet()) {
            chanels.remove(entry.getKey());
        }
    }

    public static void deleteChanel(String chanel) {
        if (chanels.get(chanel) != null) {
            chanels.get(chanel).flush();
            chanels.get(chanel).stop();
            chanels.get(chanel).close();
            chanels.remove(chanel);
        }
    }

    public static void createDefault() {
        deleteAllChanels();
        try {
            chanels.put("main", (SourceDataLine) AudioUtils.findMixer("", SPEAKER_INFO).getLine(SPEAKER_INFO));
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }

    public static boolean isPlaying(String chanel) {
        if (chanels.get(chanel) != null) {
            return chanels.get(chanel).available() != chanels.get(chanel).getBufferSize();
        }
        return false;
    }

    public static void tickDel() {
        Map<String, String> toDel = new HashMap<String, String>();
        for (Map.Entry<String, SourceDataLine> entry : chanels.entrySet()) {
            if (!isPlaying(entry.getKey()) && entry.getKey() != "main") {
                if(delList.contains(entry.getKey())) {
                    toDel.put(entry.getKey(), entry.getKey());
                    delList.remove(entry.getKey());
                }else{
                    delList.add(entry.getKey());
                }
            }else if(delList.contains(entry.getKey())){
                delList.remove(entry.getKey());
            }
        }

        for (Map.Entry<String, String> entry : toDel.entrySet()) {
            deleteChanel(entry.getKey());
        }


    }

    public static void setMixer(String name) {
        if (mixer != null && mixer.getMixerInfo().getName() == name) {
            return;
        }
        if (mixer != null) {
            mixer.close();
        }
        mixer = AudioUtils.findMixer(name, SPEAKER_INFO);
        stopAllSound();
    }

    public static String getMixer(){
        if (mixer != null) {
            return (mixer.getMixerInfo().getName());
        }
        return null;
    }


    public static int getVolume() {
        return volume;
    }

    public static void setVolume(int setvolume) {
        volume = setvolume;
    }

    public static byte[] monoToStereo(byte[] data, double leftVolume, double rightVolume) {
        byte[] stereo = new byte[data.length * 2];
        byte[] datal = adjustVolume(data, leftVolume);
        byte[] datar = adjustVolume(data, rightVolume);
        for (int i = 0; i < data.length; i += 2) {
            // Преобразование пары байтов в int
            stereo[i*2] = datal[i];
            stereo[i*2 + 1] = datal[i+1];
            stereo[i*2 + 2] = datar[i];
            stereo[i*2 + 3] = datar[i+1];

        }
        return stereo;
    }

    public static byte[] adjustVolume(byte[] audioSamples, double volume) {
        byte[] array = new byte[audioSamples.length];
        for (int i = 0; i < array.length; i += 2) {
            // convert byte pair to int
            short buf1 = audioSamples[i + 1];
            short buf2 = audioSamples[i];
            buf1 = (short) ((buf1 & 0xff) << 8);
            buf2 = (short) (buf2 & 0xff);
            short res = (short) (buf1 | buf2);
            res = (short) (res * volume);
            // convert back
            array[i] = (byte) res;
            array[i + 1] = (byte) (res  >> 8);

        }
        return array;
    }
}
