package rockstar.client.internal.inventory;


import rockstar.client.*;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.Generated;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.component.type.PotionContentsComponent;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;

public final class DonItemCatalog {
   private static final String[] internalField0359 = new String[]{"consumable", "talisman", "sphere", "tools", "armor", "potions", "other"};
   private static JsonObject internalField0539;

   public static void internalMethod00744() {
      try {
         InputStream localValue0 = DonItemCatalog.class.getResourceAsStream("/assets/rockstar/donate_items.json");
         if (localValue0 == null) {
            return;
         }

         InputStreamReader localValue1 = new InputStreamReader(localValue0, StandardCharsets.UTF_8);
         internalField0539 = (JsonObject)new Gson().fromJson(localValue1, JsonObject.class);
         localValue1.close();
      } catch (Exception localValue2) {
      }
   }

   public static List<DonItemCatalog.InternalType0503> internalMethod02566(DonItemCatalog.InternalType0189 localValue0) {
      if (localValue0 == DonItemCatalog.InternalType0189.internalField0406) {
         return internalMethod07134();
      } else {
         if (internalField0539 == null) {
            internalMethod00744();
         }

         if (internalField0539 != null && internalField0539.has(localValue0.internalMethod03969())) {
            ArrayList localValue1 = new ArrayList();
            JsonObject localValue2 = internalField0539.getAsJsonObject(localValue0.internalMethod03969());

            for (String localValue6 : internalField0359) {
               if (localValue2.has(localValue6)) {
                  List localValue7 = internalMethod03019(localValue2, localValue6, localValue0);
                  if (!localValue7.isEmpty()) {
                     localValue1.add(new DonItemCatalog.InternalType0503(localValue6, internalMethod04737(localValue6), localValue7));
                  }
               }
            }

            return localValue1;
         } else {
            return List.of();
         }
      }
   }

   public static Map<String, String> internalMethod06733(DonItemCatalog.InternalType0189 localValue0) {
      if (internalField0539 == null) {
         internalMethod00744();
      }

      LinkedHashMap localValue1 = new LinkedHashMap();
      if (internalField0539 != null && internalField0539.has(localValue0.internalMethod03969())) {
         JsonObject localValue2 = internalField0539.getAsJsonObject(localValue0.internalMethod03969());

         for (String localValue6 : internalField0359) {
            if (localValue2.has(localValue6)) {
               for (JsonElement localValue8 : localValue2.getAsJsonArray(localValue6)) {
                  JsonObject localValue9 = localValue8.getAsJsonObject();
                  if (localValue9.has("name") && localValue9.has("lore")) {
                     localValue1.put(localValue9.get("name").getAsString(), localValue9.get("lore").getAsString());
                  }
               }
            }
         }

         return localValue1;
      } else {
         return localValue1;
      }
   }

   public static List<DonItemCatalog.InternalType0190> internalMethod01447() {
      ArrayList<DonItemCatalog.InternalType0190> localValue0 = new ArrayList<>();
      HashSet<String> localValue1 = new HashSet<>();

      for (DonItemCatalog.InternalType0189 localValue5 : new DonItemCatalog.InternalType0189[]{DonItemCatalog.InternalType0189.internalField0407}) {
         for (DonItemCatalog.InternalType0503 localValue7 : internalMethod02566(localValue5)) {
            for (DonItemCatalog.InternalType0190 localValue9 : localValue7.internalMethod07260()) {
               String localValue10 = localValue9.internalMethod06194();
               if (localValue10 != null && !localValue10.isBlank() && localValue1.add(localValue10.toLowerCase())) {
                  localValue0.add(localValue9);
               }
            }
         }
      }

      localValue0.sort((localValue0x, localValue1x) -> localValue0x.internalMethod06194().compareToIgnoreCase(localValue1x.internalMethod06194()));
      return localValue0;
   }

