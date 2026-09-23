package haron.markers;

import haron.markers.MarkerIcon;
import java.awt.Color;

public class MarkerStyle {
    public final Color a;
    public final MarkerIcon b;

    public MarkerIcon icon() {
        return this.b;
    }

    public MarkerStyle(Color color) {
        this(color, MarkerIcon.EVENT);
    }

    public MarkerStyle(Color color, MarkerIcon h1tssl2) {
        this.a = color;
        this.b = h1tssl2 != null ? h1tssl2 : MarkerIcon.EVENT;
    }

    public Color color() {
        return this.a;
    }
}

