package mrkto.mvoice.proxy;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.utils.other.OpusNotLoadedException;
import net.labymod.opus.OpusCodec;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import javax.annotation.Nullable;


public class ServerProxy{
    public void preInit(FMLPreInitializationEvent event) throws OpusNotLoadedException {


        MappetVoice.logger.info("server pre-init complete");

    }
    public void init(FMLInitializationEvent event)
    {
        MappetVoice.logger.info("server init complete");

    }
    public void postInit(FMLPostInitializationEvent event) {
        if(MappetVoice.opus.get()){
            AudioUtils.loadOpus();
            codec = OpusCodec.newBuilder().withChannels(1).build();
        }
        MappetVoice.logger.info("server post-init complete");
    }
    private static OpusCodec codec;
    public static byte[] decode(byte[] data){
        return codec.decodeFrame(data);
    }
    public static boolean isLoaded(){
        return (codec != null);
    }
}
