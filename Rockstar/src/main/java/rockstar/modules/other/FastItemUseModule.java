package rockstar.modules.other;





import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.SettingOwner;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.setting.SliderSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.util.EnchantmentUtils;
import rockstar.client.module.Module;

@ModuleInfo(name="Fast Item Use", category=ModuleCategory.OTHER, internalMethod09633="modules.descriptions.fast_item_use")
public class FastItemUseModule
extends Module {
    private BooleanSetting internalField0650;
    private BooleanSetting internalField0651;
    private BooleanSetting internalField1261;
    private SliderSetting internalField0383;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (this.internalField0651.internalMethod04496() && this.internalMethod09572()) {
            this.internalMethod09573();
        }
        if (this.internalField0650.internalMethod04496() && this.internalMethod09574()) {
            this.internalMethod09573();
        }
        if (this.internalField1261.internalMethod04496() && this.internalMethod09713()) {
            this.internalMethod09573();
        }
    };

    public FastItemUseModule() {
        this.internalMethod09571();
    }

    private void internalMethod09571() {
        this.internalField0650 = new BooleanSetting((SettingOwner)this, "modules.settings.fast_item_use.bow", "modules.settings.fast_item_use.bow.description").internalMethod06630();
        this.internalField0651 = new BooleanSetting((SettingOwner)this, "modules.settings.fast_item_use.trident", "modules.settings.fast_item_use.trident.description").internalMethod06630();
        this.internalField1261 = new BooleanSetting((SettingOwner)this, "modules.settings.fast_item_use.crossbow", "modules.settings.fast_item_use.crossbow.description").internalMethod06630();
        this.internalField0383 = new SliderSetting(this, "modules.settings.fast_item_use.delay").internalMethod08074(10.0f).internalMethod02732(20.0f).internalMethod05900(1.0f).internalMethod08673(1.0f);
    }

    private void internalMethod09573() {
        if (FastItemUseModule.internalField0149.player == null) {
            return;
        }
        FastItemUseModule.internalField0149.player.networkHandler.sendPacket((Packet)new PlayerActionC2SPacket(PlayerActionC2SPacket.Action.RELEASE_USE_ITEM, BlockPos.ORIGIN, FastItemUseModule.internalField0149.player.getHorizontalFacing()));
        FastItemUseModule.internalField0149.player.stopUsingItem();
    }

    private boolean internalMethod09572() {
        if (FastItemUseModule.internalField0149.player == null) {
            return false;
        }
        ItemStack itemStack = FastItemUseModule.internalField0149.player.getMainHandStack();
        return itemStack.getItem() == Items.TRIDENT && EnchantmentUtils.internalMethod03526(itemStack, (RegistryKey<Enchantment>)Enchantments.RIPTIDE) > 0 && FastItemUseModule.internalField0149.player.isUsingItem() && (float)FastItemUseModule.internalField0149.player.getItemUseTime() >= this.internalField0383.internalMethod08576() && FastItemUseModule.internalField0149.player.getAttackCooldownProgress(0.5f) > 0.92f;
    }

    private boolean internalMethod09574() {
        if (FastItemUseModule.internalField0149.player == null) {
            return false;
        }
        return FastItemUseModule.internalField0149.player.getMainHandStack().getItem() == Items.BOW && FastItemUseModule.internalField0149.player.isUsingItem() && (float)FastItemUseModule.internalField0149.player.getItemUseTime() >= this.internalField0383.internalMethod08576();
    }

    private boolean internalMethod09713() {
        if (FastItemUseModule.internalField0149.player == null) {
            return false;
        }
        return FastItemUseModule.internalField0149.player.getMainHandStack().getItem() == Items.CROSSBOW && FastItemUseModule.internalField0149.player.isUsingItem() && (float)FastItemUseModule.internalField0149.player.getItemUseTime() >= this.internalField0383.internalMethod08576();
    }
}
