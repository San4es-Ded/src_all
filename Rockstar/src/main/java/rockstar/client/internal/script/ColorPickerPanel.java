package rockstar.client.internal.script;







import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.compat.RenderSystem;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import lombok.Generated;
import net.minecraft.client.gui.screen.Screen;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.Rect;
import rockstar.client.ui.UiRenderContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.ThemeVariant;
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
import rockstar.client.ui.LegacyUiElement;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.render.ScissorStack;

public class ColorPickerPanel
extends LegacyUiElement
implements ScreenMetricsAccess,
WindowAccess {
    private final AnimatedValue internalField0809 = new AnimatedValue(300L, 0.0f, Easing.internalField1327);
    private final AnimatedValue internalField1321 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
    public final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
    private final AnimatedValue internalField1322 = new AnimatedValue(300L, 0.0f, Easing.internalField1327);
    private final AnimatedColor internalField0814 = new AnimatedColor(300L);
    private final AnimatedColor internalField0813 = new AnimatedColor(200L);
    private final String internalField0248;
    private boolean internalField0277;
    private float internalField1049;
    private boolean internalField0276;
    private boolean internalField1099;
    private float internalField1046;
    private float internalField1456;
    private boolean internalField1100;
    private boolean internalField1102;
    private boolean internalField1101;
    private final boolean internalField1516;
    private final AnimatedValue internalField1323 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1324 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1623 = new AnimatedValue(500L, Easing.internalField1325);
    private final AnimatedValue internalField1618 = new AnimatedValue(500L, Easing.internalField1325);
    private float internalField1457;
    private float internalField1458;
    private float internalField1459;
    private float internalField1460;
    public static final List<InternalType0004> internalField0416 = new CopyOnWriteArrayList<InternalType0004>(List.of(new InternalType0004(new ColorRGBA(0.0f, 122.0f, 255.0f)), new InternalType0004(new ColorRGBA(52.0f, 199.0f, 89.0f)), new InternalType0004(new ColorRGBA(255.0f, 204.0f, 0.0f)), new InternalType0004(new ColorRGBA(255.0f, 59.0f, 48.0f)), new InternalType0004(new ColorRGBA(151.0f, 71.0f, 255.0f))));

    public ColorPickerPanel(float f, float f2, float f3, boolean bl, ColorRGBA colorRGBA, String string) {
        super(f, f2, 143.0f, bl ? 160.0f : 136.0f);
        this.internalField1049 = f3;
        this.internalField1516 = bl;
        this.internalField0277 = true;
        this.internalField0813.internalMethod03487(colorRGBA);
        this.internalField0248 = string;
        this.internalMethod07209(colorRGBA);
    }

    public static void internalMethod04224(List<InternalType0004> list) {
        internalField0416.clear();
        internalField0416.addAll(list);
    }

    @Override
    public void internalMethod05619(UiRenderContext iII) {
        if (this.internalField1100) {
            this.internalField1457 = UiUtils.internalMethod04852(0.0f, 1.0f, this.internalField0206 + 22.0f, 66.0f, iII.internalMethod05261());
        }
        if (this.internalField1102) {
            this.internalField1458 = 1.0f - UiUtils.internalMethod04852(0.0f, 1.0f, this.internalField0205 + 6.0f, 114.0f, iII.internalMethod05259());
            this.internalField1459 = 1.0f - UiUtils.internalMethod04852(0.0f, 1.0f, this.internalField0206 + 20.0f, 70.0f, iII.internalMethod05261());
        }
        if (this.internalField1101) {
            this.internalField1460 = UiUtils.internalMethod04852(0.0f, 1.0f, this.internalField0205 + 7.0f, 88.0f, iII.internalMethod05259());
        }
        if (this.internalField0276) {
            this.internalField0205 = (float)iII.internalMethod05259() - this.internalField1046;
            this.internalField0206 = (float)iII.internalMethod05261() - this.internalField1456;
        }
        if (this.internalField0205 + this.internalField1048 + 5.0f > internalField0389.internalMethod03585()) {
            this.internalField0205 = internalField0389.internalMethod03585() - this.internalField1048 - 5.0f;
        }
        if (this.internalField0206 + this.internalField1047 + 5.0f > internalField0389.internalMethod03589()) {
            this.internalField0206 = internalField0389.internalMethod03589() - this.internalField1047 - 5.0f;
        }
        internalField0416.removeIf(nestedValue2001 -> nestedValue2001.internalField0809.internalMethod02881() == 0.0f && !nestedValue2001.internalField0277);
        this.internalField1322.internalMethod06645(this.internalField1099 ? Easing.internalField0812 : Easing.internalField1328);
        this.internalField1322.internalMethod07062(this.internalField1099);
        this.internalField0809.internalMethod06645(this.internalField0277 ? Easing.internalField0812 : Easing.internalField1328);
        this.internalField0809.internalMethod07062(this.internalField0277);
        this.internalField1321.internalMethod07062(this.internalField0809.internalMethod02881() >= 0.6f);
        this.internalField0808.internalMethod07062(this.internalField0276);
        this.internalField1323.internalMethod07059(this.internalField1457);
        this.internalField1324.internalMethod07059(1.0f - this.internalField1458);
        this.internalField1623.internalMethod07059(1.0f - this.internalField1459);
        this.internalField1618.internalMethod07059(this.internalField1460);
        this.internalField0814.internalMethod03893(ColorRGBA.fromHSB(this.internalField1457, 1.0f, 1.0f));
        boolean bl = RockstarClient.getInstance().internalMethod04467().internalMethod05065() == ThemeVariant.internalField0395;
        ColorRGBA colorRGBA = ThemeColors.internalMethod07738().withAlpha(255.0f * (bl ? 0.9f - 0.6f * InterfaceModule.internalMethod07584() : 0.7f));
        ColorRGBA colorRGBA2 = ColorRGBA.fromHSB(this.internalField1457, this.internalField1458, this.internalField1459);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)Math.min(1.0f, this.internalField0809.internalMethod02881()));
        HudRenderUtils.internalMethod08976(iII.getMatrices(), this.internalField0205 + this.internalField1048 / this.internalField1049, this.internalField0206 + this.internalField1047 / this.internalField1049, 0.5f + this.internalField0809.internalMethod02881() * 0.5f);
        ScissorStack.internalMethod06303(iII.getMatrices(), this.internalField0205 + 1.0f, this.internalField0206 + 1.0f, this.internalField1048 - 2.0f, this.internalField1047 - 2.0f);
        iII.drawShadow(this.internalField0205 - 5.0f, this.internalField0206 - 5.0f, this.internalField1048 + 10.0f, this.internalField1047 + 10.0f, 15.0f, CornerRadii.internalMethod03908(6.0f), ColorRGBA.BLACK.withAlpha(255.0f * (0.1f + 0.15f * this.internalField0808.internalMethod02881())));
        ScissorStack.internalMethod07643();
        if (InterfaceModule.internalMethod09917()) {
            iII.drawBlurredRect(this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047, 45.0f, 7.0f, CornerRadii.internalMethod03908(6.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0809.internalMethod02881() * InterfaceModule.internalMethod07585()));
        }
        if (InterfaceModule.internalMethod09719()) {
            iII.drawLiquidGlass(this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047, 7.0f, 0.05f - 0.03f * this.internalField0808.internalMethod02881(), CornerRadii.internalMethod03908(6.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0809.internalMethod02881() * InterfaceModule.internalMethod07584()));
        }
        iII.drawSquircle(this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047, 7.0f, CornerRadii.internalMethod03908(6.0f), colorRGBA);
        ScissorStack.internalMethod06303(iII.getMatrices(), this.internalField0205, this.internalField0206, this.internalField1048, this.internalField1047);
        iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(7.0f), this.internalField0248, this.internalField0205 + this.internalField1048 / 2.0f, this.internalField0206 + 7.0f, ThemeColors.internalMethod08459());
        iII.drawIcon("colorpicker/pipette", this.internalField0205 + 7.0f, this.internalField0206 + 6.0f, 8.0f);
        if (UiUtils.internalMethod05786(this.internalField0205 + 7.0f, this.internalField0206 + 6.0f, 8.0, 8.0, iII.internalMethod05259(), iII.internalMethod05261())) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - 15.0f, this.internalField0206 + 5.0f, 10.0f, 10.0f, CornerRadii.internalMethod03908(5.0f), ThemeColors.internalMethod08573());
        iII.drawIcon("xmark", this.internalField0205 + this.internalField1048 - 15.0f, this.internalField0206 + 5.0f, 10.0f);
        if (UiUtils.internalMethod05786(this.internalField0205 + this.internalField1048 - 15.0f, this.internalField0206 + 5.0f, 10.0, 10.0, iII.internalMethod05259(), iII.internalMethod05261())) {
            CursorManager.internalMethod06882(CursorType.internalField0567);
        }
        iII.drawRoundedTexture(RockstarClient.id("textures/hue.png"), this.internalField0205 + this.internalField1048 - 18.0f, this.internalField0206 + 20.0f, 12.0f, 70.0f, CornerRadii.internalMethod03908(4.0f));
        iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - 16.0f, this.internalField0206 + 22.0f + 64.0f * this.internalField1323.internalMethod02881(), 8.0f, 2.0f, CornerRadii.internalMethod03908(0.2f), ThemeColors.internalField1312);
        if (UiUtils.internalMethod06450(this.internalField0205 + this.internalField1048 - 18.0f, this.internalField0206 + 20.0f, 12.0, 70.0, iII) || this.internalField1100) {
            CursorManager.internalMethod06882(CursorType.internalField1205);
        }
        iII.drawRoundedRect(this.internalField0205 + 6.0f, this.internalField0206 + 20.0f, 114.0f, 70.0f, CornerRadii.internalMethod03908(4.0f), QuadColorGradient.internalMethod05983(this.internalField0814.internalMethod04159(), ThemeColors.internalField1309, ThemeColors.internalField1312, ThemeColors.internalField1309));
        iII.drawRoundedRect(this.internalField0205 + 6.0f + 114.0f * this.internalField1324.internalMethod02881() - 3.5f, this.internalField0206 + 20.0f + 70.0f * this.internalField1623.internalMethod02881() - 3.5f, 7.0f, 7.0f, CornerRadii.internalMethod03908(2.5f), ThemeColors.internalField1312);
        iII.drawRoundedRect(this.internalField0205 + 7.0f + 114.0f * this.internalField1324.internalMethod02881() - 3.5f, this.internalField0206 + 21.0f + 70.0f * this.internalField1623.internalMethod02881() - 3.5f, 5.0f, 5.0f, CornerRadii.internalMethod03908(1.5f), colorRGBA2);
        if (UiUtils.internalMethod06450(this.internalField0205 + 6.0f, this.internalField0206 + 20.0f, 114.0, 70.0, iII) || this.internalField1102) {
            CursorManager.internalMethod06882(CursorType.internalField1207);
        }
        if (this.internalField1516) {
            iII.drawText(Fonts.internalField0449.internalMethod01432(5.0f), LanguageManager.internalMethod07214("colorpicker.opacity").toUpperCase(), this.internalField0205 + 6.0f, this.internalField0206 + 95.0f, ThemeColors.internalMethod08459().withAlpha(191.25f));
            iII.drawRoundedTexture(RockstarClient.id("textures/empty.png"), this.internalField0205 + 6.0f, this.internalField0206 + 102.0f, 100.0f, 12.0f, CornerRadii.internalMethod03908(5.0f));
            iII.drawRoundedRect(this.internalField0205 + 6.0f - 0.5f, this.internalField0206 + 102.0f - 0.5f, 101.0f, 13.0f, CornerRadii.internalMethod03908(5.0f), new VerticalColorGradient(colorRGBA2.withAlpha(0.0f), colorRGBA2));
            iII.drawRoundedRect(this.internalField0205 + this.internalField1048 - 32.0f, this.internalField0206 + 102.0f, 26.0f, 12.0f, CornerRadii.internalMethod03908(2.0f), ThemeColors.internalMethod08573().withAlpha(255.0f));
            iII.drawCenteredText(Fonts.internalField0449.internalMethod01432(6.0f), (int)(this.internalField1460 * 100.0f) + "%", this.internalField0205 + this.internalField1048 - 32.0f + 13.0f, this.internalField0206 + 106.0f, ThemeColors.internalMethod08459());
            iII.drawRoundedBorder(this.internalField0205 + 7.0f + 88.0f * this.internalField1618.internalMethod02881(), this.internalField0206 + 103.0f, 10.0f, 10.0f, 0.5f, CornerRadii.internalMethod03908(4.0f), ThemeColors.internalField1312);
            iII.drawRoundedRect(this.internalField0205 + 8.0f + 88.0f * this.internalField1618.internalMethod02881(), this.internalField0206 + 104.0f, 8.0f, 8.0f, CornerRadii.internalMethod03908(3.0f), this.internalMethod01145());
            if (UiUtils.internalMethod06450(this.internalField0205 + 6.0f, this.internalField0206 + 102.0f, 100.0, 12.0, iII) || this.internalField1101) {
                CursorManager.internalMethod06882(CursorType.internalField1208);
            }
        }
        iII.drawRoundedRect(this.internalField0205 + 6.0f, this.internalField0206 + this.internalField1047 - 36.0f, 29.0f, 29.0f, CornerRadii.internalMethod03908(5.0f), this.internalMethod01145());
        float f = 0.0f;
        float f2 = 0.0f;
        for (InternalType0004 object : internalField0416) {
            object.internalField0809.internalMethod07062(object.internalField0277);
            object.internalField0808.internalMethod07062(object.internalField0777.getHue() == this.internalField1457 && object.internalField0777.getSaturation() == this.internalField1459 && object.internalField0777.getBrightness() == this.internalField1458);
            if (object.internalField0808.internalMethod02881() > 0.0f) {
                float f3 = object.internalField0808.internalMethod02881();
                iII.drawRoundedRect(this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0f, 11.0f, CornerRadii.internalMethod03908(4.5f), object.internalField0777.withAlpha(255.0f * object.internalField0809.internalMethod02881()));
                iII.drawRoundedBorder(this.internalField0205 + 45.0f + f - 1.0f + 2.0f * f3, this.internalField0206 + this.internalField1047 - 36.0f + f2 - 1.0f + 2.0f * f3, 13.0f - 4.0f * f3, 13.0f - 4.0f * f3, 0.5f, CornerRadii.internalMethod03908(6.5f - 2.0f * f3), ThemeColors.internalField1312.withAlpha(255.0f * object.internalField0809.internalMethod02881() * object.internalField0808.internalMethod02881()));
            } else {
                iII.drawRoundedRect(this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0f, 11.0f, CornerRadii.internalMethod03908(4.5f), object.internalField0777.withAlpha(255.0f * object.internalField0809.internalMethod02881()));
            }
            if (UiUtils.internalMethod06450(this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0, 11.0, iII)) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
            if (!(45.0f + (f += 20.0f * object.internalField0809.internalMethod02881()) > this.internalField1048)) continue;
            f = 0.0f;
            f2 += 18.0f * object.internalField0809.internalMethod02881();
        }
        if (internalField0416.size() < 10) {
            iII.drawRoundedRect(this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0f, 11.0f, CornerRadii.internalMethod03908(4.5f), ThemeColors.internalMethod08573());
            iII.drawIcon("plus", this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0f);
            if (UiUtils.internalMethod06450(this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0, 11.0, iII)) {
                CursorManager.internalMethod06882(CursorType.internalField0567);
            }
        }
        ScissorStack.internalMethod07643();
        HudRenderUtils.internalMethod00012(iII.getMatrices());
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        if (this.internalField1322.internalMethod02881() > 0.0f) {
            Rect rect = new Rect(iII.internalMethod05259(), iII.internalMethod05261() + 10, 45.0f + Fonts.internalField1154.internalMethod01432(6.0f).internalMethod00965(LanguageManager.internalMethod07214("colorpicker.click_to_sample")), 30.0f);
            RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)Math.min(1.0f, this.internalField1322.internalMethod02881()));
            HudRenderUtils.internalMethod08976(iII.getMatrices(), rect.getX() + rect.getWidth() / 2.0f, rect.getY() + rect.getHeight() / 2.0f, 0.5f + this.internalField1322.internalMethod02881() * 0.5f);
            iII.drawBlurredRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), 45.0f, 7.0f, CornerRadii.internalMethod03908(6.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField1322.internalMethod02881()));
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

    public ColorRGBA internalMethod01145() {
        this.internalField0813.internalMethod03893(ColorRGBA.fromHSB(this.internalField1457, this.internalField1458, this.internalField1459).withAlpha(this.internalField1516 ? 255.0f * this.internalField1460 : 255.0f));
        return this.internalField0813.internalMethod04159();
    }

    @Override
    public void internalMethod05727(int n, int n2, int n3) {
        if (rockstar.client.compat.InputCompat.isCopy(n)) {
            ColorPickerPanel.internalField0149.keyboard.setClipboard(this.internalMethod01145().toHex());
        } else if (rockstar.client.compat.InputCompat.isPaste(n)) {
            String string = ColorPickerPanel.internalField0149.keyboard.getClipboard();
            try {
                this.internalMethod07209(ColorRGBA.fromHex(string));
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
        super.internalMethod05727(n, n2, n3);
    }

    @Override
    public void internalMethod01643(double d, double d2, MouseButton typedParameter1015) {
        boolean bl = internalField0416.size() < 10;
        float f = 0.0f;
        float f2 = 0.0f;
        for (InternalType0004 nestedValue2001 : internalField0416) {
            if (UiUtils.internalMethod05785(this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0, 11.0, d, d2)) {
                if (typedParameter1015.internalMethod02957() != 0) {
                    nestedValue2001.internalField0277 = false;
                    RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
                } else {
                    this.internalMethod07209(nestedValue2001.internalField0777);
                }
                return;
            }
            if (nestedValue2001.internalField0777.getHue() == this.internalField1457 && nestedValue2001.internalField0777.getSaturation() == this.internalField1459 && nestedValue2001.internalField0777.getBrightness() == this.internalField1458) {
                bl = false;
            }
            if (!(45.0f + (f += 20.0f) > this.internalField1048)) continue;
            f = 0.0f;
            f2 += 18.0f;
        }
        if (UiUtils.internalMethod05785(this.internalField0205 + 45.0f + f, this.internalField0206 + this.internalField1047 - 36.0f + f2, 11.0, 11.0, d, d2) && bl) {
            internalField0416.add(new InternalType0004(this.internalMethod01145()));
            RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
            return;
        }
        if (typedParameter1015.internalMethod02957() != 0) {
            this.internalField1099 = false;
            return;
        }
        if (this.internalField1099) {
            ColorRGBA colorRGBA = ColorRGBA.fromPixel((float)(d * internalField0389.internalMethod03584()), (float)((double)internalField0267.getHeight() - d2 * internalField0389.internalMethod03584()));
            this.internalMethod07209(colorRGBA);
            this.internalField1099 = false;
        }
        if (UiUtils.internalMethod05785(this.internalField0205 + 7.0f, this.internalField0206 + 6.0f, 8.0, 8.0, d, d2)) {
            this.internalField1099 = true;
            return;
        }
        if (UiUtils.internalMethod05785(this.internalField0205 + this.internalField1048 - 15.0f, this.internalField0206 + 5.0f, 10.0, 10.0, d, d2)) {
            this.internalField0277 = false;
            this.internalField1049 = 2.0f;
            return;
        }
        if (UiUtils.internalMethod05785(this.internalField0205 + this.internalField1048 - 18.0f, this.internalField0206 + 20.0f, 12.0, 70.0, d, d2)) {
            this.internalField1100 = true;
            return;
        }
        if (UiUtils.internalMethod05785(this.internalField0205 + 6.0f, this.internalField0206 + 20.0f, 114.0, 70.0, d, d2)) {
            this.internalField1102 = true;
            return;
        }
        if (UiUtils.internalMethod05785(this.internalField0205 + 6.0f, this.internalField0206 + 102.0f, 100.0, 12.0, d, d2)) {
            this.internalField1101 = true;
            return;
        }
        if (this.internalMethod04931(d, d2)) {
            this.internalField0276 = true;
            this.internalField1046 = (float)(d - (double)this.internalField0205);
            this.internalField1456 = (float)(d2 - (double)this.internalField0206);
        }
    }

    @Override
    public void internalMethod02863(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0276 = false;
        this.internalField1102 = false;
        this.internalField1100 = false;
        this.internalField1101 = false;
    }

    public void internalMethod07209(ColorRGBA colorRGBA) {
        this.internalField1457 = colorRGBA.getHue();
        this.internalField1458 = colorRGBA.getBrightness();
        this.internalField1459 = colorRGBA.getSaturation();
        this.internalField1460 = colorRGBA.getAlpha() / 255.0f;
        this.internalField0813.internalMethod03893(colorRGBA);
    }

    @Generated
    public AnimatedValue internalMethod07207() {
        return this.internalField0809;
    }

    @Generated
    public boolean internalMethod00217() {
        return this.internalField0277;
    }

    @Generated
    public void internalMethod00902(boolean bl) {
        this.internalField0277 = bl;
    }

    @Generated
    public boolean internalMethod00252() {
        return this.internalField0276;
    }

    @Generated
    public boolean internalMethod07882() {
        return this.internalField1099;
    }

    public static class InternalType0004 {
        public final ColorRGBA internalField0777;
        public final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
        public final AnimatedValue internalField0809 = new AnimatedValue(300L, 0.0f, Easing.internalField1626);
        public boolean internalField0277 = true;

        @Generated
        public InternalType0004(ColorRGBA colorRGBA) {
            this.internalField0777 = colorRGBA;
        }

        @Generated
        public ColorRGBA internalMethod03353() {
            return this.internalField0777;
        }

        @Generated
        public AnimatedValue internalMethod03354() {
            return this.internalField0808;
        }

        @Generated
        public AnimatedValue internalMethod04093() {
            return this.internalField0809;
        }

        @Generated
        public boolean internalMethod06825() {
            return this.internalField0277;
        }
    }
}
