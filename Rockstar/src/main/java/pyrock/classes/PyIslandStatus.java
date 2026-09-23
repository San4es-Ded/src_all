package pyrock.classes;





import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.internal.script.*;
import jep.python.PyCallable;
import net.minecraft.text.Text;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.internal.script.DynamicIslandHud;
import rockstar.client.internal.script.DynamicIslandStatus;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.util.ClientMessages;

public class PyIslandStatus
extends DynamicIslandStatus {
    private final PythonScript owner;
    private PyCallable visibleWhen;
    private PyCallable measureCallback;
    private PyCallable renderCallback;
    private PyCallable clickCallback;
    private PyCallable colorCallback;
    private ColorRGBA color = ThemeColors.internalMethod07738();
    private boolean expandable;
    private boolean disposed;
    private boolean errored;
    private DynamicIslandHud island;
    private float x;
    private float y;
    private float width;
    private float height;
    private float alpha;

    public PyIslandStatus(PythonScript typedValue155, MultiSelectSetting typedValue173, String string, float f, float f2, float f3) {
        super(typedValue173, string, false);
        this.owner = typedValue155;
        this.size.internalMethod03811(Math.max(1.0f, f), Math.max(1.0f, f2), Math.max(0.0f, f3));
    }

    public PyIslandStatus visibleWhen(PyCallable pyCallable) {
        this.visibleWhen = pyCallable;
        this.errored = false;
        return this;
    }

    public PyIslandStatus showWhen(PyCallable pyCallable) {
        return this.visibleWhen(pyCallable);
    }

    public PyIslandStatus measure(PyCallable pyCallable) {
        this.measureCallback = pyCallable;
        this.errored = false;
        return this;
    }

    public PyIslandStatus renderer(PyCallable pyCallable) {
        this.renderCallback = pyCallable;
        this.errored = false;
        return this;
    }

    public PyIslandStatus render(PyCallable pyCallable) {
        return this.renderer(pyCallable);
    }

    public PyIslandStatus onClick(PyCallable pyCallable) {
        this.clickCallback = pyCallable;
        this.errored = false;
        return this;
    }

    public PyIslandStatus clickCallback(PyCallable pyCallable) {
        return this.onClick(pyCallable);
    }

    public PyIslandStatus colorFn(PyCallable pyCallable) {
        this.colorCallback = pyCallable;
        this.errored = false;
        return this;
    }

    public PyIslandStatus color(ColorRGBA colorRGBA) {
        this.color = colorRGBA == null ? ThemeColors.internalMethod07738() : colorRGBA;
        this.colorCallback = null;
        return this;
    }

    public PyIslandStatus color(double d, double d2, double d3) {
        return this.color(d, d2, d3, 255.0);
    }

    public PyIslandStatus color(double d, double d2, double d3, double d4) {
        return this.color(new ColorRGBA((float)d, (float)d2, (float)d3, (float)d4));
    }

    public PyIslandStatus size(double d, double d2) {
        return this.size(d, d2, this.size.internalField1048);
    }

    public PyIslandStatus size(double d, double d2, double d3) {
        this.size.internalMethod03811(Math.max(1.0f, (float)d), Math.max(1.0f, (float)d2), Math.max(0.0f, (float)d3));
        return this;
    }

    public PyIslandStatus width(double d) {
        this.size.internalField0205 = Math.max(1.0f, (float)d);
        return this;
    }

    public PyIslandStatus height(double d) {
        this.size.internalField0206 = Math.max(1.0f, (float)d);
        return this;
    }

    public PyIslandStatus radius(double d) {
        this.size.internalField1048 = Math.max(0.0f, (float)d);
        return this;
    }

    public PyIslandStatus expandable(boolean bl) {
        this.expandable = bl;
        return this;
    }

    public PyIslandStatus selected(boolean bl) {
        if (bl) {
            this.getParent().internalMethod05668(this);
        } else {
            this.getParent().internalMethod07492().remove(this);
        }
        RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
        return this;
    }

    public boolean selected() {
        return this.isSelected();
    }

    @Override
    public boolean isExpandable() {
        return this.expandable;
    }

    @Override
    public float radius(DynamicIslandHud typedValue201) {
        return this.size.internalField1048;
    }

    @Override
    public void prepare(DynamicIslandHud typedValue201) {
        this.island = typedValue201;
        if (!this.alive() || this.measureCallback == null) {
            return;
        }
        try (AutoCloseable autoCloseable = PythonScript.internalMethod02561(this.owner);){
            this.measureCallback.call(new Object[]{this});
        }
        catch (Throwable throwable) {
            this.handleError("measure", throwable);
        }
    }

    @Override
    public void render(UiRenderContext iII, DynamicIslandHud typedValue201, float f, float f2, float f3, float f4, float f5) {
        this.island = typedValue201;
        this.x = f;
        this.y = f2;
        this.width = f3;
        this.height = f4;
        this.alpha = f5;
        if (!this.alive() || this.renderCallback == null) {
            return;
        }
        try (AutoCloseable autoCloseable = PythonScript.internalMethod02561(this.owner);){
            this.renderCallback.call(new Object[]{iII, this});
        }
        catch (Throwable throwable) {
            this.handleError("render", throwable);
        }
    }

    @Override
    public void click(float f, float f2, int n) {
        if (!this.alive() || this.clickCallback == null) {
            return;
        }
        try (AutoCloseable autoCloseable = PythonScript.internalMethod02561(this.owner);){
            this.clickCallback.call(new Object[]{Float.valueOf(f), Float.valueOf(f2), n, this});
        }
        catch (Throwable throwable) {
            this.handleError("click", throwable);
        }
    }

    @Override
    public boolean canShow() {
        if (!this.alive()) {
            return false;
        }
        if (this.visibleWhen == null) {
            return true;
        }
        try (AutoCloseable ignored = PythonScript.internalMethod02561(this.owner)) {
            return PyIslandStatus.truthy(this.visibleWhen.call(new Object[]{this}));
        } catch (Throwable throwable) {
            this.handleError("visible", throwable);
            return false;
        }
    }

    @Override
    public ColorRGBA getColor() {
        if (!this.alive() || this.colorCallback == null) {
            return this.color;
        }
        try (AutoCloseable ignored = PythonScript.internalMethod02561(this.owner)) {
            Object value = this.colorCallback.call(new Object[]{this});
            return value instanceof ColorRGBA ? (ColorRGBA)value : this.color;
        } catch (Throwable throwable) {
            this.handleError("color", throwable);
            return this.color;
        }
    }

    public boolean ownedBy(PythonScript typedValue155) {
        return this.owner == typedValue155;
    }

    public boolean remove() {
        this.dispose();
        DynamicIslandHud typedValue201 = RockstarClient.getInstance().internalMethod01271().internalMethod01259();
        return typedValue201 != null && typedValue201.internalMethod00752(this);
    }

    public void dispose() {
        this.disposed = true;
        this.visibleWhen = null;
        this.measureCallback = null;
        this.renderCallback = null;
        this.clickCallback = null;
        this.colorCallback = null;
    }

    public float getX() {
        return this.x;
    }

    public float getY() {
        return this.y;
    }

    public float getWidth() {
        return this.width;
    }

    public float getHeight() {
        return this.height;
    }

    public float renderAlpha() {
        return this.alpha;
    }

    public float dragAlpha() {
        return 0.0f;
    }

    public boolean extended() {
        return this.island != null && this.island.internalMethod08516();
    }

    public float extending() {
        return this.island == null ? 0.0f : this.island.internalMethod05767().internalMethod02881();
    }

    private boolean alive() {
        return !this.disposed && !this.errored && (this.owner == null || this.owner.internalMethod08681());
    }

    private void handleError(String string, Throwable throwable) {
        int n;
        if (this.errored) {
            return;
        }
        this.errored = true;
        this.visibleWhen = null;
        this.measureCallback = null;
        this.renderCallback = null;
        this.clickCallback = null;
        this.colorCallback = null;
        this.getParent().internalMethod07492().remove(this);
        String string2 = throwable.getMessage();
        if (string2 == null || string2.isBlank()) {
            string2 = throwable.getClass().getSimpleName();
        }
        if ((n = string2.indexOf(58)) >= 0 && n + 1 < string2.length()) {
            string2 = string2.substring(n + 1).trim();
        }
        ClientMessages.internalMethod09025(Text.of((String)("[Python Island Error] " + this.getName() + " (" + string + "): " + string2)));
        RockstarClient.internalField0572.error("Python island status error in '{}' during {}", new Object[]{this.getName(), string, throwable});
    }

    private static boolean truthy(Object object) {
        if (object == null) {
            return false;
        }
        if (object instanceof Boolean) {
            Boolean bl = (Boolean)object;
            return bl;
        }
        if (object instanceof Number) {
            Number number = (Number)object;
            return number.doubleValue() != 0.0;
        }
        if (object instanceof CharSequence) {
            CharSequence charSequence = (CharSequence)object;
            return !charSequence.isEmpty();
        }
        return true;
    }
}
