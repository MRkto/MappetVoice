package mrkto.mvoice.api.Events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;

public class OnPlayerStartSpeak extends Event {
    private final EntityPlayerMP player;
    private final byte speakType;
    public OnPlayerStartSpeak(EntityPlayerMP message, byte speakType) {
        this.player = message;
        this.speakType = speakType;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }
    public byte getType() {
        return speakType;
    }
}
