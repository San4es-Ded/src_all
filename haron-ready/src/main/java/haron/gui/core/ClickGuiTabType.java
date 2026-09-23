package haron.gui.core;

public enum ClickGuiTabType {
    MODULES("modules", ""),
    MARKERS("markers", ""),
    FRIENDS("friends", ""),
    EVENTS("events", ""),
    CONFIGS("configs", "");

    private final String id;
    private final String icon;

    private ClickGuiTabType(String string2, String string3) {
        this.id = string2;
        this.icon = string3;
    }

    public String b() {
        return this.icon;
    }

    public String a() {
        return this.id;
    }
}

