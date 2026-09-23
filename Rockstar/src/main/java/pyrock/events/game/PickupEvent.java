package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="pickup")
public class PickupEvent
extends ClientEvent {
    private Entity entity;
    private ItemStack itemStack;
    private int count;

    @Generated
    public Entity getEntity() {
        return this.entity;
    }

    @Generated
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    @Generated
    public int getCount() {
        return this.count;
    }

    @Generated
    public PickupEvent(Entity entity, ItemStack itemStack, int n) {
        this.entity = entity;
        this.itemStack = itemStack;
        this.count = n;
    }
}

