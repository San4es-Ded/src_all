package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.ui.*;
import rockstar.client.*;
import java.util.function.Consumer;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.SizedFont;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.script.ItemSettingsPopup;
import rockstar.client.internal.ui.ItemSettingsElement;

public class ItemSettingsButtonRow
implements ItemSettingsElement {
    private final String internalField0248;
    private final ColorRGBA internalField0777;
    private final Consumer<ItemSettingsPopup> internalField0922;

    public ItemSettingsButtonRow(String string, ColorRGBA colorRGBA, Consumer<ItemSettingsPopup> consumer) {
        this.internalField0248 = string;
        this.internalField0777 = colorRGBA;
        this.internalField0922 = consumer;
    }

    @Override
    public float internalMethod05812() {
        return 18.0f;
    }

    @Override
    public void internalMethod05576(UiRenderContext iII, float f, float f2, float f3) {
        iII.drawRoundedRect(f, f2, f3, this.internalMethod05812(), CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod08573());
        SizedFont typedValue020 = Fonts.internalField1154.internalMethod01432(7.0f);
        float f4 = f + (f3 - typedValue020.internalMethod00965(this.internalField0248)) / 2.0f;
        float f5 = f2 + (this.internalMethod05812() - typedValue020.internalMethod04890()) / 2.0f;
        iII.drawText(typedValue020, this.internalField0248, f4, f5, this.internalField0777);
    }

    @Override
    public boolean internalMethod06574(ItemSettingsPopup typedValue097, double d, double d2, int n) {
        this.internalField0922.accept(typedValue097);
        return true;
    }
}

