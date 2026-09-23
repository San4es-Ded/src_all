package haron.modules.hud;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Lyrics Text", b="Показывает текст текущей песни на экране", c=ModuleCategory.HUD)
public class LyricsText
extends HaronModule {
    public final NumberSetting fontSize = new NumberSetting("Размер шрифта", 2.0f, 1.0f, 4.0f, 0.1f);
    public final BooleanSetting shadow = new BooleanSetting("Тень", true);
    public final BooleanSetting background = new BooleanSetting("Фон", true);

    public LyricsText() {
        this.collectSettings();
    }
}

