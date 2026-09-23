package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.data.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.MouseButton;
import rockstar.client.internal.script.CustomTextField;
import rockstar.client.data.AuctionItem;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.inventory.PotionItemUtils;
import rockstar.client.ui.UiUtils;

public class AuctionListingEditor {
    private final float internalField0205 = 10.0f;
    private final float internalField0206 = 74.0f;
    private ItemStack internalField0878;
    private ItemStack internalField0879;
    private String internalField0248;
    private float internalField1048;
    private float internalField1047;
    private float internalField1049;
    private float internalField1046;
    private final CustomTextField internalField0936 = new CustomTextField(Fonts.internalField1154.internalMethod01432(7.0f));
    private final CustomTextField internalField0935 = new CustomTextField(Fonts.internalField1154.internalMethod01432(7.0f));
    private final CustomTextField internalField1391 = new CustomTextField(Fonts.internalField1154.internalMethod01432(7.0f));
    private boolean internalField0277;
    private AuctionItem.InternalType0158 internalField0592 = AuctionItem.InternalType0158.internalField0592;

    public AuctionListingEditor() {
        this.internalField0936.internalMethod07563(true);
        this.internalField0936.internalMethod09000("\u041c\u0430\u043a\u0441");
        this.internalField0936.internalMethod00484("25000");
        this.internalField0935.internalMethod07563(true);
        this.internalField0935.internalMethod09000("\u0423\u0440\u043e\u0432\u0435\u043d\u044c");
        this.internalField0935.internalMethod00484("1");
        this.internalField1391.internalMethod07563(true);
        this.internalField1391.internalMethod09000("\u041f\u0440\u043e\u0446\u0435\u043d\u0442");
        this.internalField1391.internalMethod00484("20");
    }

    public long internalMethod03727() {
        return this.internalMethod05783(this.internalField0936.internalMethod06202());
    }

    public double internalMethod03725() {
        String string = this.internalField1391.internalMethod06202();
        if (string == null || string.trim().isEmpty()) {
            return 20.0;
        }
        try {
            return Double.parseDouble(string.trim());
        }
        catch (NumberFormatException numberFormatException) {
            return 20.0;
        }
    }

    public ItemStack internalMethod04458() {
        if (this.internalField0878 == null) {
            return null;
        }
        if (!PotionItemUtils.internalMethod02701(this.internalField0878)) {
            return this.internalField0878;
        }
        String string = this.internalField0935.internalMethod06202();
        if (string == null || string.trim().isEmpty()) {
            return this.internalField0878;
        }
        try {
            int n = Integer.parseInt(string.trim()) - 1;
            if (n < 0) {
                n = 0;
            }
            return PotionItemUtils.internalMethod06437(this.internalField0878, n);
        }
        catch (NumberFormatException numberFormatException) {
            return this.internalField0878;
        }
    }

    private long internalMethod05783(String string) {
        if (string == null) {
            return 0L;
        }
        String string2 = string.trim().toLowerCase(Locale.ROOT);
        if (string2.isEmpty()) {
            return 0L;
        }
        try {
            return Long.parseLong(string2);
        }
        catch (NumberFormatException numberFormatException) {
            return 0L;
        }
    }

    public void internalMethod02590(int n, int n2, int n3) {
        this.internalField0936.internalMethod05727(n, n2, n3);
        this.internalField0935.internalMethod05727(n, n2, n3);
        this.internalField1391.internalMethod05727(n, n2, n3);
    }

    public void internalMethod04096(char c, int n) {
        this.internalField0936.internalMethod05413(c, n);
        this.internalField0935.internalMethod05413(c, n);
        this.internalField1391.internalMethod05413(c, n);
    }

    public void internalMethod04771(double d, double d2, MouseButton typedParameter1015) {
        this.internalField0936.internalMethod02863(d, d2, typedParameter1015);
        this.internalField0935.internalMethod02863(d, d2, typedParameter1015);
        this.internalField1391.internalMethod02863(d, d2, typedParameter1015);
    }

    public void internalMethod02528(UiRenderContext iII, float f, float f2, float f3, float f4) {
        this.internalField1048 = f;
        this.internalField1047 = f2;
        this.internalField1049 = f3;
        this.internalField1046 = f4;
        this.internalMethod03863(iII, f, f2, f3, f4);
        if (this.internalField0878 != null) {
            if (this.internalField0878 != this.internalField0879 && PotionItemUtils.internalMethod02701(this.internalField0878)) {
                this.internalField0935.internalMethod00484(String.valueOf(PotionItemUtils.internalMethod02700(this.internalField0878) + 1));
                this.internalField0879 = this.internalField0878;
            }
            this.internalMethod01126(iII);
            float f5 = this.internalMethod01125(iII);
            f5 = this.internalField0592 == AuctionItem.InternalType0158.internalField0592 ? this.internalMethod07089(iII, f5) : this.internalMethod00886(iII, f5);
            if (PotionItemUtils.internalMethod02701(this.internalField0878)) {
                f5 = this.internalMethod08170(iII, f5);
            }
            this.internalMethod07090(iII, f5);
        }
    }

