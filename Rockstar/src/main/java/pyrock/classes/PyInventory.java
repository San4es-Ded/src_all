package pyrock.classes;



import rockstar.client.inventory.*;
import rockstar.client.internal.game.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry.Reference;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.inventory.InventoryUtils;
import rockstar.client.inventory.InventorySlot;
import rockstar.client.internal.game.HotbarSlotSwitcher;
import rockstar.client.inventory.HotbarSlot;

public class PyInventory {
   public int selected() {
      return MinecraftClientAccess.internalField0149.player == null ? 0 : MinecraftClientAccess.internalField0149.player.getInventory().getSelectedSlot();
   }

   public boolean select(int localValue1) {
      if (localValue1 < 0 || localValue1 > 8 || MinecraftClientAccess.internalField0149.player == null || MinecraftClientAccess.internalField0149.getNetworkHandler() == null) {
         return false;
      } else if (MinecraftClientAccess.internalField0149.player.getInventory().getSelectedSlot() == localValue1) {
         return true;
      } else {
         MinecraftClientAccess.internalField0149.player.getInventory().setSelectedSlot(localValue1);
         MinecraftClientAccess.internalField0149.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(localValue1));
         return true;
      }
   }

   @Nullable
   public ItemStack stack(int localValue1) {
      InventorySlot localValue2 = slot(localValue1);
      if (localValue2 == null) {
         return null;
      } else {
         ItemStack localValue3 = localValue2.internalMethod03427();
         return localValue3 != null && !localValue3.isEmpty() ? localValue3 : null;
      }
   }

   @Nullable
   public String id(int localValue1) {
      ItemStack localValue2 = this.stack(localValue1);
      return localValue2 == null ? null : Registries.ITEM.getId(localValue2.getItem()).toString();
   }

   @Nullable
   public String label(int localValue1) {
      ItemStack localValue2 = this.stack(localValue1);
      return localValue2 == null ? null : localValue2.getName().getString();
   }

   public int count(int localValue1) {
      ItemStack localValue2 = this.stack(localValue1);
      return localValue2 == null ? 0 : localValue2.getCount();
   }

   public int find(String localValue1, String localValue2) {
      Item localValue3 = item(localValue1);
      if (localValue3 == null) {
         return -1;
      } else {
         for (int localValue7 : range(localValue2)) {
            ItemStack localValue8 = this.stack(localValue7);
            if (localValue8 != null && localValue8.getItem() == localValue3) {
               return localValue7;
            }
         }

         return -1;
      }
   }

   public List<Integer> findAll(String localValue1, String localValue2) {
      ArrayList localValue3 = new ArrayList();
      Item localValue4 = item(localValue1);
      if (localValue4 == null) {
         return localValue3;
      } else {
         for (int localValue8 : range(localValue2)) {
            ItemStack localValue9 = this.stack(localValue8);
            if (localValue9 != null && localValue9.getItem() == localValue4) {
               localValue3.add(localValue8);
            }
         }

         return localValue3;
      }
   }

   public int total(String localValue1, String localValue2) {
      Item localValue3 = item(localValue1);
      if (localValue3 == null) {
         return 0;
      } else {
         int localValue4 = 0;

         for (int localValue8 : range(localValue2)) {
            ItemStack localValue9 = this.stack(localValue8);
            if (localValue9 != null && localValue9.getItem() == localValue3) {
               localValue4 += localValue9.getCount();
            }
         }

         return localValue4;
      }
   }

   public int empty(String localValue1) {
      for (int localValue5 : range(localValue1)) {
         if (this.stack(localValue5) == null) {
            return localValue5;
         }
      }

      return -1;
   }

   public int hold(String localValue1) {
      int localValue2 = this.find(localValue1, "hotbar");
      return localValue2 >= 0 && this.select(localValue2) ? localValue2 : -1;
   }

   public boolean swap(int localValue1, int localValue2) {
      if (slot(localValue1) != null && slot(localValue2) != null && MinecraftClientAccess.internalField0149.player != null) {
         InventoryUtils.internalMethod05750(localValue1, localValue2);
         return true;
      } else {
         return false;
      }
   }

   public boolean move(int localValue1, int localValue2) {
      InventorySlot localValue3 = slot(localValue1);
      InventorySlot localValue4 = slot(localValue2);
      if (localValue3 != null && localValue4 != null) {
         InventoryUtils.internalMethod01016(localValue3, localValue4);
         return true;
      } else {
         return false;
      }
   }

   public boolean quickMove(int localValue1) {
      InventorySlot localValue2 = slot(localValue1);
      if (localValue2 != null && MinecraftClientAccess.internalField0149.player != null) {
         InventoryUtils.internalMethod03592(localValue2.internalMethod06662());
         return true;
      } else {
         return false;
      }
   }

   public boolean toOffhand(int localValue1) {
      InventorySlot localValue2 = slot(localValue1);
      if (localValue2 == null) {
         return false;
      } else {
         InventoryUtils.internalMethod02096(localValue2);
         return true;
      }
   }

   public boolean toArmor(int localValue1, String localValue2) {
      InventorySlot localValue3 = slot(localValue1);
      int localValue4 = armorIndex(localValue2);
      return localValue3 != null && localValue4 >= 0 ? InventoryUtils.internalMethod05406(localValue3, localValue4) : false;
   }

   public boolean withSlot(int localValue1, Runnable localValue2) {
      if (localValue1 >= 0 && localValue1 <= 8 && localValue2 != null) {
         HotbarSlot localValue3 = InventoryUtils.internalMethod02738(localValue1);
         HotbarSlotSwitcher.internalMethod07593(localValue3, localValue2);
         return true;
      } else {
         return false;
      }
   }

   public double durability(int localValue1) {
      ItemStack localValue2 = this.stack(localValue1);
      if (localValue2 != null && localValue2.isDamageable()) {
         int localValue3 = localValue2.getMaxDamage();
         return localValue3 <= 0 ? -1.0 : 100.0 * (localValue3 - localValue2.getDamage()) / localValue3;
      } else {
         return -1.0;
      }
   }

   public int enchant(int localValue1, String localValue2) {
      ItemStack localValue3 = this.stack(localValue1);
      if (localValue3 != null && MinecraftClientAccess.internalField0149.world != null && localValue2 != null) {
         Identifier localValue4 = identifier(localValue2);
         if (localValue4 == null) {
            return 0;
         } else {
            try {
               Registry localValue5 = MinecraftClientAccess.internalField0149.world.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT);
               Reference localValue6 = (Reference)localValue5.getEntry(localValue4).orElse(null);
               return localValue6 == null ? 0 : EnchantmentHelper.getLevel(localValue6, localValue3);
            } catch (Throwable localValue7) {
               return 0;
            }
         }
      } else {
         return 0;
      }
   }

   public List<Integer> slots(String localValue1) {
      ArrayList localValue2 = new ArrayList();

      for (int localValue6 : range(localValue1)) {
         localValue2.add(localValue6);
      }

      return localValue2;
   }

   public boolean offhandIs(String localValue1) {
      Item localValue2 = item(localValue1);
      return localValue2 != null && InventoryUtils.internalMethod06573(localValue2);
   }

   public boolean containerOpen() {
      return MinecraftClientAccess.internalField0149.player != null
         && MinecraftClientAccess.internalField0149.currentScreen != null
         && MinecraftClientAccess.internalField0149.player.currentScreenHandler != MinecraftClientAccess.internalField0149.player.playerScreenHandler;
   }

   @Nullable
   private static InventorySlot slot(int localValue0) {
      if (MinecraftClientAccess.internalField0149.player == null) {
         return null;
      } else if (localValue0 >= 0 && localValue0 <= 8) {
         return InventoryUtils.internalMethod02738(localValue0);
      } else if (localValue0 >= 9 && localValue0 <= 35) {
         return InventoryUtils.internalMethod02743(localValue0 - 9);
      } else if (localValue0 >= 36 && localValue0 <= 39) {
         return InventoryUtils.internalMethod02737(39 - localValue0);
      } else {
         return localValue0 == 40 ? InventoryUtils.internalMethod06162() : null;
      }
   }

   private static int armorIndex(String localValue0) {
      if (localValue0 == null) {
         return -1;
      } else {
         String localValue1 = localValue0.toLowerCase(Locale.ROOT);

         return switch (localValue1) {
            case "boots", "\u0431\u043e\u0442\u0438\u043d\u043a\u0438" -> 0;
            case "leggings", "\u043f\u043e\u043d\u043e\u0436\u0438", "\u0448\u0442\u0430\u043d\u044b" -> 1;
            case "chestplate", "\u043d\u0430\u0433\u0440\u0443\u0434\u043d\u0438\u043a" -> 2;
            case "helmet", "\u0448\u043b\u0435\u043c" -> 3;
            default -> -1;
         };
      }
   }

   private static int[] range(String localValue0) {
      String localValue1 = localValue0 == null ? "all" : localValue0.toLowerCase(Locale.ROOT);

      return switch (localValue1) {
         case "hotbar" -> ints(0, 8);
         case "main", "inventory", "backpack" -> ints(9, 35);
         case "armor" -> ints(36, 39);
         case "offhand" -> new int[]{40};
         default -> ints(0, 40);
      };
   }

   private static int[] ints(int localValue0, int localValue1) {
      int[] localValue2 = new int[localValue1 - localValue0 + 1];

      for (int localValue3 = 0; localValue3 < localValue2.length; localValue3++) {
         localValue2[localValue3] = localValue0 + localValue3;
      }

      return localValue2;
   }

   @Nullable
   private static Identifier identifier(String localValue0) {
      if (localValue0 != null && !localValue0.isBlank()) {
         return localValue0.contains(":") ? Identifier.tryParse(localValue0) : Identifier.tryParse("minecraft:" + localValue0);
      } else {
         return null;
      }
   }

   @Nullable
   private static Item item(String localValue0) {
      Identifier localValue1 = identifier(localValue0);
      if (localValue1 == null) {
         return null;
      } else {
         Item localValue2 = (Item)Registries.ITEM.get(localValue1);
         return localValue2 == Items.AIR && !"minecraft:air".equals(localValue1.toString()) ? null : localValue2;
      }
   }
}
