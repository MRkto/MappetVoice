package mrkto.mvoice.utils.other.mclib;

import mrkto.mvoice.MappetVoice;
import mchorse.mclib.client.gui.utils.Icon;
import mchorse.mclib.client.gui.utils.IconRegistry;
import net.minecraft.util.ResourceLocation;
public class MVIcons {
    public static final ResourceLocation TEXTURE = new ResourceLocation(MappetVoice.MOD_ID, "textures/voice.png");

    public static final Icon MutedD = new Icon(TEXTURE, 0, 0, 16, 16, 64, 64);
    public static final Icon MutedMicro = new Icon(TEXTURE, 16, 0, 16, 16, 64, 64);
    public static final Icon MutedSpeaker = new Icon(TEXTURE, 32, 0, 16, 16, 64, 64);
    public static final Icon MutedEarphones = new Icon(TEXTURE, 48, 0, 16, 16, 64, 64);
    public static final Icon D = new Icon(TEXTURE, 0, 16, 16, 16, 64, 64);
    public static final Icon Micro = new Icon(TEXTURE, 16, 16, 16, 16, 64, 64);
    public static final Icon Speaker = new Icon(TEXTURE, 32, 16, 16, 16, 64, 64);
    public static final Icon Earphones = new Icon(TEXTURE, 48, 16, 16, 16, 64, 64);

    public static void register()
    {
        IconRegistry.register("MutedDinamo", MutedD);
        IconRegistry.register("MutedMicro", MutedMicro);
        IconRegistry.register("MutedSpeaker", MutedSpeaker);
        IconRegistry.register("MutedEarphones", MutedEarphones);

        IconRegistry.register("Dinamo", D);
        IconRegistry.register("Micro", Micro);
        IconRegistry.register("Speaker", Speaker);
        IconRegistry.register("Earphones", Earphones);
    }
}
