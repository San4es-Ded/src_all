package rockstar.client.internal.inventory;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.function.Predicate;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import rockstar.client.internal.core.ItemCooldownHolder;
import rockstar.client.internal.core.SequenceStep;

public class ItemSwapAction
implements SequenceStep {
    private final Item internalField0152;
    private final Predicate<ItemStack> internalField0486;
    private boolean internalField0277 = false;

    public ItemSwapAction(Item item, Predicate<ItemStack> predicate) {
        this.internalField0152 = item;
        this.internalField0486 = predicate;
    }

    public ItemSwapAction(Item item) {
        this(item, null);
    }

    @Override
    public void internalMethod01925() {
        if (this.internalField0486 != null) {
            ItemCooldownHolder.internalField0006.internalMethod02474(this.internalField0152, this.internalField0486);
        } else {
            ItemCooldownHolder.internalField0006.internalMethod07082(this.internalField0152);
        }
        this.internalField0277 = true;
    }

    @Override
    public boolean internalMethod01926() {
        return this.internalField0277;
    }

    @Override
    public void internalMethod01928() {
        this.internalField0277 = false;
    }
}

