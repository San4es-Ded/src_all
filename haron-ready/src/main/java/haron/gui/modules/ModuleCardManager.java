package haron.gui.modules;

import haron.client.MinecraftClientAccess;
import haron.core.BooleanCoercion;
import haron.gui.core.InteractionOverlayController;
import haron.gui.core.GuiInput;
import haron.gui.core.ToggleableEntry;
import haron.gui.core.ClickGuiScreen;
import haron.gui.modules.ModuleListEntry;
import haron.gui.modules.ModuleCard;
import haron.gui.modules.VerticalPlacement;
import haron.module.HaronModule;
import haron.render.icons.IconTexture;
import haron.render.icons.HaronIcons;
import haron.render.ShapeRenderer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.client.util.math.MatrixStack;

public class ModuleCardManager {
    private final List<ModuleCard> c = new ArrayList<ModuleCard>();
    private float d;
    private float e;
    public static int a;
    public static boolean b;

    public boolean hasOpenPanelAt(int n, int n2) {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.d(n, n2) && !sudbet2.f(n, n2)) continue;
            return true;
        }
        return false;
    }

    public ModuleCard findCardByBounds(int n, int n2) {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.e(n, n2)) continue;
            return sudbet2;
        }
        return null;
    }

    public ToggleableEntry e() {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.g()) continue;
            return sudbet2.l();
        }
        if (!b) {
            return null;
        }
        return null;
    }

    public void e(int n, int n2) {
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().c(n, n2);
        }
    }

    public void i() {
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().b();
        }
    }

    public void b() {
        boolean bl = false;
        int n = 0;
        for (ModuleCard object : this.c) {
            if (object.f() || !object.g()) continue;
            bl = true;
            GuiInput.a(object.j(), object.i(), 150.0f, object.k());
            break;
        }
        if (!bl) {
            for (ModuleCard sudbet2 : this.c) {
                if (sudbet2.f() || sudbet2.g() || !sudbet2.u()) continue;
                n = 1;
                float[] fArray = sudbet2.w();
                if (fArray == null) break;
                GuiInput.a(fArray[0], fArray[1], fArray[2], fArray[3]);
                break;
            }
        }
        if (!bl && n == 0) {
            GuiInput.a();
        }
        InteractionOverlayController.a().c(BooleanCoercion.from(n));
    }

    public boolean b(int n, int n2) {
        return BooleanCoercion.from(this.a(n, n2) != null ? 1 : 0);
    }

    public void b(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3, float f4) {
        for (ModuleCard sudbet2 : this.c) {
            if (!sudbet2.g()) continue;
            sudbet2.a(matrixStack, s7swsm2, f, f2, n, n2, f3, f4);
        }
    }

    public boolean c(int n, int n2) {
        ModuleCard sudbet2 = this.a(n, n2);
        if (sudbet2 != null) {
            sudbet2.a(n, n2);
            return true;
        }
        for (ModuleCard sudbet3 : this.c) {
            if (sudbet3.f() || !sudbet3.d(n, n2)) continue;
            sudbet3.a(n, n2);
            return true;
        }
        if (this.c()) {
            this.f();
            return true;
        }
        if (!this.d()) {
            return false;
        }
        this.h();
        return true;
    }

    public boolean c() {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.g()) continue;
            return true;
        }
        return false;
    }

    public void h() {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || sudbet2.g()) continue;
            sudbet2.v();
        }
    }

    public void f() {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.g()) continue;
            sudbet2.b();
        }
    }

    public boolean l() {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.h()) continue;
            return true;
        }
        return false;
    }

    public boolean d(int n, int n2) {
        return false;
    }

    public boolean d() {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || sudbet2.g() || !sudbet2.u()) continue;
            return true;
        }
        return false;
    }

    public void a(HaronModule jxs16t2) {
        if (jxs16t2 == null || jxs16t2.m().isEmpty()) {
            return;
        }
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.l().e() != jxs16t2 || sudbet2.f()) continue;
            sudbet2.b();
            return;
        }
        this.i();
        this.h();
        this.c.add(new ModuleCard(new ModuleListEntry(jxs16t2), jxs16t2, -1, false, this.d - 32.0f, 15.0f, VerticalPlacement.CENTER));
    }

    public void a(ToggleableEntry mt5wd72, int n, boolean bl, float f, float f2) {
        float f3;
        if (mt5wd72.e() == null || mt5wd72.e().m().isEmpty()) {
            return;
        }
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().e()) continue;
            iterator.remove();
        }
        ModuleCard object3 = null;
        for (ModuleCard object22 : this.c) {
            if (object22.l() != mt5wd72 || object22.g()) continue;
            object3 = object22;
            break;
        }
        if (object3 != null) {
            if (object3.f()) {
                object3.d();
            } else {
                object3.b();
            }
            return;
        }
        Iterator<ModuleCard> iterator2 = this.c.iterator();
        while (iterator2.hasNext()) {
            ModuleCard panel = iterator2.next();
            panel.c();
            iterator2.remove();
        }
        this.h();
        IconTexture arrowIcon = HaronIcons.getInfo("arrow_v");
        float f4 = arrowIcon != null ? (float)arrowIcon.c() / 2.0f : 0.0f;
        ArrayList<ModuleCard> arrayList = new ArrayList<ModuleCard>();
        for (ModuleCard f5 : this.c) {
            if (f5.f() || f5.n() != bl) continue;
            arrayList.add(f5);
        }
        VerticalPlacement placement = VerticalPlacement.CENTER;
        float f5 = new ModuleCard(mt5wd72, mt5wd72.e(), n, bl, f, f2, placement).k();
        if (!arrayList.isEmpty()) {
            float centeredY = ModuleCard.a(f, f2, VerticalPlacement.CENTER, f4, f5);
            ArrayList<ModuleCard> overlaps = new ArrayList<ModuleCard>();
            for (ModuleCard candidate : arrayList) {
                if (!ModuleCard.a(centeredY, f5, candidate.i(), candidate.k())) continue;
                overlaps.add(candidate);
            }
            if (!overlaps.isEmpty()) {
                ModuleCard firstOverlap = overlaps.get(0);
                VerticalPlacement preferred = f + f2 / 2.0f >= firstOverlap.i() + firstOverlap.k() / 2.0f ? VerticalPlacement.TOP : VerticalPlacement.BOTTOM;
                f3 = ModuleCard.a(f, f2, preferred, f4, f5);
                ArrayList<ModuleCard> f11 = new ArrayList<ModuleCard>();
                for (ModuleCard f7 : arrayList) {
                    if (!ModuleCard.a(f3, f5, f7.i(), f7.k())) continue;
                    f11.add(f7);
                }
                if (f11.isEmpty()) {
                    placement = preferred;
                } else {
                    VerticalPlacement alternate = preferred != VerticalPlacement.TOP ? VerticalPlacement.TOP : VerticalPlacement.BOTTOM;
                    float f6 = ModuleCard.a(f, f2, alternate, f4, f5);
                    ArrayList<ModuleCard> arrayList2 = new ArrayList<ModuleCard>();
                    for (ModuleCard sudbet2 : arrayList) {
                        if (!ModuleCard.a(f6, f5, sudbet2.i(), sudbet2.k())) continue;
                        arrayList2.add(sudbet2);
                    }
                    if (arrayList2.isEmpty()) {
                        placement = alternate;
                    } else {
                        placement = VerticalPlacement.CENTER;
                        Iterator<ModuleCard> iterator3 = overlaps.iterator();
                        while (iterator3.hasNext()) {
                            ((ModuleCard)iterator3.next()).b();
                        }
                    }
                }
            }
        }
        ModuleCard sudbet3 = new ModuleCard(mt5wd72, mt5wd72.e(), n, bl, f, f2, placement);
        float f7 = (float)MinecraftClientAccess.d.getWidth() / 4.0f - 205.5f;
        float f8 = f7 + ClickGuiScreen.d();
        float f9 = 42.0f;
        f3 = ClickGuiScreen.d() - 42.0f;
        float f10 = f7 + 42.0f + (f3 - 150.0f) / 2.0f;
        sudbet3.a(f10, f2, false);
        this.c.add(sudbet3);
    }

    public void a(ToggleableEntry mt5wd72, int n, boolean bl, float f, float f2, float f3, float f4, int n2, int n3) {
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().e()) continue;
            iterator.remove();
        }
        for (ModuleCard existing : this.c) {
            if (existing.l() != mt5wd72 || existing.f() || !existing.g()) continue;
            existing.b();
            return;
        }
        Iterator<ModuleCard> iterator2 = this.c.iterator();
        while (iterator2.hasNext()) {
            ModuleCard existing = iterator2.next();
            if (existing.f()) continue;
            existing.c();
            iterator2.remove();
        }
        this.h();
        ModuleCard created = ModuleCard.a(mt5wd72, mt5wd72.e(), n, bl, f2, f4);
        created.a((float)n2 - 75.0f, (float)n3 - 30.0f - 8.0f, true);
        this.c.add(created);
    }

    public void a(ToggleableEntry mt5wd72, int n, boolean bl, float f, float f2, float f3, float f4, float f5, float f6) {
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            if (!iterator.next().e()) continue;
            iterator.remove();
        }
        for (ModuleCard existing : this.c) {
            if (existing.l() != mt5wd72 || existing.f() || !existing.g()) continue;
            existing.b();
            return;
        }
        Iterator<ModuleCard> iterator2 = this.c.iterator();
        while (iterator2.hasNext()) {
            ModuleCard existing = iterator2.next();
            if (existing.f()) continue;
            existing.c();
            iterator2.remove();
        }
        this.h();
        ModuleCard created = ModuleCard.a(mt5wd72, mt5wd72.e(), n, bl, f2, f4);
        created.a(f5 + 6.0f - 75.0f, f6 - 30.0f - 4.0f, false);
        this.c.add(created);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2, float f3, float f4) {
        ModuleCard sudbet2 = this.a(n, n2);
        boolean bl = sudbet2 != null;
        for (ModuleCard object : this.c) {
            if (object.g()) continue;
            object.a(matrixStack, s7swsm2, f, f2, n, n2, f3, f4, BooleanCoercion.from(!bl || sudbet2 == object ? 0 : 1));
        }
        for (ModuleCard sudbet3 : this.c) {
            if (sudbet3.g()) continue;
            sudbet3.b(matrixStack, s7swsm2, n, n2);
        }
    }

    public List<ModuleCard> a() {
        return this.c;
    }

    public void a(float f, float f2, float f3, int n, float f4, float f5) {
        this.d = f;
        this.e = f2;
        float f6 = f - f3;
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            ModuleCard sudbet2 = iterator.next();
            if (sudbet2.e()) {
                iterator.remove();
                continue;
            }
            int n2 = sudbet2.m();
            if (n2 < 0) continue;
            sudbet2.a(f6 + (float)(n2 / n) * (f4 + f5) + 1.0f);
            if (sudbet2.f() || !sudbet2.a(f, f2)) continue;
            sudbet2.b();
        }
    }

    public void a(int n, int n2, double d, double d2) {
        for (ModuleCard sudbet2 : this.c) {
            if (!sudbet2.f()) {
                sudbet2.a(n, n2, d, d2);
                continue;
            }
            if (!b) continue;
        }
    }

    public boolean a(float f, int n, int n2) {
        if (this.c()) {
            this.f();
            return true;
        }
        if (this.d()) {
            this.h();
            return true;
        }
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.e(n, n2)) continue;
            sudbet2.a(f, n, n2);
            return true;
        }
        return false;
    }

    public boolean a(int n, int n2, int n3) {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.a(n, n2, n3)) continue;
            return true;
        }
        return false;
    }

    public boolean a(char c, int n) {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || !sudbet2.a(c, n)) continue;
            return true;
        }
        return false;
    }

    public ModuleCard a(int n, int n2) {
        for (ModuleCard sudbet2 : this.c) {
            if (sudbet2.f() || sudbet2.g() || !sudbet2.f(n, n2)) continue;
            return sudbet2;
        }
        return null;
    }

    public void k() {
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            ModuleCard sudbet2 = iterator.next();
            sudbet2.c();
            iterator.remove();
        }
    }

    public void g() {
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            ModuleCard sudbet2 = iterator.next();
            if (!sudbet2.g()) continue;
            sudbet2.c();
            iterator.remove();
        }
    }

    public void j() {
        Iterator<ModuleCard> iterator = this.c.iterator();
        while (iterator.hasNext()) {
            iterator.next().c();
        }
        this.c.clear();
    }
}
