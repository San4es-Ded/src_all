package haron.gui.core;

import haron.gui.core.ClickGuiTabType;
import haron.render.ShapeRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface ClickGuiTab {
    default public boolean b() {
        return false;
    }

    public void b(float var1, float var2, int var3, int var4);

    public void c(float var1, float var2, int var3, int var4);

    public void a(MatrixStack var1, ShapeRenderer var2, float var3, float var4, int var5, int var6);

    default public boolean a(int n, int n2, int n3) {
        return false;
    }

    public ClickGuiTabType a();

    public void a(float var1);

    public void a(float var1, float var2, int var3, int var4, double var5, double var7);

    public void a(float var1, float var2, int var3, int var4);
}

