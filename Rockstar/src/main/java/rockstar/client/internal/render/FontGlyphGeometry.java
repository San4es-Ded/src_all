package rockstar.client.internal.render;


import rockstar.client.*;
import net.minecraft.client.render.VertexConsumer;
import org.joml.Matrix4f;

public final class FontGlyphGeometry {
    private final int internalField0227;
    private final int internalField0228;
    private final float internalField0206;
    private final float internalField1048;
    private final float internalField1047;
    private final float internalField1049;
    private final float internalField1046;

    public FontGlyphGeometry(int n, int n2, float f, float f2, float f3, float f4, float f5) {
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField0206 = f;
        this.internalField1048 = f2;
        this.internalField1046 = f3;
        this.internalField1047 = f4;
        this.internalField1049 = f5;
    }

    public int internalMethod03043() {
        return this.internalField0227;
    }

    public int internalMethod03049() {
        return this.internalField0228;
    }

    public float internalMethod03042() {
        return this.internalField0206;
    }

    public float internalMethod03048() {
        return this.internalField1049;
    }

    public float internalMethod08530() {
        return this.internalField1046;
    }

    public float internalMethod08533() {
        return this.internalField1048;
    }

    public float internalMethod08547() {
        return this.internalField1047;
    }

    public boolean internalMethod03044() {
        return this.internalField0228 < 0;
    }

    public float internalMethod04431(float f) {
        return this.internalField0206 * f;
    }

    public float internalMethod02108(float f, float f2, float f3) {
        return f + this.internalMethod06074(f2, f3) * f2;
    }

    public float internalMethod07031(float f, float f2, float f3) {
        return f - this.internalMethod07839(f2, f3) * f2;
    }

    public static float internalMethod05649(Matrix4f matrix4f) {
        float f = matrix4f.m00() * matrix4f.m11() - matrix4f.m01() * matrix4f.m10();
        float f2 = (float)Math.sqrt(Math.abs(f));
        return f2 > 1.0E-4f ? f2 : 1.0f;
    }

    public float internalMethod04505(float f) {
        return this.internalMethod04355(f, 1.0f);
    }

    public float internalMethod04355(float f, float f2) {
        return 1.0f / (f * f2);
    }

    public float internalMethod06074(float f, float f2) {
        return this.internalField1048 - this.internalMethod04355(f, f2);
    }

    public float internalMethod08420(float f, float f2) {
        return this.internalField1047 + this.internalMethod04355(f, f2);
    }

    public float internalMethod08772(float f, float f2) {
        return this.internalField1046 - this.internalMethod04355(f, f2);
    }

    public float internalMethod07839(float f, float f2) {
        return this.internalField1049 + this.internalMethod04355(f, f2);
    }

    public float internalMethod08165(float f, float f2) {
        return (this.internalMethod08420(f, f2) - this.internalMethod06074(f, f2)) * f;
    }

    public float internalMethod09575(float f, float f2) {
        return (this.internalMethod07839(f, f2) - this.internalMethod08772(f, f2)) * f;
    }

    public float internalMethod00174(Matrix4f matrix4f, VertexConsumer vertexConsumer, float f, float f2, float f3, float f4, int n) {
        return this.internalMethod03374(matrix4f, vertexConsumer, f, f2, f3, f4, n, 1.0f);
    }

    public float internalMethod03374(Matrix4f matrix4f, VertexConsumer vertexConsumer, float f, float f2, float f3, float f4, int n, float f5) {
        if (this.internalField0228 >= 0) {
            float f6 = Math.max(1.0f, f5) / (f * FontGlyphGeometry.internalMethod05649(matrix4f));
            float f7 = this.internalField1048 - f6;
            float f8 = this.internalField1047 + f6;
            float f9 = this.internalField1049 + f6;
            float f10 = this.internalField1046 - f6;
            float f11 = f2 + f7 * f;
            float f12 = f2 + f8 * f;
            float f13 = f3 - f9 * f;
            float f14 = f3 - f10 * f;
            vertexConsumer.vertex(matrix4f, f11, f13, f4).texture(f7, f9).color(n).light(this.internalMethod08531());
            vertexConsumer.vertex(matrix4f, f11, f14, f4).texture(f7, f10).color(n).light(this.internalMethod08531());
            vertexConsumer.vertex(matrix4f, f12, f14, f4).texture(f8, f10).color(n).light(this.internalMethod08531());
            vertexConsumer.vertex(matrix4f, f12, f13, f4).texture(f8, f9).color(n).light(this.internalMethod08531());
        }
        return this.internalField0206 * f;
    }

    public void internalMethod01939(Matrix4f matrix4f, VertexConsumer vertexConsumer, float f, float f2, float f3, int n) {
        if (this.internalField0228 < 0) {
            return;
        }
        float f4 = 1.0f / (f3 * FontGlyphGeometry.internalMethod05649(matrix4f));
        float f5 = this.internalField1048 - f4;
        float f6 = this.internalField1047 + f4;
        float f7 = this.internalField1049 + f4;
        float f8 = this.internalField1046 - f4;
        float f9 = f + f5 * f3;
        float f10 = f + f6 * f3;
        float f11 = f2 + (1.0f - f7) * f3;
        float f12 = f2 + (1.0f - f8) * f3;
        vertexConsumer.vertex(matrix4f, f9, f11, 0.0f).texture(f5, f7).color(n).light(this.internalMethod08531());
        vertexConsumer.vertex(matrix4f, f9, f12, 0.0f).texture(f5, f8).color(n).light(this.internalMethod08531());
        vertexConsumer.vertex(matrix4f, f10, f12, 0.0f).texture(f6, f8).color(n).light(this.internalMethod08531());
        vertexConsumer.vertex(matrix4f, f10, f11, 0.0f).texture(f6, f7).color(n).light(this.internalMethod08531());
    }

    public int internalMethod08531() {
        int n = this.internalField0228 + 1;
        return n & Short.MAX_VALUE | n >> 15 << 16;
    }
}

