package haron.hud.core;

import haron.core.BooleanCoercion;
import haron.gui.core.GuiInput;
import haron.hud.core.HudElement;
import haron.hud.core.HudPositionSnapshot;
import haron.hud.elements.HotkeysHudElement;
import haron.hud.elements.PotionsHudElement;
import haron.hud.elements.TargetHudElement;
import haron.hud.elements.CooldownsHudElement;
import haron.hud.notifications.NotificationHudManager;
import haron.hud.snap.SnapResult;
import haron.hud.snap.GuideOrientation;
import haron.hud.snap.HudSnapEngine;
import haron.hud.snap.SnapGuide;
import haron.module.ModuleManager;
import haron.render.ScaledGuiProjection;
import haron.render.ShapeRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.math.MatrixStack;
import org.lwjgl.glfw.GLFW;
import ru.haron.Haron;

public class HudManager {
    private static final int r = 50;
    private static final HudManager a = new HudManager();
    private static final Color m = new Color(100, 180, 255, 180);
    private static final Color n = new Color(200, 100, 255, 180);
    private static final Color o = new Color(255, 150, 80, 180);
    private final MinecraftClient b = MinecraftClient.getInstance();
    private final List<HudElement> c = new ArrayList<HudElement>();
    private boolean i = false;
    private HudElement j = null;
    private int k = 0;
    private List<SnapGuide> l = new ArrayList<SnapGuide>();
    private final Deque<HudPositionSnapshot> p = new LinkedList<HudPositionSnapshot>();
    private final Deque<HudPositionSnapshot> q = new LinkedList<HudPositionSnapshot>();
    private float s = -1.0f;
    private float t = -1.0f;
    private boolean u = false;
    private NotificationHudManager d = new NotificationHudManager();
    private PotionsHudElement e = new PotionsHudElement(10.0f, 10.0f);
    private HotkeysHudElement f = new HotkeysHudElement(10.0f, 60.0f);
    private CooldownsHudElement g = new CooldownsHudElement(10.0f, 110.0f);
    private TargetHudElement h = new TargetHudElement(10.0f, 160.0f);

    private double scaleMouseY(double d) {
        return d * this.b.getWindow().getScaleFactor() / 2.0;
    }

    private double scaleMouseX(double d) {
        return d * this.b.getWindow().getScaleFactor() / 2.0;
    }

    private HudManager() {
        this.c(this.e);
        this.c(this.f);
        this.c(this.g);
        this.c(this.h);
    }

    private boolean e(HudElement hylpge2) {
        return hylpge2 == this.e ? ModuleManager.POTIONS_HUD.k() : (hylpge2 == this.f ? ModuleManager.HOTKEYS_HUD.k() : (hylpge2 == this.g ? ModuleManager.COOLDOWNS_HUD.k() : (hylpge2 == this.h ? ModuleManager.TARGET_HUD.k() : hylpge2.c())));
    }

    public void e() {
    }

    public PotionsHudElement i() {
        int n = 12;
        return this.e;
    }

    public boolean b(double d, double d2) {
        if (this.j == null) {
            return false;
        }
        double d3 = d * this.b.getWindow().getScaleFactor() / 2.0;
        double d4 = d2 * this.b.getWindow().getScaleFactor() / 2.0;
        float f = (float)((double)this.b.getWindow().getFramebufferWidth() / 2.0);
        float f2 = (float)((double)this.b.getWindow().getFramebufferHeight() / 2.0);
        this.g();
        SnapResult comxfd2 = HudSnapEngine.a(this.j, (float)d3 - this.j.r(), (float)d4 - this.j.s(), f, f2, this.t(), this.u);
        this.j.a(comxfd2, f, f2);
        this.l = comxfd2.c();
        return true;
    }

    public boolean b(double d, double d2, int n) {
        if (!this.i) {
            return false;
        }
        double d3 = this.scaleMouseX(d);
        double d4 = this.scaleMouseY(d2);
        boolean bl = false;
        if (this.j != null) {
            this.j.b(d3, d4, n);
            this.j = null;
            this.l.clear();
            bl = true;
        }
        if (this.d != null && ModuleManager.WATERMARK.k()) {
            this.d.b(d3, d4, n);
        }
        return bl;
    }

    public void b(MatrixStack matrixStack) {
    }

    public void b() {
    }

    public void b(HudElement hylpge2) {
        this.c.remove(hylpge2);
    }

    private List<HudElement> s() {
        ArrayList<HudElement> arrayList = new ArrayList<HudElement>(this.c);
        arrayList.sort((hylpge2, hylpge3) -> {
            int n = 714;
            return Integer.compare(hylpge3.p(), hylpge2.p());
        });
        return arrayList;
    }