    private void internalMethod01126(UiRenderContext iII) {
        float f = this.internalField1048 + 10.0f;
        float f2 = this.internalField1047 + 10.0f;
        String string = this.internalField0248 != null ? this.internalField0248 : this.internalField0878.getName().getString();
        iII.drawItem(this.internalField0878, f, f2, 0.875f);
        iII.drawText(Fonts.internalField1154.internalMethod01432(8.0f), string, f + 18.0f, f2 + 4.0f, ThemeColors.internalMethod08459());
    }

    private float internalMethod01125(UiRenderContext iII) {
        float f = this.internalField1048 + 10.0f;
        float f2 = this.internalField1047 + 34.0f;
        float f3 = (this.internalField1049 - 20.0f - 4.0f) / 2.0f;
        float f4 = 16.0f;
        ColorRGBA colorRGBA = new ColorRGBA(255.0f, 1.0f, 1.0f);
        ColorRGBA colorRGBA2 = ThemeColors.internalMethod08573().mulAlpha(0.6f);
        iII.drawRoundedRect(f, f2, f3, f4, CornerRadii.internalMethod03908(3.0f), this.internalField0592 == AuctionItem.InternalType0158.internalField0592 ? colorRGBA : colorRGBA2);
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), "\u0424\u0438\u043a\u0441", f + 8.0f, f2 + 5.0f, ThemeColors.internalMethod08459());
        iII.drawRoundedRect(f + f3 + 4.0f, f2, f3, f4, CornerRadii.internalMethod03908(3.0f), this.internalField0592 == AuctionItem.InternalType0158.internalField0591 ? colorRGBA : colorRGBA2);
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), "\u0420\u044b\u043d\u043e\u043a", f + f3 + 4.0f + 6.0f, f2 + 5.0f, ThemeColors.internalMethod08459());
        return f2 + f4;
    }

    private float internalMethod07089(UiRenderContext iII, float f) {
        float f2 = this.internalField1048 + 10.0f;
        float f3 = this.internalField1049 - 20.0f;
        float f4 = f + 10.0f;
        float f5 = f4 + Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890() + 4.0f;
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), "\u041c\u0430\u043a\u0441 \u0446\u0435\u043d\u0430", f2, f4, ThemeColors.internalMethod08459().mulAlpha(0.8f));
        iII.drawRoundedRect(f2, f5, f3, 14.0f, CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod08573().mulAlpha(0.6f));
        this.internalField0936.internalMethod05191(f2 + 2.0f, f5, f3 - 2.0f, 14.0f);
        this.internalField0936.internalMethod00143(ThemeColors.internalMethod08459());
        this.internalField0936.internalMethod03398(iII);
        return f5 + 14.0f;
    }

    private float internalMethod00886(UiRenderContext iII, float f) {
        float f2 = this.internalField1048 + 10.0f;
        float f3 = this.internalField1049 - 20.0f;
        float f4 = f + 10.0f;
        float f5 = f4 + Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890() + 4.0f;
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), "\u041d\u0438\u0436\u0435 \u0440\u044b\u043d\u043a\u0430 %", f2, f4, ThemeColors.internalMethod08459().mulAlpha(0.8f));
        iII.drawRoundedRect(f2, f5, f3, 14.0f, CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod08573().mulAlpha(0.6f));
        this.internalField1391.internalMethod05191(f2 + 2.0f, f5, f3 - 2.0f, 14.0f);
        this.internalField1391.internalMethod00143(ThemeColors.internalMethod08459());
        this.internalField1391.internalMethod03398(iII);
        return f5 + 14.0f;
    }

    private float internalMethod08170(UiRenderContext iII, float f) {
        float f2 = this.internalField1048 + 10.0f;
        float f3 = this.internalField1049 - 20.0f;
        float f4 = f + 10.0f;
        float f5 = f4 + Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890() + 4.0f;
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), "\u0423\u0440\u043e\u0432\u0435\u043d\u044c \u0437\u0435\u043b\u044c\u044f", f2, f4, ThemeColors.internalMethod08459().mulAlpha(0.8f));
        iII.drawRoundedRect(f2, f5, f3, 14.0f, CornerRadii.internalMethod03908(3.0f), ThemeColors.internalMethod08573().mulAlpha(0.6f));
        this.internalField0935.internalMethod05191(f2 + 4.0f, f5, f3 - 8.0f, 14.0f);
        this.internalField0935.internalMethod00143(ThemeColors.internalMethod08459());
        this.internalField0935.internalMethod03398(iII);
        return f5 + 14.0f;
    }

    private void internalMethod07090(UiRenderContext iII, float f) {
        float f2 = this.internalField1048 + 10.0f;
        float f3 = f + 15.0f;
        iII.drawRoundedRect(f2, f3, 74.0f, 16.0f, CornerRadii.internalMethod03908(2.0f), new ColorRGBA(255.0f, 1.0f, 1.0f));
        iII.drawText(Fonts.internalField1154.internalMethod01432(8.0f), "\u0414\u043e\u0431\u0430\u0432\u0438\u0442\u044c", f2 + 10.0f, f3 + 4.0f, ThemeColors.internalMethod08459());
    }

    public void internalMethod06033(double d, double d2, MouseButton typedParameter1015) {
        if (typedParameter1015 != MouseButton.internalField0102 || this.internalField0878 == null) {
            return;
        }
        float f = this.internalField1048 + 10.0f;
        float f2 = this.internalField1047 + 34.0f;
        float f3 = (this.internalField1049 - 20.0f - 4.0f) / 2.0f;
        float f4 = 16.0f;
        if (UiUtils.internalMethod05785(f, f2, f3, f4, d, d2)) {
            this.internalField0592 = AuctionItem.InternalType0158.internalField0592;
            return;
        }
        if (UiUtils.internalMethod05785(f + f3 + 4.0f, f2, f3, f4, d, d2)) {
            this.internalField0592 = AuctionItem.InternalType0158.internalField0591;
            return;
        }
        this.internalField0936.internalMethod01643(d, d2, typedParameter1015);
        this.internalField1391.internalMethod01643(d, d2, typedParameter1015);
        if (PotionItemUtils.internalMethod02701(this.internalField0878)) {
            this.internalField0935.internalMethod01643(d, d2, typedParameter1015);
        }
        if (this.internalMethod04097(d, d2)) {
            this.internalField0277 = true;
        }
    }

    private boolean internalMethod04097(double d, double d2) {
        float f = this.internalField1047 + 34.0f + 16.0f + 10.0f + Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890() + 4.0f + 14.0f;
        if (PotionItemUtils.internalMethod02701(this.internalField0878)) {
            f += 10.0f + Fonts.internalField1154.internalMethod01432(7.0f).internalMethod04890() + 4.0f + 14.0f;
        }
        float f2 = this.internalField1048 + 10.0f;
        float f3 = f + 15.0f;
        return UiUtils.internalMethod05785(f2, f3, 74.0, 16.0, d, d2);
    }

    public boolean internalMethod03728() {
        boolean bl = this.internalField0277;
        this.internalField0277 = false;
        return bl;
    }

    private void internalMethod03863(UiRenderContext iII, float f, float f2, float f3, float f4) {
        iII.drawBlurredRect(f, f2, f3, f4, 45.0f, 5.0f, CornerRadii.internalMethod03908(8.0f), ColorRGBA.WHITE.withAlpha(255.0f));
        iII.drawSquircle(f, f2, f3, f4, 2.0f, CornerRadii.internalMethod03908(8.0f), ThemeColors.internalMethod07738().withAlpha(255.0f * ThemeColors.internalMethod02435().internalMethod08704()));
    }

    @Generated
    public float internalMethod03726() {
        return this.internalField0205;
    }

    @Generated
    public float internalMethod03732() {
        return this.internalField0206;
    }

    @Generated
    public ItemStack internalMethod02262() {
        return this.internalField0878;
    }

    @Generated
    public ItemStack internalMethod08082() {
        return this.internalField0879;
    }

    @Generated
    public String internalMethod00635() {
        return this.internalField0248;
    }

    @Generated
    public float internalMethod09091() {
        return this.internalField1048;
    }

    @Generated
    public float internalMethod09092() {
        return this.internalField1047;
    }

    @Generated
    public float internalMethod09097() {
        return this.internalField1049;
    }

    @Generated
    public float internalMethod09098() {
        return this.internalField1046;
    }

    @Generated
    public CustomTextField internalMethod06932() {
        return this.internalField0936;
    }

    @Generated
    public CustomTextField internalMethod07627() {
        return this.internalField0935;
    }

    @Generated
    public CustomTextField internalMethod09017() {
        return this.internalField1391;
    }

    @Generated
    public boolean internalMethod03733() {
        return this.internalField0277;
    }

    @Generated
    public AuctionItem.InternalType0158 internalMethod04120() {
        return this.internalField0592;
    }

    @Generated
    public void internalMethod04862(ItemStack itemStack) {
        this.internalField0878 = itemStack;
    }

    @Generated
    public void internalMethod01899(ItemStack itemStack) {
        this.internalField0879 = itemStack;
    }

    @Generated
    public void internalMethod05784(String string) {
        this.internalField0248 = string;
    }

    @Generated
    public void internalMethod07270(float f) {
        this.internalField1048 = f;
    }

    @Generated
    public void internalMethod07339(float f) {
        this.internalField1047 = f;
    }

    @Generated
    public void internalMethod08473(float f) {
        this.internalField1049 = f;
    }

    @Generated
    public void internalMethod08479(float f) {
        this.internalField1046 = f;
    }

    @Generated
    public void internalMethod07271(boolean bl) {
        this.internalField0277 = bl;
    }

    @Generated
    public void internalMethod07349(AuctionItem.InternalType0158 nestedValue2021) {
        this.internalField0592 = nestedValue2021;
    }
}

