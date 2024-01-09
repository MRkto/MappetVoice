package mrkto.mvoice.client.audio.interfaces;

import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public interface IAudioSystemManager {
    IAudioInput getInput();
    IAudioOutput getOutput();
    void processSound(byte[] data, BlockPos position, double posX, double posY, double posZ, float rotationPitch, float rotationYaw, double distance, String name);
    //используется для подгрузки библиотек и прочего
    boolean preInitiate();
    //используется для подгрузки неообходимого для полноценной работы запускается при заходе в мир
    boolean initiate();
    //нужен для очистки памяти вызывается при выходе из мира
    void terminate();
    //нужен что бы небыло утечек памяти при выходе из игры и т.д. запускается при выходе из игры
    void fullTerminate();
    //это нужно для получения клона объекта что бы не возникало проблемм с переиспользованием одного экземпляра
    IAudioSystemManager getNewInstance();
}
