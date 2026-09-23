package rockstar.client.internal.auth;




import rockstar.client.data.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import com.google.gson.JsonObject;
import lombok.Generated;
import org.jetbrains.annotations.ApiStatus;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.core.NonBlankString;

public final class RealmsServerModel {
    private final long internalField0229;
    private final String internalField0248;
    private final String internalField0247;
    private final String internalField1077;
    private final String internalField1076;
    private final String internalField1079;
    private final boolean internalField0277;
    private final int internalField0227;
    private final String internalField1078;
    private final int internalField0228;
    private final boolean internalField0276;
    private final String internalField1501;
    private final JsonObject internalField0539;

    @ApiStatus.Internal
    public static RealmsServerModel internalMethod01457(JsonObjectNode typedValue030) {
        return new RealmsServerModel(typedValue030.internalMethod05143("id", -1L), NonBlankString.internalMethod04450(typedValue030.internalMethod01170("name", null)), NonBlankString.internalMethod04450(typedValue030.internalMethod01170("motd", null)), NonBlankString.internalMethod04450(typedValue030.internalMethod01170("owner", null)), NonBlankString.internalMethod04450(typedValue030.internalMethod01170("ownerUUID", null)), typedValue030.internalMethod01170("state", "CLOSED"), typedValue030.internalMethod05145("expired", false), typedValue030.internalMethod05142("daysLeft", 0), typedValue030.internalMethod01170("worldType", "NORMAL"), typedValue030.internalMethod05142("maxPlayers", 0), typedValue030.internalMethod01170("compatibility", "COMPATIBLE").equals("COMPATIBLE"), NonBlankString.internalMethod04450(typedValue030.internalMethod01170("activeVersion", null)), typedValue030.internalMethod02940());
    }

    public String internalMethod07157(String string) {
        return this.internalField0248 == null ? string : this.internalField0248;
    }

    public String internalMethod00022(String string) {
        return this.internalField0247 == null ? string : this.internalField0247;
    }

    public String internalMethod07701(String string) {
        return this.internalField1077 == null ? string : this.internalField1077;
    }

    public String internalMethod07950(String string) {
        return this.internalField1076 == null ? string : this.internalField1076;
    }

    public String internalMethod09124(String string) {
        return this.internalField1501 == null ? string : this.internalField1501;
    }

    @Generated
    public RealmsServerModel(long l, String string, String string2, String string3, String string4, String string5, boolean bl, int n, String string6, int n2, boolean bl2, String string7, JsonObject jsonObject) {
        this.internalField0229 = l;
        this.internalField0248 = string;
        this.internalField0247 = string2;
        this.internalField1077 = string3;
        this.internalField1076 = string4;
        this.internalField1079 = string5;
        this.internalField0277 = bl;
        this.internalField0227 = n;
        this.internalField1078 = string6;
        this.internalField0228 = n2;
        this.internalField0276 = bl2;
        this.internalField1501 = string7;
        this.internalField0539 = jsonObject;
    }

    @Generated
    public long internalMethod02976() {
        return this.internalField0229;
    }

    @Generated
    public String internalMethod05416() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod01919() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod07918() {
        return this.internalField1077;
    }

    @Generated
    public String internalMethod08822() {
        return this.internalField1076;
    }

    @Generated
    public String internalMethod07975() {
        return this.internalField1079;
    }

    @Generated
    public boolean internalMethod02977() {
        return this.internalField0277;
    }

    @Generated
    public int internalMethod02975() {
        return this.internalField0227;
    }

    @Generated
    public String internalMethod08507() {
        return this.internalField1078;
    }

    @Generated
    public int internalMethod02980() {
        return this.internalField0228;
    }

    @Generated
    public boolean internalMethod02981() {
        return this.internalField0276;
    }

    @Generated
    public String internalMethod09271() {
        return this.internalField1501;
    }

