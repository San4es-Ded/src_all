package pyrock.events.render;


import rockstar.client.event.*;
import lombok.Generated;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.event.ClientEvent;

public class ChatRenderEvent
extends ClientEvent {
    private final CustomDrawContext context;
    private final float tickDelta;

    @Generated
    public CustomDrawContext getContext() {
        return this.context;
    }

    @Generated
    public float getTickDelta() {
        return this.tickDelta;
    }

    @Generated
    public ChatRenderEvent(CustomDrawContext customDrawContext, float f) {
        this.context = customDrawContext;
        this.tickDelta = f;
    }
}

