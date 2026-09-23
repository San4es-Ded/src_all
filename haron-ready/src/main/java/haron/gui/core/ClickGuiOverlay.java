package haron.gui.core;

import haron.render.ShapeRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface ClickGuiOverlay {
    default public void b(float f, float f2, int n, int n2) {
    }

    public void a(float var1, float var2, int var3, int var4);

    public void a(MatrixStack var1, ShapeRenderer var2, float var3, float var4, int var5, int var6);
}

