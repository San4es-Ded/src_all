package haron.modules.visuals;

import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import java.awt.Color;

@ModuleInfo(a="World Customizer", b="Настройка мира", c=ModuleCategory.VISUALS)
public class WorldCustomizer
extends HaronModule {
    private final SettingGroup e = new SettingGroup("Туман");
    private final BooleanSetting f = new BooleanSetting("Настроить туман", true);
    private final BooleanSetting g = new BooleanSetting("Кастомная дальность", false).a(() -> {
        return this.f.a();
    });
    private final NumberSetting h = new NumberSetting("Дальность тумана", 0.3f, 0.1f, 1.0f, 0.01f).a(() -> this.f.a() && this.g.a());
    private final SettingGroup i = new SettingGroup("Небо");
    private final BooleanSetting j = new BooleanSetting("Настроить небо", true);
    private final SettingGroup k = new SettingGroup("Цвет");
    private final BooleanSetting l = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting m = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> {
        return !this.l.a();
    });
    public static int a;
    public static boolean b;

    public int s() {
        return this.u().getRGB();
    }

    public boolean n() {
        return this.k() && this.f.a();
    }

    @Override
    public void f() {
        super.f();
        if (WorldCustomizer.c.worldRenderer != null) {
            WorldCustomizer.c.worldRenderer.reload();
        }
    }

    public boolean o() {
        return this.k() && this.f.a() && this.g.a();
    }

    public float p() {
        float f = this.q();
        return Math.min(f * 0.75f, Math.max(0.0f, f - 0.01f));
    }

    public int t() {
        return this.u().getRGB();
    }

    public float q() {
        int n = 433;
        return Math.max(1.0f, this.h.a() * 250.0f);
    }

    private Color u() {
        return this.l.a() ? ModuleManager.CLIENT_COLOR.n() : this.m.a();
    }

    public boolean r() {
        return this.k() && this.j.a();
    }
}

