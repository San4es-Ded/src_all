package rockstar.client.internal.framework;



import rockstar.client.setting.*;
import rockstar.client.*;
import java.util.function.Predicate;
import rockstar.client.setting.MultiSelectSetting;

public class PredicateSelectOption<T>
extends MultiSelectSetting.InternalType0091 {
    private final Predicate<T> internalField0486;

    public PredicateSelectOption(MultiSelectSetting typedValue173, String string, Predicate<T> predicate) {
        super(typedValue173, string);
        this.internalField0486 = predicate;
    }

    public PredicateSelectOption(MultiSelectSetting typedValue173, String string, String string2, Predicate<T> predicate) {
        super(typedValue173, string, string2);
        this.internalField0486 = predicate;
    }

    public boolean internalMethod06699(T t) {
        return this.internalField0486.test(t) && this.isSelected();
    }
}

