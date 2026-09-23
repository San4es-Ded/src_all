package rockstar.modules.combat;






import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import pyrock.events.player.ClientPlayerTickEvent;

@ModuleInfo(
   name = "Auto Armor",
   category = ModuleCategory.COMBAT,
   internalMethod09633 = "modules.descriptions.auto_armor"
)
public class AutoArmorModule extends Module {
   private final Stopwatch internalField0519 = new Stopwatch();
   private SliderSetting internalField0383;
   private BooleanSetting internalField0650;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      PlayerInventory localValue2 = internalField0149.player.getInventory();
      int[] localValue3 = new int[4];
      int[] localValue4 = new int[4];
      this.internalMethod04752(localValue2, localValue3, localValue4);
      ArrayList<Integer> localValue5 = new ArrayList<>(Arrays.asList(0, 1, 2, 3));
      Collections.shuffle(localValue5);

      for (int localValue7 : localValue5) {
         int localValue8 = localValue3[localValue7];
         if (localValue8 != -1) {
            ItemStack localValue9 = rockstar.client.util.LegacyItemTypes.armorItem(localValue2, localValue7);
            if ((localValue9.isEmpty() || localValue2.getEmptySlot() != -1)
               && (
                  !this.internalField0650.internalMethod04496()
                     || internalField0149.player.getEquippedStack(EquipmentSlot.CHEST).getItem() != Items.ELYTRA
                     || localValue7 != 2
               )) {
               this.internalMethod05228(localValue2, localValue8, localValue7);
               break;
            }
         }
      }
   };

   private void internalMethod09376() {
      this.internalField0383 = new SliderSetting(this, "modules.settings.auto_armor.delay")
         .internalMethod05900(50.0F)
         .internalMethod02732(1000.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(250.0F)
         .internalMethod06240(" ms");
      this.internalField0650 = new BooleanSetting(this, "modules.settings.auto_armor.elytra");
   }

   public AutoArmorModule() {
      this.internalMethod09376();
   }

   private void internalMethod04752(PlayerInventory localValue1, int[] localValue2, int[] localValue3) {
      for (int localValue4 = 0; localValue4 < 4; localValue4++) {
         localValue2[localValue4] = -1;
         ItemStack localValue5 = rockstar.client.util.LegacyItemTypes.armorItem(localValue1, localValue4);
         if (LegacyItemTypes.isArmor(localValue5)) {
            localValue3[localValue4] = this.internalMethod06542(localValue5);
         }
      }

      for (int localValue10 = 0; localValue10 < 36; localValue10++) {
         ItemStack localValue11 = localValue1.getStack(localValue10);
         if (LegacyItemTypes.isArmor(localValue11)) {
            EquipmentSlot localValue14 = LegacyItemTypes.getArmorSlot(localValue11);
            byte localValue8;
            switch (localValue14) {
               case HEAD:
                  localValue8 = 3;
                  break;
               case CHEST:
                  localValue8 = 2;
                  break;
               case LEGS:
                  localValue8 = 1;
                  break;
               case FEET:
                  localValue8 = 0;
                  break;
               default:
                  continue;
            }

            int localValue9 = this.internalMethod06542(localValue11);
            if (localValue9 > localValue3[localValue8]) {
               localValue2[localValue8] = localValue10;
               localValue3[localValue8] = localValue9;
            }
         }
      }
   }

   private void internalMethod05228(PlayerInventory localValue1, int localValue2, int localValue3) {
      if (localValue2 < 9) {
         localValue2 += 36;
      }

      if (this.internalField0519.internalMethod02365((long)this.internalField0383.internalMethod08576())) {
         ItemStack localValue4 = rockstar.client.util.LegacyItemTypes.armorItem(localValue1, localValue3);
         if (!localValue4.isEmpty()) {
            internalField0149.interactionManager.clickSlot(0, 8 - localValue3, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
         }

         internalField0149.interactionManager.clickSlot(0, localValue2, 0, SlotActionType.QUICK_MOVE, internalField0149.player);
         this.internalField0519.internalMethod00701();
      }
   }

   private int internalMethod06542(ItemStack localValue2) {
      CustomItemUtils.InternalType0254 localValue3 = CustomItemUtils.internalMethod03238(localValue2);
      if (localValue3 != null && "SunHelmet".equals(localValue3.internalMethod01319())) {
         return Integer.MAX_VALUE;
      } else {
         int localValue6 = LegacyItemTypes.getDefense(localValue2);
         int localValue7 = LegacyItemTypes.getToughness(localValue2);
         int localValue8 = EnchantmentUtils.internalMethod03526(localValue2, Enchantments.PROTECTION);
         return localValue6 * 5 + localValue8 * 3 + localValue7;
      }
   }
}
