package mrkto.mvoice.client.audio.interfaces;

import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public interface IAudioOutput {
    void playSound(byte[] data, double sourceX, double sourceY, double sourceZ, double listenerX, double listenerY, double listenerZ, double pitch, double yaw, double Maxdistance, String chanel);
    void stopSound(String chanel);
    void stopAllSounds();
    ArrayList<String> getAudioDevices();
    void setMixer(String name);
    String getMixer();
    void update();
    boolean isAvailable(String chanel);
}
