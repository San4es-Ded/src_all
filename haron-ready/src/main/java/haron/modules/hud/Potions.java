package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Potions", b="Shows active potion effects.", c=ModuleCategory.HUD)
public class Potions
extends HaronModule {
    private final BooleanSetting vanillaHud = new BooleanSetting("Ванильный HUD", false);
    private final NumberSetting scale = new NumberSetting("Масштаб", 1.0f, 1.0f, 2.0f, 0.1f);

    public Potions() {
        this.collectSettings();
    }

    public BooleanSetting n() {
        return this.vanillaHud;
    }

    public NumberSetting o() {
        return this.scale;
    }
}

