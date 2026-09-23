package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import jep.python.PyCallable;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.ButtonSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyButtonSetting {
    private final ButtonSetting setting;

    public PyButtonSetting(PyModule pyModule, String string) {
        this.setting = new ButtonSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyButtonSetting(PyHudElement pyHudElement, String string) {
        this.setting = new ButtonSetting(pyHudElement, string);
    }

    public PyButtonSetting(PyEspElement pyEspElement, String string) {
        this.setting = new ButtonSetting(pyEspElement.getElement(), string);
    }

    public PyButtonSetting(ButtonSetting typedValue166) {
        this.setting = typedValue166;
    }

    public PyButtonSetting action(PyCallable pyCallable) {
        this.setting.internalMethod07149(() -> {
            try {
                pyCallable.call(new Object[0]);
            }
            catch (Exception exception) {
                exception.printStackTrace();
            }
        });
        return this;
    }

    public void click() {
        this.setting.internalMethod03496().run();
    }

    @Generated
    public ButtonSetting getSetting() {
        return this.setting;
    }
}

