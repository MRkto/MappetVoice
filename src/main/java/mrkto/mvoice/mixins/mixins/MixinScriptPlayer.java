package mrkto.mvoice.mixins.mixins;

import mchorse.mappet.api.scripts.code.entities.ScriptPlayer;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.EventHandler;
import mrkto.mvoice.api.Voice.data.PLayersData;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.entity.player.EntityPlayerMP;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ScriptPlayer.class, remap = false)
public abstract class MixinScriptPlayer {
    @Shadow
    public abstract EntityPlayerMP getMinecraftPlayer();
    public void join(String group){
        MappetVoice.voice.getGroup(group).join(getMinecraftPlayer());
    }
    public void leave(String group){
        MappetVoice.voice.getGroup(group).leave(getMinecraftPlayer());
    }
    public boolean isMuted(){
        return PLayersData.get().getPlayerData(getMinecraftPlayer()).isMuted();
    }
    public void mute(){
        PLayersData data = PLayersData.get();
        data.getPlayerData(getMinecraftPlayer()).setMuted(true);
        data.save();
    }
    public void unmute(){
        PLayersData data = PLayersData.get();
        data.getPlayerData(getMinecraftPlayer()).setMuted(false);
        data.save();
    }
    public boolean isLocalMuted(String name){
        return PLayersData.get().getPlayerData(getMinecraftPlayer()).getLocalMutedList().contains(name);
    }
    public void localMute(String name){
        PLayersData data = PLayersData.get();
        data.getPlayerData(getMinecraftPlayer()).getLocalMutedList().add(name);
        data.save();
    }
    public void localUnmute(String name){
        PLayersData data = PLayersData.get();
        data.getPlayerData(getMinecraftPlayer()).getLocalMutedList().remove(name);
        data.save();
    }
    public boolean isSpeaking(){
        return EventHandler.list.get(getMinecraftPlayer().getName()) == 1;
    }
    public boolean isRadioSpeaking(){
        return EventHandler.list.get(getMinecraftPlayer().getName()) == 2;
    }
    public boolean isSilent(){
        return EventHandler.list.get(getMinecraftPlayer().getName()) == 0;
    }

}
