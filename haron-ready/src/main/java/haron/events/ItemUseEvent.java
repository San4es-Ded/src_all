package haron.events;

import haron.events.CancellableEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemUseEvent
extends CancellableEvent {
    private final ItemStack stack;
    private final World world;
    private final LivingEntity user;

    public World world() {
        return this.world;
    }

    public ItemUseEvent(ItemStack itemStack, World world, LivingEntity livingEntity) {
        this.stack = itemStack;
        this.world = world;
        this.user = livingEntity;
    }

    public ItemStack stack() {
        int n = 842;
        return this.stack;
    }

    public World e() {
        return this.world;
    }

    public LivingEntity f() {
        return this.user;
    }

    public ItemStack d() {
        return this.stack;
    }

    public LivingEntity user() {
        return this.user;
    }
}

