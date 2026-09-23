package rockstar.client.internal.inventory;





import rockstar.client.setting.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.item.ItemStack;
import rockstar.client.setting.Setting;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.core.AssistItemCategory;

public interface AssistSwapItem {
    public String internalMethod06026();

    public ItemStack internalMethod06489();

    public AssistItemCategory internalMethod06886();

    public int internalMethod03234();

    public void internalMethod01910(int localValue1);

    public void internalMethod03235();

    public boolean internalMethod03236();

    default public boolean internalMethod04619() {
        return false;
    }

    default public void internalMethod04618() {
    }

    default public boolean internalMethod07961() {
        return false;
    }

    default public String internalMethod00556() {
        return LanguageManager.internalMethod07214(this.internalMethod06026());
    }

    default public boolean internalMethod05467(ItemStack itemStack) {
        return itemStack != null && !itemStack.isEmpty() && itemStack.getItem() == this.internalMethod06489().getItem();
    }

    public List<Setting> getSettings();
}

