package rockstar.client.internal.ui;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.script.ItemSettingsPopup;
import rockstar.client.internal.ui.ItemSettingsElement;
import rockstar.client.internal.game.SmoothScrollHandler;
import rockstar.client.render.ScissorStack;

public class ItemSettingsCategory
implements ItemSettingsElement {
    private final String internalField0248;
    private final ItemStack internalField0878;
    private final List<ItemSettingsElement> internalField0416 = new ArrayList<ItemSettingsElement>();
    private boolean internalField0277;
    private float internalField0205;
    private float internalField0206;
    private float internalField1048;
    private final SmoothScrollHandler internalField0004 = new SmoothScrollHandler();

    public ItemSettingsCategory(String string, ItemStack itemStack) {
        this.internalField0248 = string;
        this.internalField0878 = itemStack;
    }

    public ItemSettingsCategory(String string) {
        this(string, new ItemStack((ItemConvertible)Items.ENCHANTED_BOOK));
    }

    public void internalMethod05394(ItemSettingsElement typedValue098) {
        this.internalField0416.add(typedValue098);
    }

    @Override
    public float internalMethod05812() {
        if (!this.internalField0277) {
            return 20.0f;
        }
        float f = 0.0f;
        for (ItemSettingsElement typedValue098 : this.internalField0416) {
            f += typedValue098.internalMethod05812();
        }
        return 20.0f + Math.min(f, 100.0f);
    }

    @Override
    public void internalMethod05576(UiRenderContext iII, float f, float f2, float f3) {
        this.internalField0205 = f2;
        this.internalField0206 = f;
        this.internalField1048 = f3;
        this.internalMethod03156(iII, f, f2, f3);
        if (this.internalField0277 && !this.internalField0416.isEmpty()) {
            float f4 = f2 + 23.0f;
            float f5 = 100.0f;
            float f6 = this.internalMethod08960();
            this.internalField0004.internalMethod04308(-(f6 - f5));
            this.internalField0004.internalMethod02322();
            float f7 = Math.min(f6, f5);
            ScissorStack.internalMethod02389(f, f4, f3, f7);
            float f8 = f4 - (float)this.internalField0004.internalMethod02321();
            for (ItemSettingsElement typedValue098 : this.internalField0416) {
                typedValue098.internalMethod05576(iII, f, f8, f3);
                f8 += typedValue098.internalMethod05812();
            }
            ScissorStack.internalMethod07643();
        }
    }

    private void internalMethod03156(UiRenderContext iII, float f, float f2, float f3) {
        iII.drawRoundedRect(f, f2, f3, 20.0f, CornerRadii.internalMethod03908(4.0f), ThemeColors.internalMethod08573());
        iII.getMatrices().pushMatrix();
        iII.getMatrices().translate(0.0f, 0.0f);
        iII.drawBatchItem(this.internalField0878, f + 4.0f, f2 + 4.0f, 0.75f);
        iII.getMatrices().popMatrix();
        iII.drawText(Fonts.internalField0449.internalMethod01432(7.0f), this.internalField0248, f + 22.0f, f2 + 8.0f, ThemeColors.internalMethod08459());
    }

    private float internalMethod08960() {
        float f = 0.0f;
        for (ItemSettingsElement typedValue098 : this.internalField0416) {
            f += typedValue098.internalMethod05812();
        }
        return f;
    }

    public void internalMethod06557(double d) {
        if (this.internalField0277) {
            this.internalField0004.internalMethod04249(d);
        }
    }

    @Override
    public boolean internalMethod06574(ItemSettingsPopup typedValue097, double d, double d2, int n) {
        if (d2 < (double)(this.internalField0205 + 20.0f)) {
            this.internalField0277 = !this.internalField0277;
            return true;
        }
        if (this.internalField0277) {
            float f = this.internalField0205 + 20.0f - (float)this.internalField0004.internalMethod02321();
            for (ItemSettingsElement typedValue098 : this.internalField0416) {
                if (d2 >= (double)f && d2 < (double)(f + typedValue098.internalMethod05812())) {
                    return typedValue098.internalMethod06574(typedValue097, d, d2, n);
                }
                f += typedValue098.internalMethod05812();
            }
        }
        return true;
    }

    @Generated
    public String internalMethod07051() {
        return this.internalField0248;
    }

    @Generated
    public ItemStack internalMethod00640() {
        return this.internalField0878;
    }

    @Generated
    public List<ItemSettingsElement> internalMethod06935() {
        return this.internalField0416;
    }

    @Generated
    public boolean internalMethod02650() {
        return this.internalField0277;
    }

    @Generated
    public float internalMethod02652() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod08948() {
        return this.internalField0206;
    }

    @Generated
    public float internalMethod08950() {
        return this.internalField1048;
    }

    @Generated
    public SmoothScrollHandler internalMethod03655() {
        return this.internalField0004;
    }
}

