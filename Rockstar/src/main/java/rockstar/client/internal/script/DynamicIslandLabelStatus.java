package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.render.*;
import rockstar.client.*;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.Insets;
import rockstar.client.render.Fonts;
import rockstar.client.ui.TextAlignment;
import rockstar.client.render.CornerRadii;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.script.UiElementFactory;
import rockstar.client.internal.script.DynamicIslandStatus;
import rockstar.client.ui.AnimatedNumberLabel;
import rockstar.client.ui.ThemeColors;
import rockstar.client.server.ServerUtils;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.UiNode;
import rockstar.client.ui.UiContainer;

public class DynamicIslandLabelStatus
extends DynamicIslandStatus
implements MinecraftClientAccess {
    private String internalField0248 = "";
    private String internalField0247 = "";
    private int internalField0227 = -1;
    private String internalField1077 = "text";
    private ColorRGBA internalField0777 = ThemeColors.internalMethod02531();
    private UiNode internalField0633;

    public DynamicIslandLabelStatus(MultiSelectSetting typedValue173, String string) {
        super(typedValue173, string);
    }

    @Override
    public UiNode content(DynamicIslandHud typedValue201) {
        if (this.internalField0633 == null) {
            UiContainer typedValue007 = UiElementFactory.internalMethod01948(15.0f, Insets.internalMethod00105(0.0f, 4.0f, 0.0f, 4.0f), 3.5f).internalMethod01855(TextAlignment.internalField0621);
            UiContainer typedValue008 = UiElementFactory.internalMethod04233(() -> -20.0f * (1.0f - this.animation.internalMethod02881())).internalMethod05895().internalMethod09266(8.0f).internalMethod03514(Insets.internalMethod00105(0.0f, 2.5f, 0.0f, 3.0f)).internalMethod01855(TextAlignment.internalField0621).internalMethod07178((iII, typedValue006) -> iII.drawRoundedRect(typedValue006.x(), typedValue006.y(), typedValue006.w(), typedValue006.h(), CornerRadii.internalMethod03908(3.0f), this.internalField0777.withAlpha(255.0f * this.animation.internalMethod02881())));
            typedValue008.internalMethod03907(UiElementFactory.internalMethod03257(Fonts.internalField0449.internalMethod01432(6.0f), () -> this.internalField0248, () -> ThemeColors.internalMethod01303(this.internalField0777).withAlpha(255.0f * this.animation.internalMethod02881())));
            typedValue008.internalMethod03907(new AnimatedNumberLabel(Fonts.internalField0449.internalMethod01432(6.0f), () -> this.internalField0227).internalMethod02983(5.0f).internalMethod00089().internalMethod05034(() -> ThemeColors.internalMethod01303(this.internalField0777).withAlpha(255.0f * this.animation.internalMethod02881())).interactive(false));
            typedValue008.internalMethod03907(UiElementFactory.internalMethod03257(Fonts.internalField0449.internalMethod01432(6.0f), () -> this.internalField0247, () -> ThemeColors.internalMethod01303(this.internalField0777).withAlpha(255.0f * this.animation.internalMethod02881())));
            typedValue007.internalMethod03907(typedValue008);
            typedValue007.internalMethod03907(UiElementFactory.internalMethod04195(Fonts.internalField0449.internalMethod01432(7.0f), () -> this.internalField1077, () -> ThemeColors.internalMethod08459().withAlpha(255.0f * this.animation.internalMethod02881()), () -> 10.0f * (1.0f - this.animation.internalMethod02881())));
            this.internalField0633 = typedValue007;
        }
        return this.internalField0633;
    }

    public void internalMethod03185(String string, int n, String string2, ColorRGBA colorRGBA) {
        this.internalMethod01781("", string, n, string2, colorRGBA);
    }

    public void internalMethod01781(String string, String string2, int n, String string3, ColorRGBA colorRGBA) {
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField0227 = n;
        this.internalField1077 = string3;
        this.internalField0777 = colorRGBA;
    }

    @Override
    public boolean canShow() {
        return ServerUtils.internalField0277;
    }
}

