package rockstar.client.internal.inventory;



import rockstar.client.util.*;
import rockstar.client.*;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import lombok.Generated;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public final class CustomItemRankingUtils implements MinecraftClientAccess {
   private static final Map<String, Integer> internalField0543 = Map.of(
      "\u0410\u0440\u0435\u0441\u0430",
      1,
      "\u0425\u0430\u043e\u0441\u0430",
      2,
      "\u0422\u0438\u0442\u0430\u043d\u0430",
      3,
      "\u0413\u0438\u0434\u0440\u044b",
      4,
      "\u0411\u0435\u0441\u0442\u0438\u0438",
      5,
      "\u0418\u043a\u0430\u0440\u0430",
      6,
      "\u0421\u0430\u0442\u0438\u0440\u0430",
      7,
      "\u042d\u0440\u0438\u0434\u0430",
      8,
      "\u041c\u043e\u0440\u043e\u0437\u0430",
      9
   );
   private static final Map<String, Integer> internalField0544 = Map.of(
      "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f",
      1,
      "\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f",
      2,
      "\u042f\u0440\u043e\u0441\u0442\u0438",
      3,
      "\u0420\u0430\u0437\u0434\u043e\u0440\u0430",
      4,
      "\u0422\u0438\u0440\u0430\u043d\u0430",
      5,
      "\u0414\u0435\u043c\u043e\u043d\u0430",
      6,
      "\u0412\u0438\u0445\u0440\u044f",
      7,
      "\u041c\u0440\u0430\u043a\u0430",
      8,
      "\u0413\u0440\u0438\u043d\u0447\u0430",
      9
   );
   private static final Map<String, Integer> internalField1197 = Map.of("Infinity", 1, "Eternity", 2, "Stinger", 3);
   private static final Map<String, Integer> internalField1196 = Map.of("Armortality", 3, "Eternity", 4, "Immortal", 5, "Stinger", 6, "Flash", 7, "Cerber", 8);
   private static final Map<String, Integer> internalField1195 = Map.of(
      "\u041a\u0440\u0443\u0448\u0438\u0442\u0435\u043b\u044f",
      1,
      "\u041a\u0430\u0440\u0430\u0442\u0435\u043b\u044f",
      2,
      "\u042f\u0440\u043e\u0441\u0442\u0438",
      3,
      "\u0414\u0435\u0434\u0430\u043b\u0430",
      4,
      "\u0413\u0440\u0430\u043d\u0438",
      5,
      "\u0413\u0430\u0440\u043c\u043e\u043d\u0438\u0438",
      6,
      "\u0415\u0445\u0438\u0434\u043d\u044b",
      7,
      "\u0422\u0440\u0438\u0442\u043e\u043d\u0430",
      8,
      "\u0424\u0435\u043d\u0438\u043a\u0441\u0430",
      9
   );
   private static final Map<String, Integer> internalField1198 = Map.of(
      "\u0410\u043d\u0434\u0440\u043e\u043c\u0435\u0434\u044b",
      1,
      "\u0422\u0438\u0442\u0430\u043d\u0430",
      2,
      "\u0410\u043f\u043e\u043b\u043b\u043e\u043d\u0430",
      3,
      "\u0410\u0441\u0442\u0440\u0435\u044f",
      4,
      "\u041e\u0441\u0438\u0440\u0438\u0441\u0430",
      5,
      "\u041f\u0430\u043d\u0434\u043e\u0440\u044b",
      6,
      "\u0425\u0438\u043c\u0435\u0440\u044b",
      7
   );
   private static final Map<String, Integer> internalField1560 = Map.of(
      "attribute-item-tkryshitela",
      1,
      "attribute-item-tkaratela",
      2,
      "attribute-item-tjarosti",
      3,
      "attribute-item-trazdora",
      4,
      "attribute-item-ttirana",
      5,
      "attribute-item-tdemona",
      6,
      "attribute-item-tvihra",
      7,
      "attribute-item-tmraka",
      8,
      "attribute-item-tgrincha",
      9
   );
   private static final Map<String, Integer> internalField1559 = Map.of(
      "attribute-item-saresa",
      1,
      "attribute-item-shaosa",
      2,
      "attribute-item-stitana",
      3,
      "attribute-item-sgidri",
      4,
      "attribute-item-sbestii",
      5,
      "attribute-item-sikara",
      6,
      "attribute-item-ssatira",
      7,
      "attribute-item-serida",
      8,
      "attribute-item-smoroza",
      9
   );
   public static int internalMethod04472(ItemStack localValue0) {
      if (localValue0 != null && !localValue0.isEmpty() && localValue0.getItem() == Items.TOTEM_OF_UNDYING) {
         CustomItemUtils.InternalType0254 localValue1 = CustomItemUtils.internalMethod03238(localValue0);
         if (localValue1 == null) {
            return localValue0.hasEnchantments() ? 1 : 0;
         } else if (localValue1.internalMethod08992()) {
            String localValue2 = localValue1.internalMethod01319();
            if (localValue2 != null && !localValue2.isEmpty()) {
               int localValue3 = internalMethod02639(localValue1);
               return localValue3 == 999 ? 1000 : 100 - localValue3;
            } else {
               return localValue0.hasEnchantments() ? 1 : 0;
            }
         } else {
            return 50;
         }
      } else {
         return Integer.MAX_VALUE;
      }
   }

   public static int internalMethod05103(ItemStack localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         CustomItemUtils.InternalType0254 localValue1 = CustomItemUtils.internalMethod03238(localValue0);
         return localValue1 != null ? internalMethod02639(localValue1) : 999;
      } else {
         return 999;
      }
   }

   public static int internalMethod02639(CustomItemUtils.InternalType0254 localValue0) {
      if (localValue0 == null) {
         return 999;
      } else {
         return switch (localValue0.internalMethod07218()) {
            case internalField1338 -> internalMethod07925(localValue0);
            case internalField1336 -> internalMethod08776(localValue0);
            case internalField0834, internalField0835 -> internalMethod00465(localValue0);
            case internalField1335 -> internalMethod08615(localValue0);
            default -> 999;
         };
      }
   }

   private static int internalMethod00465(CustomItemUtils.InternalType0254 localValue0) {
      String localValue1 = localValue0.internalMethod01319();
      if (localValue0.internalMethod08992()) {
         if (localValue1 != null && internalField1197.containsKey(localValue1)) {
            return internalField1197.get(localValue1);
         } else {
            CustomItemUtils.InternalType0441 localValue3 = localValue0.internalMethod07219();
            if (localValue3 != null) {
               return switch (localValue3) {
                  case internalField1341 -> 4;
                  case internalField1339 -> 5;
                  case internalField0836 -> 6;
                  case internalField0837 -> 7;
                  default -> 999;
               };
            } else {
               return 999;
            }
         }
      } else if (localValue0.internalMethod08988()) {
         if (localValue1 != null && internalField1196.containsKey(localValue1)) {
            return internalField1196.get(localValue1);
         } else {
            int localValue2 = localValue0.internalMethod08987();
            return localValue2 > 0 ? 10 - localValue2 : 999;
         }
      } else {
         return 999;
      }
   }

   private static int internalMethod07925(CustomItemUtils.InternalType0254 localValue0) {
      String localValue1 = localValue0.internalMethod01319();
      if (localValue1 == null) {
         return 999;
      } else if (localValue0.internalMethod08988()) {
         return internalField0543.getOrDefault(localValue1, 999);
      } else {
         return localValue0.internalMethod08992() ? internalField0544.getOrDefault(localValue1, 999) : 999;
      }
   }

   private static int internalMethod08776(CustomItemUtils.InternalType0254 localValue0) {
      String localValue1 = localValue0.internalMethod01319();
      if (localValue1 == null) {
         return 999;
      } else if (localValue0.internalMethod08988()) {
         return internalField1198.getOrDefault(localValue1, 999);
      } else {
         return localValue0.internalMethod08992() ? internalField1195.getOrDefault(localValue1, 999) : 999;
      }
   }

   private static int internalMethod08615(CustomItemUtils.InternalType0254 localValue0) {
      String localValue1 = localValue0.internalMethod01319();
      if (localValue1 == null) {
         return 999;
      } else if (localValue0.internalMethod08988()) {
         return internalField1559.getOrDefault(localValue1, 999);
      } else {
         return localValue0.internalMethod08992() ? internalField1560.getOrDefault(localValue1, 999) : 999;
      }
   }

   private static int internalMethod08203(CustomItemUtils.InternalType0254 localValue0) {
      if (localValue0 == null) {
         return 999;
      } else if ("Cerber".equals(localValue0.internalMethod01319())) {
         return 200;
      } else {
         int localValue1 = localValue0.internalMethod08985();
         int localValue2 = localValue0.internalMethod06309();
         if (localValue1 >= 3) {
            return 10 - localValue1;
         } else if (localValue2 >= 3) {
            return 20 - localValue2;
         } else if (localValue0.internalMethod08986()) {
            return 30 + internalField1196.getOrDefault(localValue0.internalMethod01319(), 999);
         } else {
            return localValue1 == 2 ? 40 - localValue1 : 50 - localValue2;
         }
      }
   }

   public static ItemStack internalMethod07640(List<ItemStack> localValue0) {
      return internalMethod01159(localValue0, CustomItemUtils.InternalType0254::internalMethod08988, Comparator.comparingInt(CustomItemRankingUtils::internalMethod05103));
   }

   public static ItemStack internalMethod07158(List<ItemStack> localValue0) {
      return internalMethod02213(localValue0, CustomItemUtils.InternalType0254::internalMethod08988, Comparator.comparingInt(CustomItemRankingUtils::internalMethod05103));
   }

   public static ItemStack internalMethod08465(List<ItemStack> localValue0) {
      return internalMethod01159(localValue0, CustomItemUtils.InternalType0254::internalMethod08992, Comparator.comparingInt(CustomItemRankingUtils::internalMethod05103));
   }

   public static ItemStack internalMethod08390(List<ItemStack> localValue0) {
      return internalMethod02213(localValue0, CustomItemUtils.InternalType0254::internalMethod08992, Comparator.comparingInt(CustomItemRankingUtils::internalMethod05103));
   }

   public static ItemStack internalMethod08812(List<ItemStack> localValue0) {
      boolean localValue1 = localValue0.stream().anyMatch(localValue0x -> localValue0x.getItem() == Items.TOTEM_OF_UNDYING && CustomItemUtils.internalMethod03238(localValue0x) == null);
      return localValue1 ? null : internalMethod08390(localValue0);
   }

   public static ItemStack internalMethod08722(List<ItemStack> localValue0) {
      return internalMethod01159(
         localValue0,
         localValue0x -> localValue0x.internalMethod08988() && localValue0x.internalMethod06306() >= 3,
         Comparator.comparingInt(localValue0x -> 10 - CustomItemUtils.internalMethod03238(localValue0x).internalMethod06306())
      );
   }

   public static ItemStack internalMethod09471(List<ItemStack> localValue0) {
      return internalMethod01159(
         localValue0,
         localValue0x -> localValue0x.internalMethod08988() && localValue0x.internalMethod06306() < 3,
         Comparator.comparingInt(localValue0x -> internalMethod08203(CustomItemUtils.internalMethod03238(localValue0x)))
      );
   }

   public static ItemStack internalMethod09915(List<ItemStack> localValue0) {
      return internalMethod02876(
         localValue0,
         localValue0x -> localValue0x.internalMethod08988() && localValue0x.internalMethod06306() < 3,
         Comparator.comparingInt(localValue0x -> internalMethod08203(CustomItemUtils.internalMethod03238(localValue0x))),
         internalMethod09471(localValue0)
      );
   }

   private static ItemStack internalMethod01159(List<ItemStack> localValue0, Predicate<CustomItemUtils.InternalType0254> localValue1, Comparator<ItemStack> localValue2) {
      return internalMethod02876(localValue0, localValue1, localValue2, null);
   }

   private static ItemStack internalMethod02876(
      List<ItemStack> localValue0, Predicate<CustomItemUtils.InternalType0254> localValue1, Comparator<ItemStack> localValue2, ItemStack localValue3
   ) {
      return localValue0.stream().filter(localValue2x -> {
         CustomItemUtils.InternalType0254 localValue3x = CustomItemUtils.internalMethod03238(localValue2x);
         return localValue3x != null && localValue1.test(localValue3x) && (localValue3 == null || !ItemStack.areEqual(localValue2x, localValue3));
      }).min(localValue2).orElse(null);
   }

   private static ItemStack internalMethod02213(List<ItemStack> localValue0, Predicate<CustomItemUtils.InternalType0254> localValue1, Comparator<ItemStack> localValue2) {
      return localValue0.stream().filter(localValue1x -> {
         CustomItemUtils.InternalType0254 localValue2x = CustomItemUtils.internalMethod03238(localValue1x);
         return localValue2x != null && localValue1.test(localValue2x);
      }).max(localValue2).orElse(null);
   }

   @Generated
   private CustomItemRankingUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
