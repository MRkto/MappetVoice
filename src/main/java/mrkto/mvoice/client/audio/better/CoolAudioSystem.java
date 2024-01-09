package mrkto.mvoice.client.audio.better;

import mrkto.mvoice.client.audio.DefaultRecorder;
import mrkto.mvoice.client.audio.interfaces.IAudioInput;
import mrkto.mvoice.client.audio.interfaces.IAudioOutput;
import mrkto.mvoice.client.audio.interfaces.IAudioSystemManager;
import net.minecraft.util.math.BlockPos;
public class CoolAudioSystem implements IAudioSystemManager {
    protected DefaultRecorder input;

    protected CoolSpeaker output;
    public CoolAudioSystem(){
        this.input = new DefaultRecorder();
        this.output = new CoolSpeaker();
    }

    @Override
    public IAudioInput getInput() {
        return input;
    }

    @Override
    public IAudioOutput getOutput() {
        return output;
    }

    @Override
    public void processSound(byte[] data, BlockPos position, double posX, double posY, double posZ, float rotationPitch, float rotationYaw, double distance, String name) {
        output.playSound(data, position.getX(), position.getY(), position.getZ(), posX, posY, posZ, rotationPitch, rotationYaw, distance, name);
    }

    @Override
    public boolean preInitiate() {
        return true;
    }

    @Override
    public boolean initiate() {
        return true;
    }

    @Override
    public void terminate() {
        input.stopRecording();
        output.stopAllSounds();
    }

    @Override
    public void fullTerminate() {
        input = null;

    }

    @Override
    public IAudioSystemManager getNewInstance() {
        return new CoolAudioSystem();
    }


}
