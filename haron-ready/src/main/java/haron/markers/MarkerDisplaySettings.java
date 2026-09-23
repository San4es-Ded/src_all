package haron.markers;

public final class MarkerDisplaySettings {
    private static int markerMode;
    private static boolean showFriendMarkers;
    private static boolean showHiddenMarkers;
    private static boolean markersEnabled;
    private static boolean showEventMarkers;

    public static boolean showEventMarkers() {
        return showEventMarkers;
    }

    public static void setMarkerMode(int n) {
        markerMode = n;
    }

    public static boolean showHiddenMarkers() {
        return showHiddenMarkers;
    }

    public static void setMarkersEnabled(boolean bl) {
        markersEnabled = bl;
    }

    public static boolean markersEnabled() {
        return markersEnabled;
    }

    public static boolean showFriendMarkers() {
        return showFriendMarkers;
    }

    public static int markerMode() {
        return markerMode;
    }

    public static void setShowFriendMarkers(boolean bl) {
        showFriendMarkers = bl;
    }

    public static void setShowEventMarkers(boolean bl) {
        showEventMarkers = bl;
    }

    public static void setShowHiddenMarkers(boolean bl) {
        showHiddenMarkers = bl;
    }

    private MarkerDisplaySettings() {
    }

    static {
        markersEnabled = true;
        showEventMarkers = true;
    }

    public static boolean e() {
        return MarkerDisplaySettings.showHiddenMarkers();
    }

    public static void b(boolean bl) {
        MarkerDisplaySettings.setShowFriendMarkers(bl);
    }

    public static int b() {
        return MarkerDisplaySettings.markerMode();
    }

    public static void c(boolean bl) {
        MarkerDisplaySettings.setShowEventMarkers(bl);
    }

    public static boolean c() {
        return MarkerDisplaySettings.showFriendMarkers();
    }

    public static boolean d() {
        int n = 385;
        return MarkerDisplaySettings.showEventMarkers();
    }

    public static void d(boolean bl) {
        MarkerDisplaySettings.setShowHiddenMarkers(bl);
    }

    public static boolean a() {
        return MarkerDisplaySettings.markersEnabled();
    }

    public static void a(boolean bl) {
        MarkerDisplaySettings.setMarkersEnabled(bl);
    }

    public static void a(int n) {
        MarkerDisplaySettings.setMarkerMode(n);
    }
}

