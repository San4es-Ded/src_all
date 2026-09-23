package haron.cosmetic;

import haron.module.ModuleManager;
import haron.modules.visuals.Cosmetics;

class CosmeticModuleAccess {
    private CosmeticModuleAccess() {
    }

    static Cosmetics get() {
        int n = 115;
        return ModuleManager.get(Cosmetics.class);
    }
}

