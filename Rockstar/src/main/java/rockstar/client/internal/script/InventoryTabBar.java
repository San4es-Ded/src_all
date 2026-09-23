package rockstar.client.internal.script;





import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.internal.inventory.DonItemCatalog;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.UiUtils;

public class InventoryTabBar {
    private final float internalField0205 = 16.0f;
    private DonItemCatalog.InternalType0189 internalField0406 = DonItemCatalog.InternalType0189.internalField0406;
    private final DonItemCatalog.InternalType0189[] internalField0079 = DonItemCatalog.InternalType0189.values();

    public void internalMethod01378(UiRenderContext iII, float f, float f2, float f3) {
        float f4 = this.internalMethod02211(f3);
        for (int i = 0; i < this.internalField0079.length; ++i) {
            this.internalMethod02609(iII, this.internalField0079[i], f + 10.0f + (float)i * (f4 + 4.0f), f2, f4);
        }
    }

    public boolean internalMethod05280(double d, double d2, float f, float f2, float f3) {
        float f4 = this.internalMethod02211(f3);
        for (int i = 0; i < this.internalField0079.length; ++i) {
            float f5 = f + 10.0f + (float)i * (f4 + 4.0f);
            if (!UiUtils.internalMethod05785(f5, f2, f4, 16.0, d, d2)) continue;
            if (this.internalField0406 != this.internalField0079[i]) {
                this.internalField0406 = this.internalField0079[i];
                return true;
            }
            return false;
        }
        return false;
    }

    private void internalMethod02609(UiRenderContext iII, DonItemCatalog.InternalType0189 nestedValue2026, float f, float f2, float f3) {
        boolean bl = nestedValue2026 == this.internalField0406;
        ColorRGBA colorRGBA = bl ? ThemeColors.internalMethod02531().withAlpha(180.0f) : ThemeColors.internalMethod08573().withAlpha(153.0f);
        iII.drawRoundedRect(f, f2, f3, 16.0f, CornerRadii.internalMethod03908(3.0f), colorRGBA);
        iII.drawCenteredText(Fonts.internalField1154.internalMethod01432(7.0f), nestedValue2026.internalMethod00534(), f + f3 / 2.0f, f2 + (16.0f - Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890()) / 2.0f, bl ? ThemeColors.internalMethod01303(colorRGBA) : ThemeColors.internalMethod08459());
    }

    private float internalMethod02211(float f) {
        return (f - 20.0f - 4.0f * (float)(this.internalField0079.length - 1)) / (float)this.internalField0079.length;
    }

    @Generated
    public float internalMethod06944() {
        return this.internalField0205;
    }

    @Generated
    public DonItemCatalog.InternalType0189 internalMethod05474() {
        return this.internalField0406;
    }
}

