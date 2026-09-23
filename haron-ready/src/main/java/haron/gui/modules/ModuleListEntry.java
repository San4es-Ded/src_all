package haron.gui.modules;

import haron.gui.core.ToggleableEntry;
import haron.module.ModuleCategory;
import haron.module.HaronModule;

public class ModuleListEntry
implements ToggleableEntry {
    private final HaronModule a;

    public ModuleListEntry(HaronModule jxs16t2) {
        this.a = jxs16t2;
    }

    @Override
    public HaronModule e() {
        return this.a;
    }

    public ModuleCategory i() {
        return this.a.i();
    }

    @Override
    public boolean b() {
        return this.a.k();
    }

    @Override
    public boolean d() {
        return this.g();
    }

    @Override
    public void a(boolean bl) {
        this.a.b(bl);
    }

    @Override
    public String a() {
        return this.a.g();
    }
}

