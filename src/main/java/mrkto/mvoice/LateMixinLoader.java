package mrkto.mvoice;

import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import zone.rong.mixinbooter.ILateMixinLoader;

import javax.annotation.Nullable;

import java.util.*;

public class LateMixinLoader implements IFMLLoadingPlugin, ILateMixinLoader
{
    @Override
    public List<String> getMixinConfigs()
    {
        return new ArrayList<>(Arrays.asList(
            "mixins/mixins.voice.json",
            "mixins/mixins.doc.json"
    ));
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[0];
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Nullable
    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public boolean shouldMixinConfigQueue(String mixinConfig) {
        return !mixinConfig.equals("mixins/mixins.doc.json") || !Loader.isModLoaded("mappetextras");
    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
