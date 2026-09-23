package rockstar.modules.other;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import net.minecraft.client.gui.screen.DeathScreen;
import net.minecraft.text.Text;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.event.EventListener;
import rockstar.client.internal.game.WaypointStore;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.ClientMessages;
import rockstar.client.module.Module;

@ModuleInfo(name="Death Cords", category=ModuleCategory.OTHER, internalMethod09633="modules.descriptions.death_cords")
public class DeathCordsModule
extends Module {
    private boolean internalField0277;
    private BooleanSetting internalField0650;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (DeathCordsModule.internalField0149.currentScreen instanceof DeathScreen && DeathCordsModule.internalField0149.player != null) {
            if (this.internalField0277) {
                int n = (int)DeathCordsModule.internalField0149.player.getX();
                int n2 = (int)DeathCordsModule.internalField0149.player.getY();
                int n3 = (int)DeathCordsModule.internalField0149.player.getZ();
                ClientMessages.internalMethod01809(Text.of((String)LanguageManager.internalMethod00160("death_cords.coords", n + " " + n2 + " " + n3)));
                if (this.internalField0650.internalMethod04496()) {
                    WaypointStore typedValue188 = RockstarClient.getInstance().internalMethod06121();
                    if (typedValue188.internalMethod06387("Death")) {
                        typedValue188.internalMethod06386("Death");
                    }
                    typedValue188.internalMethod05412("Death", n, n2, n3);
                }
                this.internalField0277 = false;
            }
        } else {
            this.internalField0277 = true;
        }
    };

    public DeathCordsModule() {
        this.internalMethod09570();
    }

    private void internalMethod09570() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.death_cords.waypoint");
    }
}
