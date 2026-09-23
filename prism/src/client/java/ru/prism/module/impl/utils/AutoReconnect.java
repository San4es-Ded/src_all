package ru.prism.module.impl.utils;

import net.minecraft.client.gui.screen.DisconnectedScreen;
import net.minecraft.client.gui.screen.multiplayer.ConnectScreen;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import ru.prism.manager.event_impl.EventDisconnect;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.math.ChatUtils;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Auto Reconnect",
        desc = "Сам возвращает тебя на сервер после кика или обрыва связи, чтобы не тыкать заново.",
        category = Category.UTILITIES
)
public class AutoReconnect extends Module {

    public static AutoReconnect get() {
        return Instance.get(AutoReconnect.class);
    }

    public final BooleanSetting autoReconnect = new BooleanSetting(this, "Включить", true);
    public final SliderSetting delay = new SliderSetting(this, "Задержка", 2.0F, 0.5F, 10.0F, 0.5F);

    private ServerInfo lastServer;
    private boolean reconnecting;

    @EventHandler
    public void onTick(EventTick event) {
        ServerInfo current = mc.getCurrentServerEntry();
        if (current != null) {
            lastServer = current;
        }
    }

    @EventHandler
    public void onDisconnect(EventDisconnect event) {
        if (!autoReconnect.getValue() || reconnecting) return;
        if (lastServer == null || lastServer.address == null || lastServer.address.isEmpty()) return;

        ServerInfo server = lastServer;
        String address = server.address;
        long waitMillis = Math.max(1L, (long) (delay.getValue() * 1000.0F));
        reconnecting = true;

        Thread thread = new Thread(() -> {
            try {
                Thread.sleep(waitMillis);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                reconnecting = false;
                return;
            }
            mc.execute(() -> connect(server, address));
        }, "prism-auto-reconnect");
        thread.setDaemon(true);
        thread.start();
    }

    private void connect(ServerInfo server, String address) {
        reconnecting = false;
        if (!isEnabled()) return;
        if (!(mc.currentScreen instanceof DisconnectedScreen)) return;
        if (!ServerAddress.isValid(address)) return;

        ChatUtils.addChatMessage("§7Переподключаюсь к §f" + address);
        ConnectScreen.connect(mc.currentScreen, mc, ServerAddress.parse(address), server, false, null);
    }

    @Override
    public void onDisable() {
        reconnecting = false;
        super.onDisable();
    }
}
