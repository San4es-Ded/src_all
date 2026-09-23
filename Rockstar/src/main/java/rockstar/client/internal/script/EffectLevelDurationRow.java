package rockstar.client.internal.script;






import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.function.BiConsumer;
import lombok.Generated;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.entry.RegistryEntry;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.ui.UiRenderContext;
import rockstar.client.render.Fonts;
import rockstar.client.render.CornerRadii;
import rockstar.client.ui.ThemeColors;
import rockstar.client.internal.ui.RoundedTextFieldWidget;
import rockstar.client.internal.script.ItemSettingsPopup;
import rockstar.client.internal.ui.ItemSettingsElement;
import rockstar.client.internal.core.EditableNumericRow;
import rockstar.client.internal.core.NumericFieldHelper;

public class EffectLevelDurationRow
implements ItemSettingsElement,
EditableNumericRow {
    private final String internalField0248;
    private final RegistryEntry<StatusEffect> internalField0151;
    private final RoundedTextFieldWidget internalField0762;
    private final RoundedTextFieldWidget internalField0761;
    private final BiConsumer<Integer, Integer> internalField0048;
    private boolean internalField0277;
    private boolean internalField0276;
    private int internalField0227;
    private int internalField0228;

    public EffectLevelDurationRow(String string, RegistryEntry<StatusEffect> registryEntry, int n, int n2, BiConsumer<Integer, Integer> biConsumer) {
        this.internalField0248 = string;
        this.internalField0151 = registryEntry;
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField0048 = biConsumer;
        this.internalField0277 = n >= 0 && n2 > 0;
        this.internalField0762 = new RoundedTextFieldWidget("");
        this.internalField0762.internalMethod03483(String.valueOf(Math.max(0, n + 1)));
        this.internalField0761 = new RoundedTextFieldWidget("");
        this.internalField0761.internalMethod03483(String.valueOf(n2));
    }

    @Override
    public float internalMethod05812() {
        if (!this.internalField0277 || !this.internalField0276) {
            return 20.0f;
        }
        return 56.0f;
    }

    @Override
    public void internalMethod05576(UiRenderContext iII, float f, float f2, float f3) {
        ColorRGBA colorRGBA = this.internalField0277 ? ThemeColors.internalMethod02531().withAlpha(50.0f) : ThemeColors.internalMethod08573();
        iII.drawRoundedRect(f, f2, f3, 17.0f, CornerRadii.internalMethod03908(3.0f), colorRGBA);
        iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), this.internalField0248, f + 6.0f, f2 + 6.0f, ThemeColors.internalMethod08459());
        if (this.internalField0277 && this.internalField0276) {
            float f4 = f2 + 20.0f;
            iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), "\u0423\u0440\u043e\u0432\u0435\u043d\u044c", f + 4.0f, f4 + 4.0f, ThemeColors.internalMethod08459().withAlpha(180.0f));
            this.internalField0762.internalMethod07039(iII, f + f3 - 30.0f, f4, 26.0f, 14.0f);
            iII.drawText(Fonts.internalField1154.internalMethod01432(7.0f), "\u0414\u043b\u0438\u0442\u0435\u043b\u044c\u043d\u043e\u0441\u0442\u044c", f + 4.0f, (f4 += 18.0f) + 4.0f, ThemeColors.internalMethod08459().withAlpha(180.0f));
            this.internalField0761.internalMethod07039(iII, f + f3 - 30.0f, f4, 26.0f, 14.0f);
        }
    }

    @Override
    public boolean internalMethod06574(ItemSettingsPopup typedValue097, double d, double d2, int n) {
        if (this.internalField0276) {
            if (this.internalField0762.internalMethod05028(d, d2)) {
                this.internalField0762.internalMethod06527(true);
                this.internalField0761.internalMethod06527(false);
                return true;
            }
            if (this.internalField0761.internalMethod05028(d, d2)) {
                this.internalField0761.internalMethod06527(true);
                this.internalField0762.internalMethod06527(false);
                return true;
            }
        }
        if (n == 1) {
            if (!this.internalField0277) {
                return false;
            }
            if (this.internalField0276) {
                this.internalMethod05714();
            }
            this.internalField0276 = !this.internalField0276;
            return true;
        }
        if (n == 0) {
            if (this.internalField0276) {
                this.internalMethod05714();
                this.internalField0276 = false;
            }
            if (this.internalField0277) {
                this.internalField0277 = false;
                this.internalField0227 = -1;
                this.internalField0228 = 0;
                this.internalField0048.accept(-1, 0);
            } else {
                this.internalField0277 = true;
                this.internalField0227 = 0;
                this.internalField0228 = 600;
                this.internalField0762.internalMethod03483("1");
                this.internalField0761.internalMethod03483("600");
                this.internalField0048.accept(this.internalField0227, this.internalField0228);
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean internalMethod00940() {
        return this.internalField0762.internalMethod06300() || this.internalField0761.internalMethod06300();
    }

    @Override
    public void internalMethod00942() {
        if (this.internalMethod00940()) {
            this.internalMethod05714();
            this.internalField0048.accept(this.internalField0227, this.internalField0228);
            this.internalField0762.internalMethod06527(false);
            this.internalField0761.internalMethod06527(false);
        }
    }

    public void internalMethod05714() {
        NumericFieldHelper.internalMethod06060(this.internalField0762, this.internalField0227, NumericFieldHelper.internalMethod05399(1, 256), NumericFieldHelper.internalMethod02618(0), n -> {
            this.internalField0227 = n - 1;
        });
        NumericFieldHelper.internalMethod01391(this.internalField0761, this.internalField0228, NumericFieldHelper.internalMethod06031(1), n -> {
            this.internalField0228 = n;
        });
    }

    @Generated
    public String internalMethod07250() {
        return this.internalField0248;
    }

    @Generated
    public RegistryEntry<StatusEffect> internalMethod05512() {
        return this.internalField0151;
    }

    @Generated
    public RoundedTextFieldWidget internalMethod03675() {
        return this.internalField0762;
    }

    @Generated
    public RoundedTextFieldWidget internalMethod05066() {
        return this.internalField0761;
    }

    @Generated
    public BiConsumer<Integer, Integer> internalMethod04171() {
        return this.internalField0048;
    }

    @Generated
    public boolean internalMethod05718() {
        return this.internalField0277;
    }

    @Generated
    public boolean internalMethod08248() {
        return this.internalField0276;
    }

    @Generated
    public int internalMethod05713() {
        return this.internalField0227;
    }

    @Generated
    public int internalMethod05717() {
        return this.internalField0228;
    }
}

