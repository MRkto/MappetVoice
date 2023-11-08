package mrkto.mvoice.proxy;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.EventHandler;
import mrkto.mvoice.utils.other.OpusNotLoadedException;
import mrkto.mvoice.utils.other.mclib.MVIcons;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import mrkto.mvoice.utils.PacketUtils;


public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event) throws OpusNotLoadedException {
        MappetVoice.logger.info("pre-init complete");
    }

    public void init(FMLInitializationEvent event)
    {
        MVIcons.register();
        MappetVoice.logger.info("init complete");
    }

    public void postInit(FMLPostInitializationEvent event) {
        MappetVoice.logger.info("post-init complete");
    }
}
