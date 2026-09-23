package haron.markers.providers;

import haron.markers.AutoEventMarkerTracker;
import haron.markers.providers.AbstractEventMarkerProvider;
import haron.markers.MarkerStyle;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class ActiveEventMarkerProvider
extends AbstractEventMarkerProvider {
    private static final Map<String, MarkerStyle> EVENT_STYLES = new LinkedHashMap<String, MarkerStyle>();

    public ActiveEventMarkerProvider() {
        super("/event active", EVENT_STYLES);
    }

    static {
        EVENT_STYLES.put("ценный груз", ActiveEventMarkerProvider.style(255, 182, 193));
        EVENT_STYLES.put("контейнер", ActiveEventMarkerProvider.style(148, 0, 211));
        EVENT_STYLES.put("посылка", ActiveEventMarkerProvider.style(95, 158, 160));
        EVENT_STYLES.put("золотая лихорадка", ActiveEventMarkerProvider.style(255, 215, 0));
        EVENT_STYLES.put("цветочная поляна", ActiveEventMarkerProvider.style(70, 130, 180));
        EVENT_STYLES.put("опытный тыпо", ActiveEventMarkerProvider.style(255, 140, 0));
        EVENT_STYLES.put("смертельная шахта", ActiveEventMarkerProvider.style(139, 0, 0));
        EVENT_STYLES.put("таинственный корабль", ActiveEventMarkerProvider.style(160, 82, 45));
    }

    public Map b() {
        return super.b();
    }

    @Override
    public String c() {
        return super.c();
    }

    @Override
    public boolean a(String string, String string2, AutoEventMarkerTracker g2mprb2) {
        int n = 984;
        return super.a(string, string2, g2mprb2);
    }

    @Override
    public boolean a(String string) {
        return super.a(string);
    }

    public Set a() {
        return super.a();
    }
}

