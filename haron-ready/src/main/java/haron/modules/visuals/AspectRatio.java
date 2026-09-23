package haron.modules.visuals;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ModeSetting;

@ModuleInfo(a="Aspect Ratio", b="Changes the screen aspect ratio", c=ModuleCategory.VISUALS)
public class AspectRatio
extends HaronModule {
    private static final String MODE_16_9 = "16:9";
    private static final String MODE_4_3 = "4:3";
    private static final String MODE_21_9 = "21:9";
    private static final String MODE_1_1 = "1:1";
    private static final String MODE_CUSTOM = "Custom";
    public final ModeSetting a = new ModeSetting("Режим", new String[]{"16:9", "4:3", "21:9", "1:1", "Custom"}, "16:9");
    public final NumberSetting b = new NumberSetting("Кастомное соотношение", 1.78f, 0.5f, 3.0f, 0.01f).a(() -> {
        return this.a.b("Custom");
    });

    public float n() {
        switch (this.a.d()) {
            case "4:3": {
                return 1.3333334f;
            }
            case "21:9": {
                return 2.3333333f;
            }
            case "1:1": {
                return 1.0f;
            }
            case "Custom": {
                return this.b.a();
            }
        }
        return 1.7777778f;
    }
}

