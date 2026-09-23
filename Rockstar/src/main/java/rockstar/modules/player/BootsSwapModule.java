package rockstar.modules.player;










import rockstar.client.util.*;
import rockstar.client.ui.*;
import rockstar.client.setting.*;
import rockstar.client.notification.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.Comparator;
import java.util.List;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.text.Text;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.window.KeyPressEvent;
import pyrock.events.window.MouseEvent;
import pyrock.utility.render.ColorRGBA;

@ModuleInfo(
   name = "Boots Swap",
   category = ModuleCategory.PLAYER,
   internalMethod09633 = "modules.descriptions.boots_swap"
)
public class BootsSwapModule extends Module {
   private BooleanSetting internalField0650;
   private KeybindSetting internalField0648;
   private BootsSwapModule.InternalType0454 internalField0909;
   private boolean internalField0277 = false;
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      if (this.internalField0650.internalMethod04496()) {
         ItemStack localValue2 = internalField0149.player.getMainHandStack();
         boolean localValue3 = localValue2.getItem() == Items.MACE;
         if (localValue3 && !this.internalField0277) {
            ArmorSlot localValue6 = InventoryUtils.internalMethod07992();
            if (!this.internalMethod02965(localValue6.internalMethod03427())) {
               this.internalMethod09886();
            }
         } else if (!localValue3 && this.internalField0277) {
            ArmorSlot localValue4 = InventoryUtils.internalMethod07992();
            if (this.internalMethod02965(localValue4.internalMethod03427())) {
               this.internalMethod09887();
            }
         }

         this.internalField0277 = localValue3;
      }

