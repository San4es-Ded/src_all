package rockstar.client.data;



import rockstar.client.util.*;
import rockstar.client.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import it.unimi.dsi.fastutil.objects.Object2IntArrayMap;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import it.unimi.dsi.fastutil.objects.Object2IntMap.Entry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.PotionItem;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;

public final class AuctionItem {
   private static final List<AuctionItem.InternalType0159> internalField0416 = new ArrayList<>();
   private static final Map<String, Integer> internalField0543 = new HashMap<>();

   public static void internalMethod06356(ItemStack localValue0, long localValue1, String localValue3, String localValue4, AuctionItem.InternalType0158 localValue5, double localValue6) {
      internalMethod02533(localValue0, localValue1, localValue3, localValue4, localValue5, localValue6, false, 0L, 1, 10);
   }

   public static void internalMethod04841(ItemStack localValue0, long localValue1, String localValue3, String localValue4, AuctionItem.InternalType0158 localValue5, double localValue6, boolean localValue8) {
      internalMethod02533(localValue0, localValue1, localValue3, localValue4, localValue5, localValue6, localValue8, 0L, 1, 10);
   }

   public static void internalMethod02533(
      ItemStack localValue0, long localValue1, String localValue3, String localValue4, AuctionItem.InternalType0158 localValue5, double localValue6, boolean localValue8, long localValue9, int localValue11, int localValue12
   ) {
      internalField0416.removeIf(localValue1x -> localValue1x.internalMethod00891() != null && localValue1x.internalMethod00891().equals(localValue4));
      internalField0416.add(
         new AuctionItem.InternalType0159(localValue0.copy(), localValue1, localValue3, localValue4, localValue5, localValue6, localValue8, Math.max(0L, localValue9), Math.max(1, localValue11), Math.max(1, localValue12))
      );
   }

   public static List<AuctionItem.InternalType0159> internalMethod00895() {
      return List.copyOf(internalField0416);
   }

   public static void internalMethod07500() {
      internalField0416.clear();
   }

   public static void internalMethod04444(String localValue0) {
      internalField0416.removeIf(localValue1 -> localValue1.internalMethod00891() != null && localValue1.internalMethod00891().equals(localValue0));
      internalField0543.remove(localValue0);
   }

   public static void internalMethod04691(String localValue0, int localValue1) {
      if (localValue0 != null && localValue1 > 0) {
         internalField0543.merge(localValue0, localValue1, Integer::sum);
      }
   }

   public static void internalMethod06853(String localValue0, int localValue1) {
      if (localValue0 != null && localValue1 > 0) {
         internalField0543.merge(localValue0, -localValue1, Integer::sum);
         if (internalField0543.getOrDefault(localValue0, 0) <= 0) {
            internalField0543.remove(localValue0);
         }
      }
   }

   public static void internalMethod07503() {
      internalField0543.clear();
   }

   public static int internalMethod04443(String localValue0) {
      return localValue0 == null ? 0 : internalField0543.getOrDefault(localValue0, 0);
   }

