package haron.modules.visuals;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Cosmetics", b="Rabbit / Wheelchair cosmetics on self", c=ModuleCategory.VISUALS)
public class Cosmetics
extends HaronModule {
    private final ModeSetting model = new ModeSetting("Модель", new String[]{"Кролик", "Коляска"}, "Кролик");
    private final NumberSetting size = new NumberSetting("Размер", 1.0f, 0.7f, 1.3f, 0.05f);
    private final BooleanSetting firstPerson = new BooleanSetting("Тело от 1-го лица", false);

    public boolean isFirstPersonBody() {
        return this.firstPerson.get();
    }

    public boolean isRabbit() {
        return this.rabbit();
    }

    public boolean isWheelchair() {
        return this.wheelchair();
    }

    public boolean rabbit() {
        return this.model.d().equals("Кролик");
    }

    public boolean wheelchair() {
        return this.model.d().equals("Коляска");
    }

    public float scale() {
        return this.size.get();
    }
}

