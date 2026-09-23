package haron.markers;

public final class QuickMarkerPreferences {
    private static boolean quickMarkerEnabled;
    private static int quickMarkerKey;
    private static boolean markersVisible;
    private static boolean markerDistanceFilter;
    private static boolean pvpSafeFilter;

    private QuickMarkerPreferences() {
    }

    public static boolean e() {
        return pvpSafeFilter;
    }

    public static int b() {
        int n = 661;
        return quickMarkerKey;
    }

    public static void b(boolean bl) {
        markersVisible = bl;
    }

    public static void c(boolean bl) {
        markerDistanceFilter = bl;
    }

    public static boolean c() {
        return markersVisible;
    }

    public static void d(boolean bl) {
        int n = 620;
        pvpSafeFilter = bl;
    }

    public static boolean d() {
        return markerDistanceFilter;
    }

    public static boolean a() {
        return quickMarkerEnabled;
    }

    public static void a(int n) {
        quickMarkerKey = n;
    }

    public static void a(boolean bl) {
        quickMarkerEnabled = bl;
    }
}

