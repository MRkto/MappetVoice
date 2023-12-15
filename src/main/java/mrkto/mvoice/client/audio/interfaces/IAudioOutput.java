package mrkto.mvoice.client.audio.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.sound.sampled.DataLine;
@SideOnly(Side.CLIENT)
public interface IAudioOutput {
    void playSound(byte[] data, double sourceX, double sourceY, double sourceZ, double listenerX, double listenerY, double listenerZ, double pitch, double yaw, double Maxdistance, String chanel);
    void stopSound(String chanel);
    DataLine.Info getFromat();
    void setMixer(String name);
    String getMixer();
    void update();
}
