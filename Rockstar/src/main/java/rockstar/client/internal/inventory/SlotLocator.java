package rockstar.client.internal.inventory;



import rockstar.client.inventory.*;
import rockstar.client.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import rockstar.client.inventory.InventorySlot;

public interface SlotLocator {
    @Nullable
    public InventorySlot internalMethod02934(Item localValue1);

    @Nullable
    public InventorySlot internalMethod04352(Item localValue1);

    @Nullable
    public InventorySlot internalMethod05459(ItemStack localValue1);

    @Nullable
    public InventorySlot internalMethod04947(int localValue1);

    public int internalMethod04400(Item localValue1);
}

