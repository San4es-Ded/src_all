package rockstar.client.network;


import rockstar.client.*;
import rockstar.client.internal.core.*;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.CookieManager;
import java.net.MalformedURLException;
import java.net.URL;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rockstar.client.internal.core.HttpRequestOptions;
import rockstar.client.internal.core.OptionalValueHolder;
import rockstar.client.network.FluentObject;
import rockstar.client.network.RockstarHttpClient;
import rockstar.client.network.RockstarHttpResponse;
import rockstar.client.network.ResponseHandler;

public class RockstarHttpRequest
extends FluentObject<RockstarHttpRequest> {
    private final String internalField0248;
    private final URL internalField0360;
    private boolean internalField0277;
    private boolean internalField0276;
    private InternalType0210 internalField0530 = InternalType0210.internalField0530;
    private final OptionalValueHolder<CookieManager> internalField0887 = new OptionalValueHolder();
    private final OptionalValueHolder<HttpRequestOptions> internalField0886 = new OptionalValueHolder();
    private final OptionalValueHolder<Boolean> internalField1365 = new OptionalValueHolder();
    private WeakReference<RockstarHttpClient> internalField0015;

    public RockstarHttpRequest(String string, String string2) throws MalformedURLException {
        this(string, new URL(string2));
    }

    public RockstarHttpRequest(String string, URL uRL) {
        this.internalField0248 = string;
        this.internalField0360 = uRL;
    }

    public String internalMethod06034() {
        return this.internalField0248;
    }

    public URL internalMethod03635() {
        return this.internalField0360;
    }

    public boolean internalMethod02703() {
        return this.internalField0277;
    }

    public RockstarHttpRequest internalMethod03411(boolean bl) {
        this.internalField0277 = bl;
        return this;
    }

    public boolean internalMethod07965() {
        return this.internalField0276;
    }

    public RockstarHttpRequest internalMethod04290(boolean bl) {
        this.internalField0276 = bl;
        return this;
    }

    public InternalType0210 internalMethod03460() {
        return this.internalField0530;
    }

    public RockstarHttpRequest internalMethod08955(boolean bl) {
        return this.internalMethod04003(bl ? InternalType0210.internalField0531 : InternalType0210.internalField1191);
    }

    public RockstarHttpRequest internalMethod04003(@Nonnull InternalType0210 nestedValue2029) {
        this.internalField0530 = nestedValue2029;
        return this;
    }

    public boolean internalMethod07966() {
        return this.internalField0887.internalMethod02064();
    }

    public RockstarHttpRequest internalMethod00446() {
        this.internalField0887.internalMethod02063();
        return this;
    }

    @Nullable
    public CookieManager internalMethod02997() {
        return this.internalField0887.internalMethod02038();
    }

    public RockstarHttpRequest internalMethod00332(@Nullable CookieManager cookieManager) {
        this.internalField0887.internalMethod01273(cookieManager);
        return this;
    }

    public boolean internalMethod07977() {
        return this.internalField0886.internalMethod02064();
    }

    public RockstarHttpRequest internalMethod01799() {
        this.internalField0886.internalMethod02063();
        return this;
    }

    @Nonnull
    public HttpRequestOptions internalMethod04025() {
        return this.internalField0886.internalMethod02038();
    }

    public RockstarHttpRequest internalMethod00961(@Nonnull HttpRequestOptions typedParameter1017) {
        this.internalField0886.internalMethod01273(typedParameter1017);
        return this;
    }

    public boolean internalMethod07978() {
        return this.internalField1365.internalMethod02064();
    }

    public RockstarHttpRequest internalMethod08396() {
        this.internalField1365.internalMethod02063();
        return this;
    }

    public boolean internalMethod09158() {
        return this.internalField1365.internalMethod02038();
    }

    public RockstarHttpRequest internalMethod09108(boolean bl) {
        this.internalField1365.internalMethod01273(bl);
        return this;
    }

    public RockstarHttpRequest internalMethod03607(@Nullable RockstarHttpClient typedValue034) {
        this.internalField0015 = typedValue034 == null ? null : new WeakReference<RockstarHttpClient>(typedValue034);
        return this;
    }

    public RockstarHttpResponse internalMethod06764() throws IOException {
        RockstarHttpClient typedValue034 = null;
        if (this.internalField0015 != null) {
            typedValue034 = (RockstarHttpClient)this.internalField0015.get();
        }
        if (typedValue034 == null) {
            typedValue034 = new RockstarHttpClient();
        }
        return typedValue034.internalMethod03397(this);
    }

    public <R> R internalMethod02842(ResponseHandler<R> typedValue037) throws IOException {
        RockstarHttpClient typedValue034 = null;
        if (this.internalField0015 != null) {
            typedValue034 = (RockstarHttpClient)this.internalField0015.get();
        }
        if (typedValue034 == null) {
            typedValue034 = new RockstarHttpClient();
        }
        return typedValue034.internalMethod04616(this, typedValue037);
    }

    public static enum InternalType0210 {
        internalField0530,
        internalField0531,
        internalField1191;

    }
}

