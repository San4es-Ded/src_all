package rockstar.client.internal.render;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import java.nio.ByteBuffer;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.client.texture.AbstractTexture;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.internal.core.GlPixelStoreUtils;

public class ItemIconAtlas
implements MinecraftClientAccess {
    private NativeImage internalField0771;
    private NativeImageBackedTexture internalField0789;
    private final Map<Identifier, Integer> internalField0543 = new HashMap<Identifier, Integer>();
    private int internalField1055 = 0;
    private boolean internalField0277 = false;
    private boolean internalField0276 = false;

    private void internalMethod01279() {
        if (this.internalField0789 == null) {
            this.internalField0771 = new NativeImage(128, 128, true);
            this.internalField0789 = new NativeImageBackedTexture(() -> "Rockstar render image", this.internalField0771);
            rockstar.client.render.TextureCompat.setFilter(this.internalField0789, false, false);
        }
    }

    public int internalMethod05441(Identifier identifier) {
        this.internalMethod01279();
        Integer n = this.internalField0543.get(identifier);
        if (n != null) {
            return n;
        }
        if (this.internalField0543.size() >= 256) {
            this.internalField0543.clear();
            this.internalField1055 = 0;
        }
        if (!this.internalMethod01956(identifier, n = Integer.valueOf(this.internalField1055++))) {
            this.internalField0543.remove(identifier);
            --this.internalField1055;
            return -1;
        }
        this.internalField0543.put(identifier, n);
        this.internalField0277 = true;
        return n;
    }

    public void internalMethod01277() {
        if (this.internalField0789 == null) {
            return;
        }
        if (this.internalField0277 || !this.internalField0276) {
            this.internalField0789.upload();
            this.internalField0277 = false;
            this.internalField0276 = true;
        }
    }

    public int internalMethod01276() {
        this.internalMethod01279();
        return rockstar.client.render.FramebufferCompat.glId(this.internalField0789.getGlTexture());
    }

    public float internalMethod03360(int n) {
        return (float)(n % 16 * 8) / 128.0f;
    }

    public float internalMethod03436(int n) {
        return (float)(n / 16 * 8) / 128.0f;
    }

    public float internalMethod01275() {
        return 0.0625f;
    }

    private boolean internalMethod01956(Identifier identifier, int n) {
        AbstractTexture abstractTexture = internalField0149.getTextureManager().getTexture(identifier);
        int n2 = rockstar.client.render.FramebufferCompat.glId(abstractTexture.getGlTexture());
        if (n2 <= 0) {
            return false;
        }
        GlStateManager._bindTexture((int)n2);
        int n3 = GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4096);
        int n4 = GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4097);
        if (n3 < 64 || n4 < 64) {
            return false;
        }
        ByteBuffer byteBuffer = BufferUtils.createByteBuffer((int)(n3 * n4 * 4));
        GlPixelStoreUtils.internalMethod07619();
        GL11.glGetTexImage((int)3553, (int)0, (int)6408, (int)5121, (ByteBuffer)byteBuffer);
        int n5 = n % 16 * 8;
        int n6 = n / 16 * 8;
        for (int i = 0; i < 8; ++i) {
            for (int j = 0; j < 8; ++j) {
                int n7 = this.internalMethod00661(byteBuffer, n3, 8 + j, 8 + i);
                int n8 = this.internalMethod00661(byteBuffer, n3, 40 + j, 8 + i);
                this.internalField0771.setColorArgb(n5 + j, n6 + i, this.internalMethod05793(n8, n7));
            }
        }
        return true;
    }

    private int internalMethod00661(ByteBuffer byteBuffer, int n, int n2, int n3) {
        int n4 = (n3 * n + n2) * 4;
        int n5 = byteBuffer.get(n4) & 0xFF;
        int n6 = byteBuffer.get(n4 + 1) & 0xFF;
        int n7 = byteBuffer.get(n4 + 2) & 0xFF;
        int n8 = byteBuffer.get(n4 + 3) & 0xFF;
        return n8 << 24 | n5 << 16 | n6 << 8 | n7;
    }

    private int internalMethod05793(int n, int n2) {
        float f = (float)(n >> 24 & 0xFF) / 255.0f;
        float f2 = (float)(n2 >> 24 & 0xFF) / 255.0f;
        float f3 = f + f2 * (1.0f - f);
        if (f3 <= 0.0f) {
            return 0;
        }
        int n3 = n >> 16 & 0xFF;
        int n4 = n >> 8 & 0xFF;
        int n5 = n & 0xFF;
        int n6 = n2 >> 16 & 0xFF;
        int n7 = n2 >> 8 & 0xFF;
        int n8 = n2 & 0xFF;
        int n9 = Math.round(((float)n3 * f + (float)n6 * f2 * (1.0f - f)) / f3);
        int n10 = Math.round(((float)n4 * f + (float)n7 * f2 * (1.0f - f)) / f3);
        int n11 = Math.round(((float)n5 * f + (float)n8 * f2 * (1.0f - f)) / f3);
        int n12 = Math.round(f3 * 255.0f);
        return n12 << 24 | n9 << 16 | n10 << 8 | n11;
    }
}
