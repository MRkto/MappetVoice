package mrkto.mvoice.client.audio.better;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.client.ClientData;
import mrkto.mvoice.client.audio.interfaces.IAudioOutput;

import javax.sound.sampled.*;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CoolSpeaker implements IAudioOutput {
    public final DataLine.Info SPEAKER_INFO = new DataLine.Info(SourceDataLine.class, AudioUtils.FORMATS);
    private final Map<String, TimedDataLine> lines = new ConcurrentHashMap<>();
    private Mixer mixer;
    public CoolSpeaker(){
        setMixer(ClientData.getInstance().getSpeakerName());
    }

    @Override
    public void playSound(byte[] data, double sourceX, double sourceY, double sourceZ, double listenerX, double listenerY, double listenerZ, double pitch, double yaw, double Maxdistance, String chanel) {
        if(isAvailable(chanel)){
            TimedDataLine line = lines.get(chanel);
            FloatControl control = (FloatControl) line.getControl(FloatControl.Type.BALANCE);
            control.setValue(getBalance(sourceX, sourceY, sourceZ, listenerX, listenerY, listenerZ, pitch, yaw));
            data = AudioUtils.adjustVolume(AudioUtils.monoToStereo(data), ((double) MappetVoice.volumes.get() / 100) * (ClientData.getInstance().getVolume(chanel) / 100) * getGain(sourceX, sourceY, sourceZ, listenerX, listenerY, listenerZ, Maxdistance));
            line.getLine().write(data, 0, data.length);
        }

    }
    public float getBalance(double sourceX, double sourceY, double sourceZ, double listenerX, double listenerY, double listenerZ, double pitch, double yaw){
        double directionX = sourceX - listenerX;
        double directionZ = sourceZ - listenerZ;

        double distance = Math.sqrt(Math.pow(directionX, 2) + Math.pow(directionZ, 2));
        double listenerLookAtSourceX = directionX / distance;
        double listenerLookAtSourceZ = directionZ / distance;

        double listenerLookAtX = Math.cos(yaw / 180 * Math.PI);
        double listenerLookAtZ = Math.sin(yaw / 180 * Math.PI);

        return -1 * (float) (listenerLookAtSourceX * listenerLookAtX + listenerLookAtSourceZ * listenerLookAtZ);
    }
    public float getGain(double sourceX, double sourceY, double sourceZ, double listenerX, double listenerY, double listenerZ, double maxDistance){
        double directionX = sourceX - listenerX;
        double directionY = sourceY - listenerY;
        double directionZ = sourceZ - listenerZ;

        double distance = Math.sqrt(Math.pow(directionX, 2) + Math.pow(directionY, 2) + Math.pow(directionZ, 2));

        return (float) Math.min(distance <= maxDistance ? 1.0 - (distance / maxDistance) : 0.0, 1.0);
    }
    @Override
    public boolean isAvailable(String chanel){
        TimedDataLine line = lines.get(chanel);
        if(line != null){
            return true;
        }else {
            try{
                final SourceDataLine nline = (SourceDataLine) mixer.getLine(SPEAKER_INFO);
                nline.open(AudioUtils.FORMATS, 1920*2*4);
                nline.start();
                final TimedDataLine tline = new TimedDataLine(nline);

                lines.put(chanel, tline);
                return true;
            }catch (Exception ignored){
            }
        }
        return false;
    }
    @Override
    public void stopSound(String chanel) {
        if(lines.containsKey(chanel)){
            lines.get(chanel).getLine().flush();
            lines.get(chanel).getLine().stop();
            lines.get(chanel).getLine().close();
        }
    }

    @Override
    public void stopAllSounds() {
        for (Map.Entry<String, TimedDataLine> entry : lines.entrySet()) {
            entry.getValue().getLine().flush();
            entry.getValue().getLine().stop();
            entry.getValue().getLine().close();
        }
    }

    @Override
    public ArrayList<String> getAudioDevices(){
        return AudioUtils.findAudioDevices(SPEAKER_INFO);
    };

    @Override
    public void setMixer(String name) {
        if (mixer != null) {
            mixer.close();
        }
        mixer = AudioUtils.findMixer(name, SPEAKER_INFO);
        stopAllSounds();
    }

    @Override
    public String getMixer() {
        return mixer.getMixerInfo().getName();
    }

    @Override
    public void update() {
        final long currentTime = System.currentTimeMillis();
        lines.forEach((id, lineInfo) ->
        {
            if (currentTime - lineInfo.getLastAccessed() > 2500)
            {
                lines.remove(id).close();
            }
        });
    }

}
