package haron.gui.config;

public enum ConfigAction {
    SAVE_TO("Сохранить в"),
    SHARE("Поделиться"),
    RENAME("Переименовать"),
    DELETE("Удалить");

    private final String label;
    public static int e;
    public static boolean f;

    private ConfigAction(String string2) {
        this.label = string2;
    }

    public String a() {
        return this.label;
    }
}

