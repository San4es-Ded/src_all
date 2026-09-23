package rockstar.modules.combat;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.*;

import java.util.List;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Hand;
import rockstar.client.setting.SliderSetting;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.inventory.MainInventorySlot;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;

@ModuleInfo(name="Auto Soup", category=ModuleCategory.COMBAT)
public class AutoSoupModule
extends Module {
    int internalField0227 = -1;
    int internalField0228 = -1;
    int internalField1053 = -1;
    private SliderSetting internalField0383;
    private final Stopwatch internalField0519 = new Stopwatch();

    public AutoSoupModule() {
        this.internalMethod09822();
    }

    private void internalMethod09822() {
        this.internalField0383 = new SliderSetting(this, "modules.settings.auto_soup.health").internalMethod08673(1.0f).internalMethod05900(1.0f).internalMethod02732(20.0f).internalMethod08074(10.0f);
    }

    @Override
    public void internalMethod08229() {
        if (this.internalField1053 >= 0) {
            if (this.internalField1053 == 2) {
                AutoSoupModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField0228);
            } else if (this.internalField1053 == 1) {
                AutoSoupModule.internalField0149.interactionManager.interactItem((PlayerEntity)AutoSoupModule.internalField0149.player, Hand.MAIN_HAND);
            } else if (this.internalField1053 == 0) {
                AutoSoupModule.internalField0149.player.dropSelectedItem(true);
                AutoSoupModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField0227);
            }
            --this.internalField1053;
            return;
        }
        if (AutoSoupModule.internalField0149.player.getHealth() >= this.internalField0383.internalMethod08576() || !this.internalField0519.internalMethod02365(300L)) {
            return;
        }
        HotbarSlot typedValue231 = InventorySlots.internalMethod02872().internalMethod02510(Items.MUSHROOM_STEW);
        if (typedValue231 != null) {
            this.internalField0227 = AutoSoupModule.internalField0149.player.getInventory().getSelectedSlot();
            AutoSoupModule.internalField0149.player.getInventory().setSelectedSlot(this.internalField0228 = typedValue231.internalMethod08745());
            this.internalField1053 = 1;
        } else {
            List<MainInventorySlot> list = InventorySlots.internalMethod03558().internalMethod07613(Items.MUSHROOM_STEW);
            List<HotbarSlot> list2 = InventorySlots.internalMethod02872().internalMethod00168(ItemStack::isEmpty);
            if (!list.isEmpty() && !list2.isEmpty()) {
                int n = Math.min(list.size(), list2.size());
                n = Math.min(n, 8);
                for (int i = 0; i < n; ++i) {
                    MainInventorySlot typedValue233 = list.get(i);
                    HotbarSlot typedValue232 = list2.get(i);
                    InventoryUtils.internalMethod08821(typedValue233.internalMethod06662(), typedValue232.internalMethod08745());
                }
                this.internalField0227 = AutoSoupModule.internalField0149.player.getInventory().getSelectedSlot();
                this.internalField0228 = list2.get(0).internalMethod08745();
                this.internalField1053 = 2;
            }
        }
        this.internalField0519.internalMethod00701();
    }
}
