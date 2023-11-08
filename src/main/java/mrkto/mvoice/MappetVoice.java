package mrkto.mvoice;

import mchorse.mclib.config.values.ValueFloat;
import mchorse.mclib.utils.resources.RLUtils;
import mrkto.mvoice.api.Voice.VoiceManager;
import mrkto.mvoice.audio.speaker.speakerWriter;
import mrkto.mvoice.client.ValueVoiceButtons;
import mrkto.mvoice.items.RadioItem;
import mrkto.mvoice.utils.PacketUtils;
import mrkto.mvoice.utils.other.OpusNotLoadedException;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.minecraftforge.fml.common.event.FMLServerStoppingEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLModDisabledEvent;
import net.minecraftforge.fml.common.SidedProxy;

import mrkto.mvoice.proxy.CommonProxy;

import mchorse.mclib.McLib;
import mchorse.mclib.config.ConfigBuilder;
import mchorse.mclib.events.RegisterConfigEvent;
import mchorse.mclib.config.values.ValueInt;
import mchorse.mclib.config.values.ValueBoolean;

import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.File;
@Mod.EventBusSubscriber
@Mod(
        modid = MappetVoice.MOD_ID,
        name = MappetVoice.NAME,
        version = MappetVoice.VERSION
)
public class MappetVoice {

    public static final String MOD_ID = "mvoice";
    public static final String NAME = "MappetVoice";
    public static final String VERSION = "0.0.1";
    @SidedProxy(serverSide = "mrkto.mvoice.proxy.ServerProxy", clientSide = "mrkto.mvoice.proxy.ClientProxy")
    public static CommonProxy proxy;

    public static final SimpleNetworkWrapper NETWORK = new SimpleNetworkWrapper(MOD_ID);

    @Mod.Instance(MOD_ID)
    public static MappetVoice INSTANCE;
    //configuration
    public static ValueBoolean push;

    public static ValueInt volumes;
    public static ValueInt volumem;
    public static ValueFloat fadetime;
    public static ValueInt range;
    public static ValueBoolean radioItem;
    public static ValueBoolean hearOther;
    public static ValueBoolean hearOtherRadio;
    public static ValueBoolean onRadioSound;
    public static ValueBoolean offRadioSound;
    public static ValueBoolean needInArm;
    public static ValueBoolean noise;
    public static ValueBoolean switchRadioSound;
    public static ValueBoolean onRangeNoise;
    public static ValueInt maxNoise;
    public static ValueInt minNoise;
    public static ValueInt NoiseRange;
    public static Logger logger;
    public static VoiceManager voice;
    public static MinecraftServer server;
    public static File config;
    public static Item radio = new RadioItem();

    @SubscribeEvent
    public void onConfigRegister(RegisterConfigEvent event) {
        range = event.opAccess.category("Voice settings").getInt("hearrange", 25, 1, 200);
        range.syncable();
        ConfigBuilder builder = event.createBuilder(MOD_ID);

        builder.category("general").register(new ValueVoiceButtons("buttons").clientSide()).getCategory();
        push = builder.getBoolean("push", true);
        push.clientSide();
        volumes = builder.getInt("volumes", 100, 0, 200);
        volumes.clientSide();
        volumem = builder.getInt("volumem", 100, 0, 200);
        volumem.clientSide();
        fadetime = builder.getFloat("fadetime", 0.1f, 0.01f, 2);
        fadetime.clientSide();
        builder.category("radios");
        onRadioSound = builder.getBoolean("onRadioSound", true);
        onRadioSound.syncable();
        offRadioSound = builder.getBoolean("offRadioSound", true);
        offRadioSound.syncable();
        switchRadioSound = builder.getBoolean("switchRadioSound", true);
        switchRadioSound.syncable();
        onRangeNoise = builder.getBoolean("onRangeNoise", true);
        onRangeNoise.syncable();
        noise = builder.getBoolean("noise", true);
        noise.syncable();
        minNoise = builder.getInt("minNoise", 50, 0, 100);
        minNoise.syncable();
        maxNoise = builder.getInt("maxNoise", 90, 0, 100);
        maxNoise.syncable();
        NoiseRange = builder.getInt("NoiseRange", 50, 0, 10000);
        NoiseRange.syncable();
        radioItem = builder.getBoolean("useItem", true);
        radioItem.syncable();
        hearOther = builder.getBoolean("hearOther", true);
        hearOther.syncable();
        hearOtherRadio = builder.getBoolean("hearOtherRadio", true);
        hearOtherRadio.syncable();
        needInArm = builder.getBoolean("needInArm", true);
        needInArm.syncable();

    }
    @EventHandler
    public void preInit(FMLPreInitializationEvent event) throws OpusNotLoadedException {
        McLib.EVENT_BUS.register(this);
        logger = event.getModLog();
        config = event.getModConfigurationDirectory();
        MinecraftForge.EVENT_BUS.register(new mrkto.mvoice.api.EventHandler());
        PacketUtils.register();
        proxy.preInit(event);
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        proxy.init(event);
    }

    @EventHandler
    public void postInit(FMLPostInitializationEvent event) {
        proxy.postInit(event);
    }

    @EventHandler
    public void serverStart(FMLServerStartingEvent event) {
        voice = new VoiceManager();
        server = event.getServer();
        logger.info("hello world!");
    }
    @EventHandler
    public void serverStop(FMLServerStoppingEvent event){
        voice = null;
        server = null;
        logger.info("gg");
    }
    @EventHandler
    public void disableMod(FMLModDisabledEvent event){
        speakerWriter.deleteAllChanels();
        logger.info("bye");
    }
    @SubscribeEvent
    public static void onRegistryItem(Register<Item> e){
        e.getRegistry().register(radio);
    }
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public static void onRegistryModel(ModelRegistryEvent e) {
        registryModel(radio);
    }
    @SideOnly(Side.CLIENT)
    private static void registryModel(Item item) {
        final ResourceLocation regName = item.getRegistryName();
        final ModelResourceLocation mrl = new ModelResourceLocation(regName != null ? regName : RLUtils.create(""), "inventory");
        ModelBakery.registerItemVariants(item, mrl);
        ModelLoader.setCustomModelResourceLocation(item, 0, mrl);
    }
}
