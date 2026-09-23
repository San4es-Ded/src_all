package haron.settings;

import haron.settings.TextTokenType;
import haron.settings.Setting;
import java.util.function.Supplier;

public class ValidatedTextSetting
extends Setting<String> {
    private final TextTokenType tokenType;
    private final String suffix;

    public TextTokenType tokenType() {
        return this.tokenType;
    }

    @Override
    public Setting<String> visibleWhen(Supplier<Boolean> supplier) {
        super.visibleWhen(supplier);
        return this;
    }

    public Setting<String> visibleWhen2(Supplier supplier) {
        return this.visibleWhen(supplier);
    }

    public ValidatedTextSetting(String string, String string2, TextTokenType cqdcre2, String string3, String string4) {
        super(string, string2, string3);
        this.tokenType = cqdcre2;
        this.suffix = string4;
    }

    public ValidatedTextSetting(String string, TextTokenType cqdcre2, String string2, String string3) {
        this(string, "", cqdcre2, string2, string3);
    }

    public ValidatedTextSetting(String string, TextTokenType cqdcre2, String string2) {
        this(string, "", cqdcre2, string2, "");
    }

    public ValidatedTextSetting(String string, String string2) {
        this(string, "", TextTokenType.TEXT, string2, "");
    }

    public String get() {
        return (String)this.k();
    }

    public TextTokenType b() {
        return this.tokenType();
    }

    public String c() {
        return this.suffix();
    }

    public String suffix() {
        return this.suffix;
    }

    public boolean d() {
        return this.isValid();
    }

    public ValidatedTextSetting a(Supplier<Boolean> supplier) {
        return (ValidatedTextSetting)this.visibleWhen(supplier);
    }

    @Override
    public void a(String string) {
        this.set(string);
    }

    public String a() {
        return this.get();
    }

    public void set(String string) {
        super.a(string);
    }

    public boolean isValid() {
        String string = (String)this.k();
        if (string == null || string.isEmpty()) {
            return false;
        }
        if (this.tokenType != TextTokenType.NUMBER && this.tokenType != TextTokenType.PRICE) {
            return true;
        }
        try {
            Double.parseDouble(string);
            return true;
        }
        catch (NumberFormatException numberFormatException) {
            return false;
        }
    }
}

