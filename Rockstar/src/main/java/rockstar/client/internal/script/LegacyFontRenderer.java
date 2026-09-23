package rockstar.client.internal.script;





import rockstar.client.render.*;
import rockstar.client.internal.render.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.mojang.blaze3d.opengl.GlStateManager;
import rockstar.client.compat.RenderSystem;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import rockstar.client.compat.ShaderProgram;
import net.minecraft.client.render.BufferBuilder;
import rockstar.client.compat.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import rockstar.client.render.CornerRadii;
import rockstar.client.render.FontFamily;
import rockstar.client.internal.render.FontGlyphGeometry;
import rockstar.client.internal.render.SlugFontRenderer;
import rockstar.client.internal.render.FontGlyphAtlas;
import rockstar.client.render.RenderPipeline;
import rockstar.client.internal.core.GlPixelStoreUtils;
import rockstar.client.render.ScissorStack;
import rockstar.client.render.UiBatchRenderer;

public final class LegacyFontRenderer
implements FontFamily.InternalType0232 {
    private static final float[] internalField0615 = new float[]{0.0f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, -4096.0f, -4096.0f, -4096.0f, -4096.0f, -2.0f, -2.0f, -2.0f, -2.0f, 0.0f, -4096.0f, 0.0f, -16.0f, -16.0f, -16.0f, -16.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f};
    private static final float[] internalField0616 = new float[]{8.0f, 16.0f, 32768.0f, 1.0f, 4096.0f, 4096.0f, 64.0f, 64.0f, 512.0f, 512.0f, 512.0f, 512.0f, 64.0f, 64.0f, 64.0f, 1.0f, 4096.0f, 4096.0f, 4096.0f, 4096.0f, 2.0f, 2.0f, 2.0f, 2.0f, 1.0f, 4096.0f, 4096.0f, 16.0f, 16.0f, 16.0f, 16.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f};
    private float[] internalField1238 = new float[12288];
    private float[] internalField1240 = new float[3072];
    private int[] internalField0618 = new int[1024];
    private int internalField1464;
    private final int[] internalField0617 = new int[8];
    private int internalField1470;
    private float[] internalField1239 = new float[32];
    private int internalField1465;
    private float internalField1458 = -4096.0f;
    private float internalField1459 = -4096.0f;
    private float internalField1460 = 4096.0f;
    private float internalField1461 = 4096.0f;
    private final int[] internalField1241 = new int[3];
    private int internalField1463 = -1;
    private int internalField1466;
    private ShortBuffer internalField0890;
    private boolean internalField0277 = true;
    private Matrix4f internalField0788;
    private float internalField1462 = 1.0f;
    private float internalField1455;
    private float internalField1723;
    private float internalField1731;
    private float internalField1727;
    private float internalField1728;
    private float internalField1717;
    private float internalField1718;
    private float internalField1719;
    private float internalField1721;

    public LegacyFontRenderer() {
    }

    public boolean internalMethod03872() {
        return this.internalField1464 > 0;
    }

    public boolean internalMethod03877() {
        if (!this.internalField0277) {
            return false;
        }
        RenderSystem.assertOnRenderThread();
        if (RenderPipeline.internalField0995 == null || RenderPipeline.internalField0995.internalMethod01220() == null) {
            return false;
        }
        return this.internalMethod03352(1);
    }

    public void internalMethod03871() {
        this.internalField1464 = 0;
        this.internalField1470 = 0;
        this.internalField1465 = 0;
        this.internalField1458 = -4096.0f;
        this.internalField1459 = -4096.0f;
        this.internalField1460 = 4096.0f;
        this.internalField1461 = 4096.0f;
    }

    public void internalMethod00882(float f, float f2, float f3, float f4) {
        this.internalMethod03428((this.internalField1465 + 1) * 4);
        int n = this.internalField1465++ * 4;
        this.internalField1239[n] = this.internalField1458;
        this.internalField1239[n + 1] = this.internalField1459;
        this.internalField1239[n + 2] = this.internalField1460;
        this.internalField1239[n + 3] = this.internalField1461;
        this.internalField1458 = f;
        this.internalField1459 = f2;
        this.internalField1460 = f + f3;
        this.internalField1461 = f2 + f4;
    }

    public void internalMethod03876() {
        if (this.internalField1465 <= 0) {
            return;
        }
        int n = --this.internalField1465 * 4;
        this.internalField1458 = this.internalField1239[n];
        this.internalField1459 = this.internalField1239[n + 1];
        this.internalField1460 = this.internalField1239[n + 2];
        this.internalField1461 = this.internalField1239[n + 3];
    }

    public void internalMethod02918(Matrix4f matrix4f, float f, float f2, float f3, float f4, int n, int n2, int n3, int n4) {
        int n5 = this.internalMethod02917(matrix4f, f, f2, f3, f4, n, n2, n3, n4);
        this.internalMethod00078(n5, 0, 0.0f);
    }

    public void internalMethod05080(Matrix4f matrix4f, float f, float f2, float f3, float f4, CornerRadii typedParameter1014, float f5, float f6, int n, int n2, int n3, int n4) {
        float f7 = f5 * 1.5f;
        int n5 = this.internalMethod02917(matrix4f, f - f7 * 0.5f, f2 - f7 * 0.5f, f3 + f7, f4 + f7, n, n2, n3, n4);
        this.internalMethod07457(n5, 1.0f, f3, f4, typedParameter1014, f5, f6);
    }

    public void internalMethod07386(Matrix4f matrix4f, float f, float f2, float f3, float f4, CornerRadii typedParameter1014, float f5, float f6, int n, int n2, int n3, int n4, boolean bl) {
        this.internalMethod05080(matrix4f, f, f2, f3, f4, typedParameter1014, f5, f6, n, n2, n3, n4);
        int n5 = this.internalField1464 - 1;
        if (bl) {
            this.internalMethod00078(n5, 15, 1.0f);
            this.internalMethod00079(n5, 32, n);
            this.internalMethod00079(n5, 36, n2);
            this.internalMethod00079(n5, 40, n3);
            this.internalMethod00079(n5, 44, n4);
        }
    }

    public void internalMethod05986(Matrix4f matrix4f, float f, float f2, float f3, float f4, CornerRadii typedParameter1014, float f5, float f6, float f7, float f8, int n) {
        float f9 = f7 * 1.5f;
        int n2 = this.internalMethod02917(matrix4f, f - f9 * 0.5f, f2 - f9 * 0.5f, f3 + f9, f4 + f9, n, n, n, n);
        this.internalMethod07457(n2, 2.0f, f3, f4, typedParameter1014, f7, f8);
        this.internalMethod00078(n2, 12, f5);
        this.internalMethod00078(n2, 13, f6);
        this.internalMethod00078(n2, 14, f7);
    }

    public void internalMethod06943(Matrix4f matrix4f, int n, float f, float f2, float f3, float f4, float f5, float f6, CornerRadii typedParameter1014, float f7, float f8, float f9, float f10, float f11, float f12, int n2) {
        int n3 = this.internalMethod03350(n);
        int n4 = this.internalMethod02917(matrix4f, f, f2, f3, f4, n2, n2, n2, n2);
        this.internalMethod07457(n4, 4.0f, f5, f6, typedParameter1014, f7, f8);
        this.internalMethod00078(n4, 1, n3);
        this.internalMethod05019(n4, f9, f10, f11, f12);
    }

    public void internalMethod04147(Matrix4f matrix4f, int n, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, int n2) {
        int n3 = this.internalMethod03350(n);
        int n4 = this.internalMethod02917(matrix4f, f, f2, f3, f4, n2, n2, n2, n2);
        this.internalMethod00078(n4, 0, 5.0f);
        this.internalMethod00078(n4, 1, n3);
        this.internalMethod05019(n4, f5, f6, f7, f8);
    }

    public void internalMethod01449(Matrix4f matrix4f, FontFamily typedValue022, String string, float f, float f2, float f3, float f4, int n, float f5, float f6, boolean bl, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.internalField0788 = matrix4f;
        this.internalField1462 = FontGlyphGeometry.internalMethod05649(matrix4f);
        this.internalField1455 = f5;
        this.internalField1723 = f6;
        this.internalField1731 = bl ? 1.0f : 0.0f;
        this.internalField1727 = this.internalMethod05910(matrix4f, f7, f3, f4);
        this.internalField1728 = f8;
        this.internalField1717 = f9;
        this.internalField1718 = f10;
        boolean bl2 = f12 > 0.001f;
        this.internalField1719 = bl2 ? f11 : -1.0f;
        this.internalField1721 = bl2 ? f12 : -1.0f;
        typedValue022.internalMethod03437(string, f, f2, f3, f4, n, this);
    }

    public void internalMethod06400(Matrix4f matrix4f, FontFamily typedValue022, int n, float f, float f2, float f3, int n2) {
        FontGlyphGeometry typedValue023 = typedValue022.internalMethod02359(n);
        if (typedValue023 == null || typedValue023.internalMethod03044()) {
            return;
        }
        float f4 = FontGlyphGeometry.internalMethod05649(matrix4f);
        float f5 = typedValue023.internalMethod06074(f3, f4);
        float f6 = typedValue023.internalMethod08420(f3, f4);
        float f7 = typedValue023.internalMethod08772(f3, f4);
        float f8 = typedValue023.internalMethod07839(f3, f4);
        float f9 = f + f5 * f3;
        float f10 = f2 + (1.0f - f8) * f3;
        int n3 = this.internalMethod02917(matrix4f, f9, f10, (f6 - f5) * f3, (f8 - f7) * f3, n2, n2, n2, n2);
        this.internalMethod00078(n3, 0, 3.0f);
        this.internalMethod00078(n3, 2, typedValue023.internalMethod03049() + 1);
        this.internalMethod00078(n3, 3, 0.0f);
        this.internalMethod00078(n3, 7, 0.5f);
        this.internalMethod05019(n3, f5, f8, f6, f7);
    }

    @Override
    public void internalMethod02648(FontGlyphGeometry typedValue023, float f, float f2, float f3, float f4, int n) {
        if (typedValue023.internalMethod03044()) {
            return;
        }
        float f5 = typedValue023.internalMethod02108(f2, f, this.internalField1462);
        float f6 = typedValue023.internalMethod07031(f3, f, this.internalField1462);
        int n2 = this.internalMethod02917(this.internalField0788, f5, f6, typedValue023.internalMethod08165(f, this.internalField1462), typedValue023.internalMethod09575(f, this.internalField1462), n, n, n, n);
        this.internalMethod00078(n2, 0, 3.0f);
        this.internalMethod00078(n2, 2, typedValue023.internalMethod03049() + 1);
        this.internalMethod00078(n2, 3, this.internalField1455);
        this.internalMethod00078(n2, 7, this.internalField1723);
        this.internalMethod05019(n2, typedValue023.internalMethod06074(f, this.internalField1462), typedValue023.internalMethod07839(f, this.internalField1462), typedValue023.internalMethod08420(f, this.internalField1462), typedValue023.internalMethod08772(f, this.internalField1462));
        this.internalMethod00078(n2, 24, this.internalField1731);
        this.internalMethod00078(n2, 25, this.internalField1727);
        this.internalMethod00078(n2, 26, this.internalField1728);
        this.internalMethod00078(n2, 27, this.internalField1717);
        this.internalMethod00078(n2, 28, this.internalField1718);
        this.internalMethod00078(n2, 29, this.internalField1719);
        this.internalMethod00078(n2, 30, this.internalField1721);
    }

    public void internalMethod08347() {
        int n;
        ShaderProgram shaderProgram;
        int n2;
        int n3;
        if (this.internalField1464 == 0) {
            return;
        }
        RenderSystem.assertOnRenderThread();
        ScissorStack.internalMethod09075();
        if (!this.internalMethod03352(this.internalField1464)) {
            this.internalField1464 = 0;
            this.internalField1470 = 0;
            return;
        }
        this.internalField0890.clear();
        int n4 = this.internalField1464 * 48;
        for (n3 = 0; n3 < n4; ++n3) {
            n2 = n3 % 48;
            this.internalField0890.put(this.internalMethod00070(this.internalField1238[n3], internalField0615[n2], internalField0616[n2]));
        }
        this.internalField0890.flip();
        n3 = this.internalMethod03870();
        GlStateManager._activeTexture((int)33984);
        GlStateManager._bindTexture((int)n3);
        this.internalMethod08362();
        this.internalMethod08348();
        GL11.glTexSubImage2D((int)3553, (int)0, (int)0, (int)0, (int)12, (int)this.internalField1464, (int)6408, (int)5123, (ShortBuffer)this.internalField0890);
        n2 = GL11.glGetError();
        if (n2 != 0) {
            this.internalMethod02042("upload", n2);
            this.internalField1464 = 0;
            this.internalField1470 = 0;
            return;
        }
        float[] fArray = (float[])RenderSystem.getShaderColor().clone();
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        UiBatchRenderer.internalMethod08433();
        RenderSystem.disableCull();
        RenderSystem.setShaderTexture((int)0, (int)n3);
        SlugFontRenderer.internalMethod06418(9, 10, 11);
        for (int i = 0; i < 8; ++i) {
            RenderSystem.setShaderTexture((int)(i + 1), (int)(i < this.internalField1470 ? this.internalField0617[i] : 0));
        }
        ShaderProgram shaderProgram2 = shaderProgram = RenderPipeline.internalField0995 == null ? null : RenderPipeline.internalField0995.internalMethod01220();
        if (shaderProgram == null) {
            this.internalMethod02042("shader", 0);
            this.internalField1464 = 0;
            this.internalField1470 = 0;
            for (int i = 0; i <= 11; ++i) {
                RenderSystem.setShaderTexture((int)i, (int)0);
            }
            RenderSystem.enableCull();
            RenderSystem.disableBlend();
            RenderSystem.setShaderColor((float)fArray[0], (float)fArray[1], (float)fArray[2], (float)fArray[3]);
            return;
        }
        shaderProgram.addSamplerTexture("Sampler0", n3);
        FontGlyphAtlas internalValue0010 = FontGlyphAtlas.internalMethod05614();
        shaderProgram.addSamplerTexture("Sampler9", internalValue0010.internalMethod04784());
        shaderProgram.addSamplerTexture("Sampler10", internalValue0010.internalMethod04788());
        shaderProgram.addSamplerTexture("Sampler11", internalValue0010.internalMethod08779());
        for (int i = 0; i < 8; ++i) {
            shaderProgram.addSamplerTexture("Sampler" + (i + 1), i < this.internalField1470 ? this.internalField0617[i] : 0);
        }
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        for (int i = 0; i < this.internalField1464; ++i) {
            n = i * 12;
            int n5 = i * 4;
            float f = i;
            bufferBuilder.vertex(this.internalField1240[n], this.internalField1240[n + 1], this.internalField1240[n + 2]).texture(f, 0.0f).color(this.internalField0618[n5]);
            bufferBuilder.vertex(this.internalField1240[n + 3], this.internalField1240[n + 4], this.internalField1240[n + 5]).texture(f, 1.0f).color(this.internalField0618[n5 + 1]);
            bufferBuilder.vertex(this.internalField1240[n + 6], this.internalField1240[n + 7], this.internalField1240[n + 8]).texture(f, 2.0f).color(this.internalField0618[n5 + 2]);
            bufferBuilder.vertex(this.internalField1240[n + 9], this.internalField1240[n + 10], this.internalField1240[n + 11]).texture(f, 3.0f).color(this.internalField0618[n5 + 3]);
        }
        BuiltBuffer builtBuffer = bufferBuilder.endNullable();
        if (builtBuffer != null) {
            BufferRenderer.drawWithGlobalProgram((BuiltBuffer)builtBuffer);
        }
        for (n = 0; n <= 11; ++n) {
            RenderSystem.setShaderTexture((int)n, (int)0);
        }
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShaderColor((float)fArray[0], (float)fArray[1], (float)fArray[2], (float)fArray[3]);
        this.internalField1464 = 0;
        this.internalField1470 = 0;
    }

    private int internalMethod02917(Matrix4f matrix4f, float f, float f2, float f3, float f4, int n, int n2, int n3, int n4) {
        this.internalMethod03351(this.internalField1464 + 1);
        int n5 = this.internalField1464++;
        int n6 = n5 * 12;
        this.internalMethod02339(matrix4f, f, f2, 0.0f, n6);
        this.internalMethod02339(matrix4f, f, f2 + f4, 0.0f, n6 + 3);
        this.internalMethod02339(matrix4f, f + f3, f2 + f4, 0.0f, n6 + 6);
        this.internalMethod02339(matrix4f, f + f3, f2, 0.0f, n6 + 9);
        int n7 = n5 * 4;
        this.internalField0618[n7] = n;
        this.internalField0618[n7 + 1] = n2;
        this.internalField0618[n7 + 2] = n3;
        this.internalField0618[n7 + 3] = n4;
        int n8 = n5 * 48;
        Arrays.fill(this.internalField1238, n8, n8 + 48, 0.0f);
        this.internalMethod00078(n5, 16, this.internalField1458);
        this.internalMethod00078(n5, 17, this.internalField1459);
        this.internalMethod00078(n5, 18, this.internalField1460);
        this.internalMethod00078(n5, 19, this.internalField1461);
        return n5;
    }

    private void internalMethod07457(int n, float f, float f2, float f3, CornerRadii typedParameter1014, float f4, float f5) {
        this.internalMethod00078(n, 0, f);
        this.internalMethod00078(n, 4, f2);
        this.internalMethod00078(n, 5, f3);
        this.internalMethod00078(n, 6, f5);
        this.internalMethod00078(n, 7, f4);
        this.internalMethod00078(n, 8, typedParameter1014.internalMethod05337());
        this.internalMethod00078(n, 9, typedParameter1014.internalMethod08942());
        this.internalMethod00078(n, 10, typedParameter1014.internalMethod05340());
        this.internalMethod00078(n, 11, typedParameter1014.internalMethod08939());
    }

    private void internalMethod05019(int n, float f, float f2, float f3, float f4) {
        this.internalMethod00078(n, 20, f);
        this.internalMethod00078(n, 21, f2);
        this.internalMethod00078(n, 22, f3);
        this.internalMethod00078(n, 23, f4);
    }

    private void internalMethod00079(int n, int n2, int n3) {
        this.internalMethod00078(n, n2, (float)(n3 >>> 16 & 0xFF) / 255.0f);
        this.internalMethod00078(n, n2 + 1, (float)(n3 >>> 8 & 0xFF) / 255.0f);
        this.internalMethod00078(n, n2 + 2, (float)(n3 & 0xFF) / 255.0f);
        this.internalMethod00078(n, n2 + 3, (float)(n3 >>> 24 & 0xFF) / 255.0f);
    }

    private int internalMethod03350(int n) {
        for (int i = 0; i < this.internalField1470; ++i) {
            if (this.internalField0617[i] != n) continue;
            return i;
        }
        if (this.internalField1470 == 8) {
            this.internalMethod08347();
        }
        this.internalField0617[this.internalField1470] = n;
        return this.internalField1470++;
    }

    private void internalMethod00078(int n, int n2, float f) {
        this.internalField1238[n * 48 + n2] = f;
    }

    private short internalMethod00070(float f, float f2, float f3) {
        float f4 = (f - f2) / (f3 - f2);
        int n = Math.round(Math.max(0.0f, Math.min(1.0f, f4)) * 65535.0f);
        return (short)n;
    }

    private void internalMethod02339(Matrix4f matrix4f, float f, float f2, float f3, int n) {
        this.internalField1240[n] = matrix4f.m00() * f + matrix4f.m10() * f2 + matrix4f.m20() * f3 + matrix4f.m30();
        this.internalField1240[n + 1] = matrix4f.m01() * f + matrix4f.m11() * f2 + matrix4f.m21() * f3 + matrix4f.m31();
        this.internalField1240[n + 2] = matrix4f.m02() * f + matrix4f.m12() * f2 + matrix4f.m22() * f3 + matrix4f.m32();
    }

    private float internalMethod05910(Matrix4f matrix4f, float f, float f2, float f3) {
        return matrix4f.m00() * f + matrix4f.m10() * f2 + matrix4f.m20() * f3 + matrix4f.m30();
    }

    private void internalMethod03351(int n) {
        int n2 = this.internalField1238.length / 48;
        if (n <= n2) {
            return;
        }
        int n3 = Integer.highestOneBit(n - 1) << 1;
        this.internalField1238 = Arrays.copyOf(this.internalField1238, n3 * 48);
        this.internalField1240 = Arrays.copyOf(this.internalField1240, n3 * 12);
        this.internalField0618 = Arrays.copyOf(this.internalField0618, n3 * 4);
    }

    private void internalMethod03428(int n) {
        if (n <= this.internalField1239.length) {
            return;
        }
        this.internalField1239 = Arrays.copyOf(this.internalField1239, Integer.highestOneBit(n - 1) << 1);
    }

    private boolean internalMethod03352(int n) {
        if (!this.internalField0277) {
            return false;
        }
        if (this.internalField1241[0] == 0) {
            for (int i = 0; i < this.internalField1241.length; ++i) {
                this.internalField1241[i] = GL11.glGenTextures();
                this.internalMethod08787(this.internalField1241[i]);
            }
        }
        if (n > this.internalField1466) {
            this.internalMethod08348();
            this.internalField1466 = Integer.highestOneBit(n - 1) << 1;
            if (this.internalField1466 < 256) {
                this.internalField1466 = 256;
            }
            for (int n2 : this.internalField1241) {
                int n3;
                GlStateManager._activeTexture((int)33984);
                GlStateManager._bindTexture((int)n2);
                this.internalMethod08362();
                GL11.glTexImage2D((int)3553, (int)0, (int)32859, (int)12, (int)this.internalField1466, (int)0, (int)6408, (int)5123, (ByteBuffer)null);
                int n4 = GL11.glGetError();
                int n5 = n4 == 0 ? GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4096) : 0;
                int n6 = n3 = n4 == 0 ? GL11.glGetTexLevelParameteri((int)3553, (int)0, (int)4097) : 0;
                if (n4 == 0 && n5 == 12 && n3 == this.internalField1466) continue;
                this.internalMethod02042("allocation", n4);
                return false;
            }
            this.internalField0890 = BufferUtils.createShortBuffer((int)(this.internalField1466 * 48));
        }
        return true;
    }

    private void internalMethod08787(int n) {
        GlStateManager._activeTexture((int)33984);
        GlStateManager._bindTexture((int)n);
        GL11.glTexParameteri((int)3553, (int)10241, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10240, (int)9728);
        GL11.glTexParameteri((int)3553, (int)10242, (int)33071);
        GL11.glTexParameteri((int)3553, (int)10243, (int)33071);
        GL11.glTexParameteri((int)3553, (int)33084, (int)0);
        GL11.glTexParameteri((int)3553, (int)33085, (int)0);
    }

    private int internalMethod03870() {
        this.internalField1463 = (this.internalField1463 + 1) % this.internalField1241.length;
        return this.internalField1241[this.internalField1463];
    }

    private void internalMethod02042(String string, int n) {
        this.internalField0277 = false;
        System.err.println("[Rockstar] Low-draw UI batching disabled after command texture " + string + " failure (OpenGL error " + n + ").");
    }

    private void internalMethod08348() {
        while (GL11.glGetError() != 0) {
        }
    }

    private void internalMethod08362() {
        GlPixelStoreUtils.internalMethod07623();
    }
}

