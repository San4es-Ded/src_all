package haron.modules.utilities;

import haron.events.SoundPlayEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.utilities.SoundRule;
import haron.settings.NumberSetting;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import java.util.HashMap;
import java.util.Map;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="Sound Controller", b="Контроль громкости определённых звуков", c=ModuleCategory.UTILITIES)
public class SoundController
extends HaronModule {
    private final SettingGroup a = new SettingGroup("Фейерверк");
    private final BooleanSetting b = new BooleanSetting("Включить", false);
    private final NumberSetting e = new NumberSetting("Громкость", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.b.a();
    });
    private final SettingGroup f = new SettingGroup("Пузырёк опыта");
    private final BooleanSetting g = new BooleanSetting("Бросок", false);
    private final NumberSetting h = new NumberSetting("Громкость броска", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.g.a();
    });
    private final BooleanSetting i = new BooleanSetting("Разбивание", false);
    private final NumberSetting j = new NumberSetting("Громкость разбивания", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.i.a();
    });
    private final BooleanSetting k = new BooleanSetting("Подбор опыта", false);
    private final NumberSetting l = new NumberSetting("Громкость подбора", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.k.a();
    });
    private final SettingGroup m = new SettingGroup("Оружие");
    private final BooleanSetting n = new BooleanSetting("Трезубец", false);
    private final NumberSetting o = new NumberSetting("Громкость трезубца", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.n.a();
    });
    private final BooleanSetting p = new BooleanSetting("Удочка", false);
    private final NumberSetting q = new NumberSetting("Громкость удочки", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.p.a();
    });
    private final SettingGroup r = new SettingGroup("Атака");
    private final BooleanSetting s = new BooleanSetting("Звук удара", false);
    private final NumberSetting t = new NumberSetting("Громкость удара", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.s.a();
    });
    private final BooleanSetting u = new BooleanSetting("Звук крита", false);
    private final NumberSetting v = new NumberSetting("Громкость крита", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.u.a();
    });
    private final SettingGroup w = new SettingGroup("Прочее");
    private final BooleanSetting x = new BooleanSetting("Сплеш", false);
    private final NumberSetting y = new NumberSetting("Громкость сплеша", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.x.a();
    });
    private final BooleanSetting z = new BooleanSetting("Крит FunTime", false);
    private final NumberSetting A = new NumberSetting("Громкость FunTime", 50.0f, 0.0f, 100.0f, 1.0f).a(() -> {
        return this.z.a();
    });
    private final Map<String, SoundRule> B = new HashMap<String, SoundRule>();

    public SoundController() {
        this.B.put("minecraft:entity.firework_rocket.launch", new SoundRule(this.b, this.e));
        this.B.put("minecraft:entity.experience_bottle.throw", new SoundRule(this.g, this.h));
        this.B.put("minecraft:entity.splash_potion.break", new SoundRule(this.i, this.j));
        this.B.put("minecraft:entity.experience_orb.pickup", new SoundRule(this.k, this.l));
        this.B.put("minecraft:item.trident.return", new SoundRule(this.n, this.o));
        this.B.put("minecraft:item.trident.hit_ground", new SoundRule(this.n, this.o));
        this.B.put("minecraft:block.beacon.deactivate", new SoundRule(this.n, this.o));
        this.B.put("minecraft:entity.fishing_bobber.retrieve", new SoundRule(this.p, this.q));
        this.B.put("minecraft:entity.player.attack.sweep", new SoundRule(this.s, this.t));
        this.B.put("minecraft:entity.player.attack.strong", new SoundRule(this.s, this.t));
        this.B.put("minecraft:entity.player.attack.weak", new SoundRule(this.s, this.t));
        this.B.put("minecraft:entity.generic.splash", new SoundRule(this.x, this.y));
        this.B.put("minecraft:entity.player.splash", new SoundRule(this.x, this.y));
        this.B.put("minecraft:entity.player.attack.crit", new SoundRule(this.u, this.v));
        this.B.put("minecraft:entity.player.levelup", new SoundRule(this.z, this.A));
    }

    @EventHandler
    public void a(SoundPlayEvent lst7uj2) {
    }
}

