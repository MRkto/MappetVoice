package mrkto.mvoice.api.Events;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.common.eventhandler.Event;


public class OnPlayerStopSpeak extends Event {
    private final EntityPlayerMP player;

    public OnPlayerStopSpeak(EntityPlayerMP message) {
        this.player = message;
    }

    public EntityPlayerMP getPlayer() {
        return player;
    }
}
