package rockstar.client.internal.auth;




import rockstar.client.data.*;
import rockstar.client.internal.config.*;
import rockstar.client.*;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Generated;
import rockstar.client.data.JsonObjectNode;
import rockstar.client.internal.config.XblTitleToken;
import rockstar.client.internal.config.XblUserToken;
import rockstar.client.internal.auth.XblXstsToken;

public final class XblSisuTokens {
    private final XblUserToken internalField0463;
    private final XblTitleToken internalField0462;
    private final XblXstsToken internalField0464;

    public static XblSisuTokens internalMethod06146(JsonObject jsonObject) {
        return XblSisuTokens.internalMethod07517(new JsonObjectNode(jsonObject));
    }

    public static XblSisuTokens internalMethod07517(JsonObjectNode typedValue030) {
        return new XblSisuTokens(XblUserToken.internalMethod00778(typedValue030.internalMethod01060("userToken")), XblTitleToken.internalMethod06742(typedValue030.internalMethod01060("titleToken")), XblXstsToken.internalMethod06637(typedValue030.internalMethod01060("xstsToken")));
    }

    public static JsonObject internalMethod04856(XblSisuTokens typedValue087) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("_saveVersion", (Number)1);
        jsonObject.add("userToken", (JsonElement)XblUserToken.internalMethod05736(typedValue087.internalField0463));
        jsonObject.add("titleToken", (JsonElement)XblTitleToken.internalMethod03846(typedValue087.internalField0462));
        jsonObject.add("xstsToken", (JsonElement)XblXstsToken.internalMethod06839(typedValue087.internalField0464));
        return jsonObject;
    }

    @Generated
    public XblSisuTokens(XblUserToken typedValue041, XblTitleToken typedValue088, XblXstsToken typedValue043) {
        this.internalField0463 = typedValue041;
        this.internalField0462 = typedValue088;
        this.internalField0464 = typedValue043;
    }

    @Generated
    public XblUserToken internalMethod02222() {
        return this.internalField0463;
    }

    @Generated
    public XblTitleToken internalMethod02221() {
        return this.internalField0462;
    }

    @Generated
    public XblXstsToken internalMethod02225() {
        return this.internalField0464;
    }

    @Generated
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof XblSisuTokens)) {
            return false;
        }
        XblSisuTokens typedValue087 = (XblSisuTokens)object;
        XblUserToken typedValue041 = this.internalMethod02222();
        XblUserToken typedValue042 = typedValue087.internalMethod02222();
        if (typedValue041 == null ? typedValue042 != null : !((Object)typedValue041).equals(typedValue042)) {
            return false;
        }
        XblTitleToken typedValue088 = this.internalMethod02221();
        XblTitleToken typedValue089 = typedValue087.internalMethod02221();
        if (typedValue088 == null ? typedValue089 != null : !((Object)typedValue088).equals(typedValue089)) {
            return false;
        }
        XblXstsToken typedValue043 = this.internalMethod02225();
        XblXstsToken typedValue044 = typedValue087.internalMethod02225();
        return !(typedValue043 == null ? typedValue044 != null : !((Object)typedValue043).equals(typedValue044));
    }

    @Generated
    public int hashCode() {
        int n = 59;
        int n2 = 1;
        XblUserToken typedValue041 = this.internalMethod02222();
        n2 = n2 * 59 + (typedValue041 == null ? 43 : ((Object)typedValue041).hashCode());
        XblTitleToken typedValue088 = this.internalMethod02221();
        n2 = n2 * 59 + (typedValue088 == null ? 43 : ((Object)typedValue088).hashCode());
        XblXstsToken typedValue043 = this.internalMethod02225();
        n2 = n2 * 59 + (typedValue043 == null ? 43 : ((Object)typedValue043).hashCode());
        return n2;
    }

    @Generated
    public String toString() {
        return "XblSisuTokens(userToken=" + this.internalMethod02222() + ", titleToken=" + this.internalMethod02221() + ", xstsToken=" + this.internalMethod02225() + ")";
    }
}

