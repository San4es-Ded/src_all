package rockstar.client.network;


import rockstar.client.*;
import java.util.Objects;
import javax.annotation.Nonnull;

public class HttpHeader {
    @Nonnull
    private final String internalField0248;
    @Nonnull
    private final String internalField0247;

    public HttpHeader(@Nonnull String string, @Nonnull String string2) {
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    @Nonnull
    public String internalMethod02839() {
        return this.internalField0248;
    }

    @Nonnull
    public String internalMethod04441() {
        return this.internalField0247;
    }

    public String toString() {
        return "HttpHeader{name='" + this.internalField0248 + '\'' + ", value='" + this.internalField0247 + '\'' + '}';
    }

    public boolean equals(Object object) {
        if (this == object) {
            return true;
        }
        if (object == null || this.getClass() != object.getClass()) {
            return false;
        }
        HttpHeader typedValue039 = (HttpHeader)object;
        return Objects.equals(this.internalField0248, typedValue039.internalField0248) && Objects.equals(this.internalField0247, typedValue039.internalField0247);
    }

    public int hashCode() {
        return Objects.hash(this.internalField0248, this.internalField0247);
    }
}

