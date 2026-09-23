package haron.markers;

import haron.markers.Waypoint;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class MarkerRegistry {
    private static final List<Waypoint> MARKERS = Collections.synchronizedList(new ArrayList());

    private MarkerRegistry() {
    }

    public static void e() {
        int n = 654;
        MARKERS.removeIf(yo0tnu2 -> {
            return yo0tnu2.o();
        });
    }

    public static void b() {
        MARKERS.clear();
    }

    public static void b(Waypoint yo0tnu2) {
        int n = 536;
        MARKERS.remove(yo0tnu2);
    }

    public static void c() {
        MARKERS.clear();
    }

    public static List<Waypoint> f() {
        int n = 500;
        return MarkerRegistry.a();
    }

    public static boolean d() {
        int n = 568;
        return MARKERS.isEmpty();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static List<Waypoint> a() {
        ArrayList<Waypoint> arrayList;
        List<Waypoint> list = MARKERS;
        synchronized (list) {
            arrayList = new ArrayList<Waypoint>(MARKERS);
        }
        return arrayList;
    }

    public static void a(Waypoint yo0tnu2) {
        if (yo0tnu2 != null) {
            MARKERS.add(yo0tnu2);
        }
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public static Waypoint a(int n, int n2, int n3) {
        List<Waypoint> list = MARKERS;
        synchronized (list) {
            for (Waypoint yo0tnu2 : MARKERS) {
                if (yo0tnu2.b() != n || yo0tnu2.c() != n2 || yo0tnu2.d() != n3) continue;
                return yo0tnu2;
            }
            return null;
        }
    }

    public static List<Waypoint> g() {
        return MarkerRegistry.a();
    }
}

