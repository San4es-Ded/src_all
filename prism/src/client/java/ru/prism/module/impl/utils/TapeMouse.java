package ru.prism.module.impl.utils;

import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Tape Mouse",
        desc = "Ебашит мышкой за тебя, пока ты дрочишь на киллы.",
        category = Category.UTILITIES
)
public class TapeMouse extends Module {

    public static TapeMouse get() {
        return Instance.get(TapeMouse.class);
    }

    public SliderSetting delay = new SliderSetting(this, "Задержка", 10, 1, 100, 1);
    public ModeSetting button = new ModeSetting(this, "Кнопка", "Левая", "Правая");

    private int ticks;

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null || mc.interactionManager == null) return;
        if (mc.currentScreen != null || mc.player.isUsingItem()) return;

        if (ticks > 0) {
            ticks--;
            return;
        }

        if (button.is("Левая")) {
            clickLeft();
        } else {
            clickRight();
        }

        ticks = delay.getValue().intValue();
    }

    private void clickLeft() {
        if (mc.crosshairTarget instanceof EntityHitResult entityHit) {
            mc.interactionManager.attackEntity(mc.player, entityHit.getEntity());
        } else if (mc.crosshairTarget instanceof BlockHitResult blockHit) {
            mc.interactionManager.updateBlockBreakingProgress(blockHit.getBlockPos(), blockHit.getSide());
        }
        mc.player.swingHand(Hand.MAIN_HAND);
    }

    private void clickRight() {
        if (mc.crosshairTarget instanceof BlockHitResult blockHit
                && mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND, blockHit).isAccepted()) {
            return;
        }
        mc.interactionManager.interactItem(mc.player, Hand.MAIN_HAND);
    }

    @Override
    public void onEnable() {
        ticks = 0;
    }

    @Override
    public void onDisable() {
        ticks = 0;
    }
}
