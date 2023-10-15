package mrkto.mvoice.client.gui;

import mchorse.mclib.client.gui.framework.GuiBase;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiButtonElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiStringListElement;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.keys.IKey;
import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.audio.speaker.speakerWriter;
import mrkto.mvoice.utils.AudioUtils;
import mrkto.mvoice.utils.PlayerUtils;
import net.minecraft.client.Minecraft;

import java.util.ArrayList;
import java.util.List;

public class GuiVoiceChange extends GuiBase {

        public GuiStringListElement Speakerlist;
        public GuiStringListElement Microlist;
        public GuiButtonElement SpeakerButton;
        public GuiButtonElement MicroButton;

        public GuiVoiceChange() {
            super();


            Minecraft mc = Minecraft.getMinecraft();
            GuiElement lelement = new GuiElement(mc);
            GuiElement relement = new GuiElement(mc);
            lelement.flex().relative(this.viewport).xy(0.25F, 0.5F).w(0.3F).anchor(0.5F, 0.5F).column(5).vertical().stretch();
            relement.flex().relative(this.viewport).xy(0.75F, 0.5F).w(0.3F).anchor(0.5F, 0.5F).column(5).vertical().stretch();

            ArrayList<String> mlist = AudioUtils.findAudioDevices(microReader.getFromat());
            ArrayList<String> slist = AudioUtils.findAudioDevices(speakerWriter.getFromat());

            this.Speakerlist = new GuiStringListElement(mc, this::GuiSpeakerList);
            this.Speakerlist.background().setList(slist);
            this.Speakerlist.sort();
            this.Speakerlist.flex().relative(this.viewport).x(0.5F, -10).y(0.5F).w(50).h(100).anchor(1F, 0.5F);
            this.Speakerlist.setVisible(false);
            this.Microlist = new GuiStringListElement(mc, this::GuiMicroList);
            this.Microlist.background().setList(mlist);
            this.Microlist.sort();
            this.Microlist.flex().relative(this.viewport).x(0.5F, -10).y(0.5F).w(50).h(100).anchor(1F, 0.5F);
            this.Microlist.setVisible(false);
            this.MicroButton = new GuiButtonElement(mc, IKey.str(PlayerUtils.getMicro()), this::GuiMicroButton);
            this.SpeakerButton = new GuiButtonElement(mc, IKey.str(PlayerUtils.getSpeaker()), this::GuiSpeakerButton);
            GuiLabel rguiLabel = new GuiLabel(mc, IKey.lang("mvoice.settings.speaker"));
            GuiLabel lguiLabel = new GuiLabel(mc, IKey.lang("mvoice.settings.micro"));
            rguiLabel.flex().h(10);
            lguiLabel.flex().h(10);
            lelement.add(lguiLabel);
            lelement.add(MicroButton);
            lelement.add(Microlist);
            relement.add(rguiLabel);
            relement.add(SpeakerButton);
            relement.add(Speakerlist);
            this.root.add(lelement);
            this.root.add(relement);
        }

        private void GuiMicroButton(GuiButtonElement guiButtonElement) {
            this.Microlist.setVisible(true);
        }
        private void GuiSpeakerButton(GuiButtonElement guiButtonElement) {
            this.Speakerlist.setVisible(true);
        }

        private void GuiSpeakerList(List<String> strings) {
            this.SpeakerButton.label = IKey.str(this.Speakerlist.getList().get(this.Speakerlist.getIndex()));
            AudioUtils.setSpeaker(this.SpeakerButton.label.get());
            this.Speakerlist.setVisible(false);
        }
        private void GuiMicroList(List<String> strings) {
            this.MicroButton.label = IKey.str(this.Microlist.getList().get(this.Microlist.getIndex()));
            AudioUtils.setMicro(this.MicroButton.label.get());
            this.Microlist.setVisible(false);
        }



        @Override
        public boolean doesGuiPauseGame() {return false;}

        @Override
        protected void closeScreen() {
            super.closeScreen();
        }

        @Override
        public void drawScreen(int mouseX, int mouseY, float partialTicks) {
            this.drawDefaultBackground();
            super.drawScreen(mouseX, mouseY, partialTicks);
        }
    }

