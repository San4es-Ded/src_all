package haron.modules.utilities;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Streamer Mode", b="Hides player and server information.", c=ModuleCategory.UTILITIES)
public class StreamerMode
extends HaronModule {
    public final BooleanSetting hidePlayerName = new BooleanSetting("Скрыть имя игрока", true);
    public final BooleanSetting hideServerNumber = new BooleanSetting("Скрыть номер сервера", true);
    public final BooleanSetting hideDebugInfo = new BooleanSetting("Скрыть отладочную инфу", true);
}

