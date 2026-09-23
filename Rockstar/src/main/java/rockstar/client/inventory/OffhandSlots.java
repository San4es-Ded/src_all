package rockstar.client.inventory;


import rockstar.client.*;
import java.util.List;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.OffhandSlot;

public class OffhandSlots
extends SlotCollection<OffhandSlot> {
    public OffhandSlots() {
        super(List.of(new OffhandSlot()));
    }
}

