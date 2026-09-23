package rockstar.client.internal.core;



import rockstar.client.server.*;
import rockstar.client.*;
import rockstar.client.server.KnownServer;
import rockstar.client.server.ServerUtils;

public final class ServerTypeChecks {
    private ServerTypeChecks() {
    }

    public static boolean internalMethod06291() {
        return ServerUtils.internalMethod01786(KnownServer.internalField1571) || ServerUtils.internalMethod01786(KnownServer.internalField1566) || ServerUtils.internalMethod01786(KnownServer.internalField1569);
    }

    public static boolean internalMethod06293() {
        return !ServerTypeChecks.internalMethod06291() || ServerUtils.internalMethod01786(KnownServer.internalField1566);
    }

    public static boolean internalMethod08904() {
        return ServerUtils.internalMethod01786(KnownServer.internalField1220) || !ServerUtils.internalMethod01786(KnownServer.internalField1220) && !ServerUtils.internalMethod01786(KnownServer.internalField0578) && !ServerUtils.internalMethod01786(KnownServer.internalField0579);
    }

    public static boolean internalMethod08905() {
        return !ServerTypeChecks.internalMethod06291() || ServerUtils.internalMethod01786(KnownServer.internalField1569);
    }

    public static boolean internalMethod08919() {
        return !ServerUtils.internalMethod01786(KnownServer.internalField1568) && !ServerUtils.internalMethod01786(KnownServer.internalField1220) && !ServerUtils.internalMethod01786(KnownServer.internalField1219) && !ServerUtils.internalMethod01786(KnownServer.internalField1570) && !ServerUtils.internalMethod01786(KnownServer.internalField1572);
    }
}

