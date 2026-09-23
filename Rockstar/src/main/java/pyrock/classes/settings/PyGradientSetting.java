package pyrock.classes.settings;




import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import lombok.Generated;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.GradientColorSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyGradientSetting {
    private final GradientColorSetting setting;

    public PyGradientSetting(PyModule pyModule, String string) {
        this.setting = new GradientColorSetting(pyModule.getModule(), string);
        this.defaults();
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyGradientSetting(PyHudElement pyHudElement, String string) {
        this.setting = new GradientColorSetting(pyHudElement, string);
        this.defaults();
    }

    public PyGradientSetting(PyEspElement pyEspElement, String string) {
        this.setting = new GradientColorSetting(pyEspElement.getElement(), string);
        this.defaults();
    }

    public PyGradientSetting(GradientColorSetting typedValue168) {
        this.setting = typedValue168;
    }

    private void defaults() {
        this.setting.internalMethod00046(new ColorRGBA(255.0f, 255.0f, 255.0f, 255.0f), new ColorRGBA(255.0f, 255.0f, 255.0f, 255.0f));
        this.setting.internalMethod04397(true);
    }

    public PyGradientSetting set(ColorRGBA colorRGBA, ColorRGBA colorRGBA2) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod00046(colorRGBA, colorRGBA2);
        return this;
    }

    public PyGradientSetting first(ColorRGBA colorRGBA) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod01227(colorRGBA);
        return this;
    }

    public PyGradientSetting second(ColorRGBA colorRGBA) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod04884(colorRGBA);
        return this;
    }

    public ColorRGBA getFirst() {
        return this.setting.internalMethod05319();
    }

    public ColorRGBA getSecond() {
        return this.setting.internalMethod01482();
    }

    public ColorRGBA at(double d) {
        float f = (float)Math.max(0.0, Math.min(1.0, d));
        return this.getFirst().mix(this.getSecond(), f);
    }

    public PyGradientSetting alpha(boolean bl) {
        this.setting.internalMethod04397(bl);
        return this;
    }

    public boolean hasAlpha() {
        return this.setting.internalMethod04496();
    }

    @Generated
    public GradientColorSetting getSetting() {
        return this.setting;
    }
}

