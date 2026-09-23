package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.render.*;
import rockstar.client.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.Insets;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.ui.QuadColorGradient;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.script.EmojiTextNode;
import rockstar.client.internal.script.UiElementFactory;
import rockstar.client.internal.script.DynamicIslandStatus;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public class DynamicIslandDefaultStatus
extends DynamicIslandStatus {
    private UiNode internalField0633;

    public DynamicIslandDefaultStatus(MultiSelectSetting typedValue173) {
        super(typedValue173, "default");
    }

    @Override
    public UiNode content(DynamicIslandHud typedValue201) {
        if (this.internalField0633 == null) {
            UiContainer typedValue007 = UiElementFactory.internalMethod01948(15.0f, Insets.internalMethod00105(0.0f, 5.0f, 0.0f, 4.0f), 4.0f).internalMethod01855(TextAlignment.internalField0621).internalMethod07178((iII, typedValue006) -> {
                ColorRGBA colorRGBA = ThemeColors.internalMethod02531();
                ColorRGBA colorRGBA2 = colorRGBA.withAlpha(71.4f);
                ColorRGBA colorRGBA3 = colorRGBA.withAlpha(0.0f);
                float f = typedValue201.internalMethod06413().internalMethod02881();
                iII.drawSquircle(typedValue006.x(), typedValue006.y(), typedValue006.w() * 0.69f, typedValue006.h(), 2.0f, CornerRadii.internalMethod07937(f, f), new QuadColorGradient(colorRGBA2, colorRGBA2, colorRGBA3, colorRGBA3));
            });
            typedValue007.internalMethod03907(new EmojiTextNode(Fonts.internalField0449.internalMethod01432(7.0f), typedValue201::internalMethod07542, ThemeColors::internalMethod08459, () -> 10.0f * (1.0f - this.animation.internalMethod02881())));
            this.internalField0633 = typedValue007;
        }
        return this.internalField0633;
    }

    @Override
    public boolean canShow() {
        return true;
    }
}

