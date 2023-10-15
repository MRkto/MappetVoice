package mrkto.mvoice.proxy;

import mrkto.mvoice.MappetVoice;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;

import javax.annotation.Nullable;


public class ServerProxy extends CommonProxy{
    @Override
    public void preInit(FMLPreInitializationEvent event)
    {


        MappetVoice.logger.info("server pre-init complete");
        super.preInit(event);
    }
    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        super.postInit(event);
    }
}
