package mrkto.mvoice.client.gui.VoiceMclibPanel;

import mchorse.mappet.client.gui.utils.overlays.GuiOverlay;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.mclib.GuiDashboard;
import mchorse.mclib.client.gui.mclib.GuiDashboardPanel;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.utils.Direction;
import mrkto.mvoice.client.ClientData;
import mrkto.mvoice.client.gui.GuiPlayerElement;
import mrkto.mvoice.client.gui.GuiPlayerSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class GuiMclibVoiceDashboard extends GuiDashboardPanel<GuiDashboard>
{
    GuiPlayerListElement list;
    public GuiMclibVoiceDashboard(Minecraft mc, GuiDashboard dashboard)
    {
        super(mc, dashboard);

        this.list = new GuiPlayerListElement(mc, IKey.lang("mvoice.settings.playerlist"), this::playerList);
        this.list.flex().relative(this.flex()).set(0, 0, 120, 0).h(1F).x(1F, -120);
        this.add(this.list);

        GuiIconElement reload = new GuiIconElement(mc, Icons.REFRESH, (b) -> this.list.reload(mc));
        reload.flex().set(0, 2, 24, 24).relative(this).x(1F, -28);

        this.add(reload);
    }

    private void playerList(Object o) {
        PlayerSettingsOverlay panel = new PlayerSettingsOverlay(this.mc, o.toString());

        GuiOverlay.addOverlay(GuiBase.getCurrent(), panel, 0.7F, 0.9F);
    }

    private void PlayerElement(GuiPlayerElement e) {
        ClientData data = ClientData.getInstance();
        data.getData().put(e.name, e.volume.value);
        data.save();
    }

    @Override
    public boolean isClientSideOnly()
    {
        return true;
    }
}
