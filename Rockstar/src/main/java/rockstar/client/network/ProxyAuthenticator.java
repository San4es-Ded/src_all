package rockstar.client.network;


import rockstar.client.*;
import java.net.Authenticator;
import java.net.PasswordAuthentication;

public class ProxyAuthenticator
extends Authenticator {
    private final PasswordAuthentication internalField0930;

    public ProxyAuthenticator(String string, String string2) {
        this.internalField0930 = new PasswordAuthentication(string, string2.toCharArray());
    }

    @Override
    protected PasswordAuthentication getPasswordAuthentication() {
        return this.internalField0930;
    }
}

