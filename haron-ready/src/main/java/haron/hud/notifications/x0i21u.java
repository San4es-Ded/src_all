package haron.hud.notifications;

import haron.hud.notifications.uqw6p7;
import haron.hud.notifications.wo1nxr;
import java.awt.Color;
import java.util.function.Supplier;

public class x0i21u {
    final String b;
    String c;
    String d;
    Runnable e;
    Color f;
    Color g;
    Supplier<Boolean> i;
    wo1nxr a = wo1nxr.BELL;
    long h = 5000L;
    boolean j = false;

    public x0i21u(String string) {
        this.b = string;
    }

    public uqw6p7 b() {
        return null;
    }

    public x0i21u b(Color color) {
        this.g = color;
        return this;
    }

    public x0i21u a(Supplier<Boolean> supplier) {
        this.i = supplier;
        return this;
    }

    public x0i21u a() {
        this.j = true;
        return this;
    }

    public x0i21u a(wo1nxr wo1nxr2) {
        this.a = wo1nxr2;
        return this;
    }

    public x0i21u a(String string, Runnable runnable) {
        this.d = string;
        this.e = runnable;
        return this;
    }

    public x0i21u a(Color color) {
        this.f = color;
        return this;
    }

    public x0i21u a(String string) {
        this.c = string;
        return this;
    }

    public x0i21u a(long l) {
        int n = 824;
        this.h = l;
        return this;
    }
}
