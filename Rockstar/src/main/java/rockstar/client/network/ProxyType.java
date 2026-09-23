package rockstar.client.network;


import rockstar.client.*;
import java.net.Proxy;

public enum ProxyType {
    internalField0273,
    internalField0272,
    internalField1098;


    public static ProxyType internalMethod02962(Proxy.Type type) {
        switch (type) {
            case HTTP: {
                return internalField0273;
            }
            case SOCKS: {
                return internalField1098;
            }
        }
        throw new IllegalArgumentException("Unknown proxy type: " + type.name());
    }
}

