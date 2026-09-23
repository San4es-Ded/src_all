package rockstar.client.ui;






import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.compat.RenderSystem;
import java.util.function.Consumer;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.Rect;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.Fonts;
import rockstar.client.animation.Motion;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.ThemeVariant;
import rockstar.client.internal.script.ColorPickerPanel;
import rockstar.client.ui.QuadColorGradient;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.ui.VerticalColorGradient;
import rockstar.client.RockstarClient;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.internal.script.AnimatedColor;
import rockstar.client.ui.ThemeColors;
import rockstar.client.core.CursorType;
import rockstar.client.core.CursorManager;
import rockstar.client.ui.UiUtils;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.ui.WindowAccess;
import rockstar.client.ui.DragConstraint;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.render.ScissorStack;
import rockstar.client.ui.UiNode;

public class ColorPickerPopup
extends UiNode
implements ScreenMetricsAccess,
WindowAccess {
    private final MinecraftClient internalField0149 = MinecraftClient.getInstance();
    private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
    private final AnimatedValue internalField1321 = new AnimatedValue(300L, 0.0f, Easing.internalField1327);
    private final AnimatedColor internalField0814 = new AnimatedColor(300L);
    private final AnimatedColor internalField0813 = new AnimatedColor(200L);
    private final AnimatedValue internalField1322 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1323 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1324 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1623 = new AnimatedValue(500L, Easing.internalField1325);
    private final String internalField0248;
    private final boolean internalField0277;
    private final Consumer<ColorRGBA> internalField0922;
    private float internalField0205 = 6.0f;
    private boolean internalField0276;
    private boolean internalField1099;
    private boolean internalField1100;
    private boolean internalField1102;
    private boolean internalField1101;
    private float internalField0206;
    private float internalField1048;
    private float internalField1047;
    private float internalField1049;

    public ColorPickerPopup(float f2, float f3, boolean bl, ColorRGBA colorRGBA, String string, Consumer<ColorRGBA> consumer) {
        this.internalField0277 = bl;
        this.internalField0248 = string;
        this.internalField0922 = consumer;
        this.internalField0813.internalMethod03487(colorRGBA);
        this.size(143.0f, bl ? 160.0f : 136.0f);
        this.at(f2, f3);
        this.transition((f, typedValue004, nestedValue2009) -> {
            nestedValue2009.internalField0205 = Math.min(1.0f, f);
        });
        this.lifeMotion(Motion.internalMethod01328(300L, Easing.internalField1327));
        this.draggable(DragConstraint.internalField0631);
        this.beginEnter(0.0f);
        this.internalMethod04100(colorRGBA);
    }

    @Override
    protected void onTick(float f, float f2, float f3) {
        if (this.internalField1099) {
            this.internalField0206 = UiUtils.internalMethod04852(0.0f, 1.0f, this.y() + 22.0f, 66.0f, f3);
        }
        if (this.internalField1100) {
            this.internalField1048 = 1.0f - UiUtils.internalMethod04852(0.0f, 1.0f, this.x() + 6.0f, 114.0f, f2);
            this.internalField1047 = 1.0f - UiUtils.internalMethod04852(0.0f, 1.0f, this.y() + 20.0f, 70.0f, f3);
        }
        if (this.internalField1102) {
            this.internalField1049 = UiUtils.internalMethod04852(0.0f, 1.0f, this.x() + 7.0f, 88.0f, f2);
        }
        float f4 = this.x();
        float f5 = this.y();
        if (f4 + this.w() + 5.0f > internalField0389.internalMethod03585()) {
            f4 = internalField0389.internalMethod03585() - this.w() - 5.0f;
        }
        if (f5 + this.h() + 5.0f > internalField0389.internalMethod03589()) {
            f5 = internalField0389.internalMethod03589() - this.h() - 5.0f;
        }
        if (f4 != this.x() || f5 != this.y()) {
            this.at(f4, f5);
        }
        ColorPickerPanel.internalField0416.removeIf(nestedValue2001 -> nestedValue2001.internalField0809.internalMethod02881() == 0.0f && !nestedValue2001.internalField0277);
        this.internalField1321.internalMethod06645(this.internalField0276 ? Easing.internalField0812 : Easing.internalField1328);
        this.internalField1321.internalMethod07062(this.internalField0276);
        this.internalField0808.internalMethod07062(this.appear() >= 0.6f);
        this.internalField0809.internalMethod07062(this.internalField1101);
        this.internalField1322.internalMethod07059(this.internalField0206);
        this.internalField1323.internalMethod07059(1.0f - this.internalField1048);
        this.internalField1324.internalMethod07059(1.0f - this.internalField1047);
        this.internalField1623.internalMethod07059(this.internalField1049);
        this.internalField0814.internalMethod03893(ColorRGBA.fromHSB(this.internalField0206, 1.0f, 1.0f));
        if (this.phase() != UiNode.InternalType0146.internalField1091 && this.internalField0922 != null) {
            this.internalField0922.accept(this.internalMethod02599());
        }
    }

    @Override
    protected void drawSelf(UiRenderContext iII, float f) {
        float f2 = this.x();
        float f3 = this.y();
        float f4 = this.w();
        float f5 = this.h();
        boolean bl = RockstarClient.getInstance().internalMethod04467().internalMethod05065() == ThemeVariant.internalField0395;
        ColorRGBA colorRGBA = ThemeColors.internalField1612.withAlpha(255.0f * (bl ? 0.9f - 0.6f * InterfaceModule.internalMethod07584() : 0.7f));
        ColorRGBA colorRGBA2 = ColorRGBA.fromHSB(this.internalField0206, this.internalField1048, this.internalField1047);
        HudRenderUtils.internalMethod08976(iII.getMatrices(), f2 + f4 / this.internalField0205, f3 + f5 / this.internalField0205, 0.5f + this.appear() * 0.5f);
        ScissorStack.internalMethod06303(iII.getMatrices(), f2 + 1.0f, f3 + 1.0f, f4 - 2.0f, f5 - 2.0f);
        iII.drawShadow(f2 - 5.0f, f3 - 5.0f, f4 + 10.0f, f5 + 10.0f, 15.0f, CornerRadii.internalMethod03908(6.0f), ColorRGBA.BLACK.withAlpha(255.0f * (0.1f + 0.15f * this.internalField0809.internalMethod02881())));
        ScissorStack.internalMethod07643();
        if (InterfaceModule.internalMethod09917()) {
            iII.drawBlurredRect(f2, f3, f4, f5, 45.0f, 7.0f, CornerRadii.internalMethod03908(6.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.appear() * InterfaceModule.internalMethod07585()));
        }
        if (InterfaceModule.internalMethod09719()) {
            iII.drawLiquidGlass(f2, f3, f4, f5, 7.0f, 0.05f - 0.03f * this.internalField0809.internalMethod02881(), CornerRadii.internalMethod03908(6.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.appear() * InterfaceModule.internalMethod07584()));
        }
        iII.drawSquircle(f2, f3, f4, f5, 7.0f, CornerRadii.internalMethod03908(6.0f), colorRGBA);
        ScissorStack.internalMethod06303(iII.getMatrices(), f2, f3, f4, f5);
        iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(7.0f), this.internalField0248, f2 + f4 / 2.0f, f3 + 7.0f, ThemeColors.internalMethod08459());
        iII.drawIcon("colorpicker/pipette", f2 + 7.0f, f3 + 6.0f, 8.0f);
        if (UiUtils.internalMethod05786(f2 + 7.0f, f3 + 6.0f, 8.0, 8.0, iII.internalMethod05259(), iII.internalMethod05261())) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        iII.drawRoundedRect(f2 + f4 - 15.0f, f3 + 5.0f, 10.0f, 10.0f, CornerRadii.internalMethod03908(5.0f), ThemeColors.internalMethod08573());
        iII.drawIcon("xmark", f2 + f4 - 15.0f, f3 + 5.0f, 10.0f);
        if (UiUtils.internalMethod05786(f2 + f4 - 15.0f, f3 + 5.0f, 10.0, 10.0, iII.internalMethod05259(), iII.internalMethod05261())) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        iII.drawRoundedTexture(RockstarClient.id("textures/hue.png"), f2 + f4 - 18.0f, f3 + 20.0f, 12.0f, 70.0f, CornerRadii.internalMethod03908(4.0f));
        iII.drawRoundedRect(f2 + f4 - 16.0f, f3 + 22.0f + 64.0f * this.internalField1322.internalMethod02881(), 8.0f, 2.0f, CornerRadii.internalMethod03908(0.2f), ThemeColors.internalField1312);
        if (UiUtils.internalMethod06450(f2 + f4 - 18.0f, f3 + 20.0f, 12.0, 70.0, iII) || this.internalField1099) {
            CursorManager.internalMethod06882(CursorType.internalField1205);
        }
        iII.drawRoundedRect(f2 + 6.0f, f3 + 20.0f, 114.0f, 70.0f, CornerRadii.internalMethod03908(4.0f), QuadColorGradient.internalMethod05983(this.internalField0814.internalMethod04159(), ThemeColors.internalField1309, ThemeColors.internalField1312, ThemeColors.internalField1309));
        iII.drawRoundedRect(f2 + 6.0f + 114.0f * this.internalField1323.internalMethod02881() - 3.5f, f3 + 20.0f + 70.0f * this.internalField1324.internalMethod02881() - 3.5f, 7.0f, 7.0f, CornerRadii.internalMethod03908(2.5f), ThemeColors.internalField1312);
        iII.drawRoundedRect(f2 + 7.0f + 114.0f * this.internalField1323.internalMethod02881() - 3.5f, f3 + 21.0f + 70.0f * this.internalField1324.internalMethod02881() - 3.5f, 5.0f, 5.0f, CornerRadii.internalMethod03908(1.5f), colorRGBA2);
        if (UiUtils.internalMethod06450(f2 + 6.0f, f3 + 20.0f, 114.0, 70.0, iII) || this.internalField1100) {
            CursorManager.internalMethod06882(CursorType.internalField1207);
        }
        if (this.internalField0277) {
            iII.drawText(Fonts.internalField0449.internalMethod01432(5.0f), LanguageManager.internalMethod07214("colorpicker.opacity").toUpperCase(), f2 + 6.0f, f3 + 95.0f, ThemeColors.internalMethod08459().withAlpha(191.25f));
            iII.drawRoundedTexture(RockstarClient.id("textures/empty.png"), f2 + 6.0f, f3 + 102.0f, 100.0f, 12.0f, CornerRadii.internalMethod03908(5.0f));
            iII.drawRoundedRect(f2 + 6.0f - 0.5f, f3 + 102.0f - 0.5f, 101.0f, 13.0f, CornerRadii.internalMethod03908(5.0f), new VerticalColorGradient(colorRGBA2.withAlpha(0.0f), colorRGBA2));
            iII.drawRoundedRect(f2 + f4 - 32.0f, f3 + 102.0f, 26.0f, 12.0f, CornerRadii.internalMethod03908(2.0f), ThemeColors.internalField1614.withAlpha(255.0f));
            iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(6.0f), (int)(this.internalField1049 * 100.0f) + "%", f2 + f4 - 32.0f + 13.0f, f3 + 106.0f, ThemeColors.internalMethod08459());
            iII.drawRoundedBorder(f2 + 7.0f + 88.0f * this.internalField1623.internalMethod02881(), f3 + 103.0f, 10.0f, 10.0f, 0.5f, CornerRadii.internalMethod03908(4.0f), ThemeColors.internalField1312);
            iII.drawRoundedRect(f2 + 8.0f + 88.0f * this.internalField1623.internalMethod02881(), f3 + 104.0f, 8.0f, 8.0f, CornerRadii.internalMethod03908(3.0f), this.internalMethod02599());
            if (UiUtils.internalMethod06450(f2 + 6.0f, f3 + 102.0f, 100.0, 12.0, iII) || this.internalField1102) {
                CursorManager.internalMethod06882(CursorType.internalField1208);
            }
        }
        iII.drawRoundedRect(f2 + 6.0f, f3 + f5 - 36.0f, 29.0f, 29.0f, CornerRadii.internalMethod03908(5.0f), this.internalMethod02599());
        float f6 = 0.0f;
        float f7 = 0.0f;
        for (ColorPickerPanel.InternalType0004 object : ColorPickerPanel.internalField0416) {
            object.internalField0809.internalMethod07062(object.internalField0277);
            object.internalField0808.internalMethod07062(object.internalField0777.getHue() == this.internalField0206 && object.internalField0777.getSaturation() == this.internalField1047 && object.internalField0777.getBrightness() == this.internalField1048);
            if (object.internalField0808.internalMethod02881() > 0.0f) {
                float f8 = object.internalField0808.internalMethod02881();
                iII.drawRoundedRect(f2 + 45.0f + f6, f3 + f5 - 36.0f + f7, 11.0f, 11.0f, CornerRadii.internalMethod03908(4.5f), object.internalField0777.withAlpha(255.0f * object.internalField0809.internalMethod02881()));
                iII.drawRoundedBorder(f2 + 45.0f + f6 - 1.0f + 2.0f * f8, f3 + f5 - 36.0f + f7 - 1.0f + 2.0f * f8, 13.0f - 4.0f * f8, 13.0f - 4.0f * f8, 0.5f, CornerRadii.internalMethod03908(6.5f - 2.0f * f8), ThemeColors.internalField1312.withAlpha(255.0f * object.internalField0809.internalMethod02881() * object.internalField0808.internalMethod02881()));
            } else {
                iII.drawRoundedRect(f2 + 45.0f + f6, f3 + f5 - 36.0f + f7, 11.0f, 11.0f, CornerRadii.internalMethod03908(4.5f), object.internalField0777.withAlpha(255.0f * object.internalField0809.internalMethod02881()));
            }
            if (UiUtils.internalMethod06450(f2 + 45.0f + f6, f3 + f5 - 36.0f + f7, 11.0, 11.0, iII)) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
            if (!(45.0f + (f6 += 20.0f * object.internalField0809.internalMethod02881()) > f4)) continue;
            f6 = 0.0f;
            f7 += 18.0f * object.internalField0809.internalMethod02881();
        }
        if (ColorPickerPanel.internalField0416.size() < 10) {
            iII.drawRoundedRect(f2 + 45.0f + f6, f3 + f5 - 36.0f + f7, 11.0f, 11.0f, CornerRadii.internalMethod03908(4.5f), ThemeColors.internalMethod08573());
            iII.drawIcon("plus", f2 + 45.0f + f6, f3 + f5 - 36.0f + f7, 11.0f);
            if (UiUtils.internalMethod06450(f2 + 45.0f + f6, f3 + f5 - 36.0f + f7, 11.0, 11.0, iII)) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
        }
        ScissorStack.internalMethod07643();
        HudRenderUtils.internalMethod00012(iII.getMatrices());
        if (this.internalField1321.internalMethod02881() > 0.0f) {
            Rect rect = new Rect(iII.internalMethod05259(), iII.internalMethod05261() + 10, 45.0f + Fonts.internalField1154.internalMethod01432(6.0f).internalMethod00965(LanguageManager.internalMethod07214("colorpicker.click_to_sample")), 30.0f);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)Math.min(1.0f, this.internalField1321.internalMethod02881()));
            HudRenderUtils.internalMethod08976(iII.getMatrices(), rect.getX() + rect.getWidth() / 2.0f, rect.getY() + rect.getHeight() / 2.0f, 0.5f + this.internalField1321.internalMethod02881() * 0.5f);
            iII.drawBlurredRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), 45.0f, 7.0f, CornerRadii.internalMethod03908(6.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField1321.internalMethod02881()));
            iII.drawSquircle(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), 7.0f, CornerRadii.internalMethod03908(6.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * (bl ? 0.8f : 0.7f)));
            ColorRGBA colorRGBA3 = ColorRGBA.fromPixel((float)((double)iII.internalMethod05259() * internalField0389.internalMethod03584()), (float)((double)internalField0267.getHeight() - (double)iII.internalMethod05261() * internalField0389.internalMethod03584()));
            iII.drawRoundedRect(rect.getX() + 5.0f, rect.getY() + 5.0f, rect.getHeight() - 10.0f, rect.getHeight() - 10.0f, CornerRadii.internalMethod03908(5.0f), colorRGBA3);
            iII.drawIcon("colorpicker/click", rect.getX() + rect.getHeight(), rect.getY() + 16.0f, 6.0f);
            iII.drawText(Fonts.internalField1154.internalMethod01432(6.0f), String.format("RGB %s %s %s", (int)colorRGBA3.getRed(), (int)colorRGBA3.getGreen(), (int)colorRGBA3.getBlue()), rect.getX() + rect.getHeight(), rect.getY() + 8.0f, ThemeColors.internalMethod08459());
            iII.drawText(Fonts.internalField1154.internalMethod01432(6.0f), LanguageManager.internalMethod07214("colorpicker.click_to_sample"), rect.getX() + rect.getHeight() + 8.0f, rect.getY() + 17.0f, ThemeColors.internalMethod08459().withAlpha(200.0f));
            HudRenderUtils.internalMethod00012(iII.getMatrices());
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        }
    }

    public ColorRGBA internalMethod02599() {
        this.internalField0813.internalMethod03893(ColorRGBA.fromHSB(this.internalField0206, this.internalField1048, this.internalField1047).withAlpha(this.internalField0277 ? 255.0f * this.internalField1049 : 255.0f));
        return this.internalField0813.internalMethod04159();
    }

    private void internalMethod04100(ColorRGBA colorRGBA) {
        this.internalField0206 = colorRGBA.getHue();
        this.internalField1048 = colorRGBA.getBrightness();
        this.internalField1047 = colorRGBA.getSaturation();
        this.internalField1049 = colorRGBA.getAlpha() / 255.0f;
        this.internalField0813.internalMethod03893(colorRGBA);
    }

    @Override
    public void close() {
        this.internalField0205 = 2.0f;
        this.lifeMotion(Motion.internalMethod01328(300L, Easing.internalField1328));
        this.internalField0276 = false;
        this.internalField1101 = false;
        this.internalField1099 = false;
        this.internalField1100 = false;
        this.internalField1102 = false;
        super.close();
    }

    @Override
    public boolean mouseClicked(float f, float f2, MouseButton typedParameter1015) {
        if (this.phase() == UiNode.InternalType0146.internalField1091 || this.phase() == UiNode.InternalType0146.internalField1090) {
            return false;
        }
        float f3 = this.x();
        float f4 = this.y();
        float f5 = this.w();
        float f6 = this.h();
        if (this.internalField0276) {
            if (typedParameter1015 == MouseButton.internalField0102) {
                ColorRGBA colorRGBA = ColorRGBA.fromPixel((float)((double)f * internalField0389.internalMethod03584()), (float)((double)internalField0267.getHeight() - (double)f2 * internalField0389.internalMethod03584()));
                this.internalMethod04100(colorRGBA);
            }
            this.internalField0276 = false;
            return true;
        }
        boolean bl = ColorPickerPanel.internalField0416.size() < 10;
        float f7 = 0.0f;
        float f8 = 0.0f;
        for (ColorPickerPanel.InternalType0004 nestedValue2001 : ColorPickerPanel.internalField0416) {
            if (UiUtils.internalMethod05785(f3 + 45.0f + f7, f4 + f6 - 36.0f + f8, 11.0, 11.0, f, f2)) {
                if (typedParameter1015.internalMethod02957() != 0) {
                    nestedValue2001.internalField0277 = false;
                    RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
                } else {
                    this.internalMethod04100(nestedValue2001.internalField0777);
                }
                return true;
            }
            if (nestedValue2001.internalField0777.getHue() == this.internalField0206 && nestedValue2001.internalField0777.getSaturation() == this.internalField1047 && nestedValue2001.internalField0777.getBrightness() == this.internalField1048) {
                bl = false;
            }
            if (!(45.0f + (f7 += 20.0f) > f5)) continue;
            f7 = 0.0f;
            f8 += 18.0f;
        }
        if (UiUtils.internalMethod05785(f3 + 45.0f + f7, f4 + f6 - 36.0f + f8, 11.0, 11.0, f, f2) && bl) {
            ColorPickerPanel.internalField0416.add(new ColorPickerPanel.InternalType0004(this.internalMethod02599()));
            RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
            return true;
        }
        if (UiUtils.internalMethod05785(f3 + 7.0f, f4 + 6.0f, 8.0, 8.0, f, f2)) {
            this.internalField0276 = true;
            return true;
        }
        if (UiUtils.internalMethod05785(f3 + f5 - 15.0f, f4 + 5.0f, 10.0, 10.0, f, f2)) {
            this.close();
            return true;
        }
        if (UiUtils.internalMethod05785(f3 + f5 - 18.0f, f4 + 20.0f, 12.0, 70.0, f, f2)) {
            this.internalField1099 = true;
            return true;
        }
        if (UiUtils.internalMethod05785(f3 + 6.0f, f4 + 20.0f, 114.0, 70.0, f, f2)) {
            this.internalField1100 = true;
            return true;
        }
        if (this.internalField0277 && UiUtils.internalMethod05785(f3 + 6.0f, f4 + 102.0f, 100.0, 12.0, f, f2)) {
            this.internalField1102 = true;
            return true;
        }
        if (this.contains(f, f2)) {
            if (typedParameter1015 == MouseButton.internalField0102) {
                this.internalField1101 = true;
                return super.mouseClicked(f, f2, typedParameter1015);
            }
            return false;
        }
        this.close();
        return false;
    }

    @Override
    public void mouseReleased(float f, float f2, MouseButton typedParameter1015) {
        this.internalField1101 = false;
        this.internalField1099 = false;
        this.internalField1100 = false;
        this.internalField1102 = false;
        super.mouseReleased(f, f2, typedParameter1015);
    }

    @Override
    public boolean keyPressed(int n, int n2, int n3) {
        if (rockstar.client.compat.InputCompat.isCopy(n)) {
            this.internalField0149.keyboard.setClipboard(this.internalMethod02599().toHex());
            return true;
        }
        if (rockstar.client.compat.InputCompat.isPaste(n)) {
            try {
                this.internalMethod04100(ColorRGBA.fromHex(this.internalField0149.keyboard.getClipboard()));
            }
            catch (Exception exception) {
                // empty catch block
            }
            return true;
        }
        return super.keyPressed(n, n2, n3);
    }
}
