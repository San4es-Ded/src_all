package haron.gui.notifications;

import haron.animation.Easings;
import haron.animation.AnimatedValue;
import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.gui.notifications.xrnge1;
import haron.gui.widgets.ScrollBar;
import haron.gui.widgets.SearchBox;
import haron.hud.core.HudServices;
import haron.hud.notifications.eq8z6w;
import haron.hud.notifications.gv8nup;
import haron.hud.notifications.oo89jz;
import haron.markers.AutoEventMarkerTracker;
import haron.markers.MarkerIcon;
import haron.markers.MarkerRegistry;
import haron.markers.Waypoint;
import haron.render.ShapeRenderer;
import haron.theme.pryrvd;
import haron.util.ColorUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;

public class u5dyg4 {
    public static final float a = 115.5f;
    private static final float b = 115.5f;
    private static final float c = 134.5f;
    private static final float d = 7.5f;
    private static final float e = 5.0f;
    private static final float f = 10.0f;
    private static final float g = 4.0f;
    private static final float h = 19.5f;
    private static final float i = 5.0f;
    private static final float j = 19.5f;
    private static final float k = 6.5f;
    private static final float l = 5.0f;
    private static final float m = 2.0f;
    private static final Color n = pryrvd.I;
    private static final Color o = pryrvd.K;
    private static final Color p = pryrvd.O;
    private final ScrollBar u;
    private Consumer<eq8z6w> A;
    private Consumer<eq8z6w> B;
    private float C;
    private float D;
    private float E;
    private float F;
    private float G;
    private final List<eq8z6w> q = new ArrayList<eq8z6w>();
    private final List<xrnge1> r = new ArrayList<xrnge1>();
    private eq8z6w s = null;
    private final AnimatedValue w = new AnimatedValue();
    private boolean x = false;
    private String y = "";
    private List<xrnge1> z = new ArrayList<xrnge1>();
    private final SearchBox t = new SearchBox(91.0f, 19.5f);

    private static MarkerIcon mapMarkerIconOf(oo89jz oo89jz2) {
        if (oo89jz2 == null) {
            return MarkerIcon.EVENT;
        }
        try {
            return MarkerIcon.valueOf(oo89jz2.name());
        }
        catch (IllegalArgumentException illegalArgumentException) {
            return MarkerIcon.EVENT;
        }
    }

    public u5dyg4() {
        this.t.a(this::a);
        this.u = new ScrollBar(2.0f, 20.0f);
        this.u.b(10.0f);
        this.u.a(pryrvd.b);
        this.u.b(pryrvd.d);
    }

