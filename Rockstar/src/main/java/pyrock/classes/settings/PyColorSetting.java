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
import rockstar.client.setting.ColorSetting;
import rockstar.client.internal.core.ScriptModule;

public class PyColorSetting {
    private final ColorSetting setting;

    public PyColorSetting(PyModule pyModule, String string) {
        this.setting = new ColorSetting(pyModule.getModule(), string);
        this.setting.internalMethod04886(new ColorRGBA(255.0f, 255.0f, 255.0f, 255.0f));
        this.setting.internalMethod05166(true);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyColorSetting(PyHudElement pyHudElement, String string) {
        this.setting = new ColorSetting(pyHudElement, string);
        this.setting.internalMethod04886(new ColorRGBA(255.0f, 255.0f, 255.0f, 255.0f));
        this.setting.internalMethod05166(true);
    }

    public PyColorSetting(PyEspElement pyEspElement, String string) {
        this.setting = new ColorSetting(pyEspElement.getElement(), string);
        this.setting.internalMethod04886(new ColorRGBA(255.0f, 255.0f, 255.0f, 255.0f));
        this.setting.internalMethod05166(true);
    }

    public PyColorSetting(ColorSetting typedValue167) {
        this.setting = typedValue167;
    }

    public PyColorSetting color(ColorRGBA colorRGBA) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod04886(colorRGBA);
        return this;
    }

    public PyColorSetting color(int n, int n2, int n3, int n4) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod04886(new ColorRGBA(n, n2, n3, n4));
        return this;
    }

    public PyColorSetting color(int n, int n2, int n3) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod04886(new ColorRGBA(n, n2, n3, 255.0f));
        return this;
    }

    public PyColorSetting alpha(boolean bl) {
        this.setting.internalMethod05166(bl);
        return this;
    }

    public ColorRGBA get() {
        return this.setting.internalMethod05620();
    }

    public boolean hasAlpha() {
        return this.setting.internalMethod04496();
    }

    @Generated
    public ColorSetting getSetting() {
        return this.setting;
    }
}

