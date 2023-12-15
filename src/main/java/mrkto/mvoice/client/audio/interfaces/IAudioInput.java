package mrkto.mvoice.client.audio.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.sound.sampled.DataLine;
@SideOnly(Side.CLIENT)
public interface IAudioInput {
    void startRecording(boolean isRadio);
    void stopRecording();
    void setMixer(String name);
    String getMixer();
    DataLine.Info getFormat();
    boolean isRecording();
    void setRadio(boolean value);
}
