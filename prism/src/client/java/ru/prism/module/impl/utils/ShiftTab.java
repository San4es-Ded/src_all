package ru.prism.module.impl.utils;

import ru.prism.manager.event_impl.AttackEvent;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import net.minecraft.client.util.InputUtil;

@ModuleInfo(
        name = "Shift Tab",
        desc = "Хитрый помощник по таймингу шифта: на миг приседает прямо во время атаки, чтобы криты вылетали стабильнее.",
        category = Category.UTILITIES
)
public class ShiftTab extends Module {

    private static final int SNEAK_TICKS = 4;

    private final BooleanSetting beforeAttack = new BooleanSetting(this, "Перед ударом", true);

    private int sneakTicksLeft;
    private boolean restorePending;
    private boolean previousSneakPressed;
    private boolean startPending;

    @EventHandler
    public void onAttack(AttackEvent event) {
        if (mc.player == null) {
            return;
        }

        if (beforeAttack.getValue()) {
            startSneakBurst();
        } else {
            startPending = true;
        }
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null) {
            return;
        }

        if (startPending) {
            startPending = false;
            startSneakBurst();
        }

        if (sneakTicksLeft > 0) {
            mc.options.sneakKey.setPressed(true);
            mc.player.setSneaking(true);
            sneakTicksLeft--;
        } else if (restorePending) {
            mc.options.sneakKey.setPressed(previousSneakPressed);
            mc.player.setSneaking(previousSneakPressed);
            restorePending = false;
        }
    }

    @Override
    protected void onDisable() {
        startPending = false;
        sneakTicksLeft = 0;
        restorePending = false;

        if (mc.player != null && mc.options != null) {
            boolean pressed = isSneakKeyPhysicallyPressed();
            mc.options.sneakKey.setPressed(pressed);
            mc.player.setSneaking(pressed);
        }
    }

    private void startSneakBurst() {
        if (mc.player == null) {
            return;
        }

        previousSneakPressed = isSneakKeyPhysicallyPressed();
        sneakTicksLeft = SNEAK_TICKS;
        restorePending = true;
        mc.options.sneakKey.setPressed(true);
        mc.player.setSneaking(true);
    }

    private boolean isSneakKeyPhysicallyPressed() {
        return mc.getWindow() != null
                && InputUtil.isKeyPressed(mc.getWindow(), mc.options.sneakKey.getDefaultKey().getCode());
    }
}
