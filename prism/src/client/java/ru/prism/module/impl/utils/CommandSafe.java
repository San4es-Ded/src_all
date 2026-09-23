package ru.prism.module.impl.utils;

import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.utils.math.ChatUtils;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Command Safe",
        desc = "Переспрашивает перед опасными командами вроде /suicide, /ci и /clear, чтобы случайно не убить себя или не выкинуть весь инвентарь.",
        category = Category.UTILITIES
)
public class CommandSafe extends Module {

    public static CommandSafe get() {
        return Instance.get(CommandSafe.class);
    }

    private String pendingCommand = "";
    private long pendingTime = 0L;

    public boolean handle(String message) {
        if (!isEnabled() || mc.player == null || message == null) return false;

        String msg = message.trim();
        String lower = msg.toLowerCase();
        boolean isSuicide = lower.startsWith("/suicide");
        boolean isCi = lower.equals("/ci") || lower.startsWith("/ci ");
        boolean isClear = lower.equals("/clear") || lower.startsWith("/clear ");
        if (!isSuicide && !isCi && !isClear) return false;

        long now = System.currentTimeMillis();
        if (msg.equalsIgnoreCase(this.pendingCommand) && now - this.pendingTime < 5000L) {
            this.pendingCommand = "";
            this.pendingTime = 0L;
            return false;
        }

        this.pendingCommand = msg;
        this.pendingTime = now;
        String commandName = isSuicide ? "/suicide" : (isCi ? "/ci" : "/clear");
        String action = isSuicide ? " убьёт вашего персонажа!" : " очистит ваш инвентарь!";
        ChatUtils.addChatMessage("§fВы уверены? Команда §c" + commandName + "§f" + action);
        ChatUtils.addChatMessage("§fДля подтверждения отправьте команду ещё раз.");
        return true;
    }

    @Override
    protected void onDisable() {
        pendingCommand = "";
        pendingTime = 0L;
    }
}
