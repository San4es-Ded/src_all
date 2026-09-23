package rockstar.modules.player;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import pyrock.events.game.StartBreakBlockEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.window.KeyPressEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.KeybindSetting;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationManager;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;
import rockstar.client.util.Stopwatch;
import rockstar.client.notification.NotificationType;

@ModuleInfo(name="Mine Helper", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.mine_helper")
public class MineHelperModule
extends Module {
    private BooleanSetting internalField0650;
    public SliderSetting internalField0383;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private KeybindSetting internalField0648;
    private final Stopwatch internalField0519 = new Stopwatch();
    private boolean internalField0277;
    private boolean internalField0276 = false;
    private final EventListener<KeyPressEvent> internalField0157 = keyPressEvent -> {
        if (this.internalField0648.internalMethod02165(keyPressEvent.getKey()) && keyPressEvent.getAction() == 1) {
            this.internalField0277 = true;
        }
    };
    private final EventListener<StartBreakBlockEvent> internalField0158 = startBreakBlockEvent -> {
        if (MineHelperModule.internalField0149.player == null) {
            return;
        }
        ItemStack itemStack = MineHelperModule.internalField0149.player.getMainHandStack();
        if (!this.internalMethod01154(itemStack)) {
            return;
        }
        double d = this.internalMethod04040(itemStack);
        if (!this.internalField0650.internalMethod04496() || d >= (double)this.internalField0383.internalMethod08576()) {
            return;
        }
        startBreakBlockEvent.cancel();
        this.internalMethod04041(itemStack);
    };
    private final EventListener<ClientPlayerTickEvent> internalField1028 = clientPlayerTickEvent -> {
        if (MineHelperModule.internalField0149.player == null || !this.internalField0277) {
            return;
        }
        ItemStack itemStack = MineHelperModule.internalField0149.player.getMainHandStack();
        RotationManager typedValue269 = RockstarClient.getInstance().internalMethod02368();
        if (this.internalMethod04040(itemStack) >= 30.0) {
            this.internalField0277 = false;
            return;
        }
        if (this.internalMethod01154(itemStack) && this.internalMethod09380()) {
            typedValue269.internalMethod00418(new Rotation(MineHelperModule.internalField0149.player.getYaw(), 88.0f), RotationBehavior.internalField1003, 180.0f, 80.0f, 80.0f, RotationPriority.internalField1012);
            if (this.internalField0519.internalMethod02365(70L)) {
                MineHelperModule.internalField0149.interactionManager.sendSequencedPacket(MineHelperModule.internalField0149.world, n -> new PlayerInteractItemC2SPacket(Hand.OFF_HAND, n, typedValue269.internalMethod08209().internalMethod00169(), 90.0f));
                this.internalField0519.internalMethod00701();
            }
        }
    };

    public MineHelperModule() {
        this.internalMethod09379();
    }

    private void internalMethod09379() {
        this.internalField0650 = new BooleanSetting((SettingOwner)this, "modules.settings.mine_helper.save_pickaxe", "modules.settings.mine_helper.save_pickaxe.description").internalMethod06630();
        this.internalField0383 = new SliderSetting(this, "modules.settings.mine_helper.percent").internalMethod08673(1.0f).internalMethod05900(1.0f).internalMethod02732(70.0f).internalMethod08074(10.0f).internalMethod06240("%");
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.mine_helper.auto_replace", "modules.settings.mine_helper.auto_replace.description");
        this.internalField1261 = new BooleanSetting((SettingOwner)this, "modules.settings.mine_helper.auto_repair", "modules.settings.mine_helper.auto_repair.description");
        this.internalField0648 = new KeybindSetting(this, "modules.settings.mine_helper.fix_key", () -> !this.internalField1261.internalMethod04496());
    }

    private void internalMethod04041(ItemStack itemStack) {
        boolean bl = false;
        if (this.internalField0651.internalMethod04496()) {
            bl = this.internalMethod04042(itemStack);
        }
        if (!bl && this.internalField0519.internalMethod02365(800L)) {
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0705, LanguageManager.internalMethod07214("mine_helper.pickaxe_almost_broken"), LanguageManager.internalMethod07214("mine_helper.no_replacement"));
            this.internalField0519.internalMethod00701();
        }
    }

    private boolean internalMethod09380() {
        if (MineHelperModule.internalField0149.player.getOffHandStack().getItem() == Items.EXPERIENCE_BOTTLE) {
            this.internalField0276 = false;
            return true;
        }
        SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872()).internalMethod07591(InventorySlots.internalMethod07766());
        InventorySlot typedValue222 = typedValue228.internalMethod03297(itemStack -> itemStack.getItem() == Items.EXPERIENCE_BOTTLE);
        if (typedValue222 == null) {
            if (!this.internalField0276) {
                RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0705, LanguageManager.internalMethod07214("mine_helper.no_bottles"), LanguageManager.internalMethod07214("mine_helper.need_bottles"));
                this.internalField0276 = true;
            }
            return false;
        }
        InventoryUtils.internalMethod01016(typedValue222, InventoryUtils.internalMethod06162());
        return true;
    }

    private boolean internalMethod04042(ItemStack itemStack) {
        HotbarSlot typedValue231 = this.internalMethod04077(itemStack);
        if (typedValue231 == null) {
            return false;
        }
        InventoryUtils.internalMethod01980(typedValue231);
        if (this.internalField0519.internalMethod02365(800L)) {
            ItemStack itemStack2 = typedValue231.internalMethod03427();
            RockstarClient.getInstance().internalMethod02503().internalMethod00599(NotificationType.internalField0704, LanguageManager.internalMethod07214("mine_helper.pickaxe_swap"), LanguageManager.internalMethod00160("mine_helper.pickaxe_swapped", this.internalMethod04040(itemStack), this.internalMethod04040(itemStack2)));
            this.internalField0519.internalMethod00701();
        }
        return true;
    }

    private HotbarSlot internalMethod04077(ItemStack itemStack) {
        double d = this.internalMethod04040(itemStack);
        HotbarSlot typedValue231 = null;
        double d2 = d;
        for (int i = 0; i < 9; ++i) {
            double d3;
            HotbarSlot typedValue232 = InventoryUtils.internalMethod02738(i);
            ItemStack itemStack2 = typedValue232.internalMethod03427();
            if (!this.internalMethod01154(itemStack2) || !((d3 = this.internalMethod04040(itemStack2)) > d2)) continue;
            d2 = d3;
            typedValue231 = typedValue232;
        }
        return typedValue231;
    }

    private boolean internalMethod01154(ItemStack itemStack) {
        return itemStack != null && itemStack.isDamageable() && LegacyItemTypes.isPickaxe(itemStack);
    }

    private double internalMethod04040(ItemStack itemStack) {
        return (double)(itemStack.getMaxDamage() - itemStack.getDamage()) / (double)itemStack.getMaxDamage() * 100.0;
    }

    @Override
    public void onDisable() {
        this.internalField0277 = false;
        this.internalField0276 = false;
    }
}
