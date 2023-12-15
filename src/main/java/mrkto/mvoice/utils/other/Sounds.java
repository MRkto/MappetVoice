package mrkto.mvoice.utils.other;

import mchorse.mclib.utils.resources.RLUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.io.IOException;
import java.io.InputStream;

public class Sounds {
    public static final SoundEvent on = registry("RadioOn");
    public static final SoundEvent off = registry("RadioOff");
    public static final SoundEvent rswitch = registry("RadioSwitch");
    private static InfinityByteArrayInputStream noise;

    @SubscribeEvent
    public void registerSounds(RegistryEvent.Register<SoundEvent> e) {
        ForgeRegistries.SOUND_EVENTS.register(on);
        ForgeRegistries.SOUND_EVENTS.register(off);
        ForgeRegistries.SOUND_EVENTS.register(rswitch);
    }

    private static SoundEvent registry(String name) {
        ResourceLocation uniqueName = new ResourceLocation("mvoice", name);
        return new SoundEvent(uniqueName).setRegistryName(uniqueName);
    }
    public static InfinityByteArrayInputStream getNoise(){
        if(noise == null){
            try {
                InputStream stream = Minecraft.getMinecraft().getResourceManager().getResource(RLUtils.create("mvoice:sounds/noise.wav")).getInputStream();
                Minecraft.getMinecraft().getResourceManager().getResource(RLUtils.create("mvoice:sounds/noise.wav"));
                noise = new InfinityByteArrayInputStream(stream);
            }catch (IOException e){e.printStackTrace();}
        }

        return noise;
    }
}
