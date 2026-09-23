package su.sacura.features.modules.settings.impl;

import java.util.function.Supplier;
import su.sacura.features.modules.settings.api.Setting;

public class ColorSetting
extends Setting<Integer> {
    public ColorSetting(String name, Integer defaultVal) {
        super(name, defaultVal);
    }

    public ColorSetting setVisible(Supplier<Boolean> bool) {
        return (ColorSetting)super.setVisible(bool);
    }

    public ColorSetting setDescription(String description) {
        return (ColorSetting)super.setDescription(description);
    }
}
