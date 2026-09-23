package rockstar.modules.other;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import globals.client.ui.RocknetMenu;
import lombok.Generated;
import net.minecraft.client.gui.screen.Screen;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.module.Module;
import rockstar.client.internal.core.ClientSoundEvents;

@ModuleInfo(name="Globals Menu", category=ModuleCategory.OTHER, defaultKey=345)
public class GlobalsMenuModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private ModeSetting.InternalType0088 internalField1066;
    private ModeSetting internalField0669;
    private ModeSetting.InternalType0088 internalField1067;
    private ModeSetting.InternalType0088 internalField1068;
    private ModeSetting.InternalType0088 internalField1065;
    private KeybindSetting internalField0648;

    public GlobalsMenuModule() {
        this.internalMethod09555();
    }

    private void internalMethod09555() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.globals.visibility");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.globals.visibility.all").select();
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.globals.visibility.friends");
        this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.globals.visibility.none");
        this.internalField0669 = new ModeSetting(this, "modules.settings.globals.notifications");
        this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.globals.notifications.all").select();
        this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.globals.notifications.friends");
        this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0669, "modules.settings.globals.notifications.none");
        this.internalField0648 = new KeybindSetting(this, "modules.settings.globals.snowball_bind").internalMethod01713(71);
    }

    @Override
    public void onEnable() {
        if (GlobalsMenuModule.internalField0149.currentScreen instanceof RocknetMenu) {
            return;
        }
        RocknetMenu rocknetMenu = RockstarClient.getInstance().internalMethod01773();
        if (rocknetMenu == null) {
            return;
        }
        internalField0149.setScreen((Screen)rocknetMenu);
        SoundsModule typedValue215 = RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class);
        if (typedValue215.isEnabled()) {
            ClientSoundEvents.internalField0130.internalMethod03864(typedValue215.internalMethod01798());
        }
        super.onEnable();
    }

    @Override
    public void onDisable() {
        if (GlobalsMenuModule.internalField0149.currentScreen instanceof RocknetMenu) {
            internalField0149.setScreen(null);
            RockstarClient.getInstance().internalMethod01773().setClosing(true);
        }
        super.onDisable();
    }

    public void internalMethod01486(String string) {
        RockstarClient.getInstance().internalMethod06050().updateVisibility(string);
    }

    public boolean internalMethod09556() {
        return !this.internalField0669.internalMethod06103(this.internalField1065);
    }

    public boolean internalMethod03294(boolean bl) {
        if (this.internalField0669.internalMethod06103(this.internalField1065)) {
            return false;
        }
        if (this.internalField0669.internalMethod06103(this.internalField1067)) {
            return true;
        }
        return this.internalField0669.internalMethod06103(this.internalField1068) && bl;
    }

    @Generated
    public ModeSetting internalMethod06435() {
        return this.internalField0668;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod02794() {
        return this.internalField0237;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod02984() {
        return this.internalField0238;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod08860() {
        return this.internalField1066;
    }

    @Generated
    public ModeSetting internalMethod07103() {
        return this.internalField0669;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod08895() {
        return this.internalField1067;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod08430() {
        return this.internalField1068;
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod08468() {
        return this.internalField1065;
    }

    @Generated
    public KeybindSetting internalMethod06378() {
        return this.internalField0648;
    }
}
