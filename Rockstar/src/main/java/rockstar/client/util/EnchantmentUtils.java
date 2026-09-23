package rockstar.client.util;


import rockstar.client.*;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntMaps;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.List;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;

public final class EnchantmentUtils {
   public static void internalMethod03498(ItemStack localValue0, Object2IntMap<RegistryEntry<Enchantment>> localValue1) {
      localValue1.clear();
      if (!localValue0.isEmpty()) {
         for (Entry localValue4 : localValue0.getItem() == Items.ENCHANTED_BOOK
            ? ((ItemEnchantmentsComponent)localValue0.get(DataComponentTypes.STORED_ENCHANTMENTS)).getEnchantmentEntries()
            : localValue0.getEnchantments().getEnchantmentEntries()) {
            localValue1.put((RegistryEntry)localValue4.getKey(), localValue4.getIntValue());
         }
      }
   }

   @SafeVarargs
   public static boolean internalMethod01710(ItemStack localValue0, RegistryKey<Enchantment>... localValue1) {
      if (localValue0.isEmpty()) {
         return false;
      } else {
         Object2IntArrayMap localValue2 = new Object2IntArrayMap();
         internalMethod03498(localValue0, localValue2);

         for (RegistryKey localValue6 : localValue1) {
            if (!internalMethod02286(localValue2, localValue6)) {
               return false;
            }
         }

         return true;
      }
   }

   public static boolean internalMethod03136(List<Text> localValue0, String localValue1) {
      for (Text localValue3 : localValue0) {
         String localValue4 = localValue3.getString().toLowerCase();
         if (localValue4.contains(localValue1.toLowerCase())) {
            return true;
         }
      }

      return false;
   }

   public static boolean internalMethod06801(List<Text> localValue0, String localValue1, int localValue2) {
      for (Text localValue4 : localValue0) {
         String localValue5 = localValue4.getString().toLowerCase();
         if (localValue5.contains(localValue1.toLowerCase())) {
            if (localValue2 <= 1) {
               return true;
            }

            if (localValue2 != 2 || !localValue5.contains("ii") && !localValue5.contains(" 2")) {
               if (localValue2 != 3 || !localValue5.contains("iii") && !localValue5.contains(" 3")) {
                  if (localValue2 != 4 || !localValue5.contains("iv") && !localValue5.contains(" 4")) {
                     if (localValue2 != 5 || !localValue5.contains("v") && !localValue5.contains(" 5")) {
                        String localValue6 = String.valueOf(localValue2);
                        if (!localValue5.contains(" " + localValue6) && !localValue5.contains(localValue6 + " ")) {
                           continue;
                        }

                        return true;
                     }

                     return true;
                  }

                  return true;
               }

               return true;
            }

            return true;
         }
      }

      return false;
   }

   public static int internalMethod03526(ItemStack localValue0, RegistryKey<Enchantment> localValue1) {
      if (localValue0.isEmpty()) {
         return 0;
      } else {
         Object2IntArrayMap localValue2 = new Object2IntArrayMap();
         internalMethod03498(localValue0, localValue2);
         return internalMethod02285(localValue2, localValue1);
      }
   }

   public static int internalMethod02285(Object2IntMap<RegistryEntry<Enchantment>> localValue0, RegistryKey<Enchantment> localValue1) {
      ObjectIterator localValue2 = Object2IntMaps.fastIterable(localValue0).iterator();

      while (localValue2.hasNext()) {
         Entry localValue3 = (Entry)localValue2.next();
         if (((RegistryEntry)localValue3.getKey()).matchesKey(localValue1)) {
            return localValue3.getIntValue();
         }
      }

      return 0;
   }

   private static boolean internalMethod02286(Object2IntMap<RegistryEntry<Enchantment>> localValue0, RegistryKey<Enchantment> localValue1) {
      ObjectIterator localValue2 = localValue0.keySet().iterator();

      while (localValue2.hasNext()) {
         RegistryEntry localValue3 = (RegistryEntry)localValue2.next();
         if (localValue3.matchesKey(localValue1)) {
            return true;
         }
      }

      return false;
   }

   @Generated
   private EnchantmentUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
