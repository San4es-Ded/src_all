package haron.markers.providers;

import haron.markers.AutoEventMarkerTracker;
import haron.markers.providers.AbstractEventMarkerProvider;
import haron.markers.MarkerStyle;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class DelayedEventMarkerProvider
extends AbstractEventMarkerProvider {
    private static final Map<String, MarkerStyle> EVENT_STYLES = new LinkedHashMap<String, MarkerStyle>();

    public DelayedEventMarkerProvider() {
        super("/event delay", EVENT_STYLES);
    }

    static {
        EVENT_STYLES.put("мистический сундук", DelayedEventMarkerProvider.style(220, 20, 60));
        EVENT_STYLES.put("маяк убийца", DelayedEventMarkerProvider.style(138, 43, 226));
        EVENT_STYLES.put("вулкан", DelayedEventMarkerProvider.style(255, 69, 0));
        EVENT_STYLES.put("загадочный маяк", DelayedEventMarkerProvider.style(75, 0, 130));
        EVENT_STYLES.put("метеоритный дождь", DelayedEventMarkerProvider.style(0, 191, 255));
        EVENT_STYLES.put("алтарь", DelayedEventMarkerProvider.style(178, 34, 34));
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

