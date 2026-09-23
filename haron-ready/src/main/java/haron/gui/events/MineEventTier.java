package haron.gui.events;

public enum MineEventTier {
    LEGENDARY("Легендарная"),
    ELITE("Элитный"),
    RICH("Богатый"),
    MYTHIC("Мифическая");

    private final String label;
    public static int e;
    public static boolean f;

    private MineEventTier(String string2) {
        this.label = string2;
    }

    public String a() {
        return this.label;
    }
}

