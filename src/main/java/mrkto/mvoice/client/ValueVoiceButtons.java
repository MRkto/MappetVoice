package mrkto.mvoice.client;

import mchorse.blockbuster.Blockbuster;
import mchorse.blockbuster.ClientProxy;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.tooltips.ITooltip;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.GuiUtils;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.gui.GuiConfigPanel;
import mchorse.mclib.config.values.ValueGUI;
import mrkto.mvoice.client.gui.GuiVoiceChange;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.File;
import java.util.Arrays;
import java.util.List;

public class ValueVoiceButtons extends ValueGUI
{
    public ValueVoiceButtons(String id)
    {
        super(id);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public List<GuiElement> getFields(Minecraft mc, GuiConfigPanel config)
    {
        GuiButtonElement settings = new GuiButtonElement(mc, IKey.lang("mvoice.config.button.settings"), (button) -> Minecraft.getMinecraft().displayGuiScreen(new GuiVoiceChange()));
        GuiIconElement settings2 = new GuiIconElement(mc, Icons.SERVER, (button) -> GuiUtils.openWebLink("https://www.youtube.com/watch?v=xvFZjo5PgG0"));

        GuiElement first = Elements.row(mc, 5, 0, 20, settings, settings2);

        return Arrays.asList(first);
    }
}
