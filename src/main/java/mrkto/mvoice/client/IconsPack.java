package mrkto.mvoice.client;

import com.google.common.collect.ImmutableSet;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;

public class IconsPack implements IResourcePack {
    private File folder;
    public IconsPack(File folder){
        this.folder = folder;
        folder.mkdir();
    }
    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        File file = new File(folder, location.getResourcePath());
        if(file.exists()){
            return new FileInputStream(file);
        }
        return null;
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        File file = new File(folder, location.getResourcePath());
        return file.exists();
    }

    @Override
    public Set<String> getResourceDomains() {
        return ImmutableSet.of("mvoice");
    }

    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        return null;
    }

    @Override
    public BufferedImage getPackImage() throws IOException {
        return null;
    }

    @Override
    public String getPackName() {
        return "mappet voice icons pack";
    }
}
