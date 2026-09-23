package haron.markers;

public enum MarkerIcon {
    HOME("home", ""),
    REPAIR("repair", ""),
    FAST("fast", ""),
    SHIELD("shield", ""),
    DEATH("death", ""),
    DIAMOND("diamond", ""),
    LOCKED("locked", ""),
    MOUNTAIN("mountain", ""),
    CALENDAR("calendar", ""),
    EVENT("calendar", "");

    private final String id;
    private final String glyph;

    private MarkerIcon(String string2, String string3) {
        this.id = string2;
        this.glyph = string3;
    }

    public String b() {
        return this.glyph;
    }

    public String a() {
        return this.id;
    }
}

