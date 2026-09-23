package rockstar.client.internal.inventory;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.internal.core.ServerTypeChecks;

public class HolyWaterPotionSwapItem
extends AbstractAssistSwapItem {
    public HolyWaterPotionSwapItem() {
        super("modules.settings.assist.holy_water", Items.SPLASH_POTION.getDefaultStack(), AssistItemCategory.internalField1162);
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
        return itemStack.getName().getString().contains("\u0421\u0432\u044f\u0442\u0430\u044f \u0432\u043e\u0434\u0430");
    }
}

