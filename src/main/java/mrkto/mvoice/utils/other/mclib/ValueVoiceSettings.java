package mrkto.mvoice.utils.other.mclib;

import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import mchorse.mappet.client.gui.scripts.themes.GuiThemeEditorOverlayPanel;
import mchorse.mappet.client.gui.scripts.themes.Themes;
import mchorse.mappet.client.gui.scripts.utils.SyntaxStyle;
import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mappet.utils.ValueSyntaxStyle;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.gui.GuiConfigPanel;
import mchorse.mclib.config.values.IConfigGuiProvider;
import mchorse.mclib.config.values.Value;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Arrays;
import java.util.List;

public class ValueVoiceSettings extends Value implements IConfigGuiProvider {


    public ValueVoiceSettings(String id) {
        super(id);
        this.clientSide();
    }


    @SideOnly(Side.CLIENT)
    public List<GuiElement> getFields(Minecraft mc, GuiConfigPanel config) {
        GuiButtonElement button = new GuiButtonElement(mc, IKey.lang("mvoice.gui.setting"), (t) -> {
            GuiOverlay.addOverlay(GuiBase.getCurrent(), new GuiThemeEditorOverlayPanel(mc), 0.6F, 0.95F);
        });
        return Arrays.asList(button.tooltip(IKey.lang(this.getCommentKey())));
    }


}
