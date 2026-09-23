package ru.prism.module.impl.utils;

import ru.prism.manager.event_impl.TextFactoryEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Streamer Mode",
        desc = "Прячет ник, сервер и координаты из F3 — стримь спокойно, зрители лишнего не увидят.",
        category = Category.UTILITIES
)
public class StreamerMode extends Module {

    public static StreamerMode get() {
        return Instance.get(StreamerMode.class);
    }

    public final BooleanSetting hidePlayer = new BooleanSetting(this, "Скрыть игрока", true);
    public final BooleanSetting hideServer = new BooleanSetting(this, "Скрыть сервер", true);
    public final BooleanSetting hideDebug = new BooleanSetting(this, "Скрыть дебаг", true);

    @EventHandler
    public void onText(TextFactoryEvent event) {
        if (event == null || event.getText() == null) return;

        if (hidePlayer.getValue() && mc.getSession() != null) {
            String username = mc.getSession().getUsername();
            if (username != null && !username.isEmpty()) {
                event.replaceText(username, "prism");
            }
        }

        if (hideServer.getValue()) {
            event.replaceRegex("(СолоЛайт|ДуоЛайт|ТриоЛайт|КланЛайт)\\s*#(\\d{1,2})", "prism");
            event.replaceRegex("Анархия-(\\d+)", "prism");
        }
    }
}
