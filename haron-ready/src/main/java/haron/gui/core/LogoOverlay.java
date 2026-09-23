package haron.gui.core;

import haron.gui.core.ClickGuiOverlay;
import haron.modules.hud.ClientColor;
import haron.render.font.FontRenderer;
import haron.render.font.ClientFonts;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import java.awt.Color;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class LogoOverlay
implements ClickGuiOverlay {
    private static final float LOGO_HEIGHT = 22.0f;
    private static final float TEX_W = 512.0f;
    private static final float TEX_H = 512.0f;
    private static final float LOGO_WIDTH = 22.0f;
    private static final float LOGO_OFFSET_X = 19.0f;
    private static final float LOGO_OFFSET_Y = 6.0f;
    private static final float TEXT_GAP = 6.0f;
    private static final float FONT_SIZE = 14.0f;
    private static final float TEXT_BASELINE_NUDGE = 1.0f;
    private static final Color HARON_SHADOW = new Color(0, 0, 0, 110);

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        Identifier identifier = HaronIcons.get("logo");
        float f3 = f + 19.0f;
        float f4 = f2 + 6.0f;
        Color color = ClientColor.currentColor();
        s7swsm2.a(identifier, f3, f4, 22.0f, 22.0f, 6.6000004f, 0.0f, 0.0f, 1.0f, 1.0f, color != null ? color : new Color(255, 255, 255, 255), matrixStack);
        FontRenderer v6hnga2 = ClientFonts.b[14];
        String string = "Haron";
        float f5 = v6hnga2.a(string);
        float f6 = v6hnga2.b(string);
        float f7 = f3 + 22.0f + 6.0f;
        float f8 = f4 + (22.0f - f6) / 2.0f + 1.0f;
        v6hnga2.a(string, f7 + 0.5f, (double)(f8 + 0.5f), HARON_SHADOW, matrixStack);
        v6hnga2.a(string, f7, (double)f8, pryrvd.LOGO_TEXT, matrixStack);
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
    }
}

