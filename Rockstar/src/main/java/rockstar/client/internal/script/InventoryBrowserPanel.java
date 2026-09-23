package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.InventoryTabBar;
import rockstar.client.internal.ui.InventoryCategoryPanel;
import rockstar.client.internal.ui.InventorySearchField;
import rockstar.client.internal.inventory.AuctionItemSelection;
import rockstar.client.internal.inventory.DonItemCatalog;
import rockstar.client.ui.ThemeColors;
import rockstar.client.MinecraftClientAccess;

public class InventoryBrowserPanel
implements MinecraftClientAccess {
    private float internalField0206;
    private float internalField1048;
    private float internalField1047;
    private float internalField1049;
    private final InventoryTabBar internalField0805 = new InventoryTabBar();
    private final InventorySearchField internalField0009 = new InventorySearchField();
    private final InventoryCategoryPanel internalField0008 = new InventoryCategoryPanel();
    private final AuctionItemSelection internalField0010 = new AuctionItemSelection();
    private List<DonItemCatalog.InternalType0503> internalField0416 = new ArrayList<DonItemCatalog.InternalType0503>();
    private List<DonItemCatalog.InternalType0503> internalField0417 = new ArrayList<DonItemCatalog.InternalType0503>();
    private ItemStack internalField0878;

    public InventoryBrowserPanel() {
        this.internalMethod00721();
    }

    public String internalMethod00457() {
        return this.internalField0010.internalMethod05808();
    }

    public String internalMethod05162() {
        return this.internalField0010.internalMethod02358();
    }

    private void internalMethod00721() {
        this.internalField0416 = DonItemCatalog.internalMethod02566(this.internalField0805.internalMethod05474());
        this.internalField0417 = this.internalField0009.internalMethod02786(this.internalField0416);
        this.internalField0008.internalMethod02153();
    }

    public float internalMethod00717() {
        float f = 30.0f;
        float f2 = 36.0f;
        return 20.0f + f + 4.0f * f2;
    }

    public void internalMethod00718() {
        this.internalField0010.internalMethod07084();
        this.internalField0878 = null;
    }

    public void internalMethod07147(ItemStack itemStack) {
        this.internalField0010.internalMethod00962(itemStack);
        this.internalField0878 = this.internalField0010.internalMethod05876();
    }

    public void internalMethod04970(UiRenderContext iII, float f, float f2, float f3, float f4) {
        this.internalField0206 = f;
        this.internalField1048 = f2;
        this.internalField1047 = f3;
        this.internalField1049 = f4;
        this.internalMethod06212(iII, f, f2, f3, f4);
        float f5 = f2 + 10.0f;
        this.internalField0805.internalMethod01378(iII, f, f5, f3);
        float f6 = f5 + this.internalField0805.internalMethod06944() + 4.0f;
        this.internalField0009.internalMethod03534(this.internalField0805.internalMethod05474() == DonItemCatalog.InternalType0189.internalField0406);
        this.internalField0009.internalMethod04235(iII, f, f6, f3);
        float f7 = f6 + this.internalField0009.internalMethod03880() + (this.internalField0009.internalMethod03881() ? 4.0f : 10.0f);
        float f8 = f4 - (f7 - f2);
        this.internalField0008.internalMethod03718(iII, this.internalField0417, this.internalField0010, f, f7, f3, f8);
        this.internalField0878 = this.internalField0010.internalMethod05876();
    }

    public void internalMethod07064(double d, double d2, MouseButton typedParameter1015) {
        if (typedParameter1015 != MouseButton.internalField0102) {
            return;
        }
        float f = this.internalField1048 + 10.0f;
        if (this.internalField0805.internalMethod05280(d, d2, this.internalField0206, f, this.internalField1047)) {
            this.internalMethod00721();
            this.internalMethod00718();
            return;
        }
        float f2 = f + this.internalField0805.internalMethod06944() + 4.0f;
        this.internalField0009.internalMethod00537(d, d2, typedParameter1015);
        float f3 = f2 + this.internalField0009.internalMethod03880() + (this.internalField0009.internalMethod03881() ? 4.0f : 10.0f);
        float f4 = this.internalField1049 - (f3 - this.internalField1048);
        this.internalField0008.internalMethod04238(d, d2, this.internalField0417, this.internalField0010, this.internalField0206, f3, f4);
        this.internalField0878 = this.internalField0010.internalMethod05876();
    }

    public void internalMethod01976(double d, double d2, double d3) {
        this.internalField0008.internalMethod06912(d, d2, d3, this.internalField0206, this.internalField1048, this.internalField1047, this.internalField1049);
    }

    public void internalMethod01983(int n, int n2, int n3) {
        this.internalField0009.internalMethod04561(n, n2, n3);
        this.internalField0417 = this.internalField0009.internalMethod02786(this.internalField0416);
    }

    public void internalMethod04074(char c, int n) {
        this.internalField0009.internalMethod02594(c, n);
        this.internalField0417 = this.internalField0009.internalMethod02786(this.internalField0416);
    }

    public void internalMethod00304(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0009.internalMethod01878(d, d2, typedParameter1015);
    }

    private void internalMethod06212(UiRenderContext iII, float f, float f2, float f3, float f4) {
        iII.drawBlurredRect(f, f2, f3, f4, 45.0f, 5.0f, CornerRadii.internalMethod03908(8.0f), ColorRGBA.WHITE.withAlpha(255.0f));
        iII.drawSquircle(f, f2, f3, f4, 2.0f, CornerRadii.internalMethod03908(8.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * ThemeColors.internalMethod02435().internalMethod08704()));
    }

    @Generated
    public ItemStack internalMethod05082() {
        return this.internalField0878;
    }
}

