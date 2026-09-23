package rockstar.client.internal.ui;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.function.Predicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.Generated;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Item.TooltipContext;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.screen.slot.Slot;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

public final class AuctionTooltipParser implements MinecraftClientAccess {
   private static final Pattern internalField0293 = Pattern.compile("^[^\\p{L}\\d]{0,3}\\d[\\d.,\\u00A0 ]*[^\\p{L}\\d]{0,3}$");
   private static final long internalField0229 = 100L;
   private static final Pattern internalField0294 = Pattern.compile("\\d[\\d.,\\u00A0 ]*\\d|\\d");
   private static final Pattern internalField1112 = Pattern.compile(
      "(?:^|\\s)(?:\u043f\u043e\u0438\u0441\u043a|\u043f\u043e\u0438\u0441|\u043f\u043e\u0438|\u043f\u043e|\u043f)\\s*:", 66
   );
   private static final Pattern internalField1111 = Pattern.compile("(?i)\u00a7[0-9a-fk-or]");
   private static final Pattern internalField1113 = Pattern.compile("\\s+");

   public static AuctionTooltipParser.InternalType0248 internalMethod06312(HandledScreen<?> localValue0, boolean localValue1, Predicate<ItemStack> localValue2) {
      ArrayList localValue3 = new ArrayList();
      AuctionTooltipParser.InternalType0247 localValue4 = null;
      double localValue5 = 0.0;
      double localValue7 = Double.MAX_VALUE;

      for (int localValue9 = 0; localValue9 < localValue0.getScreenHandler().slots.size() - 36; localValue9++) {
         Slot localValue10 = localValue0.getScreenHandler().getSlot(localValue9);
         if (localValue10 != null && localValue10.hasStack()) {
            ItemStack localValue11 = localValue10.getStack();
            if (localValue2 == null || localValue2.test(localValue11)) {
               List localValue12 = localValue11.getTooltip(
                  TooltipContext.create(internalField0149.world),
                  internalField0149.player,
                  internalField0149.options.advancedItemTooltips ? TooltipType.ADVANCED : TooltipType.BASIC
               );
               long localValue13 = internalMethod02982(localValue12);
               if (localValue13 > 0L) {
                  int localValue15 = Math.max(1, localValue11.getCount());
                  int localValue16 = localValue11.getMaxDamage();
                  int localValue17 = localValue16 - localValue11.getDamage();
                  double localValue18 = localValue1 ? (double)localValue13 / localValue15 : localValue13;
                  double localValue20 = localValue18 / internalMethod00788(localValue16, localValue17);
                  AuctionTooltipParser.InternalType0247 localValue22 = new AuctionTooltipParser.InternalType0247(localValue10.id, localValue11, localValue13, localValue15, localValue16, localValue17, localValue20);
                  localValue3.add(localValue22);
                  localValue5 += localValue20;
                  if (localValue20 < localValue7) {
                     localValue7 = localValue20;
                     localValue4 = localValue22;
                  }
               }
            }
         }
      }

      double localValue23 = localValue3.isEmpty() ? 0.0 : localValue5 / localValue3.size();
      return new AuctionTooltipParser.InternalType0248(localValue3, localValue23, localValue7, localValue4);
   }

   public static AuctionTooltipParser.InternalType0425 internalMethod05605(HandledScreen<?> localValue0, Predicate<ItemStack> localValue1) {
      long localValue2 = -1L;
      long localValue4 = -1L;
      long localValue6 = -1L;
      int localValue8 = -1;
      ArrayList localValue9 = new ArrayList();

      for (int localValue10 = 0; localValue10 < localValue0.getScreenHandler().slots.size() - 36; localValue10++) {
         Slot localValue11 = localValue0.getScreenHandler().getSlot(localValue10);
         if (localValue11 != null && localValue11.hasStack()) {
            ItemStack localValue12 = localValue11.getStack();
            if (localValue1 == null || localValue1.test(localValue12)) {
               List localValue13 = localValue12.getTooltip(
                  TooltipContext.create(internalField0149.world),
                  internalField0149.player,
                  internalField0149.options.advancedItemTooltips ? TooltipType.ADVANCED : TooltipType.BASIC
               );
               long localValue14 = -1L;
               long localValue16 = -1L;
               long localValue18 = -1L;

               for (Text localValue21 : (Iterable<Text>)(Iterable<?>)localValue13) {
                  String localValue22 = localValue21.getString();
                  String localValue23 = localValue22.toLowerCase(Locale.ROOT);
                  if (localValue23.contains("\u0431\u0438\u0440\u0436\u0430 \u0431\u0430\u043b\u0430\u043d\u0441")) {
                     localValue16 = internalMethod00706(localValue22);
                  }

                  if (localValue23.contains("\u043c\u043e\u043d\u0435\u0442")) {
                     localValue18 = internalMethod00706(localValue22);
                  }

                  if (localValue23.contains("\u043a\u0443\u0440\u0441")) {
                     localValue14 = internalMethod00706(localValue22);
                  }
               }

               if (localValue16 != -1L) {
                  localValue2 = localValue16;
               }

               if (localValue18 != -1L) {
                  localValue4 = localValue18;
               }

               if (localValue14 > 0L) {
                  localValue9.add(new AuctionTooltipParser.InternalType0426(localValue11.id, localValue14));
                  if (localValue14 > localValue6) {
                     localValue6 = localValue14;
                     localValue8 = localValue11.id;
                  }
               }
            }
         }
      }

      return new AuctionTooltipParser.InternalType0425(localValue2, localValue4, localValue6, localValue8, localValue9);
   }

