package rockstar.client.network;


import rockstar.client.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;
import java.util.Locale;
import java.util.Objects;
import java.util.Optional;
import javax.annotation.Nullable;

public class MediaType {
    private final String internalField0248;
    private final Charset internalField0091;
    private final String internalField0247;

    public static MediaType internalMethod06844(String string) {
        if (!string.contains(";")) {
            return new MediaType(string.toLowerCase(Locale.ROOT), null, null);
        }
        String[] stringArray = string.split(";");
        String string2 = stringArray[0].toLowerCase(Locale.ROOT);
        Charset charset = null;
        String string3 = null;
        for (int i = 1; i < stringArray.length; ++i) {
            String string4 = stringArray[i].trim();
            if (string4.startsWith("charset=")) {
                try {
                    charset = Charset.forName(string4.substring(8));
                }
                catch (UnsupportedCharsetException unsupportedCharsetException) {}
                continue;
            }
            if (!string4.startsWith("boundary=")) continue;
            string3 = string4.substring(9);
        }
        return new MediaType(string2, charset, string3);
    }

    public MediaType(String string) {
        this(string, null, null);
    }

    public MediaType(String string, @Nullable Charset charset) {
        this(string, charset, null);
    }

    public MediaType(String string, @Nullable String string2) {
        this(string, null, string2);
    }

    public MediaType(String string, @Nullable Charset charset, @Nullable String string2) {
        this.internalField0248 = string;
        this.internalField0091 = charset;
        this.internalField0247 = string2;
    }

    public String internalMethod05675() {
        return this.internalField0248;
    }

    public Optional<Charset> internalMethod05445() {
        return Optional.ofNullable(this.internalField0091);
    }

    public Optional<String> internalMethod01820() {
        return Optional.ofNullable(this.internalField0247);
    }

    public String toString() {
        return this.internalField0248 + (this.internalField0091 != null ? "; charset=" + this.internalField0091.name() : "") + (this.internalField0247 != null ? "; boundary=" + this.internalField0247 : "");
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        MediaType typedValue038 = (MediaType)object;
        return Objects.equals(this.internalField0248, typedValue038.internalField0248) && Objects.equals(this.internalField0091, typedValue038.internalField0091) && Objects.equals(this.internalField0247, typedValue038.internalField0247);
    }

    public int hashCode() {
        return Objects.hash(this.internalField0248, this.internalField0091, this.internalField0247);
    }
}

