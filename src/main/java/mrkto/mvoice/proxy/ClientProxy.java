package mrkto.mvoice.proxy;


import mrkto.mvoice.api.Voice.client.ClientData;
import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.audio.speaker.speakerWriter;
import mrkto.mvoice.utils.AudioUtils;
import mrkto.mvoice.utils.FileUtils;
import mrkto.mvoice.utils.other.KeyHandler;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import mrkto.mvoice.MappetVoice;

import java.io.IOException;
import java.nio.file.Files;

import static mrkto.mvoice.utils.FileUtils.getConfigFilePath;


public class ClientProxy extends CommonProxy{

    @Override
    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent event)
    {
        //MinecraftForge.EVENT_BUS.register(new KeyHandler());
        KeyHandler.register();
        if(!AudioUtils.loadOpus())
        {
            MappetVoice.logger.error("Opus initialization failed. MappetVoice will not work.");
            MappetVoice.logger.error("Opus initialization error");
            MappetVoice.logger.error("Try delete all mappetvoiceopus<number> files in " + System.getProperty("java.io.tmpdir") + " if this doesn't works your device is not support opus");
            FMLCommonHandler.instance().exitJava(0, true);
        }
        MinecraftForge.EVENT_BUS.register(new KeyHandler());
        MappetVoice.logger.info("client pre-init complete");
        super.preInit(event);

    }
    @Override
    public void init(FMLInitializationEvent event)
    {
        super.init(event);
    }
    @Override
    public void postInit(FMLPostInitializationEvent event) {
        if(!Files.exists(getConfigFilePath("Settings.json"))) {
            try {
                getConfigFilePath("Settings.json").toFile().createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if(FileUtils.getClientData() == null){
            microReader.setMixer("");
            speakerWriter.setMixer("");
            FileUtils.setClientData(new ClientData(microReader.getMixer(), speakerWriter.getMixer()));
        }
        AudioUtils.setConfig();
        super.postInit(event);
    }
}
