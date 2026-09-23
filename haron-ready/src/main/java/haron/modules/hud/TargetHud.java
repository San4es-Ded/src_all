package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Target HUD", b="Shows target information.", c=ModuleCategory.HUD)
public class TargetHud
extends HaronModule {
    private final NumberSetting scale = new NumberSetting("Масштаб", 1.0f, 1.0f, 2.0f, 0.1f);
    private final BooleanSetting showArmor = new BooleanSetting("Броня", true);
    private final BooleanSetting showDurability = new BooleanSetting("Прочность", true);

    public boolean isShowDurability() {
        return this.showDurability.get();
    }

    public boolean isShowArmor() {
        return this.showArmor.get();
    }

    public TargetHud() {
        this.collectSettings();
    }

    public NumberSetting n() {
        return this.scale;
    }
}

