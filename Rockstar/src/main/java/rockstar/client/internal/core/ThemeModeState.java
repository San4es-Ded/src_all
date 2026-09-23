package rockstar.client.internal.core;



import rockstar.client.internal.script.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.internal.script.ThemeVariant;

public class ThemeModeState {
    public ThemeVariant internalField0395 = ThemeVariant.internalField0395;

    public final void internalMethod05591() {
        this.internalField0395 = this.internalField0395 == ThemeVariant.internalField0395 ? ThemeVariant.internalField0394 : ThemeVariant.internalField0395;
    }

    public final ThemeVariant internalMethod05065() {
        return ThemeVariant.internalField0395;
    }

    @Generated
    public void internalMethod05953(ThemeVariant typedValue185) {
        this.internalField0395 = typedValue185;
    }
}
