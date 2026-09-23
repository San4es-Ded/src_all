package rockstar.client.internal.inventory;



import rockstar.client.data.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import rockstar.client.data.AuctionItem;
import rockstar.client.internal.inventory.DonItemCatalog;

public class AuctionItemSelection {
    private ItemStack internalField0878;
    private DonItemCatalog.InternalType0190 internalField0408;

    public void internalMethod03772(DonItemCatalog.InternalType0190 nestedValue0082) {
        this.internalField0878 = nestedValue0082.internalMethod05752();
        this.internalField0408 = nestedValue0082;
    }

    public void internalMethod07084() {
        this.internalField0878 = null;
        this.internalField0408 = null;
    }

    public void internalMethod00962(ItemStack itemStack) {
        if (this.internalField0878 != null && ItemStack.areItemsAndComponentsEqual((ItemStack)this.internalField0878, (ItemStack)itemStack)) {
            this.internalMethod07084();
        }
    }

    public String internalMethod05808() {
        return this.internalField0408 != null ? this.internalField0408.internalMethod06194() : null;
    }

    public String internalMethod02358() {
        return this.internalField0408 != null ? this.internalField0408.internalMethod02702() : null;
    }

    public boolean internalMethod03773(DonItemCatalog.InternalType0190 nestedValue0082) {
        return this.internalField0408 != null && this.internalField0408.internalMethod02702() != null && this.internalField0408.internalMethod02702().equals(nestedValue0082.internalMethod02702());
    }

    public boolean internalMethod03954(DonItemCatalog.InternalType0190 nestedValue0082) {
        for (AuctionItem.InternalType0159 nestedValue0068 : AuctionItem.internalMethod00895()) {
            if (nestedValue0068.internalMethod00891() == null || !nestedValue0068.internalMethod00891().equals(nestedValue0082.internalMethod02702())) continue;
            return true;
        }
        return false;
    }

    @Generated
    public ItemStack internalMethod05876() {
        return this.internalField0878;
    }

    @Generated
    public DonItemCatalog.InternalType0190 internalMethod05976() {
        return this.internalField0408;
    }
}

