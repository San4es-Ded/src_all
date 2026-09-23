package rockstar.client.internal.inventory;




import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.Locale;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

public class AutoJoinGriefTask implements BotBehavior {
   private final int internalField0227;
   private final long internalField0229;
   private final long internalField0230;
   private long internalField1059;
   private long internalField1058;
   private long internalField1060;
   private long internalField1057;
   private boolean internalField0277;

   public AutoJoinGriefTask(int localValue1, int localValue2) {
      this(localValue1, localValue2, 0L);
   }

   public AutoJoinGriefTask(int localValue1, int localValue2, long localValue3) {
      this.internalField0227 = localValue1;
      this.internalField0229 = Math.max(1, localValue2) * 1000L;
      this.internalField0230 = Math.max(0L, localValue3);
   }

   @Override
   public void internalMethod07229(BotTargetManager localValue1) {
      if (localValue1 != null && localValue1.internalMethod07697()) {
         long localValue2 = System.currentTimeMillis();
         if (!this.internalField0277) {
            this.internalField0277 = true;
            this.internalField1059 = localValue2;
            this.internalField1058 = localValue2 - this.internalField0229 + this.internalField0230 + this.internalMethod04113(localValue1);
         } else if (localValue2 - this.internalField1059 >= this.internalField0230) {
            if (localValue1.internalMethod06688().internalMethod02812()) {
               if (localValue2 - this.internalField1060 >= localValue1.internalMethod06687().internalMethod09861()) {
                  this.internalMethod03007(localValue1, localValue2);
                  this.internalField1060 = localValue2;
               }
            } else if (localValue2 - this.internalField1058 >= this.internalField0229) {
               if (localValue1.internalMethod05748(Items.COMPASS)) {
                  localValue1.internalMethod09725();
               } else {
                  localValue1.internalMethod04382(localValue1.internalMethod06687().internalMethod04545() + this.internalField0227);
               }

               this.internalField1058 = localValue2;
            }
         }
      }
   }

   private void internalMethod03007(BotTargetManager localValue1, long localValue2) {
      if (localValue2 - this.internalField1057 >= localValue1.internalMethod06687().internalMethod09845()) {
         AutoJoinGriefTask.InternalType0464 localValue4 = this.internalMethod00648(localValue1);
         int localValue5 = this.internalMethod02670(localValue4);
         if (localValue5 >= 0) {
            if (localValue1.internalMethod00257(localValue5)) {
               this.internalField1057 = localValue2;
               if (localValue5 == localValue4.internalMethod08340()) {
                  localValue1.internalMethod05456(new VelocityDecayBehavior());
               }
            }
         }
      }
   }

   private int internalMethod02670(AutoJoinGriefTask.InternalType0464 localValue1) {
      if (localValue1.internalMethod02555() && localValue1.internalMethod02551() >= 0 && localValue1.internalMethod02554() < 0) {
         return localValue1.internalMethod02551();
      } else if (localValue1.internalMethod02554() >= 0) {
         return localValue1.internalMethod02554();
      } else {
         boolean localValue2 = this.internalField0227 > 36;
         if (localValue2 && !localValue1.internalMethod02552()) {
            return localValue1.internalMethod08329();
         } else {
            return !localValue2 && localValue1.internalMethod02552() ? localValue1.internalMethod08333() : localValue1.internalMethod08340();
         }
      }
   }

   private AutoJoinGriefTask.InternalType0464 internalMethod00648(BotTargetManager localValue1) {
      boolean localValue2 = false;
      boolean localValue3 = false;
      int localValue4 = -1;
      int localValue5 = -1;
      int localValue6 = -1;
      int localValue7 = -1;
      int localValue8 = -1;
      ItemStack[] localValue9 = localValue1.internalMethod06688().internalMethod07683();

      for (int localValue10 = 0; localValue10 < localValue9.length; localValue10++) {
         ItemStack localValue11 = localValue9[localValue10];
         if (localValue11 != null && !localValue11.isEmpty()) {
            String localValue12 = this.internalMethod00303(localValue11.getName().getString());
            if (!localValue12.isBlank()) {
               if (localValue12.contains("\u0433\u0440\u0438\u0444\u0435\u0440\u0441\u043a\u043e\u0435 \u0432\u044b\u0436\u0438\u0432\u0430\u043d\u0438\u0435")
                  || localValue12.contains("grief survival")) {
                  localValue5 = this.internalMethod03018(localValue5, localValue10);
               }

               if (localValue12.contains("\u043f\u043e\u0434\u0441\u043a\u0430\u0437") || localValue12.contains("hint")) {
                  localValue3 = true;
               }

               if (this.internalMethod06054(localValue11, localValue12)) {
                  localValue4 = this.internalMethod03018(localValue4, localValue10);
               }

               if (this.internalMethod05227(localValue12)) {
                  localValue2 = true;
                  localValue7 = this.internalMethod03018(localValue7, localValue10);
               }

               if (this.internalMethod00090(localValue11, localValue12)) {
                  localValue6 = this.internalMethod03018(localValue6, localValue10);
               }

               if (localValue8 < 0 && this.internalMethod06678(localValue12)) {
                  localValue8 = localValue10;
               }
            }
         }
      }

      return new AutoJoinGriefTask.InternalType0464(localValue2, localValue3, localValue4, localValue5, localValue6, localValue7, localValue8);
   }

