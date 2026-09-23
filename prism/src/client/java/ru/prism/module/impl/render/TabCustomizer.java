package ru.prism.module.impl.render;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.module.api.settings.impl.ModeSetting;

@ModuleInfo(
        name = "Tab Customizer",
        desc = "Причёсывает табличку игроков: подсвечивает своих, рисует головы и пинг, прячет шапку с подвалом.",
        category = Category.VISUALS
)
public class TabCustomizer extends Module {

    public final BooleanSetting selfHighlight = new BooleanSetting(this, "Подсветка себя", true);
    public final BooleanSetting friendHighlight = new BooleanSetting(this, "Подсветка друзей", true);
    public final ColorSetting friendColor = new ColorSetting(this, "Цвет друзей", 65407);
    public final BooleanSetting partyHighlight = new BooleanSetting(this, "Подсветка группы", true);
    public final BooleanSetting showHeads = new BooleanSetting(this, "Головы игроков", true);
    public final ModeSetting pingDisplay = new ModeSetting(this, "Пинг", "Цифры", "Иконка", "Скрыть");
    public final BooleanSetting friendsOnTop = new BooleanSetting(this, "Друзья наверху", true);
    public final BooleanSetting hideHeader = new BooleanSetting(this, "Скрыть шапку", false);
    public final BooleanSetting hideFooter = new BooleanSetting(this, "Скрыть подвал", false);
}
