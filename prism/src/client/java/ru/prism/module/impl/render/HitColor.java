package ru.prism.module.impl.render;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.utils.colors.ColorUtil;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Hit Color",
        desc = "Красит сущности при получении урона: цветом клиента или своим.",
        category = Category.VISUALS
)
public class HitColor extends Module {

    public static boolean armorHurt;

    public final BooleanSetting clientColor = new BooleanSetting(this, "Цвет клиента", true);
    public final ColorSetting customColor = new ColorSetting(this, "Кастомный цвет", ColorUtil.RED)
            .setVisible(() -> !clientColor.getValue());

    public static HitColor getInstance() {
        return Instance.get(HitColor.class);
    }

    public int getTintColor() {
        return clientColor.getValue() ? ColorUtil.getClientColor1(1) : customColor.getValue();
    }
}
