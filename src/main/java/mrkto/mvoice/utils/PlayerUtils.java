package mrkto.mvoice.utils;


import com.google.gson.Gson;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.Voice.client.ClientData;
import mrkto.mvoice.api.Voice.data.PLayersData;
import mrkto.mvoice.api.Voice.data.PlayerData;
import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.audio.speaker.speakerWriter;
import mrkto.mvoice.items.RadioItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagString;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

import static mrkto.mvoice.MappetVoice.radio;
import static mrkto.mvoice.utils.FileUtils.getClientData;

public class PlayerUtils {
    @SideOnly(Side.CLIENT)
    public static void saveMicro(String name){
        ClientData data = FileUtils.getClientData();
        data.microName = name;
        FileUtils.setClientData(data);
    }
    @SideOnly(Side.CLIENT)
    public static void saveSpeaker(String name){
        ClientData data = FileUtils.getClientData();
        data.speakerName = name;
        FileUtils.setClientData(data);
    }
    @SideOnly(Side.CLIENT)
    public static String getMicro(){
        return AudioUtils.findMixer(FileUtils.getClientData().microName, microReader.getFromat()).getMixerInfo().getName();
    }
    @SideOnly(Side.CLIENT)
    public static String getSpeaker(){
        return AudioUtils.findMixer(FileUtils.getClientData().speakerName, speakerWriter.getFromat()).getMixerInfo().getName();
    }
    public static String getSkinlink(EntityPlayerMP player){
        String json = readJsonFromUrl("http://skinsystem.ely.by/textures/" + player.getName());
        Gson gson = new Gson();
        if (json != null) {
            objectUrl1 objectUrl = gson.fromJson(json, objectUrl1.class);
            return objectUrl.SKIN.url;
        } else {
            json = readJsonFromUrl("https://auth.tlauncher.org/skin/profile/texture/login/" + player.getName());
            objectUrl1 objectUrl = gson.fromJson(json, objectUrl1.class);
            return objectUrl.SKIN.url;

        }

    }
    @SideOnly(Side.CLIENT)
    public static Double getVolume(String name){
        ClientData data = getClientData();
        if(data.data.containsKey(name))
            return data.data.get(name);
        return 100d;
    }

    public static String readJsonFromUrl(String url) {
        URLConnection connection;
        InputStream is = null;
        try {
            connection = new URL(url).openConnection();
            if (((HttpURLConnection) connection).getResponseCode() != 200) {
                return null;
            }
            is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = rd.read()) != -1) {
                sb.append((char) cp);
            }

            return sb.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }


    class objectUrl1 {
        public objectSkin SKIN;
        class objectSkin {
            public String url;
        }
    }

}


