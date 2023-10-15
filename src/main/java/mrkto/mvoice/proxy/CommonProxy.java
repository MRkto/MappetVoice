package mrkto.mvoice.proxy;

import mrkto.mvoice.api.EventHandler;
import mrkto.mvoice.utils.other.mclib.MVIcons;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import mrkto.mvoice.utils.PacketUtils;


public class CommonProxy {
    public void preInit(FMLPreInitializationEvent event)
    {
        MinecraftForge.EVENT_BUS.register(new EventHandler());
        PacketUtils.register();


    }

    public void init(FMLInitializationEvent event)
    {
        MVIcons.register();
    }

    public void postInit(FMLPostInitializationEvent event) {

    }
}
