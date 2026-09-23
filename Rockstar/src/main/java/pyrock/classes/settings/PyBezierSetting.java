package pyrock.classes.settings;





import rockstar.client.setting.*;
import rockstar.client.animation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import java.util.List;
import lombok.Generated;
import net.minecraft.util.math.Vec2f;
import pyrock.classes.PyEspElement;
import pyrock.classes.PyHudElement;
import pyrock.classes.PyModule;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.Vector2Setting;
import rockstar.client.internal.core.ScriptModule;
import rockstar.client.animation.Easing;

public class PyBezierSetting {
    private final Vector2Setting setting;

    public PyBezierSetting(PyModule pyModule, String string) {
        this.setting = new Vector2Setting(pyModule.getModule(), string);
        if (!(pyModule.getModule() instanceof ScriptModule)) {
            PythonScript.internalMethod04663(pyModule.getModule(), this.setting);
        }
    }

    public PyBezierSetting(PyHudElement pyHudElement, String string) {
        this.setting = new Vector2Setting(pyHudElement, string);
    }

    public PyBezierSetting(PyEspElement pyEspElement, String string) {
        this.setting = new Vector2Setting(pyEspElement.getElement(), string);
    }

    public PyBezierSetting(Vector2Setting typedValue160) {
        this.setting = typedValue160;
    }

    public PyBezierSetting start(double d, double d2) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod06395((float)d, (float)d2);
        return this;
    }

    public PyBezierSetting end(double d, double d2) {
        PythonScript.internalMethod06468(this.setting);
        this.setting.internalMethod06615((float)d, (float)d2);
        return this;
    }

    public List<Float> getStart() {
        Vec2f vec2f = this.setting.internalMethod03179();
        return List.of(Float.valueOf(vec2f.x), Float.valueOf(vec2f.y));
    }

    public List<Float> getEnd() {
        Vec2f vec2f = this.setting.internalMethod00322();
        return List.of(Float.valueOf(vec2f.x), Float.valueOf(vec2f.y));
    }

    public float ease(double d) {
        float f = (float)Math.max(0.0, Math.min(1.0, d));
        Easing typedValue214 = this.setting.internalMethod03095();
        return typedValue214 == null ? f : typedValue214.ease(f, 0.0f, 1.0f, 1.0f);
    }

    public float interpolate(double d, double d2, double d3) {
        return (float)(d + (d2 - d) * (double)this.ease(d3));
    }

    @Generated
    public Vector2Setting getSetting() {
        return this.setting;
    }
}

