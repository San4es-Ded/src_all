package rockstar.client.internal.framework;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.animation.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.animation.AnimatedValue;
import rockstar.client.animation.Easing;
import rockstar.client.util.Stopwatch;

public class AnimatedStatusEntry
extends MultiSelectSetting.InternalType0091 {
    private String internalField0248 = "?";
    private String internalField0247 = "";
    private final String internalField1077;
    private boolean internalField0277;
    private final Stopwatch internalField0519 = new Stopwatch();
    private final AnimatedValue internalField0808 = new AnimatedValue(300L, 0.0f, Easing.internalField0812);
    private final AnimatedValue internalField0809 = new AnimatedValue(500L, 0.0f, Easing.internalField1327);

    public AnimatedStatusEntry(MultiSelectSetting typedValue173, String string, String string2) {
        super(typedValue173, string);
        this.select();
        this.internalField1077 = " " + string2;
    }

    public AnimatedStatusEntry(MultiSelectSetting typedValue173, String string) {
        super(typedValue173, string);
        this.select();
        this.internalField1077 = "";
    }

    public void internalMethod05700(String string, String string2) {
        if (!this.isSelected()) {
            return;
        }
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    public void internalMethod05945(String string) {
        if (!this.isSelected()) {
            return;
        }
        this.internalField0248 = string;
    }

    @Generated
    public String internalMethod06899() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod03430() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod07883() {
        return this.internalField1077;
    }

    @Generated
    public boolean internalMethod05290() {
        return this.internalField0277;
    }

    @Generated
    public Stopwatch internalMethod06474() {
        return this.internalField0519;
    }

    @Generated
    public AnimatedValue internalMethod02089() {
        return this.internalField0808;
    }

    @Generated
    public AnimatedValue internalMethod02715() {
        return this.internalField0809;
    }

    @Generated
    public AnimatedStatusEntry internalMethod06767(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }
}

