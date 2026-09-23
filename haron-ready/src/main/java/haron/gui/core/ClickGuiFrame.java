package haron.gui.core;

import haron.gui.core.ClickGuiOverlay;
import haron.gui.core.ClickGuiScreen;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import net.minecraft.client.util.math.MatrixStack;

public class ClickGuiFrame
implements ClickGuiOverlay {
    private static final float c = 90.0f;
    private static final float d = 0.5f;
    public static int a;
    public static boolean b;

    public void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        float f3 = ClickGuiScreen.d();
        float f4 = ClickGuiScreen.e();
        float f5 = ClickGuiScreen.f();
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, f5, pryrvd.w, pryrvd.w, pryrvd.w, pryrvd.w, matrixStack);
        s7swsm2.a(f, f2, f3, f4, f5, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_TOP, pryrvd.PANEL_BG_BOT, pryrvd.PANEL_BG_BOT, matrixStack);
    }
}

