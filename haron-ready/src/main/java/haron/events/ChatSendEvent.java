package haron.events;

import haron.events.CancellableEvent;

public class ChatSendEvent
extends CancellableEvent {
    private final String message;

    public ChatSendEvent(String string) {
        this.message = string;
    }

    public String d() {
        return this.message;
    }

    public String getMessage() {
        return this.message;
    }
}

