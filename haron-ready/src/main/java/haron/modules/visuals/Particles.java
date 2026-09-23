package haron.modules.visuals;

import haron.core.BooleanCoercion;
import haron.events.TotemPopEvent;
import haron.events.AttackTargetEvent;
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
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;

@ModuleInfo(a="Particles", b="Создаёт частицы при выполнении условий", c=ModuleCategory.VISUALS)
public class Particles
extends HaronModule {
    private long H;
    private Vec3d I;
    private LivingEntity K;
    public static int a;
    public static boolean b;
    private final ModeSetting e = new ModeSetting("Условия", new String[]{"При ударе", "При сносе тотема"}, new int[]{0});
    private final BooleanSetting f = new BooleanSetting("Индивидуальные настройки", false);
    private final SettingGroup g = new SettingGroup("Общие").a(() -> {
        return BooleanCoercion.from(this.f.a() ? 0 : 1);
    });
    private final ModeSetting h = new ModeSetting("Тип частиц", new String[]{"Сердце", "Искра", "Снежинка", "Сияние", "Линия", "Доллар"}, "Сердце").a(() -> {
        return BooleanCoercion.from(this.f.a() ? 0 : 1);
    });
    private final ModeSetting i = new ModeSetting("Режим физики", new String[]{"Реалистичная", "Без коллизий", "Без физики", "Притяжение"}, "Реалистичная").a(() -> {
        return BooleanCoercion.from(this.f.a() ? 0 : 1);
    });
    private final NumberSetting j = new NumberSetting("Количество", 4.0f, 1.0f, 20.0f, 1.0f).a(() -> {
        return BooleanCoercion.from(this.f.a() ? 0 : 1);
    });
    private final NumberSetting k = new NumberSetting("Размер", 0.3f, 0.1f, 0.75f, 0.05f).a(() -> {
        return BooleanCoercion.from(this.f.a() ? 0 : 1);
    });
    private final NumberSetting l = new NumberSetting("Время жизни", 20.0f, 10.0f, 50.0f, 5.0f).a(() -> {
        return BooleanCoercion.from(this.f.a() ? 0 : 1);
    });
    private final NumberSetting m = new NumberSetting("Сила разлёта", 0.3f, 0.15f, 0.35f, 0.01f).a(() -> {
        int n = this.f.a() ? 0 : 1;
        return BooleanCoercion.from(n);
    });
    private final BooleanSetting n = new BooleanSetting("Цвет клиента", BooleanCoercion.from(45027861)).a(() -> {
        return BooleanCoercion.from(this.f.a() ? 0 : 1);
    });
    private final ColorSetting o = new ColorSetting("Конечный цвет", Color.WHITE).a(() -> BooleanCoercion.from(this.n.a() || this.f.a() ? 0 : 1));
    private final SettingGroup p = new SettingGroup("Удар").a(() -> this.f.a() && this.e.b("При ударе") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ModeSetting q = new ModeSetting("Тип частиц", new String[]{"Сердце", "Искра", "Снежинка", "Сияние", "Линия", "Доллар"}, "Сердце").a(() -> this.f.a() && this.e.b("При ударе") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ModeSetting r = new ModeSetting("Режим физики", new String[]{"Реалистичная", "Без коллизий", "Без физики", "Притяжение"}, "Реалистичная").a(() -> this.f.a() && this.e.b("При ударе") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting s = new NumberSetting("Количество", 4.0f, 1.0f, 20.0f, 1.0f).a(() -> this.f.a() && this.e.b("При ударе") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting t = new NumberSetting("Размер", 0.3f, 0.1f, 0.75f, 0.05f).a(() -> this.f.a() && this.e.b("При ударе") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting u = new NumberSetting("Время жизни", 20.0f, 10.0f, 50.0f, 5.0f).a(() -> this.f.a() && this.e.b("При ударе") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting v = new NumberSetting("Сила разлёта", 0.3f, 0.15f, 0.35f, 0.01f).a(() -> this.f.a() && this.e.b("При ударе") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final BooleanSetting w = new BooleanSetting("Цвет клиента", true).a(() -> {
        if (this.f.a() && this.e.b("При ударе")) {
            return true;
        }
        return false;
    });
    private final ColorSetting x = new ColorSetting("Конечный цвет", Color.WHITE).a(() -> this.f.a() && this.e.b("При ударе") && !this.w.a() ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final SettingGroup y = new SettingGroup("Снос тотема").a(() -> this.f.a() && this.e.b("При сносе тотема") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ModeSetting z = new ModeSetting("Тип частиц", new String[]{"Сердце", "Искра", "Снежинка", "Сияние", "Линия", "Доллар"}, "Сердце").a(() -> this.f.a() && this.e.b("При сносе тотема") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ModeSetting A = new ModeSetting("Режим физики", new String[]{"Реалистичная", "Без коллизий", "Без физики", "Притяжение"}, "Реалистичная").a(() -> this.f.a() && this.e.b("При сносе тотема") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting B = new NumberSetting("Количество", 4.0f, 1.0f, 20.0f, 1.0f).a(() -> {
        if (this.f.a() && this.e.b("При сносе тотема")) {
            return true;
        }
        return false;
    });
    private final NumberSetting C = new NumberSetting("Размер", 0.3f, 0.1f, 0.75f, 0.05f).a(() -> this.f.a() && this.e.b("При сносе тотема") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting D = new NumberSetting("Время жизни", 20.0f, 10.0f, 50.0f, 5.0f).a(() -> this.f.a() && this.e.b("При сносе тотема") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final NumberSetting E = new NumberSetting("Сила разлёта", 0.3f, 0.15f, 0.35f, 0.01f).a(() -> this.f.a() && this.e.b("При сносе тотема") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final BooleanSetting F = new BooleanSetting("Цвет клиента", BooleanCoercion.from(1175694691)).a(() -> this.f.a() && this.e.b("При сносе тотема") ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private final ColorSetting G = new ColorSetting("Конечный цвет", Color.WHITE).a(() -> this.f.a() && this.e.b("При сносе тотема") && !this.F.a() ? Boolean.valueOf(true) : Boolean.valueOf(false));
    private boolean J = false;

    private String e(String string) {
        int n = -1;
        switch (string.hashCode()) {
            case -1407259064: {
                if (!string.equals("attack")) break;
                n = 0;
                break;
            }
            case 110549953: {
                if (!string.equals("totem")) break;
                n = 1;
            }
        }
        switch (n) {
            case 0: {
                return this.r.d();
            }
            case 1: {
                return this.A.d();
            }
        }
        return this.i.d();
    }

    private float b(String string) {
        int n = -1;
        switch (string.hashCode()) {
            case -1407259064: {
                if (!string.equals("attack")) break;
                n = 0;
                break;
            }
            case 110549953: {
                if (!string.equals("totem")) break;
                n = 1;
            }
        }
        switch (n) {
            case 0: {
                return this.t.a();
            }
            case 1: {
                return this.C.a();
            }
        }
        return this.k.a();
    }

    private int c(String string) {
        int n = -1;
        switch (string.hashCode()) {
            case -1407259064: {
                if (!string.equals("attack")) break;
                n = 0;
                break;
            }
            case 110549953: {
                if (!string.equals("totem")) break;
                n = 1;
            }
        }
        switch (n) {
            case 0: {
                return this.u.b();
            }
            case 1: {
                return this.D.b();
            }
        }
        return this.l.b();
    }

    @Override
    public void f() {
        super.f();
        this.J = false;
        this.K = null;
    }

    private Color f(String string) {
        if (this.f.a()) {
            int n = -1;
            switch (string.hashCode()) {
                case -1407259064: {
                    if (!string.equals("attack")) break;
                    n = 0;
                    break;
                }
                case 110549953: {
                    if (!string.equals("totem")) break;
                    n = 1;
                }
            }
            switch (n) {
                case 0: {
                    return !this.w.a() ? this.x.a() : ModuleManager.CLIENT_COLOR.n();
                }
                case 1: {
                    Color color = this.F.a() ? ModuleManager.CLIENT_COLOR.n() : this.G.a();
                    return color;
                }
            }
        }
        return !this.n.a() ? this.o.a() : ModuleManager.CLIENT_COLOR.n();
    }

    private double d(String string) {
        int n = -1;
        switch (string.hashCode()) {
            case -1407259064: {
                if (!string.equals("attack")) break;
                n = 0;
                break;
            }
            case 110549953: {
                if (!string.equals("totem")) break;
                n = 1;
            }
        }
        switch (n) {
            case 0: {
                return this.v.a();
            }
            case 1: {
                return this.E.a();
            }
        }
        return this.m.a();
    }

    @EventHandler
    public void a(AttackTargetEvent dt813s2) {
        if (this.e.b("При ударе") && dt813s2.a() != Particles.c.player && dt813s2.a() instanceof LivingEntity) {
            LivingEntity livingEntity = (LivingEntity)dt813s2.a();
            Vec3d vec3d = new Vec3d(livingEntity.getX(), livingEntity.getY() + (double)(livingEntity.getEyeHeight(livingEntity.getPose()) / 2.0f), livingEntity.getZ());
            int n = this.f.a() ? this.s.b() * 3 : this.j.b() * 3;
            int n2 = n;
            for (int i = 0; i < n2; ++i) {
                this.a(vec3d, 0.02, this.f("attack").getRGB(), "attack");
            }
        }
    }

    @EventHandler
    public void a(TotemPopEvent bjffkp2) {
        if (this.e.b("При сносе тотема")) {
            this.K = bjffkp2.a();
            this.I = new Vec3d(this.K.getX(), this.K.getY() + (double)this.K.getEyeHeight(this.K.getPose()), this.K.getZ());
            this.H = System.currentTimeMillis();
            this.J = true;
        }
    }

    private int a(String string) {
        int n = -1;
        switch (string.hashCode()) {
            case -1407259064: {
                if (!string.equals("attack")) break;
                n = 0;
                break;
            }
            case 110549953: {
                if (!string.equals("totem")) break;
                n = 1;
            }
        }
        switch (n) {
            case 0: {
                return this.s.b();
            }
            case 1: {
                return this.B.b();
            }
        }
        return this.j.b();
    }

    private void a(Vec3d vec3d, double d, int n, String string) {
        int n2 = ColorUtils.a(n);
        int n3 = !this.f.a() ? this.j.b() : this.a(string);
        int n4 = !this.f.a() ? this.l.b() : this.c(string);
        int n5 = (n4 & 0xFFFFFFF5) - (~n4 & 0xA);
        int n6 = (n4 | 0xA) + (n4 & 0xA);
        float f = !this.f.a() ? this.k.a() : this.b(string);
        float f2 = f - 0.05f;
        float f3 = f + 0.05f;
        double d2 = !this.f.a() ? (double)this.m.a() : this.d(string);
        String string2 = !this.f.a() ? this.i.d() : this.e(string);
        for (int i = 0; i < n3; ++i) {
            int n7 = ThreadLocalRandom.current().nextInt(n5, n6);
            float f4 = f2 + ThreadLocalRandom.current().nextFloat() * (f3 - f2);
            Vec3d vec3d2 = new Vec3d(ThreadLocalRandom.current().nextDouble(-d, d), ThreadLocalRandom.current().nextDouble(0.0, d), ThreadLocalRandom.current().nextDouble(-d, d));
            if (vec3d2.lengthSquared() > 0.0) {
                vec3d2 = vec3d2.normalize().multiply(ThreadLocalRandom.current().nextDouble(0.005, d2));
            }
            HudServices.PARTICLES.spawn(vec3d, vec3d2, n7, f4, this.g(string), n2, string2);
        }
    }

    @EventHandler
    public void a(RenderTickEvent zv7i6c2) {
        if (this.e.b("При сносе тотема") && this.J) {
            if (System.currentTimeMillis() - this.H > 1500L) {
                this.J = false;
                this.K = null;
                return;
            }
            if (this.K != null && this.K.isAlive()) {
                this.I = new Vec3d(this.K.getX(), this.K.getY() + (double)this.K.getHeight() / 2.0, this.K.getZ());
            }
            for (int i = 0; i < 2; ++i) {
                Vec3d vec3d;
                while ((vec3d = new Vec3d(ThreadLocalRandom.current().nextDouble(-1.0, 1.0), ThreadLocalRandom.current().nextDouble(0.1, 1.0), ThreadLocalRandom.current().nextDouble(-1.0, 1.0))).lengthSquared() > 1.0) {
                }
                HudServices.PARTICLES.spawn(this.I, vec3d.normalize().multiply(0.4), !this.f.a() ? this.l.b() : this.D.b(), !this.f.a() ? this.k.a() : this.C.a(), this.g("totem"), !ThreadLocalRandom.current().nextBoolean() ? 327424 : 0xFFFF17, !this.f.a() ? this.i.d() : this.A.d());
            }
        }
    }

    private Identifier g(String string) {
        String string2;
        if (this.f.a()) {
            int n = -1;
            switch (string.hashCode()) {
                case -1407259064: {
                    if (!string.equals("attack")) break;
                    n = 0;
                    break;
                }
                case 110549953: {
                    if (!string.equals("totem")) break;
                    n = 1;
                }
            }
            switch (n) {
                case 0: {
                    string2 = this.q.d();
                    break;
                }
                case 1: {
                    string2 = this.z.d();
                    break;
                }
                default: {
                    string2 = this.h.d();
                    break;
                }
            }
        } else {
            string2 = this.h.d();
        }
        switch (string2) {
            case "Снежинка": {
                return Identifier.of((String)"haron", (String)"textures/particle/snowflake.png");
            }
            case "Доллар": {
                return Identifier.of((String)"haron", (String)"textures/particle/dollar.png");
            }
            case "Линия": {
                return Identifier.of((String)"haron", (String)"textures/particle/line.png");
            }
            case "Искра": {
                return Identifier.of((String)"haron", (String)"textures/particle/sparkle.png");
            }
            case "Сердце": {
                return Identifier.of((String)"haron", (String)"textures/particle/heart.png");
            }
            case "Сияние": {
                return Identifier.of((String)"haron", (String)"textures/particle/glow.png");
            }
        }
        return Identifier.of((String)"haron", (String)"textures/particle/heart.png");
    }
}

