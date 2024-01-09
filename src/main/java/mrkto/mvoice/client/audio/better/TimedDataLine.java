package mrkto.mvoice.client.audio.better;

import javax.sound.sampled.Control;
import javax.sound.sampled.FloatControl;
import javax.sound.sampled.SourceDataLine;

public class TimedDataLine {
    SourceDataLine line;

    long lastAccessed;
    public TimedDataLine(SourceDataLine line){
        this.line = line;
        lastAccessed = System.currentTimeMillis();
    }

    public SourceDataLine getLine() {
        lastAccessed = System.currentTimeMillis();
        return line;
    }

    public long getLastAccessed() {
        return lastAccessed;
    }
    public Control getControl(Control.Type floatControl){
        return line.getControl(floatControl);
    }

    public void close(){
        line.flush();
        line.stop();
        line.close();
    }
}
