package su.sacura.features.modules.impl;

public enum Category {
    MOVEMENT("Movement", "c"),
    PLAYER("Player", "e"),
    RENDER("Render", "d"),
    DISPLAY("Display", "f"),
    LUA("Lua", "w"),
    THEMES("Themes", "p"),
    CONFIGS("Configs", "n");

    public final String name;
    public final String iconChar;

    private Category(String name, String iconChar) {
        this.name = name;
        this.iconChar = iconChar;
    }
}
