package haron.module;

public enum ModuleCategory {
    VISUALS("Visuals"),
    HUD("HUD"),
    UTILITIES("Utilities");

    private final String displayName;

    private ModuleCategory(String string2) {
        this.displayName = string2;
    }

    public String a() {
        return this.displayName();
    }

    public String displayName() {
        return this.displayName;
    }
}

