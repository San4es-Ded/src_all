package haron.gui.markers;

import haron.gui.core.ToggleEntryProvider;
import haron.gui.core.ToggleableEntry;
import haron.gui.markers.MysteriousBeaconSettingRow;
import haron.gui.markers.QuickMarkerSettingRow;
import haron.gui.markers.MarkerToggleSettingRow;
import haron.gui.modules.ToggleEntryListOverlay;
import haron.markers.MarkerDisplaySettings;
import haron.render.ShapeRenderer;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.client.util.math.MatrixStack;

class MarkerSettingsPanel
implements ToggleEntryProvider {
    private final List<ToggleableEntry> c = new ArrayList<ToggleableEntry>();
    private final ToggleEntryListOverlay d;
    public static int a;
    public static boolean b;

    public MarkerSettingsPanel() {
        this.c.add(new QuickMarkerSettingRow());
        this.c.add(new MarkerToggleSettingRow("Метка смерти", MarkerDisplaySettings::c, bl -> {
            MarkerDisplaySettings.b(bl);
        }));
        this.c.add(new MarkerToggleSettingRow("Авто метки, события", MarkerDisplaySettings::d, bl -> {
            MarkerDisplaySettings.c(bl);
        }));
        this.c.add(new MysteriousBeaconSettingRow());
        this.d = new ToggleEntryListOverlay(this);
    }

    @Override
    public List<? extends ToggleableEntry> e() {
        return this.c.stream().filter(mt5wd72 -> {
            return mt5wd72.c();
        }).collect(Collectors.toList());
    }

    public boolean b() {
        return this.d.c();
    }

    public void b(float f, float f2, int n, int n2) {
        this.d.b(f, f2, n, n2);
    }

    public void c() {
        this.d.g();
    }

    public void c(float f, float f2, int n, int n2) {
        this.d.c(f, f2, n, n2);
    }

    @Override
    public int f() {
        return 1;
    }

    public void a() {
        this.d.d();
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, float f, float f2, int n, int n2) {
        this.d.a(matrixStack, s7swsm2, f, f2, n, n2);
    }

    public void a(float f) {
        this.d.a(f);
    }

    public void a(float f, float f2, int n, int n2, double d, double d2) {
        this.d.a(f, f2, n, n2, d, d2);
    }

    public void a(MatrixStack matrixStack, ShapeRenderer s7swsm2, int n, int n2) {
        int n3 = 750;
        this.d.a(matrixStack, s7swsm2, n, n2);
    }

    public boolean a(int n, int n2, int n3) {
        return this.d.a(n, n2, n3);
    }

    public void a(float f, float f2, int n, int n2) {
        int n3 = 114;
        this.d.a(f, f2, n, n2);
    }
}

