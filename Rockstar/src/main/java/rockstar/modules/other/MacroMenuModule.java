package rockstar.modules.other;








import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.client.gui.screen.Screen;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.internal.core.ItemCooldownHolder;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.KeybindUtils;
import rockstar.client.internal.script.AssistScreen;
import rockstar.client.internal.inventory.AssistSwapItem;
import rockstar.client.internal.core.AssistItemCatalog;
import rockstar.client.module.Module;

@ModuleInfo(name="Macro Menu", category=ModuleCategory.OTHER)
public class MacroMenuModule
extends Module {
    private ButtonSetting internalField0663;
    private final List<AssistSwapItem> internalField0416 = AssistItemCatalog.internalMethod02335();
    private final List<AssistSwapItem> internalField0417 = new ArrayList<AssistSwapItem>(this.internalField0416);
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> {
        if (keyPressEvent.getAction() != 1) {
            return;
        }
        if (MacroMenuModule.internalField0149.currentScreen != null) {
            return;
        }
        this.internalMethod08972(keyPressEvent.getKey());
    };
    private final EventListener<MouseEvent> internalField0158 = mouseEvent -> {
        if (mouseEvent.getAction() != 1) {
            return;
        }
        if (MacroMenuModule.internalField0149.currentScreen != null) {
            return;
        }
        this.internalMethod08972(mouseEvent.getButton());
    };

    public MacroMenuModule() {
        this.internalMethod09816();
    }

    private void internalMethod09816() {
        this.internalField0663 = new ButtonSetting(this, "\u041e\u0442\u043a\u0440\u044b\u0442\u044c \u043c\u0435\u043d\u044e").internalMethod07149(() -> internalField0149.setScreen((Screen)new AssistScreen()));
    }

    private void internalMethod08972(int n) {
        for (AssistSwapItem typedValue106 : this.internalField0417) {
            if (!typedValue106.internalMethod03236() || !KeybindUtils.internalMethod04328(typedValue106.internalMethod03234(), n)) continue;
            ItemCooldownHolder.internalField0006.internalMethod04839(typedValue106.internalMethod06489().getItem(), typedValue106::internalMethod05467, typedValue106.internalMethod00556());
            return;
        }
    }

    public final void internalMethod07272(List<AssistSwapItem> list) {
        this.internalField0417.clear();
        this.internalField0417.addAll(list);
    }

    @Generated
    public List<AssistSwapItem> internalMethod00384() {
        return this.internalField0416;
    }

    @Generated
    public List<AssistSwapItem> internalMethod06124() {
        return this.internalField0417;
    }
}
