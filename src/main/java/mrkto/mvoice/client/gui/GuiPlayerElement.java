package mrkto.mvoice.client.gui;

import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mchorse.mclib.config.values.ValueDouble;
import net.minecraft.client.Minecraft;

import java.util.function.Consumer;

public class GuiPlayerElement extends GuiElement {
    public GuiTrackpadElement volume;
    public String name;
    private final Consumer<GuiPlayerElement> callback;
    public GuiPlayerElement(Minecraft mc, String name, double Volume, Consumer<GuiPlayerElement> callback) {
        super(mc);
        this.callback = callback;
        this.name = name;
        this.volume = new GuiTrackpadElement(mc, (v) -> this.callback());
        this.volume.setValue(Volume);
        this.volume.max = 200;
        this.volume.min = 0;
        this.tooltip(IKey.str(name));
        this.flex().row(5).height(20);
        this.add(volume);

    }
    private void callback()
    {
        if (this.callback != null)
        {
            this.callback.accept(this);
        }
    }
    public String toString(){
        return "Name:" + this.name + " Volume:" + this.volume.value;
    }

}
