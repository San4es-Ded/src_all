package rockstar.client.module;







import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import rockstar.client.setting.SettingOwner;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.ui.ScreenMetricsAccess;
import rockstar.client.core.Toggleable;

public interface ModuleEntry
extends SettingOwner,
MinecraftClientAccess,
ScreenMetricsAccess,
Toggleable {
    public void disable();

    public void enable();

    public void internalMethod08229();

    public boolean isEnabledByDefault();

    public String getName();

    default public String internalMethod05655() {
        String string = "modules.descriptions.%s".formatted(this.getName().toLowerCase().replace(" ", "_"));
        return LanguageManager.internalMethod07214(string);
    }

    public int getKeybind();

    public ModuleCategory getCategory();

    public boolean isEnabled();

    public boolean internalMethod08983();

    default public boolean isAvailable() {
        return true;
    }

    public AnimatedValue internalMethod00056();

    public void setKeybind(int localValue1);

    public void setEnabled(boolean localValue1, boolean localValue2);
}

