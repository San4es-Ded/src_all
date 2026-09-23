package pyrock.events.network;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.client.network.CookieStorage;
import net.minecraft.client.network.ServerAddress;
import net.minecraft.client.network.ServerInfo;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="connect")
public class ServerConnectionEvent
extends ClientEvent {
    private final ServerAddress address;
    private final ServerInfo info;
    private final CookieStorage cookieStorage;

    @Generated
    public ServerAddress getAddress() {
        return this.address;
    }

    @Generated
    public ServerInfo getInfo() {
        return this.info;
    }

    @Generated
    public CookieStorage getCookieStorage() {
        return this.cookieStorage;
    }

    @Generated
    public ServerConnectionEvent(ServerAddress serverAddress, ServerInfo serverInfo, CookieStorage cookieStorage) {
        this.address = serverAddress;
        this.info = serverInfo;
        this.cookieStorage = cookieStorage;
    }
}

