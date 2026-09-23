package haron.settings;

import haron.settings.Setting;
import java.util.function.Supplier;

public class SettingGroup
extends Setting<String> {
    @Override
    public Setting<String> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<String> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public SettingGroup(String string) {
        super(string, "", string);
    }

    public SettingGroup a(Supplier<Boolean> supplier) {
        return (SettingGroup)this.visibleWhen(supplier);
    }
}

