package haron.gui.core;

import haron.gui.core.ClickGuiTabType;
import java.awt.Color;

enum DockTab {
    MODULES_VISUALS(ClickGuiTabType.MODULES, "☄", new Color(255, 160, 55, 210), 0),
    MODULES_HUD(ClickGuiTabType.MODULES, "♳", new Color(255, 160, 55, 190), 1),
    MODULES_UTILITIES(ClickGuiTabType.MODULES, "⚙", new Color(255, 160, 55, 190), 2),
    MARKERS(ClickGuiTabType.MARKERS, "♂", new Color(255, 150, 60, 190), -1),
    FRIENDS(ClickGuiTabType.FRIENDS, "☠", new Color(255, 150, 60, 190), -1),
    EVENTS(ClickGuiTabType.EVENTS, "⏱", new Color(255, 150, 60, 190), -1);

    final ClickGuiTabType tab;
    final String icon;
    final Color iconColor;
    final int subCategory;

    private DockTab(ClickGuiTabType xtnc352, String string2, Color color, int n2) {
        this.tab = xtnc352;
        this.icon = string2;
        this.iconColor = color;
        this.subCategory = n2;
    }
}

