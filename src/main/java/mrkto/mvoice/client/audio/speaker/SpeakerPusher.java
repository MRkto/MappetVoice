package mrkto.mvoice.client.audio.speaker;

import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.client.audio.interfaces.IAudioOutput;
import mrkto.mvoice.client.audio.interfaces.IAudioSystemManager;
import mrkto.mvoice.utils.other.Sounds;
import net.labymod.opus.OpusCodec;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec2f;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public class SpeakerPusher {
    private final SpeakerBuffer buffer;
    private final OpusCodec decoder;
    private final Future<?> future;
    //based on modular voice chat
    public SpeakerPusher(ExecutorService executor, String chanel, IAudioSystemManager AudioManager)
    {
        this.buffer = new SpeakerBuffer(10);
        this.decoder = OpusCodec.newBuilder().withBitrate(68000).build();
        this.future = executor.submit(() ->
        {
            while (!Thread.currentThread().isInterrupted())
            {
                if (AudioManager.getOutput().isAvailable(chanel))
                {
                    SpeakerBuffer.AudioEntry entry = buffer.getNextPacket();
                    if(entry.isEnd())
                    {
                        AudioManager.getOutput().stopSound(chanel);
                    }
                    else
                    {
                        BlockPos listener = Minecraft.getMinecraft().player.getPosition();
                        Vec2f rot = Minecraft.getMinecraft().player.getPitchYaw();
                        BlockPos source = entry.getSource();
                        AudioManager.processSound(entry.getPacket(), source, listener.getX(), listener.getY(), listener.getZ(), rot.x, rot.y, entry.getDistance(), chanel);

                    }
                }
            }
        });
    }

    public void decodePush(byte[] opusPacket, BlockPos pos, double distance, float balance)
    {
        push(decoder.decodeFrame(opusPacket), pos, distance, balance);
    }

    private void push(byte[] packet, BlockPos pos, double distance, float balance)
    {
        if(balance != 0f){
            byte[] arr = Sounds.getNoise().createArray(0, packet.length);
            packet = AudioUtils.mergeSounds(packet, arr, balance);
        }
        buffer.pushPacket(packet, pos, distance);
    }

    public void end()
    {
        buffer.pushEnd();
    }


    public void close()
    {
        future.cancel(true);
        decoder.destroy();
    }
}