    private void c(HudElement hylpge2) {
        if (hylpge2 == null || this.c.contains(hylpge2)) {
            return;
        }
        this.c.add(hylpge2);
        this.r();
    }

    public void c() {
    }

    public void n() {
        int n = 591;
        this.a(BooleanCoercion.from(!this.i ? 1 : 0));
    }

    public NotificationHudManager h() {
        return this.d;
    }

    private void f(HudElement hylpge2) {
        this.p.push(new HudPositionSnapshot(hylpge2));
        if (this.p.size() > 50) {
            this.p.removeLast();
        }
        this.q.clear();
    }

    public void f() {
    }

    public TargetHudElement l() {
        return this.h;
    }

    public void d() {
    }

    private void d(HudElement hylpge2) {
        this.c.remove(hylpge2);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2) {
        for (SnapGuide t0jzfe2 : this.l) {
            Color color = switch (t0jzfe2.b()) {
                case SCREEN_CENTER -> n;
                case ELEMENT_EDGE, ELEMENT_CENTER -> o;
                default -> m;
            };
            if (t0jzfe2.a() == GuideOrientation.VERTICAL) {
                s7swsm2.a(t0jzfe2.c() - 0.5f, t0jzfe2.d(), 1.0f, t0jzfe2.e() - t0jzfe2.d(), 0.0f, color, matrixStack);
                continue;
            }
            s7swsm2.a(t0jzfe2.d(), t0jzfe2.c() - 0.5f, t0jzfe2.e() - t0jzfe2.d(), 1.0f, 0.0f, color, matrixStack);
        }
    }

    public boolean a(double d, double d2, double d3) {
        double d4 = d * this.b.getWindow().getScaleFactor() / 2.0;
        double d5 = d2 * this.b.getWindow().getScaleFactor() / 2.0;
        if (this.j == null) {
            for (HudElement hylpge2 : this.c) {
                if (!this.e(hylpge2) || !hylpge2.f().e() || !hylpge2.a(d4, d5, d3)) continue;
                return true;
            }
        }
        return this.d != null && ModuleManager.WATERMARK.k() && this.d.a(d4, d5, d3);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2) {
    }

    public void a(boolean bl) {
        if (this.i == bl) {
            return;
        }
        this.i = bl;
        if (bl) {
            return;
        }
        double d = this.scaleMouseX(this.b.mouse.getX());
        double d2 = this.scaleMouseY(this.b.mouse.getY());
        if (this.j != null) {
            this.j.b(d, d2, 0);
            this.j = null;
        }
        this.l.clear();
        Iterator<HudElement> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().a(0.0, 0.0, false);
        }
        if (this.d != null) {
            this.d.g();
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void a(MatrixStack matrixStack) {
        ShapeRenderer s7swsm2;
        if (this.b.world == null || this.b.player == null || (s7swsm2 = Haron.getInstance().getRender()) == null) {
            return;
        }
        ScaledGuiProjection.a(2.0);
        try {
            float f = (float)((double)this.b.getWindow().getFramebufferWidth() / 2.0);
            float f2 = (float)((double)this.b.getWindow().getFramebufferHeight() / 2.0);
            if (this.s > 0.0f && this.t > 0.0f && (Math.abs(f - this.s) > 1.0f || Math.abs(f2 - this.t) > 1.0f)) {
                this.a(f, f2);
            }
            this.s = f;
            this.t = f2;
            if (this.d != null && ModuleManager.WATERMARK.k()) {
                this.d.a(matrixStack, s7swsm2);
            }
            for (HudElement hylpge2 : this.c) {
                if (!this.e(hylpge2)) continue;
                hylpge2.e();
                hylpge2.a(matrixStack, s7swsm2, f, f2);
                hylpge2.a(s7swsm2, matrixStack);
            }
            if (this.j != null && !this.l.isEmpty()) {
                this.a(matrixStack, s7swsm2);
            }
            if (this.j != null && !this.u) {
                this.a(matrixStack, s7swsm2, f, f2);
            }
            int n = (int)(this.b.mouse.getX() * this.b.getWindow().getScaleFactor() / 2.0 / this.b.getWindow().getScaleFactor());
            int n2 = (int)(this.b.mouse.getY() * this.b.getWindow().getScaleFactor() / 2.0 / this.b.getWindow().getScaleFactor());
            int n3 = (int)((double)n * this.b.getWindow().getScaleFactor() / 2.0);
            int n4 = (int)((double)n2 * this.b.getWindow().getScaleFactor() / 2.0);
            for (HudElement hylpge3 : this.c) {
                if (!this.e(hylpge3)) continue;
                hylpge3.a(matrixStack, s7swsm2, n3, n4);
            }
        }
        finally {
            ScaledGuiProjection.a();
        }
    }

    public void a(HudElement hylpge2) {
        this.c(hylpge2);
    }

    public void a(double d, double d2, double d3, double d4) {
        double d5 = d * this.b.getWindow().getScaleFactor() / 2.0;
        double d6 = d2 * this.b.getWindow().getScaleFactor() / 2.0;
        if (this.j == null) {
            for (HudElement hylpge2 : this.c) {
                if (!this.e(hylpge2) || !hylpge2.f().e()) continue;
                hylpge2.a(d5, d6, d3, d4);
            }
        }
        if (this.d != null && ModuleManager.WATERMARK.k()) {
            this.d.a(d5, d6, d3, d4);
        }
        if (this.j != null) {
            this.g();
            float f = (float)((double)this.b.getWindow().getFramebufferWidth() / 2.0);
            float f2 = (float)((double)this.b.getWindow().getFramebufferHeight() / 2.0);
            SnapResult comxfd2 = HudSnapEngine.a(this.j, (float)d5 - this.j.r(), (float)d6 - this.j.s(), f, f2, this.t(), this.u);
            this.j.a(comxfd2, f, f2);
            this.l = comxfd2.c();
        }
    }

    public boolean a(int n, int n2) {
        return false;
    }

    public static HudManager a() {
        return a;
    }

    private void a(float f, float f2) {
        Iterator<HudElement> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().a(f, f2);
        }
    }

