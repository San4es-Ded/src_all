package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;

@ModuleInfo(a="Cooldowns HUD", b="Displays item cooldowns on screen.", c=ModuleCategory.HUD)
public class CooldownsHud
extends HaronModule {
    private final NumberSetting scale = new NumberSetting("Масштаб", 1.0f, 1.0f, 2.0f, 0.1f);

    public CooldownsHud() {
        this.collectSettings();
    }

    public NumberSetting n() {
        return this.scale;
    }
}

