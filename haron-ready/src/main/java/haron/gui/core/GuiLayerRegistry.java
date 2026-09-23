package haron.gui.core;

import haron.core.BooleanCoercion;
import haron.gui.core.GuiLayer;
import haron.gui.core.GuiHitRegion;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class GuiLayerRegistry {
    private static final GuiLayerRegistry c = new GuiLayerRegistry();
    private final Map<GuiLayer, List<GuiHitRegion>> d = new EnumMap<GuiLayer, List<GuiHitRegion>>(GuiLayer.class);
    private GuiLayer e = null;
    private int f = 0;
    private int g = 0;
    public static int a;
    public static boolean b;

    private GuiLayerRegistry() {
        for (GuiLayer v0ahvd2 : GuiLayer.values()) {
            this.d.put(v0ahvd2, new ArrayList());
        }
    }

    public boolean b(GuiLayer v0ahvd2) {
        if (this.e == null) {
            return true;
        }
        return BooleanCoercion.from(v0ahvd2.a() < this.e.a() ? 0 : 1);
    }

    public boolean b(GuiLayer v0ahvd2, double d, double d2) {
        Iterator<GuiHitRegion> iterator = this.d.get((Object)v0ahvd2).iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().a(d, d2)) continue;
            return true;
        }
        return false;
    }

    public void b() {
        Iterator<List<GuiHitRegion>> iterator = this.d.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().clear();
        }
        this.e = null;
    }

    public GuiLayer c() {
        return this.e;
    }

    public boolean c(GuiLayer v0ahvd2) {
        return BooleanCoercion.from(this.d.get((Object)v0ahvd2).isEmpty() ? 0 : 1);
    }

    public boolean c(GuiLayer v0ahvd2, double d, double d2) {
        for (GuiLayer v0ahvd3 : GuiLayer.values()) {
            if (v0ahvd3.a() < v0ahvd2.a() || !this.b(v0ahvd3, d, d2)) continue;
            return true;
        }
        return false;
    }

    private void d() {
        this.e = null;
        int n = -1;
        for (GuiLayer v0ahvd2 : GuiLayer.values()) {
            Iterator<GuiHitRegion> iterator = this.d.get((Object)v0ahvd2).iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().a(this.f, this.g) || v0ahvd2.a() <= n) continue;
                n = v0ahvd2.a();
                this.e = v0ahvd2;
            }
        }
    }

    public boolean d(GuiLayer v0ahvd2) {
        for (GuiLayer v0ahvd3 : GuiLayer.values()) {
            if (v0ahvd3.a() <= v0ahvd2.a() || this.d.get((Object)v0ahvd3).isEmpty()) continue;
            return true;
        }
        return false;
    }

    public boolean d(GuiLayer v0ahvd2, double d, double d2) {
        GuiLayer v0ahvd3 = this.a(d, d2);
        if (v0ahvd3 == null) {
            return false;
        }
        return BooleanCoercion.from(v0ahvd3.a() <= v0ahvd2.a() ? 0 : 1);
    }

    public boolean a(GuiLayer v0ahvd2, double d, double d2) {
        GuiLayer v0ahvd3 = this.a(d, d2);
        if (v0ahvd3 == null) {
            return true;
        }
        int n = v0ahvd2.a() < v0ahvd3.a() ? 0 : 1;
        return BooleanCoercion.from(n);
    }

    public void a(int n, int n2) {
        this.f = n;
        this.g = n2;
        this.d();
    }

    public void a(GuiLayer v0ahvd2, float f, float f2, float f3, float f4) {
        this.a(v0ahvd2, f, f2, f3, f4, 0.0f);
    }

    public void a(GuiLayer v0ahvd2, float f, float f2, float f3, float f4, float f5) {
        this.d.get((Object)v0ahvd2).add(new GuiHitRegion(f, f2, f3, f4, f5));
        this.d();
    }

    public void a(GuiLayer v0ahvd2) {
        this.d.get((Object)v0ahvd2).clear();
        this.d();
    }

    public static GuiLayerRegistry a() {
        return c;
    }

    public GuiLayer a(double d, double d2) {
        GuiLayer v0ahvd2 = null;
        int n = -1;
        for (GuiLayer v0ahvd3 : GuiLayer.values()) {
            Iterator<GuiHitRegion> iterator = this.d.get((Object)v0ahvd3).iterator();
            while (iterator.hasNext()) {
                if (!iterator.next().a(d, d2) || v0ahvd3.a() <= n) continue;
                n = v0ahvd3.a();
                v0ahvd2 = v0ahvd3;
            }
        }
        return v0ahvd2;
    }
}

