package su.sacura.features.modules.impl.player;

import com.google.common.eventbus.Subscribe;
import su.sacura.events.tick.EventUpdate;
import su.sacura.features.modules.api.core.Module;
import su.sacura.features.modules.api.core.ModuleAnnotations;
import su.sacura.features.modules.impl.Category;

@ModuleAnnotations(name="Auto Respawn", category=Category.PLAYER, desc="Автоматическое возрождение.")
public class AutoRespawnModule extends Module {
    @Subscribe
    public void onUpdate(EventUpdate e) {
        if (AutoRespawnModule.mc.player != null && AutoRespawnModule.mc.world != null) {
            assert (AutoRespawnModule.mc.player != null);
            if (AutoRespawnModule.mc.player.isDead()) {
                try {
                    // Исправлено: respawnPlayer() -> requestRespawn()
                    AutoRespawnModule.mc.player.requestRespawn();
                }
                catch (Exception exception) {
                    // empty catch block
                }
            }
        }
    }
}