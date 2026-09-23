package rockstar.client.internal.script;




import rockstar.client.setting.*;
import rockstar.client.server.*;
import rockstar.client.*;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.StreamSupport;
import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.util.math.Vec3d;
import pyrock.utility.render.ColorRGBA;

public class MineshaftTimerHud extends DynamicIslandLabelStatus implements MinecraftClientAccess {
   private final Pattern internalField0293 = Pattern.compile("(\\d{1,2}):(\\d{2})");
   private MineshaftTimerHud.InternalType0025 internalField0187;
   private long internalField0229;
   private Vec3d internalField0283;
   private MineshaftTimerHud.InternalType0025 internalField0188;
   private boolean internalField0277;

   public MineshaftTimerHud(MultiSelectSetting localValue1) {
      super(localValue1, "mine");
   }

   @Override
   public final void prepare(DynamicIslandHud localValue1) {
      if (!this.internalMethod07459()) {
         this.internalField0277 = false;
         super.prepare(localValue1);
      } else {
         MineshaftTimerHud.InternalType0025 localValue2 = this.internalMethod04885();
         if (localValue2 == null) {
            localValue2 = this.internalMethod00157(this.internalMethod06785(ServerUtils.internalMethod01786(KnownServer.internalField1218)));
         }

         if (localValue2 == null) {
            super.prepare(localValue1);
         } else {
            this.internalMethod01781(
               localValue2.internalMethod00989() + ":",
               "",
               localValue2.internalMethod08381(),
               localValue2.internalMethod04307().internalMethod05312(),
               localValue2.internalMethod04307().internalMethod00319()
            );
            super.prepare(localValue1);
         }
      }
   }

   @Override
   public final boolean canShow() {
      this.internalField0277 = false;
      if (!this.internalMethod07459()) {
         return false;
      } else {
         MineshaftTimerHud.InternalType0025 localValue1 = this.internalMethod00157(
            this.internalMethod06785(ServerUtils.internalMethod01786(KnownServer.internalField1218))
         );
         this.internalField0188 = localValue1;
         this.internalField0277 = true;
         return localValue1 != null;
      }
   }

   private MineshaftTimerHud.InternalType0025 internalMethod04885() {
      if (!this.internalField0277) {
         return null;
      } else {
         this.internalField0277 = false;
         return this.internalField0188;
      }
   }

   private boolean internalMethod07459() {
      return internalField0149.world != null && internalField0149.player != null;
   }

   private MineshaftTimerHud.InternalType0025 internalMethod00157(MineshaftTimerHud.InternalType0099 localValue1) {
      long localValue2 = System.currentTimeMillis();
      if (localValue1 != null && !localValue1.internalField0416.isEmpty()) {
         MineshaftTimerHud.InternalType0025 localValue4 = this.internalMethod03100(localValue1.internalField0416);
         if (localValue4 != null) {
            this.internalField0187 = localValue4;
            this.internalField0229 = localValue2;
            this.internalField0283 = localValue1.internalField0283;
            return localValue4;
         }
      }

      if (!this.internalMethod07462()) {
         return null;
      } else if (this.internalField0283 != null && this.internalMethod02502(this.internalField0283)) {
         int localValue6 = (int)((localValue2 - this.internalField0229) / 1000L);
         int localValue5 = this.internalField0187.internalMethod00987() - localValue6;
         if (localValue5 <= 0) {
            this.internalField0187 = null;
            this.internalField0283 = null;
            return null;
         } else {
            return new MineshaftTimerHud.InternalType0025(localValue5 / 60, localValue5 % 60, this.internalField0187.internalMethod04307());
         }
      } else {
         return null;
      }
   }

   private MineshaftTimerHud.InternalType0099 internalMethod06785(boolean localValue1) {
      List localValue2 = this.internalMethod01285();
      if (localValue2.isEmpty()) {
         return null;
      } else {
         ArmorStandEntity localValue3 = this.internalMethod03816(localValue1, localValue2);
         if (localValue3 == null) {
            return null;
         } else {
            Vec3d localValue4 = localValue3.getEntityPos();
            List localValue5 = localValue2.stream()
               .filter(localValue2x -> this.internalMethod06076(((net.minecraft.entity.Entity)localValue2x).getEntityPos(), localValue4) <= 64.0)
               .sorted(Comparator.comparingDouble(localValue0 -> -((net.minecraft.entity.Entity)localValue0).getEntityPos().getY()))
               .toList();
            return new MineshaftTimerHud.InternalType0099(localValue4, localValue5);
         }
      }
   }

