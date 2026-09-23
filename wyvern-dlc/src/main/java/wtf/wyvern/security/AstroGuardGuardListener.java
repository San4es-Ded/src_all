package wtf.wyvern.security;

import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;

public final class AstroGuardGuardListener {

    public AstroGuardGuardListener() {
        EventManager.register(this);
    }

    @EventTarget
    public void onUpdate(EventUpdate event) {
        AstroGuardAuth.guard();
    }
}
