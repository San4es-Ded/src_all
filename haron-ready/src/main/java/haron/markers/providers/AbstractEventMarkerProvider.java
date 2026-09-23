package haron.markers.providers;

import haron.markers.AutoEventMarkerTracker;
import haron.markers.EventMarkerProvider;
import haron.markers.MarkerStyle;
import java.awt.Color;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class AbstractEventMarkerProvider
implements EventMarkerProvider {
    private static final Pattern XYZ_PATTERN = Pattern.compile("(?:x|х)\\D*(-?\\d+)\\D+(?:y|у)\\D*(-?\\d+)\\D+(?:z|з)\\D*(-?\\d+)", 66);
    private static final Pattern TRIPLE_NUMBER_PATTERN = Pattern.compile("(-?\\d{2,6})\\D+(-?\\d{1,4})\\D+(-?\\d{2,6})");
    private static final Pattern SECONDS_PATTERN = Pattern.compile("(\\d+)\\s*(?:сек|sec|s)", 66);
    private static final Pattern MINUTES_PATTERN = Pattern.compile("(\\d+)\\s*(?:мин|min|m)", 66);
    private final String command;
    private final Map<String, MarkerStyle> styles;
    private final Set<String> eventNames;

    private String findEventName(String string) {
        String string2 = string.toLowerCase(Locale.ROOT);
        for (String string3 : this.eventNames) {
            if (!string2.contains(string3)) continue;
            return string3;
        }
        return null;
    }

    private int[] findCoordinates(String string) {
        Matcher matcher = XYZ_PATTERN.matcher(string);
        if (matcher.find()) {
            return new int[]{AbstractEventMarkerProvider.parseInt(matcher.group(1)), AbstractEventMarkerProvider.parseInt(matcher.group(2)), AbstractEventMarkerProvider.parseInt(matcher.group(3))};
        }
        Matcher matcher2 = TRIPLE_NUMBER_PATTERN.matcher(string);
        if (matcher2.find()) {
            return new int[]{AbstractEventMarkerProvider.parseInt(matcher2.group(1)), AbstractEventMarkerProvider.parseInt(matcher2.group(2)), AbstractEventMarkerProvider.parseInt(matcher2.group(3))};
        }
        return null;
    }

    private int findLifetimeSeconds(String string) {
        Matcher matcher = SECONDS_PATTERN.matcher(string);
        if (matcher.find()) {
            return AbstractEventMarkerProvider.parseInt(matcher.group(1));
        }
        Matcher matcher2 = MINUTES_PATTERN.matcher(string);
        if (matcher2.find()) {
            return AbstractEventMarkerProvider.parseInt(matcher2.group(1)) * 60;
        }
        return -1;
    }

    protected AbstractEventMarkerProvider(String string, Map<String, MarkerStyle> map) {
        this.command = string;
        this.styles = Collections.unmodifiableMap(new LinkedHashMap<String, MarkerStyle>(map));
        this.eventNames = Collections.unmodifiableSet(this.styles.keySet());
    }

    @Override
    public Map<String, MarkerStyle> b() {
        return this.styles;
    }

    @Override
    public String c() {
        return this.command;
    }

    @Override
    public boolean a(String string) {
        int n = 76;
        return string != null && !string.isBlank();
    }

    @Override
    public Set<String> a() {
        return this.eventNames;
    }

    @Override
    public boolean a(String string, String string2, AutoEventMarkerTracker g2mprb2) {
        String string3 = string != null ? string : "";
        String string4 = this.findEventName(string3);
        int[] nArray = this.findCoordinates(string2 != null ? string2 : string3);
        if (string4 != null && nArray != null) {
            g2mprb2.addMarker(string4, nArray, this.findLifetimeSeconds(string3));
            return true;
        }
        if (string4 != null) {
            g2mprb2.setPendingMarker(string4, g2mprb2.h());
            return true;
        }
        if (nArray == null) {
            return false;
        }
        g2mprb2.addPendingMarker(nArray);
        return true;
    }

    private static int parseInt(String string) {
        try {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException numberFormatException) {
            return 0;
        }
    }

    protected static MarkerStyle style(int n, int n2, int n3) {
        return new MarkerStyle(new Color(n, n2, n3));
    }
}

