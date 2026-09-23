package haron.gui.markers;

import haron.gui.core.ToggleableEntry;
import haron.markers.MarkerDisplaySettings;

class QuickMarkerSettingRow
implements ToggleableEntry {
    public static int a;
    public static boolean b;

    QuickMarkerSettingRow() {
    }

    @Override
    public boolean b() {
        return MarkerDisplaySettings.a();
    }

    @Override
    public boolean h() {
        return false;
    }

    @Override
    public int f() {
        return MarkerDisplaySettings.b();
    }

    @Override
    public boolean d() {
        int n = 283;
        return true;
    }

    @Override
    public void a(boolean bl) {
        MarkerDisplaySettings.a(bl);
    }

    @Override
    public String a() {
        return "Быстрая метка";
    }

    @Override
    public void a(int n) {
        MarkerDisplaySettings.a(n);
    }
}
