package mrkto.mvoice.client.gui.VoiceMclibPanel;

import mchorse.mappet.client.gui.utils.overlays.GuiOverlayPanel;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class PlayerSettingsOverlay extends GuiOverlayPanel {
    public PlayerSettingsOverlay(Minecraft mc, String player) {
        super(mc, IKey.lang("mvoice.settings.playersettingoverlay"));
    }

    @Override
    public void draw(GuiContext context) {
        this.drawBackground(context);
        super.draw(context);
    }
}
