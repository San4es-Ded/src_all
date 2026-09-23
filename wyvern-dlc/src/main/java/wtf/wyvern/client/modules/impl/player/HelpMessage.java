package wtf.wyvern.client.modules.impl.player;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.input.EventKey;

@ModuleAnnotation(
        name = "HelpMessage",
        category = Category.PLAYER,
        description = "Отправляет координаты в глобальный чат"
)
public final class HelpMessage extends Module {
    public static final HelpMessage INSTANCE = new HelpMessage();

    private final BindSetting bind = new BindSetting("Бинд", -1);

    private HelpMessage() {
    }

    @EventTarget
    private void onKey(EventKey event) {
        if (mc.player == null || mc.getNetworkHandler() == null || mc.currentScreen != null) return;
        if (!event.isKeyDown(bind.getKeyCode()) || bind.getKeyCode() == -1) return;

        mc.getNetworkHandler().sendChatMessage(
                "! " + mc.player.getBlockX() + " "
                        + mc.player.getBlockY() + " "
                        + mc.player.getBlockZ()
        );
    }
}
