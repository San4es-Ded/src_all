package su.sacura.features.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;
import su.sacura.features.modules.settings.impl.BooleanSetting;
import su.sacura.mixin.accessors.MinecraftClientAccessor;
import su.sacura.util.type.ISetting;

@ModuleAnnotations(name = "No Delay", category = Category.PLAYER)
public class NoDelayModule extends Module {
    private final BooleanSetting xp = (new BooleanSetting("Пузырёк опыта", Boolean.valueOf(true))).setDescription("Без задержки кидает опыт");
    private final BooleanSetting place = (new BooleanSetting("Правая кнопка мыши", Boolean.valueOf(false))).setDescription("Без задержки ставит блоки");

    public NoDelayModule() {
        addSettings(new ISetting[] { (ISetting)this.xp, (ISetting)this.place });
    }

    @Subscribe
    public void event(EventUpdate e) {
        if (check(mc.player.getMainHandStack().getItem()))
            ((MinecraftClientAccessor)mc).setUseCooldown(0);
    }

    private boolean check(Item item) {
        // Исправлено: field_8287 -> EXPERIENCE_BOTTLE
        return ((item instanceof net.minecraft.item.BlockItem && ((Boolean)this.place.get()).booleanValue()) || (item == Items.EXPERIENCE_BOTTLE && ((Boolean)this.xp.get()).booleanValue()));
    }
}