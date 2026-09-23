package rockstar.modules.combat;







import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SplashPotionItem;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Hand;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.setting.MultiSelectSetting;
import rockstar.client.event.EventListener;
import rockstar.client.module.ModuleCategory;
import rockstar.client.RockstarClient;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.inventory.PotionItemUtils;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.inventory.InventorySlots;
import rockstar.client.inventory.HotbarSlot;
import rockstar.client.inventory.MainInventorySlot;
import rockstar.client.rotation.RotationBehavior;
import rockstar.client.rotation.Rotation;
import rockstar.client.rotation.RotationPriority;
import rockstar.client.module.Module;

@ModuleInfo(name="Auto Potion", category=ModuleCategory.COMBAT, internalMethod09633="modules.descriptions.auto_potion")
public class AutoPotionModule
extends Module {
    private MultiSelectSetting internalField0675;
    private InternalType0255 internalField0687;
    private InternalType0255 internalField0688;
    private InternalType0255 internalField1278;
    private int internalField0227;
    private int internalField0228;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (AutoPotionModule.internalField0149.player == null || AutoPotionModule.internalField0149.world == null || AutoPotionModule.internalField0149.interactionManager == null || internalField0149.getNetworkHandler() == null || AutoPotionModule.internalField0149.player.isGliding()) {
            return;
        }
        ++this.internalField0227;
        List<InventorySlot> list = this.internalMethod04078();
        if (this.internalField0227 < 20 || list.isEmpty()) {
            this.internalField0228 = 0;
            return;
        }
        float f = AutoPotionModule.internalField0149.player.getYaw();
        Rotation typedValue266 = new Rotation(f, 90.0f);
        float f2 = ThreadLocalRandom.current().nextFloat(275.0f, 444.0f);
        RockstarClient.getInstance().internalMethod02368().internalMethod00418(typedValue266, RotationBehavior.internalField1003, f2, f2, f2, RotationPriority.internalField1012);
        if (RockstarClient.getInstance().internalMethod02368().internalMethod00024().internalMethod00735(typedValue266) > 1.0f) {
            this.internalField0228 = 0;
            return;
        }
        if (this.internalField0228++ < 1) {
            return;
        }
        int n2 = AutoPotionModule.internalField0149.player.getInventory().getSelectedSlot();
        boolean bl = false;
        for (InventorySlot typedValue222 : list) {
            if (!this.internalMethod07097(typedValue222)) continue;
            if (typedValue222 instanceof HotbarSlot) {
                int n3;
                HotbarSlot typedValue231 = (HotbarSlot)typedValue222;
                AutoPotionModule.internalField0149.player.getInventory().setSelectedSlot(n3 = typedValue231.internalMethod08745());
                internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n3));
                AutoPotionModule.internalField0149.interactionManager.sendSequencedPacket(AutoPotionModule.internalField0149.world, n -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, f, 90.0f));
            } else {
                InventoryUtils.internalMethod08821(typedValue222.internalMethod06662(), n2);
                internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n2));
                AutoPotionModule.internalField0149.interactionManager.sendSequencedPacket(AutoPotionModule.internalField0149.world, n -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, n, f, 90.0f));
                InventoryUtils.internalMethod08821(typedValue222.internalMethod06662(), n2);
            }
            bl = true;
        }
        AutoPotionModule.internalField0149.player.getInventory().setSelectedSlot(n2);
        internalField0149.getNetworkHandler().sendPacket((Packet)new UpdateSelectedSlotC2SPacket(n2));
        this.internalField0228 = 0;
        if (bl) {
            this.internalField0227 = 0;
            RockstarClient.getInstance().internalMethod02368().internalMethod01570(new Rotation(AutoPotionModule.internalField0149.player.getYaw(), AutoPotionModule.internalField0149.player.getPitch()));
        }
    };

    public AutoPotionModule() {
        this.internalMethod09377();
    }

    private void internalMethod09377() {
        this.internalField0675 = new MultiSelectSetting(this, "modules.settings.auto_potion.potions");
        this.internalField0687 = new InternalType0255(this.internalField0675, "modules.settings.auto_potion.potions.strength", (RegistryEntry<StatusEffect>)StatusEffects.STRENGTH);
        this.internalField0687.select();
        this.internalField0688 = new InternalType0255(this.internalField0675, "modules.settings.auto_potion.potions.speed", (RegistryEntry<StatusEffect>)StatusEffects.SPEED);
        this.internalField0688.select();
        this.internalField1278 = new InternalType0255(this.internalField0675, "modules.settings.auto_potion.potions.fire_resistance", (RegistryEntry<StatusEffect>)StatusEffects.FIRE_RESISTANCE);
        this.internalField1278.select();
    }

    @Override
    public void onEnable() {
        this.internalField0227 = 20;
        this.internalField0228 = 0;
    }

    @Override
    public void onDisable() {
        this.internalField0228 = 0;
        if (AutoPotionModule.internalField0149.player != null) {
            RockstarClient.getInstance().internalMethod02368().internalMethod01570(new Rotation(AutoPotionModule.internalField0149.player.getYaw(), AutoPotionModule.internalField0149.player.getPitch()));
        }
    }

    private List<InventorySlot> internalMethod04078() {
        ArrayList<InventorySlot> arrayList = new ArrayList<InventorySlot>();
        Predicate<ItemStack> predicate = itemStack -> !itemStack.isEmpty() && itemStack.getItem() instanceof SplashPotionItem;
        for (InternalType0255 nestedValue2033 : this.internalMethod01768()) {
            RegistryEntry<StatusEffect> registryEntry = nestedValue2033.internalField0151;
            if (AutoPotionModule.internalField0149.player.hasStatusEffect(registryEntry)) continue;
            Predicate<ItemStack> predicate2 = this.internalMethod02536(registryEntry);
            HotbarSlot typedValue231 = InventorySlots.internalMethod02872().internalMethod03297(itemStack -> predicate.test((ItemStack)itemStack) && predicate2.test((ItemStack)itemStack));
            if (typedValue231 != null && this.internalMethod07097(typedValue231)) {
                arrayList.add(typedValue231);
                continue;
            }
            MainInventorySlot typedValue233 = InventorySlots.internalMethod03558().internalMethod03297(itemStack -> predicate.test((ItemStack)itemStack) && predicate2.test((ItemStack)itemStack));
            if (typedValue233 == null || !this.internalMethod07097(typedValue233)) continue;
            arrayList.add(typedValue233);
        }
        return arrayList;
    }

    private List<InternalType0255> internalMethod01768() {
        List<InternalType0255> list = List.of(this.internalField0687, this.internalField0688, this.internalField1278);
        List<InternalType0255> list2 = this.internalField0675.internalMethod07492().stream().map(InternalType0255.class::cast).toList();
        ArrayList<InternalType0255> arrayList = new ArrayList<InternalType0255>();
        for (InternalType0255 nestedValue2033 : list) {
            if (!list2.contains(nestedValue2033)) continue;
            arrayList.add(nestedValue2033);
        }
        return arrayList;
    }

    private boolean internalMethod07097(InventorySlot typedValue222) {
        ItemStack itemStack = typedValue222.internalMethod03427();
        return !itemStack.isEmpty() && itemStack.getItem() instanceof SplashPotionItem;
    }

    private Predicate<ItemStack> internalMethod02536(RegistryEntry<StatusEffect> registryEntry) {
        return itemStack -> {
            if (itemStack.isEmpty() || !(itemStack.getItem() instanceof SplashPotionItem)) {
                return false;
            }
            return PotionItemUtils.internalMethod00984(itemStack).stream().anyMatch(statusEffectInstance -> statusEffectInstance.getEffectType() == registryEntry);
        };
    }

    static class InternalType0255
    extends MultiSelectSetting.InternalType0091 {
        final RegistryEntry<StatusEffect> internalField0151;

        InternalType0255(MultiSelectSetting typedValue173, String string, RegistryEntry<StatusEffect> registryEntry) {
            super(typedValue173, string);
            this.internalField0151 = registryEntry;
        }
    }
}
