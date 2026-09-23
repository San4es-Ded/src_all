package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.List;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.RangeSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyRangeSetting {
    private final RangeSetting setting;

    public PyRangeSetting(PyModule pyModule, String string) {
        this.setting = new RangeSetting(pyModule.getModule(), string);
        this.setting.internalMethod08834(1.0f);
        this.setting.internalMethod08219(10.0f);
        this.setting.internalMethod01407(3.0f);
        this.setting.internalMethod06328(7.0f);
        this.setting.internalMethod08853(1.0f);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyRangeSetting(PyHudElement pyHudElement, String string) {
        this.setting = new RangeSetting(pyHudElement, string);
        this.setting.internalMethod08834(1.0f);
        this.setting.internalMethod08219(10.0f);
        this.setting.internalMethod01407(3.0f);
        this.setting.internalMethod06328(7.0f);
        this.setting.internalMethod08853(1.0f);
    }

    public PyRangeSetting(PyEspElement pyEspElement, String string) {
        this.setting = new RangeSetting(pyEspElement.getElement(), string);
        this.setting.internalMethod08834(1.0f);
        this.setting.internalMethod08219(10.0f);
        this.setting.internalMethod01407(3.0f);
        this.setting.internalMethod06328(7.0f);
        this.setting.internalMethod08853(1.0f);
    }

    public PyRangeSetting(RangeSetting typedValue172) {
        this.setting = typedValue172;
    }

    public PyRangeSetting min(double d) {
        this.setting.internalMethod08834((float)d);
        return this;
    }

    public PyRangeSetting max(double d) {
        this.setting.internalMethod08219((float)d);
        return this;
    }

    public PyRangeSetting step(double d) {
        this.setting.internalMethod08853((float)d);
        return this;
    }

    public PyRangeSetting first(double d) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod01407((float)d);
        return this;
    }

    public PyRangeSetting second(double d) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod06328((float)d);
        return this;
    }

    public float getFirst() {
        return this.setting.internalMethod06919();
    }

    public float getSecond() {
        return this.setting.internalMethod07967();
    }

    public float getMin() {
        return this.setting.internalMethod07968();
    }

    public float getMax() {
        return this.setting.internalMethod07979();
    }

    public float getStep() {
        return this.setting.internalMethod07980();
    }

    public List<Float> get() {
        return List.of(Float.valueOf(this.setting.internalMethod06919()), Float.valueOf(this.setting.internalMethod07967()));
    }

    @Generated
    public RangeSetting getSetting() {
        return this.setting;
    }
}

