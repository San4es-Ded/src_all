package pyrock.classes.settings;





import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.core.ScriptModule;

public class PySelectSetting {
    private final MultiSelectSetting setting;

    public PySelectSetting(PyModule pyModule, String string) {
        this.setting = new MultiSelectSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PySelectSetting(PyHudElement pyHudElement, String string) {
        this.setting = new MultiSelectSetting(pyHudElement, string);
    }

    public PySelectSetting(PyEspElement pyEspElement, String string) {
        this.setting = new MultiSelectSetting(pyEspElement.getElement(), string);
    }

    public PySelectSetting(MultiSelectSetting typedValue173) {
        this.setting = typedValue173;
    }

    public PySelectSetting add(String string) {
        if (this.has(string)) {
            return this;
        }
        PythonScript.internalMethod06468(this.setting);
        MultiSelectSetting.InternalType0091 nestedValue0044 = new MultiSelectSetting.InternalType0091(this.setting, string);
        PythonScript.internalMethod00383(this.setting, nestedValue0044);
        return this;
    }

    public boolean has(String string) {
        if (string == null) {
            return false;
        }
        for (MultiSelectSetting.InternalType0091 nestedValue0044 : this.setting.internalMethod01792()) {
            if (!string.equalsIgnoreCase(nestedValue0044.getName()) && !string.equalsIgnoreCase(LanguageManager.internalMethod07214(nestedValue0044.getName()))) continue;
            return true;
        }
        return false;
    }

    public List<String> getValueLabels() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (MultiSelectSetting.InternalType0091 nestedValue0044 : this.setting.internalMethod01792()) {
            arrayList.add(LanguageManager.internalMethod07214(nestedValue0044.getName()));
        }
        return arrayList;
    }

    public PySelectSetting select(String string) {
        PythonScript.internalMethod06468(this.setting);
        for (MultiSelectSetting.InternalType0091 nestedValue0044 : this.setting.internalMethod01792()) {
            if (!nestedValue0044.getName().equals(string)) continue;
            this.setting.internalMethod05668(nestedValue0044);
            break;
        }
        return this;
    }

    public PySelectSetting min(int n) {
        this.setting.internalMethod03035(n);
        return this;
    }

    public PySelectSetting draggable() {
        this.setting.internalMethod05559();
        return this;
    }

    public boolean isSelected(String string) {
        for (MultiSelectSetting.InternalType0091 nestedValue0044 : this.setting.internalMethod07492()) {
            if (!nestedValue0044.getName().equals(string)) continue;
            return true;
        }
        return false;
    }

    public List<String> getSelected() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (MultiSelectSetting.InternalType0091 nestedValue0044 : this.setting.internalMethod07492()) {
            arrayList.add(nestedValue0044.getName());
        }
        return arrayList;
    }

    public List<String> getValues() {
        ArrayList<String> arrayList = new ArrayList<String>();
        for (MultiSelectSetting.InternalType0091 nestedValue0044 : this.setting.internalMethod01792()) {
            arrayList.add(nestedValue0044.getName());
        }
        return arrayList;
    }

    @Generated
    public MultiSelectSetting getSetting() {
        return this.setting;
    }
}

