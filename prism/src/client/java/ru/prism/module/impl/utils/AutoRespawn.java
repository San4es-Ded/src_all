package ru.prism.module.impl.utils;

import net.minecraft.client.gui.Element;
import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.client.gui.widget.ButtonWidget;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.StringSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Auto Respawn",
        desc = "Сам жмёт «Возродиться» после смерти, а если надо — добивает это командой на респавне.",
        category = Category.UTILITIES
)
public class AutoRespawn extends Module {

    public static AutoRespawn get() {
        return Instance.get(AutoRespawn.class);
    }

    public final BooleanSetting autoRespawn = new BooleanSetting(this, "Авто-возрождение", true);
    public final BooleanSetting sendCommand = new BooleanSetting(this, "Отправлять команду", false);
    public final StringSetting command = new StringSetting(this, "Команда", "/home")
            .setVisible(sendCommand::getValue);

    private boolean awaitingCommand;
    private int respawnTicks;

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null) return;

        if (mc.currentScreen instanceof DeathScreen deathScreen) {
            awaitingCommand = true;
            if (autoRespawn.getValue() && canRespawn(deathScreen)) {
                mc.player.requestRespawn();
                mc.setScreen(null);
                respawnTicks = 0;
            }
            return;
        }

        if (awaitingCommand) {
            respawnTicks++;
            if (mc.player.isAlive() && respawnTicks > 20) {
                awaitingCommand = false;
                sendConfiguredCommand();
            }
        }
    }

    private void sendConfiguredCommand() {
        if (!sendCommand.getValue() || mc.player.networkHandler == null) return;

        String text = command.getValue() == null ? "" : command.getValue().trim();
        if (text.isEmpty()) return;

        if (text.startsWith("/")) {
            mc.player.networkHandler.sendChatCommand(text.substring(1));
        } else {
            mc.player.networkHandler.sendChatMessage(text);
        }
    }

    private boolean canRespawn(DeathScreen screen) {
        for (Element element : screen.children()) {
            if (element instanceof ButtonWidget button && button.active) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDisable() {
        awaitingCommand = false;
        respawnTicks = 0;
        super.onDisable();
    }
}
