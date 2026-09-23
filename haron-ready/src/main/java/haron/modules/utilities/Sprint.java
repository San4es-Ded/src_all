package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="Sprint", b="Automatically holds the sprint key.", c=ModuleCategory.UTILITIES)
public class Sprint
extends HaronModule {
    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
        if (Sprint.c.player != null) {
            Sprint.c.options.sprintKey.setPressed(Sprint.c.player.age > 3);
        }
    }
}

