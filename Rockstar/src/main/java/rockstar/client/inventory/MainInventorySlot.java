package rockstar.client.inventory;


import rockstar.client.*;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import rockstar.client.inventory.InventorySlot;

public class MainInventorySlot
extends InventorySlot {
    private final int internalField0227;

    public MainInventorySlot(int n) {
        if (n < 0 || n > 26) {
            throw new IllegalArgumentException("Inventory Slot ID must be between 0 and 26");
        }
        this.internalField0227 = n;
    }

    @Override
    public ItemStack internalMethod03427() {
        if (MainInventorySlot.internalField0149.player == null || MainInventorySlot.internalField0149.player.getInventory() == null) {
            return ItemStack.EMPTY;
        }
        return MainInventorySlot.internalField0149.player.getInventory().getStack(this.internalField0227 + 9);
    }

    @Override
    public int internalMethod06662() {
        return this.internalField0227 + 9;
    }

    @Generated
    public int internalMethod08633() {
        return this.internalField0227;
    }
}

