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
import rockstar.client.setting.VectorRangeSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyPositionSetting {
    private final VectorRangeSetting setting;

    public PyPositionSetting(PyModule pyModule, String string) {
        this.setting = new VectorRangeSetting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyPositionSetting(PyHudElement pyHudElement, String string) {
        this.setting = new VectorRangeSetting(pyHudElement, string);
    }

    public PyPositionSetting(PyEspElement pyEspElement, String string) {
        this.setting = new VectorRangeSetting(pyEspElement.getElement(), string);
    }

    public PyPositionSetting(VectorRangeSetting typedValue171) {
        this.setting = typedValue171;
    }

    public PyPositionSetting set(double d, double d2) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod07235((float)d, (float)d2);
        return this;
    }

    public PyPositionSetting x(double d) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod02107((float)d);
        return this;
    }

    public PyPositionSetting y(double d) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod07041((float)d);
        return this;
    }

    public float getX() {
        return this.setting.internalMethod03695();
    }

    public float getY() {
        return this.setting.internalMethod03697();
    }

    public List<Float> get() {
        return List.of(Float.valueOf(this.setting.internalMethod03695()), Float.valueOf(this.setting.internalMethod03697()));
    }

    public PyPositionSetting bounds(double d, double d2, double d3, double d4) {
        this.setting.internalMethod08471((float)d).internalMethod08506((float)d2).internalMethod07903((float)d3).internalMethod07929((float)d4);
        return this;
    }

    public float getMinX() {
        return this.setting.internalMethod08931();
    }

    public float getMaxX() {
        return this.setting.internalMethod08932();
    }

    public float getMinY() {
        return this.setting.internalMethod08949();
    }

    public float getMaxY() {
        return this.setting.internalMethod08951();
    }

    @Generated
    public VectorRangeSetting getSetting() {
        return this.setting;
    }
}

