package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import java.util.Random;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.auth.ClientDataConfig;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.ui.RockstarScreen;

public class IslandGameScreen
extends RockstarScreen
implements MinecraftClientAccess,
ScreenMetricsAccess {
    private static final Random internalField0362 = new Random();
    private float internalField0205;
    private float internalField0206;
    private float internalField1048;
    private float internalField1047;
    private float internalField1049;
    private float internalField1046;
    private float internalField1456;
    private final float[] internalField0615 = new float[3];
    private int internalField0227;
    private int internalField0228;
    private boolean internalField0277;
    private long internalField0229;
    private final DynamicIslandHud internalField0209 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();

    public final void init() {
        this.internalMethod07583();
        this.internalMethod07580();
        this.internalField0229 = System.nanoTime();
    }

    private void internalMethod07580() {
        float f = internalField0389.internalMethod03589() / 2.0f;
        this.internalField0206 = f + 160.0f - 18.0f - 7.0f;
        this.internalField1048 = this.internalField0205 = internalField0389.internalMethod03585() / 2.0f;
        this.internalMethod08748();
    }

    private void internalMethod07583() {
        this.internalField0227 = 0;
        this.internalField0228 = 3;
        this.internalField0277 = false;
        this.internalField0615[2] = 0.0f;
        this.internalField0615[1] = 0.0f;
        this.internalField0615[0] = 0.0f;
    }

    private void internalMethod08748() {
        this.internalField1047 = this.internalField0205 = (this.internalField1048 = internalField0389.internalMethod03585() / 2.0f);
        this.internalField1049 = this.internalField0206 - 7.0f - 10.0f;
        this.internalField1046 = (internalField0362.nextFloat() * 2.0f - 1.0f) * 70.0f;
        this.internalField1456 = -170.0f;
    }

    @Override
    public final void render(UiRenderContext iII) {
        long l = System.nanoTime();
        float f = MathHelper.clamp((float)((float)(l - this.internalField0229) / 1.0E9f), (float)0.0f, (float)0.05f);
        this.internalField0229 = l;
        this.internalMethod03362(f, iII);
        this.internalMethod02157(iII);
    }

    private void internalMethod03362(float f, UiRenderContext iII) {
        float f2 = internalField0389.internalMethod03585();
        float f3 = internalField0389.internalMethod03589();
        float f4 = f2 / 2.0f - 140.0f + 18.0f;
        float f5 = f2 / 2.0f + 140.0f - 18.0f;
        float f6 = f3 / 2.0f - 160.0f + 18.0f;
        float f7 = f3 / 2.0f + 160.0f - 18.0f;
        for (int i = 0; i < this.internalField0615.length; ++i) {
            this.internalField0615[i] = Math.max(0.0f, this.internalField0615[i] - f * (1.2f + (float)i * 0.4f));
        }
        float f8 = MathHelper.clamp((float)iII.internalMethod05259(), (float)(f4 + 48.0f), (float)(f5 - 48.0f));
        if (!this.internalField0277) {
            this.internalField0205 += (f8 - this.internalField0205) * Math.min(1.0f, f * 16.0f);
        }
        if (this.internalField0277) {
            return;
        }
        float f9 = this.internalField1047;
        float f10 = this.internalField1049;
        this.internalField1047 += this.internalField1046 * f;
        this.internalField1049 += this.internalField1456 * f;
        if (this.internalField1047 - 6.0f < f4 || this.internalField1047 + 6.0f > f5) {
            float f11 = this.internalField1046 = this.internalField1047 - 6.0f < f4 ? Math.abs(this.internalField1046) : -Math.abs(this.internalField1046);
        }
        if (this.internalField1049 - 6.0f < f6) {
            this.internalField1456 = Math.abs(this.internalField1456);
        }
        this.internalMethod05506(f6);
        this.internalMethod03848(f9, f10);
        if (this.internalField1049 - 6.0f > f7) {
            if (--this.internalField0228 <= 0) {
                this.internalField0277 = true;
                this.internalField0615[2] = 1.0f;
                this.internalMethod08760();
            } else {
                this.internalMethod08748();
            }
        }
    }

    private void internalMethod05506(float f) {
        float f2;
        if (this.internalField0209 == null) {
            return;
        }
        float f3 = this.internalField0209.getX();
        float f4 = this.internalField0209.getY();
        float f5 = this.internalField0209.getWidth();
        float f6 = this.internalField0209.getHeight();
        float f7 = f4 + f6;
        if (this.internalField1049 - 6.0f <= f7 && this.internalField1049 + 6.0f >= f4 && Math.abs(this.internalField1047 - (f2 = MathHelper.clamp((float)this.internalField1047, (float)f3, (float)(f3 + f5)))) <= 7.5f && this.internalField1456 < 0.0f) {
            this.internalField1049 = f7 + 6.0f;
            this.internalField1456 = Math.abs(this.internalField1456) + Math.min(90.0f, (float)this.internalField0227 * 2.5f);
            this.internalField1046 += (this.internalField1047 - (f3 + f5 / 2.0f)) / (f5 / 2.0f) * 45.0f;
            ++this.internalField0227;
            this.internalField0615[0] = 1.0f;
            this.internalField0615[2] = 0.75f;
        }
    }

    private void internalMethod02157(UiRenderContext iII) {
        float f = internalField0389.internalMethod03585();
        float f2 = internalField0389.internalMethod03589();
        float f3 = f / 2.0f - 140.0f;
        float f4 = f2 / 2.0f - 160.0f;
        ColorRGBA colorRGBA = ThemeColors.internalMethod02531().withAlpha(220.0f);
        ColorRGBA colorRGBA2 = new ColorRGBA(20.0f, 24.0f, 36.0f, 200 + (int)(this.internalField0615[2] * 30.0f));
        iII.drawSquircle(f3, f4, 280.0f, 320.0f, 8.0f, CornerRadii.internalMethod03908(24.0f), colorRGBA2);
        ColorRGBA colorRGBA3 = colorRGBA.mix(ColorRGBA.WHITE, 0.25f).withAlpha(240.0f);
        ColorRGBA colorRGBA4 = colorRGBA3.mix(ColorRGBA.WHITE, this.internalField0615[1] * 0.7f);
        iII.drawLiquidGlass(this.internalField0205 - 45.0f, this.internalField0206 - 7.0f, 90.0f, 14.0f, 8.0f, 1.0f, CornerRadii.internalMethod03908(7.0f), colorRGBA4);
        iII.drawLiquidGlass(this.internalField1047 - 6.0f, this.internalField1049 - 6.0f, 12.0f, 12.0f, 8.0f, 1.0f, CornerRadii.internalMethod03908(6.0f), ColorRGBA.WHITE.mix(ThemeColors.internalMethod02531(), 0.3f));
        this.internalMethod00175(iII, colorRGBA, f4);
        if (this.internalField0209 != null) {
            this.internalField0209.render(iII);
        }
    }

    private void internalMethod00175(UiRenderContext iII, ColorRGBA colorRGBA, float f) {
        ClientDataConfig typedValue139 = (ClientDataConfig)RockstarClient.getInstance().internalMethod03371().internalMethod01175("client");
        float f2 = internalField0389.internalMethod03585();
        if (typedValue139 != null) {
            iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(6.5f), "Games: " + typedValue139.internalMethod02138() + "   |   Best: " + typedValue139.internalMethod02133(), f2 / 2.0f, f + 338.0f, ColorRGBA.WHITE.mulAlpha(0.6f));
        }
        iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(7.0f), "Score", f2 / 2.0f - 60.0f, f + 6.0f, ColorRGBA.WHITE.mulAlpha(0.75f));
        iII.drawCenteredText(Fonts.internalField0450.internalMethod01432(12.0f), String.valueOf(this.internalField0227), f2 / 2.0f - 60.0f, f + 22.0f, colorRGBA);
        iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(7.0f), "Lives", f2 / 2.0f + 60.0f, f + 6.0f, ColorRGBA.WHITE.mulAlpha(0.75f));
        iII.drawCenteredText(Fonts.internalField0450.internalMethod01432(12.0f), String.valueOf(Math.max(this.internalField0228, 0)), f2 / 2.0f + 60.0f, f + 22.0f, colorRGBA);
        if (this.internalField0277) {
            float f3 = f2 / 2.0f;
            float f4 = f + 160.0f;
            iII.drawBlurredRect(f3 - 90.0f, f4 - 40.0f, 180.0f, 80.0f, 35.0f, CornerRadii.internalMethod03908(18.0f), new ColorRGBA(12.0f, 14.0f, 22.0f, 200.0f));
            iII.drawCenteredText(Fonts.internalField0450.internalMethod01432(10.0f), "Game Over", f3, f4 - 14.0f, ColorRGBA.WHITE);
            iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(7.5f), "Score: " + this.internalField0227, f3, f4, ColorRGBA.WHITE);
            iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(6.5f), "Click to play again", f3, f4 + 10.0f, ColorRGBA.WHITE.mulAlpha(0.85f));
        }
    }

    private void internalMethod03848(float f, float f2) {
        float f3 = 45.0f;
        float f4 = this.internalField0206 - 7.0f;
        float f5 = this.internalField0206 + 7.0f;
        if (this.internalField1456 > 0.0f) {
            float f6;
            float f7;
            float f8 = f2 + 6.0f;
            float f9 = this.internalField1049 + 6.0f;
            if (f8 <= f4 && f9 >= f4 && f9 != f8 && (f7 = MathHelper.lerp((float)(f6 = MathHelper.clamp((float)((f4 - f8) / (f9 - f8)), (float)0.0f, (float)1.0f)), (float)f, (float)this.internalField1047)) >= this.internalField0205 - f3 && f7 <= this.internalField0205 + f3) {
                this.internalField1047 = f7;
                this.internalField1049 = f4 - 6.0f;
                this.internalMethod05624(f7, f3);
                return;
            }
        }
        if (this.internalField1049 + 6.0f >= f4 && this.internalField1049 - 6.0f <= f5 && this.internalField1047 >= this.internalField0205 - f3 && this.internalField1047 <= this.internalField0205 + f3 && this.internalField1456 > 0.0f) {
            this.internalField1049 = f4 - 6.0f;
            this.internalMethod05624(this.internalField1047, f3);
        }
    }

    private void internalMethod05624(float f, float f2) {
        this.internalField1456 = -Math.abs(this.internalField1456) - Math.min(80.0f, (float)this.internalField0227 * 2.0f);
        this.internalField1046 = MathHelper.clamp((float)(this.internalField1046 + (f - this.internalField0205) / f2 * 60.0f), (float)-180.0f, (float)180.0f);
        this.internalField0615[1] = 1.0f;
    }

    private void internalMethod08760() {
        ClientDataConfig typedValue139 = (ClientDataConfig)RockstarClient.getInstance().internalMethod03371().internalMethod01175("client");
        if (typedValue139 == null) {
            return;
        }
        typedValue139.internalMethod06332(typedValue139.internalMethod02138() + 1);
        if (this.internalField0227 > typedValue139.internalMethod02133()) {
            typedValue139.internalMethod06263(this.internalField0227);
        }
        RockstarClient.getInstance().internalMethod03371().internalMethod08923();
    }

    @Override
    public final void onMouseClicked(double d, double d2, MouseButton typedParameter1015) {
        if (this.internalField0277 && typedParameter1015 == MouseButton.internalField0102) {
            this.internalMethod07583();
            this.internalMethod07580();
        }
        super.onMouseClicked(d, d2, typedParameter1015);
    }

    public void renderBackground(DrawContext context, int mouseX, int mouseY, float delta) {
    }

    public final boolean shouldPause() {
        return false;
    }
}
