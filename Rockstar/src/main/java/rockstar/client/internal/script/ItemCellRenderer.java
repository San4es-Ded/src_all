package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.CornerRadii;
import rockstar.client.internal.inventory.DonItemCatalog;
import rockstar.client.ui.ThemeColors;

public class ItemCellRenderer {
    public static void internalMethod00722(UiRenderContext iII, SizedFont typedValue020, DonItemCatalog.InternalType0190 nestedValue2027, float f, float f2, float f3, float f4, boolean bl) {
        Object object;
        ColorRGBA colorRGBA = ThemeColors.internalMethod08573().withAlpha(191.25f);
        iII.drawRoundedRect(f, f2, f3, f4, CornerRadii.internalMethod03908(2.0f), colorRGBA);
        if (bl) {
            object = ThemeColors.internalMethod02531().withAlpha(110.0f);
            iII.drawRoundedRect(f, f2, f3, f4, CornerRadii.internalMethod03908(2.0f), (ColorRGBA)object);
            colorRGBA = colorRGBA.mix(((ColorRGBA)object).withAlpha(255.0f), ((ColorRGBA)object).getAlpha() / 255.0f);
        }
        iII.drawItem(nestedValue2027.internalMethod05752(), f + (f3 - 16.0f) / 2.0f, f2 + (f4 - 16.0f) / 2.0f - 6.0f, 1.0f);
        object = (nestedValue2027.internalMethod06194() != null ? nestedValue2027.internalMethod06194() : nestedValue2027.internalMethod05752().getName().getString()).trim();
        if (!((String)object).isEmpty()) {
            ItemCellRenderer.internalMethod05343(iII, typedValue020, (String)object, f, f2 + f4 - 10.0f, f3, ThemeColors.internalMethod01303(colorRGBA));
        }
    }

    private static void internalMethod05343(UiRenderContext iII, SizedFont typedValue020, String string, float f, float f2, float f3, ColorRGBA colorRGBA) {
        List<String> list = ItemCellRenderer.internalMethod06806(typedValue020, string, f3);
        if (list.size() == 1) {
            ItemCellRenderer.internalMethod00619(iII, typedValue020, list.getFirst(), f, f2, f3, colorRGBA);
        } else {
            ItemCellRenderer.internalMethod00619(iII, typedValue020, list.getFirst(), f, f2 - 2.0f, f3, colorRGBA);
            ItemCellRenderer.internalMethod00619(iII, typedValue020, list.get(1), f, f2 + typedValue020.internalMethod04890() - 1.0f, f3, colorRGBA);
        }
    }

    private static void internalMethod00619(UiRenderContext iII, SizedFont typedValue020, String string, float f, float f2, float f3, ColorRGBA colorRGBA) {
        float f4 = typedValue020.internalMethod00965(string);
        if (f4 <= f3) {
            iII.drawText(typedValue020, string, f + (f3 - f4) / 2.0f + 1.0f, f2, colorRGBA);
        } else {
            iII.drawFadeoutText(typedValue020, string, f + 2.0f, f2, colorRGBA, 0.8f, 1.0f, f3 - 4.0f);
        }
    }

    private static List<String> internalMethod06806(SizedFont typedValue020, String string, float f) {
        String[] stringArray = string.split("\\s+");
        if (stringArray.length <= 1) {
            return List.of(string);
        }
        ArrayList<String> arrayList = new ArrayList<String>(2);
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < stringArray.length; ++i) {
            String string2;
            if (stringArray[i].isEmpty()) continue;
            String string3 = string2 = stringBuilder.isEmpty() ? stringArray[i] : String.valueOf(stringBuilder) + " " + stringArray[i];
            if (typedValue020.internalMethod00965(string2) <= f || stringBuilder.isEmpty()) {
                stringBuilder.setLength(0);
                stringBuilder.append(string2);
                continue;
            }
            arrayList.add(stringBuilder.toString());
            stringBuilder.setLength(0);
            for (int j = i; j < stringArray.length; ++j) {
                if (stringArray[j].isEmpty()) continue;
                if (!stringBuilder.isEmpty()) {
                    stringBuilder.append(' ');
                }
                stringBuilder.append(stringArray[j]);
            }
            arrayList.add(stringBuilder.toString());
            return arrayList;
        }
        if (!stringBuilder.isEmpty()) {
            arrayList.add(stringBuilder.toString());
        }
        return arrayList.isEmpty() ? List.of(string) : arrayList;
    }
}

