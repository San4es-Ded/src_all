package su.sacura.features.modules.settings.impl;

import java.util.function.Supplier;
import su.sacura.features.modules.settings.api.Setting;

public class SliderSetting
extends Setting<Float> {
    public float min;
    public float max;
    public float increment;

    public SliderSetting(String name, float defaultVal, float min, float max, float increment) {
        super(name, Float.valueOf(defaultVal));
        this.min = min;
        this.max = max;
        this.increment = increment;
    }

    public SliderSetting setVisible(Supplier<Boolean> bool) {
        return (SliderSetting)super.setVisible(bool);
    }

    public SliderSetting setDescription(String description) {
        return (SliderSetting)super.setDescription(description);
    }
}
