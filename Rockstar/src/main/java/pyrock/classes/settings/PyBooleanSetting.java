package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyBooleanSetting {
    private final BooleanSetting setting;

    public PyBooleanSetting(PyModule pyModule, String string) {
        this.setting = new BooleanSetting(pyModule.getModule(), string);
        this.setting.internalMethod02034(false);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyBooleanSetting(PyHudElement pyHudElement, String string) {
        this.setting = new BooleanSetting(pyHudElement, string);
        this.setting.internalMethod02034(false);
    }

    public PyBooleanSetting(PyEspElement pyEspElement, String string) {
        this.setting = new BooleanSetting(pyEspElement.getElement(), string);
        this.setting.internalMethod02034(false);
    }

    public PyBooleanSetting(BooleanSetting typedValue164) {
        this.setting = typedValue164;
    }

    public PyBooleanSetting set(boolean bl) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod02034(bl);
        return this;
    }

    public boolean get() {
        return this.setting.internalMethod04496();
    }

    public PyBooleanSetting toggle() {
        PythonScript.internalMethod06468(this.setting);
        this.setting.toggle();
        return this;
    }

    @Generated
    public BooleanSetting getSetting() {
        return this.setting;
    }
}

