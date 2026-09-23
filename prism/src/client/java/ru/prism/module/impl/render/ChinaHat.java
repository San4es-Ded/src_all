package ru.prism.module.impl.render;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "China Hat",
        desc = "Надевает китайскую шляпу тебе на голову, для стиля.",
        category = Category.VISUALS
)
public class ChinaHat extends Module {


    public static ChinaHat getInstance() {
        return Instance.get(ChinaHat.class);
    }


}
