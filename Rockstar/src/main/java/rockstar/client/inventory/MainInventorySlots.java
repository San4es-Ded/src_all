package rockstar.client.inventory;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.MainInventorySlot;

public class MainInventorySlots
extends SlotCollection<MainInventorySlot> {
    public MainInventorySlots() {
        super(MainInventorySlots.internalMethod02122());
    }

    private static List<MainInventorySlot> internalMethod02122() {
        ArrayList<MainInventorySlot> arrayList = new ArrayList<MainInventorySlot>();
        for (int i = 0; i < 27; ++i) {
            arrayList.add(new MainInventorySlot(i));
        }
        return arrayList;
    }
}

