package rockstar.client.inventory;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.HotbarSlot;

public class HotbarSlots
extends SlotCollection<HotbarSlot> {
    public HotbarSlots() {
        super(HotbarSlots.internalMethod03384());
    }

    private static List<HotbarSlot> internalMethod03384() {
        ArrayList<HotbarSlot> arrayList = new ArrayList<HotbarSlot>();
        for (int i = 0; i < 9; ++i) {
            arrayList.add(new HotbarSlot(i));
        }
        return arrayList;
    }
}

