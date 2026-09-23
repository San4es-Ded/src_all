package pyrock.classes.settings;





import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.List;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.ModeSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.core.ScriptModule;

public class PyModeSetting {
    private final ModeSetting setting;

    public PyModeSetting(PyModule pyModule, String string) {
        this.setting = new ModeSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyModeSetting(PyHudElement pyHudElement, String string) {
        this.setting = new ModeSetting(pyHudElement, string);
    }

    public PyModeSetting(PyEspElement pyEspElement, String string) {
        this.setting = new ModeSetting(pyEspElement.getElement(), string);
    }

    public PyModeSetting(ModeSetting typedValue170) {
        this.setting = typedValue170;
    }

    public PyModeSetting add(String string) {
        if (this.has(string)) {
            return this;
        }
        PythonScript.internalMethod06468(this.setting);
        ModeSetting.InternalType0088 nestedValue0042 = new ModeSetting.InternalType0088(this.setting, string);
        PythonScript.internalMethod06874(this.setting, nestedValue0042);
        return this;
    }

    public boolean has(String string) {
        if (string == null) {
            return false;
        }
        for (ModeSetting.InternalType0088 nestedValue0042 : this.setting.internalMethod06723()) {
            if (!string.equalsIgnoreCase(nestedValue0042.getName()) && !string.equalsIgnoreCase(LanguageManager.internalMethod07214(nestedValue0042.getName()))) continue;
            return true;
        }
        return false;
    }

    public String[] options() {
        List<ModeSetting.InternalType0088> list = this.setting.internalMethod06723();
        String[] stringArray = new String[list.size()];
        for (int i = 0; i < stringArray.length; ++i) {
            stringArray[i] = list.get(i).getName();
        }
        return stringArray;
    }

    public String[] optionLabels() {
        String[] stringArray = this.options();
        String[] stringArray2 = new String[stringArray.length];
        for (int i = 0; i < stringArray.length; ++i) {
            stringArray2[i] = LanguageManager.internalMethod07214(stringArray[i]);
        }
        return stringArray2;
    }

    public int modeIndex() {
        List<ModeSetting.InternalType0088> list = this.setting.internalMethod06723();
        for (int i = 0; i < list.size(); ++i) {
            if (list.get(i) != this.setting.internalMethod07418()) continue;
            return i;
        }
        return -1;
    }

    public boolean isKey(String string) {
        ModeSetting.InternalType0088 nestedValue0042 = this.setting.internalMethod07418();
        if (nestedValue0042 == null || string == null) {
            return false;
        }
        return string.equalsIgnoreCase(nestedValue0042.getName()) || string.equalsIgnoreCase(LanguageManager.internalMethod07214(nestedValue0042.getName()));
    }

    public int count() {
        return this.setting.internalMethod06723().size();
    }

    public PyModeSetting select(String string) {
        PythonScript.internalMethod06468(this.setting);
        for (ModeSetting.InternalType0088 nestedValue0042 : this.setting.internalMethod06723()) {
            if (!nestedValue0042.getName().equals(string)) continue;
            this.setting.internalMethod03917(nestedValue0042);
            break;
        }
        return this;
    }

    public String get() {
        return this.setting.internalMethod07418() != null ? this.setting.internalMethod07418().getName() : null;
    }

    public boolean is(String string) {
        return this.setting.internalMethod07418() != null && this.setting.internalMethod07418().getName().equals(string);
    }

    @Generated
    public ModeSetting getSetting() {
        return this.setting;
    }
}

