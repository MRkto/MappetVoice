package mrkto.mvoice.utils.other;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class InfinityByteArrayInputStream extends InputStream {

    private byte[] buf;
    private int pos;
    public InfinityByteArrayInputStream(byte[] buf){
        this.buf = buf;
        this.pos = 0;
    }
    public InfinityByteArrayInputStream(InputStream is) throws IOException {
        this.buf = IOUtils.toByteArray(is);
        this.pos = 0;

    }
    public InfinityByteArrayInputStream(byte[] buf, int off, int length){
        if(length > buf.length)
            throw new IndexOutOfBoundsException("buf len:" + buf.length + " set len:" + length);
        if(off > buf.length)
            throw new IndexOutOfBoundsException("buf len:" + buf.length + " set off:" + length);
        System.arraycopy(buf, 0, buf, 0, length);
        this.buf = buf;
        this.pos = off;
    }
    @Override
    public synchronized int read(){
        return (pos < buf.length) ? (buf[pos++] & 0xff) : buf[pos++ - buf.length];
    }
    @Override
    public synchronized int read(byte[] b){
        return read(b, 0, b.length);
    }
    @Override
    public synchronized int read(byte[] b, int off, int len){
        if(b == null)
            throw new NullPointerException();
        if(off < 0 || len < 0 || len > b.length - off)
            throw new IndexOutOfBoundsException("array len:" + b.length + " offset:" + off + " write len:" + len);

        if (len == 0) {
            return 0;
        }

        byte[] localBuf = buf.clone();
        while(len + pos > localBuf.length){
            localBuf = ArrayUtils.addAll(localBuf, buf);
        }

        System.arraycopy(localBuf, pos, b, off, len);
        pos += len;

        if(buf.length - pos <= 0){
            int num = (int) Math.floor((double) pos / (double)buf.length);
            int avail = buf.length * num - pos;
            this.pos = Math.abs(avail);
        }
        return len;
    }
    @Override
    public synchronized long skip(long n) {
        pos += n;
        if(buf.length - pos <= 0){
            int num = (int) Math.floor((double) pos / (double)buf.length);
            int avail = buf.length - pos;
            num = avail + buf.length * num;
            pos = Math.abs(num);
        }
        return n;
    }
    public synchronized void reset() {
        pos = 0;
    }
    public byte[] toArray(){
        return this.buf;
    }
    public byte[] createArray(int off, int len){
        byte[] arr = new byte[off + len];
        read(arr, off, len);
        return arr;
    }
}
