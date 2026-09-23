package rockstar.client.inventory;


import rockstar.client.*;
import net.minecraft.item.ItemStack;
import rockstar.client.inventory.InventorySlot;

public class OffhandSlot
extends InventorySlot {
    @Override
    public ItemStack internalMethod03427() {
        if (OffhandSlot.internalField0149.player == null || OffhandSlot.internalField0149.player.getInventory() == null) {
            return ItemStack.EMPTY;
        }
        return OffhandSlot.internalField0149.player.getOffHandStack();
    }

    @Override
    public int internalMethod06662() {
        return 45;
    }
}
