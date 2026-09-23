package ru.prism.module.impl.utils;

import ru.prism.Client;
import ru.prism.manager.event_impl.EventUpdate;
import ru.prism.manager.event_impl.WorldLoadEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Sprint",
        desc = "Ебашит спринт сам, чтобы твои пальцы не уставали.",
        category = Category.UTILITIES
)
public class Sprint extends Module {

    public static Sprint get() {
        return Instance.get(Sprint.class);
    }

    public int tick;

    @EventHandler
    public void onEvent(WorldLoadEvent e) {
        tick += 4;
    }



    @EventHandler
    public void onEvent(EventUpdate eventUpdate) {
        if (mc.player == null || mc.world == null) {
            return;
        }

        if(!mc.player.isAlive()) {
            tick += 2;
        }

        if (tick != 0) {
            mc.player.setSprinting(false);
            mc.options.sprintKey.setPressed(false);
            tick--;
            return;
        }

        boolean horizontal = mc.player.horizontalCollision && !mc.player.collidedSoftly;
        boolean sneaking = mc.player.isSneaking() && !mc.player.isSwimming();
        boolean canSprint = !horizontal && mc.player.forwardSpeed > 0;
        if (sneaking)
            return;


        mc.options.sprintKey.setPressed(true);


    }
    @Override
    public void onDisable() {
        super.onDisable();

        if (mc.player != null) {
            mc.player.setSprinting(false);
        }
    }

}
