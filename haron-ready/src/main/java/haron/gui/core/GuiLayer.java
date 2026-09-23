package haron.gui.core;

public enum GuiLayer {
    BACKGROUND(0),
    CATEGORIES(1),
    TABS(2),
    CONTENT(3),
    SCROLLBAR(4),
    SETTINGS_PANEL(5),
    DROPDOWN(6),
    KEYBIND_PANEL(7),
    MODAL_OVERLAY(8);

    private final int order;

    private GuiLayer(int n2) {
        this.order = n2;
    }

    public int a() {
        return this.order;
    }
}

