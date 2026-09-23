package ru.prism.module.impl.utils;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Item Scroller",
        desc = "Быстро перекладывает предметы, пока зажаты Shift и левая кнопка мыши.",
        category = Category.UTILITIES
)
public class ItemScroller extends Module {

    public static ItemScroller getInstance() {
        return Instance.get(ItemScroller.class);
    }

    public SliderSetting delay = new SliderSetting(this, "Задержка", 50, 10, 500, 10);

}
