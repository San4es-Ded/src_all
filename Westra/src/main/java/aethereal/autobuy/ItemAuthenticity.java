package aethereal.autobuy;

import java.util.List;
import net.minecraft.class_2561;

public class ItemAuthenticity {
   public static String normalizeName(String name) {
      return name == null ? "" : name.toLowerCase().replaceAll("[^a-zа-я0-9]", "");
   }

   public static boolean passesCheck(BuyItems.BuyItem item, List<class_2561> lore) {
      return true;
   }
}
