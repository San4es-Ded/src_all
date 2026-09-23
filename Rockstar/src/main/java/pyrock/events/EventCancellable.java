package pyrock.events;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;

public class EventCancellable
extends ClientEvent {
    private boolean cancelled;

    public final void cancel() {
        this.cancelled = true;
    }

    @Generated
    public boolean isCancelled() {
        return this.cancelled;
    }
}

