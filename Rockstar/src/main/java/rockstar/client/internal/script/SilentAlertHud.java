package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.util.math.MathHelper;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.ui.Insets;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.ui.QuadColorGradient;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.script.UiElementFactory;
import rockstar.client.internal.script.DynamicIslandStatus;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.UiNode;
import rockstar.client.notification.NotificationType;
import rockstar.client.notification.SilentNotification;
import rockstar.client.ui.UiContainer;

public class SilentAlertHud
extends DynamicIslandStatus {
    private static final ColorRGBA internalField0777 = new ColorRGBA(74.0f, 222.0f, 128.0f);
    private static final ColorRGBA internalField0776 = new ColorRGBA(239.0f, 68.0f, 68.0f);
    SilentNotification internalField0706;
    private UiNode internalField0633;

    public SilentAlertHud(MultiSelectSetting typedValue173) {
        super(typedValue173, "alerts");
    }

    @Override
    public void prepare(DynamicIslandHud typedValue201) {
        this.internalField0706 = this.internalMethod05721();
    }

    @Override
    public UiNode content(DynamicIslandHud typedValue201) {
        if (this.internalField0633 == null) {
            UiContainer typedValue007 = UiElementFactory.internalMethod01948(15.0f, Insets.internalMethod00105(0.0f, 5.0f, 0.0f, 4.5f), 4.0f).internalMethod01855(TextAlignment.internalField0621).internalMethod07178((iII, typedValue006) -> {
                if (this.internalField0706 == null) {
                    return;
                }
                float f = this.animation.internalMethod02881() * this.internalField0706.internalMethod06523().internalMethod02881();
                if (f <= 0.0f) {
                    return;
                }
                ColorRGBA colorRGBA = this.internalMethod06635();
                ColorRGBA colorRGBA2 = colorRGBA.withAlpha(71.4f * f);
                ColorRGBA colorRGBA3 = colorRGBA.withAlpha(0.0f);
                float f2 = typedValue201.internalMethod06413().internalMethod02881();
                iII.drawSquircle(typedValue006.x(), typedValue006.y(), typedValue006.w() * 0.69f, typedValue006.h(), 2.0f, CornerRadii.internalMethod07937(f2, f2), new QuadColorGradient(colorRGBA2, colorRGBA2, colorRGBA3, colorRGBA3));
            });
            typedValue007.internalMethod03907(new InternalType0510());
            typedValue007.internalMethod03907(new InternalType0511());
            this.internalField0633 = typedValue007;
        }
        return this.internalField0633;
    }

    @Override
    public boolean canShow() {
        List<SilentNotification> list = this.internalMethod01782();
        if (list.isEmpty()) {
            return false;
        }
        SilentNotification typedValue050 = list.getLast();
        return !typedValue050.internalMethod02160().internalMethod02365(typedValue050.internalMethod06599());
    }

    private SilentNotification internalMethod05721() {
        List<SilentNotification> list = this.internalMethod01782();
        return list.isEmpty() ? null : list.getLast();
    }

    public List<SilentNotification> internalMethod01782() {
        return RockstarClient.getInstance().internalMethod02503().internalMethod02336().stream().filter(typedValue047 -> typedValue047 instanceof SilentNotification).map(typedValue047 -> (SilentNotification)typedValue047).toList();
    }

    public ColorRGBA internalMethod06635() {
        if (this.internalField0706 == null) {
            return internalField0777;
        }
        return switch (this.internalField0706.internalMethod05376()) {
            case NotificationType.internalField0704 -> internalField0777;
            case NotificationType.internalField0705 -> internalField0776;
            default -> this.internalField0706.internalMethod05376().internalMethod03291();
        };
    }

    final class InternalType0510
    extends UiNode {
        InternalType0510() {
            this.size(6.0f, 6.0f);
            this.interactive(false);
        }

        @Override
        protected void drawSelf(UiRenderContext iII, float f) {
            if (SilentAlertHud.this.internalField0706 == null) {
                return;
            }
            for (SilentNotification typedValue050 : SilentAlertHud.this.internalMethod01782()) {
                typedValue050.internalMethod06523().internalMethod07061(500L);
                typedValue050.internalMethod06523().internalMethod07062(SilentAlertHud.this.internalField0706 == typedValue050);
            }
            float f2 = SilentAlertHud.this.animation.internalMethod02881() * SilentAlertHud.this.internalField0706.internalMethod06523().internalMethod02881();
            if (f2 <= 0.0f) {
                return;
            }
            long l = SilentAlertHud.this.internalField0706.internalMethod06599();
            float f3 = l <= 0L ? 0.0f : MathHelper.clamp((float)(1.0f - (float)SilentAlertHud.this.internalField0706.internalMethod02160().internalMethod00700() / (float)l), (float)0.0f, (float)1.0f);
            float f4 = this.x() + this.w() / 2.0f - 6.0f * (1.0f - f2);
            float f5 = this.y() + this.h() / 2.0f;
            float f6 = Math.min(this.w(), this.h()) / 2.0f + 0.5f;
            ColorRGBA colorRGBA = ThemeColors.internalMethod08459().withAlpha(40.8f * f2);
            ColorRGBA colorRGBA2 = SilentAlertHud.this.internalMethod06635().withAlpha(255.0f * f2);
            iII.drawCircleProgress(f4, f5, f6, 1.2f, 1.0f, colorRGBA);
            iII.drawCircleProgress(f4, f5, f6, 1.2f, f3, colorRGBA2);
        }
    }

    final class InternalType0511
    extends UiNode {
        InternalType0511() {
            this.interactive(false);
        }

        @Override
        protected void measure() {
            String string = SilentAlertHud.this.internalField0706 == null ? "" : SilentAlertHud.this.internalField0706.internalMethod03176();
            this.prefW = string.isEmpty() ? 0.0f : Fonts.internalField0449.internalMethod01432(7.0f).internalMethod00965(string);
            this.prefH = Fonts.internalField0449.internalMethod01432(7.0f).internalMethod04890();
        }

        @Override
        protected void drawSelf(UiRenderContext iII, float f) {
            if (SilentAlertHud.this.internalField0706 == null) {
                return;
            }
            String string = SilentAlertHud.this.internalField0706.internalMethod03176();
            float f2 = SilentAlertHud.this.animation.internalMethod02881();
            float f3 = SilentAlertHud.this.internalField0706.internalMethod06523().internalMethod02881();
            iII.drawText(Fonts.internalField0449.internalMethod01432(7.0f), string, this.x() + 6.0f * (1.0f - f2 * f3), this.y(), ThemeColors.internalMethod08459().withAlpha(255.0f * f3));
        }
    }
}

