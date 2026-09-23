package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="finish_eat")
public class FinishEatEvent
extends ClientEvent {
    private final PlayerEntity user;
    private final ItemStack stack;

    @Generated
    public PlayerEntity getUser() {
        return this.user;
    }

    @Generated
    public ItemStack getStack() {
        return this.stack;
    }

    @Generated
    public FinishEatEvent(PlayerEntity playerEntity, ItemStack itemStack) {
        this.user = playerEntity;
        this.stack = itemStack;
    }
}

