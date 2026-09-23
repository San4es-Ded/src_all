package rockstar.client.internal.script;






import rockstar.client.rotation.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import pyrock.events.newton.NewtonFailedEvent;
import pyrock.events.newton.NewtonFinishedEvent;
import pyrock.events.newton.NewtonNodeEvent;
import pyrock.events.newton.NewtonPathEvent;
import pyrock.events.newton.NewtonStartedEvent;
import rockstar.client.event.ClientEvent;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.NewtonTaskEvent;
import rockstar.client.internal.core.NewtonTaskNodeEvent;
import rockstar.client.internal.core.NewtonTaskFailedEvent;
import rockstar.client.internal.core.NewtonTaskFinishedEvent;
import rockstar.client.internal.core.NewtonTaskStartedEvent;
import rockstar.client.internal.rotation.NewtonCoreManager;

public final class NewtonEventDispatcher {
    private NewtonEventDispatcher() {
    }

    public static void internalMethod07616(String string) {
        NewtonEventDispatcher.internalMethod02289(new NewtonStartedEvent(string));
    }

    public static void internalMethod06392(String string, int n) {
        NewtonEventDispatcher.internalMethod02289(new NewtonPathEvent(string, n));
        NewtonEventDispatcher.internalMethod06057(new NewtonTaskStartedEvent(string, n));
    }

    public static void internalMethod00498(String string, int n, int n2, int n3, int n4, int n5) {
        NewtonEventDispatcher.internalMethod02289(new NewtonNodeEvent(string, n, n2, n3, n4, n5));
        NewtonEventDispatcher.internalMethod06057(new NewtonTaskNodeEvent(string, n, n2, n3, n4, n5));
    }

    public static void internalMethod06127(String string) {
        NewtonEventDispatcher.internalMethod02289(new NewtonFinishedEvent(string));
        NewtonEventDispatcher.internalMethod06057(new NewtonTaskFinishedEvent(string));
    }

    public static void internalMethod04499(String string, String string2) {
        NewtonEventDispatcher.internalMethod02289(new NewtonFailedEvent(string, string2));
        NewtonEventDispatcher.internalMethod06057(new NewtonTaskFailedEvent(string, string2));
    }

    private static void internalMethod02289(ClientEvent typedValue134) {
        try {
            RockstarClient typedParameter1001 = RockstarClient.getInstance();
            if (typedParameter1001 != null && typedParameter1001.internalMethod03317() != null) {
                typedParameter1001.internalMethod03317().internalMethod06883(typedValue134);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }

    private static void internalMethod06057(NewtonTaskEvent typedValue283) {
        try {
            if (NewtonCoreManager.internalMethod00010()) {
                NewtonCoreManager.internalMethod00114().internalMethod00112().internalMethod07208(typedValue283);
            }
        }
        catch (Throwable throwable) {
            // empty catch block
        }
    }
}

