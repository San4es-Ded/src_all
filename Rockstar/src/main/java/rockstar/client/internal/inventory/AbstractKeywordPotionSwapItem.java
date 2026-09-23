package rockstar.client.internal.inventory;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import rockstar.client.internal.core.AssistItemCategory;
import rockstar.client.internal.inventory.AbstractAssistSwapItem;

public abstract class AbstractKeywordPotionSwapItem
extends AbstractAssistSwapItem {
    private final Item internalField0152;
    private final String[] internalField0359;

    public AbstractKeywordPotionSwapItem(String string, Item item, AssistItemCategory typedValue100, String ... stringArray) {
        super(string, item.getDefaultStack(), typedValue100);
        this.internalField0152 = item;
        this.internalField0359 = stringArray;
    }

    @Override
    public boolean internalMethod05467(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty() || itemStack.getItem() != this.internalField0152) {
            return false;
        }
        String string = this.internalMethod00963(itemStack.getName().getString());
        for (String string2 : this.internalField0359) {
            if (string.contains(this.internalMethod00963(string2))) continue;
            return false;
        }
        return true;
    }

    public Item internalMethod03447() {
        return this.internalField0152;
    }

    private String internalMethod00963(String string) {
        return string == null ? "" : string.toLowerCase().replace('\u0451', '\u0435');
    }
}

