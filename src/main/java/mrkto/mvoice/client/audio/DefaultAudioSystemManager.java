package mrkto.mvoice.client.audio;

import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.client.audio.interfaces.IAudioInput;
import mrkto.mvoice.client.audio.interfaces.IAudioOutput;
import mrkto.mvoice.client.audio.interfaces.IAudioSystemManager;
import mrkto.mvoice.utils.other.Sounds;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
public class DefaultAudioSystemManager implements IAudioSystemManager {
    protected DefaultRecorder input;
    protected DefaultSpeaker output;
    public DefaultAudioSystemManager(){
        this.input = new DefaultRecorder();
        this.output = new DefaultSpeaker();
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
    public void processSound(byte[] data, BlockPos position, double posX, double posY, double posZ, float rotationPitch, float rotationYaw, double distance, String name, float balance) {
        output.playSound(data, position.getX(), position.getY(), position.getZ(), posX, posY, posZ, rotationPitch, rotationYaw, distance, name);
    }

    @Override
    public boolean preInitiate() {
        return true;
    }

    @Override
    public boolean initiate() {
        try{
            output.createDefault();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public void terminate() {
        output.deleteAllChanels();
        input.stopRecording();
    }

    @Override
    public void fullTerminate() {
        input = null;
        output = null;
    }

    @Override
    public IAudioSystemManager getNewInstance() {
        return new DefaultAudioSystemManager();
    }


}
