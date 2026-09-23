package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;

@ModuleInfo(a="Media Player", b="Показывает текущую песню на экране", c=ModuleCategory.HUD)
public class MediaPlayer
extends HaronModule {
    public final NumberSetting scale = new NumberSetting("Масштаб", 1.0f, 0.5f, 2.0f, 0.1f);

    public MediaPlayer() {
        this.collectSettings();
    }
}

