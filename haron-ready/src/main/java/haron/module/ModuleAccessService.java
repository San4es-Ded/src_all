package haron.module;

import haron.client.MinecraftClientAccess;
import haron.hud.core.HudServiceInfo;
import haron.hud.core.HudService;

@HudServiceInfo(enabledByDefault=true)
public class ModuleAccessService
extends HudService
implements MinecraftClientAccess {
    public boolean isModuleAllowed(String string) {
        return true;
    }

    public boolean a(String string) {
        return this.isModuleAllowed(string);
    }

    @Override
    public void initialize() {
        super.initialize();
    }
}
