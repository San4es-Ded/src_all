package haron.gui.markers;

import haron.gui.core.ToggleableEntry;
import java.util.function.Consumer;
import java.util.function.Supplier;

class MarkerToggleSettingRow
implements ToggleableEntry {
    private final String c;
    private final Supplier<Boolean> d;
    private final Consumer<Boolean> e;
    public static int a;
    public static boolean b;

    public MarkerToggleSettingRow(String string, Supplier<Boolean> supplier, Consumer<Boolean> consumer) {
        this.c = string;
        this.d = supplier;
        this.e = consumer;
    }

    @Override
    public boolean b() {
        return this.d.get();
    }

    @Override
    public boolean d() {
        return false;
    }

    @Override
    public String a() {
        return this.c;
    }

    @Override
    public void a(boolean bl) {
        this.e.accept(bl);
    }
}

