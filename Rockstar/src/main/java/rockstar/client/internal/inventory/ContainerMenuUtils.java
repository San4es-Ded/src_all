package rockstar.client.internal.inventory;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.util.LegacyItemTypes;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.GenericContainerScreenHandler;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.text.Text;

public final class ContainerMenuUtils implements MinecraftClientAccess {
   public static void internalMethod06018(GenericContainerScreenHandler localValue0, int localValue1) {
      if (internalField0149.interactionManager != null && localValue1 >= 0 && localValue1 < localValue0.slots.size()) {
         internalField0149.interactionManager.clickSlot(localValue0.syncId, localValue1, 0, SlotActionType.PICKUP, internalField0149.player);
         internalField0149.player.currentScreenHandler.onSlotClick(localValue1, 0, SlotActionType.PICKUP, internalField0149.player);
      }
   }

   public static int internalMethod03242(int localValue0) {
      return switch (localValue0) {
         case 1 -> 20;
         case 2 -> 21;
         case 3 -> 22;
         case 4 -> 29;
         case 5 -> 30;
         case 6 -> 31;
         case 7 -> 38;
         case 8 -> 39;
         case 9 -> 40;
         default -> -1;
      };
   }

   public static int internalMethod03296(int localValue0) {
      return switch (localValue0) {
         case 1 -> 11;
         case 2 -> 12;
         case 3 -> 13;
         default -> 10;
      };
   }

   public static int internalMethod02224(GenericContainerScreenHandler localValue0, String localValue1) {
      String localValue2 = internalMethod06937(localValue1);
      if (localValue2.isBlank()) {
         return -1;
      } else {
         for (int localValue3 = 0; localValue3 < localValue0.slots.size(); localValue3++) {
            if (localValue0.getSlot(localValue3).hasStack()) {
               String localValue4 = internalMethod01418(localValue0.getSlot(localValue3).getStack());
               String localValue5 = internalMethod06937(localValue4);
               if (localValue5.contains(localValue2) && localValue5.contains("\u043b\u0430\u0439\u0442") && !localValue5.contains("#")) {
                  return localValue3;
               }
            }
         }

         return -1;
      }
   }

   public static List<Integer> internalMethod01343(GenericContainerScreenHandler localValue0) {
      ArrayList localValue1 = new ArrayList();

      for (int localValue2 = 0; localValue2 < localValue0.slots.size(); localValue2++) {
         if (localValue0.getSlot(localValue2).hasStack()) {
            String localValue3 = internalMethod06937(internalMethod01418(localValue0.getSlot(localValue2).getStack()));
            if (!localValue3.contains("#")
               && localValue3.contains("\u043b\u0430\u0439\u0442")
               && (
                  localValue3.contains("\u0441\u043e\u043b\u043e")
                     || localValue3.contains("\u0434\u0443\u043e")
                     || localValue3.contains("\u0442\u0440\u0438\u043e")
                     || localValue3.contains("\u043a\u043b\u0430\u043d")
               )) {
               localValue1.add(localValue2);
            }
         }
      }

      return localValue1;
   }

   public static int internalMethod05511(ServerModeCategory localValue0) {
      return switch (localValue0) {
         case internalField0584 -> 0;
         case internalField0583 -> 1;
         case internalField1223 -> 2;
         case internalField1225 -> 3;
         default -> -1;
      };
   }

   public static int internalMethod00770(GenericContainerScreenHandler localValue0, String localValue1, int localValue2) {
      if (localValue2 <= 0) {
         return -1;
      } else {
         String localValue3 = internalMethod06937(localValue1);

         for (int localValue4 = 0; localValue4 < localValue0.slots.size(); localValue4++) {
            if (localValue0.getSlot(localValue4).hasStack()) {
               String localValue5 = internalMethod06937(internalMethod01418(localValue0.getSlot(localValue4).getStack()));
               if (internalMethod06256(localValue5, localValue2) && localValue5.contains("\u043b\u0430\u0439\u0442") && (localValue3.isBlank() || localValue5.contains(localValue3))) {
                  return localValue4;
               }
            }
         }

         return -1;
      }
   }

   public static int internalMethod03939(ServerModeCategory localValue0, int localValue1) {
      byte localValue2 = switch (localValue0) {
         case internalField0584 -> 1;
         case internalField0583 -> 13;
         case internalField1223 -> 27;
         case internalField1225 -> 40;
         default -> -1;
      };
      return localValue2 >= 0 && internalMethod03905(localValue1) == localValue0 ? 18 + (localValue1 - localValue2) : -1;
   }

   public static ServerModeCategory internalMethod03905(int localValue0) {
      if (localValue0 >= 1 && localValue0 <= 12) {
         return ServerModeCategory.internalField0584;
      } else if (localValue0 >= 13 && localValue0 <= 26) {
         return ServerModeCategory.internalField0583;
      } else if (localValue0 >= 27 && localValue0 <= 39) {
         return ServerModeCategory.internalField1223;
      } else {
         return localValue0 >= 40 ? ServerModeCategory.internalField1225 : ServerModeCategory.internalField1224;
      }
   }

   private static boolean internalMethod06256(String localValue0, int localValue1) {
      String localValue2 = "#" + localValue1;
      int localValue3 = 0;

      while (localValue3 < localValue0.length()) {
         int localValue4 = localValue0.indexOf(localValue2, localValue3);
         if (localValue4 < 0) {
            return false;
         }

         int localValue5 = localValue4 + localValue2.length();
         if (localValue5 == localValue0.length() || !Character.isDigit(localValue0.charAt(localValue5))) {
            return true;
         }

         localValue3 = localValue5;
      }

      return false;
   }

   private static String internalMethod01418(ItemStack localValue0) {
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
         internalMethod01395(localValue1, localValue4, "minecraft:custom_name");
         internalMethod01395(localValue1, localValue4, "minecraft:item_name");
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

   private static void internalMethod01395(StringBuilder localValue0, NbtCompound localValue1, String localValue2) {
      if (localValue1.contains(localValue2)) {
         localValue0.append(' ').append(localValue1.getString(localValue2));
      }
   }

   private static String internalMethod06937(String localValue0) {
      return localValue0 == null ? "" : localValue0.replaceAll("\u00a7.", "").replace('\u0451', '\u0435').replace('\u0401', '\u0415').toLowerCase(Locale.ROOT);
   }

   @Generated
   private ContainerMenuUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
