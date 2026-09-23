package haron.settings;

import haron.settings.Setting;
import java.util.function.Supplier;

public class NumberSetting
extends Setting<Float> {
    private final float min;
    private final float max;
    private final float step;

    public int roundedInt() {
        return Math.round(this.get());
    }

    @Override
    public Setting<Float> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<Float> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public NumberSetting(String string, float f, float f2, float f3, float f4) {
        this(string, "", f, f2, f3, f4);
    }

    public NumberSetting(String string, float f, float f2, float f3) {
        this(string, "", f, f2, f3, 0.1f);
    }

    public NumberSetting(String string, String string2, float f, float f2, float f3, float f4) {
        super(string, string2, Float.valueOf(f));
        this.min = f2;
        this.max = f3;
        this.step = f4;
        this.set(f);
    }

    public float get() {
        return ((Float)this.k()).floatValue();
    }

    public float min() {
        return this.min;
    }

    public float max() {
        int n = 368;
        return this.max;
    }

    public float e() {
        return this.step();
    }

    public int b() {
        return this.roundedInt();
    }

    public float c() {
        return this.min();
    }

    public float d() {
        return this.max();
    }

    public float a() {
        return this.get();
    }

    public NumberSetting a(Supplier<Boolean> supplier) {
        return (NumberSetting)this.visibleWhen(supplier);
    }

    public void a(float f) {
        this.set(f);
    }

    public void set(float f) {
        super.a(Float.valueOf(Math.max(this.min, Math.min(this.max, (float)Math.round(f / this.step) * this.step))));
    }

    public float step() {
        return this.step;
    }
}
