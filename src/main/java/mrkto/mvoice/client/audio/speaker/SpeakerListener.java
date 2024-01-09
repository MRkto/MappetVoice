package mrkto.mvoice.client.audio.speaker;

import mrkto.mvoice.MappetVoice;
import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpeakerListener {
    private final ExecutorService executor = Executors.newCachedThreadPool();
    public static SpeakerListener instance = new SpeakerListener();

    private final Map<String, SpeakerPusher> bufferMap;
    //based on modular voice chat
    public SpeakerListener()
    {
        bufferMap = new HashMap<>();
    }

    public void accept(String chanel, byte[] opusPacket, BlockPos pos, double distance, float balance)
    {
        if (MappetVoice.AudioManager.getOutput().isAvailable(chanel))
        {
            if(opusPacket == null)
            {
                bufferMap.computeIfAbsent(chanel, $ -> new SpeakerPusher(executor, chanel ,MappetVoice.AudioManager)).end();
            }
            else
            {
                bufferMap.computeIfAbsent(chanel, $ -> new SpeakerPusher(executor, chanel ,MappetVoice.AudioManager)).decodePush(opusPacket, pos, distance, balance);
            }
        }
    }

    public void close()
    {
        bufferMap.values().forEach(SpeakerPusher::close);
        bufferMap.clear();
        executor.shutdown();
    }
}
