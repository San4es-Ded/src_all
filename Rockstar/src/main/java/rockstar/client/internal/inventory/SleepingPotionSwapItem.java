package rockstar.client.internal.inventory;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.internal.core.ServerTypeChecks;

public class SleepingPotionSwapItem
extends AbstractAssistSwapItem {
    public SleepingPotionSwapItem() {
        super("modules.settings.assist.sleeping_potion", Items.SPLASH_POTION.getDefaultStack(), AssistItemCategory.internalField1162);
    }

    @Override
    public boolean internalMethod03236() {
        return ServerTypeChecks.internalMethod06293();
    }

    @Override
    public boolean internalMethod05467(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty() || itemStack.getItem() != Items.SPLASH_POTION) {
            return false;
        }
        return itemStack.getName().getString().contains("\u0421\u043d\u043e\u0442\u0432\u043e\u0440\u043d\u043e\u0435");
    }
}

