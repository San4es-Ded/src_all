package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import globals.client.Information;
import globals.shared.proto.Packets;
import pyrock.utility.render.ColorRGBA;
import pyrock.utility.render.CustomDrawContext;
import rockstar.modules.visual.InterfaceModule;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.ToastNotification;
import rockstar.client.render.CornerRadii;
import rockstar.client.animation.Easing;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.MathUtils;
import rockstar.client.render.HudRenderUtils;

public class PlayerJoinNotification
extends ToastNotification {
    private final Packets.InternalType0451 internalField0043;
    private final String internalField0248;
    private final String internalField0247;

    public PlayerJoinNotification(Packets.InternalType0451 nestedValue2055, String string) {
        super(5000L);
        this.internalField0043 = nestedValue2055;
        this.internalField0248 = nestedValue2055.username();
        this.internalField0247 = string;
    }

    @Override
    public final void internalMethod06653(CustomDrawContext customDrawContext, float f) {
        String string = this.internalField0247.length() > 32 ? this.internalField0247.substring(0, 32) + " " : this.internalField0247;
        float f2 = Math.max(Fonts.internalField0450.internalMethod01432(7.0f).internalMethod00965(this.internalField0248), Fonts.internalField0449.internalMethod01432(6.0f).internalMethod00965(string));
        float f3 = f2 + 40.0f;
        this.internalField1321.internalMethod06645(Easing.internalField1327);
        this.internalField1321.internalMethod07061(300L);
        float f4 = (float)customDrawContext.getScaledWindowWidth() - f3 - 8.0f;
        float f5 = (float)customDrawContext.getScaledWindowHeight() - 32.0f - this.internalField1321.internalMethod07059(f);
        float f6 = 26.0f;
        int n = (int)(255.0f * this.internalField0808.internalMethod02881());
        HudRenderUtils.internalMethod08976(customDrawContext.getMatrices(), f4 + f3 / 2.0f, f5 + 12.0f + f6 / 2.0f, 0.5f + 0.5f * this.internalField0808.internalMethod02881());
        if (InterfaceModule.internalMethod09719()) {
            customDrawContext.drawLiquidGlass(f4, f5, f3, f6, 7.0f, 0.08f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0808.internalMethod02881() * InterfaceModule.internalMethod07584()));
            customDrawContext.drawSquircle(f4, f5, f3, f6, 7.0f, CornerRadii.internalMethod03908(7.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * MathUtils.internalMethod02587(ThemeColors.internalMethod02435().internalMethod08704(), ThemeColors.internalMethod02435().internalMethod08705(), InterfaceModule.internalMethod07584()) * this.internalField0808.internalMethod02881()));
        } else {
            customDrawContext.drawBlurredRect(f4, f5, f3 + 2.0f, f6, 45.0f, 7.0f, CornerRadii.internalMethod03908(7.0f), ColorRGBA.WHITE.withAlpha(255.0f * this.internalField0808.internalMethod02881() * InterfaceModule.internalMethod07585()));
            customDrawContext.drawSquircle(f4, f5, f3 + 2.0f, f6, 7.0f, CornerRadii.internalMethod03908(7.0f), new ColorRGBA(0.0f, 0.0f, 0.0f).withAlpha((int)(140.25f * this.internalField0808.internalMethod02881())));
        }
        customDrawContext.drawText(Fonts.internalField0450.internalMethod01432(7.0f), this.internalField0248, f4 + 10.0f, f5 + 7.0f, ColorRGBA.WHITE.withAlpha(n));
        if (string.length() > 32) {
            customDrawContext.drawFadeoutText(Fonts.internalField0449.internalMethod01432(6.0f), string, f4 + 10.0f, f5 + 15.0f, ColorRGBA.WHITE.withAlpha(n), 0.7f, 1.0f, f3 - 16.0f);
        } else {
            customDrawContext.drawText(Fonts.internalField0449.internalMethod01432(6.0f), string, f4 + 10.0f, f5 + 15.0f, ColorRGBA.WHITE.withAlpha(n));
        }
        HudRenderUtils.internalMethod00012(customDrawContext.getMatrices());
    }
}
