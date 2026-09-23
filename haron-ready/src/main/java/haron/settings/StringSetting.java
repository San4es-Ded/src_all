package haron.settings;

import haron.settings.Setting;
import java.util.function.Supplier;

public class StringSetting
extends Setting<String> {
    @Override
    public Setting<String> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<String> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public StringSetting(String string, String string2, String string3) {
        super(string, string2, string3 != null ? string3 : "");
    }

    public StringSetting(String string, String string2) {
        this(string, "", string2);
    }

    public String get() {
        return (String)this.k();
    }

    public StringSetting a(Supplier<Boolean> supplier) {
        return (StringSetting)this.visibleWhen(supplier);
    }

    @Override
    public void a(String string) {
        this.set(string);
    }

    public String a() {
        return this.get();
    }

    public void set(String string) {
        super.a(string != null ? string : "");
    }
}