   private boolean internalMethod06678(String localValue1) {
      String localValue2 = String.valueOf(this.internalField0227);
      return localValue1.contains("\u0433\u0440\u0438\u0444 #" + localValue2)
         || localValue1.contains("\u0433\u0440\u0438\u0444 \u2116" + localValue2)
         || localValue1.contains("\u0433\u0440\u0438\u0444 " + localValue2)
         || localValue1.contains("grief #" + localValue2)
         || localValue1.contains("grief " + localValue2);
   }

   private boolean internalMethod00090(ItemStack localValue1, String localValue2) {
      return localValue2.contains("\u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0430\u044f")
         || localValue2.contains("next")
         || localValue1.getItem() == Items.ARROW
            && !localValue2.contains("\u043f\u0440\u0435\u0434\u044b\u0434\u0443\u0449")
            && !localValue2.contains("\u043d\u0430\u0437\u0430\u0434")
            && !localValue2.contains("back");
   }

   private boolean internalMethod05227(String localValue1) {
      return localValue1.contains("\u043f\u0440\u0435\u0434\u044b\u0434\u0443\u0449") || localValue1.contains("previous");
   }

   private boolean internalMethod06054(ItemStack localValue1, String localValue2) {
      return localValue1.getItem() != Items.ARROW && localValue1.getItem() != Items.BARRIER
         ? false
         : localValue2.contains("\u043d\u0430\u0437\u0430\u0434")
            || localValue2.contains("back")
            || localValue2.contains("\u043c\u0435\u043d\u044e")
            || localValue2.contains("\u0432\u044b\u0445\u043e\u0434")
            || localValue2.contains("\u0437\u0430\u043a\u0440\u044b\u0442\u044c");
   }

   private int internalMethod03018(int localValue1, int localValue2) {
      return localValue1 < 0 ? localValue2 : localValue1;
   }

   private String internalMethod00303(String localValue1) {
      return localValue1 == null ? "" : localValue1.replaceAll("\u00a7.", "").toLowerCase(Locale.ROOT);
   }

   private long internalMethod04113(BotTargetManager localValue1) {
      String localValue2 = localValue1.internalMethod03426();
      return localValue2 == null ? 0L : Math.floorMod((long)localValue2.hashCode(), Math.max(1L, this.internalField0229));
   }

   @Override
   public String internalMethod06553() {
      return "AutoJoinGrief " + this.internalField0227;
   }

   static final class InternalType0464 {
      private final boolean internalField0277;
      private final boolean internalField0276;
      private final int internalField0227;
      private final int internalField0228;
      private final int internalField1053;
      private final int internalField1055;
      private final int internalField1056;

      InternalType0464(boolean localValue1, boolean localValue2, int localValue3, int localValue4, int localValue5, int localValue6, int localValue7) {
         this.internalField0277 = localValue1;
         this.internalField0276 = localValue2;
         this.internalField0227 = localValue3;
         this.internalField0228 = localValue4;
         this.internalField1053 = localValue5;
         this.internalField1055 = localValue6;
         this.internalField1056 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0464[onSecondPage=" + this.internalField0277 + ", hasHints=" + this.internalField0276 + ", backSlot=" + this.internalField0227 + ", mainMenuSlot=" + this.internalField0228 + ", nextPageSlot=" + this.internalField1053 + ", previousPageSlot=" + this.internalField1055 + ", targetGriefSlot=" + this.internalField1056 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0276);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1056);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AutoJoinGriefTask.InternalType0464 other = (AutoJoinGriefTask.InternalType0464) localValue1;
         return java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0276, other.internalField0276)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053)
            && java.util.Objects.equals(this.internalField1055, other.internalField1055)
            && java.util.Objects.equals(this.internalField1056, other.internalField1056);
      }

      public boolean internalMethod02552() {
         return this.internalField0277;
      }

      public boolean internalMethod02555() {
         return this.internalField0276;
      }

      public int internalMethod02551() {
         return this.internalField0227;
      }

      public int internalMethod02554() {
         return this.internalField0228;
      }

      public int internalMethod08329() {
         return this.internalField1053;
      }

      public int internalMethod08333() {
         return this.internalField1055;
      }

      public int internalMethod08340() {
         return this.internalField1056;
      }
   }
}
