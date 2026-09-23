package rockstar.client.inventory;


import rockstar.client.*;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import rockstar.client.inventory.InventorySlot;

public class HotbarSlot
extends InventorySlot {
    private final int internalField0227;

    public HotbarSlot(int n) {
        if (n < 0 || n > 8) {
            throw new IllegalArgumentException("Hotbar Slot ID must be between 0 and 8");
        }
        this.internalField0227 = n;
    }

    @Override
    public ItemStack internalMethod03427() {
        if (HotbarSlot.internalField0149.player == null || HotbarSlot.internalField0149.player.getInventory() == null) {
            return ItemStack.EMPTY;
        }
        return HotbarSlot.internalField0149.player.getInventory().getStack(this.internalField0227);
    }

    @Override
    public int internalMethod06662() {
        return 36 + this.internalField0227;
    }

    @Generated
    public int internalMethod08745() {
        return this.internalField0227;
    }
}

