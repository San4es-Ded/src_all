package haron.gui.core;

import haron.module.HaronModule;

public interface ToggleableEntry {
    default public HaronModule e() {
        return null;
    }

    public boolean b();

    default public boolean c() {
        return true;
    }

    default public boolean h() {
        return this.d();
    }

    default public int f() {
        HaronModule jxs16t2 = this.e();
        if (jxs16t2 != null) {
            return jxs16t2.j();
        }
        return -1;
    }

    default public boolean d() {
        return false;
    }

    public String a();

    default public void a(int n) {
        HaronModule jxs16t2 = this.e();
        if (jxs16t2 != null) {
            jxs16t2.a(n);
        }
    }

    public void a(boolean var1);

    default public boolean g() {
        HaronModule jxs16t2 = this.e();
        return jxs16t2 != null && !jxs16t2.m().isEmpty();
    }
}

