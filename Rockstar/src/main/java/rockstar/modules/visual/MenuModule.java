package rockstar.modules.visual;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;

import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import rockstar.modules.other.SoundsModule;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.ModeSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.core.AbstractMenuScreen;
import rockstar.client.internal.script.MenuOpenHandler;
import rockstar.client.internal.script.ModernMenuScreen;
import rockstar.client.internal.script.WorldPanelScreen;
import rockstar.client.util.KeybindUtils;
import rockstar.client.module.Module;
import rockstar.client.internal.core.ClientSoundEvents;

@ModuleInfo(name="Menu", category=ModuleCategory.VISUALS, defaultKey=344, internalMethod09633="modules.descriptions.menu")
public class MenuModule
extends Module {
    private ModeSetting internalField0668;
    private ModeSetting.InternalType0088 internalField0237;
    private ModeSetting.InternalType0088 internalField0238;
    private KeybindSetting internalField0648;
    private Screen internalField0691;
    private WorldPanelScreen internalField0551;
    private static boolean internalField0277;

    public MenuModule() {
        this.internalMethod09724();
    }

    private void internalMethod09724() {
        this.internalField0668 = new ModeSetting(this, "modules.settings.menu.mode");
        this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.menu.mode.dropdown");
        this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.menu.mode.modern").select();
        this.internalField0648 = new KeybindSetting(this, "modules.settings.menu.hide_key").internalMethod01713(342);
    }

    @Override
    public void onEnable() {
        boolean bl = this.internalField0238.isSelected();
        if (bl && MenuModule.internalField0149.currentScreen instanceof ModernMenuScreen) {
            return;
        }
        if (!bl && MenuModule.internalField0149.currentScreen instanceof WorldPanelScreen) {
            return;
        }
        this.internalField0691 = this.internalMethod03700();
        internalField0277 = true;
        internalField0149.setScreen(this.internalField0691);
        SoundsModule typedValue215 = RockstarClient.getInstance().getModuleManager().getModule(SoundsModule.class);
        if (typedValue215.isEnabled()) {
            ClientSoundEvents.internalField0130.internalMethod03864(typedValue215.internalMethod01798());
        }
        super.onEnable();
    }

    public Screen internalMethod03700() {
        if (this.internalField0238.isSelected()) {
            AbstractMenuScreen typedValue204 = RockstarClient.getInstance().internalMethod04334();
            AbstractMenuScreen typedValue205 = typedValue204 instanceof ModernMenuScreen ? typedValue204 : new ModernMenuScreen();
            RockstarClient.getInstance().internalMethod02331(typedValue205);
            return typedValue205;
        }
        if (this.internalField0551 == null) {
            this.internalField0551 = new WorldPanelScreen();
        }
        return this.internalField0551;
    }

    public static boolean internalMethod01859(int n) {
        return !internalField0277 && MenuModule.internalMethod01915(n);
    }

    public static void internalMethod08804(int n) {
        MenuModule typedValue321 = RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class);
        if (typedValue321 != null && KeybindUtils.internalMethod02025(typedValue321.getKeybind()) == n) {
            internalField0277 = false;
        }
    }

    private static boolean internalMethod01915(int n) {
        MenuModule typedValue321 = RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class);
        return typedValue321 != null && KeybindUtils.internalMethod04328(typedValue321.getKeybind(), n);
    }

    public static void internalMethod09723() {
        MenuModule typedValue321 = RockstarClient.getInstance().getModuleManager().getModule(MenuModule.class);
        MinecraftClient.getInstance().setScreen(typedValue321.internalMethod03700());
    }

    @Override
    public void onDisable() {
        internalField0277 = false;
        if (MenuModule.internalField0149.currentScreen == this.internalField0691) {
            internalField0149.setScreen(null);
        }
        super.onDisable();
    }

    @Generated
    public ModeSetting.InternalType0088 internalMethod01968() {
        return this.internalField0238;
    }

    @Generated
    public KeybindSetting internalMethod00598() {
        return this.internalField0648;
    }
}
