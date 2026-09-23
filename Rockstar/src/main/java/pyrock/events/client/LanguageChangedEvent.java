package pyrock.events.client;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="language_changed")
public class LanguageChangedEvent
extends ClientEvent {
    private final String code;

    @Generated
    public String getCode() {
        return this.code;
    }

    @Generated
    public LanguageChangedEvent(String string) {
        this.code = string;
    }
}

