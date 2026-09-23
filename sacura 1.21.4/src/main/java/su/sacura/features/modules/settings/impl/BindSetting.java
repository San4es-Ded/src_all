package su.sacura.features.modules.settings.impl;

import java.util.function.Supplier;
import su.sacura.events.input.KeyEvent;
import su.sacura.features.modules.settings.api.Setting;

public class BindSetting
extends Setting<Integer> {
    public BindSetting(String name, Integer defaultVal) {
        super(name, defaultVal);
    }

    public BindSetting setVisible(Supplier<Boolean> bool) {
        return (BindSetting)super.setVisible(bool);
    }

    public BindSetting setDescription(String description) {
        return (BindSetting)super.setDescription(description);
    }

    public boolean matches(KeyEvent event) {
        return ((Integer)this.get()).intValue() == event.key();
    }
}
