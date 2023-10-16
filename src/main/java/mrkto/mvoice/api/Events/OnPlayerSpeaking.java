package mrkto.mvoice.api.Events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnPlayerSpeaking  extends Event {
    private final EntityPlayerMP player;
    private final boolean isRadio;
    private final float volume;
    private final byte[] data;
    public OnPlayerSpeaking(EntityPlayerMP message, boolean isRadio, float volume, byte[] data) {
        this.player = message;
        this.isRadio = isRadio;
        this.volume = volume;
        this.data = data;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }
    public boolean isRadio() {
        return isRadio;
    }
    public float getVolume(){
        return volume;
    }
    public byte[] getData(){
        return data;
    }
}