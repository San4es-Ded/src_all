package haron.gui.events;

public enum ServerEventType {
    METEOR("Метеор"),
    BEACON("Маяк"),
    MINE("Шахта");

    private final String label;
    public static int d;
    public static boolean e;

    private ServerEventType(String string2) {
        this.label = string2;
    }

    public String a() {
        return this.label;
    }
}

