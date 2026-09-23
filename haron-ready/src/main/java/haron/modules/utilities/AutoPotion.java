package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.BooleanSetting;
import java.util.Iterator;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;

@ModuleInfo(a="Auto Potion", b="Автоматически использует выбранные зелья", c=ModuleCategory.UTILITIES)
public class AutoPotion
extends HaronModule {
    private boolean g;
    public static int a;
    public static boolean b;
    private final BooleanSetting e = new BooleanSetting("Невидимость", true);
    private final BooleanSetting f = new BooleanSetting("Скорость", false);
    private int h = -1;
    private boolean i = false;

    @Override
    public void f() {
        super.f();
        if (this.g) {
            AutoPotion.c.options.useKey.setPressed(this.i);
        }
        if (this.h != -1) {
            AutoPotion.c.player.getInventory().selectedSlot = this.h;
        }
        this.g = false;
        this.h = -1;
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        ItemStack itemStack;
        int n;
        if (AutoPotion.c.world == null || AutoPotion.c.player == null) {
            return;
        }
        if (!this.g) {
            this.i = AutoPotion.c.options.useKey.isPressed();
        }
        int n2 = -1;
        int n3 = -1;
        if (((Boolean)this.e.k()).booleanValue() && (!AutoPotion.c.player.hasStatusEffect(StatusEffects.INVISIBILITY) || AutoPotion.c.player.getStatusEffect(StatusEffects.INVISIBILITY) != null && AutoPotion.c.player.getStatusEffect(StatusEffects.INVISIBILITY).getDuration() <= 100)) {
            for (n = 0; n < 9; ++n) {
                itemStack = AutoPotion.c.player.getInventory().getStack(n);
                if (itemStack.isEmpty() || itemStack.getItem() != Items.POTION || !this.a(itemStack, (RegistryEntry<StatusEffect>)StatusEffects.INVISIBILITY)) continue;
                n2 = n;
                break;
            }
        }
        if (((Boolean)this.f.k()).booleanValue() && (!AutoPotion.c.player.hasStatusEffect(StatusEffects.SPEED) || AutoPotion.c.player.getStatusEffect(StatusEffects.SPEED) != null && AutoPotion.c.player.getStatusEffect(StatusEffects.SPEED).getDuration() <= 100)) {
            for (n = 0; n < 9; ++n) {
                itemStack = AutoPotion.c.player.getInventory().getStack(n);
                if (itemStack.isEmpty() || itemStack.getItem() != Items.POTION) continue;
                if (this.a(itemStack, (RegistryEntry<StatusEffect>)StatusEffects.SPEED)) {
                    n3 = n;
                    break;
                }
                if (!b) continue;
            }
        }
        n = -1;
        if (n2 != -1) {
            n = n2;
        } else if (n3 != -1) {
            n = n3;
        }
        if (n != -1) {
            if (!this.g) {
                this.h = AutoPotion.c.player.getInventory().selectedSlot;
            }
            AutoPotion.c.player.getInventory().selectedSlot = n;
            AutoPotion.c.options.useKey.setPressed(true);
            this.g = true;
            return;
        }
        if (this.g) {
            if (this.h != -1) {
                AutoPotion.c.player.getInventory().selectedSlot = this.h;
                this.h = -1;
            }
            AutoPotion.c.options.useKey.setPressed(this.i);
            this.g = false;
        }
    }

    private boolean a(ItemStack itemStack, RegistryEntry<StatusEffect> registryEntry) {
        PotionContentsComponent potionContentsComponent = (PotionContentsComponent)itemStack.get(DataComponentTypes.POTION_CONTENTS);
        if (potionContentsComponent == null) {
            return false;
        }
        Iterator iterator = potionContentsComponent.customEffects().iterator();
        while (iterator.hasNext()) {
            if (!((StatusEffectInstance)iterator.next()).getEffectType().equals(registryEntry)) continue;
            return true;
        }
        return potionContentsComponent.potion().map(registryEntry2 -> {
            return ((Potion)registryEntry2.value()).getEffects().stream().anyMatch(statusEffectInstance -> {
                return statusEffectInstance.getEffectType().equals((Object)registryEntry);
            });
        }).orElse(false);
    }
}

