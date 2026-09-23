package aethereal.autobuy;

import java.util.HashMap;
import java.util.Map;

public class BuyConfig {
   private static final Map<String, Boolean> enabled = new HashMap<>();
   private static final Map<String, Long> prices = new HashMap<>();
   private static int discountPercentage = 15;

   public static boolean isEnabled(String id) {
      return enabled.getOrDefault(id, false);
   }

   public static long getPrice(String id) {
      return prices.getOrDefault(id, 0L);
   }

   public static int getDiscountPercentage() {
      return discountPercentage;
   }

   public static void setDiscountPercentage(int pct) {
      discountPercentage = pct;
   }

   public static void setEnabled(String id, boolean val) {
      enabled.put(id, val);
   }

   public static void setPrice(String id, long price) {
      prices.put(id, price);
   }
}
