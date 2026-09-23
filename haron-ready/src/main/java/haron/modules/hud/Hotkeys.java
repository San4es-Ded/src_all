package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;

@ModuleInfo(a="Hotkeys", b="Displays active key bindings.", c=ModuleCategory.HUD)
public class Hotkeys
extends HaronModule {
    private final NumberSetting scale = new NumberSetting("Масштаб", 1.0f, 1.0f, 2.0f, 0.1f);

    public Hotkeys() {
        this.collectSettings();
    }

    public NumberSetting n() {
        int n = 742;
        return this.scale;
    }
}