    public void e() {
        List<eq8z6w> list = gv8nup.a();
        for (eq8z6w object : list) {
            boolean bl = false;
            Iterator<eq8z6w> iterator = this.q.iterator();
            while (iterator.hasNext()) {
                if (iterator.next() != object) continue;
                bl = true;
                break;
            }
            if (bl) continue;
            this.q.add(object);
            this.r.add(new xrnge1(object));
        }
        this.q.removeIf(eq8z6w2 -> {
            boolean bl = list.contains(eq8z6w2);
            if (!bl) {
                this.r.removeIf(xrnge12 -> {
                    return BooleanCoercion.from(xrnge12.a() == eq8z6w2 ? 1 : 0);
                });
                if (this.s == eq8z6w2) {
                    this.s = null;
                }
            }
            return BooleanCoercion.from(!bl ? 1 : 0);
        });
        this.i();
        if (this.s != null) {
            boolean bl = false;
            Iterator<xrnge1> iterator = this.z.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().a() != this.s) continue;
                bl = true;
                break;
            }
            if (bl) {
                return;
            }
            this.s = null;
            if (this.A != null) {
                this.A.accept(null);
            }
        }
    }

    private void i() {
        List<xrnge1> arrayList = this.y.isEmpty() ? new ArrayList<xrnge1>(this.r) : this.r.stream().filter(xrnge12 -> {
            return xrnge12.a().a().toLowerCase().contains(this.y);
        }).collect(Collectors.toList());
        List<xrnge1> list = arrayList.stream().filter(xrnge12 -> {
            return this.d(xrnge12.a());
        }).collect(Collectors.toList());
        list.sort((xrnge12, xrnge13) -> {
            boolean bl = xrnge12.a().j();
            if (bl != xrnge13.a().j()) {
                return bl ? -1 : 1;
            }
            return Long.compare(xrnge12.a().k(), xrnge13.a().k());
        });
        this.z = list;
    }

    public void b(Consumer<eq8z6w> consumer) {
        this.B = consumer;
    }

    public eq8z6w b(eq8z6w eq8z6w2) {
        int n = this.q.indexOf(eq8z6w2);
        if (n < 0) {
            return null;
        }
        this.q.remove(n);
        this.r.remove(n);
        gv8nup.b(eq8z6w2);
        Waypoint yo0tnu2 = MarkerRegistry.a(eq8z6w2.b(), eq8z6w2.c(), eq8z6w2.d());
        if (yo0tnu2 != null) {
            MarkerRegistry.b(yo0tnu2);
        }
        eq8z6w eq8z6w3 = null;
        if (this.s == eq8z6w2) {
            this.s = null;
            if (!this.q.isEmpty()) {
                eq8z6w3 = n > 0 ? this.q.get(n - 1) : this.q.get(0);
            }
        }
        this.i();
        float f = this.G - 10.0f;
        float f2 = this.j();
        if (f2 <= f) {
            this.u.e();
        } else {
            this.u.b(f2, f);
        }
        return eq8z6w3;
    }

    public eq8z6w b() {
        return this.s;
    }

    public void c(eq8z6w eq8z6w2) {
        this.s = eq8z6w2;
        for (xrnge1 xrnge12 : this.r) {
            xrnge12.a(BooleanCoercion.from(xrnge12.a() == eq8z6w2 ? 1 : 0));
        }
    }

    public eq8z6w c() {
        if (this.q.isEmpty()) {
            return null;
        }
        return this.q.get(0);
    }

    public static float h() {
        int n = 472;
        return 38.5f;
    }

    public boolean f() {
        return this.t.c();
    }

    public eq8z6w d() {
        return this.s == null || !this.q.contains(this.s) ? this.c() : this.s;
    }

    private boolean d(eq8z6w eq8z6w2) {
        if (eq8z6w2.j()) {
            String string;
            AutoEventMarkerTracker g2mprb2 = HudServices.MAP_MARKER_MODULE;
            int n = g2mprb2.h();
            String string2 = g2mprb2.i();
            MinecraftClient minecraftClient = MinecraftClient.getInstance();
            String string3 = string = minecraftClient.getCurrentServerEntry() != null ? minecraftClient.getCurrentServerEntry().address.toLowerCase() : "";
            if (string.contains("crypt")) {
                if (eq8z6w2.l() != n) {
                    return false;
                }
                if (eq8z6w2.m() != null && string2 != null && !eq8z6w2.m().equals(string2)) {
                    return false;
                }
            } else if (!string.contains("crypt") && eq8z6w2.l() != n) {
                return false;
            }
            return true;
        }
        return true;
    }

    public boolean a(char c, int n) {
        return this.t.c() ? this.t.a(c, n) : false;
    }

    public boolean a(float f, float f2, float f3, int n, int n2) {
        if (this.t.a(n, n2)) {
            return true;
        }
        float f4 = f2 + 46.5f + 3.0f;
        if (GuiInput.a(f + 91.0f + 5.0f, f4 - 3.0f, 19.5f, 19.5f, (double)n, (double)n2)) {
            this.k();
            return true;
        }
        float f5 = f3 - 43.5f;
        float f6 = f5 * 0.8f;
        float f7 = f2 + 43.5f + (f5 - f6);
        float f8 = f7 + 5.0f;
        float f9 = f6 - 10.0f - 10.0f;
        float f10 = this.j();
        if (f10 > f9 && this.u.a(f + 115.5f - 2.0f + 1.0f, f8, f9, f10, f9, n, n2)) {
            return true;
        }
        float f11 = f + 5.0f;
        if (GuiInput.a(f11, f8, 101.5f, f9, (double)n, (double)n2)) {
            float f12 = f8 - this.u.b();
            for (xrnge1 xrnge12 : this.z) {
                if (xrnge12.a(f11, f12, 101.5f, n, n2)) {
                    if (xrnge12.c()) {
                        this.c(xrnge12.a());
                        if (this.A != null) {
                            this.A.accept(xrnge12.a());
                        }
                    }
                    return true;
                }
                f12 += 32.5f;
            }
        }
        return false;
    }

    public void a(eq8z6w eq8z6w2) {
        this.q.add(eq8z6w2);
        this.r.add(new xrnge1(eq8z6w2));
        gv8nup.a(eq8z6w2);
        this.i();
    }

    public List<eq8z6w> a() {
        return this.q;
    }

    public void a(Consumer<eq8z6w> consumer) {
        this.A = consumer;
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3) {
        int n3 = (int)(255.0f * f3);
        boolean bl = GuiInput.a(f, f2, 19.5f, 19.5f, (double)n, (double)n2);
        if (bl != this.x) {
            this.w.a(bl ? 1.0 : 0.0, 0.15, Easings.h);
            this.x = bl;
        }
        float f4 = (float)this.w.j();
        Color color = pryrvd.b(pryrvd.q, f3);
        Color color2 = pryrvd.b(pryrvd.n, f3);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, 20.5f, 20.5f, 6.5f, color, color, color2, color2, matrixStack);
        Color color3 = ColorUtils.a(pryrvd.CELL_BG_TOP, u5dyg4.n, f4);
        Color color4 = ColorUtils.a(pryrvd.CELL_BG_BOT, o, f4);
        Color color5 = pryrvd.a(color3, n3);
        Color color6 = pryrvd.a(color4, n3);
        s7swsm2.a(f, f2, 19.5f, 19.5f, 6.5f, color5, color5, color6, color6, matrixStack);
        Color color7 = pryrvd.a(ColorUtils.a(pryrvd.b, p, f4), n3);
        float f5 = f + 7.25f;
        float f6 = f2 + 7.25f;
        s7swsm2.a(f5, f6 + 2.5f - 0.75f, 5.0f, 1.5f, color7, matrixStack);
        s7swsm2.a(f5 + 2.5f - 0.75f, f6, 1.5f, 5.0f, color7, matrixStack);
        if (bl) {
            GuiInput.g();
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, int n, int n2, float f5) {
        boolean bl;
        float f6 = f + 5.0f;
        float f7 = f2 + 5.0f;
        float f8 = f3 - 10.0f;
        float f9 = f4 - 10.0f;
        float f10 = this.j();
        boolean bl2 = bl = f10 > f9;
        if (!bl) {
            this.u.e();
        }
        s7swsm2.b().a(f, f2 + 4.0f, f3, f4 - 8.0f, matrixStack);
        float f11 = f7 - this.u.b();
        for (xrnge1 xrnge12 : this.z) {
            if (f11 + 27.5f >= f7 && f11 <= f7 + f9) {
                xrnge12.a(matrixStack, s7swsm2, f6, f11, f8, n, n2, f5);
            }
            f11 += 32.5f;
        }
        s7swsm2.b().a(matrixStack);
        if (bl) {
            this.u.a(matrixStack, s7swsm2, f + f3 - 2.0f + 1.0f, f7, f9, f10, f9, n, n2, false);
        }
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, float f4, float f5) {
        Color color = pryrvd.b(pryrvd.m, f5);
        Color color2 = pryrvd.b(pryrvd.n, f5);
        s7swsm2.a(f - 0.5f, f2 - 0.5f, f3 + 1.0f, f4 + 1.0f, 7.5f, color, color, color2, color2, matrixStack);
        int n = (int)(255.0f * f5);
        Color color3 = pryrvd.a(pryrvd.CELL_BG_TOP, n);
        Color color4 = pryrvd.a(pryrvd.CELL_BG_BOT, n);
        s7swsm2.a(f, f2, f3, f4, 7.5f, color3, color3, color4, color4, matrixStack);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, float f3, int n, int n2, float f4) {
        this.C = f;
        this.D = f2;
        this.E = 115.5f;
        this.F = f3;
        this.G = f3;
        this.u.a();
        this.w.a();
        float f5 = f3 - 43.5f;
        float f6 = f5 * 0.8f;
        float f7 = f2 + 43.5f + (f5 - f6);
        this.a(matrixStack, s7swsm2, f, f7, 115.5f, f6, f4);
        this.t.a(matrixStack, s7swsm2, f, f2 + 46.5f, 91.0f, 19.5f, n, n2);
        this.a(matrixStack, s7swsm2, f + 91.0f + 5.0f, f2 + 46.5f, n, n2, f4);
        this.a(matrixStack, s7swsm2, f, f7, 115.5f, f6 - 10.0f, n, n2, f4);
    }

    private void a(String string) {
        this.y = string.toLowerCase().trim();
        this.i();
        this.u.e();
    }

    public boolean a(int n, int n2, int n3) {
        return this.t.c() ? this.t.a(n, n2, n3) : false;
    }

    public void a(float f, int n, int n2) {
        float f2 = this.D + 10.0f + 4.0f + 19.5f + 5.0f;
        float f3 = this.E - 38.5f;
        if (GuiInput.a(this.C, f2, 115.5f, f3, (double)n, (double)n2)) {
            this.u.a(f, this.j(), f3 - 10.0f);
        }
    }

    public void a(int n, int n2) {
        this.u.d();
    }

    public void a(int n, int n2, double d, double d2) {
        if (this.u.c()) {
            float f = this.G - 10.0f;
            this.u.a(n2, this.j(), f);
        }
    }

    private void k() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        int n = 0;
        int n2 = 64;
        int n3 = 0;
        if (minecraftClient.player != null) {
            n = (int)minecraftClient.player.getX();
            n2 = (int)minecraftClient.player.getY();
            n3 = (int)minecraftClient.player.getZ();
        }
        eq8z6w eq8z6w2 = new eq8z6w("Метка", n, n2, n3, pryrvd.y, oo89jz.HOME);
        this.a(eq8z6w2);
        this.c(eq8z6w2);
        if (MarkerRegistry.a(n, n2, n3) == null) {
            MarkerRegistry.a(new Waypoint(eq8z6w2.a(), n, n2, n3, eq8z6w2.e(), u5dyg4.mapMarkerIconOf(eq8z6w2.f())));
        }
        if (this.B != null) {
            this.B.accept(eq8z6w2);
        }
        if (this.A != null) {
            this.A.accept(eq8z6w2);
        }
    }

    public boolean g() {
        int n = 296;
        return this.q.isEmpty();
    }

    private float j() {
        if (this.z.isEmpty()) {
            return 0.0f;
        }
        return (float)this.z.size() * 32.5f - 5.0f;
    }
}
