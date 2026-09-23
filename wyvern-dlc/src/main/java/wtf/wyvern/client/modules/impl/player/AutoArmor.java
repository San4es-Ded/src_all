package wtf.wyvern.client.modules.impl.player;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.SlotActionType;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "AutoArmor",
   category = Category.PLAYER,
   description = "Автоматически экипирует броню"
)
@FastNative
public final class AutoArmor extends Module implements IMinecraft {
   public static final AutoArmor INSTANCE = new AutoArmor();
   private final SliderSetting delay = new SliderSetting("Задержка", 25.0F, 0.0F, 1000.0F, 1.0F);
   private long lastEquipTime = 0L;

   private AutoArmor() {
   }

   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player != null && mc.world != null) {
         if (!this.isMoving()) {
            long currentTime = System.currentTimeMillis();
            if (!((float)(currentTime - this.lastEquipTime) < this.delay.getCurrent())) {
               for(int i = 0; i < 4; ++i) {
                  ItemStack currentArmor = mc.player.getInventory().getArmorStack(i);
                  if (currentArmor.isEmpty()) {
                     for(int j = 0; j < 36; ++j) {
                        ItemStack stack = mc.player.getInventory().getStack(j);
                        if (!stack.isEmpty() && stack.getItem() instanceof ArmorItem) {
                           ArmorItem armorItem = (ArmorItem)stack.getItem();
                           if (this.getArmorSlotIndex(armorItem) == i) {
                              int slotToEquip = j;
                              if (j < 9) {
                                 slotToEquip = j + 36;
                              }

                              mc.interactionManager.clickSlot(0, slotToEquip, 0, SlotActionType.QUICK_MOVE, mc.player);
                              this.lastEquipTime = currentTime;
                              return;
                           }
                        }
                     }
                  }
               }

            }
         }
      }
   }

   private boolean isMoving() {
      return mc.player.input.movementForward != 0.0F || mc.player.input.movementSideways != 0.0F;
   }

   private int getArmorSlotIndex(ArmorItem armor) {
      String itemName = armor.toString().toLowerCase();
      if (!itemName.contains("helmet") && !itemName.contains("skull")) {
         if (!itemName.contains("chestplate") && !itemName.contains("tunic")) {
            if (!itemName.contains("leggings") && !itemName.contains("pants")) {
               return !itemName.contains("boots") && !itemName.contains("shoes") ? 0 : 0;
            } else {
               return 1;
            }
         } else {
            return 2;
         }
      } else {
         return 3;
      }
   }
}
