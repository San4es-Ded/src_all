package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.util.Identifier;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="set_cooldown")
public class EventSetCooldown
extends ClientEvent {
    private int cooldown;
    private Identifier cooldownGroup;

    @Generated
    public int getCooldown() {
        return this.cooldown;
    }

    @Generated
    public Identifier getCooldownGroup() {
        return this.cooldownGroup;
    }

    @Generated
    public void setCooldown(int n) {
        this.cooldown = n;
    }

    @Generated
    public void setCooldownGroup(Identifier identifier) {
        this.cooldownGroup = identifier;
    }

    @Generated
    public EventSetCooldown(int n, Identifier identifier) {
        this.cooldown = n;
        this.cooldownGroup = identifier;
    }
}

