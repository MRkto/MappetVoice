package mrkto.mvoice.client.audio.speaker;

import net.minecraft.util.math.BlockPos;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpeakerBuffer {
    private final BlockingQueue<AudioEntry> queue;

    SpeakerBuffer(int size)
    {
        this.queue = new LinkedBlockingQueue<>(size);
    }
    //based on modular voice chat
    public AudioEntry getNextPacket()
    {
        try
        {
            return queue.take();
        } catch (InterruptedException ex)
        {
            throw new AssertionError();
        }
    }

    public void pushPacket(byte[] packet, BlockPos pos, double distance)
    {
        AudioEntry entry = new AudioEntry(packet, pos, distance);
        if (!queue.offer(entry))
        {
            queue.poll();
            queue.offer(entry);
        }
    }

    public void pushEnd()
    {
        AudioEntry entry= new AudioEntry(true);
        if(!queue.offer(entry))
        {
            queue.poll();
            queue.offer(entry);
        }
    }

    static class AudioEntry
    {
        private byte[] packet;
        private BlockPos pos;
        private double distance;


        private boolean end;

        AudioEntry(byte[] packet, BlockPos pos, double distance)
        {
            this.packet = packet;
            this.pos = pos;
            this.distance = distance;
        }

        AudioEntry(boolean end)
        {
            this.end = end;
            this.packet = null;
            this.pos = null;
        }

        public boolean isEnd()
        {
            return end;
        }


        byte[] getPacket()
        {
            return packet;
        }
        BlockPos getSource()
        {
            return pos;
        }
        double getDistance()
        {
            return distance;
        }

    }
}
