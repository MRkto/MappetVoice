package mrkto.mvoice.proxy;


import mchorse.mclib.McLib;
import mchorse.mclib.utils.ReflectionUtils;
import mrkto.mvoice.api.Events.OnEngineRegistry;
import mrkto.mvoice.client.*;
import mrkto.mvoice.client.audio.AudioEngineLoader;
import mrkto.mvoice.client.gui.VoicePanels;
import mrkto.mvoice.utils.other.OpusNotLoadedException;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mrkto.mvoice.MappetVoice;

import java.io.File;

public class ClientProxy extends ServerProxy{

    @Override
    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent event) throws OpusNotLoadedException {
        if (!AudioUtils.loadOpus()) {
            MappetVoice.logger.error("\u001B[31m#############################################################################################\u001B[0m");
            MappetVoice.logger.error("\u001B[31m# Opus initialization failed. MappetVoice will not work.\u001B[0m");
            MappetVoice.logger.error("\u001B[31m# Opus initialization error\u001B[0m");
            MappetVoice.logger.error("\u001B[31m# Try delete all opus<numbers> files in " + System.getProperty("java.io.tmpdir") + " if this doesn't works your device is not support opus or other problems\u001B[0m");
            MappetVoice.logger.error("\u001B[31m# if your operating system is windows, macos or linux, inform the author of the modification\u001B[0m");
            MappetVoice.logger.error("\u001B[31m#############################################################################################\u001B[0m");
            throw new OpusNotLoadedException("unable to load opus");
        }
        KeyHandler.register();

        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        MinecraftForge.EVENT_BUS.register(new ClientEventHandler());
        McLib.EVENT_BUS.register(new VoicePanels());
        MinecraftForge.EVENT_BUS.post(new OnEngineRegistry());

        ReflectionUtils.registerResourcePack(new IconsPack(ClientData.getConfigFilePath("icons").toFile()));

        MappetVoice.logger.info("client pre-init complete");


    }

    @Override
    public void init(FMLInitializationEvent event)
    {
        AudioEngineLoader.loadEngine(ClientData.getInstance().getEngineName());
        MappetVoice.logger.info("client init complete");
    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        MappetVoice.logger.info("client post-init complete");
    }
}
