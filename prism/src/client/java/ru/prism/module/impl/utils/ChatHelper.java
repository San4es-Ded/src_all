package ru.prism.module.impl.utils;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.network.packet.s2c.play.ChatMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.util.Formatting;

import ru.prism.manager.event_impl.EventPacket;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Chat Helper",
        desc = "Помощник для чата: анти-спам, сохранение истории и фикс раскладки команд.",
        category = Category.UTILITIES
)
public class ChatHelper extends Module {

    public static ChatHelper get() {
        return Instance.get(ChatHelper.class);
    }

    public final BooleanSetting antiSpam = new BooleanSetting(this, "Анти-спам", true);
    public final BooleanSetting saveHistory = new BooleanSetting(this, "Сохранять историю", true);
    public final BooleanSetting fixLayout = new BooleanSetting(this, "Фикс раскладки", true);

    private final List<String> chatHistory = new ArrayList<>();

    private static final String RU = "йцукенгшщзхъфывапролджэячсмитьбю.ЙЦУКЕНГШЩЗХЪФЫВАПРОЛДЖЭЯЧСМИТЬБЮ,ёЁ";
    private static final String EN = "qwertyuiop[]asdfghjkl;'zxcvbnm,./QWERTYUIOP{}ASDFGHJKL:\"ZXCVBNM<>?`~";

    @EventHandler
    public void onPacket(EventPacket event) {
        if (!isEnabled() || event.isSend() || !antiSpam.getValue()) return;

        String text = null;
        if (event.getPacket() instanceof GameMessageS2CPacket packet) {
            text = packet.content().getString();
        } else if (event.getPacket() instanceof ChatMessageS2CPacket packet) {
            text = packet.body().content();
        }

        if (text != null && isSpam(text)) {
            event.cancel();
        }
    }

    public String fixLayout(String message) {
        if (!isEnabled() || !fixLayout.getValue() || message == null || message.isEmpty()) return message;

        char first = message.charAt(0);
        if (first != '.' && first != '/') return message;

        int space = message.indexOf(' ');
        String commandToken = space >= 0 ? message.substring(0, space) : message;
        if (!hasRussian(commandToken)) return message;

        return convertLayout(commandToken) + (space >= 0 ? message.substring(space) : "");
    }

    private boolean hasRussian(String str) {
        for (char ch : str.toCharArray()) {
            if (ch >= 1072 && ch <= 1103 || ch >= 1040 && ch <= 1071 || ch == 1105 || ch == 1025) {
                return true;
            }
        }

        return false;
    }

    private String convertLayout(String str) {
        StringBuilder sb = new StringBuilder(str.length());

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (i == 0 && ch == '.') {
                sb.append('/');
            } else {
                int idx = RU.indexOf(ch);
                sb.append(idx >= 0 ? EN.charAt(idx) : ch);
            }
        }

        return sb.toString();
    }

    private boolean isSpam(String text) {
        if (text == null || text.trim().isEmpty()) return false;

        String clean = Formatting.strip(text).trim().toLowerCase();
        if (this.chatHistory.contains(clean)) {
            return true;
        }

        String[] words = clean.split("\\s+");

        for (int i = 0; i < words.length - 1; i++) {
            if (words[i].length() > 2 && words[i].equals(words[i + 1])) {
                return true;
            }
        }

        this.chatHistory.add(clean);
        if (this.chatHistory.size() > 100) {
            this.chatHistory.remove(0);
        }

        return false;
    }
}
