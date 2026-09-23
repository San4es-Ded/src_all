package rockstar.client.internal.inventory;






import rockstar.client.notification.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import rockstar.client.internal.core.ItemCooldownHolder;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.notification.NotificationType;

public class BackpackSwapItem
extends AbstractAssistSwapItem {
    private static final List<Item> internalField0416 = List.of(Items.MAGENTA_SHULKER_BOX, Items.PURPLE_SHULKER_BOX, Items.RED_SHULKER_BOX, Items.PINK_SHULKER_BOX, Items.BLUE_SHULKER_BOX);

    public BackpackSwapItem() {
        super("modules.settings.assist.backpack", Items.SHULKER_BOX.getDefaultStack(), AssistItemCategory.internalField1161);
    }

    public void internalMethod08913() {
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod02872().internalMethod07591(InventorySlots.internalMethod03558()).internalMethod07591(InventorySlots.internalMethod07766());
        boolean bl = internalField0416.stream().anyMatch(item -> typedValue228.internalMethod03297(itemStack -> itemStack != null && !itemStack.isEmpty() && itemStack.getItem() == item) != null);
        if (!bl) {
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0705, LanguageManager.internalMethod07214("swap.item_not_found"), LanguageManager.internalMethod00160("swap.item_required", LanguageManager.internalMethod07214("modules.settings.assist.backpack").toLowerCase()));
            return;
        }
        for (Item item2 : internalField0416) {
            ItemCooldownHolder.internalField0006.internalMethod07082(item2);
        }
    }
}

