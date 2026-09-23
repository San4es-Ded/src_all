package su.sacura.features.modules.settings.impl;

import java.util.function.Supplier;
import su.sacura.features.modules.settings.api.Setting;

public class BooleanSetting
extends Setting<Boolean> {
    public BooleanSetting(String name, Boolean defaultVal) {
        super(name, defaultVal);
    }

    public BooleanSetting setVisible(Supplier<Boolean> bool) {
        return (BooleanSetting)super.setVisible(bool);
    }

    public BooleanSetting setDescription(String description) {
        return (BooleanSetting)super.setDescription(description);
    }
}
