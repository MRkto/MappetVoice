package mrkto.mvoice.utils;


import com.google.gson.Gson;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.api.Voice.data.ClientData;
import mrkto.mvoice.api.Voice.data.PlayersData;
import mrkto.mvoice.audio.microphone.microReader;
import mrkto.mvoice.audio.speaker.speakerWriter;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;

public class PlayerUtils {
    @SideOnly(Side.CLIENT)
    public static void saveMicro(String name){
        ClientData data = ClientData.get();
        data.setMicroName(name);
        data.save();
    }
    @SideOnly(Side.CLIENT)
    public static void saveSpeaker(String name){
        ClientData data = ClientData.get();
        data.setSpeakerName(name);
        data.save();
    }
    @SideOnly(Side.CLIENT)
    public static String getMicro(){
        return AudioUtils.findMixer(ClientData.get().getMicroName(), microReader.getFromat()).getMixerInfo().getName();
    }
    @SideOnly(Side.CLIENT)
    public static String getSpeaker(){
        return AudioUtils.findMixer(ClientData.get().getSpeakerName(), speakerWriter.getFromat()).getMixerInfo().getName();
    }
    public static boolean setWave(EntityPlayerMP player, String wave){
        PlayersData data = PlayersData.get();
        data.getPlayerData(player).setWave(wave);
        data.save();
        if(!MappetVoice.radioItem.get()){
            return true;
        }
        boolean set = false;
        for(ItemStack item : player.inventory.mainInventory){
            if(item.getItem().getRegistryName() == MappetVoice.radio.getRegistryName()){
                NBTTagCompound tag = new NBTTagCompound();
                tag.setString("wave", wave);
                item.setTagCompound(tag);
                set = true;
            }
        }
        return set;
    }
    public static String getWave(EntityPlayerMP player){

        if(!MappetVoice.radioItem.get()){
            return PlayersData.get().getPlayerData(player).getWave();
        }

        ItemStack item = player.getHeldItemMainhand();
        if(item.getItem().getRegistryName() == MappetVoice.radio.getRegistryName() && item.getTagCompound() != null && !item.getTagCompound().getString("wave").equals("")){
            return item.getTagCompound().getString("wave");
        }
        item = player.getHeldItemOffhand();
        if(item.getItem().getRegistryName() == MappetVoice.radio.getRegistryName() && item.getTagCompound() != null && !item.getTagCompound().getString("wave").equals("")){
            return item.getTagCompound().getString("wave");
        }

        if(MappetVoice.needInArm.get()){
            return null;
        }

        for(ItemStack Item : player.inventory.mainInventory){
            if(Item.getItem().getRegistryName() == MappetVoice.radio.getRegistryName() && Item.getTagCompound() != null && !Item.getTagCompound().getString("wave").equals("")){
                return Item.getTagCompound().getString("wave");
            }
        }
        return null;
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
        ClientData data = ClientData.get();
        if(data.getData() != null && data.getData().containsKey(name))
            return data.getData().get(name);
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


