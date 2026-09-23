package rockstar.client.internal.render;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import java.nio.Buffer;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;
import org.lwjgl.opengl.GL11;
import org.lwjgl.system.MemoryUtil;
import rockstar.client.internal.core.GlyphSpanIndex;
import rockstar.client.internal.core.GlyphGeometryBuffer;
import rockstar.client.internal.core.GlPixelStoreUtils;

public final class FontGlyphAtlas {
    private static final FontGlyphAtlas internalField0655 = new FontGlyphAtlas();
    private final GlyphGeometryBuffer internalField0654 = new GlyphGeometryBuffer();
    private int internalField0227;
    private int internalField0228;
    private int internalField1053;
    private int internalField1055;
    private int internalField1056;
    private int internalField1054;
    private boolean internalField0277;

    private FontGlyphAtlas() {
    }

    public static FontGlyphAtlas internalMethod05614() {
        return internalField0655;
    }

    public synchronized int internalMethod02482(GlyphSpanIndex typedValue024) {
        int n = this.internalField0654.internalMethod01811(typedValue024);
        this.internalField0277 = true;
        return n;
    }

    public int internalMethod04784() {
        return this.internalField0227;
    }

    public int internalMethod04788() {
        return this.internalField0228;
    }

    public int internalMethod08779() {
        return this.internalField1053;
    }

    public synchronized void internalMethod04785() {
        if (!this.internalField0277 || !RenderSystem.isOnRenderThread()) {
            return;
        }
        this.internalField0277 = false;
        int n = GlStateManager._getInteger((int)32873);
        this.internalField0227 = this.internalMethod06389(this.internalField0227, this.internalField1055, this.internalField0654.internalMethod02932(), 4096, 34842, this.internalField0654.internalMethod00637(), this.internalField0654.internalMethod07842(), this.internalField0654.internalMethod02931(), 4);
        this.internalField1055 = this.internalField0654.internalMethod02932();
        this.internalField0228 = this.internalMethod04258(this.internalField0228, this.internalField1056, this.internalField0654.internalMethod07828(), this.internalField0654.internalMethod00638(), this.internalField0654.internalMethod07843(), this.internalField0654.internalMethod02933());
        this.internalField1056 = this.internalField0654.internalMethod07828();
        this.internalField1053 = this.internalMethod06389(this.internalField1053, this.internalField1054, this.internalField0654.internalMethod07831(), 2048, 34836, this.internalField0654.internalMethod00698(), this.internalField0654.internalMethod09207(), this.internalField0654.internalMethod07829(), 4);
        this.internalField1054 = this.internalField0654.internalMethod07831();
        this.internalField0654.internalMethod02930();
        GlStateManager._bindTexture((int)n);
    }

    private int internalMethod06389(int n, int n2, int n3, int n4, int n5, float[] fArray, int n6, boolean bl, int n7) {
        boolean bl2;
        if (n6 <= 0 && n != 0 && !bl) {
            return n;
        }
        boolean bl3 = bl2 = n == 0 || bl || n2 != n3;
        if (n == 0) {
            n = GlStateManager._genTexture();
        }
        GlStateManager._bindTexture((int)n);
        GlPixelStoreUtils.internalMethod07623();
        if (bl2) {
            this.internalMethod04789();
            FloatBuffer floatBuffer = MemoryUtil.memAllocFloat((int)(n4 * n3 * n7));
            floatBuffer.put(fArray, 0, Math.min(fArray.length, floatBuffer.remaining()));
            while (floatBuffer.hasRemaining()) {
                floatBuffer.put(0.0f);
            }
            floatBuffer.flip();
            GL11.glTexImage2D((int)3553, (int)0, (int)n5, (int)n4, (int)n3, (int)0, (int)6408, (int)5126, (FloatBuffer)floatBuffer);
            MemoryUtil.memFree((Buffer)floatBuffer);
            return n;
        }
        int n8 = Math.min(n6, n3);
        if ((n8 = Math.min(n8, fArray.length / (n4 * n7))) <= 0) {
            return n;
        }
        FloatBuffer floatBuffer = MemoryUtil.memAllocFloat((int)(n4 * n8 * n7));
        floatBuffer.put(fArray, 0, n4 * n8 * n7).flip();
        GL11.glTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)n4, (int)n8, (int)6408, (int)5126, (FloatBuffer)floatBuffer);
        MemoryUtil.memFree((Buffer)floatBuffer);
        return n;
    }

    private int internalMethod04258(int n, int n2, int n3, int[] nArray, int n4, boolean bl) {
        int n5;
        boolean bl2;
        if (n4 <= 0 && n != 0 && !bl) {
            return n;
        }
        boolean bl3 = bl2 = n == 0 || bl || n2 != n3;
        if (n == 0) {
            n = GlStateManager._genTexture();
        }
        GlStateManager._bindTexture((int)n);
        GlPixelStoreUtils.internalMethod07623();
        int n6 = n5 = bl2 ? n3 : Math.min(n4, n3);
        if (n5 <= 0) {
            return n;
        }
        ShortBuffer shortBuffer = MemoryUtil.memAllocShort((int)(4096 * n5 * 2));
        int n7 = Math.min(nArray.length, 4096 * n5 * 2);
        for (int i = 0; i < n7; ++i) {
            shortBuffer.put((short)nArray[i]);
        }
        while (shortBuffer.hasRemaining()) {
            shortBuffer.put((short)0);
        }
        shortBuffer.flip();
        if (bl2) {
            this.internalMethod04789();
            GL11.glTexImage2D((int)3553, (int)0, (int)33338, (int)4096, (int)n3, (int)0, (int)33320, (int)5123, (ShortBuffer)shortBuffer);
        } else {
            GL11.glTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)4096, (int)n5, (int)33320, (int)5123, (ShortBuffer)shortBuffer);
        }
        MemoryUtil.memFree((Buffer)shortBuffer);
        return n;
    }

    private void internalMethod04789() {
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        GL11.glTexParameteri((int)3553, (int)33084, (int)0);
        GL11.glTexParameteri((int)3553, (int)33085, (int)0);
    }
}

