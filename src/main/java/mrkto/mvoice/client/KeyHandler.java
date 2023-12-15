package mrkto.mvoice.client;

import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.gui.GuiVoiceChange;
import mrkto.mvoice.network.Dispatcher;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.lwjgl.input.Keyboard;

public class KeyHandler {
    private static final String KeyGroup = "Mappet Voice";
    @SideOnly(Side.CLIENT)
    private static final KeyBinding KeySpeak = new KeyBinding(I18n.format("mvoice.keybinds.speak"), Keyboard.KEY_V, KeyGroup);
    @SideOnly(Side.CLIENT)
    private static final KeyBinding KeyMute = new KeyBinding(I18n.format("mvoice.keybinds.other"), Keyboard.KEY_M, KeyGroup);
    @SideOnly(Side.CLIENT)
    private static final KeyBinding KeyRadio = new KeyBinding(I18n.format("mvoice.keybinds.radio"), Keyboard.KEY_LMENU, KeyGroup);
    private static boolean KeySpeakPressed = false;
    private static boolean KeyMutePressed = false;
    private static boolean KeyRadioPressed = false;

    @SideOnly(Side.CLIENT)
    public static void register() {
        ClientRegistry.registerKeyBinding(KeySpeak);
        ClientRegistry.registerKeyBinding(KeyMute);
        ClientRegistry.registerKeyBinding(KeyRadio);
    }

    /**
     * Обработчик события нажатия клавиш в игре
     */
    @SideOnly(Side.CLIENT)
    @SubscribeEvent
    public void inputUpdate(InputEvent.KeyInputEvent event){
        if(MappetVoice.voiceaction.get()){
            actionOnUpdate();
        }else{
            standardUpdate();
        }


        if(KeyMute.isKeyDown() && !KeyMutePressed){
            Minecraft.getMinecraft().displayGuiScreen(new GuiVoiceChange());
            KeyMutePressed = true;
        }
        if(!KeyMute.isKeyDown() && KeyMutePressed){
            KeyMutePressed = false;
        }
    }
    private void standardUpdate(){
        String wave = PlayerUtils.getWave(Minecraft.getMinecraft().player);
        if(KeyRadio.isKeyDown() && !KeyRadioPressed && !KeySpeakPressed && wave != null && !wave.equals("")){
            KeyRadioPressed = true;
            MappetVoice.AudioManager.getInput().startRecording(true);
        }
        if(!KeyRadio.isKeyDown() && KeyRadioPressed && !KeySpeakPressed){
            KeyRadioPressed = false;
            MappetVoice.AudioManager.getInput().stopRecording();
        }

        if(KeySpeak.isKeyDown() && !KeySpeakPressed && !KeyRadioPressed){
            KeySpeakPressed = true;
            MappetVoice.AudioManager.getInput().startRecording(false);
        }
        if(!KeySpeak.isKeyDown() && KeySpeakPressed && !KeyRadioPressed){
            KeySpeakPressed = false;
            MappetVoice.AudioManager.getInput().stopRecording();
        }
    }
    private void actionOnUpdate(){
        String wave = PlayerUtils.getWave(Minecraft.getMinecraft().player);
        if(KeyRadio.isKeyDown() && !KeyRadioPressed && !KeySpeakPressed && wave != null && !wave.equals("")){
            KeyRadioPressed = true;
            Dispatcher.postEventOnServer("StartSpeak", true);
            MappetVoice.AudioManager.getInput().setRadio(true);
        }
        if(!KeyRadio.isKeyDown() && KeyRadioPressed && !KeySpeakPressed){
            KeyRadioPressed = false;
            Dispatcher.postEventOnServer("StopSpeak");
            Dispatcher.postEventOnServer("StartSpeak", false);
            MappetVoice.AudioManager.getInput().setRadio(false);
        }
    }
}
