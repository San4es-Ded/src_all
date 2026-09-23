package haron.modules.visuals;

import haron.core.BooleanCoercion;
import haron.events.RenderTickEvent;
import haron.hud.core.HudServices;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.concurrent.ThreadLocalRandom;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(a="World Particles", b="Падающие частицы в мире", c=ModuleCategory.VISUALS)
public class WorldParticles
extends HaronModule {
    private final ModeSetting e = new ModeSetting("Тип частиц", new String[]{"Сердце", "Искра", "Снежинка", "Сияние", "Линия", "Доллар"}, "Снежинка");
    private final ModeSetting f = new ModeSetting("Режим физики", new String[]{"Реалистичная", "Без коллизий", "Без физики"}, "Без физики");
    private final SettingGroup g = new SettingGroup("Спавн");
    private final NumberSetting h = new NumberSetting("Частота спавна", 5.0f, 1.0f, 10.0f, 1.0f);
    private final NumberSetting i = new NumberSetting("Количество за спавн", 5.0f, 1.0f, 15.0f, 1.0f);
    private final NumberSetting j = new NumberSetting("Радиус спавна", 30.0f, 5.0f, 50.0f, 1.0f);
    private final NumberSetting k = new NumberSetting("Высота спавна", 10.0f, 5.0f, 30.0f, 1.0f);
    private final SettingGroup l = new SettingGroup("Частицы");
    private final NumberSetting m = new NumberSetting("Размер", 0.25f, 0.1f, 1.0f, 0.05f);
    private final NumberSetting n = new NumberSetting("Время жизни", 100.0f, 30.0f, 300.0f, 10.0f);
    private final NumberSetting o = new NumberSetting("Сила притяжения", 0.02f, 0.0f, 0.1f, 0.01f);
    private final BooleanSetting p = new BooleanSetting("Горизонтальное движение", true);
    private final NumberSetting q = new NumberSetting("Скорость движения", 0.05f, 0.0f, 0.2f, 0.01f).a(() -> {
        return this.p.a();
    });
    private final SettingGroup r = new SettingGroup("Цвет");
    private final BooleanSetting s = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting t = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> {
        int n = 848;
        return BooleanCoercion.from(this.s.a() ? 0 : 1);
    });
    private long u = 0L;
    public static int a;
    public static boolean b;

    private void n() {
        Vec3d vec3d = WorldParticles.c.player.getPos();
        int n = this.i.b();
        for (int i = 0; i < n; ++i) {
            HudServices.PARTICLES.spawn(new Vec3d(vec3d.x + ThreadLocalRandom.current().nextDouble(-this.j.a(), this.j.a()), vec3d.y + (!this.f.b("Без физики") ? (double)this.k.a() : ThreadLocalRandom.current().nextDouble(0.0, this.k.a())), vec3d.z + ThreadLocalRandom.current().nextDouble(-this.j.a(), this.j.a())), !this.p.a() ? Vec3d.ZERO : new Vec3d(ThreadLocalRandom.current().nextDouble(-this.q.a(), this.q.a()), 0.0, ThreadLocalRandom.current().nextDouble(-this.q.a(), this.q.a())), this.n.b(), this.m.a() + (ThreadLocalRandom.current().nextFloat() * 0.1f - 0.05f), this.p(), ColorUtils.a(this.o().getRGB()), this.o.a(), this.f.d());
        }
    }

    @EventHandler
    public void a(RenderTickEvent zv7i6c2) {
        if (WorldParticles.c.player == null || WorldParticles.c.world == null) {
            return;
        }
        long l = System.currentTimeMillis();
        if ((double)(l - this.u) >= 1000.0 / (double)this.h.a()) {
            this.n();
            this.u = l;
        }
    }

    private Color o() {
        return !this.s.a() ? this.t.a() : ModuleManager.CLIENT_COLOR.n();
    }

    private Identifier p() {
        switch (this.e.d()) {
            case "Сердце": {
                return Identifier.of((String)"haron", (String)"textures/particle/heart.png");
            }
            case "Искра": {
                return Identifier.of((String)"haron", (String)"textures/particle/sparkle.png");
            }
            case "Снежинка": {
                return Identifier.of((String)"haron", (String)"textures/particle/snowflake.png");
            }
            case "Сияние": {
                return Identifier.of((String)"haron", (String)"textures/particle/glow.png");
            }
            case "Линия": {
                return Identifier.of((String)"haron", (String)"textures/particle/line.png");
            }
            case "Доллар": {
                return Identifier.of((String)"haron", (String)"textures/particle/dollar.png");
            }
        }
        return Identifier.of((String)"haron", (String)"textures/particle/snowflake.png");
    }
}

