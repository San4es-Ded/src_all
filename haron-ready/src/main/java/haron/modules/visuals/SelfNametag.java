package haron.modules.visuals;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.BooleanSetting;

@ModuleInfo(a="Self Nametag", b="Shows your nametag in third person.", c=ModuleCategory.VISUALS)
public class SelfNametag
extends HaronModule {
    public final BooleanSetting showHP = new BooleanSetting("Показывать HP", true);
    public final BooleanSetting showBg = new BooleanSetting("Фон за текстом", true);
}

