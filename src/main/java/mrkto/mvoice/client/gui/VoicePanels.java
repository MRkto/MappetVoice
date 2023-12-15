package mrkto.mvoice.client.gui;

import mchorse.mappet.client.gui.GuiMappetDashboard;
import mchorse.mclib.client.gui.mclib.GuiAbstractDashboard;
import mchorse.mclib.client.gui.mclib.GuiDashboard;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.events.RegisterDashboardPanels;
import mchorse.mclib.events.RemoveDashboardPanels;
import mrkto.mvoice.client.gui.VoiceMclibPanel.GuiMclibVoiceDashboard;
import mrkto.mvoice.utils.other.mclib.MVIcons;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class VoicePanels {
    GuiMclibVoiceDashboard voicePanel;
    @SubscribeEvent
    public void OnDashboardRegister(RegisterDashboardPanels event){
        Minecraft mc = Minecraft.getMinecraft();
        GuiAbstractDashboard dashboard = event.dashboard;
        if(dashboard instanceof GuiDashboard){
            this.voicePanel = new GuiMclibVoiceDashboard(mc, (GuiDashboard) dashboard);
            dashboard.panels.registerPanel(this.voicePanel, IKey.lang("mvoice.settings.dashboard"), MVIcons.Earphones);
        }
        if(dashboard instanceof GuiMappetDashboard){
            String da = "da";
        }

    }
    @SubscribeEvent
    public void OnRemoveDashboardPanels(RemoveDashboardPanels event){
        this.voicePanel = null;
    }
}
