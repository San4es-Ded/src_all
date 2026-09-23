package pyrock.events.client;



import rockstar.client.module.*;
import rockstar.client.event.*;
import lombok.Generated;
import pyrock.classes.PyModule;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="module_toggled")
public class ModuleToggledEvent
extends ClientEvent {
    private final PyModule module;
    private final boolean state;

    @Generated
    public PyModule getModule() {
        return this.module;
    }

    @Generated
    public boolean isState() {
        return this.state;
    }

    @Generated
    public ModuleToggledEvent(PyModule pyModule, boolean bl) {
        this.module = pyModule;
        this.state = bl;
    }
}

