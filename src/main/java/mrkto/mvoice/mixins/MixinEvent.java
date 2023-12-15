package mrkto.mvoice.mixins;

import mchorse.mappet.api.scripts.code.ScriptEvent;
import mrkto.mvoice.api.scripts.code.Voice;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ScriptEvent.class, remap = false)
public class MixinEvent {
    public Voice getVoice(){
        return new Voice();
    }
}
