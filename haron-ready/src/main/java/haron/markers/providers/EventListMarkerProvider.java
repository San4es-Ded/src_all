package haron.markers.providers;

import haron.markers.AutoEventMarkerTracker;
import haron.markers.providers.AbstractEventMarkerProvider;
import haron.markers.MarkerStyle;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class EventListMarkerProvider
extends AbstractEventMarkerProvider {
    private static final Map<String, MarkerStyle> EVENT_STYLES = new LinkedHashMap<String, MarkerStyle>();

    public EventListMarkerProvider() {
        super("/events", EVENT_STYLES);
    }

    static {
        EVENT_STYLES.put("пороховой карьер", EventListMarkerProvider.style(220, 20, 60));
        EVENT_STYLES.put("павший самурай", EventListMarkerProvider.style(105, 105, 105));
        EVENT_STYLES.put("дерево мудрости", EventListMarkerProvider.style(34, 139, 34));
        EVENT_STYLES.put("экскалибур", EventListMarkerProvider.style(255, 215, 0));
        EVENT_STYLES.put("пиратский корабль", EventListMarkerProvider.style(139, 69, 19));
    }

    public Map b() {
        return super.b();
    }

    @Override
    public String c() {
        int n = 88;
        return super.c();
    }

    @Override
    public boolean a(String string, String string2, AutoEventMarkerTracker g2mprb2) {
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

