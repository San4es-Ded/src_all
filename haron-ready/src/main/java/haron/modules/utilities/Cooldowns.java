package haron.modules.utilities;

import haron.core.BooleanCoercion;
import haron.events.ItemUseEvent;
import haron.events.ClientTickEvent;
import haron.events.PlayerInteractEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.player.lrsc12;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;
import java.util.HashMap;
import java.util.Map;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;

@ModuleInfo(a="Cooldowns", b="Добавляет кастомный кулдаун на зелья исцеления", c=ModuleCategory.UTILITIES)
public class Cooldowns
extends HaronModule {
    private final BooleanSetting e = new BooleanSetting("Преобразовывать задержки", true);
    private final BooleanSetting f = new BooleanSetting("Только в Пвп (Исцеление)", true);
    private final NumberSetting g = new NumberSetting("Размер шрифта таймеров", 1.0f, 0.5f, 2.0f, 0.1f);
    private final Map<Item, Long> h = new HashMap<Item, Long>();
    public static int a;
    public static boolean b;

    public boolean b(Item item) {
        if (this.h.containsKey(item)) {
            if (this.a(item) > 0.0f) {
                return true;
            }
        } else if (b) {
            throw new ExceptionInInitializerError();
        }
        return false;
    }

    private float c(Item item) {
        long l = System.currentTimeMillis() - this.h.getOrDefault(item, 0L);
        if ((double)l >= 18500.0) {
            this.h.remove(item);
            return 0.0f;
        }
        return 1.0f - (float)((double)l / 18500.0);
    }

    public boolean n() {
        return (Boolean)this.e.k();
    }

    @Override
    public void f() {
        this.h.clear();
        super.f();
    }

    @EventHandler
    public void a(PlayerInteractEvent ynkh5k2) {
        ItemStack itemStack;
        if (Cooldowns.c.player != null && Cooldowns.a(itemStack = ynkh5k2.d().getStackInHand(ynkh5k2.f())) && (double)(System.currentTimeMillis() - this.h.getOrDefault(itemStack.getItem(), 0L)) < 18500.0) {
            ynkh5k2.b();
        }
    }

    @EventHandler
    public void a(ItemUseEvent o6d1cm2) {
        if (((Boolean)this.f.k()).booleanValue() && !lrsc12.a().a()) {
            return;
        }
        ItemStack itemStack = o6d1cm2.d();
        if (Cooldowns.a(itemStack)) {
            this.h.put(itemStack.getItem(), System.currentTimeMillis());
        }
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (((Boolean)this.f.k()).booleanValue()) {
            if (lrsc12.a().a()) {
                return;
            }
            this.h.clear();
        }
    }

    public float a(Item item) {
        int n = 44;
        return this.c(item);
    }

    public static boolean a(ItemStack itemStack) {
        PotionContentsComponent potionContentsComponent;
        if (!(itemStack.getItem() instanceof PotionItem)) {
            return false;
        }
        if (itemStack.contains(DataComponentTypes.POTION_CONTENTS) && (potionContentsComponent = (PotionContentsComponent)itemStack.get(DataComponentTypes.POTION_CONTENTS)) != null) {
            return potionContentsComponent.potion().isPresent() && ((Potion)((RegistryEntry)potionContentsComponent.potion().get()).value()).getEffects().stream().anyMatch(statusEffectInstance -> {
                int n = 771;
                return BooleanCoercion.from(statusEffectInstance.getEffectType() != StatusEffects.INSTANT_HEALTH ? 0 : 1);
            }) ? true : potionContentsComponent.customEffects().stream().anyMatch(statusEffectInstance -> {
                return BooleanCoercion.from(statusEffectInstance.getEffectType() != StatusEffects.INSTANT_HEALTH ? 0 : 1);
            });
        }
        return false;
    }

    public BooleanSetting o() {
        return this.e;
    }

    public BooleanSetting p() {
        return this.f;
    }

    public NumberSetting q() {
        return this.g;
    }

    public Map<Item, Long> r() {
        return this.h;
    }
}

