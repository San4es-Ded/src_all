package pyrock.events.client;


import rockstar.client.event.*;
import lombok.Generated;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="notification")
public class NotificationEvent
extends ClientEvent {
    private final String style;
    private final String type;
    private final String title;
    private final String text;

    @Generated
    public String getStyle() {
        return this.style;
    }

    @Generated
    public String getType() {
        return this.type;
    }

    @Generated
    public String getTitle() {
        return this.title;
    }

    @Generated
    public String getText() {
        return this.text;
    }

    @Generated
    public NotificationEvent(String string, String string2, String string3, String string4) {
        this.style = string;
        this.type = string2;
        this.title = string3;
        this.text = string4;
    }
}

