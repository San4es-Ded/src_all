package rockstar.client.internal.inventory;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;
import rockstar.client.internal.core.ServerTypeChecks;

public class HlopushkaPotionSwapItem
extends AbstractAssistSwapItem {
    public HlopushkaPotionSwapItem() {
        super("modules.settings.assist.hlopushka", Items.SPLASH_POTION.getDefaultStack(), AssistItemCategory.internalField1162);
    }

    @Override
    public boolean internalMethod03236() {
        return ServerTypeChecks.internalMethod06293();
    }

    @Override
    public boolean internalMethod05467(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty()) {
            return false;
        }
        if (itemStack.getItem() != Items.SPLASH_POTION) {
            return false;
        }
        String string = itemStack.getName().getString();
        return string.contains("\u0425\u043b\u043e\u043f\u0443\u0448\u043a\u0430");
    }
}

