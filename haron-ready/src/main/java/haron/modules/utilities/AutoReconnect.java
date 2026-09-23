package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import meteordevelopment.orbit.EventHandler;

@ModuleInfo(a="Auto Reconnect", b="Keeps reconnect state available for the reconnect screen.", c=ModuleCategory.UTILITIES)
public class AutoReconnect
extends HaronModule {
    public boolean n() {
        return false;
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
    }

    public int o() {
        return 0;
    }
}