    public boolean a(double d, double d2, int n) {
        if (!this.i || !this.b.isWindowFocused()) {
            return false;
        }
        double d3 = this.scaleMouseX(d);
        double d4 = this.scaleMouseY(d2);
        if (this.d != null && ModuleManager.WATERMARK.k() && this.d.a(d3, d4, n)) {
            return true;
        }
        for (HudElement hylpge2 : this.s()) {
            if (!this.e(hylpge2) || !hylpge2.a(d3, d4, n)) continue;
            if (n != 0 || !hylpge2.i()) {
                return true;
            }
            this.j = hylpge2;
            this.f(hylpge2);
            this.l.clear();
            return true;
        }
        return false;
    }

    /*
     * WARNING - void declaration
     */
    public void a(double d, double d2) {
        if (this.i) {
            HudElement hovered = null;
            this.g();
            if (!this.b.isWindowFocused()) {
                for (HudElement hylpge2 : this.c) {
                    if (!this.e(hylpge2)) continue;
                    hylpge2.a(0.0, 0.0, false);
                }
                return;
            }
            double d3 = d * this.b.getWindow().getScaleFactor() / 2.0;
            double d4 = d2 * this.b.getWindow().getScaleFactor() / 2.0;
            if (this.j != null) {
                for (HudElement hylpge3 : this.c) {
                    if (!this.e(hylpge3)) continue;
                    hylpge3.a(d3, d4, BooleanCoercion.from(hylpge3 == this.j ? 1 : 0));
                }
                GuiInput.g();
                return;
            }
            boolean bl = false;
            for (HudElement object2 : this.c) {
                if (!this.e(object2) || !object2.f().c() || !object2.f().c((int)d3, (int)d4)) continue;
                bl = true;
                break;
            }
            if (bl) {
                for (HudElement hylpge2 : this.c) {
                    if (!this.e(hylpge2)) continue;
                    hylpge2.a(d3, d4, false);
                }
                return;
            }
            for (HudElement hylpge3 : this.s()) {
                if (!this.e(hylpge3) || !hylpge3.a(d3, d4)) continue;
                hovered = hylpge3;
                break;
            }
            for (HudElement hylpge5 : this.c) {
                if (!this.e(hylpge5)) continue;
                hylpge5.a(d3, d4, BooleanCoercion.from(hylpge5 == hovered ? 1 : 0));
            }
            if (hovered != null) {
                GuiInput.g();
            }
        }
    }

    public boolean m() {
        return this.i;
    }

    public List<HudElement> o() {
        return this.c;
    }

    public boolean p() {
        return BooleanCoercion.from(this.j != null ? 1 : 0);
    }

    public CooldownsHudElement k() {
        return this.g;
    }

    private List<HudElement> t() {
        return this.c.stream().filter(this::e).collect(Collectors.toList());
    }

    public void g() {
        long l = this.b.getWindow().getHandle();
        this.u = BooleanCoercion.from(GLFW.glfwGetKey((long)l, (int)342) == 1 || GLFW.glfwGetKey((long)l, (int)346) == 1 ? 1 : 0);
    }

    public HotkeysHudElement j() {
        return this.f;
    }

    public boolean q() {
        return this.u;
    }

    private void r() {
        int n = 537;
        this.c.sort(Comparator.comparingInt(hylpge2 -> {
            return hylpge2.p();
        }));
    }
}
