package haron.gui.modules;

import haron.client.MinecraftClientAccess;
import haron.gui.core.ClickGuiOverlay;
import haron.gui.core.ToggleEntryProvider;
import haron.gui.core.GuiInput;
import haron.gui.core.ToggleableEntry;
import haron.gui.core.GuiLayerRegistry;
import haron.gui.core.GuiLayer;
import haron.gui.core.ClickGuiScreen;
import haron.gui.modules.ModuleCardManager;
import haron.gui.modules.ModuleCard;
import haron.gui.modules.ToggleEntryRenderer;
import haron.gui.widgets.ScrollBar;
import haron.module.HaronModule;
import haron.render.ShapeRenderer;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;

public class ToggleEntryListOverlay
implements ClickGuiOverlay {
    private final ToggleEntryProvider a;
    private static final float b = 19.0f;
    private static final float c = 48.0f;
    private static final float d = 26.0f;
    private static final float e = 6.5f;
    private static final float f = 8.0f;
    private static final float g = 2.0f;
    private static final float LEFT_PAD = 6.0f;
    private static final float RIGHT_PAD = 19.0f;
    private float k;
    private float l;
    private float m;
    private float n;
    private int o;
    private int p;
    private boolean q = false;
    private final ScrollBar h = new ScrollBar(6.0f, 25.0f);
    private final ModuleCardManager i = new ModuleCardManager();
    private final ToggleEntryRenderer j = new ToggleEntryRenderer();

    public boolean hasOpenSettingsAt(int n, int n2) {
        return this.i.hasOpenPanelAt(n, n2);
    }

    public void closeOpenSettings() {
        this.i.f();
    }

    public boolean hasOpenSettings() {
        return this.i.c();
    }

    public ModuleCard findCardByBounds(int n, int n2) {
        return this.i.findCardByBounds(n, n2);
    }

    public ToggleEntryListOverlay(ToggleEntryProvider j4t5m02) {
        this.a = j4t5m02;
    }

    public void e() {
        this.i.i();
    }

    public void i() {
        this.i.k();
    }

    @Override
    public void b(float f, float f2, int n, int n2) {
        List<? extends ToggleableEntry> list;
        boolean bl;
        this.h.a();
        boolean bl2 = bl = (MinecraftClientAccess.c.currentScreen instanceof ClickGuiScreen ? ((ClickGuiScreen)MinecraftClientAccess.c.currentScreen).clickBtn : 0) == 1;
        if (this.i.hasOpenPanelAt(n, n2)) {
            if (bl) {
                this.i.f();
                return;
            }
            this.i.c(n, n2);
            return;
        }
        float f3 = f2 + 48.0f;
        float f4 = ClickGuiScreen.e() - 48.0f - 5.0f;
        if ((float)n2 < f3 || (float)n2 > f3 + f4 || (list = this.a.e()) == null || list.isEmpty()) {
            return;
        }
        int n3 = this.a.f();
        float f5 = this.k();
        float f6 = f + 6.0f;
        float f7 = f3 - this.h.b();
        for (int i = 0; i < list.size(); ++i) {
            ToggleableEntry mt5wd72 = list.get(i);
            int n4 = i % n3;
            float f8 = f6 + (float)n4 * (f5 + 6.5f) + 1.0f;
            float f9 = f7 + (float)(i / n3) * 34.0f + 1.0f;
            if (!GuiInput.a(f8, f9, f5, 26.0f, (double)n, (double)n2)) continue;
            float f10 = f8 + f5 - ToggleEntryRenderer.b() - ToggleEntryRenderer.a();
            float f11 = f9 + 13.0f - ToggleEntryRenderer.b() / 2.0f;
            float f12 = f10 - ToggleEntryRenderer.d() - ToggleEntryRenderer.c();
            float f13 = f9 + 13.0f - ToggleEntryRenderer.c() / 2.0f;
            if (mt5wd72.d() && mt5wd72.g() && GuiInput.a(f12, f13, ToggleEntryRenderer.c(), ToggleEntryRenderer.c(), (double)n, (double)n2)) {
                this.i.a(mt5wd72, i, n4 != 0, f8, f9, f5, 26.0f, f12, f13);
                return;
            }
            if (!bl) {
                mt5wd72.a(!mt5wd72.b());
                return;
            }
            if (mt5wd72.g()) {
                this.i.a(mt5wd72, i, n4 != 0, f8, f9);
                return;
            }
            return;
        }
        if (bl) {
            float f14 = this.k();
            float f15 = f + 6.0f;
            float f16 = f2 + 48.0f;
            for (int i = 0; i < list.size(); ++i) {
                ToggleableEntry mt5wd73 = list.get(i);
                int n5 = i % n3;
                float f17 = f15 + (float)n5 * (f14 + 6.5f) + 1.0f;
                float f18 = f16 + (float)(i / n3) * 34.0f + 1.0f;
                if (!GuiInput.a(f17, f18, f14, 26.0f, (double)n, (double)n2)) continue;
                if (mt5wd73.g()) {
                    this.i.a(mt5wd73, i, n5 != 0, f17, f18);
                    return;
                }
                return;
            }
        }
    }

    public void b() {
        this.i.b();
    }

    public ModuleCard b(int n, int n2) {
        return this.i.a(n, n2);
    }

    public void c(float f, float f2, int n, int n2) {
        this.h.d();
        this.i.e(n, n2);
    }

    public boolean c() {
        return this.i.l();
    }

    private float c(int n, int n2) {
        return (float)((int)Math.ceil((double)n / (double)n2)) * 34.0f - 8.0f;
    }

    public void h() {
        this.i.h();
    }

    public void f() {
        this.i.j();
    }

    public void d() {
        this.h.e();
        this.i.i();
    }

    public void a(HaronModule jxs16t2) {
        this.i.a(jxs16t2);
    }

    public boolean a(int n, int n2) {
        return this.i.b(n, n2);
    }

    private boolean a(List<? extends ToggleableEntry> list, float f, float f2, float f3, int n, int n2) {
        if ((float)n2 < f2 || (float)n2 > f2 + f3) {
            return false;
        }
        int n3 = this.a.f();
        float f4 = this.k();
        float f5 = f2 - this.h.b();
        float f6 = f + 6.0f;
        for (int i = 0; i < list.size(); ++i) {
            float f7 = f6 + (float)(i % n3) * (f4 + 6.5f) + 1.0f;
            float f8 = f5 + (float)(i / n3) * 34.0f + 1.0f;
            if (!GuiInput.a(f7, f8, f4, 26.0f, (double)n, (double)n2)) continue;
            return true;
        }
        return false;
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        this.i.b(matrixStack, s7swsm2, this.k, this.l, n, n2, this.m, this.n);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2) {
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
        this.b(f, f2, n, n2);
    }

    private void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, List<? extends ToggleableEntry> list, float f, float f2, float f3, int n, int n2) {
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.k = f;
        this.l = f2;
        this.o = n;
        this.p = n2;
        this.h.a();
        List<? extends ToggleableEntry> list = this.a.e();
        if (list == null || list.isEmpty()) {
            return;
        }
        int n3 = this.a.f();
        float f3 = this.k();
        float f4 = f2 + 48.0f;
        float f5 = ClickGuiScreen.e() - 48.0f - 19.0f;
        float f6 = (float)((int)Math.ceil((double)list.size() / (double)n3)) * 34.0f - 8.0f;
        if (f6 < 0.0f) {
            f6 = 0.0f;
        }
        float f7 = Math.max(0.0f, f6 - f5);
        float f8 = this.h.b();
        if (f8 > f7) {
            this.h.e(f7);
            f8 = f7;
        }
        float f9 = f4 - f8;
        float f10 = f + 6.0f;
        float f11 = f4 + f5;
        GuiLayerRegistry.a().a(GuiLayer.CONTENT, f, f4, ClickGuiScreen.d(), f5);
        s7swsm2.b().a(f, f4, ClickGuiScreen.d(), f5, matrixStack);
        for (int i = 0; i < list.size(); ++i) {
            ToggleableEntry mt5wd72 = list.get(i);
            int n4 = i % n3;
            float f12 = f10 + (float)n4 * (f3 + 6.5f) + 1.0f;
            float f13 = f9 + (float)(i / n3) * 34.0f + 1.0f;
            if (!(f13 + 26.0f >= f4) || !(f13 <= f11)) continue;
            this.j.a(matrixStack, s7swsm2, mt5wd72, f12, f13, f3, 26.0f, n, n2, false, false, false, false);
        }
        s7swsm2.b().a(matrixStack);
        if (f6 > f5 + 1.0f) {
            this.h.a(matrixStack, s7swsm2, f + ClickGuiScreen.d() - 2.0f, f4, f5, f6, f5, n, n2, false);
        }
        this.i.b(matrixStack, s7swsm2, this.k, this.l, n, n2, this.m, this.n);
    }

    public List<ModuleCard> a() {
        return this.i.a();
    }

    public boolean a(int n, int n2, int n3) {
        return this.i.a(n, n2, n3);
    }

    public boolean a(char c, int n) {
        return this.i.a(c, n);
    }

    public void a(float f) {
        try {
            if (this.i.hasOpenPanelAt(this.o, this.p) && this.i.a(f, this.o, this.p)) {
                return;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        try {
            if (this.h.c()) {
                return;
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        List<? extends ToggleableEntry> list = this.a.e();
        if (list == null || list.isEmpty()) {
            return;
        }
        int n = this.a.f();
        this.h.a(f, this.c(list.size(), n), ClickGuiScreen.e() - 48.0f - 19.0f);
    }

    public void a(float f, float f2, int n, int n2, double d, double d2) {
        List<? extends ToggleableEntry> list;
        this.i.a(n, n2, d, d2);
        if (this.h.c() && (list = this.a.e()) != null) {
            this.h.a(n2, this.c(list.size(), this.a.f()), ClickGuiScreen.e() - 48.0f - 18.0f);
        }
    }

    public void a(float f, int n, int n2) {
        this.o = n;
        this.p = n2;
        this.a(f);
    }

    public boolean p() {
        return this.i.l();
    }

    private float k() {
        int n = this.a.f();
        float f = ClickGuiScreen.d() - 6.0f - 19.0f;
        return n == 1 ? f - 2.0f : (f - 6.5f) / 2.0f - 1.0f;
    }

    public void g() {
        this.i.g();
    }

    public boolean j() {
        return this.i.c();
    }
}

