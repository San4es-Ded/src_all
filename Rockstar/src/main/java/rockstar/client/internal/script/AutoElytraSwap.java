package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.server.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.AttributeModifiersComponent;
import net.minecraft.component.type.EquippableComponent;
import net.minecraft.component.type.AttributeModifiersComponent.Entry;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import pyrock.events.player.ClientPlayerTickEvent;

public final class AutoElytraSwap implements MinecraftClientAccess {
   private static AutoElytraSwap internalField0532;
   private AutoElytraSwap.InternalType0399 internalField0839;
   private AutoElytraSwap.InternalType0400 internalField0841;
   private final Stopwatch internalField0519;
   private final Stopwatch internalField0518;
   private final EventListener<ClientPlayerTickEvent> internalField0157;

   private AutoElytraSwap() {
      this.internalField0839 = AutoElytraSwap.InternalType0399.internalField0839;
      this.internalField0519 = new Stopwatch();
      this.internalField0518 = new Stopwatch();
      this.internalField0157 = localValue1 -> {
         this.internalMethod07908();
         if (this.internalField0841 == null && this.internalField0839 != AutoElytraSwap.InternalType0399.internalField0839 && internalField0149.player != null) {
            if (internalField0149.currentScreen == null) {
               if (RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class).internalMethod06994().isEmpty()) {
                  if (this.internalField0519.internalMethod02365(400L)) {
                     ArmorSlot localValue2 = InventoryUtils.internalMethod06826();
                     boolean localValue3 = localValue2.internalMethod00210() == Items.ELYTRA;
                     boolean localValue4 = this.internalField0839 == AutoElytraSwap.InternalType0399.internalField0840 ? localValue3 : !localValue3;
                     if (localValue4) {
                        this.internalField0839 = AutoElytraSwap.InternalType0399.internalField0839;
                     } else {
                        SlotCollection localValue5 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
                        InventorySlot localValue6 = this.internalField0839 == AutoElytraSwap.InternalType0399.internalField0840
                           ? this.internalMethod03479(localValue5)
                           : this.internalMethod01086(localValue5);
                        if (localValue6 == null) {
                           this.internalField0839 = AutoElytraSwap.InternalType0399.internalField0839;
                        } else {
                           this.internalField0841 = new AutoElytraSwap.InternalType0400(localValue6, localValue2);
                           this.internalField0519.internalMethod00701();
                        }
                     }
                  }
               }
            }
         }
      };
   }

   public static AutoElytraSwap internalMethod04072() {
      if (internalField0532 == null) {
         internalField0532 = new AutoElytraSwap();
         RockstarClient.getInstance().internalMethod03317().internalMethod00647(internalField0532);
      }

      return internalField0532;
   }

   public boolean internalMethod04911() {
      return this.internalField0841 != null || this.internalField0839 != AutoElytraSwap.InternalType0399.internalField0839;
   }

   public boolean internalMethod01075(long localValue1) {
      return this.internalField0518.internalMethod02365(localValue1);
   }

   public void internalMethod04910() {
      this.internalField0839 = AutoElytraSwap.InternalType0399.internalField0840;
      this.internalField0519.internalMethod02364(0L);
   }

   public void internalMethod04912() {
      this.internalField0839 = AutoElytraSwap.InternalType0399.internalField1342;
      this.internalField0519.internalMethod02364(0L);
   }

   private void internalMethod07908() {
      if (this.internalField0841 != null && internalField0149.player != null) {
         int localValue1 = this.internalField0841.internalField0022.internalMethod06662();
         if (localValue1 >= 36 && localValue1 <= 44) {
            InventoryUtils.internalMethod08821(this.internalField0841.internalField0023.internalMethod06662(), localValue1 - 36);
            this.internalField0841 = null;
            this.internalField0518.internalMethod00701();
         } else if (RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class).internalMethod06994().isEmpty()) {
            if (ServerUtils.internalMethod01786(KnownServer.internalField0578)) {
               if (this.internalField0841.internalField0227 == 0) {
                  InventoryUtils.internalMethod08821(localValue1, 8);
                  InventoryUtils.internalMethod08821(this.internalField0841.internalField0023.internalMethod06662(), 8);
                  InventoryUtils.internalMethod08821(localValue1, 8);
               }
            } else if (this.internalField0841.internalField0227 == 0) {
               InventoryUtils.internalMethod08821(localValue1, 8);
            } else if (this.internalField0841.internalField0227 == 1) {
               InventoryUtils.internalMethod08821(this.internalField0841.internalField0023.internalMethod06662(), 8);
            } else if (this.internalField0841.internalField0227 == 2) {
               InventoryUtils.internalMethod08821(localValue1, 8);
            }

            if (++this.internalField0841.internalField0227 >= 3) {
               this.internalField0841 = null;
               this.internalField0518.internalMethod00701();
            }
         }
      }
   }

   private InventorySlot internalMethod03479(SlotCollection<InventorySlot> localValue1) {
      return localValue1.internalMethod03297(localValue0 -> localValue0.getItem() == Items.ELYTRA && !localValue0.willBreakNextUse());
   }

   private InventorySlot internalMethod01086(SlotCollection<InventorySlot> localValue1) {
      InventorySlot localValue2 = null;
      double localValue3 = -1.0;

      for (InventorySlot localValue6 : localValue1.internalMethod02638()) {
         ItemStack localValue7 = localValue6.internalMethod03427();
         if (!localValue7.isEmpty() && localValue7.getItem() != Items.ELYTRA) {
            EquippableComponent localValue8 = (EquippableComponent)localValue7.get(DataComponentTypes.EQUIPPABLE);
            if (localValue8 != null && localValue8.slot() == EquipmentSlot.CHEST) {
               double localValue9 = this.internalMethod04993(localValue7) + EnchantmentUtils.internalMethod03526(localValue7, Enchantments.PROTECTION) * 0.6;
               if (localValue9 > localValue3) {
                  localValue3 = localValue9;
                  localValue2 = localValue6;
               }
            }
         }
      }

      return localValue2;
   }

   private double internalMethod04993(ItemStack localValue1) {
      AttributeModifiersComponent localValue2 = (AttributeModifiersComponent)localValue1.get(DataComponentTypes.ATTRIBUTE_MODIFIERS);
      if (localValue2 == null) {
         return 0.0;
      } else {
         double localValue3 = 0.0;

         for (Entry localValue6 : localValue2.modifiers()) {
            if (localValue6.attribute() == EntityAttributes.ARMOR || localValue6.attribute() == EntityAttributes.ARMOR_TOUGHNESS) {
               localValue3 += localValue6.modifier().value();
            }
         }

         return localValue3;
      }
   }

   static enum InternalType0399 {
      internalField0839,
      internalField0840,
      internalField1342;
   }

   static final class InternalType0400 {
      int internalField0227;
      final InventorySlot internalField0022;
      final InventorySlot internalField0023;

      InternalType0400(InventorySlot localValue1, InventorySlot localValue2) {
         this.internalField0022 = localValue1;
         this.internalField0023 = localValue2;
      }
   }
}
