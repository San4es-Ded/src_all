package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.Locale;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.TimeSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyTimeSetting {
    private final TimeSetting setting;

    public PyTimeSetting(PyModule pyModule, String string) {
        this.setting = new TimeSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyTimeSetting(PyHudElement pyHudElement, String string) {
        this.setting = new TimeSetting(pyHudElement, string);
    }

    public PyTimeSetting(PyEspElement pyEspElement, String string) {
        this.setting = new TimeSetting(pyEspElement.getElement(), string);
    }

    public PyTimeSetting(TimeSetting typedValue180) {
        this.setting = typedValue180;
    }

    public PyTimeSetting set(int n) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod04593(n);
        return this;
    }

    public int get() {
        return this.setting.internalMethod09221();
    }

    public long millis() {
        return this.setting.internalMethod02082();
    }

    public int ticks() {
        return this.setting.internalMethod08553();
    }

    public String formatted() {
        return this.setting.internalMethod08164();
    }

    public int hours() {
        return this.setting.internalMethod02081();
    }

    public int minutes() {
        return this.setting.internalMethod02085();
    }

    public int seconds() {
        return this.setting.internalMethod08552();
    }

    public PyTimeSetting units(String string) {
        this.setting.internalMethod03569(PyTimeSetting.unit(string));
        return this;
    }

    public PyTimeSetting units(String string, String string2) {
        this.setting.internalMethod03569(PyTimeSetting.unit(string), PyTimeSetting.unit(string2));
        return this;
    }

    public PyTimeSetting units(String string, String string2, String string3) {
        this.setting.internalMethod03569(PyTimeSetting.unit(string), PyTimeSetting.unit(string2), PyTimeSetting.unit(string3));
        return this;
    }

    public PyTimeSetting unit(String string, boolean bl) {
        switch (PyTimeSetting.unit(string)) {
            case internalField0779: {
                this.setting.internalMethod04919(bl);
                break;
            }
            case internalField0780: {
                this.setting.internalMethod01760(bl);
                break;
            }
            case internalField1313: {
                this.setting.internalMethod08823(bl);
            }
        }
        return this;
    }

    public PyTimeSetting maxHours(int n) {
        this.setting.internalMethod06907(n);
        return this;
    }

    public PyTimeSetting maxMinutes(int n) {
        this.setting.internalMethod03793(n);
        return this;
    }

    private static TimeSetting.InternalType0178 unit(String string) {
        String string2;
        return switch (string2 = string == null ? "" : string.trim().toLowerCase(Locale.ROOT)) {
            case "h", "hour", "hours", "\u0447\u0430\u0441", "\u0447\u0430\u0441\u044b" -> TimeSetting.InternalType0178.internalField0779;
            case "s", "sec", "second", "seconds", "\u0441\u0435\u043a", "\u0441\u0435\u043a\u0443\u043d\u0434\u044b" -> TimeSetting.InternalType0178.internalField1313;
            default -> TimeSetting.InternalType0178.internalField0780;
        };
    }

    @Generated
    public TimeSetting getSetting() {
        return this.setting;
    }
}

