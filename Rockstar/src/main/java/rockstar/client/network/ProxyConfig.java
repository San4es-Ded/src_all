package rockstar.client.network;


import rockstar.client.*;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import rockstar.client.network.ProxyType;
import rockstar.client.network.ProxyAuthenticator;
import rockstar.client.network.AuthenticatedProxySelector;

public class ProxyConfig {
    private ProxyType internalField0273;
    private SocketAddress internalField0932;
    private String internalField0248;
    private String internalField0247;

    public ProxyConfig() {
    }

    public ProxyConfig(ProxyType typedValue040, String string, int n) {
        this(typedValue040, string, n, null, null);
    }

    public ProxyConfig(ProxyType typedValue040, String string, int n, @Nullable String string2, @Nullable String string3) {
        this(typedValue040, new InetSocketAddress(string, n), string2, string3);
    }

    public ProxyConfig(ProxyType typedValue040, SocketAddress socketAddress) {
        this(typedValue040, socketAddress, null, null);
    }

    public ProxyConfig(ProxyType typedValue040, SocketAddress socketAddress, @Nullable String string, @Nullable String string2) {
        this.internalField0273 = typedValue040;
        this.internalField0932 = socketAddress;
        this.internalField0248 = string;
        this.internalField0247 = string2;
    }

    public ProxyConfig internalMethod00591(ProxyType typedValue040, String string, int n) {
        return this.internalMethod07463(typedValue040, new InetSocketAddress(string, n));
    }

    public ProxyConfig internalMethod07463(ProxyType typedValue040, SocketAddress socketAddress) {
        this.internalField0273 = typedValue040;
        this.internalField0932 = socketAddress;
        return this;
    }

    public ProxyConfig internalMethod02244() {
        this.internalField0273 = null;
        this.internalField0932 = null;
        return this;
    }

    public boolean internalMethod01161() {
        return this.internalField0273 != null && this.internalField0932 != null;
    }

    @Nullable
    public ProxyType internalMethod02245() {
        return this.internalField0273;
    }

    public ProxyConfig internalMethod04873(@Nonnull ProxyType typedValue040) {
        this.internalField0273 = typedValue040;
        return this;
    }

    @Nullable
    public SocketAddress internalMethod07403() {
        return this.internalField0932;
    }

    public ProxyConfig internalMethod06759(@Nonnull SocketAddress socketAddress) {
        this.internalField0932 = socketAddress;
        return this;
    }

    public boolean internalMethod01162() {
        return this.internalField0248 != null && this.internalField0247 != null;
    }

    @Nullable
    public String internalMethod01659() {
        return this.internalField0248;
    }

    public ProxyConfig internalMethod00565(@Nullable String string) {
        this.internalField0248 = string;
        return this;
    }

    @Nullable
    public String internalMethod06232() {
        return this.internalField0247;
    }

    public ProxyConfig internalMethod03164(@Nullable String string) {
        this.internalField0247 = string;
        return this;
    }

    public AuthenticatedProxySelector internalMethod02248() {
        if (!this.internalMethod01161()) {
            throw new IllegalStateException("Proxy is not set");
        }
        return new AuthenticatedProxySelector(this.internalMethod07281(), this.internalField0248, this.internalField0247);
    }

    public ProxyAuthenticator internalMethod02247() {
        if (!this.internalMethod01161()) {
            throw new IllegalStateException("Proxy is not set");
        }
        if (!this.internalMethod01162()) {
            throw new IllegalStateException("Username or password is not set");
        }
        return new ProxyAuthenticator(this.internalField0248, this.internalField0247);
    }

    public Proxy internalMethod07281() {
        switch (this.internalField0273) {
            case internalField0273: {
                return new Proxy(Proxy.Type.HTTP, this.internalField0932);
            }
            case internalField0272: {
                try {
                    Class<?> clazz = Class.forName("sun.net.SocksProxy");
                    Method method = clazz.getDeclaredMethod("create", SocketAddress.class, Integer.TYPE);
                    return (Proxy)method.invoke(null, this.internalField0932, 4);
                }
                catch (Throwable throwable) {
                    throw new UnsupportedOperationException("SOCKS4 proxy type is not supported", throwable);
                }
            }
            case internalField1098: {
                return new Proxy(Proxy.Type.SOCKS, this.internalField0932);
            }
        }
        throw new IllegalStateException("Unknown proxy type: " + this.internalField0273.name());
    }
}

