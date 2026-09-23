package aethereal.autobuy;

import com.google.gson.JsonObject;

public class AnLogic {
   private static boolean enabled = false;
   private static boolean sellPaused = false;
   private static final JsonObject parsedPrices = new JsonObject();

   public static boolean isEnabled() {
      return enabled;
   }

   public static void toggle() {
      enabled = !enabled;
   }

   public static void update() {
      if (enabled) {
         ;
      }
   }

   public static void loadPrices() {
   }

   public static void setSellPaused(boolean paused) {
      sellPaused = paused;
   }

   public static boolean isSellPaused() {
      return sellPaused;
   }

   public static JsonObject getParsedPrices() {
      return parsedPrices;
   }

   public static String normalizeName(String name) {
      return name == null ? "" : name.toLowerCase().replaceAll("[^a-zа-я0-9]", "");
   }
}
