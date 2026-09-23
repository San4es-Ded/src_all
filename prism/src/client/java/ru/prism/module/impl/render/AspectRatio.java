package ru.prism.module.impl.render;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Aspect Ratio",
        desc = "Меняет соотношение сторон экрана — вытягивай картинку как душе угодно.",
        category = Category.VISUALS
)
public class AspectRatio extends Module {

    public static AspectRatio getInstance() {
        return Instance.get(AspectRatio.class);
    }

    public final ModeSetting mode = new ModeSetting(this, "Режим", "16:9", "16:10", "4:3", "21:9", "1:1", "Свой");
    public final SliderSetting ratio = new SliderSetting(this, "Соотношение", 1.78F, 0.5F, 3.0F, 0.01F)
            .setVisible(() -> mode.is("Свой"));

    public float getAspectRatio() {
        return switch (mode.getValue()) {
            case "16:10" -> 1.6F;
            case "4:3" -> 1.3333334F;
            case "21:9" -> 2.3333333F;
            case "1:1" -> 1.0F;
            case "Свой" -> ratio.getValue();
            default -> 1.7777778F;
        };
    }
}
