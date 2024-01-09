package mrkto.mvoice.client.audio;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.client.ClientData;
import mrkto.mvoice.client.audio.interfaces.IAudioOutput;
import net.minecraft.client.Minecraft;
import net.minecraft.util.SoundCategory;

import javax.sound.sampled.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.sleep;

public class DefaultSpeaker implements IAudioOutput {
    public final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, AudioUtils.FORMATS);
    private Mixer mixer;
    private int volume;
    private Map<String, SourceDataLine> chanels = new HashMap<String, SourceDataLine>() {{
        try {
            put("main", (SourceDataLine) AudioUtils.findMixer("", SPEAKER_INFO).getLine(SPEAKER_INFO));
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }
    }};
    private ArrayList delList = new ArrayList<String>();

    public void playSound(byte[] data, float leftVolume, float rightVolume, String chanel) {
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
            byte[] stereoData = AudioUtils.monoToStereo(data, leftVolume  * ((float) MappetVoice.volumes.get() / 100)  * (ClientData.getInstance().getVolume(chanel) / 100), rightVolume * ((float) MappetVoice.volumes.get() / 100) * (ClientData.getInstance().getVolume(chanel) / 100));
            DataLine.open(format, 1920 * 2 * 4);
            DataLine.start();
            DataLine.write(stereoData, 0, stereoData.length);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        } catch (NullPointerException e){
            e.printStackTrace();
            MappetVoice.logger.error("DataLine == null (do not report unless the message is repeated frequently)");
        }
    }
    public void playSound(byte[] data, double sourceX, double sourceY, double sourceZ, double listenerX, double listenerY, double listenerZ, double pitch, double yaw, double Maxdistance, String chanel){
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

    public void stopSound(String chanel) {
        if (chanels.get(chanel) == null) {
            chanels.get(chanel).flush();
            chanels.get(chanel).stop();
            chanels.get(chanel).close();

        }

    }

    @Override
    public ArrayList<String> getAudioDevices() {
        return AudioUtils.findAudioDevices(SPEAKER_INFO);
    }

    @Override
    public void stopAllSounds() {
        for (Map.Entry<String, SourceDataLine> entry : chanels.entrySet()) {
            entry.getValue().flush();
            entry.getValue().stop();
            entry.getValue().close();
        }
    }

    public void deleteAllChanels() {
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

    public void deleteChanel(String chanel) {
        if (chanels.get(chanel) != null) {
            chanels.get(chanel).flush();
            chanels.get(chanel).stop();
            chanels.get(chanel).close();
            chanels.remove(chanel);
        }
    }

    public void createDefault() {
        deleteAllChanels();
        try {
            chanels.put("main", (SourceDataLine) AudioUtils.findMixer("", SPEAKER_INFO).getLine(SPEAKER_INFO));
        } catch (LineUnavailableException e) {
            throw new RuntimeException(e);
        }

    }

    public boolean isPlaying(String chanel) {
        if (chanels.get(chanel) != null) {
            return chanels.get(chanel).available() != chanels.get(chanel).getBufferSize();
        }
        return false;
    }

    public void update() {
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

    @Override
    public boolean isAvailable(String chanel) {
        return true;
    }

    public void setMixer(String name) {
        if (mixer != null && mixer.getMixerInfo().getName() == name) {
            return;
        }
        if (mixer != null) {
            mixer.close();
        }
        mixer = AudioUtils.findMixer(name, SPEAKER_INFO);
        stopAllSounds();
    }

    public String getMixer(){
        if (mixer != null) {
            return (mixer.getMixerInfo().getName());
        }
        return null;
    }
}
