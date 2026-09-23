package haron.modules.visuals;

import haron.core.BooleanCoercion;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;

@ModuleInfo(a="Hitbox Customizer", b="Настраивает отображение хитбоксов сущностей", c=ModuleCategory.VISUALS)
public class HitboxCustomizer
extends HaronModule {
    public final ModeSetting a = new ModeSetting("Режим обводки", new String[]{"Обычный", "Углы"}, "Обычный");
    public final NumberSetting b = new NumberSetting("Длина углов", 0.5f, 0.1f, 1.0f, 0.05f).a(() -> {
        return this.a.b("Углы");
    });
    private final BooleanSetting g = new BooleanSetting("Линии взгляда", false);
    private final BooleanSetting h = new BooleanSetting("Заполнять", true);
    private final SettingGroup i = new SettingGroup("Цвет");
    private final BooleanSetting j = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting k = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> {
        int n = this.j.a() ? 0 : 1;
        return BooleanCoercion.from(n);
    });

    public boolean n() {
        return BooleanCoercion.from(this.k() && this.g.a() ? 1 : 0);
    }

    public boolean o() {
        return BooleanCoercion.from(this.k() && this.h.a() ? 1 : 0);
    }

    public Color p() {
        int n = 716;
        return !this.j.a() ? this.k.a() : ModuleManager.CLIENT_COLOR.n();
    }

    public boolean q() {
        return this.a.b("Углы");
    }

    public float r() {
        int n = 600;
        return this.b.a() / 4.0f;
    }
}

