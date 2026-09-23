package rockstar.client.notification;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.MathUtils;
import rockstar.client.render.HudRenderUtils;
import rockstar.client.notification.NotificationView;
import rockstar.client.notification.NotificationType;

public class DetailedNotification
extends NotificationView {
    private final NotificationType internalField0704;
    private final String internalField0248;
    private final String internalField0247;

    public DetailedNotification(NotificationType typedValue048, String string, String string2) {
        super(2500L);
        this.internalField0704 = typedValue048;
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    @Override
    public final void internalMethod04213(CustomDrawContext customDrawContext, float f) {
        float f2 = Math.max(Fonts.internalField0450.internalMethod01432(7.0f).internalMethod00965(this.internalField0248), Fonts.internalField0449.internalMethod01432(6.0f).internalMethod00965(this.internalField0247));
        float f3 = f2 + 32.0f;
        this.internalField1321.internalMethod06645(Easing.internalField1327);
        this.internalField1321.internalMethod07061(300L);
        float f4 = (float)customDrawContext.getScaledWindowWidth() / 2.0f - f3 / 2.0f;
        float f5 = (float)customDrawContext.getScaledWindowHeight() - 90.0f - this.internalField1321.internalMethod07059(f);
        float f6 = 26.0f;
        int n = (int)(255.0f * this.internalField0808.internalMethod02881());
        HudRenderUtils.internalMethod08976(customDrawContext.getMatrices(), f4 + f3 / 2.0f, f5 + 12.0f + f6 / 2.0f, 0.5f + 0.5f * this.internalField0808.internalMethod02881());
        if (InterfaceModule.internalMethod09719()) {
            customDrawContext.drawLiquidGlass(f4, f5, f3, f6, 7.0f, 0.08f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0808.internalMethod02881() * InterfaceModule.internalMethod07584()));
            customDrawContext.drawSquircle(f4, f5, f3, f6, 7.0f, CornerRadii.internalMethod03908(7.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * MathUtils.internalMethod02587(ThemeColors.internalMethod02435().internalMethod08704(), ThemeColors.internalMethod02435().internalMethod08705(), InterfaceModule.internalMethod07584()) * this.internalField0808.internalMethod02881()));
        } else {
            customDrawContext.drawBlurredRect(f4, f5, f3, f6, 45.0f, 7.0f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0808.internalMethod02881() * InterfaceModule.internalMethod07585()));
            customDrawContext.drawSquircle(f4, f5, f3, f6, 7.0f, CornerRadii.internalMethod03908(7.0f), new ColorRGBA(0.0f, 0.0f, 0.0f).withAlpha((int)(140.25f * this.internalField0808.internalMethod02881())));
            customDrawContext.drawRoundedRect(f4 + f6 / 2.0f - 9.0f, f5 + f6 / 2.0f - 9.0f, 18.0f, 18.0f, CornerRadii.internalMethod03908(4.0f), new ColorRGBA(0.0f, 0.0f, 0.0f).withAlpha((int)(51.0f * this.internalField0808.internalMethod02881())));
        }
        customDrawContext.drawIcon(this.internalField0704.internalMethod07259(), f4 + f6 / 2.0f - 5.0f, f5 + f6 / 2.0f - 5.0f, 10.0f, this.internalField0704.internalMethod03291().withAlpha((float)n * 0.8f));
        customDrawContext.drawText(Fonts.internalField0450.internalMethod01432(7.0f), this.internalField0248, f4 + 27.0f, f5 + 7.0f, ColorRGBA.WHITE.withAlpha(n));
        customDrawContext.drawText(Fonts.internalField0449.internalMethod01432(6.0f), this.internalField0247, f4 + 27.0f, f5 + 15.0f, ColorRGBA.WHITE.withAlpha(n));
        HudRenderUtils.internalMethod00012(customDrawContext.getMatrices());
    }

    @Generated
    public NotificationType internalMethod01027() {
        return this.internalField0704;
    }

    @Generated
    public String internalMethod01429() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod06047() {
        return this.internalField0247;
    }
}
