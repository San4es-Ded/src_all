package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.client.gui.screen.Screen;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="close_screen")
public class CloseScreenEvent
extends ClientEvent {
    private final Screen screen;

    @Generated
    public Screen getScreen() {
        return this.screen;
    }

    @Generated
    public CloseScreenEvent(Screen screen) {
        this.screen = screen;
    }
}

