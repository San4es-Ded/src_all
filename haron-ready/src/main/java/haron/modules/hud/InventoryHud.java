package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;

@ModuleInfo(a="Inventory HUD", b="Отображение инвентаря.", c=ModuleCategory.HUD)
public class InventoryHud
extends HaronModule {
    private final NumberSetting scale = new NumberSetting("Масштаб", 1.0f, 0.8f, 2.0f, 0.1f);

    public InventoryHud() {
        this.collectSettings();
    }

    public NumberSetting n() {
        return this.scale;
    }
}

