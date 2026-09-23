package wtf.wyvern.client.modules.impl.player;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "AutoEat", category = Category.PLAYER,
        description = "Автоматически выбирает и ест подходящую еду")
@FastNative
public final class AutoEat extends Module {
    public static final AutoEat INSTANCE = new AutoEat();

    private final SliderSetting hunger = new SliderSetting("Голод", 14.0F, 1.0F, 19.0F, 1.0F);
    private final SliderSetting health = new SliderSetting("Здоровье для яблока", 12.0F, 2.0F, 20.0F, 1.0F);
    private final BooleanSetting goldenApples = new BooleanSetting("Золотые яблоки", true);

    private boolean eating;
    private boolean useStarted;
    private int useTicks;
    private int originalSlot = -1;
    private int swappedInventorySlot = -1;
    private int swappedHotbarSlot = -1;

    private AutoEat() {
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) {
            stopEating();
            return;
        }

        if (eating) {
            tickEating();
            return;
        }

        if (mc.currentScreen != null || mc.player.isUsingItem()) return;

        boolean lowHealth = mc.player.getHealth() + mc.player.getAbsorptionAmount() <= health.getCurrent();
        boolean lowHunger = mc.player.getHungerManager().getFoodLevel() <= Math.round(hunger.getCurrent());
        if (!lowHealth && !lowHunger) return;

        int slot = findFoodSlot(lowHealth, lowHunger);
        if (slot != -1 && equip(slot)) {
            eating = true;
            useStarted = false;
            useTicks = 0;
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
            mc.options.useKey.setPressed(true);
        }
    }

    private void tickEating() {
        useTicks++;
        mc.options.useKey.setPressed(true);

        if (mc.player.isUsingItem()) {
            useStarted = true;
        } else if (useStarted || useTicks > 80 || mc.player.getMainHandStack().isEmpty()) {
            stopEating();
        } else if (useTicks % 4 == 0) {
            mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
        }
    }

    private int findFoodSlot(boolean lowHealth, boolean lowHunger) {
        int best = -1;
        double bestScore = -1.0D;
        for (int slot = 0; slot < 36; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (stack.isEmpty()) continue;

            if (goldenApples.isEnabled() && lowHealth
                    && (stack.isOf(Items.ENCHANTED_GOLDEN_APPLE) || stack.isOf(Items.GOLDEN_APPLE))) {
                double score = stack.isOf(Items.ENCHANTED_GOLDEN_APPLE) ? 10_000.0D : 9_000.0D;
                if (score > bestScore) {
                    best = slot;
                    bestScore = score;
                }
                continue;
            }

            if (!lowHunger) continue;
            FoodComponent food = stack.get(DataComponentTypes.FOOD);
            if (food == null || stack.isOf(Items.GOLDEN_APPLE) || stack.isOf(Items.ENCHANTED_GOLDEN_APPLE)) continue;
            double score = food.nutrition() * 2.0D + food.saturation();
            if (score > bestScore) {
                best = slot;
                bestScore = score;
            }
        }
        return best;
    }

    private boolean equip(int inventorySlot) {
        originalSlot = mc.player.getInventory().selectedSlot;
        if (inventorySlot < 9) {
            mc.player.getInventory().selectedSlot = inventorySlot;
            syncSlot(inventorySlot);
            return true;
        }

        swappedInventorySlot = inventorySlot;
        swappedHotbarSlot = originalSlot;
        mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, inventorySlot,
                swappedHotbarSlot, SlotActionType.SWAP, mc.player);
        syncSlot(swappedHotbarSlot);
        return !mc.player.getMainHandStack().isEmpty();
    }

    private void stopEating() {
        if (mc.options != null) mc.options.useKey.setPressed(false);
        if (mc.player != null && mc.interactionManager != null) {
            if (swappedInventorySlot != -1 && swappedHotbarSlot != -1) {
                mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId, swappedInventorySlot,
                        swappedHotbarSlot, SlotActionType.SWAP, mc.player);
            }
            if (originalSlot != -1) {
                mc.player.getInventory().selectedSlot = originalSlot;
                syncSlot(originalSlot);
            }
        }
        eating = false;
        useStarted = false;
        useTicks = 0;
        originalSlot = -1;
        swappedInventorySlot = -1;
        swappedHotbarSlot = -1;
    }

    private void syncSlot(int slot) {
        if (mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(slot));
        }
    }

    @Override
    public void onDisable() {
        stopEating();
        super.onDisable();
    }
}
