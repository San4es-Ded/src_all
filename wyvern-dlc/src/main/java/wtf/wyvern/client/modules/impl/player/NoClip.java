package wtf.wyvern.client.modules.impl.player;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;

@ModuleAnnotation(
        name = "NoClip",
        category = Category.PLAYER,
        description = "Позволяет проходить через блоки на поддерживаемых серверах"
)
public final class NoClip extends Module {
    public static final NoClip INSTANCE = new NoClip();

    private NoClip() {
    }

    @Override
    public void onDisable() {
        if (mc.player != null) {
            mc.player.noClip = false;
        }
        super.onDisable();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null) return;
        mc.player.noClip = true;
    }
}
