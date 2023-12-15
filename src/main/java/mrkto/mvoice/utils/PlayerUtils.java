package mrkto.mvoice.utils;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.minecraft.MinecraftProfileTexture;
import mrkto.mvoice.MappetVoice;
import mrkto.mvoice.capability.Profile;
import mrkto.mvoice.client.ClientData;
import mrkto.mvoice.client.gui.VoiceMclibPanel.GuiPlayerListElement;
import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.DefaultPlayerSkin;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntitySkull;
import net.minecraft.util.ResourceLocation;
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
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerUtils {
    public static boolean setWave(EntityPlayerMP player, String wave){
        Profile.get(player).setWave(wave);
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
    public static String getWave(EntityPlayer player){

        if(!MappetVoice.radioItem.get()){
            return Profile.get(player).getWave();
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
    public static void getSkinlink(String player){
        new Thread(() -> {

            String json = readJsonFromUrl("http://skinsystem.ely.by/textures/" + player);
            Gson gson = new Gson();
            if (json != null) {
                objectUrl1 objectUrl = gson.fromJson(json, objectUrl1.class);
                GuiPlayerListElement.skins.put(player, objectUrl.SKIN.url);
            } else {
                json = readJsonFromUrl("https://auth.tlauncher.org/skin/profile/texture/login/" + player);
                objectUrl1 objectUrl = gson.fromJson(json, objectUrl1.class);
                GuiPlayerListElement.skins.put(player, objectUrl.SKIN.url);

            }
            GameProfile profile = null;

            for(NetworkPlayerInfo networkplayerinfo : Minecraft.getMinecraft().getConnection().getPlayerInfoMap()){
                if(!networkplayerinfo.getGameProfile().getName().equals(player))
                    continue;
                profile = networkplayerinfo.getGameProfile();
            }
            if(profile != null){
                GuiPlayerListElement.skins.put(player, getSkin(profile));
            }
        }
        ).start();
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
    public static String getSkin(GameProfile profile) {
        Minecraft minecraft = Minecraft.getMinecraft();
        Map<MinecraftProfileTexture.Type, MinecraftProfileTexture> map = minecraft.getSkinManager().loadSkinFromCache(profile);

        if (map.containsKey(MinecraftProfileTexture.Type.SKIN)) {
            return minecraft.getSkinManager().loadSkin(map.get(MinecraftProfileTexture.Type.SKIN), MinecraftProfileTexture.Type.SKIN).toString();
        }
        return "minecraft:textures/entity/steve.png";
    }


    static class objectUrl1 {
        public objectSkin SKIN;
        static class objectSkin {
            public String url;
        }
    }
}


