package haron.modules.utilities;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;

@ModuleInfo(a="Item Scroller", b="Quickly moves items while Shift and left mouse are held.", c=ModuleCategory.UTILITIES)
public class ItemScroller
extends HaronModule {
    public final NumberSetting delay = new NumberSetting("Задержка", 50.0f, 10.0f, 500.0f, 10.0f);
}