   public static long internalMethod02982(List<Text> localValue0) {
      long localValue1 = -1L;

      for (Text localValue4 : localValue0) {
         String localValue5 = internalMethod04387(localValue4.getString());
         if (!localValue5.isEmpty() && !localValue5.contains("%")) {
            if (internalMethod01028(localValue5.toLowerCase(Locale.ROOT), localValue5)) {
               long localValue6 = internalMethod00706(localValue5);
               if (localValue6 > 0L) {
                  return localValue6;
               }
            } else if (internalField0293.matcher(localValue5).matches()) {
               long localValue8 = internalMethod00706(localValue5);
               if (localValue8 >= 100L) {
                  localValue1 = Math.max(localValue1, localValue8);
               }
            }
         }
      }

      return localValue1;
   }

   private static boolean internalMethod01028(String localValue0, String localValue1) {
      boolean localValue2 = localValue0.contains("\u0446\u0435\u043d\u0430")
         || localValue0.contains("\u0446e\u043d\u0430")
         || localValue0.contains("\u0446\u0435\u043da")
         || localValue0.contains("\u0446e\u043da")
         || localValue0.contains("$")
         || localValue0.contains("price")
         || localValue0.contains("\u0441\u0442\u043e\u0438\u043c");
      return localValue2 && !localValue1.contains("%");
   }

   public static long internalMethod02205(String localValue0) {
      return internalMethod00706(internalMethod04387(localValue0));
   }

   private static long internalMethod00706(String localValue0) {
      Matcher localValue1 = internalField0294.matcher(localValue0);
      long localValue2 = -1L;

      while (localValue1.find()) {
         String localValue4 = localValue1.group().replaceAll("[^\\d]", "");
         if (!localValue4.isEmpty()) {
            try {
               localValue2 = Math.max(localValue2, Long.parseLong(localValue4));
            } catch (NumberFormatException localValue6) {
            }
         }
      }

      return localValue2;
   }

   private static double internalMethod00788(int localValue0, int localValue1) {
      return localValue0 <= 0 ? 1.0 : Math.max(0.1, (double)localValue1 / localValue0);
   }

   public static String internalMethod03300(String localValue0) {
      return internalMethod04387(localValue0).toLowerCase(Locale.ROOT);
   }

   public static String internalMethod04387(String localValue0) {
      if (localValue0 != null && !localValue0.isEmpty()) {
         String localValue1 = internalField1111.matcher(localValue0).replaceAll("");
         StringBuilder localValue2 = new StringBuilder(localValue1.length());
         localValue1.codePoints().forEach(localValue1x -> {
            if (!internalMethod00158(localValue1x)) {
               localValue2.appendCodePoint(localValue1x);
            }
         });
         return internalField1113.matcher(localValue2).replaceAll(" ").trim();
      } else {
         return "";
      }
   }

   private static boolean internalMethod00158(int localValue0) {
      int localValue1 = Character.getType(localValue0);
      return localValue1 == 18 || localValue1 == 15 || localValue1 == 16 || localValue1 == 19 || localValue1 == 0;
   }

   public static boolean internalMethod02206(@NotNull String localValue0) {
      return localValue0.contains("\u0430\u0443\u043a\u0446\u0438\u043e\u043d")
         || localValue0.contains("\u043f\u043e\u0438\u0441\u043a")
         || internalMethod00707(localValue0)
         || internalField1112.matcher(localValue0).find();
   }

   public static boolean internalMethod00707(@NotNull String localValue0) {
      return localValue0.contains("\u0434\u043e\u043d\u043c\u0430\u0440\u043a\u0435\u0442")
         || localValue0.contains("\u0434\u043e\u043d \u043c\u0430\u0440\u043a\u0435\u0442");
   }

   public static boolean internalMethod08274(@NotNull String localValue0) {
      String localValue1 = internalMethod03300(localValue0);
      return internalMethod02206(localValue1) || AuctionSearchTracker.internalMethod06217(localValue1);
   }

   public static boolean internalMethod07078(HandledScreen<?> localValue0) {
      return localValue0 != null && AuctionSearchTracker.internalMethod01007(localValue0);
   }

   public static boolean internalMethod07991(@NotNull String localValue0) {
      String localValue1 = internalMethod03300(localValue0);
      return internalMethod00707(localValue1) ? true : AuctionSearchTracker.internalMethod01248();
   }

