package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import rockstar.client.MinecraftClientAccess;

public class FogNoiseTexture
implements MinecraftClientAccess {
    public static final float internalField0205 = 288.0f;
    public static final float internalField0206 = 0.25f;
    public static final float internalField1048 = 1152.0f;
    private NativeImageBackedTexture internalField0789;
    private volatile NativeImage internalField0771;
    private boolean internalField0277;

    public int internalMethod06321() {
        if (this.internalField0789 != null) {
            return rockstar.client.render.FramebufferCompat.glId(this.internalField0789.getGlTexture());
        }
        NativeImage nativeImage = this.internalField0771;
        if (nativeImage != null) {
            this.internalField0771 = null;
            this.internalField0789 = new NativeImageBackedTexture(() -> "Rockstar skin image", nativeImage);
            rockstar.client.render.TextureCompat.setFilter(this.internalField0789, true, false);
            return rockstar.client.render.FramebufferCompat.glId(this.internalField0789.getGlTexture());
        }
        if (!this.internalField0277) {
            this.internalField0277 = true;
            Thread thread = new Thread(this::internalMethod06322, "rockstar-fog-noise");
            thread.setDaemon(true);
            thread.start();
        }
        return 0;
    }

    private void internalMethod06322() {
        NativeImage nativeImage = new NativeImage(512, 512, false);
        for (int i = 0; i < 512; ++i) {
            for (int j = 0; j < 512; ++j) {
                float f;
                float f2 = 0.0f;
                float f3 = 0.0f;
                float f4 = 0.0f;
                float f5 = 0.6f;
                int n = 16;
                for (int k = 0; k < 4; ++k) {
                    f = FogNoiseTexture.internalMethod00666(j, i, n, k * 131);
                    f2 += f * f5;
                    f3 += (k < 2 ? f : 0.5f) * f5;
                    f4 += f5;
                    f5 *= 0.45f;
                    n <<= 1;
                }
                float f6 = 0.0f;
                f = 0.0f;
                f5 = 0.6f;
                n = 16;
                for (int k = 0; k < 3; ++k) {
                    f6 += FogNoiseTexture.internalMethod00666(j, i, n, 7919 + k * 131) * f5;
                    f += f5;
                    f5 *= 0.45f;
                    n <<= 1;
                }
                nativeImage.setColorArgb(j, i, 0xFF000000 | FogNoiseTexture.internalMethod07046(f2 / f4) << 16 | FogNoiseTexture.internalMethod07046(f3 / f4) << 8 | FogNoiseTexture.internalMethod07046(f6 / f));
            }
        }
        this.internalField0771 = nativeImage;
    }

    private static float internalMethod00666(int n, int n2, int n3, int n4) {
        float f = (float)(n * n3) / 512.0f;
        float f2 = (float)(n2 * n3) / 512.0f;
        int n5 = (int)Math.floor(f);
        int n6 = (int)Math.floor(f2);
        float f3 = FogNoiseTexture.internalMethod07045(f - (float)n5);
        float f4 = FogNoiseTexture.internalMethod07045(f2 - (float)n6);
        int n7 = n3 - 1;
        int n8 = n5 & n7;
        int n9 = n5 + 1 & n7;
        int n10 = n6 & n7;
        int n11 = n6 + 1 & n7;
        float f5 = FogNoiseTexture.internalMethod05396(FogNoiseTexture.internalMethod05403(n8, n10, n4), FogNoiseTexture.internalMethod05403(n9, n10, n4), f3);
        float f6 = FogNoiseTexture.internalMethod05396(FogNoiseTexture.internalMethod05403(n8, n11, n4), FogNoiseTexture.internalMethod05403(n9, n11, n4), f3);
        return FogNoiseTexture.internalMethod05396(f5, f6, f4);
    }

    private static float internalMethod05403(int n, int n2, int n3) {
        int n4 = n * 374761393 + n2 * 668265263 + n3 * 1274126177;
        n4 = (n4 ^ n4 >>> 13) * 1274126177;
        n4 ^= n4 >>> 16;
        return (float)(n4 & 0xFFFFFF) / 1.6777215E7f;
    }

    private static float internalMethod07045(float f) {
        return f * f * (3.0f - 2.0f * f);
    }

    private static float internalMethod05396(float f, float f2, float f3) {
        return f + (f2 - f) * f3;
    }

    private static int internalMethod07046(float f) {
        return Math.max(0, Math.min(255, Math.round(f * 255.0f)));
    }
}
