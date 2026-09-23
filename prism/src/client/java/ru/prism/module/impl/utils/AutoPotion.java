package ru.prism.module.impl.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.registry.entry.RegistryEntry;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;

@ModuleInfo(
        name = "Auto Potion",
        desc = "Сам подкидывает в руку и выпивает зелья, когда эффект слетел или вот-вот кончится — невидимость и скорость держатся без тебя.",
        category = Category.UTILITIES
)
public class AutoPotion extends Module {

    private final BooleanSetting invisibility = new BooleanSetting(this, "Невидимость", true);
    private final BooleanSetting speed = new BooleanSetting(this, "Скорость", false);

    private boolean active;
    private boolean wasUsePressed;
    private int previousSlot = -1;

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.world == null || mc.player == null) return;

        if (!active) {
            wasUsePressed = mc.options.useKey.isPressed();
        }

        int invisibilitySlot = -1;
        int speedSlot = -1;

        if (invisibility.getValue() && needsEffect(StatusEffects.INVISIBILITY)) {
            invisibilitySlot = findPotion(StatusEffects.INVISIBILITY);
        }

        if (speed.getValue() && needsEffect(StatusEffects.SPEED)) {
            speedSlot = findPotion(StatusEffects.SPEED);
        }

        int slot = invisibilitySlot != -1 ? invisibilitySlot : speedSlot;

        if (slot != -1) {
            if (!active) {
                previousSlot = mc.player.getInventory().getSelectedSlot();
            }

            mc.player.getInventory().setSelectedSlot(slot);
            mc.options.useKey.setPressed(true);
            active = true;
        } else if (active) {
            if (previousSlot != -1) {
                mc.player.getInventory().setSelectedSlot(previousSlot);
                previousSlot = -1;
            }

            mc.options.useKey.setPressed(wasUsePressed);
            active = false;
        }
    }

    @Override
    protected void onDisable() {
        if (active) {
            mc.options.useKey.setPressed(wasUsePressed);
        }

        if (previousSlot != -1 && mc.player != null) {
            mc.player.getInventory().setSelectedSlot(previousSlot);
        }

        active = false;
        previousSlot = -1;
    }

    private boolean needsEffect(RegistryEntry<StatusEffect> effect) {
        StatusEffectInstance instance = mc.player.getStatusEffect(effect);
        return instance == null || instance.getDuration() <= 100;
    }

    private int findPotion(RegistryEntry<StatusEffect> effect) {
        for (int i = 0; i < 9; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (!stack.isEmpty() && stack.isOf(Items.POTION) && hasEffect(stack, effect)) {
                return i;
            }
        }
        return -1;
    }

    private boolean hasEffect(ItemStack stack, RegistryEntry<StatusEffect> effect) {
        PotionContentsComponent contents = stack.get(DataComponentTypes.POTION_CONTENTS);
        if (contents == null) return false;

        for (StatusEffectInstance instance : contents.customEffects()) {
            if (instance.getEffectType().equals(effect)) {
                return true;
            }
        }

        return contents.potion()
                .map(entry -> entry.value().getEffects().stream()
                        .anyMatch(instance -> instance.getEffectType().equals(effect)))
                .orElse(false);
    }
}
