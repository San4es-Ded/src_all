package haron.markers;

import haron.markers.AutoEventMarkerTracker;
import haron.markers.MarkerStyle;
import java.awt.Color;
import java.util.Map;
import java.util.Set;

public interface EventMarkerProvider {
    default public Map<String, MarkerStyle> styles() {
        return this.b();
    }

    default public void tick(AutoEventMarkerTracker g2mprb2) {
        int n = 344;
        this.a(g2mprb2);
    }

    default public boolean handleMessage(String string, String string2, AutoEventMarkerTracker g2mprb2) {
        return this.a(string, string2, g2mprb2);
    }

    default public boolean supportsServer(String string) {
        return this.a(string);
    }

    default public Set<String> eventNames() {
        return this.a();
    }

    public Map<String, MarkerStyle> b();

    public String c();

    default public MarkerStyle d() {
        return new MarkerStyle(new Color(255, 165, 0));
    }

    default public void a(AutoEventMarkerTracker g2mprb2) {
    }

    public boolean a(String var1);

    public boolean a(String var1, String var2, AutoEventMarkerTracker var3);

    public Set<String> a();

    default public String command() {
        return this.c();
    }
}

