package rockstar.client.notification;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.internal.render.*;
import lombok.Generated;
import net.minecraft.client.render.VertexFormats;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.MathUtils;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.internal.render.UiVertexBatch;
import rockstar.client.internal.render.PlainGlyphBatch;
import rockstar.client.notification.NotificationView;

public abstract class IconNotification
extends NotificationView {
    protected final String internalField0248;
    protected String internalField0247;
    protected ColorRGBA internalField0777;

    protected IconNotification(String string, String string2, ColorRGBA colorRGBA) {
        super(2500L);
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField0777 = colorRGBA;
    }

    protected abstract void internalMethod01476(CustomDrawContext localValue1, float localValue2, float localValue3, float localValue4);

    @Override
    public float internalMethod06598() {
        return 25.0f;
    }

    @Override
    public final void internalMethod04213(CustomDrawContext customDrawContext, float f) {
        SizedFont typedValue020 = Fonts.internalField0449.internalMethod01432(7.0f);
        float f2 = typedValue020.internalMethod00965(this.internalField0248) + 26.0f;
        float f3 = 20.0f;
        float f4 = (this.internalMethod06598() - f3) / 2.0f;
        this.internalField1321.internalMethod06645(Easing.internalField1327);
        this.internalField1321.internalMethod07061(300L);
        float f5 = (float)customDrawContext.getScaledWindowWidth() / 2.0f - f2 / 2.0f;
        float f6 = (float)customDrawContext.getScaledWindowHeight() - 87.0f - this.internalField1321.internalMethod07059(f) + f4;
        float f7 = this.internalField0808.internalMethod02881();
        int n = (int)(255.0f * f7);
        HudRenderUtils.internalMethod08976(customDrawContext.getMatrices(), f5 + f2 / 2.0f, f6 + 10.0f, 0.5f + 0.5f * f7);
        if (InterfaceModule.internalMethod09719()) {
            customDrawContext.drawLiquidGlass(f5, f6, f2, 20.0f, 7.0f, 0.08f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * f7 * InterfaceModule.internalMethod07584()));
            customDrawContext.drawSquircle(f5, f6, f2, 20.0f, 7.0f, CornerRadii.internalMethod03908(7.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * MathUtils.internalMethod02587(ThemeColors.internalMethod02435().internalMethod08704(), ThemeColors.internalMethod02435().internalMethod08705(), InterfaceModule.internalMethod07584()) * f7));
        } else {
            customDrawContext.drawBlurredRect(f5, f6, f2, 20.0f, 45.0f, 7.0f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * f7 * InterfaceModule.internalMethod07585()));
            customDrawContext.drawSquircle(f5, f6, f2, 20.0f, 7.0f, CornerRadii.internalMethod03908(7.0f), new ColorRGBA(0.0f, 0.0f, 0.0f).withAlpha((int)(140.25f * f7)));
        }
        this.internalMethod01476(customDrawContext, f5 + 5.0f, f6 + 5.0f, f7);
        float f8 = f5 + 20.0f;
        float f9 = f6 + (20.0f - typedValue020.internalMethod04890()) / 2.0f;
        PlainGlyphBatch typedValue252 = new PlainGlyphBatch(VertexFormats.POSITION_TEXTURE_COLOR, typedValue020.internalMethod01335());
        ColorRGBA colorRGBA = ColorRGBA.WHITE.withAlpha(n);
        if (this.internalField0247 != null && this.internalField0777 != null && this.internalField0248.contains(this.internalField0247)) {
            int n2 = this.internalField0248.indexOf(this.internalField0247);
            String string = this.internalField0248.substring(0, n2);
            String string2 = this.internalField0248.substring(n2 + this.internalField0247.length());
            float f10 = f8;
            if (!string.isEmpty()) {
                customDrawContext.drawText(typedValue020, string, f10, f9, colorRGBA);
                f10 += typedValue020.internalMethod00965(string);
            }
            customDrawContext.drawText(typedValue020, this.internalField0247, f10, f9, this.internalField0777.withAlpha(n));
            f10 += typedValue020.internalMethod00965(this.internalField0247);
            if (!string2.isEmpty()) {
                customDrawContext.drawText(typedValue020, string2, f10, f9, colorRGBA);
            }
        } else {
            customDrawContext.drawText(typedValue020, this.internalField0248, f8, f9, colorRGBA);
        }
        ((UiVertexBatch)typedValue252).internalMethod09053();
        HudRenderUtils.internalMethod00012(customDrawContext.getMatrices());
    }

    @Generated
    public String internalMethod07467() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod04018() {
        return this.internalField0247;
    }

    @Generated
    public ColorRGBA internalMethod07257() {
        return this.internalField0777;
    }
}
