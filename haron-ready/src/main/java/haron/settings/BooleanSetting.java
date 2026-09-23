package haron.settings;

import haron.settings.Setting;
import java.util.function.Supplier;

public class BooleanSetting
extends Setting<Boolean> {
    @Override
    public Setting<Boolean> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<Boolean> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public BooleanSetting(String string, String string2, boolean bl) {
        super(string, string2, bl);
    }

    public BooleanSetting(String string, boolean bl) {
        this(string, "", bl);
    }

    public boolean get() {
        return (Boolean)this.k();
    }

    public void b() {
        this.toggle();
    }

    public BooleanSetting a(Supplier<Boolean> supplier) {
        return (BooleanSetting)this.visibleWhen(supplier);
    }

    public boolean a() {
        return this.get();
    }

    public void a(boolean bl) {
        this.set(bl);
    }

    public void set(boolean bl) {
        super.a(bl);
    }

    public void toggle() {
        this.set(!this.get());
    }
}
