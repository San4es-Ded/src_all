package rockstar.client.internal.event;




import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.module.ModuleEntry;

public class ModuleToggleEvent
extends ClientEvent {
    private final ModuleEntry internalField0403;

    @Generated
    public ModuleEntry internalMethod05415() {
        return this.internalField0403;
    }

    @Generated
    public ModuleToggleEvent(ModuleEntry typedValue145) {
        this.internalField0403 = typedValue145;
    }
}

