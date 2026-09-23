package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.Comparator;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.Hand;
import wtf.wyvern.core.events.impl.input.EventKey;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BindSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.gui.screens.autoswap.AutoSwapWheelScreen;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "AutoSwap",
   category = Category.COMBAT,
   description = "Меняет предметы по клавише"
)
public final class AutoSwap extends Module {
   public static final AutoSwap INSTANCE = new AutoSwap();
   private final ModeSetting mode = new ModeSetting("Режим", "Двойной", "Тройной");
   private final ModeSetting itemType = new ModeSetting("Предмет", () -> this.mode.is("Двойной"), "Щит", "Золотые яблоки", "Тотем бессмертия", "Сфера");
   private final ModeSetting swapType = new ModeSetting("Свапать на", () -> this.mode.is("Двойной"), "Щит", "Золотые яблоки", "Тотем бессмертия", "Сфера");
   private final BindSetting keyToSwap = new BindSetting("Кнопка", -1);
   private boolean swap;
   private final WheelSlotItem[] wheelSlots = new WheelSlotItem[3];

   @FastNative
   @EventTarget
   public void onKey(EventKey event) {
      if (this.mode.is("Тройной")) {
         if (event.isKeyDown(this.keyToSwap.getKeyCode()) && mc.currentScreen == null) {
            mc.setScreen(new AutoSwapWheelScreen(this, this.keyToSwap.getKeyCode()));
         }
      } else if (mc.currentScreen == null && event.isKeyDown(this.keyToSwap.getKeyCode())) {
         this.swap = true;
      }
   }

   @EventTarget
   public void onTick(EventUpdate event) {
      if (this.swap) {
         Slot first = PlayerInventoryUtil.getSlot(this.getItemByType(this.itemType.get()), Comparator.comparing((s) -> {
            return s.getStack().hasEnchantments();
         }), (s) -> {
            return s.id != 46 && s.id != 45 && s.id != 5 && s.id != 6 && s.id != 7 && s.id != 8;
         });
         Slot second = PlayerInventoryUtil.getSlot(this.getItemByType(this.swapType.get()), Comparator.comparing((s) -> {
            return s.getStack().hasEnchantments();
         }), (s) -> {
            return s.id != 46 && s.id != 45 && s.id != 5 && s.id != 6 && s.id != 7 && s.id != 8;
         });
         Slot validSlot = first != null && !mc.player.getOffHandStack().isOf(first.getStack().getItem()) ? first : second;
         if (validSlot != null) {
            this.stopSprinting();
            PlayerInventoryUtil.swapHand(validSlot, Hand.OFF_HAND, false);
         }
         this.swap = false;
      }

   }

   private void stopSprinting() {
      if (!mc.player.isSprinting()) return;
      mc.player.setSprinting(false);
      mc.getNetworkHandler().sendPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.STOP_SPRINTING));
      mc.options.sprintKey.setPressed(false);
   }

   @FastNative
   private Item getItemByType(String itemType) {
      return switch (itemType) {
         case "Щит" -> Items.SHIELD;
         case "Золотые яблоки" -> Items.GOLDEN_APPLE;
         case "Тотем бессмертия" -> Items.TOTEM_OF_UNDYING;
         case "Сфера" -> Items.PLAYER_HEAD;
         default -> Items.AIR;
      };
   }

   @FastNative
   public void setWheelSlotItem(int index, ItemStack stack) {
      if (index >= 0 && index < this.wheelSlots.length && !stack.isEmpty()) {
         this.wheelSlots[index] = new WheelSlotItem(stack.getItem(), stack.getName().getString());
      }
   }

   @FastNative
   public ItemStack getWheelSlotStack(int index) {
      if (mc.player == null || index < 0 || index >= this.wheelSlots.length || this.wheelSlots[index] == null) return ItemStack.EMPTY;
      WheelSlotItem selected = this.wheelSlots[index];
      for (int i = 0; i < mc.player.getInventory().size(); ++i) {
         ItemStack stack = mc.player.getInventory().getStack(i);
         if (!stack.isEmpty() && stack.getItem() == selected.item && stack.getName().getString().equals(selected.name)) return stack;
      }
      return ItemStack.EMPTY;
   }

   public void swapToWheelSlot(int index) {
      ItemStack wanted = this.getWheelSlotStack(index);
      if (wanted.isEmpty()) return;
      Slot slot = PlayerInventoryUtil.getSlot(wanted, Comparator.comparing((s) -> s.getStack().hasEnchantments()),
         (s) -> s.id != 46 && s.id != 45 && s.id != 5 && s.id != 6 && s.id != 7 && s.id != 8);
      if (slot == null) return;
      PlayerInventoryUtil.swapHand(slot, Hand.OFF_HAND, false);
   }

   private static final class WheelSlotItem {
      private final Item item;
      private final String name;

      private WheelSlotItem(Item item, String name) {
         this.item = item;
         this.name = name;
      }
   }
}