   private ArmorStandEntity internalMethod03816(boolean localValue1, List<ArmorStandEntity> localValue2) {
      if (localValue2.isEmpty()) {
         return null;
      } else {
         List localValue3 = localValue1
            ? List.of(
               "\u0448\u0430\u0445\u0442\u0430",
               "\u043e\u0441\u0442\u0430\u043b\u043e\u0441\u044c",
               "\u043e\u0431\u044b\u0447\u043d\u0430\u044f",
               "\u0440\u0435\u0434\u043a\u0430\u044f",
               "\u044d\u043f\u0438\u0447\u0435\u0441\u043a\u0430\u044f",
               "\u043b\u0435\u0433\u0435\u043d\u0434\u0430\u0440\u043d\u0430\u044f",
               "\u043c\u0438\u0444\u0438\u0447\u0435\u0441\u043a\u0430\u044f"
            )
            : List.of(
               "\u0430\u0432\u0442\u043e-\u0448\u0430\u0445\u0442\u0430",
               "\u0448\u0430\u0445\u0442\u0430",
               "\u0441\u043b\u0435\u0434\u0443\u044e\u0449\u0430\u044f",
               "\u043e\u0431\u043d\u043e\u0432\u043b\u0435\u043d\u0438\u0435",
               "\u043e\u0441\u0442\u0430\u043b\u043e\u0441\u044c"
            );
         return localValue2.stream().filter(localValue2x -> {
            String localValue3x = localValue2x.getCustomName().getString().toLowerCase(Locale.ROOT);
            return this.internalMethod04781(localValue3x) ? false : localValue3.stream().anyMatch(value -> localValue3x.contains((CharSequence)value));
         }).min(Comparator.comparingDouble(this::internalMethod07374)).orElse(null);
      }
   }

   private List<ArmorStandEntity> internalMethod01285() {
      return StreamSupport.<Entity>stream(internalField0149.world.getEntities().spliterator(), false)
         .filter(localValue0 -> localValue0 instanceof ArmorStandEntity)
         .map(localValue0 -> (ArmorStandEntity)localValue0)
         .filter(localValue0 -> localValue0.isAlive() && localValue0.getCustomName() != null)
         .filter(localValue1 -> this.internalMethod02502(localValue1.getEntityPos()))
         .toList();
   }

   private MineshaftTimerHud.InternalType0025 internalMethod03100(List<ArmorStandEntity> localValue1) {
      String localValue2 = null;
      MineshaftTimerHud.InternalType0100 localValue3 = MineshaftTimerHud.InternalType0100.internalField0333;

      for (int localValue4 = 0; localValue4 < localValue1.size(); localValue4++) {
         String localValue5 = ((ArmorStandEntity)localValue1.get(localValue4)).getCustomName().getString().toLowerCase(Locale.ROOT);
         if (!this.internalMethod04781(localValue5) && localValue5.contains("\u0442\u0435\u043a\u0443\u0449")) {
            MineshaftTimerHud.InternalType0100 localValue6 = MineshaftTimerHud.InternalType0100.internalMethod02425(localValue5);
            if (localValue6 != null) {
               localValue3 = localValue6;
               break;
            }

            if (localValue4 + 1 < localValue1.size()) {
               MineshaftTimerHud.InternalType0100 localValue7 = MineshaftTimerHud.InternalType0100.internalMethod02425(
                  ((ArmorStandEntity)localValue1.get(localValue4 + 1)).getCustomName().getString()
               );
               if (localValue7 != null) {
                  localValue3 = localValue7;
                  break;
               }
            }
         }
      }

      if (localValue3 == MineshaftTimerHud.InternalType0100.internalField0333) {
         for (ArmorStandEntity localValue13 : localValue1) {
            String localValue15 = localValue13.getCustomName().getString().toLowerCase(Locale.ROOT);
            MineshaftTimerHud.InternalType0100 localValue17 = MineshaftTimerHud.InternalType0100.internalMethod02425(localValue15);
            if (localValue17 != null) {
               localValue3 = localValue17;
               break;
            }
         }
      }

      for (ArmorStandEntity localValue14 : localValue1) {
         String localValue16 = localValue14.getCustomName().getString().toLowerCase(Locale.ROOT);
         Matcher localValue18 = this.internalField0293.matcher(localValue16);
         if (localValue18.find()) {
            localValue2 = localValue18.group();
            break;
         }

         if (localValue16.contains("\u043e\u0441\u0442\u0430\u043b\u043e\u0441\u044c")) {
            int localValue8 = this.internalMethod03836(localValue16, "", "\u043c\u0438\u043d.");
            int localValue9 = this.internalMethod03836(localValue16, "\u043c\u0438\u043d.", "\u0441\u0435\u043a.");
            if (localValue8 >= 0 && localValue9 >= 0) {
               localValue2 = String.format("%d:%02d", localValue8, localValue9);
               break;
            }
         }
      }

      if (localValue2 == null) {
         return null;
      } else {
         String[] localValue12 = localValue2.split(":");
         return new MineshaftTimerHud.InternalType0025(Integer.parseInt(localValue12[0]), Integer.parseInt(localValue12[1]), localValue3);
      }
   }

   private boolean internalMethod04781(String localValue1) {
      return localValue1.contains("\u044d\u043d\u0434\u0430")
         || localValue1.contains("\u0430\u0434\u0430")
         || localValue1.contains("\u044d\u043d\u0434")
         || localValue1.contains("\u0430\u0434");
   }

