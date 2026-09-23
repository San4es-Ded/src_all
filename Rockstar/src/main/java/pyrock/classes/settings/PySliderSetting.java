package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.SliderSetting;
import rockstar.client.internal.core.ScriptModule;

public class PySliderSetting {
    private final SliderSetting setting;

    public PySliderSetting(PyModule pyModule, String string) {
        this.setting = new SliderSetting(pyModule.getModule(), string);
        this.setting.internalMethod05900(1.0f);
        this.setting.internalMethod02732(10.0f);
        this.setting.internalMethod08074(5.0f);
        this.setting.internalMethod08673(1.0f);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PySliderSetting(PyHudElement pyHudElement, String string) {
        this.setting = new SliderSetting(pyHudElement, string);
        this.setting.internalMethod05900(1.0f);
        this.setting.internalMethod02732(10.0f);
        this.setting.internalMethod08074(5.0f);
        this.setting.internalMethod08673(1.0f);
    }

    public PySliderSetting(PyEspElement pyEspElement, String string) {
        this.setting = new SliderSetting(pyEspElement.getElement(), string);
        this.setting.internalMethod05900(1.0f);
        this.setting.internalMethod02732(10.0f);
        this.setting.internalMethod08074(5.0f);
        this.setting.internalMethod08673(1.0f);
    }

    public PySliderSetting(SliderSetting typedValue174) {
        this.setting = typedValue174;
    }

    public PySliderSetting min(double d) {
        this.setting.internalMethod05900((float)d);
        return this;
    }

    public PySliderSetting max(double d) {
        this.setting.internalMethod02732((float)d);
        return this;
    }

    public PySliderSetting step(double d) {
        this.setting.internalMethod08673((float)d);
        return this;
    }

    public PySliderSetting set(double d) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod08074((float)d);
        return this;
    }

    public PySliderSetting suffix(String string) {
        this.setting.internalMethod06240(string);
        return this;
    }

    public float get() {
        return this.setting.internalMethod08576();
    }

    public float getMin() {
        return this.setting.internalMethod05288();
    }

    public float getMax() {
        return this.setting.internalMethod05291();
    }

    public float getStep() {
        return this.setting.internalMethod08575();
    }

    @Generated
    public SliderSetting getSetting() {
        return this.setting;
    }
}

