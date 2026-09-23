package rockstar.client.internal.inventory;



import rockstar.client.inventory.*;
import rockstar.client.*;
import rockstar.client.util.LegacyItemTypes;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;

public final class ServerSelectMenuNavigator implements MinecraftClientAccess {
   public static boolean internalMethod00388() {
      if (internalField0149.player != null && internalField0149.world != null && internalField0149.interactionManager != null) {
         SlotCollection localValue0 = InventorySlots.internalMethod02872();
         HotbarSlot localValue1 = (HotbarSlot)localValue0.internalMethod02510(Items.COMPASS);
         if (localValue1 == null) {
            return false;
         } else {
            int localValue2 = localValue1.internalMethod08745();
            internalField0149.player.getInventory().setSelectedSlot(localValue2);
            internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(localValue2));
            internalField0149.interactionManager
               .sendSequencedPacket(
                  internalField0149.world,
                  localValue0x -> new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, localValue0x, internalField0149.player.getYaw(), internalField0149.player.getPitch())
               );
            return true;
         }
      } else {
         return false;
      }
   }

   public static boolean internalMethod00290(String localValue0) {
      String localValue1 = internalMethod05469(localValue0);
      return localValue1.contains("\u0432\u044b\u0431\u043e\u0440 \u0441\u0435\u0440\u0432\u0435\u0440\u0430")
         || localValue1.contains("\u0432\u044b\u0431\u0435\u0440\u0438\u0442\u0435 \u0441\u0435\u0440\u0432\u0435\u0440");
   }

   public static boolean internalMethod06582(String localValue0, boolean localValue1) {
      String localValue2 = internalMethod05469(localValue0);
      return !localValue2.contains("\u0432\u044b\u0431\u043e\u0440 \u043c\u0438\u0440\u0430") ? false : !localValue1 || localValue2.contains("\u0433\u0440\u0438\u0444");
   }

   public static int internalMethod06353(GenericContainerScreenHandler localValue0, boolean localValue1) {
      int localValue2 = internalMethod07997(localValue0);
      int localValue3 = -1;

      for (int localValue4 = 0; localValue4 < localValue2; localValue4++) {
         if (localValue0.getSlot(localValue4).hasStack()) {
            ItemStack localValue5 = localValue0.getSlot(localValue4).getStack();
            String localValue6 = internalMethod05469(internalMethod04591(localValue5));
            if (localValue1) {
               if (localValue5.getItem() == Items.CRAFTING_TABLE && (localValue6.contains("\u0433\u0440\u0438\u0444") || localValue6.contains("\u0432\u044b\u0436\u0438\u0432"))) {
                  return localValue4;
               }

               if (localValue3 == -1 && localValue6.contains("\u0433\u0440\u0438\u0444")) {
                  localValue3 = localValue4;
               }
            } else if (localValue6.contains("\u0430\u043d\u0430\u0440\u0445")) {
               return localValue4;
            }
         }
      }

      if (localValue1) {
         for (int localValue7 = 0; localValue7 < localValue2; localValue7++) {
            if (localValue0.getSlot(localValue7).hasStack() && localValue0.getSlot(localValue7).getStack().getItem() == Items.CRAFTING_TABLE) {
               return localValue7;
            }
         }
      }

      return localValue3;
   }

   public static ServerSelectMenuNavigator.InternalType0222 internalMethod01194(GenericContainerScreenHandler localValue0, String localValue1, int localValue2, boolean localValue3) {
      if (localValue2 <= 0) {
         return ServerSelectMenuNavigator.InternalType0222.internalField0534;
      } else {
         int localValue4 = internalMethod04648(localValue0, localValue2, localValue3);
         if (localValue4 != -1) {
            internalMethod06352(localValue0, localValue4);
            return ServerSelectMenuNavigator.InternalType0222.internalField1192;
         } else {
            int localValue5 = internalMethod03927(localValue0, localValue1, localValue2);
            if (localValue5 != -1) {
               internalMethod06352(localValue0, localValue5);
               return ServerSelectMenuNavigator.InternalType0222.internalField1192;
            } else {
               int localValue6 = Integer.MAX_VALUE;
               int localValue7 = -1;
               int localValue8 = internalMethod07997(localValue0);

               for (int localValue9 = 0; localValue9 < localValue8; localValue9++) {
                  if (localValue0.getSlot(localValue9).hasStack()) {
                     int localValue10 = internalMethod05018(localValue0.getSlot(localValue9).getStack(), localValue3);
                     if (localValue10 > 0) {
                        localValue6 = Math.min(localValue6, localValue10);
                        localValue7 = Math.max(localValue7, localValue10);
                     }
                  }
               }

               if (localValue7 > 0) {
                  if (localValue2 < localValue6 && internalMethod07935(localValue1)) {
                     return internalMethod06354(localValue0, false)
                        ? ServerSelectMenuNavigator.InternalType0222.internalField0535
                        : ServerSelectMenuNavigator.InternalType0222.internalField0534;
                  } else if (localValue2 > localValue7 && internalMethod06898(localValue1)) {
                     return internalMethod06354(localValue0, true)
                        ? ServerSelectMenuNavigator.InternalType0222.internalField0535
                        : ServerSelectMenuNavigator.InternalType0222.internalField0534;
                  } else {
                     return ServerSelectMenuNavigator.InternalType0222.internalField0534;
                  }
               } else if (internalMethod06898(localValue1)) {
                  return internalMethod06354(localValue0, true)
                     ? ServerSelectMenuNavigator.InternalType0222.internalField0535
                     : ServerSelectMenuNavigator.InternalType0222.internalField0534;
               } else {
                  return ServerSelectMenuNavigator.InternalType0222.internalField0534;
               }
            }
         }
      }
   }

   public static void internalMethod06352(GenericContainerScreenHandler localValue0, int localValue1) {
      if (internalField0149.player != null && internalField0149.interactionManager != null && localValue1 >= 0 && localValue1 < localValue0.slots.size()) {
         internalField0149.interactionManager.clickSlot(localValue0.syncId, localValue1, 0, SlotActionType.PICKUP, internalField0149.player);
         internalField0149.player.currentScreenHandler.onSlotClick(localValue1, 0, SlotActionType.PICKUP, internalField0149.player);
      }
   }

   private static int internalMethod04648(GenericContainerScreenHandler localValue0, int localValue1, boolean localValue2) {
      int localValue3 = internalMethod07997(localValue0);

      for (int localValue4 = 0; localValue4 < localValue3; localValue4++) {
         if (localValue0.getSlot(localValue4).hasStack() && internalMethod05018(localValue0.getSlot(localValue4).getStack(), localValue2) == localValue1) {
            return localValue4;
         }
      }

      return -1;
   }

   private static int internalMethod03927(GenericContainerScreenHandler localValue0, String localValue1, int localValue2) {
      int localValue3 = internalMethod00289(localValue1);
      if (localValue3 <= 0) {
         return -1;
      } else {
         int localValue4 = localValue2 - (localValue3 - 1) * 32;
         if (localValue4 >= 1 && localValue4 <= 32) {
            int localValue5 = (localValue4 - 1) / 8;
            int localValue6 = (localValue4 - 1) % 8;
            if (localValue6 >= 4) {
               localValue6++;
            }

            int localValue7 = localValue5 * 9 + localValue6;
            if (localValue7 >= 0 && localValue7 < internalMethod07997(localValue0) && localValue0.getSlot(localValue7).hasStack()) {
               return localValue0.getSlot(localValue7).getStack().getItem() == Items.ARROW ? -1 : localValue7;
            } else {
               return -1;
            }
         } else {
            return -1;
         }
      }
   }

   private static boolean internalMethod06354(GenericContainerScreenHandler localValue0, boolean localValue1) {
      int localValue2 = localValue1 ? internalMethod04228(localValue0) : internalMethod05507(localValue0);
      if (localValue2 == -1) {
         return false;
      } else {
         internalMethod06352(localValue0, localValue2);
         return true;
      }
   }

   private static int internalMethod04228(GenericContainerScreenHandler localValue0) {
      int localValue1 = internalMethod07997(localValue0);
      int localValue2 = -1;

      for (int localValue3 = 0; localValue3 < localValue1; localValue3++) {
         if (localValue0.getSlot(localValue3).hasStack()) {
            ItemStack localValue4 = localValue0.getSlot(localValue3).getStack();
            if (localValue4.getItem() == Items.ARROW || localValue4.getItem() == Items.SPECTRAL_ARROW) {
               String localValue5 = internalMethod05469(internalMethod04591(localValue4));
               if (localValue5.contains("\u0441\u043b\u0435\u0434") || localValue5.contains("\u0434\u0430\u043b\u044c") || localValue5.contains("next")) {
                  return localValue3;
               }

               if (localValue3 % 9 >= 4) {
                  localValue2 = localValue3;
               }
            }
         }
      }

      return localValue2;
   }

   private static int internalMethod05507(GenericContainerScreenHandler localValue0) {
      int localValue1 = internalMethod07997(localValue0);
      int localValue2 = -1;

      for (int localValue3 = 0; localValue3 < localValue1; localValue3++) {
         if (localValue0.getSlot(localValue3).hasStack()) {
            ItemStack localValue4 = localValue0.getSlot(localValue3).getStack();
            if (localValue4.getItem() == Items.ARROW || localValue4.getItem() == Items.SPECTRAL_ARROW) {
               String localValue5 = internalMethod05469(internalMethod04591(localValue4));
               if (localValue5.contains("\u043f\u0440\u0435\u0434")
                  || localValue5.contains("\u043d\u0430\u0437\u0430\u0434")
                  || localValue5.contains("previous")
                  || localValue5.contains("back")) {
                  return localValue3;
               }

               if (localValue3 % 9 <= 4 && localValue2 == -1) {
                  localValue2 = localValue3;
               }
            }
         }
      }

      return localValue2;
   }

   private static int internalMethod05018(ItemStack localValue0, boolean localValue1) {
      String localValue2 = internalMethod05469(internalMethod04591(localValue0));
      int localValue3 = internalMethod06581(localValue2, localValue2.indexOf(35) + 1);
      if (localValue3 > 0 && localValue2.indexOf(35) >= 0) {
         return localValue3;
      } else {
         String localValue4 = localValue1 ? "\u0433\u0440\u0438\u0444" : "\u0430\u043d\u0430\u0440\u0445";
         int localValue5 = localValue2.indexOf(localValue4);
         if (localValue5 >= 0) {
            localValue3 = internalMethod06581(localValue2, localValue5 + localValue4.length());
            if (localValue3 > 0) {
               return localValue3;
            }
         }

         String localValue6 = localValue1 ? "grief" : "anarchy";
         localValue5 = localValue2.indexOf(localValue6);
         if (localValue5 >= 0) {
            localValue3 = internalMethod06581(localValue2, localValue5 + localValue6.length());
            if (localValue3 > 0) {
               return localValue3;
            }
         }

         return -1;
      }
   }

   private static boolean internalMethod06898(String localValue0) {
      int localValue1 = internalMethod00289(localValue0);
      int localValue2 = internalMethod06897(localValue0);
      return localValue1 <= 0 || localValue2 <= 0 || localValue1 < localValue2;
   }

   private static boolean internalMethod07935(String localValue0) {
      return internalMethod00289(localValue0) > 1;
   }

   private static int internalMethod00289(String localValue0) {
      String localValue1 = internalMethod05469(localValue0);
      int localValue2 = localValue1.indexOf(47);
      return localValue2 == -1 ? -1 : internalMethod00695(localValue1, localValue2);
   }

   private static int internalMethod06897(String localValue0) {
      String localValue1 = internalMethod05469(localValue0);
      int localValue2 = localValue1.indexOf(47);
      return localValue2 == -1 ? -1 : internalMethod06581(localValue1, localValue2 + 1);
   }

   private static int internalMethod07997(GenericContainerScreenHandler localValue0) {
      return Math.max(0, localValue0.slots.size() - 36);
   }

   private static String internalMethod04591(ItemStack localValue0) {
      StringBuilder localValue1 = new StringBuilder(localValue0.getName().getString());

      try {
         if (internalField0149.world == null) {
            return localValue1.toString();
         }

         for (Text localValue3 : localValue0.getTooltip(TooltipContext.create(internalField0149.world), internalField0149.player, TooltipType.BASIC)) {
            localValue1.append(' ').append(localValue3.getString());
         }

         NbtCompound localValue9 = LegacyItemTypes.toNbt(localValue0, internalField0149.world.getRegistryManager());
         if (!localValue9.contains("components")) {
            return localValue1.toString();
         }

         NbtCompound localValue4 = localValue9.getCompound("components").orElseGet(net.minecraft.nbt.NbtCompound::new);
         internalMethod05704(localValue1, localValue4, "minecraft:custom_name");
         internalMethod05704(localValue1, localValue4, "minecraft:item_name");
         if (localValue4.contains("minecraft:lore")) {
            NbtList localValue5 = localValue4.getList("minecraft:lore").orElseGet(net.minecraft.nbt.NbtList::new);

            for (int localValue6 = 0; localValue6 < localValue5.size(); localValue6++) {
               localValue1.append(' ').append(localValue5.getString(localValue6));
            }
         }
      } catch (Exception localValue7) {
      }

      return localValue1.toString();
   }

   private static void internalMethod05704(StringBuilder localValue0, NbtCompound localValue1, String localValue2) {
      if (localValue1.contains(localValue2)) {
         localValue0.append(' ').append(localValue1.getString(localValue2));
      }
   }

   private static int internalMethod06581(String localValue0, int localValue1) {
      if (localValue1 < 0) {
         return -1;
      } else {
         for (int localValue2 = localValue1; localValue2 < localValue0.length(); localValue2++) {
            if (Character.isDigit(localValue0.charAt(localValue2))) {
               int localValue3 = localValue2 + 1;

               while (localValue3 < localValue0.length() && Character.isDigit(localValue0.charAt(localValue3))) {
                  localValue3++;
               }

               return internalMethod07934(localValue0.substring(localValue2, localValue3));
            }
         }

         return -1;
      }
   }

   private static int internalMethod00695(String localValue0, int localValue1) {
      int localValue2 = localValue1 - 1;

      while (localValue2 >= 0 && !Character.isDigit(localValue0.charAt(localValue2))) {
         localValue2--;
      }

      if (localValue2 < 0) {
         return -1;
      } else {
         int localValue3 = localValue2 + 1;

         while (localValue2 >= 0 && Character.isDigit(localValue0.charAt(localValue2))) {
            localValue2--;
         }

         return internalMethod07934(localValue0.substring(localValue2 + 1, localValue3));
      }
   }

   private static int internalMethod07934(String localValue0) {
      try {
         return Integer.parseInt(localValue0);
      } catch (NumberFormatException localValue2) {
         return -1;
      }
   }

   private static String internalMethod05469(String localValue0) {
      return localValue0 == null ? "" : localValue0.replaceAll("\u00a7.", "").replace('\u0451', '\u0435').replace('\u0401', '\u0415').toLowerCase(Locale.ROOT);
   }

   @Generated
   private ServerSelectMenuNavigator() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static enum InternalType0222 {
      internalField0534,
      internalField0535,
      internalField1192;
   }
}
