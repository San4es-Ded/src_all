package pyrock.classes;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.List;
import lombok.Generated;
import pyrock.classes.PySetting;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.Setting;
import rockstar.client.module.ModuleEntry;
import rockstar.client.module.ModuleManager;
import rockstar.client.internal.core.ScriptModule;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.util.TextUtils;

public class PyModule {
    private final ModuleEntry module;
    private final String name;
    private final String category;

    public PyModule(String string, String string2) {
        this.name = string.trim();
        this.category = string2.trim();
        ModuleCategory typedValue150 = ModuleCategory.internalMethod05008(this.category);
        if (typedValue150 == null) {
            typedValue150 = ModuleCategory.OTHER;
        }
        ScriptModule typedValue149 = new ScriptModule(this.name, typedValue150, -1);
        RockstarClient.getInstance().getModuleManager().getModules().add(typedValue149);
        ModuleManager.internalMethod03046();
        PythonScript.internalMethod06022(typedValue149);
        this.module = typedValue149;
    }

    public PyModule(ModuleEntry typedValue145) {
        this.module = typedValue145;
        this.name = typedValue145.getName();
        this.category = typedValue145.getCategory().name();
    }

    public PySetting[] settings() {
        List<Setting> list = this.module.getSettings();
        PySetting[] pySettingArray = new PySetting[list.size()];
        for (int i = 0; i < list.size(); ++i) {
            pySettingArray[i] = new PySetting(list.get(i));
        }
        return pySettingArray;
    }

    public PySetting[] settingsApi() {
        return this.settings();
    }

    public boolean isEnabled() {
        return this.module.isEnabled();
    }

    public PyModule setEnabled(boolean bl) {
        this.module.setEnabled(bl, false);
        return this;
    }

    public PyModule toggle() {
        this.module.toggle();
        return this;
    }

    public int getKey() {
        return this.module.getKeybind();
    }

    public String getKeyName() {
        return TextUtils.internalMethod04982(this.module.getKeybind());
    }

    public PyModule setKey(int n) {
        this.module.setKeybind(n);
        return this;
    }

    public String getDesc() {
        return this.module.internalMethod05655();
    }

    public PyModule setDesc(String string) {
        ModuleEntry typedValue145 = this.module;
        if (typedValue145 instanceof ScriptModule) {
            ScriptModule typedValue149 = (ScriptModule)typedValue145;
            typedValue149.internalMethod05500(string);
        }
        return this;
    }

    @Generated
    public ModuleEntry getModule() {
        return this.module;
    }

    @Generated
    public String getName() {
        return this.name;
    }

    @Generated
    public String getCategory() {
        return this.category;
    }
}
