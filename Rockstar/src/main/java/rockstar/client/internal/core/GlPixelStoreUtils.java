package rockstar.client.internal.core;


import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import org.lwjgl.opengl.GL15;

public final class GlPixelStoreUtils {
    private GlPixelStoreUtils() {
    }

    public static void internalMethod07619() {
        GL15.glBindBuffer((int)35051, (int)0);
        GlStateManager._pixelStore((int)3333, (int)1);
        GlStateManager._pixelStore((int)3330, (int)0);
        GlStateManager._pixelStore((int)3331, (int)0);
        GlStateManager._pixelStore((int)3332, (int)0);
    }

    public static void internalMethod07623() {
        GL15.glBindBuffer((int)35052, (int)0);
        GlStateManager._pixelStore((int)3317, (int)1);
        GlStateManager._pixelStore((int)3314, (int)0);
        GlStateManager._pixelStore((int)3315, (int)0);
        GlStateManager._pixelStore((int)3316, (int)0);
    }
}

