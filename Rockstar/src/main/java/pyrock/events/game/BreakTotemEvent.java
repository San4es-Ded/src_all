package pyrock.events.game;


import rockstar.client.event.*;
import lombok.Generated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import rockstar.client.event.ClientEvent;
import rockstar.client.event.EventName;

@EventName(internalMethod03601="break_totem")
public class BreakTotemEvent
extends ClientEvent {
    private final LivingEntity entity;
    private final ItemStack stack;

    @Generated
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Generated
    public ItemStack getStack() {
        return this.stack;
    }

    @Generated
    public BreakTotemEvent(LivingEntity livingEntity, ItemStack itemStack) {
        this.entity = livingEntity;
        this.stack = itemStack;
    }
}

