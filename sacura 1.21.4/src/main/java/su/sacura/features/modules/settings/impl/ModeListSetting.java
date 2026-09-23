package su.sacura.features.modules.settings.impl;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;
import su.sacura.features.modules.settings.api.Setting;

public class ModeListSetting
        extends Setting<List<BooleanSetting>> {
    public ModeListSetting(String name, BooleanSetting ... strings) {
        super(name, Arrays.asList(strings));
    }

    public BooleanSetting getValueByName(String settingName) {
        // Исправлено: убран raw тип List, добавлена правильная типизация
        return this.get().stream()
                .filter(booleanSetting -> booleanSetting.getName().equalsIgnoreCase(settingName))
                .findFirst()
                .orElse(null);
    }

    public BooleanSetting get(int index) {
        // Исправлено: убран raw тип List
        return this.get().get(index);
    }

    public ModeListSetting setVisible(Supplier<Boolean> bool) {
        return (ModeListSetting)super.setVisible(bool);
    }

    public ModeListSetting setDescription(String description) {
        return (ModeListSetting)super.setDescription(description);
    }
}