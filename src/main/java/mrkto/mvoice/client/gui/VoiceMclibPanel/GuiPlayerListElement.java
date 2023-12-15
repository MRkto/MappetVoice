package mrkto.mvoice.client.gui.VoiceMclibPanel;

import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiDraw;
import mchorse.mclib.client.gui.utils.Area;
import mchorse.mclib.client.gui.utils.ScrollArea;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.metamorph.api.MorphManager;
import mchorse.metamorph.api.morphs.AbstractMorph;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.util.math.MathHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class GuiPlayerListElement extends GuiListElement<String> {
    public static Map<String, String> skins = new HashMap<>();
    public IKey title;
    public GuiPlayerListElement(Minecraft mc, IKey title, Consumer callback) {
        super(mc, callback);

        this.title = title;
        this.area = new Area();
        this.scroll = new ScrollArea(20);

        for(NetworkPlayerInfo networkplayerinfo : mc.getConnection().getPlayerInfoMap()){
            if(networkplayerinfo.getGameProfile().getName().equals(mc.player.getName()))
                continue;
            this.list.add(networkplayerinfo.getGameProfile().getName());
        }
        this.scroll.setSize(this.list.size());
        this.scroll.clamp();
    }
    public void reload(Minecraft mc){
        this.list.clear();

        for(NetworkPlayerInfo networkplayerinfo : mc.getConnection().getPlayerInfoMap()){
            if(networkplayerinfo.getGameProfile().getName().equals(mc.player.getName()))
                continue;
            this.list.add(networkplayerinfo.getGameProfile().getName());
        }
        this.scroll.setSize(this.list.size());
        this.scroll.clamp();

        skins.clear();
    }

    @Override
    protected void drawElementPart(String element, int i, int x, int y, boolean hover, boolean selected)
    {
        GuiContext context = GuiBase.getCurrent();
        int h = this.scroll.scrollItemSize;
        String skin = "blockbuster:textures/blocks/black.png";
        if(skins.containsKey(element)){
            skin = skins.get(element);
        } else {
            skins.put(element, "blockbuster:textures/blocks/black.png");
            PlayerUtils.getSkinlink(element);
        }

        AbstractMorph morph;
        try {
            morph = MorphManager.INSTANCE.morphFromNBT(JsonToNBT.getTagFromJson("{Skin:\""+skin+"\",Settings:{Hands:1b},Name:\"blockbuster.fred\"}"));
        } catch (NBTException e) {
            throw new RuntimeException(e);
        }



        int mny = MathHelper.clamp(y, this.scroll.y, this.scroll.ey());
        int mxy = MathHelper.clamp(y + 20, this.scroll.y, this.scroll.ey());

        if (mxy - mny > 0)
        {
            GuiDraw.scissor(x, mny, this.scroll.w, mxy - mny, context);
            morph.renderOnScreen(this.mc.player, x + this.scroll.w - 16, y + 30, 20, 1);
            GuiDraw.unscissor(context);
        }

        String label = String.format("(%s)", element);

        this.font.drawStringWithShadow(label, x + 10, y + 6, hover ? 16777120 : 0xffffff);
        Gui.drawRect(x, y + h - 1, x + this.area.w, y + h, 0x88181818);
    }
    @Override
    public void draw(GuiContext context)
    {
        this.area.draw(0xff333333);

        Gui.drawRect(this.area.x, this.area.y, this.area.ex(), this.area.y + 30, 0x44000000);
        this.font.drawStringWithShadow(this.title.get(), this.area.x + 10, this.area.y + 11, 0xcccccc);

        super.draw(context);
    }
    @Override
    public void resize()
    {
        super.resize();

        this.scroll.copy(this.area);
        this.scroll.y += 30;
        this.scroll.h -= 30;
    }
}
