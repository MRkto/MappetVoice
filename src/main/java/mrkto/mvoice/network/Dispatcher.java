package mrkto.mvoice.network;

import mchorse.mclib.network.AbstractDispatcher;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.client.AudioUtils;
import mrkto.mvoice.client.ClientEventHandler;
import mrkto.mvoice.network.packets.*;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.relauncher.Side;

public class Dispatcher{
    private static final AbstractDispatcher DISPATCHER = new AbstractDispatcher(MappetVoice.MOD_ID) {
        public void register() {
            this.register(EventPacket.class, EventPacket.ServerHandler.class, Side.SERVER);
            this.register(RadioPacket.class, RadioPacket.ServerHandler.class, Side.SERVER);
            this.register(SyncCapabilityPacket.class, SyncCapabilityPacket.ClientHandler.class, Side.CLIENT);

        }
    };
    private static final AbstractDispatcher VOICE_DISPATCHER = new AbstractDispatcher("VOICEDISPATCHER") {
        public void register() {
            this.register(SoundPacketClient.class, SoundPacketClient.ClientHandler.class, Side.CLIENT);
            this.register(SoundPacket.class, SoundPacket.ServerHandler.class, Side.SERVER);


        }
    };
    public static void sendTo(IMessage message, EntityPlayerMP player) {
        DISPATCHER.sendTo(message, player);
    }

    public static void sendToServer(IMessage message) {
        DISPATCHER.sendToServer(message);
    }
    public static void sendSoundTo(byte[] data, BlockPos DataPos, EntityPlayerMP player, String name, double distance) {
        VOICE_DISPATCHER.sendTo(new SoundPacketClient(data, DataPos, name, distance, 0f), player);
    }
    public static void sendSoundTo(byte[] data, BlockPos DataPos, EntityPlayerMP player, String name, double distance, float balance) {
        VOICE_DISPATCHER.sendTo(new SoundPacketClient(data, DataPos, name, distance, balance), player);
    }
    public static boolean sending = false;
    public static float value = 0.0f;
    public static void sendSoundToServer(byte[] data, boolean isRadio) {
        float volume = AudioUtils.calcVolume(data);
        boolean high = volume > MappetVoice.numofaction.get();
        value = Math.max(0f, Math.min(MappetVoice.numofaction.get(), high ? value - MappetVoice.numofaction.get() / 20 : value + MappetVoice.numofaction.get() / 5));
        sending = !MappetVoice.voiceaction.get() || value != MappetVoice.numofaction.get();
        if(!sending)
            return;
        byte[] encodedData = AudioUtils.encode(data);
        VOICE_DISPATCHER.sendToServer(new SoundPacket(encodedData, isRadio, volume));
    }
    public static void postEventOnServer(String event, boolean isRadio) {
        DISPATCHER.sendToServer(new EventPacket(event, isRadio));
    }
    public static void postEventOnServer(String event) {
        DISPATCHER.sendToServer(new EventPacket(event, false));
    }
    public static void register() {
        DISPATCHER.register();
        VOICE_DISPATCHER.register();
    }
}
