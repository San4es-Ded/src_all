package rockstar.client.internal.ui;





import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.setting.Setting;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.setting.RangeSetting;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.internal.ui.SettingRowContainer;
import rockstar.client.animation.Easing;
import rockstar.client.ui.UiContainer;

public final class SettingComponentFactory {
    private SettingComponentFactory() {
    }

    public static boolean internalMethod00080(Setting typedValue157) {
        return typedValue157 instanceof BooleanSetting || typedValue157 instanceof SliderSetting || typedValue157 instanceof RangeSetting || typedValue157 instanceof MultiSelectSetting || typedValue157 instanceof ModeSetting;
    }

    public static UiContainer internalMethod03724(Setting typedValue157) {
        return SettingComponentFactory.internalMethod07309(typedValue157).internalMethod09936().internalMethod06712(typedValue157::isVisible, Easing.internalField1828, 220L);
    }

    public static UiContainer internalMethod07309(Setting typedValue157) {
        UiContainer typedValue006 = typedValue157.createComponent();
        if (typedValue006 == null) {
            return new SettingRowContainer(typedValue157);
        }
        return typedValue006.internalMethod09609().internalMethod02146(0.0f, 9.0f);
    }
}

