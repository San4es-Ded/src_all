package pulse.markers;

public final class MarkerOptions {
   private static int markerMode;
   private static boolean showFriendMarkers;
   private static boolean showHiddenMarkers;
   private static boolean markersEnabled = true;
   private static boolean showEventMarkers = true;

   private MarkerOptions() {
   }

   public static boolean markersEnabled() {
      return markersEnabled;
   }

   public static void setMarkersEnabled(boolean z) {
      markersEnabled = z;
   }

   public static int markerMode() {
      return markerMode;
   }

   public static void setMarkerMode(int i) {
      markerMode = i;
   }

   public static boolean showFriendMarkers() {
      return showFriendMarkers;
   }

   public static void setShowFriendMarkers(boolean z) {
      showFriendMarkers = z;
   }

   public static boolean showEventMarkers() {
      return showEventMarkers;
   }

   public static void setShowEventMarkers(boolean z) {
      showEventMarkers = z;
   }

   public static boolean showHiddenMarkers() {
      return showHiddenMarkers;
   }

   public static void setShowHiddenMarkers(boolean z) {
      showHiddenMarkers = z;
   }

   public static boolean a() {
      return markersEnabled();
   }

   public static void a(boolean z) {
      setMarkersEnabled(z);
   }

   public static int b() {
      return markerMode();
   }

   public static void a(int i) {
      setMarkerMode(i);
   }

   public static boolean c() {
      return showFriendMarkers();
   }

   public static void b(boolean z) {
      setShowFriendMarkers(z);
   }

   public static boolean d() {
      return showEventMarkers();
   }

   public static void c(boolean z) {
      setShowEventMarkers(z);
   }

   public static boolean e() {
      return showHiddenMarkers();
   }

   public static void d(boolean z) {
      setShowHiddenMarkers(z);
   }
}
