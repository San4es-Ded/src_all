package pyrock.events.newton;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="newton_finished")
public class NewtonFinishedEvent
extends ClientEvent {
    private final String process;

    @Generated
    public String getProcess() {
        return this.process;
    }

    @Generated
    public NewtonFinishedEvent(String string) {
        this.process = string;
    }
}

