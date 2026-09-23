package haron.hud.notifications;

public enum oo89jz {
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
    private final String icon;

    public String glyph() {
        return this.icon;
    }

    private oo89jz(String string2, String string3) {
        this.id = string2;
        this.icon = string3;
    }

    public String b() {
        return this.icon;
    }

    public String a() {
        return this.id;
    }

    public String id() {
        return this.id;
    }
}

