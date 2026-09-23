package pyrock.events;




import rockstar.client.util.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;
import jep.python.PyCallable;
import lombok.Generated;
import net.minecraft.text.Text;
import pyrock.events.PyEvent;
import rockstar.client.internal.script.PythonScript;
import rockstar.client.internal.script.EmbeddedPythonRuntime;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;
import rockstar.client.RockstarClient;
import rockstar.client.util.ClientMessages;
import rockstar.client.MinecraftClientAccess;

public class PyEvents {
    private final PythonScript owner;
    private final Map<String, List<PyCallable>> listeners = new HashMap<String, List<PyCallable>>();
    private final Map<String, PyEvent> eventObjects = new HashMap<String, PyEvent>();
    private final Map<Class<?>, String> eventNameCache = new ConcurrentHashMap();
    private Consumer<Exception> errorHandler;
    private volatile boolean disposed;

    public PyEvents() {
        this(null);
    }

    public PyEvents(PythonScript typedValue155) {
        this.owner = typedValue155;
    }

    public void register(String string2, PyCallable pyCallable) {
        if (this.disposed) {
            return;
        }
        this.listeners.computeIfAbsent(string2.toLowerCase(), string -> new ArrayList()).add(pyCallable);
    }

    public void fire(ClientEvent typedValue134) {
        if (!this.isAlive()) {
            return;
        }
        String string = this.getEventName(typedValue134.getClass());
        if (string == null) {
            return;
        }
        List<PyCallable> list = this.listeners.get(string);
        if (list == null || list.isEmpty()) {
            return;
        }
        if (!EmbeddedPythonRuntime.internalMethod08724()) {
            MinecraftClientAccess.internalField0149.execute(() -> {
                if (this.isAlive()) {
                    this.fireOnOwnerThread(string, typedValue134);
                }
            });
            return;
        }
        this.fireOnOwnerThread(string, typedValue134);
    }

    private void fireOnOwnerThread(String string, ClientEvent typedValue134) {
        if (!this.isAlive()) {
            return;
        }
        List<PyCallable> list = this.listeners.get(string);
        if (list == null || list.isEmpty()) {
            return;
        }
        for (PyCallable pyCallable : list) {
            try {
                AutoCloseable autoCloseable = PythonScript.internalMethod02561(this.owner);
                try {
                    pyCallable.call(new Object[]{typedValue134});
                }
                finally {
                    if (autoCloseable == null) continue;
                    autoCloseable.close();
                }
            }
            catch (Exception exception) {
                String string2 = exception.getMessage();
                if (string2 != null && string2.contains(":")) {
                    string2 = string2.substring(string2.indexOf(":") + 1).trim();
                }
                ClientMessages.internalMethod09025(Text.of((String)("[Python Error] " + string2)));
                RockstarClient.internalField0572.error("Python error in event '" + string + "':", (Throwable)exception);
                if (this.errorHandler == null) break;
                this.errorHandler.accept(exception);
                break;
            }
        }
    }

    private String getEventName(Class<?> clazz) {
        String string = this.eventNameCache.get(clazz);
        if (string != null) {
            return string.isEmpty() ? null : string;
        }
        EventName typedValue135 = clazz.getAnnotation(EventName.class);
        String string2 = typedValue135 == null ? "" : typedValue135.internalMethod03601().toLowerCase();
        this.eventNameCache.put(clazz, string2);
        return string2.isEmpty() ? null : string2;
    }

    public PyEvent getEvent(String string2) {
        return this.eventObjects.computeIfAbsent(string2, string -> new PyEvent((String)string, this));
    }

    public void dispose() {
        this.disposed = true;
        this.listeners.clear();
        this.eventObjects.clear();
        this.errorHandler = null;
    }

    private boolean isAlive() {
        return !this.disposed && (this.owner == null || this.owner.internalMethod08681());
    }

    @Generated
    public void setErrorHandler(Consumer<Exception> consumer) {
        this.errorHandler = consumer;
    }
}

