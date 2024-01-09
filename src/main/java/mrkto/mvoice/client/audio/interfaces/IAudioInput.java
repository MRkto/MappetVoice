package mrkto.mvoice.client.audio.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.sound.sampled.DataLine;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public interface IAudioInput {
    void startRecording(boolean isRadio);
    void stopRecording();
    void setMixer(String name);
    String getMixer();
    ArrayList<String> getAudioDevices();
    boolean isRecording();
    void setRadio(boolean value);
}
