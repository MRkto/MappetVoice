package mrkto.mvoice.proxy;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.utils.other.OpusNotLoadedException;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import javax.annotation.Nullable;


public class ServerProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent event) throws OpusNotLoadedException {


        MappetVoice.logger.info("server pre-init complete");
        super.preInit(event);
    }
    @Override
    public void init(FMLInitializationEvent event)
    {
        MappetVoice.logger.info("server init complete");
        super.init(event);
    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MappetVoice.logger.info("server post-init complete");
        super.postInit(event);
    }
}