      GuiMoveModule localValue5 = RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class);
      if (this.internalField0909 != null) {
         if (this.internalField0909.internalField0022.internalMethod06662() >= 36 && this.internalField0909.internalField0022.internalMethod06662() <= 44) {
            InventoryUtils.internalMethod08821(
               this.internalField0909.internalField0023.internalMethod06662(), this.internalField0909.internalField0022.internalMethod06662() - 36
            );
            this.internalField0909 = null;
         } else if (this.internalField0909.internalField0227 == 0 && localValue5.internalMethod06994().isEmpty()) {
            InventoryUtils.internalMethod08821(this.internalField0909.internalField0022.internalMethod06662(), 8);
            this.internalField0909.internalField0227++;
         } else if (this.internalField0909.internalField0227 == 1 && localValue5.internalMethod06994().isEmpty()) {
            InventoryUtils.internalMethod08821(this.internalField0909.internalField0023.internalMethod06662(), 8);
            this.internalField0909.internalField0227++;
         } else if (this.internalField0909.internalField0227 == 2 && localValue5.internalMethod06994().isEmpty()) {
            InventoryUtils.internalMethod08821(this.internalField0909.internalField0022.internalMethod06662(), 8);
            this.internalField0909.internalField0227++;
         }

         if (this.internalField0909 != null && this.internalField0909.internalField0227 >= 3) {
            this.internalField0909 = null;
         }
      }
   };
   private final EventListener<KeyPressEvent> internalField0158 = localValue1 -> {
      if (this.internalField0648.internalMethod02165(localValue1.getKey()) && localValue1.getAction() == 1 && internalField0149.currentScreen == null) {
         this.internalMethod09691();
      }
   };
   private final EventListener<MouseEvent> internalField1028 = localValue1 -> {
      if (this.internalField0648.internalMethod02165(localValue1.getButton()) && localValue1.getAction() == 1 && internalField0149.currentScreen == null) {
         this.internalMethod09691();
      }
   };

   public BootsSwapModule() {
      this.internalMethod09690();
   }

   private void internalMethod09690() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.boots_swap.automatic");
      this.internalField0648 = new KeybindSetting(this, "modules.settings.boots_swap.swap_key", () -> this.internalField0650.internalMethod04496());
   }

   private void internalMethod09691() {
      ArmorSlot localValue1 = InventoryUtils.internalMethod07992();
      SlotCollection localValue2 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
      boolean localValue3 = this.internalMethod02965(localValue1.internalMethod03427());
      InventorySlot localValue4;
      if (localValue3) {
         localValue4 = this.internalMethod06490(localValue2, false);
      } else {
         localValue4 = this.internalMethod06490(localValue2, true);
      }

      if (this.internalField0909 == null && localValue4 != null) {
         this.internalField0909 = new BootsSwapModule.InternalType0454(localValue4, localValue1);
         boolean localValue5 = this.internalMethod02965(localValue4.internalMethod03427());
         String localValue6 = localValue5
            ? "\u041f\u043e\u043f\u0440\u044b\u0433\u0443\u043d"
            : "\u041e\u0431\u044b\u0447\u043d\u044b\u0435 \u0431\u043e\u0442\u0438\u043d\u043a\u0438";
         ColorRGBA localValue7 = ThemeColors.internalMethod02531();
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod02784(
               new ItemNotification(LanguageManager.internalMethod00160("alerts.equipped", localValue6), localValue4.internalMethod03427())
                  .internalMethod03390(localValue6)
                  .internalMethod05942(localValue7)
            );
      }
   }

   private void internalMethod09886() {
      ArmorSlot localValue1 = InventoryUtils.internalMethod07992();
      SlotCollection localValue2 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
      InventorySlot localValue3 = this.internalMethod06490(localValue2, true);
      if (this.internalField0909 == null && localValue3 != null) {
         this.internalField0909 = new BootsSwapModule.InternalType0454(localValue3, localValue1);
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod02784(
               new ItemNotification(
                     LanguageManager.internalMethod00160("alerts.equipped", "\u041f\u043e\u043f\u0440\u044b\u0433\u0443\u043d"), localValue3.internalMethod03427()
                  )
                  .internalMethod03390("\u041f\u043e\u043f\u0440\u044b\u0433\u0443\u043d")
                  .internalMethod05942(ThemeColors.internalMethod02531())
            );
      }
   }

   private void internalMethod09887() {
      ArmorSlot localValue1 = InventoryUtils.internalMethod07992();
      SlotCollection localValue2 = InventorySlots.internalMethod03558().internalMethod07591(InventorySlots.internalMethod02872());
      InventorySlot localValue3 = this.internalMethod06490(localValue2, false);
      if (this.internalField0909 == null && localValue3 != null) {
         this.internalField0909 = new BootsSwapModule.InternalType0454(localValue3, localValue1);
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod02784(
               new ItemNotification(
                     LanguageManager.internalMethod00160(
                        "alerts.equipped", "\u041e\u0431\u044b\u0447\u043d\u044b\u0435 \u0431\u043e\u0442\u0438\u043d\u043a\u0438"
                     ),
                     localValue3.internalMethod03427()
                  )
                  .internalMethod03390("\u041e\u0431\u044b\u0447\u043d\u044b\u0435 \u0431\u043e\u0442\u0438\u043d\u043a\u0438")
                  .internalMethod05942(ThemeColors.internalMethod02531())
            );
      }
   }

   private InventorySlot internalMethod06490(SlotCollection<InventorySlot> localValue1, boolean localValue2) {
      List<InventorySlot> localValue3 = localValue1.internalMethod00168(
         localValue2x -> LegacyItemTypes.getArmorSlot(localValue2x) == net.minecraft.entity.EquipmentSlot.FEET
            && this.internalMethod02965(localValue2x) == localValue2
      );
      return localValue3.isEmpty() ? null : localValue3.stream().max(Comparator.comparingInt(localValue1x -> this.internalMethod02964(localValue1x.internalMethod03427()))).orElse(null);
   }

   private int internalMethod02964(ItemStack localValue1) {
      if (LegacyItemTypes.isArmor(localValue1)) {
         int localValue5 = LegacyItemTypes.getDefense(localValue1);
         int localValue6 = LegacyItemTypes.getToughness(localValue1);
         int localValue7 = EnchantmentUtils.internalMethod03526(localValue1, Enchantments.PROTECTION);
         return localValue5 * 5 + localValue7 * 3 + localValue6;
      } else {
         return 0;
      }
   }

   private boolean internalMethod02965(ItemStack localValue1) {
      if (localValue1 != null && !localValue1.isEmpty()) {
         String localValue2 = localValue1.getName().getString();
         if (localValue2.contains("\u041f\u043e\u043f\u0440\u044b\u0433\u0443\u043d")) {
            return true;
         } else {
            try {
               for (Text localValue5 : localValue1.getTooltip(TooltipContext.create(internalField0149.world), internalField0149.player, TooltipType.BASIC)) {
                  String localValue6 = localValue5.getString();
                  if (localValue6.contains("\u041f\u043e\u043f\u0440\u044b\u0433\u0443\u043d")) {
                     return true;
                  }
               }
            } catch (Exception localValue7) {
            }

            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void onDisable() {
      this.internalField0277 = false;
   }

   static class InternalType0454 {
      int internalField0227;
      final InventorySlot internalField0022;
      final InventorySlot internalField0023;

      InternalType0454(InventorySlot localValue1, InventorySlot localValue2) {
         this.internalField0022 = localValue1;
         this.internalField0023 = localValue2;
      }
   }
}
