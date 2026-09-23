package ru.prism.module.impl.render;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Item Physics",
        desc = "Реалистичная физика предметов на земле: лежат плашмя и не крутятся.",
        category = Category.VISUALS
)
public class ItemPhysics extends Module {

    public static ItemPhysics getInstance() {
        return Instance.get(ItemPhysics.class);
    }
}
