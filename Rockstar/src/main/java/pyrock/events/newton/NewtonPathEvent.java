package pyrock.events.newton;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="newton_path")
public class NewtonPathEvent
extends ClientEvent {
    private final String process;
    private final int steps;

    @Generated
    public String getProcess() {
        return this.process;
    }

    @Generated
    public int getSteps() {
        return this.steps;
    }

    @Generated
    public NewtonPathEvent(String string, int n) {
        this.process = string;
        this.steps = n;
    }
}

