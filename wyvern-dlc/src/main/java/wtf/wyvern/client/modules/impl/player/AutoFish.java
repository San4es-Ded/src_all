package wtf.wyvern.client.modules.impl.player;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.FishingRodItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.server.EventPacket;

@ModuleAnnotation(
        name = "AutoFish",
        category = Category.PLAYER,
        description = "Автоматизирует процесс рыбалки"
)
public final class AutoFish extends Module {
    public static final AutoFish INSTANCE = new AutoFish();

    private final BooleanSetting takeRod =
            new BooleanSetting("Автоматически брать удочку", true);

    private boolean caught;
    private boolean needsCast;
    private int rodHotbarSlot = -1;
    private long lastActionTime;
    private long catchTime;

    private AutoFish() {
    }

    @Override
    public void onDisable() {
        caught = false;
        needsCast = false;
        rodHotbarSlot = -1;
        lastActionTime = 0L;
        catchTime = 0L;
        super.onDisable();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) return;

        if (takeRod.isEnabled() && rodHotbarSlot == -1) {
            findBestFishingRodInHotbar();
        }

        if (rodHotbarSlot != -1 && mc.player.getInventory().selectedSlot != rodHotbarSlot) {
            selectRod();
        }

        long now = System.currentTimeMillis();
        if (caught && now - catchTime >= 600L) {
            useFishingRod();
            caught = false;
            needsCast = true;
            lastActionTime = now;
        }

        if (needsCast && now - lastActionTime >= 300L) {
            useFishingRod();
            needsCast = false;
            lastActionTime = now;
        }
    }

    @EventTarget
    private void onPacket(EventPacket event) {
        if (!event.isReceive() || mc.player == null || mc.world == null) return;
        if (event.getPacket() instanceof PlaySoundS2CPacket packet
                && packet.getSound().value() == SoundEvents.ENTITY_FISHING_BOBBER_SPLASH) {
            caught = true;
            catchTime = System.currentTimeMillis();
        }
    }

    private void selectRod() {
        if (mc.player == null || rodHotbarSlot < 0 || rodHotbarSlot >= 9) return;
        mc.player.getInventory().selectedSlot = rodHotbarSlot;
        mc.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(rodHotbarSlot));
    }

    private void useFishingRod() {
        if (mc.player == null || mc.interactionManager == null
                || rodHotbarSlot < 0 || rodHotbarSlot >= 9) return;

        ItemStack stack = mc.player.getInventory().getStack(rodHotbarSlot);
        if (!(stack.getItem() instanceof FishingRodItem)) return;
        if (mc.player.getInventory().selectedSlot != rodHotbarSlot) {
            selectRod();
        }
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
    }

    private void findBestFishingRodInHotbar() {
        if (mc.player == null) return;

        int bestSlot = -1;
        int maxEnchantments = -1;
        for (int slot = 0; slot < 9; slot++) {
            ItemStack stack = mc.player.getInventory().getStack(slot);
            if (!(stack.getItem() instanceof FishingRodItem)) continue;

            int enchantmentCount = EnchantmentHelper.getEnchantments(stack).getSize();
            if (enchantmentCount > maxEnchantments) {
                maxEnchantments = enchantmentCount;
                bestSlot = slot;
            }
        }
        rodHotbarSlot = bestSlot;
    }
}