   private static List<DonItemCatalog.InternalType0190> internalMethod03019(JsonObject localValue0, String localValue1, DonItemCatalog.InternalType0189 localValue2) {
      ArrayList localValue3 = new ArrayList();
      localValue0.getAsJsonArray(localValue1).forEach(localValue3x -> {
         JsonObject localValue4 = localValue3x.getAsJsonObject();
         String localValue5 = localValue4.get("item").getAsString();
         String localValue6 = localValue4.has("name") ? localValue4.get("name").getAsString() : null;
         String localValue7 = localValue4.has("id") ? localValue4.get("id").getAsString() : null;
         String localValue8 = localValue4.has("nbtKey") ? localValue4.get("nbtKey").getAsString() : null;
         String localValue9 = localValue4.has("texture") ? localValue4.get("texture").getAsString() : null;
         String localValue10 = localValue2.internalMethod03969() + "_" + localValue1 + "_" + localValue7;

         try {
            Identifier localValue11 = Identifier.tryParse(localValue5);
            if (localValue11 != null) {
               Item localValue12 = (Item)Registries.ITEM.get(localValue11);
               if (localValue12 != Items.AIR) {
                  ItemStack localValue13 = localValue12.getDefaultStack();
                  if (localValue8 != null) {
                     internalMethod04601(localValue13, localValue8, localValue2);
                  }

                  if (localValue9 != null && !localValue9.isBlank()) {
                     internalMethod02608(localValue13, localValue9);
                  }

                  localValue3.add(new DonItemCatalog.InternalType0190(localValue13, localValue6, localValue10));
               }
            }
         } catch (Exception localValue14) {
         }
      });
      return localValue3;
   }

   private static String internalMethod04737(String localValue0) {
      return switch (localValue0) {
         case "consumable" -> "\u0420\u0430\u0441\u0445\u043e\u0434\u043d\u0438\u043a\u0438";
         case "talisman" -> "\u0422\u0430\u043b\u0438\u0441\u043c\u0430\u043d\u044b";
         case "sphere" -> "\u0421\u0444\u0435\u0440\u044b";
         case "tools" -> "\u0418\u043d\u0441\u0442\u0440\u0443\u043c\u0435\u043d\u0442\u044b";
         case "armor" -> "\u0411\u0440\u043e\u043d\u044f";
         case "potions" -> "\u0417\u0435\u043b\u044c\u044f";
         case "other" -> "\u0414\u0440\u0443\u0433\u043e\u0435";
         default -> localValue0;
      };
   }

   private static List<DonItemCatalog.InternalType0503> internalMethod07134() {
      ArrayList localValue0 = new ArrayList();
      Registries.ITEM.stream().filter(localValue0x -> localValue0x != Items.AIR).forEach(localValue1 -> {
         if (internalMethod01116(localValue1)) {
            internalMethod07410(localValue1, localValue0);
         } else {
            internalMethod01982(localValue1, localValue0);
         }
      });
      return List.of(new DonItemCatalog.InternalType0503("all", "\u0412\u0441\u0435 \u043f\u0440\u0435\u0434\u043c\u0435\u0442\u044b", localValue0));
   }

   private static boolean internalMethod01116(Item localValue0) {
      return localValue0 == Items.POTION || localValue0 == Items.SPLASH_POTION || localValue0 == Items.LINGERING_POTION;
   }

   private static void internalMethod07410(Item localValue0, List<DonItemCatalog.InternalType0190> localValue1) {
      Registries.POTION.streamEntries().forEach(localValue2 -> {
         try {
            ItemStack localValue3 = localValue0.getDefaultStack();
            localValue3.set(DataComponentTypes.POTION_CONTENTS, new PotionContentsComponent(localValue2));
            String localValue4 = Registries.ITEM.getId(localValue0).toString() + "_" + localValue2.getIdAsString();
            localValue1.add(new DonItemCatalog.InternalType0190(localValue3, null, localValue4));
         } catch (Exception localValue5) {
         }
      });
   }

