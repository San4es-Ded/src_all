package haron.hud.notifications;

import java.awt.Color;

public enum wo1nxr {
    BELL("", new Color(49, 49, 69), new Color(87, 215, 106)),
    WARNING("", new Color(243, 82, 66), new Color(243, 82, 66)),
    FIRE("", new Color(248, 135, 64), new Color(248, 135, 64)),
    CLOUD("", new Color(100, 72, 227), new Color(100, 72, 227));

    private final String icon;
    private final Color background;
    private final Color accent;

    private wo1nxr(String string2, Color color, Color color2) {
        this.icon = string2;
        this.background = color;
        this.accent = color2;
    }

    public Color b() {
        return this.background;
    }

    public Color c() {
        return this.accent;
    }

    public String a() {
        return this.icon;
    }
}

