package mrkto.mvoice.mixins;

import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Voice implements IModule {

    private static Voice instance;
    public static IModule getInstance() {
        if (Voice.instance == null) {
            Voice.instance = new Voice();
        }
        return Voice.instance;
    }

    @Override
    public void preInit(FMLPreInitializationEvent event) {



    }

    @Override
    public void init(FMLInitializationEvent event) {



    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {



    }

    @Override
    public List<String> getMixinConfigs() {
        

        return new ArrayList<>(Arrays.asList(
                "mixins/mixins.voice.json"
        ));
    }
}
