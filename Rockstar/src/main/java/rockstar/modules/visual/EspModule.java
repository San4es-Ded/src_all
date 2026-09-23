package rockstar.modules.visual;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.script.*;
import rockstar.client.*;

import net.minecraft.client.gui.screen.Screen;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.script.EspEditorScreen;
import rockstar.client.module.Module;

@ModuleInfo(name="ESP", category=ModuleCategory.VISUALS, internalMethod08049=true, internalMethod09633="modules.descriptions.esp")
public class EspModule
extends Module {
    private ButtonSetting internalField0663;

    public EspModule() {
        this.internalMethod09738();
    }

    private void internalMethod09738() {
        this.internalField0663 = new ButtonSetting(this, "modules.settings.esp.open_menu").internalMethod07149(() -> internalField0149.setScreen((Screen)new EspEditorScreen()));
    }
}
