package mrkto.mvoice;

import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import mrkto.mvoice.mixins.IModule;
import zone.rong.mixinbooter.ILateMixinLoader;
import mrkto.mvoice.mixins.Voice;
import javax.annotation.Nullable;
import net.minecraft.nbt.JsonToNBT;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class LateMixinLoader implements IFMLLoadingPlugin, ILateMixinLoader
{
    @Override
    public List<String> getMixinConfigs()
    {
        return Voice.getInstance().getMixinConfigs();
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
    public String getAccessTransformerClass()
    {
        return null;
    }
}
