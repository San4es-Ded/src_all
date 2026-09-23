package rockstar.client.ui;



import rockstar.client.setting.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.client.util.math.Vector2f;
import org.lwjgl.glfw.GLFW;
import pyrock.utility.render.Rect;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.setting.Setting;
import rockstar.client.setting.Vector2Setting;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.RegistryListSetting;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.setting.ColorSetting;
import rockstar.client.setting.GradientColorSetting;
import rockstar.client.setting.SectionSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.VectorRangeSetting;
import rockstar.client.setting.RangeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.setting.TextSetting;
import rockstar.client.ui.SettingComponent;
import rockstar.client.ui.Vector2SettingComponent;
import rockstar.client.ui.KeybindSettingComponent;
import rockstar.client.ui.RegistryListSettingComponent;
import rockstar.client.ui.BooleanSettingComponent;
import rockstar.client.ui.ButtonSettingComponent;
import rockstar.client.ui.ColorSettingComponent;
import rockstar.client.ui.GradientColorSettingComponent;
import rockstar.client.ui.SectionSettingComponent;
import rockstar.client.ui.ModeSettingComponent;
import rockstar.client.ui.VectorRangeSettingComponent;
import rockstar.client.ui.RangeSettingComponent;
import rockstar.client.ui.MultiSelectSettingComponent;
import rockstar.client.ui.SliderSettingComponent;
import rockstar.client.ui.TextSettingComponent;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.ui.LegacyUiElement;

public final class UiUtils {
    public static float internalMethod07116(float f, float f2) {
        return (float)Math.ceil(f2 / 2.0f - f / 2.0f);
    }

    public static double internalMethod07115(double d, double d2) {
        return Math.ceil(d2 / 2.0 - d / 2.0);
    }

    public static boolean internalMethod05786(double d, double d2, double d3, double d4, int n, int n2) {
        return (double)n >= d && (double)n < d + d3 && (double)n2 >= d2 && (double)n2 < d2 + d4;
    }

    public static boolean internalMethod06450(double d, double d2, double d3, double d4, UiRenderContext iII) {
        return UiUtils.internalMethod05786(d, d2, d3, d4, iII.internalMethod05259(), iII.internalMethod05261());
    }

    public static boolean internalMethod01807(Rect rect, double d, double d2) {
        return UiUtils.internalMethod05785(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight(), d, d2);
    }

    public static boolean internalMethod03150(LegacyUiElement typedValue001, double d, double d2) {
        return UiUtils.internalMethod05785(typedValue001.internalMethod02045(), typedValue001.internalMethod02048(), typedValue001.internalMethod08827(), typedValue001.internalMethod07809(), d, d2);
    }

    public static boolean internalMethod05785(double d, double d2, double d3, double d4, double d5, double d6) {
        return d5 >= d && d5 < d + d3 && d6 >= d2 && d6 < d2 + d4;
    }

    public static float internalMethod04852(float f, float f2, float f3, float f4, double d) {
        return (float)(Math.min(1.0, Math.max(0.0, (d - (double)f3) / (double)f4)) * (double)(f2 - f)) + f;
    }

    public static float internalMethod01005(float f, float f2, float f3, float f4, double d) {
        return (float)((d - (double)f3) / (double)f4 * (double)(f2 - f)) + f;
    }

    public static float internalMethod07541(float f, float f2, float f3) {
        return (f - f2) / (f3 - f2);
    }

    public static Vector2f internalMethod03634() {
        return new Vector2f((float)(MinecraftClientAccess.internalField0149.mouse.getX() / ScreenMetricsAccess.internalField0389.internalMethod03584()), (float)(MinecraftClientAccess.internalField0149.mouse.getY() / ScreenMetricsAccess.internalField0389.internalMethod03584()));
    }

    public static boolean internalMethod07370(int n) {
        return GLFW.glfwGetMouseButton((long)MinecraftClientAccess.internalField0149.getWindow().getHandle(), (int)n) == 1;
    }

    public static SettingComponent internalMethod06603(Setting typedValue157, LegacyUiElement typedValue001) {
        SettingComponent typedValue209 = null;
        if (typedValue157 instanceof BooleanSetting) {
            BooleanSetting typedValue164 = (BooleanSetting)typedValue157;
            typedValue209 = new BooleanSettingComponent(typedValue164, typedValue001);
        } else if (typedValue157 instanceof KeybindSetting) {
            KeybindSetting typedValue161 = (KeybindSetting)typedValue157;
            typedValue209 = new KeybindSettingComponent(typedValue161, typedValue001);
        } else if (typedValue157 instanceof ColorSetting) {
            ColorSetting typedValue167 = (ColorSetting)typedValue157;
            typedValue209 = new ColorSettingComponent(typedValue167, typedValue001);
        } else if (typedValue157 instanceof GradientColorSetting) {
            GradientColorSetting typedValue168 = (GradientColorSetting)typedValue157;
            typedValue209 = new GradientColorSettingComponent(typedValue168, typedValue001);
        } else if (typedValue157 instanceof ModeSetting) {
            ModeSetting typedValue170 = (ModeSetting)typedValue157;
            typedValue209 = new ModeSettingComponent(typedValue170, typedValue001);
        } else if (typedValue157 instanceof RangeSetting) {
            RangeSetting typedValue172 = (RangeSetting)typedValue157;
            typedValue209 = new RangeSettingComponent(typedValue172, typedValue001);
        } else if (typedValue157 instanceof VectorRangeSetting) {
            VectorRangeSetting typedValue171 = (VectorRangeSetting)typedValue157;
            typedValue209 = new VectorRangeSettingComponent(typedValue171, typedValue001);
        } else if (typedValue157 instanceof Vector2Setting) {
            Vector2Setting typedValue160 = (Vector2Setting)typedValue157;
            typedValue209 = new Vector2SettingComponent(typedValue160, typedValue001);
        } else if (typedValue157 instanceof ButtonSetting) {
            ButtonSetting typedValue166 = (ButtonSetting)typedValue157;
            typedValue209 = new ButtonSettingComponent(typedValue166, typedValue001);
        } else if (typedValue157 instanceof RegistryListSetting) {
            RegistryListSetting typedValue163 = (RegistryListSetting)typedValue157;
            typedValue209 = new RegistryListSettingComponent(typedValue163, typedValue001);
        } else if (typedValue157 instanceof MultiSelectSetting) {
            MultiSelectSetting typedValue173 = (MultiSelectSetting)typedValue157;
            typedValue209 = new MultiSelectSettingComponent(typedValue173, typedValue001);
        } else if (typedValue157 instanceof SliderSetting) {
            SliderSetting typedValue174 = (SliderSetting)typedValue157;
            typedValue209 = new SliderSettingComponent(typedValue174, typedValue001);
        } else if (typedValue157 instanceof TextSetting) {
            TextSetting typedValue179 = (TextSetting)typedValue157;
            typedValue209 = new TextSettingComponent(typedValue179, typedValue001);
        } else if (typedValue157 instanceof SectionSetting) {
            SectionSetting typedValue169 = (SectionSetting)typedValue157;
            typedValue209 = new SectionSettingComponent(typedValue169, typedValue001);
        }
        if (typedValue209 != null) {
            typedValue209.internalMethod02325();
        }
        return typedValue209;
    }

    @Generated
    private UiUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

