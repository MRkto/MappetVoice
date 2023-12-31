package mrkto.mvoice.mixins;

import mchorse.mappet.api.scripts.code.entities.ScriptPlayer;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.EventHandler;
import mrkto.mvoice.capability.Profile;
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
        return Profile.get(getMinecraftPlayer()).getMuted();
    }
    public void mute(){
        Profile.get(getMinecraftPlayer()).setMuted(true);
    }
    public void unmute(){
        Profile.get(getMinecraftPlayer()).setMuted(false);
    }
    public boolean isLocalMuted(String name){
        return Profile.get(getMinecraftPlayer()).getMutedList().contains(name);
    }
    public void localMute(String name){
        Profile.get(getMinecraftPlayer()).addMuted(name);
    }
    public void localUnmute(String name){
        Profile.get(getMinecraftPlayer()).removeMuted(name);
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
    public boolean setWave(String wave){
        return PlayerUtils.setWave(getMinecraftPlayer(), wave);
    }
    public String getWave(){
        return PlayerUtils.getWave(getMinecraftPlayer());
    }

}
