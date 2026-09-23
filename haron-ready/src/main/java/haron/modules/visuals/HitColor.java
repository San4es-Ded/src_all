package haron.modules.visuals;

import haron.core.BooleanCoercion;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.ColorSetting;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;

@ModuleInfo(a="Hit Color", b="Изменяет цвет сущностей при получении урона", c=ModuleCategory.VISUALS)
public class HitColor
extends HaronModule {
    private final ModeSetting e = new ModeSetting("Режим", new String[]{"Полностью", "Скин"}, "Полностью");
    private final BooleanSetting f = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting g = new ColorSetting("Кастомный цвет", Color.RED).a(() -> BooleanCoercion.from((Boolean)this.f.k() != false ? 0 : 1));
    public static int a;
    public static boolean b;

    public Color n() {
        return (Boolean)this.f.k() == false ? (Color)this.g.k() : ModuleManager.CLIENT_COLOR.n();
    }

    public boolean o() {
        return this.k();
    }

    public boolean p() {
        int n = 522;
        return this.e.c("Полностью");
    }
}

