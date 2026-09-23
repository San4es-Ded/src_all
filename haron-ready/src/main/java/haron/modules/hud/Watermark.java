package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Watermark", b="Отображает водяной знак клиента", c=ModuleCategory.HUD)
public class Watermark
extends HaronModule {
    public final BooleanSetting showFps = new BooleanSetting("FPS", true);
    public final BooleanSetting showPing = new BooleanSetting("Пинг", true);
    public final NumberSetting scale = new NumberSetting("Размер", 1.0f, 0.5f, 2.0f, 0.05f);

    public Watermark() {
        this.collectSettings();
    }
}

