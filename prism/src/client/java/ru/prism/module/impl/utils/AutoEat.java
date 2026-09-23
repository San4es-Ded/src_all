package ru.prism.module.impl.utils;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.module.api.settings.impl.StringSetting;

@ModuleInfo(
        name = "Auto Eat",
        desc = "Сам жуёт еду, когда голод просел до выбранного уровня, или дёргает команду вроде /feed — без тебя.",
        category = Category.UTILITIES
)
public class AutoEat extends Module {

    private final ModeSetting mode = new ModeSetting(this, "Режим", "Из руки", "Команда");
    private final StringSetting command = new StringSetting(this, "Команда", "/feed")
            .setVisible(() -> mode.is("Команда"));
    private final SliderSetting hungerLevel = new SliderSetting(this, "Голод", 15.0F, 1.0F, 20.0F, 1.0F);

    private boolean active;
    private boolean wasUsePressed;
    private int previousSlot = -1;
    private int commandCooldown;

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null) return;

        if (commandCooldown > 0) commandCooldown--;

        if (!active) {
            wasUsePressed = mc.options.useKey.isPressed();
        }

        if (mc.player.getHungerManager().getFoodLevel() > hungerLevel.getValue()) {
            release();
            return;
        }

        if (mode.is("Команда")) {
            release();
            if (commandCooldown <= 0) {
                sendConfiguredCommand();
                commandCooldown = 20;
            }
            return;
        }

        int slot = findFoodSlot();

        if (slot == -1) {
            release();
            return;
        }

        if (!active) {
            previousSlot = mc.player.getInventory().getSelectedSlot();
            active = true;
        }

        mc.player.getInventory().setSelectedSlot(slot);
        mc.options.useKey.setPressed(true);
    }

    private int findFoodSlot() {
        for (int i = 0; i < 9; i++) {
            if (isFood(mc.player.getInventory().getStack(i))) {
                return i;
            }
        }
        return -1;
    }

    private boolean isFood(ItemStack stack) {
        return stack != null && !stack.isEmpty() && stack.getComponents().contains(DataComponentTypes.FOOD);
    }

    private void sendConfiguredCommand() {
        if (mc.getNetworkHandler() == null) return;

        String text = command.getValue() == null ? "" : command.getValue().trim();
        if (text.isEmpty()) return;

        if (text.startsWith("/")) {
            mc.getNetworkHandler().sendChatCommand(text.substring(1));
        } else {
            mc.getNetworkHandler().sendChatMessage(text);
        }
    }

    private void release() {
        if (!active) return;

        if (previousSlot != -1) {
            mc.player.getInventory().setSelectedSlot(previousSlot);
            previousSlot = -1;
        }

        mc.options.useKey.setPressed(wasUsePressed);
        active = false;
    }

    @Override
    protected void onDisable() {
        release();
        commandCooldown = 0;
    }
}
