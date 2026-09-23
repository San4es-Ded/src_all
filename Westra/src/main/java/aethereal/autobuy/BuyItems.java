package aethereal.autobuy;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_1792;

public class BuyItems {
   private static final List<BuyItems.BuyItem> items = new ArrayList<>();

   public static List<BuyItems.BuyItem> getItems() {
      return items;
   }

   public static void register(BuyItems.BuyItem item) {
      items.add(item);
   }

   public static enum AuthCheck {
      NONE,
      STARRED_HEADER;
   }

   public static class BuyItem {
      private final String id;
      private final String displayName;
      private final class_1792 item;
      private final String ahSearchQuery;
      private final BuyItems.AuthCheck authCheck;

      public BuyItem(String id, String displayName, class_1792 item, String ahSearchQuery, BuyItems.AuthCheck authCheck) {
         this.id = id;
         this.displayName = displayName;
         this.item = item;
         this.ahSearchQuery = ahSearchQuery;
         this.authCheck = authCheck;
      }

      public String getId() {
         return this.id;
      }

      public String getDisplayName() {
         return this.displayName;
      }

      public class_1792 getItem() {
         return this.item;
      }

      public String getAhSearchQuery() {
         return this.ahSearchQuery;
      }

      public BuyItems.AuthCheck getAuthCheck() {
         return this.authCheck;
      }
   }
}
