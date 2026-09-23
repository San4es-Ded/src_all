package haron.events;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;

public class TotemPopEvent {
    private final LivingEntity entity;
    private final ItemStack totemStack;

    public LivingEntity entity() {
        return this.entity;
    }

    public ItemStack totemStack() {
        return this.totemStack;
    }

    public TotemPopEvent(LivingEntity livingEntity, ItemStack itemStack) {
        this.entity = livingEntity;
        this.totemStack = itemStack;
    }

    public ItemStack b() {
        return this.totemStack;
    }

    public LivingEntity a() {
        return this.entity;
    }
}

