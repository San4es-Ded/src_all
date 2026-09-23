package rockstar.client.internal.inventory;



import rockstar.client.inventory.*;
import rockstar.client.*;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.internal.inventory.SlotSwapExecutor;

public class OffhandSwapExecutor
implements SlotSwapExecutor {
    @Override
    public boolean internalMethod04420(InventorySlot typedValue222) {
        InventoryUtils.internalMethod08821(typedValue222.internalMethod06662(), 40);
        return true;
    }

    @Override
    public boolean internalMethod01313(InventorySlot typedValue222) {
        InventoryUtils.internalMethod08821(typedValue222.internalMethod06662(), 40);
        return true;
    }
}

