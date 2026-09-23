package su.sacura.features.modules.settings.impl;

import java.util.function.Supplier;
import su.sacura.features.modules.settings.api.Setting;

public class StringSetting
extends Setting<String> {
    public StringSetting(String name, String defaultVal) {
        super(name, defaultVal);
    }

    public StringSetting setVisible(Supplier<Boolean> bool) {
        return (StringSetting)super.setVisible(bool);
    }

    public StringSetting setDescription(String description) {
        return (StringSetting)super.setDescription(description);
    }
}
