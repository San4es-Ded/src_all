package ru.prism.module.impl.utils;

import ru.prism.manager.event_impl.EventPacket;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.utils.math.ServerUtil;
import ru.prism.utils.other.Instance;
import net.minecraft.network.packet.c2s.play.ChatCommandSignedC2SPacket;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.network.packet.c2s.play.CommandExecutionC2SPacket;

@ModuleInfo(name = "Pvp Safe", category = Category.COMBAT,
        desc = "Не даёт тебе позорно слиться с сервера прямо посреди драки.")
public class PvpSafe extends Module {

    public static PvpSafe get() {
        return Instance.get(PvpSafe.class);
    }

    /** Активна ли защита прямо сейчас (модуль включён и идёт ПВП). */
    public static boolean isActive() {
        PvpSafe module = get();
        return module != null && module.isEnabled() && ServerUtil.isPvp();
    }

    @EventHandler
    public void onPacket(EventPacket event) {
        if (!isEnabled() || !event.isSend() || !ServerUtil.isPvp()) return;

        String text = null;
        if (event.getPacket() instanceof ChatMessageC2SPacket packet) {
            text = packet.chatMessage();
        } else if (event.getPacket() instanceof ChatCommandSignedC2SPacket packet) {
            text = packet.command();
        } else if (event.getPacket() instanceof CommandExecutionC2SPacket packet) {
            text = packet.command();
        }

        if (text != null && isBlocked(text)) {
            event.cancel();
        }
    }

    /** Содержит ли сообщение/команда запрещённые во время ПВП слова. */
    public static boolean isBlocked(String raw) {
        if (raw == null || raw.isEmpty()) return false;

        String s = raw.toLowerCase().trim();
        if (s.startsWith("/")) s = s.substring(1).trim();

        // команда (первое слово): hub / limbo / anarchy / an / an<число>
        String cmd = s.split("\\s+", 2)[0];
        if (cmd.equals("hub") || cmd.equals("limbo") || cmd.equals("anarchy") || cmd.startsWith("an")) {
            return true;
        }

        // сообщение содержит слово выхода
        return s.matches(".*\\b(hub|limbo|anarchy)\\b.*");
    }
}
