package rockstar.client.inventory;


import rockstar.client.*;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.ArmorSlots;
import rockstar.client.inventory.HotbarSlots;
import rockstar.client.inventory.MainInventorySlots;
import rockstar.client.inventory.OffhandSlots;
import rockstar.client.inventory.ArmorSlot;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.inventory.MainInventorySlot;
import rockstar.client.inventory.OffhandSlot;

public class InventorySlots {
    private InventorySlots() {
    }

    public static SlotCollection<HotbarSlot> internalMethod02872() {
        return new HotbarSlots();
    }

    public static SlotCollection<MainInventorySlot> internalMethod03558() {
        return new MainInventorySlots();
    }

    public static SlotCollection<ArmorSlot> internalMethod08231() {
        return new ArmorSlots();
    }

    public static SlotCollection<OffhandSlot> internalMethod07766() {
        return new OffhandSlots();
    }
}

