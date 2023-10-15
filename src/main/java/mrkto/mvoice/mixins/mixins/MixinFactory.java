package mrkto.mvoice.mixins.mixins;

import mchorse.mappet.api.scripts.code.ScriptFactory;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.scripts.code.Voice;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = ScriptFactory.class, remap = false)
public class MixinFactory {
    public Voice getVoice(){
        return new Voice();
    }

}