    @Generated
    public JsonObject internalMethod01716() {
        return this.internalField0539;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof RealmsServerModel)) {
            return false;
        }
        RealmsServerModel typedValue062 = (RealmsServerModel)object;
        if (this.internalMethod02976() != typedValue062.internalMethod02976()) {
            return false;
        }
        if (this.internalMethod02977() != typedValue062.internalMethod02977()) {
            return false;
        }
        if (this.internalMethod02975() != typedValue062.internalMethod02975()) {
            return false;
        }
        if (this.internalMethod02980() != typedValue062.internalMethod02980()) {
            return false;
        }
        if (this.internalMethod02981() != typedValue062.internalMethod02981()) {
            return false;
        }
        String string = this.internalMethod05416();
        String string2 = typedValue062.internalMethod05416();
        if (string == null ? string2 != null : !string.equals(string2)) {
            return false;
        }
        String string3 = this.internalMethod01919();
        String string4 = typedValue062.internalMethod01919();
        if (string3 == null ? string4 != null : !string3.equals(string4)) {
            return false;
        }
        String string5 = this.internalMethod07918();
        String string6 = typedValue062.internalMethod07918();
        if (string5 == null ? string6 != null : !string5.equals(string6)) {
            return false;
        }
        String string7 = this.internalMethod08822();
        String string8 = typedValue062.internalMethod08822();
        if (string7 == null ? string8 != null : !string7.equals(string8)) {
            return false;
        }
        String string9 = this.internalMethod07975();
        String string10 = typedValue062.internalMethod07975();
        if (string9 == null ? string10 != null : !string9.equals(string10)) {
            return false;
        }
        String string11 = this.internalMethod08507();
        String string12 = typedValue062.internalMethod08507();
        if (string11 == null ? string12 != null : !string11.equals(string12)) {
            return false;
        }
        String string13 = this.internalMethod09271();
        String string14 = typedValue062.internalMethod09271();
        if (string13 == null ? string14 != null : !string13.equals(string14)) {
            return false;
        }
        JsonObject jsonObject = this.internalMethod01716();
        JsonObject jsonObject2 = typedValue062.internalMethod01716();
        return !(jsonObject == null ? jsonObject2 != null : !jsonObject.equals(jsonObject2));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        long l = this.internalMethod02976();
        n2 = n2 * 59 + (int)(l >>> 32 ^ l);
        n2 = n2 * 59 + (this.internalMethod02977() ? 79 : 97);
        n2 = n2 * 59 + this.internalMethod02975();
        n2 = n2 * 59 + this.internalMethod02980();
        n2 = n2 * 59 + (this.internalMethod02981() ? 79 : 97);
        String string = this.internalMethod05416();
        n2 = n2 * 59 + (string == null ? 43 : string.hashCode());
        String string2 = this.internalMethod01919();
        n2 = n2 * 59 + (string2 == null ? 43 : string2.hashCode());
        String string3 = this.internalMethod07918();
        n2 = n2 * 59 + (string3 == null ? 43 : string3.hashCode());
        String string4 = this.internalMethod08822();
        n2 = n2 * 59 + (string4 == null ? 43 : string4.hashCode());
        String string5 = this.internalMethod07975();
        n2 = n2 * 59 + (string5 == null ? 43 : string5.hashCode());
        String string6 = this.internalMethod08507();
        n2 = n2 * 59 + (string6 == null ? 43 : string6.hashCode());
        String string7 = this.internalMethod09271();
        n2 = n2 * 59 + (string7 == null ? 43 : string7.hashCode());
        JsonObject jsonObject = this.internalMethod01716();
        n2 = n2 * 59 + (jsonObject == null ? 43 : jsonObject.hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "RealmsServer(id=" + this.internalMethod02976() + ", name=" + this.internalMethod05416() + ", motd=" + this.internalMethod01919() + ", ownerName=" + this.internalMethod07918() + ", ownerUid=" + this.internalMethod08822() + ", state=" + this.internalMethod07975() + ", expired=" + this.internalMethod02977() + ", daysLeft=" + this.internalMethod02975() + ", worldType=" + this.internalMethod08507() + ", maxPlayers=" + this.internalMethod02980() + ", compatible=" + this.internalMethod02981() + ", activeVersion=" + this.internalMethod09271() + ", rawResponse=" + this.internalMethod01716() + ")";
    }
}

