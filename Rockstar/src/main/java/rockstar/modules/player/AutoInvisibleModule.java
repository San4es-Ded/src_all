package rockstar.modules.player;






import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;

import java.util.Map;
import java.util.TreeMap;
import lombok.Generated;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.slot.SlotActionType;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.BooleanSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.inventory.PotionItemUtils;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.SlotCollection;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.OffhandSlot;
import rockstar.client.module.Module;

@ModuleInfo(name="Auto Invisible", category=ModuleCategory.PLAYER, internalMethod09633="modules.descriptions.auto_invisible")
public class AutoInvisibleModule
extends Module {
    private final Map<String, StatusEffectInstance> internalField0543 = new TreeMap<String, StatusEffectInstance>();
    private boolean internalField0277;
    private BooleanSetting internalField0650;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> this.internalMethod09728();

    public AutoInvisibleModule() {
        this.internalMethod09720();
    }

    private void internalMethod09720() {
        this.internalField0650 = new BooleanSetting(this, "modules.settings.auto_invisible.pre_drink");
    }

    private void internalMethod09728() {
        boolean bl;
        boolean bl2 = AutoInvisibleModule.internalField0149.player.hasStatusEffect(StatusEffects.INVISIBILITY);
        StatusEffectInstance statusEffectInstance = bl2 ? AutoInvisibleModule.internalField0149.player.getStatusEffect(StatusEffects.INVISIBILITY) : null;
        boolean bl3 = bl = !bl2;
        if (this.internalField0650.internalMethod04496() && statusEffectInstance != null && statusEffectInstance.getDuration() <= 200) {
            bl = true;
        }
        if (bl) {
            ItemStack itemStack = AutoInvisibleModule.internalField0149.player.getOffHandStack();
            boolean bl4 = this.internalMethod03572(itemStack);
            SlotCollection<InventorySlot> typedValue228 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
            InventorySlot typedValue222 = typedValue228.internalMethod03297(this::internalMethod03572);
            OffhandSlot typedValue234 = new OffhandSlot();
            if (typedValue222 != null && !bl4) {
                InventoryUtils.internalMethod01016(typedValue222, typedValue234);
            }
            if (bl4) {
                this.internalField0277 = true;
                AutoInvisibleModule.internalField0149.options.useKey.setPressed(true);
            }
        } else if (this.internalField0277) {
            AutoInvisibleModule.internalField0149.options.useKey.setPressed(false);
            this.internalField0277 = false;
            ItemStack itemStack = AutoInvisibleModule.internalField0149.player.getOffHandStack();
            if (itemStack.getItem() == Items.GLASS_BOTTLE) {
                AutoInvisibleModule.internalField0149.interactionManager.clickSlot(0, 45, 1, SlotActionType.THROW, (PlayerEntity)AutoInvisibleModule.internalField0149.player);
            }
        }
    }

    private boolean internalMethod03572(ItemStack itemStack) {
        return PotionItemUtils.internalMethod05184(itemStack, (RegistryEntry<StatusEffect>)StatusEffects.INVISIBILITY);
    }

    @Generated
    public Map<String, StatusEffectInstance> internalMethod00867() {
        return this.internalField0543;
    }
}
