package rockstar.client.internal.inventory;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.internal.core.ServerTypeChecks;

public class WrathPotionSwapItem
extends AbstractAssistSwapItem {
    public WrathPotionSwapItem() {
        super("modules.settings.assist.wrath_potion", Items.SPLASH_POTION.getDefaultStack(), AssistItemCategory.internalField1162);
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
        return itemStack.getName().getString().contains("\u0417\u0435\u043b\u044c\u0435 \u0433\u043d\u0435\u0432\u0430");
    }
}

