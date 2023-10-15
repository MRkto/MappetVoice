package mrkto.mvoice.items;

import mrkto.mvoice.client.gui.GuiRadioSettings;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RadioItem extends Item {
    public RadioItem() {
        this.setRegistryName("radio");
        this.setUnlocalizedName("radioItem");
        this.setMaxStackSize(1);
    }
    @SideOnly(Side.CLIENT)
    @Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn) {
        if(playerIn.getHeldItemMainhand() != ItemStack.EMPTY && playerIn.getName().equals(Minecraft.getMinecraft().player.getName()))
            Minecraft.getMinecraft().displayGuiScreen(new GuiRadioSettings(playerIn.getHeldItemMainhand()));
        return super.onItemRightClick(worldIn, playerIn, handIn);
    }


}
