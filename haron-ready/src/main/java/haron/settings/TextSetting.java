package haron.settings;

import haron.settings.Setting;
import java.util.function.Supplier;

public class TextSetting
extends Setting<String> {
    @Override
    public Setting<String> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<String> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public TextSetting(String string, String string2, String string3) {
        super(string, string2, string3);
    }

    public TextSetting(String string, String string2) {
        this(string, "", string2);
    }

    public String get() {
        int n = 89;
        return (String)this.k();
    }

    public TextSetting a(Supplier<Boolean> supplier) {
        return (TextSetting)this.visibleWhen(supplier);
    }

    @Override
    public void a(String string) {
        this.set(string);
    }

    public String a() {
        return this.get();
    }

    public void set(String string) {
        super.a(string);
    }
}