   public static int internalMethod03650(AuctionItem.InternalType0159 localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.player == null) {
         return 0;
      } else {
         PlayerInventory localValue2 = localValue1.player.getInventory();
         int localValue3 = 0;

         for (int localValue4 = 0; localValue4 < localValue2.size(); localValue4++) {
            ItemStack localValue5 = localValue2.getStack(localValue4);
            if (internalMethod07258(localValue5, localValue0)) {
               localValue3 += localValue5.getCount();
            }
         }

         return localValue3;
      }
   }

   public static boolean internalMethod03651(AuctionItem.InternalType0159 localValue0) {
      return internalMethod04443(localValue0.internalMethod00891()) >= Math.max(1, localValue0.internalMethod00351());
   }

   public static String internalMethod06551(AuctionItem.InternalType0159 localValue0) {
      return localValue0.internalMethod04343() != null ? localValue0.internalMethod04343() : ItemNbtUtils.internalMethod06695(localValue0.internalMethod00723());
   }

   public static AuctionItem.InternalType0159 internalMethod04294(ItemStack localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         for (AuctionItem.InternalType0159 localValue2 : internalField0416) {
            if (internalMethod07258(localValue0, localValue2)) {
               return localValue2;
            }
         }

         return null;
      } else {
         return null;
      }
   }

   public static boolean internalMethod05703(AuctionTooltipParser.InternalType0247 localValue0, double localValue1) {
      ItemStack localValue3 = localValue0.internalMethod02415();
      if (localValue3 != null && !localValue3.isEmpty()) {
         Iterator localValue4 = internalField0416.iterator();

         while (true) {
            if (!localValue4.hasNext()) {
               return false;
            }

            AuctionItem.InternalType0159 localValue5 = (AuctionItem.InternalType0159)localValue4.next();
            if (internalMethod07258(localValue3, localValue5) && !internalMethod03651(localValue5)) {
               if (localValue5.internalMethod05344() == AuctionItem.InternalType0158.internalField0592) {
                  if (localValue0.internalMethod00226() > localValue5.internalMethod00349()) {
                     continue;
                  }
                  break;
               } else {
                  if (localValue5.internalMethod05344() != AuctionItem.InternalType0158.internalField0591) {
                     break;
                  }

                  if (!(localValue1 <= 0.0)) {
                     double localValue6 = localValue1 * (1.0 - localValue5.internalMethod00347() / 100.0);
                     if (localValue0.internalMethod00224() > localValue6) {
                        continue;
                     }
                     break;
                  }
               }
            }
         }

         return true;
      } else {
         return false;
      }
   }

   public static boolean internalMethod07258(ItemStack localValue0, AuctionItem.InternalType0159 localValue1) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         ItemStack localValue2 = localValue1.internalMethod00723();
         if (localValue2 == null || localValue2.isEmpty()) {
            return false;
         } else if (!localValue2.isOf(localValue0.getItem())) {
            return false;
         } else if (localValue2.getItem() instanceof PotionItem) {
            return PotionItemUtils.internalMethod06478(localValue2, localValue0);
         } else {
            Object2IntArrayMap localValue3 = new Object2IntArrayMap();
            EnchantmentUtils.internalMethod03498(localValue2, localValue3);
            boolean localValue4 = localValue1.internalMethod00350() || !localValue3.isEmpty();
            if (localValue4 && !internalMethod05751(localValue0, localValue3, localValue1.internalMethod00350())) {
               return false;
            } else if (DonItemResolver.internalMethod06709(localValue2)) {
               if (DonItemResolver.internalMethod05530(localValue2, localValue1.internalMethod04343(), localValue0)) {
                  return true;
               } else {
                  DonItemResolver.internalMethod03133(internalMethod06551(localValue1), localValue0);
                  return false;
               }
            } else {
               CustomItemUtils.InternalType0254 localValue5 = CustomItemUtils.internalMethod03238(localValue2);
               if (localValue5 == null && localValue4) {
                  return true;
               } else {
                  CustomItemUtils.InternalType0254 localValue6 = CustomItemUtils.internalMethod03238(localValue0);
                  return localValue5 != null && localValue6 != null && localValue5.internalMethod02143(localValue6) ? true : internalMethod03435(localValue0, localValue1);
               }
            }
         }
      } else {
         return false;
      }
   }

   private static boolean internalMethod03435(ItemStack localValue0, AuctionItem.InternalType0159 localValue1) {
      String localValue2 = ItemNbtUtils.internalMethod02181(localValue0);
      return localValue1.internalMethod04343() != null
         ? localValue2.contains(localValue1.internalMethod04343())
         : localValue2.contains(ItemNbtUtils.internalMethod02181(localValue1.internalMethod00723()))
            || localValue2.contains(ItemNbtUtils.internalMethod06695(localValue1.internalMethod00723()));
   }

   private static boolean internalMethod05751(ItemStack localValue0, Object2IntMap<RegistryEntry<Enchantment>> localValue1, boolean localValue2) {
      Object2IntArrayMap localValue3 = new Object2IntArrayMap();
      EnchantmentUtils.internalMethod03498(localValue0, localValue3);
      if (localValue2 && localValue1.size() != localValue3.size()) {
         return false;
      } else {
         ObjectIterator localValue4 = localValue1.object2IntEntrySet().iterator();

         while (localValue4.hasNext()) {
            Entry localValue5 = (Entry)localValue4.next();
            int localValue6 = internalMethod01580(localValue3, (RegistryEntry<Enchantment>)localValue5.getKey());
            if (localValue2 ? localValue6 != localValue5.getIntValue() : localValue6 < localValue5.getIntValue()) {
               return false;
            }
         }

         return true;
      }
   }

   private static int internalMethod01580(Object2IntMap<RegistryEntry<Enchantment>> localValue0, RegistryEntry<Enchantment> localValue1) {
      ObjectIterator localValue2 = localValue0.object2IntEntrySet().iterator();

      while (localValue2.hasNext()) {
         Entry localValue3 = (Entry)localValue2.next();
         if (internalMethod05099((RegistryEntry<Enchantment>)localValue3.getKey(), localValue1)) {
            return localValue3.getIntValue();
         }
      }

      return 0;
   }

   private static boolean internalMethod05099(RegistryEntry<Enchantment> localValue0, RegistryEntry<Enchantment> localValue1) {
      if (localValue0.equals(localValue1)) {
         return true;
      } else {
         Optional localValue2 = localValue0.getKey();
         Optional localValue3 = localValue1.getKey();
         return localValue2.isPresent() && localValue3.isPresent() && ((RegistryKey)localValue2.get()).equals(localValue3.get());
      }
   }

   @Generated
   private AuctionItem() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static enum InternalType0158 {
      internalField0592,
      internalField0591;
   }

   public static final class InternalType0159 {
      private final ItemStack internalField0878;
      private final long internalField0229;
      private final String internalField0248;
      private final String internalField0247;
      private final AuctionItem.InternalType0158 internalField0592;
      private final double internalField0194;
      private final boolean internalField0277;
      private final long internalField0230;
      private final int internalField0227;
      private final int internalField0228;

      public InternalType0159(
         ItemStack localValue1,
         long localValue2,
         String localValue4,
         String localValue5,
         AuctionItem.InternalType0158 localValue6,
         double localValue7,
         boolean localValue9,
         long localValue10,
         int localValue12,
         int localValue13
      ) {
         this.internalField0878 = localValue1;
         this.internalField0229 = localValue2;
         this.internalField0248 = localValue4;
         this.internalField0247 = localValue5;
         this.internalField0592 = localValue6;
         this.internalField0194 = localValue7;
         this.internalField0277 = localValue9;
         this.internalField0230 = localValue10;
         this.internalField0227 = localValue12;
         this.internalField0228 = localValue13;
      }

      @Override
      public final String toString() {
         return "InternalType0159[stack=" + this.internalField0878 + ", maxPrice=" + this.internalField0229 + ", customName=" + this.internalField0248 + ", id=" + this.internalField0247 + ", mode=" + this.internalField0592 + ", percentage=" + this.internalField0194 + ", strictEnchants=" + this.internalField0277 + ", sellPrice=" + this.internalField0230 + ", sellQty=" + this.internalField0227 + ", resellThreshold=" + this.internalField0228 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0247);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0592);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0230);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionItem.InternalType0159 other = (AuctionItem.InternalType0159) localValue1;
         return java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0247, other.internalField0247)
            && java.util.Objects.equals(this.internalField0592, other.internalField0592)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0230, other.internalField0230)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228);
      }

      public ItemStack internalMethod00723() {
         return this.internalField0878;
      }

      public long internalMethod00349() {
         return this.internalField0229;
      }

      public String internalMethod04343() {
         return this.internalField0248;
      }

      public String internalMethod00891() {
         return this.internalField0247;
      }

      public AuctionItem.InternalType0158 internalMethod05344() {
         return this.internalField0592;
      }

      public double internalMethod00347() {
         return this.internalField0194;
      }

      public boolean internalMethod00350() {
         return this.internalField0277;
      }

      public long internalMethod00352() {
         return this.internalField0230;
      }

      public int internalMethod00348() {
         return this.internalField0227;
      }

      public int internalMethod00351() {
         return this.internalField0228;
      }
   }
}
