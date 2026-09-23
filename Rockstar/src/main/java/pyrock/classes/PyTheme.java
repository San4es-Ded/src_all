package pyrock.classes;


import rockstar.client.ui.*;
import java.util.Locale;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;

public class PyTheme {
    public ColorRGBA color(String string) {
        if (string == null) {
            return ThemeColors.internalField1613;
        }
        return switch (string.toLowerCase(Locale.ROOT)) {
            case "accent" -> ThemeColors.internalField1310;
            case "background", "bg" -> ThemeColors.internalMethod07738();
            case "second", "additional" -> ThemeColors.internalField1614;
            case "outline", "border" -> ThemeColors.internalField1616;
            case "shadow" -> ThemeColors.internalField1610;
            case "on_accent", "onaccent" -> ThemeColors.internalField1611;
            case "flat" -> ThemeColors.internalMethod07681();
            case "separator" -> ThemeColors.internalMethod09447();
            case "white" -> ThemeColors.internalField1312;
            case "black" -> ThemeColors.internalField1309;
            default -> ThemeColors.internalField1613;
        };
    }

    public ColorRGBA accent() {
        return ThemeColors.internalField1310;
    }

    public ColorRGBA text() {
        return ThemeColors.internalField1613;
    }

    public ColorRGBA background() {
        return ThemeColors.internalMethod07738();
    }

    public ColorRGBA second() {
        return ThemeColors.internalField1614;
    }

    public ColorRGBA outline() {
        return ThemeColors.internalField1616;
    }

    public ColorRGBA onAccent() {
        return ThemeColors.internalField1611;
    }

    public ColorRGBA readable(ColorRGBA colorRGBA) {
        return colorRGBA == null ? ThemeColors.internalField1613 : ThemeColors.internalMethod01303(colorRGBA);
    }

    public void setAccent(ColorRGBA colorRGBA) {
        if (colorRGBA != null) {
            ThemeColors.internalMethod01095(colorRGBA);
        }
    }

    public String name() {
        try {
            return RockstarClient.getInstance().internalMethod04467().internalMethod05065().name().toLowerCase(Locale.ROOT);
        }
        catch (Throwable throwable) {
            return "dark";
        }
    }

    public boolean dark() {
        return "dark".equals(this.name());
    }
}

