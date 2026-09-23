package haron.events;

import net.minecraft.item.ItemStack;

public class ItemTooltipEvent {
    private final ItemStack stack;
    private final int x;
    private final int y;

    public ItemTooltipEvent(ItemStack itemStack, int n, int n2) {
        this.stack = itemStack;
        this.x = n;
        this.y = n2;
    }

    public ItemStack stack() {
        return this.stack;
    }

    public int b() {
        return this.x;
    }

    public int x() {
        return this.x;
    }

    public int c() {
        return this.y;
    }

    public ItemStack a() {
        return this.stack;
    }

    public int y() {
        return this.y;
    }
}

