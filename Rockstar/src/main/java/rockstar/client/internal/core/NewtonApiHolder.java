package rockstar.client.internal.core;



import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.internal.game.PathCommandApi;

public final class NewtonApiHolder {
    private static volatile PathCommandApi internalField0526;

    private NewtonApiHolder() {
    }

    public static PathCommandApi internalMethod01856() {
        PathCommandApi typedValue281 = internalField0526;
        if (typedValue281 == null) {
            throw new IllegalStateException("Newton API is not initialised yet (initialise after Newton mod is loaded)");
        }
        return typedValue281;
    }

    public static boolean internalMethod05192() {
        return internalField0526 != null;
    }

    public static void internalMethod03916(PathCommandApi typedValue281) {
        if (internalField0526 != null) {
            throw new IllegalStateException("Newton API already installed");
        }
        internalField0526 = typedValue281;
    }
}

