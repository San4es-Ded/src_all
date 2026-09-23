package haron.modules.utilities;

import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.BooleanSetting;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

@ModuleInfo(a="Healing Helper", b="Highlights useful healing items.", c=ModuleCategory.UTILITIES)
public class HealingHelper
extends HaronModule {
    private final BooleanSetting onlyWhenNeeded = new BooleanSetting("Только при необходимости", true);
    private final NumberSetting healthThreshold = new NumberSetting("Порог здоровья", 14.0f, 1.0f, 20.0f, 1.0f);
    private final NumberSetting hungerThreshold = new NumberSetting("Порог голода", 15.0f, 1.0f, 20.0f, 1.0f);
    private final NumberSetting minimumPriority = new NumberSetting("Минимальный приоритет", 1.0f, 1.0f, 4.0f, 1.0f);
    private final Map<Item, Integer> itemPriority;

    private boolean shouldUseNow(Item item) {
        if (HealingHelper.c.player == null) {
            return true;
        }
        return item == Items.GOLDEN_CARROT ? HealingHelper.c.player.getHungerManager().getFoodLevel() <= this.hungerThreshold.b() : HealingHelper.c.player.getHealth() + HealingHelper.c.player.getAbsorptionAmount() <= this.healthThreshold.a();
    }

    public HealingHelper() {
        HashMap<Item, Integer> hashMap = new HashMap<Item, Integer>();
        hashMap.put(Items.POTION, 1);
        hashMap.put(Items.GOLDEN_CARROT, 2);
        hashMap.put(Items.GOLDEN_APPLE, 3);
        hashMap.put(Items.ENCHANTED_GOLDEN_APPLE, 4);
        this.itemPriority = Collections.unmodifiableMap(hashMap);
    }

    public Map<Item, Integer> s() {
        return this.itemPriority;
    }

    public List<Item> n() {
        return new ArrayList<Item>(this.itemPriority.keySet());
    }

    public int a(Item item) {
        return this.itemPriority.getOrDefault(item, 0);
    }

    public boolean a(ItemStack itemStack) {
        if (itemStack == null || itemStack.isEmpty() || this.a(itemStack.getItem()) < this.minimumPriority.b()) {
            return false;
        }
        return !this.onlyWhenNeeded.a() || this.shouldUseNow(itemStack.getItem());
    }

    public BooleanSetting o() {
        return this.onlyWhenNeeded;
    }

    public NumberSetting p() {
        return this.healthThreshold;
    }

    public NumberSetting q() {
        return this.hungerThreshold;
    }

    public NumberSetting r() {
        return this.minimumPriority;
    }
}