   private static void internalMethod01982(Item localValue0, List<DonItemCatalog.InternalType0190> localValue1) {
      try {
         localValue1.add(new DonItemCatalog.InternalType0190(localValue0.getDefaultStack(), null, Registries.ITEM.getId(localValue0).toString()));
      } catch (Exception localValue3) {
      }
   }

   private static void internalMethod04601(ItemStack localValue0, String localValue1, DonItemCatalog.InternalType0189 localValue2) {
      try {
         NbtCompound localValue3 = new NbtCompound();
         if (localValue2 == DonItemCatalog.InternalType0189.internalField0407) {
            NbtCompound localValue4 = new NbtCompound();
            localValue4.putString("minecraft:don-item", localValue1);
            if (localValue1.startsWith("potion-")) {
               localValue4.putBoolean("minecraft:is-tshop", true);
            }

            localValue3.put("PublicBukkitValues", localValue4);
         }

         localValue0.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(localValue3));
      } catch (Exception localValue5) {
      }
   }

   private static void internalMethod02608(ItemStack localValue0, String localValue1) {
      try {
         PropertyMap localValue2 = new PropertyMap(com.google.common.collect.ImmutableMultimap.of("textures", new Property("textures", localValue1)));
         localValue0.set(
            DataComponentTypes.PROFILE,
            ProfileComponent.ofStatic(new com.mojang.authlib.GameProfile(UUID.nameUUIDFromBytes(localValue1.getBytes(StandardCharsets.UTF_8)), "", localValue2))
         );
      } catch (Exception localValue3) {
      }
   }

   @Generated
   private DonItemCatalog() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static enum InternalType0189 {
      internalField0406("all", "\u0412\u0441\u0435"),
      internalField0407("funtime", "Funtime");

      private final String internalField0248;
      private final String internalField0247;

      private InternalType0189(String localValue3, String localValue4) {
         this.internalField0248 = localValue3;
         this.internalField0247 = localValue4;
      }

      @Generated
      public String internalMethod03969() {
         return this.internalField0248;
      }

      @Generated
      public String internalMethod00534() {
         return this.internalField0247;
      }
   }

   public static final class InternalType0190 {
      private final ItemStack internalField0878;
      private final String internalField0248;
      private final String internalField0247;

      public InternalType0190(ItemStack localValue1, String localValue2, String localValue3) {
         this.internalField0878 = localValue1;
         this.internalField0248 = localValue2;
         this.internalField0247 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0190[stack=" + this.internalField0878 + ", customName=" + this.internalField0248 + ", id=" + this.internalField0247 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         DonItemCatalog.InternalType0190 other = (DonItemCatalog.InternalType0190) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247);
      }

      public ItemStack internalMethod05752() {
         return this.internalField0878;
      }

      public String internalMethod06194() {
         return this.internalField0248;
      }

      public String internalMethod02702() {
         return this.internalField0247;
      }
   }

   public static final class InternalType0503 {
      private final String internalField0248;
      private final String internalField0247;
      private final List<DonItemCatalog.InternalType0190> internalField0416;

      public InternalType0503(String localValue1, String localValue2, List<DonItemCatalog.InternalType0190> localValue3) {
         this.internalField0248 = localValue1;
         this.internalField0247 = localValue2;
         this.internalField0416 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0503[type=" + this.internalField0248 + ", displayName=" + this.internalField0247 + ", items=" + this.internalField0416 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         DonItemCatalog.InternalType0503 other = (DonItemCatalog.InternalType0503) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField0416, other.internalField0416);
      }

      public String internalMethod04164() {
         return this.internalField0248;
      }

      public String internalMethod00725() {
         return this.internalField0247;
      }

      public List<DonItemCatalog.InternalType0190> internalMethod07260() {
         return this.internalField0416;
      }
   }
}
