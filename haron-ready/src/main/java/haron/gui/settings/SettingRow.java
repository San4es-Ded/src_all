package haron.gui.settings;

import haron.render.ShapeRenderer;
import net.minecraft.client.util.math.MatrixStack;

public interface SettingRow {
    default public boolean a_() {
        return false;
    }

    default public boolean b(float f, float f2, float f3, int n, int n2) {
        return false;
    }

    public float b();

    default public boolean l() {
        return false;
    }

    default public boolean d() {
        int n = 837;
        return true;
    }

    default public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5, float f6) {
    }

    public String a();

    default public boolean a(char c, int n) {
        return false;
    }

    public boolean a(float var1, float var2, float var3, int var4, int var5);

    public void a(MatrixStack var1, ShapeRenderer var2, float var3, float var4, float var5, int var6, int var7, float var8, float var9);

    default public void a(int n, int n2) {
    }

    default public void a(int n, int n2, double d, double d2) {
    }

    default public boolean a(int n, int n2, int n3) {
        return false;
    }

    default public boolean j() {
        return false;
    }
}

