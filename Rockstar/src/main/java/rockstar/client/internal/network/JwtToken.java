package rockstar.client.internal.network;





import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Base64;
import lombok.Generated;
import rockstar.client.internal.config.JsonParserUtils;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.ExpiringToken;

public final class JwtToken
implements ExpiringToken {
    private final JsonObjectNode internalField0676;
    private final JsonObjectNode internalField0677;
    private final byte[] internalField0609;

    public static JwtToken internalMethod04997(String string) {
        if (string == null) {
            throw new IllegalArgumentException("JWT string is null");
        }
        String[] stringArray = string.split("\\.");
        if (stringArray.length < 2) {
            throw new IllegalArgumentException("JWT must have at least header and payload");
        }
        JsonObjectNode typedValue030 = JsonParserUtils.internalMethod05878(new String(Base64.getUrlDecoder().decode(stringArray[0]), StandardCharsets.UTF_8)).internalMethod04512();
        JsonObjectNode typedValue031 = JsonParserUtils.internalMethod05878(new String(Base64.getUrlDecoder().decode(stringArray[1]), StandardCharsets.UTF_8)).internalMethod04512();
        byte[] byArray = stringArray.length > 2 ? Base64.getUrlDecoder().decode(stringArray[2]) : null;
        return new JwtToken(typedValue030, typedValue031, byArray);
    }

    @Override
    public long internalMethod03800() {
        if (this.internalField0677.internalMethod07773("exp")) {
            return this.internalField0677.internalMethod02177("exp") * 1000L;
        }
        return Long.MAX_VALUE;
    }

    @Generated
    public JwtToken(JsonObjectNode typedValue030, JsonObjectNode typedValue031, byte[] byArray) {
        this.internalField0676 = typedValue030;
        this.internalField0677 = typedValue031;
        this.internalField0609 = byArray;
    }

    @Generated
    public JsonObjectNode internalMethod01946() {
        return this.internalField0676;
    }

    @Generated
    public JsonObjectNode internalMethod03211() {
        return this.internalField0677;
    }

    @Generated
    public byte[] internalMethod00724() {
        return this.internalField0609;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof JwtToken)) {
            return false;
        }
        JwtToken typedValue085 = (JwtToken)object;
        JsonObjectNode typedValue030 = this.internalMethod01946();
        JsonObjectNode typedValue031 = typedValue085.internalMethod01946();
        if (typedValue030 == null ? typedValue031 != null : !((Object)typedValue030).equals(typedValue031)) {
            return false;
        }
        JsonObjectNode typedValue032 = this.internalMethod03211();
        JsonObjectNode typedValue033 = typedValue085.internalMethod03211();
        if (typedValue032 == null ? typedValue033 != null : !((Object)typedValue032).equals(typedValue033)) {
            return false;
        }
        return Arrays.equals(this.internalMethod00724(), typedValue085.internalMethod00724());
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        JsonObjectNode typedValue030 = this.internalMethod01946();
        n2 = n2 * 59 + (typedValue030 == null ? 43 : ((Object)typedValue030).hashCode());
        JsonObjectNode typedValue031 = this.internalMethod03211();
        n2 = n2 * 59 + (typedValue031 == null ? 43 : ((Object)typedValue031).hashCode());
        n2 = n2 * 59 + Arrays.hashCode(this.internalMethod00724());
        return n2;
    }

    @Generated
    public String toString() {
        return "Jwt(header=" + this.internalMethod01946() + ", payload=" + this.internalMethod03211() + ", signature=" + Arrays.toString(this.internalMethod00724()) + ")";
    }
}

