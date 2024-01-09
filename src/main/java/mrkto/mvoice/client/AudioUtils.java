package mrkto.mvoice.client;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.function.Consumer;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.audio.DefaultRecorder;
import mrkto.mvoice.client.audio.DefaultSpeaker;
import mrkto.mvoice.utils.PlayerUtils;
import net.labymod.opus.OpusCodec;
import org.apache.commons.lang3.ArrayUtils;



public class AudioUtils {
    public static final AudioFormat FORMATM = new AudioFormat(48000, 16, 1, true, false);
    public static final AudioFormat FORMATS = new AudioFormat(48000, 16, 2, true, false);
    private static final OpusCodec codec = OpusCodec.newBuilder().withBitrate(96000).build();
    public static Mixer findMixer(String name, Line.Info lineinfo){
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        Mixer omixer = null;
        // Перебираем аудиоустройства и ищем микрофоны
        for (Mixer.Info info : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(lineinfo)) {
                if(info.getName().equals(name)){
                    return mixer;
                }
                if(omixer == null){
                    omixer = mixer;
                }
            }
        }
        return omixer;
    }
    public static ArrayList<String> findAudioDevices(Line.Info lineInfo)
    {
        final ArrayList<String> list = new ArrayList<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo())
        {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(lineInfo))
            {
                list.add(mixerInfo.getName());
            }
        }
        return list;
    }
    public static  byte[] decode(byte[] data){
        return codec.decodeFrame(data);
    }
    public static  byte[] encode(byte[] data){
        return codec.encodeFrame(data);
    }
    public static boolean loadOpus()
    {
        try
        {
            OpusCodec.setupWithTemporaryFolder();
            OpusCodec testCodec = OpusCodec.createDefault();
            testCodec.decodeFrame(new byte[123]);
            return true;
        } catch (Throwable e)
        {
            e.printStackTrace();
            return false;
        }
    }
    public static byte[] mergeSounds(byte[] sound1, byte[] sound2, float volumeBalance){
        int length = Math.min(sound1.length, sound2.length);
        byte[] result = new byte[length];

        for (int i = 0; i < length; i++) {
            float sample1 = sound1[i] / 128.0f;
            float sample2 = sound2[i] / 128.0f;

            float mixed = sample1 * volumeBalance + sample2 * (1.0f - volumeBalance);

            mixed = Math.max(-1.0f, Math.min(1.0f, mixed));

            result[i] = (byte)(mixed * 128.0f);
        }

        return result;
    }
    private static float lastPeak = 0f;
    public static float calcVolume(byte[] buf) {
        int len = buf.length / 2;
        float[] samples = new float[len];

        for (int i = 0, s = 0; i < buf.length; ) {
            int sample = 0;

            sample |= buf[i++] & 0xFF;
            sample |= buf[i++] << 8;

            samples[s++] = sample / 32768f;
        }

        float peak = 0f;
        for (float sample : samples) {
            peak = Math.max(peak, Math.abs(sample));
        }

        if (lastPeak > peak) {
            peak = lastPeak * 0.875f;
        }

        lastPeak = peak;
        return lastPeak;
    }
    public static byte[] monoToStereo(byte[] data, double leftVolume, double rightVolume) {
        byte[] stereo = new byte[data.length * 2];
        byte[] datal = adjustVolume(data, leftVolume);
        byte[] datar = adjustVolume(data, rightVolume);
        for (int i = 0; i < data.length; i += 2) {

            stereo[i*2] = datal[i];
            stereo[i*2 + 1] = datal[i+1];
            stereo[i*2 + 2] = datar[i];
            stereo[i*2 + 3] = datar[i+1];

        }
        return stereo;
    }
    public static byte[] monoToStereo(byte[] data) {
        byte[] stereo = new byte[data.length * 2];

        for (int i = 0; i < data.length; i += 2) {

            stereo[i*2] = data[i];
            stereo[i*2 + 1] = data[i+1];
            stereo[i*2 + 2] = data[i];
            stereo[i*2 + 3] = data[i+1];

        }
        return stereo;
    }
    public static byte[] adjustVolume(byte[] audioSamples, double volume) {
        byte[] array = new byte[audioSamples.length];
        for (int i = 0; i < array.length; i += 2) {
            short buf1 = audioSamples[i + 1];
            short buf2 = audioSamples[i];
            buf1 = (short) ((buf1 & 0xff) << 8);
            buf2 = (short) (buf2 & 0xff);
            short res = (short) (buf1 | buf2);
            res = (short) (res * volume);
            array[i] = (byte) res;
            array[i + 1] = (byte) (res  >> 8);

        }
        return array;
    }
}
