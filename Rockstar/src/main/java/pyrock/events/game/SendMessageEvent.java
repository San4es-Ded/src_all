package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="send_message")
public class SendMessageEvent
extends EventCancellable {
    private String message;

    @Generated
    public void setMessage(String string) {
        this.message = string;
    }

    @Generated
    public String getMessage() {
        return this.message;
    }

    @Generated
    public SendMessageEvent(String string) {
        this.message = string;
    }
}