   private int internalMethod03836(String localValue1, String localValue2, String localValue3) {
      try {
         int localValue4 = localValue2.isEmpty() ? 0 : localValue1.indexOf(localValue2) + localValue2.length();
         int localValue5 = localValue1.indexOf(localValue3, localValue4);
         if (localValue4 >= 0 && localValue5 > localValue4) {
            String localValue6 = localValue1.substring(localValue4, localValue5).replaceAll("[^0-9]", "");
            return localValue6.isEmpty() ? -1 : Integer.parseInt(localValue6);
         }
      } catch (Exception localValue7) {
      }

      return -1;
   }

   private boolean internalMethod07462() {
      if (this.internalField0187 == null) {
         return false;
      } else {
         int localValue1 = (int)((System.currentTimeMillis() - this.internalField0229) / 1000L);
         return this.internalField0187.internalMethod00987() - localValue1 > 0;
      }
   }

   private boolean internalMethod02502(Vec3d localValue1) {
      return this.internalMethod06076(localValue1, internalField0149.player.getEntityPos()) <= 900.0;
   }

   private double internalMethod07374(ArmorStandEntity localValue1) {
      return this.internalMethod06076(localValue1.getEntityPos(), internalField0149.player.getEntityPos());
   }

   private double internalMethod06076(Vec3d localValue1, Vec3d localValue2) {
      double localValue3 = localValue1.x - localValue2.x;
      double localValue5 = localValue1.z - localValue2.z;
      return localValue3 * localValue3 + localValue5 * localValue5;
   }

   static final class InternalType0025 {
      private final int internalField0227;
      private final int internalField0228;
      private final MineshaftTimerHud.InternalType0100 internalField0333;

      InternalType0025(int localValue1, int localValue2, MineshaftTimerHud.InternalType0100 localValue3) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
         this.internalField0333 = localValue3;
      }

      public int internalMethod00987() {
         return this.internalField0227 * 60 + this.internalField0228;
      }

      @Override
      public final String toString() {
         return "InternalType0025[minutes=" + this.internalField0227 + ", seconds=" + this.internalField0228 + ", type=" + this.internalField0333 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0333);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MineshaftTimerHud.InternalType0025 other = (MineshaftTimerHud.InternalType0025) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField0333, other.internalField0333);
      }

      public int internalMethod00989() {
         return this.internalField0227;
      }

      public int internalMethod08381() {
         return this.internalField0228;
      }

      public MineshaftTimerHud.InternalType0100 internalMethod04307() {
         return this.internalField0333;
      }
   }

   static final class InternalType0099 {
      final Vec3d internalField0283;
      final List<ArmorStandEntity> internalField0416;

      InternalType0099(Vec3d localValue1, List<ArmorStandEntity> localValue2) {
         this.internalField0283 = localValue1;
         this.internalField0416 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0099[anchorPos=" + this.internalField0283 + ", stands=" + this.internalField0416 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MineshaftTimerHud.InternalType0099 other = (MineshaftTimerHud.InternalType0099) localValue1;
         return java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0416, other.internalField0416);
      }

      public Vec3d internalMethod05905() {
         return this.internalField0283;
      }

      public List<ArmorStandEntity> internalMethod05598() {
         return this.internalField0416;
      }
   }

   static enum InternalType0100 {
      internalField0333("\u041e\u0431\u044b\u0447\u043d\u0430\u044f", new ColorRGBA(243.0F, 151.0F, 250.0F)),
      internalField0332("\u0420\u0435\u0434\u043a\u0430\u044f", new ColorRGBA(243.0F, 151.0F, 250.0F)),
      internalField1127("\u042d\u043f\u0438\u0447\u0435\u0441\u043a\u0430\u044f", new ColorRGBA(231.0F, 0.0F, 250.0F)),
      internalField1125("\u041b\u0435\u0433\u0435\u043d\u0434\u0430\u0440\u043d\u0430\u044f", new ColorRGBA(0.0F, 128.0F, 250.0F)),
      internalField1126("\u041c\u0438\u0444\u0438\u0447\u0435\u0441\u043a\u0430\u044f", new ColorRGBA(252.0F, 84.0F, 252.0F));

      private final String internalField0248;
      private final ColorRGBA internalField0777;

      public static MineshaftTimerHud.InternalType0100 internalMethod02425(String localValue0) {
         if (localValue0 == null) {
            return null;
         } else {
            String localValue1 = localValue0.toLowerCase(Locale.ROOT);
            if (!localValue1.contains("\u044d\u043d\u0434\u0430")
               && !localValue1.contains("\u0430\u0434\u0430")
               && !localValue1.contains("\u044d\u043d\u0434")
               && !localValue1.contains("\u0430\u0434")) {
               for (MineshaftTimerHud.InternalType0100 localValue5 : values()) {
                  if (localValue1.contains(localValue5.internalField0248.toLowerCase(Locale.ROOT))) {
                     return localValue5;
                  }
               }

               return null;
            } else {
               return null;
            }
         }
      }

      @Generated
      public String internalMethod05312() {
         return this.internalField0248;
      }

      @Generated
      public ColorRGBA internalMethod00319() {
         return this.internalField0777;
      }

      @Generated
      private InternalType0100(String localValue3, ColorRGBA localValue4) {
         this.internalField0248 = localValue3;
         this.internalField0777 = localValue4;
      }
   }
}
