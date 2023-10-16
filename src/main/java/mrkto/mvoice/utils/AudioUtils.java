package mrkto.mvoice.utils;

import javax.sound.sampled.*;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.audio.speaker.speakerWriter;
import net.labymod.opus.*;
import org.apache.commons.lang3.ArrayUtils;



public class AudioUtils {
    public static final AudioFormat FORMATM = new AudioFormat(44100, 16, 1, true, false);
    public static final AudioFormat FORMATS = new AudioFormat(44100, 16, 2, true, false);
    public static OpusCodec codec = OpusCodec.newBuilder().withChannels(1).build();


    public static Mixer findMixer(String name, Line.Info lineinfo) {
        Mixer.Info[] mixerInfos = AudioSystem.getMixerInfo();
        Mixer omixer = null;
        // Перебираем аудиоустройства и ищем микрофоны
        for (Mixer.Info info : mixerInfos) {
            Mixer mixer = AudioSystem.getMixer(info);
            if (mixer.isLineSupported(lineinfo)) {
                if (info.getName().equals(name)) {
                    return mixer;
                }
                if (omixer == null) {
                    omixer = mixer;
                }
            }
        }
        return omixer;
    }

    public static ArrayList<String> findAudioDevices(Line.Info lineInfo) {
        final ArrayList<String> list = new ArrayList<>();
        for (Mixer.Info mixerInfo : AudioSystem.getMixerInfo()) {
            final Mixer mixer = AudioSystem.getMixer(mixerInfo);
            if (mixer.isLineSupported(lineInfo)) {
                list.add(mixerInfo.getName());
            }
        }
        return list;
    }

    public static boolean hasLinesOpen(Mixer mixer) {
        return mixer.getSourceLines().length != 0 || mixer.getTargetLines().length != 0;
    }

    public static byte[] encode(byte[] data) {
        return codec.encodeFrame(data);
    }

    public static byte[] decode(byte[] data) {
        return codec.decodeFrame(data);
    }

    public static boolean loadOpus() {
        try {
            loadNatives();
            return true;
        } catch (Throwable e) {
            e.printStackTrace();
            return false;
        }
    }


    private static void loadNatives() throws Throwable {
        final String tmpDir = System.getProperty("java.io.tmpdir");
        File tempLibDir = null;
        int index = 0;
        do {
            tempLibDir = new File(tmpDir, "mappetvoiceopus" + index);
            System.out.println(tempLibDir);
            ++index;
        } while (tempLibDir.exists() && !deleteDir(tempLibDir));
        tempLibDir.mkdir();
        OpusCodec.extractNatives(tempLibDir);
        OpusCodec.loadNative(tempLibDir);
    }

    private static boolean deleteDir(final File file) {
        final File[] contents = file.listFiles();
        if (contents != null) {
            File[] array;
            for (int length = (array = contents).length, i = 0; i < length; ++i) {
                final File f = array[i];
                deleteDir(f);
            }
        }
        return file.delete();
    }

    public static void setSpeaker(String name) {
        speakerWriter.setMixer(name);
        PlayerUtils.saveSpeaker(name);
    }

    public static void setMicro(String name) {
        microReader.setMixer(name);
        PlayerUtils.saveMicro(name);
    }

    public static void setConfig() {
        speakerWriter.setMixer(PlayerUtils.getSpeaker());
        microReader.setMixer(PlayerUtils.getMicro());
    }

    public static byte[] getCountBytes(byte[] data, int count, int offset) {
        int countC = count + offset;
        byte[] dataNeed = data;
        while (dataNeed.length < countC) {
            dataNeed = ArrayUtils.addAll(dataNeed, data);
        }

        InputStream is = new ByteArrayInputStream(dataNeed);


        byte[] data2 = new byte[count];
        try {
            is.skip(offset);
            is.read(data2);
        } catch (IOException e) {
            e.printStackTrace();
            return new byte[count];
        }
        return data2;
    }

    public static byte[] mergeSounds(byte[] sound1, byte[] sound2, float volumeBalance) {
        int length = Math.min(sound1.length, sound2.length);
        byte[] result = new byte[length];

        for (int i = 0; i < length; i++) {
            float sample1 = sound1[i] / 128.0f;
            float sample2 = sound2[i] / 128.0f;

            float mixed = sample1 * volumeBalance + sample2 * (1.0f - volumeBalance);


            mixed = Math.max(-1.0f, Math.min(1.0f, mixed));


            result[i] = (byte) (mixed * 128.0f);
        }

        return result;
    }
    private static float lastPeak = 0f;
    public static float calcVolume(byte[] buf) {

        float[] samples = new float[buf.length / 2];

        int b = buf.length;

        for (int i = 0, s = 0; i < b; ) {
            int sample = 0;

            sample |= buf[i++] & 0xFF;
            sample |= buf[i++] << 8;

            samples[s++] = sample / 32768f;
        }

        float peak = 0f;
        for (float sample : samples) {

            float abs = Math.abs(sample);
            if (abs > peak) {
                peak = abs;
            }

        }
        if (lastPeak > peak) {
                peak = lastPeak * 0.875f;
        }

        lastPeak = peak;
        return lastPeak;
    }

}
