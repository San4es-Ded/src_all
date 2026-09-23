package rockstar.client.internal.auth;


import rockstar.client.*;
import lombok.Generated;

public enum MsaEnvironment {
    internalField0726("https://login.live.com/", "oauth20_connect.srf", "oauth20_authorize.srf", "oauth20_token.srf", "oauth20_desktop.srf"),
    internalField0725("https://login.microsoftonline.com/common/oauth2/", "v2.0/devicecode", "v2.0/authorize", "v2.0/token", "nativeclient"),
    internalField1288("https://login.microsoftonline.com/consumers/oauth2/", "v2.0/devicecode", "v2.0/authorize", "v2.0/token", "nativeclient");

    private final String internalField0248;
    private final String internalField0247;
    private final String internalField1077;
    private final String internalField1076;
    private final String internalField1079;

    public String internalMethod02095() {
        return this.internalField0248 + this.internalField0247;
    }

    public String internalMethod06755() {
        return this.internalField0248 + this.internalField1077;
    }

    public String internalMethod08696() {
        return this.internalField0248 + this.internalField1076;
    }

    public String internalMethod08041() {
        return this.internalField0248 + this.internalField1079;
    }

    @Generated
    public String internalMethod08402() {
        return this.internalField0248;
    }

    @Generated
    public String internalMethod07686() {
        return this.internalField0247;
    }

    @Generated
    public String internalMethod09632() {
        return this.internalField1077;
    }

    @Generated
    public String internalMethod09293() {
        return this.internalField1076;
    }

    @Generated
    public String internalMethod09495() {
        return this.internalField1079;
    }

    @Generated
    private MsaEnvironment(String string2, String string3, String string4, String string5, String string6) {
        this.internalField0248 = string2;
        this.internalField0247 = string3;
        this.internalField1077 = string4;
        this.internalField1076 = string5;
        this.internalField1079 = string6;
    }
}

