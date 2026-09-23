package rockstar.client.inventory;


import rockstar.client.*;
import java.util.function.Predicate;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.inventory.InventoryUtils;

public abstract class InventorySlot
implements MinecraftClientAccess {
    public abstract ItemStack internalMethod03427();

    public abstract int internalMethod06662();

    public int internalMethod06667() {
        if (InventorySlot.internalField0149.player == null || InventorySlot.internalField0149.player.currentScreenHandler == null) {
            return 0;
        }
        return InventorySlot.internalField0149.player.currentScreenHandler.syncId;
    }

    public Item internalMethod00210() {
        return this.internalMethod03427().getItem();
    }

    public boolean internalMethod06664() {
        return this.internalMethod03427().isEmpty();
    }

    public boolean internalMethod03381(Item item) {
        return this.internalMethod03427().getItem() == item;
    }

    public boolean internalMethod07038(Predicate<ItemStack> predicate) {
        return predicate.test(this.internalMethod03427());
    }

    public void internalMethod05183(InventorySlot typedValue222) {
        InventoryUtils.internalMethod01016(this, typedValue222);
    }

    public void internalMethod06663() {
        InventoryUtils.internalMethod02096(this);
    }

    public void internalMethod06668() {
        if (InventorySlot.internalField0149.interactionManager == null) {
            return;
        }
        InventorySlot.internalField0149.interactionManager.clickSlot(this.internalMethod06667(), this.internalMethod06662(), 0, SlotActionType.PICKUP, (PlayerEntity)InventorySlot.internalField0149.player);
    }
}

