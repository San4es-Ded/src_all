package rockstar.client.internal.inventory;



import rockstar.client.inventory.*;
import rockstar.client.*;
import java.util.Comparator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.inventory.CustomItemRankingUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.internal.inventory.SlotLocator;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;

public class RankedSlotLocator
implements SlotLocator {
    private static final SlotCollection<InventorySlot> internalField0221 = InventorySlots.internalMethod07766().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod02872());
    private static final SlotCollection<InventorySlot> internalField0222 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872()).internalMethod07591(InventorySlots.internalMethod07766());
    private static final SlotCollection<InventorySlot> internalField1051 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
    private static final Comparator<InventorySlot> internalField0757 = Comparator.comparingInt(typedValue222 -> CustomItemRankingUtils.internalMethod04472(typedValue222.internalMethod03427()));

    @Override
    @Nullable
    public InventorySlot internalMethod02934(Item item) {
        if (item == Items.TOTEM_OF_UNDYING) {
            return internalField1051.internalMethod07613(item).stream().max(internalField0757).orElse(null);
        }
        return internalField0221.internalMethod07613(item).stream().findFirst().orElse(null);
    }

    @Override
    @Nullable
    public InventorySlot internalMethod04352(Item item) {
        if (item == Items.TOTEM_OF_UNDYING) {
            return internalField1051.internalMethod07613(item).stream().min(internalField0757).orElse(null);
        }
        return internalField1051.internalMethod07613(item).stream().findFirst().orElse(null);
    }

    @Override
    @Nullable
    public InventorySlot internalMethod05459(ItemStack itemStack) {
        return internalField0222.internalMethod02638().stream().filter(typedValue222 -> ItemStack.areEqual((ItemStack)typedValue222.internalMethod03427(), (ItemStack)itemStack)).findFirst().orElse(null);
    }

    @Override
    @Nullable
    public InventorySlot internalMethod04947(int n) {
        return internalField0221.internalMethod02638().stream().filter(typedValue222 -> typedValue222.internalMethod06662() == n).findFirst().orElse(null);
    }

    @Override
    public int internalMethod04400(Item item) {
        return InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872()).internalMethod07591(InventorySlots.internalMethod07766()).internalMethod07613(item).size();
    }
}

