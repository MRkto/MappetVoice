package mrkto.mvoice.client.gui;

import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.*;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mrkto.mvoice.client.ClientData;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;

public class GuiPlayerSettings extends GuiBase {
    public GuiPlayerSettings(){
        super();
        Minecraft mc = Minecraft.getMinecraft();
        GuiElement element = new GuiElement(mc);
        element.flex().relative(this.viewport).xy(0.5F, 0.3F).w(0.3F).anchor(0.5F, 0F).column(5).vertical().stretch().scroll();
        GuiLabel label = new GuiLabel(mc, IKey.lang("mvoice.settings.playerlist"));
        label.flex().h(10);
        element.add(label);
        for(NetworkPlayerInfo networkplayerinfo : mc.getConnection().getPlayerInfoMap()){
            if(!networkplayerinfo.getGameProfile().getName().equals(mc.player.getName()))
                element.add(new GuiPlayerElement(mc, networkplayerinfo.getGameProfile().getName(), ClientData.getInstance().getVolume(networkplayerinfo.getGameProfile().getName()), this::PlayerElement));
        }
        this.root.add(element);

    }

    private void PlayerElement(GuiPlayerElement e) {
        ClientData data = ClientData.getInstance();
        data.getData().put(e.name, e.volume.value);
        data.save();
    }

    @Override
    public boolean doesGuiPauseGame() {return false;}


    @Override
    protected void closeScreen() {
        super.closeScreen();
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        this.drawDefaultBackground();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
