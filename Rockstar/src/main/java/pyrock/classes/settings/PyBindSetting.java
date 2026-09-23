package pyrock.classes.settings;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.internal.core.ScriptModule;
import rockstar.client.internal.ui.KeybindParser;
import rockstar.client.util.TextUtils;

public class PyBindSetting {
    private final KeybindSetting setting;

    public PyBindSetting(PyModule pyModule, String string) {
        this.setting = new KeybindSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyBindSetting(PyHudElement pyHudElement, String string) {
        this.setting = new KeybindSetting(pyHudElement, string);
    }

    public PyBindSetting(PyEspElement pyEspElement, String string) {
        this.setting = new KeybindSetting(pyEspElement.getElement(), string);
    }

    public PyBindSetting(KeybindSetting typedValue161) {
        this.setting = typedValue161;
    }

    public PyBindSetting set(Object object) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod02164(PyBindSetting.toCode(object));
        return this;
    }

    public PyBindSetting clear() {
        return this.set(-1);
    }

    public int get() {
        return this.setting.internalMethod07477();
    }

    public String name() {
        return TextUtils.internalMethod04982(this.setting.internalMethod07477());
    }

    public boolean isSet() {
        return this.setting.internalMethod07477() != -1;
    }

    public boolean isKey(Object object) {
        return this.setting.internalMethod02165(PyBindSetting.toCode(object));
    }

    public boolean matches(Object object) {
        return this.isKey(object);
    }

    static int toCode(Object object) {
        if (object instanceof Number) {
            Number number = (Number)object;
            return number.intValue();
        }
        if (object == null) {
            return -1;
        }
        return KeybindParser.internalMethod06543(object.toString());
    }

    @Generated
    public KeybindSetting getSetting() {
        return this.setting;
    }
}

