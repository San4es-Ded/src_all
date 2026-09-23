package rockstar.client.inventory;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.ArmorSlot;

public class ArmorSlots
extends SlotCollection<ArmorSlot> {
    public ArmorSlots() {
        super(ArmorSlots.internalMethod06872());
    }

    private static List<ArmorSlot> internalMethod06872() {
        ArrayList<ArmorSlot> arrayList = new ArrayList<ArmorSlot>();
        for (int i = 0; i < 4; ++i) {
            arrayList.add(new ArmorSlot(i));
        }
        return arrayList;
    }
}

