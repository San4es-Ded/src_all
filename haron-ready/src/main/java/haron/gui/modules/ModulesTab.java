package haron.gui.modules;

import haron.audio.SoundPlayer;
import haron.core.BooleanCoercion;
import haron.gui.core.CategorySelectionModel;
import haron.gui.core.ToggleEntryProvider;
import haron.gui.core.CategorySelectorOverlay;
import haron.gui.core.ToggleableEntry;
import haron.gui.core.ScrollFadeOverlay;
import haron.gui.core.ClickGuiTab;
import haron.gui.core.ClickGuiScreen;
import haron.gui.core.ClickGuiTabType;
import haron.gui.modules.ModuleListEntry;
import haron.gui.modules.ToggleEntryListOverlay;
import haron.gui.modules.ModuleCard;
import haron.gui.widgets.SearchBox;
import haron.gui.widgets.IconButton;
import haron.module.ModuleManager;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.hud.ClientColor;
import haron.render.ShapeRenderer;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.minecraft.client.util.math.MatrixStack;

public class ModulesTab
implements CategorySelectionModel,
ToggleEntryProvider,
ClickGuiTab {
    private static final String[] a = new String[]{"Visuals", "HUD", "Utilities"};
    private static final ModuleCategory[] b = new ModuleCategory[]{ModuleCategory.VISUALS, ModuleCategory.HUD, ModuleCategory.UTILITIES};
    private static final float c = 117.5f;
    private static final float d = 15.0f;
    private static final float e = 19.0f;
    private static final float f = 19.0f;
    private static final float g = 15.0f;
    private static final float h = 4.0f;
    private static final float SEARCH_LEFT_PAD = 8.0f;
    private static final float SEARCH_TOP_PAD = 16.0f;
    private static final float SEARCH_GAP = 4.0f;
    private static final float COLOR_DOT_SIZE = 11.0f;
    private static final float COLOR_DOT_HIT_SIZE = 18.0f;
    private static final float COLOR_ROW_LEFT_GAP = 18.0f;
    private static final float COLOR_ROW_RIGHT_GAP = 18.0f;
    private static final float SETTINGS_SIZE = 15.0f;
    private static final float SETTINGS_RIGHT_PAD = 19.0f;
    private final CategorySelectorOverlay l;
    private final ToggleEntryListOverlay m;
    private final ScrollFadeOverlay n;
    private final SearchBox o;
    private final IconButton p;
    private List<ModuleListEntry> r;
    private int i = 0;
    private String q = "";
    private boolean s = false;
    private final Map<ModuleCategory, List<ModuleListEntry>> j = new HashMap<ModuleCategory, List<ModuleListEntry>>();
    private final List<ModuleListEntry> k = new ArrayList<ModuleListEntry>();

    public void openSettingsForCard(ModuleCard sudbet2) {
        if (sudbet2 == null || sudbet2.f()) {
            return;
        }
        sudbet2.b();
    }

    public boolean hasOpenSettingsAt(int n, int n2) {
        return this.m.hasOpenSettingsAt(n, n2);
    }

    private static int colorDistance(Color color, Color color2) {
        if (color == null || color2 == null) {
            return 999999;
        }
        int n = color.getRed() - color2.getRed();
        int n2 = color.getGreen() - color2.getGreen();
        int n3 = color.getBlue() - color2.getBlue();
        return Math.abs(n) + Math.abs(n2) + Math.abs(n3);
    }

    private boolean isSelectedColor(Color color) {
        return ModulesTab.colorDistance(ClientColor.currentColor(), color) < 16;
    }

    private boolean isInsideColorDot(float f, float f2, int n, int n2) {
        float f3 = 9.0f;
        return (float)n >= f - 9.0f && (float)n <= f + 9.0f && (float)n2 >= f2 - 9.0f && (float)n2 <= f2 + 9.0f;
    }

    private float getSettingsX(float f) {
        return f + ClickGuiScreen.d() - 19.0f - 15.0f;
    }

    private float getSettingsY(float f) {
        return this.getSearchY(f);
    }

    public void openSettings(HaronModule jxs16t2) {
        this.m.a(jxs16t2);
    }

    private void renderColorDots(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        Color[] colorArray = ClientColor.presets();
        if (colorArray == null || colorArray.length == 0) {
            return;
        }
        float f3 = this.getSearchX(f);
        float f4 = this.getSearchY(f2);
        float f5 = 117.5f;
        float f6 = 15.0f;
        float f7 = this.getSettingsX(f);
        float f8 = f3 + 117.5f + 18.0f;
        float f9 = f7 - 18.0f;
        float f10 = Math.max(0.0f, f9 - f8);
        float f11 = colorArray.length <= 1 ? 0.0f : f10 / (float)(colorArray.length - 1);
        float f12 = 16.0f;
        if (colorArray.length > 1 && f11 < 16.0f) {
            f11 = 16.0f;
        }
        float f13 = f4 + 7.5f;
        for (int i = 0; i < colorArray.length; ++i) {
            Color color;
            float f14;
            float f15;
            Color color2 = colorArray[i];
            float f16 = f8 + (float)i * f11;
            boolean bl = this.isInsideColorDot(f16, f13, n, n2);
            boolean bl2 = this.isSelectedColor(color2);
            float f17 = 11.0f;
            if (bl2) {
                f17 = 13.0f;
            } else if (bl) {
                f17 = 12.5f;
            }
            if (bl2 || bl) {
                f15 = bl2 ? f17 + 6.0f : f17 + 4.0f;
                f14 = f16 - f15 / 2.0f;
                float f18 = f13 - f15 / 2.0f;
                if (bl2) {
                    Color color3 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue());
                    color = ModulesTab.withAlpha(color3, 180);
                } else {
                    color = ModulesTab.withAlpha(Color.WHITE, 60);
                }
                s7swsm2.a(f14, f18, f15, f15, f15 / 2.0f, color, color, color, color, matrixStack);
            }
            f15 = f16 - f17 / 2.0f;
            f14 = f13 - f17 / 2.0f;
            Color color4 = new Color(color2.getRed(), color2.getGreen(), color2.getBlue(), 255);
            s7swsm2.a(f15, f14, f17, f17, f17 / 2.0f, color4, color4, color4, color4, matrixStack);
            color = ModulesTab.withAlpha(Color.WHITE, bl2 ? 65 : (bl ? 45 : 25));
            s7swsm2.a(f15 + 2.0f, f14 + 2.0f, f17 - 4.0f, f17 - 4.0f, (f17 - 4.0f) / 2.0f, color, color, color, color, matrixStack);
        }
    }

    private static Color withAlpha(Color color, int n) {
        if (color == null) {
            color = Color.WHITE;
        }
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), Math.max(0, Math.min(255, n)));
    }

    public ModuleCard getCardAt(int n, int n2) {
        return this.m.findCardByBounds(n, n2);
    }

    private float getSearchX(float f) {
        return f + 8.0f;
    }

    private float getSearchY(float f) {
        return f + 16.0f;
    }

    public ModuleCard findCardAtBounds(int n, int n2) {
        for (ModuleCard sudbet2 : this.m.a()) {
            if (sudbet2.f() || !sudbet2.e(n, n2)) continue;
            return sudbet2;
        }
        return null;
    }

    private boolean handleColorDotsClick(float f, float f2, int n, int n2) {
        Color[] colorArray = ClientColor.presets();
        if (colorArray == null || colorArray.length == 0) {
            return false;
        }
        float f3 = this.getSearchX(f);
        float f4 = this.getSearchY(f2);
        float f5 = 117.5f;
        float f6 = 15.0f;
        float f7 = this.getSettingsX(f);
        float f8 = f3 + 117.5f + 18.0f;
        float f9 = f7 - 18.0f;
        float f10 = Math.max(0.0f, f9 - f8);
        float f11 = colorArray.length <= 1 ? 0.0f : f10 / (float)(colorArray.length - 1);
        float f12 = 16.0f;
        if (colorArray.length > 1 && f11 < 16.0f) {
            f11 = 16.0f;
        }
        float f13 = f4 + 7.5f;
        for (int i = 0; i < colorArray.length; ++i) {
            float f14 = f8 + (float)i * f11;
            if (!this.isInsideColorDot(f14, f13, n, n2)) continue;
            ClientColor.setStaticColor(colorArray[i]);
            try {
                SoundPlayer.play("disabled1", 0.45f);
            }
            catch (Throwable throwable) {
                // empty catch block
            }
            return true;
        }
        return false;
    }

    public int getCurrentSubCategory() {
        return this.i;
    }

    public ModulesTab() {
        for (ModuleCategory jtlzd02 : ModuleCategory.values()) {
            ArrayList<ModuleListEntry> arrayList = new ArrayList<ModuleListEntry>();
            Iterator<HaronModule> iterator = ModuleManager.byCategory(jtlzd02).iterator();
            while (iterator.hasNext()) {
                arrayList.add(new ModuleListEntry(iterator.next()));
            }
            arrayList.sort(Comparator.comparing(l7ueve2 -> {
                return l7ueve2.a().toLowerCase();
            }));
            this.j.put(jtlzd02, arrayList);
            this.k.addAll(arrayList);
        }
        this.k.sort(Comparator.comparing(l7ueve2 -> {
            return l7ueve2.a().toLowerCase();
        }));
        this.r = new ArrayList<ModuleListEntry>();
        this.l = new CategorySelectorOverlay(this);
        this.m = new ToggleEntryListOverlay(this);
        this.n = new ScrollFadeOverlay();
        this.o = new SearchBox(117.5f, 15.0f);
        this.o.a(this::a);
        this.p = new IconButton();
        this.p.a((Void void_) -> {
            this.s();
        });
    }

    @Override
    public List<? extends ToggleableEntry> e() {
        if (this.s) {
            return this.r == null || this.r.isEmpty() ? Collections.emptyList() : this.r;
        }
        List<ModuleListEntry> list = this.j.get((Object)this.g());
        return list == null ? Collections.emptyList() : list;
    }

    public boolean i() {
        Iterator<ModuleCard> iterator = this.m.a().iterator();
        while (iterator.hasNext()) {
            if (iterator.next().f()) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean b() {
        return BooleanCoercion.from(this.o.c() || this.m.c() ? 1 : 0);
    }

    @Override
    public void b(float f, float f2, int n, int n2) {
        this.o.b(false);
        this.m.b();
        this.m.b(f, f2, n, n2);
    }

    private boolean b(String string, String string2) {
        String[] stringArray = string.split(" ");
        StringBuilder stringBuilder = new StringBuilder();
        for (String string3 : stringArray) {
            if (string3.isEmpty()) continue;
            stringBuilder.append(string3.charAt(0));
        }
        return stringBuilder.toString().contains(string2.replace(" ", ""));
    }

    private void s() {
    }

    @Override
    public String[] c() {
        return a;
    }

    @Override
    public void c(float f, float f2, int n, int n2) {
        this.m.c(f, f2, n, n2);
    }

    public void n() {
        this.m.h();
    }

    public List<ModuleCard> h() {
        return this.m.a();
    }

    @Override
    public int f() {
        return 2;
    }

    public void l() {
        this.m.f();
    }

    @Override
    public int d() {
        return this.i;
    }

    @Override
    public ClickGuiTabType a() {
        return ClickGuiTabType.MODULES;
    }

    public boolean a(char c, int n) {
        if (this.m.p()) {
            return this.m.a(c, n);
        }
        return this.o.c() ? this.o.a(c, n) : this.m.a(c, n);
    }

    public void a(ModuleCategory jtlzd02) {
        for (int i = 0; i < b.length; ++i) {
            if (b[i] != jtlzd02) continue;
            if (this.i != i) {
                this.i = i;
                this.m.d();
                return;
            }
            return;
        }
    }

    private void a(String string) {
        String string2 = this.q = string == null ? "" : string.toLowerCase().trim();
        if (this.q.isEmpty()) {
            this.s = false;
            if (this.r != null) {
                this.r.clear();
                return;
            }
            return;
        }
        this.s = true;
        this.r = new ArrayList<ModuleListEntry>();
        for (int i = 0; i < this.k.size(); ++i) {
            ModuleListEntry l7ueve2 = this.k.get(i);
            if (!l7ueve2.a().toLowerCase().contains(this.q)) continue;
            this.r.add(l7ueve2);
        }
        this.m.d();
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        this.m.a(matrixStack, s7swsm2, n, n2);
    }

    private boolean a(String string, String string2) {
        String string3 = string.toLowerCase();
        return string3.contains(string2) || string3.replace(" ", "").contains(string2.replace(" ", "")) || this.b(string3, string2);
    }

    @Override
    public void a(float f, float f2, int n, int n2, double d, double d2) {
        this.m.a(f, f2, n, n2, d, d2);
    }

    @Override
    public void a(float f, float f2, int n, int n2) {
        this.m.b();
        if (this.o.a(n, n2) || this.p.a(n, n2)) {
            return;
        }
        if (this.handleColorDotsClick(f, f2, n, n2)) {
            return;
        }
        this.m.a(f, f2, n, n2);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2) {
        this.m.a(matrixStack, s7swsm2);
    }

    @Override
    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.m.b();
        float f3 = this.getSearchX(f);
        float f4 = this.getSearchY(f2);
        float f5 = 117.5f;
        float f6 = 15.0f;
        this.o.a(matrixStack, s7swsm2, f3, f4, 117.5f, 15.0f, n, n2);
        this.renderColorDots(matrixStack, s7swsm2, f, f2, n, n2);
        float f7 = this.getSettingsX(f);
        float f8 = this.getSettingsY(f2);
        this.p.a(matrixStack, s7swsm2, f7, f8, 15.0f, n, n2);
        this.m.a(matrixStack, s7swsm2, f, f2, n, n2);
        this.n.a(matrixStack, s7swsm2, f, f2, n, n2);
    }

    @Override
    public void a(int n) {
        if (n < 0 || n >= b.length || this.i == n) {
            return;
        }
        this.i = n;
        this.m.d();
    }

    @Override
    public boolean a(int n, int n2, int n3) {
        if (this.m.p()) {
            this.m.a(n, n2, n3);
            return true;
        }
        if (this.o.c() && this.o.a(n, n2, n3)) {
            return true;
        }
        if (n == 256 && this.m.hasOpenSettings()) {
            this.m.closeOpenSettings();
            return true;
        }
        return this.m.a(n, n2, n3);
    }

    public void a(float f, int n, int n2) {
        this.m.a(f, n, n2);
    }

    @Override
    public void a(float f) {
        this.m.a(f);
    }

    public void m() {
        int n = 986;
        this.m.g();
    }

    public void o() {
        int n = 246;
        this.o.b(false);
        this.o.b();
        this.m.i();
    }

    public boolean p() {
        return this.o.c();
    }

    public void k() {
        this.m.e();
    }

    public ModuleCategory g() {
        return b[this.i];
    }

    public boolean j() {
        return this.m.j();
    }

    public String q() {
        int n = 991;
        return this.q;
    }

    public boolean r() {
        return this.s;
    }
}