   @Generated
   private AuctionTooltipParser() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   public static final class InternalType0247 {
      private final int internalField0227;
      private final ItemStack internalField0878;
      private final long internalField0229;
      private final int internalField0228;
      private final int internalField1053;
      private final int internalField1055;
      private final double internalField0194;

      public InternalType0247(int localValue1, ItemStack localValue2, long localValue3, int localValue5, int localValue6, int localValue7, double localValue8) {
         this.internalField0227 = localValue1;
         this.internalField0878 = localValue2;
         this.internalField0229 = localValue3;
         this.internalField0228 = localValue5;
         this.internalField1053 = localValue6;
         this.internalField1055 = localValue7;
         this.internalField0194 = localValue8;
      }

      @Override
      public final String toString() {
         return "InternalType0247[slot=" + this.internalField0227 + ", stack=" + this.internalField0878 + ", price=" + this.internalField0229 + ", count=" + this.internalField0228 + ", maxDurability=" + this.internalField1053 + ", curDurability=" + this.internalField1055 + ", effPrice=" + this.internalField0194 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0878);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionTooltipParser.InternalType0247 other = (AuctionTooltipParser.InternalType0247) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0878, other.internalField0878)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053)
            && java.util.Objects.equals(this.internalField1055, other.internalField1055)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194);
      }

      public int internalMethod00225() {
         return this.internalField0227;
      }

      public ItemStack internalMethod02415() {
         return this.internalField0878;
      }

      public long internalMethod00226() {
         return this.internalField0229;
      }

      public int internalMethod00227() {
         return this.internalField0228;
      }

      public int internalMethod08632() {
         return this.internalField1053;
      }

      public int internalMethod08634() {
         return this.internalField1055;
      }

      public double internalMethod00224() {
         return this.internalField0194;
      }
   }

   public static final class InternalType0248 {
      private final List<AuctionTooltipParser.InternalType0247> internalField0416;
      private final double internalField0194;
      private final double internalField0193;
      private final AuctionTooltipParser.InternalType0247 internalField0052;

      public InternalType0248(List<AuctionTooltipParser.InternalType0247> localValue1, double localValue2, double localValue4, AuctionTooltipParser.InternalType0247 localValue6) {
         this.internalField0416 = localValue1;
         this.internalField0194 = localValue2;
         this.internalField0193 = localValue4;
         this.internalField0052 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0248[items=" + this.internalField0416 + ", avgEffPrice=" + this.internalField0194 + ", minEffPrice=" + this.internalField0193 + ", cheapest=" + this.internalField0052 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0193);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0052);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionTooltipParser.InternalType0248 other = (AuctionTooltipParser.InternalType0248) localValue1;
         return java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0193, other.internalField0193)
            && java.util.Objects.equals(this.internalField0052, other.internalField0052);
      }

      public List<AuctionTooltipParser.InternalType0247> internalMethod05468() {
         return this.internalField0416;
      }

      public double internalMethod01990() {
         return this.internalField0194;
      }

      public double internalMethod01991() {
         return this.internalField0193;
      }

      public AuctionTooltipParser.InternalType0247 internalMethod04917() {
         return this.internalField0052;
      }
   }

   public static final class InternalType0425 {
      private final long internalField0229;
      private final long internalField0230;
      private final long internalField1059;
      private final int internalField0227;
      private final List<AuctionTooltipParser.InternalType0426> internalField0416;

      public InternalType0425(long localValue1, long localValue3, long localValue5, int localValue7, List<AuctionTooltipParser.InternalType0426> localValue8) {
         this.internalField0229 = localValue1;
         this.internalField0230 = localValue3;
         this.internalField1059 = localValue5;
         this.internalField0227 = localValue7;
         this.internalField0416 = localValue8;
      }

      @Override
      public final String toString() {
         return "InternalType0425[exchangeBalance=" + this.internalField0229 + ", balance=" + this.internalField0230 + ", bestRate=" + this.internalField1059 + ", bestSlot=" + this.internalField0227 + ", allOffers=" + this.internalField0416 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0230);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1059);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionTooltipParser.InternalType0425 other = (AuctionTooltipParser.InternalType0425) localValue1;
         return java.util.Objects.equals(this.internalField0229, other.internalField0229)
            && java.util.Objects.equals(this.internalField0230, other.internalField0230)
            && java.util.Objects.equals(this.internalField1059, other.internalField1059)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0416, other.internalField0416);
      }

      public long internalMethod06316() {
         return this.internalField0229;
      }

      public long internalMethod06317() {
         return this.internalField0230;
      }

      public long internalMethod09034() {
         return this.internalField1059;
      }

      public int internalMethod06315() {
         return this.internalField0227;
      }

      public List<AuctionTooltipParser.InternalType0426> internalMethod01860() {
         return this.internalField0416;
      }
   }

   public static final class InternalType0426 {
      private final int internalField0227;
      private final long internalField0229;

      public InternalType0426(int localValue1, long localValue2) {
         this.internalField0227 = localValue1;
         this.internalField0229 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0426[slot=" + this.internalField0227 + ", rate=" + this.internalField0229 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0229);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AuctionTooltipParser.InternalType0426 other = (AuctionTooltipParser.InternalType0426) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0229, other.internalField0229);
      }

      public int internalMethod05934() {
         return this.internalField0227;
      }

      public long internalMethod05935() {
         return this.internalField0229;
      }
   }
}
