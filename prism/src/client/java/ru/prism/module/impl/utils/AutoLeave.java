package ru.prism.module.impl.utils;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

import ru.prism.Client;
import ru.prism.manager.event_impl.EventPacket;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.event_impl.WorldLoadEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.mixin.PlayerListHudAccessor;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.ModeSetting;
import ru.prism.utils.math.ChatUtils;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Auto Leave",
        desc = "Как только радар засечёт чужого игрока рядом — сразу уводит тебя с сервера, пока не застали врасплох.",
        category = Category.UTILITIES
)
public class AutoLeave extends Module {

    public static AutoLeave get() {
        return Instance.get(AutoLeave.class);
    }

    public final ModeSetting mode = new ModeSetting(this, "Режим", "Выход с сервера", "Хаб");

    private static final String RADAR_HEADER = "[✇] Радар ›";
    private static final String SERVER_TAG = "Анархия-";
    private static final String SCAN_COMMAND = "near max";
    private static final String HUB_COMMAND = "hub";

    private static final Pattern NEAR_PLAYER = Pattern.compile("\\((.*?)\\)\\s+.*?\\s+(\\S+)\\s+[—–-]\\s+(\\d+\\.\\d+)\\s+блоков");
    private static final Pattern COOLDOWN_MESSAGE = Pattern.compile(".*Команда будет доступна через\\s+(\\d+)\\s+мин\\.?\\s*(\\d+)?\\s*сек\\.?");
    private static final Pattern NOBODY_NEAR = Pattern.compile("Вокруг вас никого нет");
    private static final Pattern REPEAT_REQUEST = Pattern.compile("Повторите текст еще раз\\.");

    private final Map<Integer, Long> cooldowns = new HashMap<>();
    private boolean radarActive;
    private boolean waiting;
    private boolean left;
    private int serverId = -1;
    private long waitStart;
    private long lastPurge;

    @Override
    public void onEnable() {
        reset();
        if (isActive()) {
            waiting = true;
            waitStart = System.currentTimeMillis();
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        reset();
        super.onDisable();
    }

    private void reset() {
        cooldowns.clear();
        radarActive = false;
        waiting = false;
        left = false;
        serverId = -1;
        waitStart = 0L;
        lastPurge = 0L;
    }

    @EventHandler
    public void onWorldLoad(WorldLoadEvent event) {
        if (!isActive()) return;

        radarActive = false;
        waiting = true;
        left = false;
        waitStart = System.currentTimeMillis();
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (!isActive()) return;

        long now = System.currentTimeMillis();
        int currentServer = serverId();
        if (currentServer != -1 && currentServer != serverId) {
            serverId = currentServer;
            radarActive = false;
            left = false;
        }

        if (now - lastPurge > 1000L) {
            lastPurge = now;
            purge(now);
        }

        if (waiting) {
            if (serverId != -1) {
                waiting = false;
                if (cooldowns.getOrDefault(serverId, 0L) <= now) {
                    triggerNear();
                    left = true;
                }
            } else if (now - waitStart > 5000L) {
                waiting = false;
            }
        }

        if (waiting || left || serverId == -1) return;

        if (cooldowns.getOrDefault(serverId, 0L) <= now) {
            triggerNear();
            left = true;
        }
    }

    private void purge(long now) {
        Iterator<Map.Entry<Integer, Long>> iterator = cooldowns.entrySet().iterator();

        while (iterator.hasNext()) {
            Map.Entry<Integer, Long> entry = iterator.next();
            if (now >= entry.getValue()) {
                iterator.remove();
                if (entry.getKey() == serverId && !left && !radarActive) {
                    triggerNear();
                    left = true;
                }
            }
        }
    }

    @EventHandler
    public void onPacket(EventPacket event) {
        if (!isActive() || event.isSend()) return;
        if (!(event.getPacket() instanceof GameMessageS2CPacket packet)) return;

        String text = Formatting.strip(packet.content().getString());
        if (text.contains(RADAR_HEADER)) {
            radarActive = true;
        }

        Matcher cooldown = COOLDOWN_MESSAGE.matcher(text);
        if (cooldown.find()) {
            applyCooldown(cooldown);
            event.cancel();
            return;
        }

        if (!radarActive) return;

        if (NOBODY_NEAR.matcher(text).find()) {
            radarActive = false;
            left = false;
        } else if (NEAR_PLAYER.matcher(text).find()) {
            handleDetection(text);
        } else if (REPEAT_REQUEST.matcher(text).find()) {
            event.cancel();
            triggerNear();
        }
    }

    private void triggerNear() {
        if (mc.player == null) return;

        mc.player.networkHandler.sendChatCommand(SCAN_COMMAND);
        radarActive = true;
    }

    private void applyCooldown(Matcher matcher) {
        radarActive = false;
        left = false;

        long seconds = Integer.parseInt(matcher.group(1)) * 60L
                + (matcher.group(2) != null ? Integer.parseInt(matcher.group(2)) : 0);
        cooldowns.put(serverId, System.currentTimeMillis() + seconds * 1000L + 2000L);
    }

    private void handleDetection(String text) {
        Matcher matcher = NEAR_PLAYER.matcher(text);
        if (!matcher.find()) return;

        String name = matcher.group(2);
        if (Client.get().friendManager().isFriend(name)) {
            radarActive = false;
            left = false;
            return;
        }

        radarActive = false;
        left = false;
        if (mc.player == null) return;

        String reason = "Обнаружен игрок " + name + " на расстоянии " + matcher.group(3) + " блоков";
        if (mode.is("Выход с сервера")) {
            if (mc.getNetworkHandler() != null) {
                mc.getNetworkHandler().getConnection().disconnect(Text.literal(reason));
            }
        } else if (mode.is("Хаб")) {
            mc.player.networkHandler.sendChatCommand(HUB_COMMAND);
            ChatUtils.addChatMessage(reason);
        }
    }

    private int serverId() {
        try {
            if (mc.inGameHud == null || mc.inGameHud.getPlayerListHud() == null) return -1;

            Text header = ((PlayerListHudAccessor) mc.inGameHud.getPlayerListHud()).getHeader();
            if (header == null) return -1;

            String text = Formatting.strip(header.getString());
            if (text == null || !text.contains(SERVER_TAG)) return -1;

            String[] parts = text.split(SERVER_TAG);
            if (parts.length < 2) return -1;

            try {
                return Integer.parseInt(parts[1].trim());
            } catch (NumberFormatException exception) {
                return -1;
            }
        } catch (Exception exception) {
            return -1;
        }
    }

    private boolean isActive() {
        return mc.world != null
                && mc.world.getRegistryKey().getValue().toString().equals("minecraft:overworld")
                && !mc.isInSingleplayer();
    }
}
