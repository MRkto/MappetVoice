package mrkto.mvoice.mixins;

import mrkto.mvoice.LateMixinLoader;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Voice{

    private static Voice instance;
    public static Voice getInstance() {
        if (Voice.instance == null) {
            Voice.instance = new Voice();
        }
        return Voice.instance;
    }


    public void preInit(FMLPreInitializationEvent event) {
        event.getEventType();

    }


    public void init(FMLInitializationEvent event) {



    }


    public void postInit(FMLPostInitializationEvent event) {



    }


    public List<String> getMixinConfigs() {
        

        return new ArrayList<>(Arrays.asList(
                "mixins/mixins.voice.json",
                "mixins/mixins.doc.json"
        ));
    }
}
