package haron.events;

import haron.events.CancellableEvent;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class PlayerInteractEvent
extends CancellableEvent {
    private final PlayerEntity player;
    private final World world;
    private final Hand hand;

    public PlayerEntity player() {
        return this.player;
    }

    public World world() {
        return this.world;
    }

    public Hand hand() {
        return this.hand;
    }

    public PlayerInteractEvent(PlayerEntity playerEntity, World world, Hand hand) {
        this.player = playerEntity;
        this.world = world;
        this.hand = hand;
    }

    public World e() {
        return this.world;
    }

    public Hand f() {
        return this.hand;
    }

    public PlayerEntity d() {
        return this.player;
    }
}

