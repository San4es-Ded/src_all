package haron.gui.markers;

import haron.gui.core.ToggleableEntry;
import haron.markers.MarkerDisplaySettings;

class MysteriousBeaconSettingRow
implements ToggleableEntry {
    public static int a;
    public static boolean b;

    MysteriousBeaconSettingRow() {
    }

    @Override
    public boolean b() {
        return MarkerDisplaySettings.e();
    }

    @Override
    public boolean c() {
        return MarkerDisplaySettings.d();
    }

    @Override
    public boolean d() {
        return false;
    }

    @Override
    public String a() {
        return "Загадочный маяк";
    }

    @Override
    public void a(boolean bl) {
        MarkerDisplaySettings.d(bl);
    }
}
