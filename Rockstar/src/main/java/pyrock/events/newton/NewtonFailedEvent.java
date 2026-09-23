package pyrock.events.newton;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="newton_failed")
public class NewtonFailedEvent
extends ClientEvent {
    private final String process;
    private final String reason;

    @Generated
    public String getProcess() {
        return this.process;
    }

    @Generated
    public String getReason() {
        return this.reason;
    }

    @Generated
    public NewtonFailedEvent(String string, String string2) {
        this.process = string;
        this.reason = string2;
    }
}

