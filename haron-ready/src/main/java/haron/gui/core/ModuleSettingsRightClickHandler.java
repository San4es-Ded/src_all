package haron.gui.core;

import haron.client.MinecraftClientAccess;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiScreen;
import haron.gui.modules.ModulesTab;
import haron.gui.modules.ModuleCard;
import haron.module.HaronModule;
import net.minecraft.client.gui.screen.Screen;

public final class ModuleSettingsRightClickHandler
implements MinecraftClientAccess {
    public static boolean handleRightClick(double d, double d2) {
        HaronModule jxs16t2;
        Screen screen = ModuleSettingsRightClickHandler.c.currentScreen;
        if (!(screen instanceof ClickGuiScreen)) {
            return false;
        }
        ClickGuiTab ta3d0p2 = ((ClickGuiScreen)screen).a();
        if (!(ta3d0p2 instanceof ModulesTab)) {
            return true;
        }
        ModulesTab so7bsc2 = (ModulesTab)ta3d0p2;
        ModuleCard sudbet2 = so7bsc2.findCardAtBounds((int)d, (int)d2);
        if (sudbet2 == null || (jxs16t2 = sudbet2.l().e()) == null || jxs16t2.m().isEmpty()) {
            return true;
        }
        so7bsc2.openSettingsForCard(sudbet2);
        return true;
    }

    private ModuleSettingsRightClickHandler() {
    }
}

