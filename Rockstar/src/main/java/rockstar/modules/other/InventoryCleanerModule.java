package rockstar.modules.other;







import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.AxeItem;
import net.minecraft.item.BowItem;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MaceItem;
import net.minecraft.item.TridentItem;
import net.minecraft.screen.slot.SlotActionType;
import pyrock.events.player.ClientPlayerTickEvent;

@ModuleInfo(
   name = "Inventory Cleaner",
   category = ModuleCategory.OTHER,
   internalMethod09633 = "modules.descriptions.inventory_cleaner"
)
public class InventoryCleanerModule extends Module {
   private final Stopwatch internalField0519 = new Stopwatch();
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private RegistryListSetting internalField0649;
   private final List<InventorySlot> internalField0416 = new ArrayList<>();
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (this.isEnabled() && internalField0149.player != null && internalField0149.player.currentScreenHandler != null) {
         if (this.internalField0519.internalMethod02365(150L)) {
            this.internalField0416.clear();
            if (this.internalField0237.isSelected()) {
               this.internalMethod02381(InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872()));
            } else {
               this.internalMethod05564(InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872()));
            }

            if (this.internalField0416.isEmpty()) {
               return;
            }

            InventorySlot localValue2 = this.internalField0416.removeFirst();
            internalField0149.interactionManager
               .clickSlot(internalField0149.player.currentScreenHandler.syncId, localValue2.internalMethod06662(), 1, SlotActionType.THROW, internalField0149.player);
            this.internalField0519.internalMethod00701();
         }
      }
   };

   public InventoryCleanerModule() {
      this.internalMethod09156();
   }

   private void internalMethod09156() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.inventory_cleaner.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.inventory_cleaner.mode.1_8");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.inventory_cleaner.mode.custom").select();
      this.internalField0649 = new RegistryListSetting(this, "modules.settings.inventory_cleaner.blocks", () -> !this.internalField0238.isSelected())
         .internalMethod06411();
   }

   private void internalMethod05564(SlotCollection<InventorySlot> localValue1) {
      Set localValue2 = this.internalField0649.internalMethod09099();
      if (!localValue2.isEmpty()) {
         for (InventorySlot localValue4 : localValue1.internalMethod02638()) {
            if (!localValue4.internalMethod06664() && localValue2.contains(localValue4.internalMethod00210())) {
               this.internalField0416.add(localValue4);
            }
         }
      }
   }

   private void internalMethod02381(SlotCollection<InventorySlot> localValue1) {
      for (InventorySlot localValue3 : localValue1.internalMethod02638()) {
         ItemStack localValue4 = localValue3.internalMethod03427();
         if (!localValue4.isEmpty() && !this.internalMethod00821(localValue3) && (this.internalMethod00918(localValue4, localValue1) || this.internalMethod03338(localValue4, localValue1))) {
            this.internalField0416.add(localValue3);
         }
      }
   }

   private boolean internalMethod00821(InventorySlot localValue1) {
      return internalField0149.player != null && localValue1.internalMethod06662() == 36 + internalField0149.player.getInventory().getSelectedSlot();
   }

   private boolean internalMethod00918(ItemStack localValue1, SlotCollection<InventorySlot> localValue2) {
      if (LegacyItemTypes.isArmor(localValue1)) {
         EquipmentSlot localValue12 = LegacyItemTypes.getArmorSlot(localValue1);
         ItemStack localValue5 = internalField0149.player.getEquippedStack(localValue12);
         int localValue6 = this.internalMethod00714(localValue1);
         if (LegacyItemTypes.isArmor(localValue5)) {
            localValue6 = Math.max(localValue6, this.internalMethod00714(localValue5));
         }

         for (InventorySlot localValue8 : localValue2.internalMethod02638()) {
            ItemStack localValue9 = localValue8.internalMethod03427();
            if (localValue9 != localValue1 && LegacyItemTypes.getArmorSlot(localValue9) == localValue12) {
               localValue6 = Math.max(localValue6, this.internalMethod00714(localValue9));
            }
         }

         return this.internalMethod00714(localValue1) < localValue6;
      } else {
         return false;
      }
   }

   private boolean internalMethod03338(ItemStack localValue1, SlotCollection<InventorySlot> localValue2) {
      String localValue3 = this.internalMethod05483(localValue1);
      if (localValue3 == null) {
         return false;
      } else {
         int localValue4 = this.internalMethod05886(localValue1);
         int localValue5 = localValue4;
         ItemStack localValue6 = internalField0149.player.getMainHandStack();
         if (localValue3.equals(this.internalMethod05483(localValue6))) {
            localValue5 = Math.max(localValue4, this.internalMethod05886(localValue6));
         }

         ItemStack localValue7 = internalField0149.player.getOffHandStack();
         if (localValue3.equals(this.internalMethod05483(localValue7))) {
            localValue5 = Math.max(localValue5, this.internalMethod05886(localValue7));
         }

         for (InventorySlot localValue9 : localValue2.internalMethod02638()) {
            ItemStack localValue10 = localValue9.internalMethod03427();
            if (localValue10 != localValue1 && localValue3.equals(this.internalMethod05483(localValue10))) {
               localValue5 = Math.max(localValue5, this.internalMethod05886(localValue10));
            }
         }

         return localValue4 < localValue5;
      }
   }

   private String internalMethod05483(ItemStack localValue1) {
      if (localValue1.isEmpty()) {
         return null;
      } else {
         Item localValue2 = localValue1.getItem();
         if (LegacyItemTypes.isSword(localValue1)) {
            return "sword";
         } else if (localValue2 instanceof AxeItem) {
            return "axe";
         } else if (localValue2 instanceof BowItem) {
            return "bow";
         } else if (localValue2 instanceof CrossbowItem) {
            return "crossbow";
         } else if (localValue2 instanceof TridentItem) {
            return "trident";
         } else {
            return localValue2 instanceof MaceItem ? "mace" : null;
         }
      }
   }

   private int internalMethod00714(ItemStack localValue1) {
      if (LegacyItemTypes.isArmor(localValue1)) {
         CustomItemUtils.InternalType0254 localValue11 = CustomItemUtils.internalMethod03238(localValue1);
         if (localValue11 != null && "SunHelmet".equals(localValue11.internalMethod01319())) {
            return Integer.MAX_VALUE;
         } else {
            int localValue6 = LegacyItemTypes.getDefense(localValue1);
            int localValue7 = LegacyItemTypes.getToughness(localValue1);
            int localValue8 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.PROTECTION);
            int localValue9 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.FIRE_PROTECTION)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.BLAST_PROTECTION)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.PROJECTILE_PROTECTION);
            int localValue10 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.FEATHER_FALLING)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.RESPIRATION)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.AQUA_AFFINITY)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.DEPTH_STRIDER)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.THORNS)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.UNBREAKING)
               + EnchantmentUtils.internalMethod03526(localValue1, Enchantments.MENDING);
            return localValue6 * 50 + localValue8 * 30 + localValue7 * 10 + localValue9 * 12 + localValue10 * 4 + this.internalMethod08909(localValue1);
         }
      } else {
         return 0;
      }
   }

   private int internalMethod05886(ItemStack localValue1) {
      Item localValue2 = localValue1.getItem();
      int localValue3 = this.internalMethod04024(localValue2);
      if (CustomItemUtils.internalMethod03238(localValue1) != null) {
         localValue3 += 1000;
      }

      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.SHARPNESS) * 30;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.POWER) * 30;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.DENSITY) * 30;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.BREACH) * 24;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.SMITE) * 12;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.BANE_OF_ARTHROPODS) * 12;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.IMPALING) * 18;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.FIRE_ASPECT) * 14;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.KNOCKBACK) * 8;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.LOOTING) * 10;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.SWEEPING_EDGE) * 8;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.PUNCH) * 8;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.FLAME) * 12;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.INFINITY) * 16;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.MULTISHOT) * 16;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.QUICK_CHARGE) * 14;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.PIERCING) * 10;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.LOYALTY) * 8;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.RIPTIDE) * 10;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.WIND_BURST) * 18;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.UNBREAKING) * 4;
      localValue3 += EnchantmentUtils.internalMethod03526(localValue1, Enchantments.MENDING) * 6;
      return localValue3 + this.internalMethod08909(localValue1);
   }

   private int internalMethod04024(Item localValue1) {
      if (localValue1 == Items.NETHERITE_SWORD || localValue1 == Items.NETHERITE_AXE || localValue1 == Items.MACE) {
         return 90;
      } else if (localValue1 == Items.DIAMOND_SWORD || localValue1 == Items.DIAMOND_AXE || localValue1 == Items.TRIDENT) {
         return 80;
      } else if (localValue1 == Items.IRON_SWORD || localValue1 == Items.IRON_AXE || localValue1 == Items.CROSSBOW) {
         return 70;
      } else if (localValue1 == Items.STONE_SWORD || localValue1 == Items.STONE_AXE || localValue1 == Items.BOW) {
         return 60;
      } else if (localValue1 == Items.GOLDEN_SWORD || localValue1 == Items.GOLDEN_AXE) {
         return 50;
      } else {
         return localValue1 != Items.WOODEN_SWORD && localValue1 != Items.WOODEN_AXE ? 0 : 40;
      }
   }

   private int internalMethod08909(ItemStack localValue1) {
      return !localValue1.isDamageable() ? 0 : (int)((double)(localValue1.getMaxDamage() - localValue1.getDamage()) / localValue1.getMaxDamage() * 10.0);
   }
}
