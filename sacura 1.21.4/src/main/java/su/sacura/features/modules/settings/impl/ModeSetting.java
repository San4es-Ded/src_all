package su.sacura.features.modules.settings.impl;

import java.util.function.Supplier;
import su.sacura.features.modules.settings.api.Setting;

public class ModeSetting
extends Setting<String> {
    public String[] strings;

    public ModeSetting(String name, String defaultVal, String ... strings) {
        super(name, defaultVal);
        this.strings = strings;
    }

    public int getIndex() {
        int index = 0;
        for (String val : this.strings) {
            if (val.equalsIgnoreCase((String)this.get())) {
                return index;
            }
            ++index;
        }
        return 0;
    }

    public boolean is(String s) {
        return ((String)this.get()).equalsIgnoreCase(s);
    }

    public ModeSetting setVisible(Supplier<Boolean> bool) {
        return (ModeSetting)super.setVisible(bool);
    }

    public ModeSetting setDescription(String description) {
        return (ModeSetting)super.setDescription(description);
    }
}
