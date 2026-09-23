package haron.render;

import haron.render.ScissorStack;
import java.awt.Color;
import net.minecraft.client.util.Window;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;

public interface ShapeRenderer {
    public void b(float var1, float var2, float var3, float var4, float var5, float var6, Color var7, MatrixStack var8);

    public void b(float var1, float var2, float var3, float var4, MatrixStack var5);

    public ScissorStack b();

    default public void b(float f, float f2, float f3, float f4, float f5, Color color, MatrixStack matrixStack) {
        this.b(f, f2, f3, f4, 0.0f, f5, color, matrixStack);
    }

    public void c(float var1, float var2, float var3, float var4, MatrixStack var5);

    default public Color a(Color color, int n) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), MathHelper.clamp((int)n, (int)0, (int)255));
    }

    default public Color a(Color color, float f) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), MathHelper.clamp((int)((int)(f * 255.0f)), (int)0, (int)255));
    }

    default public void a(Identifier identifier, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, Color color, MatrixStack matrixStack, boolean bl) {
        this.a(identifier, f, f2, f3, f4, f5, f6, f7, f8, f9, color, matrixStack);
    }

    public void a(Identifier var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, float var10, Color var11, MatrixStack var12);

    public void a(float var1, float var2, float var3, float var4, float var5, float var6, MatrixStack var7);

    public void a(MatrixStack var1);

    default public void a(float f, float f2, float f3, float f4, float f5, MatrixStack matrixStack) {
        this.b(f, f2, f3, f4, matrixStack);
    }

    public void a(float var1, MatrixStack var2);

    public void a(float var1, float var2, float var3, float var4, float var5, float var6, Color var7, MatrixStack var8);

    public void a(float var1, float var2, float var3, Color var4, MatrixStack var5);

    public void a(float var1, float var2, float var3, float var4, float var5, float var6, float var7, float var8, Color var9, Color var10, Color var11, Color var12, MatrixStack var13);

    default public void a(float f, float f2, float f3, float f4, float f5, Color color, Color color2, Color color3, Color color4, MatrixStack matrixStack) {
        this.a(f, f2, f3, f4, f5, f5, f5, f5, color, color2, color3, color4, matrixStack);
    }

    public void a(float var1, float var2, float var3, float var4, float var5, Color var6, MatrixStack var7);

    default public void a(float f, float f2, float f3, float f4, Color color, MatrixStack matrixStack) {
        this.a(f, f2, f3, f4, 0.0f, color, matrixStack);
    }

    default public void a(Identifier identifier, float f, float f2, float f3, float f4, float f5, Color color, MatrixStack matrixStack) {
        this.a(identifier, f, f2, f3, f4, f5, 0.0f, 0.0f, 1.0f, 1.0f, color, matrixStack);
    }

    public void a(Identifier var1, float var2, float var3, float var4, float var5, Color var6, MatrixStack var7);

    public void a(int var1, float var2, float var3, float var4, float var5, Color var6, MatrixStack var7);

    public Window a();

    public void a(float var1, float var2, float var3, float var4, MatrixStack var5);
}

