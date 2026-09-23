package rockstar.client.internal.ui;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.List;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.internal.script.ItemCellRenderer;
import rockstar.client.internal.inventory.AuctionItemSelection;
import rockstar.client.internal.inventory.DonItemCatalog;
import rockstar.client.ui.ThemeColors;
import rockstar.client.ui.UiUtils;
import rockstar.client.render.ScissorStack;

public class InventoryCategoryPanel {
    private float internalField1049 = 0.0f;
    private float internalField1046 = 0.0f;

    public void internalMethod03718(UiRenderContext iII, List<DonItemCatalog.InternalType0503> list, AuctionItemSelection typedValue090, float f, float f2, float f3, float f4) {
        ScissorStack.internalMethod06303(iII.getMatrices(), f, f2, f3, f4);
        float f5 = f2 - this.internalField1049;
        float f6 = 0.0f;
        boolean bl = false;
        for (DonItemCatalog.InternalType0503 nestedValue2066 : list) {
            int n = this.internalMethod02514(nestedValue2066, typedValue090);
            if (n == 0) continue;
            bl = true;
            if (f5 > f2 + f4) {
                f6 += 12.0f + this.internalMethod06736(n);
                continue;
            }
            if (f5 + 12.0f >= f2) {
                iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), nestedValue2066.internalMethod00725(), f + 10.0f, f5, ThemeColors.internalMethod08459().mulAlpha(0.9f));
            }
            f5 += 12.0f;
            f5 = this.internalMethod05842(iII, nestedValue2066, typedValue090, f5, f, f2, f4);
            f6 = f5 - (f2 - this.internalField1049);
        }
        if (!bl) {
            String string = "\u041d\u0435\u0442 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u043e\u0432";
            float f7 = Fonts.internalField1154.internalMethod01432(8.0f).internalMethod00965(string);
            float f8 = f + (f3 - f7) / 2.0f;
            float f9 = f2 + (f4 - Fonts.internalField1154.internalMethod01432(8.0f).internalMethod04890()) / 2.0f;
            iII.drawText(Fonts.internalField1154.internalMethod01432(8.0f), string, f8, f9, ThemeColors.internalField1312.withAlpha(0.7f));
        }
        this.internalField1046 = Math.max(0.0f, f6 - f4);
        this.internalField1049 = Math.max(0.0f, Math.min(this.internalField1049, this.internalField1046));
        ScissorStack.internalMethod07643();
    }

    public void internalMethod04238(double d, double d2, List<DonItemCatalog.InternalType0503> list, AuctionItemSelection typedValue090, float f, float f2, float f3) {
        if (d2 < (double)f2 || d2 > (double)(f2 + f3)) {
            return;
        }
        float f4 = f2 - this.internalField1049;
        for (DonItemCatalog.InternalType0503 nestedValue2066 : list) {
            if (this.internalMethod02514(nestedValue2066, typedValue090) == 0) continue;
            if (this.internalMethod04419(nestedValue2066, typedValue090, d, d2, f4 += 12.0f, f)) {
                return;
            }
            f4 += this.internalMethod06736(this.internalMethod02514(nestedValue2066, typedValue090));
        }
    }

    public void internalMethod06912(double d, double d2, double d3, float f, float f2, float f3, float f4) {
        if (UiUtils.internalMethod05785(f, f2, f3, f4, d, d2)) {
            this.internalField1049 = Math.max(0.0f, Math.min(this.internalField1049 - (float)d3 * 20.0f, this.internalField1046));
        }
    }

    public void internalMethod02153() {
        this.internalField1049 = 0.0f;
        this.internalField1046 = 0.0f;
    }

    private float internalMethod05842(UiRenderContext iII, DonItemCatalog.InternalType0503 nestedValue2066, AuctionItemSelection typedValue090, float f, float f2, float f3, float f4) {
        int n = 0;
        int n2 = 0;
        for (DonItemCatalog.InternalType0190 nestedValue2027 : nestedValue2066.internalMethod07260()) {
            if (typedValue090.internalMethod03954(nestedValue2027)) continue;
            int n3 = n % 5;
            int n4 = n / 5;
            n2 = Math.max(n2, n4);
            float f5 = f2 + 10.0f + (float)n3 * 36.0f;
            float f6 = f + (float)n4 * 36.0f;
            if (f6 + 34.0f >= f3 && f6 <= f3 + f4) {
                ItemCellRenderer.internalMethod00722(iII, Fonts.internalField1154.internalMethod01432(6.0f), nestedValue2027, f5, f6, 30.0f, 34.0f, typedValue090.internalMethod03773(nestedValue2027));
            }
            ++n;
        }
        return f + (float)(n2 + 1) * 36.0f + 8.0f;
    }

    private boolean internalMethod04419(DonItemCatalog.InternalType0503 nestedValue2066, AuctionItemSelection typedValue090, double d, double d2, float f, float f2) {
        int n = 0;
        for (DonItemCatalog.InternalType0190 nestedValue2027 : nestedValue2066.internalMethod07260()) {
            if (typedValue090.internalMethod03954(nestedValue2027)) continue;
            int n2 = n % 5;
            float f3 = f2 + 10.0f + (float)n2 * 36.0f;
            int n3 = n / 5;
            float f4 = f + (float)n3 * 36.0f;
            if (UiUtils.internalMethod05785(f3, f4, 30.0, 34.0, d, d2)) {
                typedValue090.internalMethod03772(nestedValue2027);
                return true;
            }
            ++n;
        }
        return false;
    }

    private int internalMethod02514(DonItemCatalog.InternalType0503 nestedValue2066, AuctionItemSelection typedValue090) {
        int n = 0;
        for (DonItemCatalog.InternalType0190 nestedValue2027 : nestedValue2066.internalMethod07260()) {
            if (typedValue090.internalMethod03954(nestedValue2027)) continue;
            ++n;
        }
        return n;
    }

    private float internalMethod06736(int n) {
        int n2 = (n + 5 - 1) / 5;
        return (float)n2 * 36.0f + 8.0f;
    }
}

