package rockstar.client.internal.core;


import rockstar.client.*;
import javax.annotation.Nonnull;
import lombok.Generated;
import rockstar.client.internal.core.HttpRetryHandler;
import rockstar.client.internal.core.RetryAfterResolver;

public class HttpRequestOptions {
    private int internalField0227;
    private int internalField0228;
    @Nonnull
    private HttpRetryHandler internalField0875;

    public HttpRequestOptions() {
        this(0, 0);
    }

    public HttpRequestOptions(int n, int n2) {
        this(n, n2, new RetryAfterResolver());
    }

    public HttpRequestOptions(int n, int n2, @Nonnull HttpRetryHandler typedParameter019) {
        this.internalField0227 = n;
        this.internalField0228 = n2;
        this.internalField0875 = typedParameter019;
    }

    @Generated
    public int internalMethod05746() {
        return this.internalField0227;
    }

    @Generated
    public int internalMethod05749() {
        return this.internalField0228;
    }

    @Nonnull
    @Generated
    public HttpRetryHandler internalMethod06818() {
        return this.internalField0875;
    }

    @Generated
    public HttpRequestOptions internalMethod07052(int n) {
        this.internalField0227 = n;
        return this;
    }

    @Generated
    public HttpRequestOptions internalMethod00292(int n) {
        this.internalField0228 = n;
        return this;
    }

    @Generated
    public HttpRequestOptions internalMethod04267(@Nonnull HttpRetryHandler typedParameter019) {
        if (typedParameter019 == null) {
            throw new NullPointerException("retryHandler is marked non-null but is null");
        }
        this.internalField0875 = typedParameter019;
        return this;
    }
}

