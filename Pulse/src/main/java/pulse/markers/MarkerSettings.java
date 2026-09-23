package pulse.markers;

public final class MarkerSettings {
   private static boolean quickMarkerEnabled;
   private static int quickMarkerKey;
   private static boolean markersVisible;
   private static boolean markerDistanceFilter;
   private static boolean pvpSafeFilter;

   private MarkerSettings() {
   }

   public static boolean a() {
      return quickMarkerEnabled;
   }

   public static void a(boolean z) {
      quickMarkerEnabled = z;
   }

   public static int b() {
      return quickMarkerKey;
   }

   public static void a(int i) {
      quickMarkerKey = i;
   }

   public static boolean c() {
      return markersVisible;
   }

   public static void b(boolean z) {
      markersVisible = z;
   }

   public static boolean d() {
      return markerDistanceFilter;
   }

   public static void c(boolean z) {
      markerDistanceFilter = z;
   }

   public static boolean e() {
      return pvpSafeFilter;
   }

   public static void d(boolean z) {
      pvpSafeFilter = z;
   }
}
