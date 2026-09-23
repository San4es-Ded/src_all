package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.SectionSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyInfoSetting {
    private final SectionSetting setting;

    public PyInfoSetting(PyModule pyModule, String string) {
        this.setting = new SectionSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyInfoSetting(PyHudElement pyHudElement, String string) {
        this.setting = new SectionSetting(pyHudElement, string);
    }

    public PyInfoSetting(PyEspElement pyEspElement, String string) {
        this.setting = new SectionSetting(pyEspElement.getElement(), string);
    }

    public PyInfoSetting(SectionSetting typedValue169) {
        this.setting = typedValue169;
    }

    public PyInfoSetting level(int n) {
        this.setting.internalMethod04286(n);
        return this;
    }

    public PyInfoSetting splitted() {
        this.setting.internalMethod00288();
        return this;
    }

    public PyInfoSetting centered() {
        this.setting.internalMethod00983();
        return this;
    }

    @Generated
    public SectionSetting getSetting() {
        return this.setting;
    }
}

