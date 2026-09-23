package ru.prism.module.impl.utils;

import java.util.HashMap;
import java.util.Map;

import ru.prism.manager.event_impl.SoundPlayEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.DelimiterSetting;
import ru.prism.module.api.settings.impl.SliderSetting;

@ModuleInfo(
        name = "Sound Controller",
        desc = "Крутит громкость отдельных звуков — приглуши трезубец, криты, сплеши и прочую надоедливую мелочь.",
        category = Category.UTILITIES
)
public class SoundController extends Module {

    private final DelimiterSetting fireworkGroup = new DelimiterSetting(this, "Фейерверк");
    private final BooleanSetting firework = new BooleanSetting(this, "Включить", false);
    private final SliderSetting fireworkVolume = new SliderSetting(this, "Громкость", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> firework.getValue());

    private final DelimiterSetting bottleGroup = new DelimiterSetting(this, "Пузырёк опыта");
    private final BooleanSetting bottleThrow = new BooleanSetting(this, "Бросок", false);
    private final SliderSetting bottleThrowVolume = new SliderSetting(this, "Громкость броска", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> bottleThrow.getValue());
    private final BooleanSetting bottleBreak = new BooleanSetting(this, "Разбивание", false);
    private final SliderSetting bottleBreakVolume = new SliderSetting(this, "Громкость разбивания", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> bottleBreak.getValue());
    private final BooleanSetting orbPickup = new BooleanSetting(this, "Подбор опыта", false);
    private final SliderSetting orbPickupVolume = new SliderSetting(this, "Громкость подбора", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> orbPickup.getValue());

    private final DelimiterSetting weaponGroup = new DelimiterSetting(this, "Оружие");
    private final BooleanSetting trident = new BooleanSetting(this, "Трезубец", false);
    private final SliderSetting tridentVolume = new SliderSetting(this, "Громкость трезубца", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> trident.getValue());
    private final BooleanSetting rod = new BooleanSetting(this, "Удочка", false);
    private final SliderSetting rodVolume = new SliderSetting(this, "Громкость удочки", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> rod.getValue());

    private final DelimiterSetting attackGroup = new DelimiterSetting(this, "Атака");
    private final BooleanSetting hit = new BooleanSetting(this, "Звук удара", false);
    private final SliderSetting hitVolume = new SliderSetting(this, "Громкость удара", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> hit.getValue());
    private final BooleanSetting crit = new BooleanSetting(this, "Звук крита", false);
    private final SliderSetting critVolume = new SliderSetting(this, "Громкость крита", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> crit.getValue());

    private final DelimiterSetting miscGroup = new DelimiterSetting(this, "Прочее");
    private final BooleanSetting splash = new BooleanSetting(this, "Сплеш", false);
    private final SliderSetting splashVolume = new SliderSetting(this, "Громкость сплеша", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> splash.getValue());
    private final BooleanSetting funTimeCrit = new BooleanSetting(this, "Крит FunTime", false);
    private final SliderSetting funTimeCritVolume = new SliderSetting(this, "Громкость FunTime", 50.0F, 0.0F, 100.0F, 1.0F)
            .setVisible(() -> funTimeCrit.getValue());

    private final Map<String, SoundVolumeBinding> bindings = new HashMap<>();

    public SoundController() {
        this.bindings.put("minecraft:entity.firework_rocket.launch", new SoundVolumeBinding(this.firework, this.fireworkVolume));
        this.bindings.put("minecraft:entity.experience_bottle.throw", new SoundVolumeBinding(this.bottleThrow, this.bottleThrowVolume));
        this.bindings.put("minecraft:entity.splash_potion.break", new SoundVolumeBinding(this.bottleBreak, this.bottleBreakVolume));
        this.bindings.put("minecraft:entity.experience_orb.pickup", new SoundVolumeBinding(this.orbPickup, this.orbPickupVolume));
        this.bindings.put("minecraft:item.trident.return", new SoundVolumeBinding(this.trident, this.tridentVolume));
        this.bindings.put("minecraft:item.trident.hit_ground", new SoundVolumeBinding(this.trident, this.tridentVolume));
        this.bindings.put("minecraft:block.beacon.deactivate", new SoundVolumeBinding(this.trident, this.tridentVolume));
        this.bindings.put("minecraft:entity.fishing_bobber.retrieve", new SoundVolumeBinding(this.rod, this.rodVolume));
        this.bindings.put("minecraft:entity.player.attack.sweep", new SoundVolumeBinding(this.hit, this.hitVolume));
        this.bindings.put("minecraft:entity.player.attack.strong", new SoundVolumeBinding(this.hit, this.hitVolume));
        this.bindings.put("minecraft:entity.player.attack.weak", new SoundVolumeBinding(this.hit, this.hitVolume));
        this.bindings.put("minecraft:entity.generic.splash", new SoundVolumeBinding(this.splash, this.splashVolume));
        this.bindings.put("minecraft:entity.player.splash", new SoundVolumeBinding(this.splash, this.splashVolume));
        this.bindings.put("minecraft:entity.player.attack.crit", new SoundVolumeBinding(this.crit, this.critVolume));
        this.bindings.put("minecraft:entity.player.levelup", new SoundVolumeBinding(this.funTimeCrit, this.funTimeCritVolume));
        this.bindings.put("minecraft:entity.experience_bottle.break", new SoundVolumeBinding(this.bottleBreak, this.bottleBreakVolume));
    }

    @EventHandler
    public void onSoundPlay(SoundPlayEvent event) {
        if (event.getSound() == null || event.getSound().getId() == null) return;

        String id = event.getSound().getId().toString();
        SoundVolumeBinding binding = this.bindings.get(id);

        if (binding == null && id.contains(":")) {
            binding = this.bindings.get("minecraft:" + id.substring(id.indexOf(':') + 1));
        }

        if (binding != null && binding.toggle().getValue()) {
            event.setVolume(binding.volume().getValue() / 100.0F);
        }
    }

    private record SoundVolumeBinding(BooleanSetting toggle, SliderSetting volume) {
    }
}
