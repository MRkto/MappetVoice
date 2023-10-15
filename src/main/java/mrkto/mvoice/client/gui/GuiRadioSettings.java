package mrkto.mvoice.client.gui;

import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTextElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.Voice.client.ClientData;
import mrkto.mvoice.items.RadioItem;
import mrkto.mvoice.network.common.EventPacket;
import mrkto.mvoice.network.common.RadioPacket;
import mrkto.mvoice.utils.FileUtils;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
@SideOnly(Side.CLIENT)
public class GuiRadioSettings extends GuiBase {

    GuiTextElement picker;
    String value = "";
    ItemStack stack;
    public GuiRadioSettings(ItemStack item) {
        super();
        Minecraft mc = Minecraft.getMinecraft();
        this.stack = item;
        picker = new GuiTextElement(mc, 5, this::text);
        picker.flex().relative(this.viewport).xy(0.5F, 0.7F).wh(0.15f, 0.05f);

        value = stack.getTagCompound() != null ? stack.getTagCompound().getString("wave") : "";
        picker.setText(value);

        this.root.add(picker);

    }

    private void text(String s) {
        value = s;
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }


    @Override
    protected void closeScreen() {
        MappetVoice.NETWORK.sendToServer(new RadioPacket(this.value));
        super.closeScreen();

    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {

        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}
