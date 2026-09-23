package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;

@ModuleInfo(a="Saturation HUD", b="Отображение полоски сытости над едой.", c=ModuleCategory.HUD)
public class SaturationHud
extends HaronModule {
    private final NumberSetting scale = new NumberSetting("Масштаб", 1.0f, 0.8f, 2.0f, 0.1f);

    public SaturationHud() {
        this.collectSettings();
    }

    public NumberSetting n() {
        int n = 610;
        return this.scale;
    }
}

