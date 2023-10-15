package mrkto.mvoice.client.gui;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.*;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.values.ValueString;
import mrkto.mvoice.MappetVoice;
import mchorse.metamorph.api.MorphManager;
import mrkto.mvoice.api.Voice.client.ClientData;
import mrkto.mvoice.utils.FileUtils;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.nbt.JsonToNBT;

import javax.sound.sampled.SourceDataLine;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

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
                element.add(new GuiPlayerElement(mc, networkplayerinfo.getGameProfile().getName(), PlayerUtils.getVolume(networkplayerinfo.getGameProfile().getName()), this::PlayerElement));
        }
        this.root.add(element);

    }

    private void PlayerElement(GuiPlayerElement e) {
        ClientData data = ClientData.get();
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
