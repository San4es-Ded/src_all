package rockstar.client.inventory;


import rockstar.client.*;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import rockstar.client.inventory.InventorySlot;

public class ArmorSlot
extends InventorySlot {
    private final int internalField0227;

    public ArmorSlot(int n) {
        if (n < 0 || n > 3) {
            throw new IllegalArgumentException("Armor Slot Index must be between 0 and 3");
        }
        this.internalField0227 = n;
    }

    @Override
    public ItemStack internalMethod03427() {
        if (ArmorSlot.internalField0149.player == null || ArmorSlot.internalField0149.player.getInventory() == null) {
            return ItemStack.EMPTY;
        }
        return rockstar.client.util.LegacyItemTypes.armorItem(ArmorSlot.internalField0149.player.getInventory(), this.internalField0227);
    }

    @Override
    public int internalMethod06662() {
        return 8 - this.internalField0227;
    }

    @Generated
    public int internalMethod07772() {
        return this.internalField0227;
    }
}

