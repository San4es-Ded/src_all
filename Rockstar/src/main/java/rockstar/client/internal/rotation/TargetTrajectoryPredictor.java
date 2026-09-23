package rockstar.client.internal.rotation;




import rockstar.client.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Deque;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public final class TargetTrajectoryPredictor {
   private static final double internalField0194 = 0.12;
   private static final int internalField0227 = 16;

   private TargetTrajectoryPredictor() {
   }

   public static TargetTrajectoryPredictor.InternalType0130 internalMethod02301(
      Entity localValue0, Vec3d localValue1, int localValue2, Vec3d localValue3, double localValue4, TargetTrajectoryPredictor.InternalType0029 localValue6
   ) {
      World localValue7 = localValue0.getEntityWorld();
      Vec3d localValue8 = localValue0.getEntityPos();
      boolean localValue9 = localValue0.isOnGround();
      boolean localValue10 = localValue0.isSneaking();
      boolean localValue11 = localValue0.isSprinting();
      double localValue12 = localValue0.getWidth();
      double localValue14 = localValue0.getHeight();
      double localValue16 = Math.hypot(localValue1.x, localValue1.z);
      boolean localValue18 = localValue16 > 0.048;
      float localValue19 = localValue18 ? (float)Math.toDegrees(Math.atan2(-localValue1.x, localValue1.z)) : localValue0.getYaw();
      int localValue20 = Math.max(1, localValue6.internalMethod03928());
      int localValue21 = Math.max(1, localValue6.internalMethod08688());
      ArrayList localValue22 = new ArrayList();
      localValue22.add(localValue8);
      List localValue23 = new ArrayList();
      localValue23.add(new TargetTrajectoryPredictor.InternalType0129(new PlayerMovementSimulator(localValue8, localValue1, localValue9, localValue11, localValue10, localValue19, localValue7), 1.0, localValue19, localValue22));
      int localValue24 = 0;

      for (boolean localValue25 = true; localValue24 < localValue6.internalMethod03924(); localValue25 = false) {
         int localValue26 = Math.min(localValue21, localValue6.internalMethod03924() - localValue24);
         localValue23 = internalMethod06685(localValue23, 16);
         ArrayList localValue27 = new ArrayList();

         for (TargetTrajectoryPredictor.InternalType0129 localValue29 : (Iterable<TargetTrajectoryPredictor.InternalType0129>)(Iterable<?>)localValue23) {
            double localValue30 = Math.hypot(localValue29.internalField0110.internalField0282.x, localValue29.internalField0110.internalField0282.z);
            double localValue32 = localValue25 ? Math.min(0.9, 0.05 + 0.85 * internalMethod05757(localValue2 / 8.0)) : 0.06;
            double localValue34 = 1.0 - localValue32;
            boolean localValue36 = localValue6.internalMethod03925() && localValue29.internalField0110.internalField0277;
            double localValue37 = localValue36 ? localValue34 * 0.12 : 0.0;
            double localValue39 = localValue34 - localValue37;
            double localValue41 = 1.5 + localValue30 * 10.0;
            double[] localValue43 = new double[localValue20];
            double localValue44 = 0.0;

            for (int localValue46 = 0; localValue46 < localValue20; localValue46++) {
               double localValue47 = localValue46 * ((Math.PI * 2) / localValue20);
               localValue43[localValue46] = Math.exp(localValue41 * Math.cos(localValue47));
               localValue44 += localValue43[localValue46];
            }

            localValue27.add(
               internalMethod02747(localValue29, localValue26, localValue29.internalField0205, TargetTrajectoryPredictor.InternalType0030.internalField0522, localValue29.internalField0194 * localValue32, localValue12, localValue14)
            );

            for (int localValue70 = 0; localValue70 < localValue20; localValue70++) {
               float localValue71 = localValue29.internalField0205 + localValue70 * (360.0F / localValue20);
               localValue27.add(
                  internalMethod02747(
                     localValue29, localValue26, localValue71, TargetTrajectoryPredictor.InternalType0030.internalField0523, localValue29.internalField0194 * localValue39 * localValue43[localValue70] / localValue44, localValue12, localValue14
                  )
               );
            }

            if (localValue36) {
               localValue27.add(
                  internalMethod02747(
                     localValue29, localValue26, localValue29.internalField0205, TargetTrajectoryPredictor.InternalType0030.internalField1190, localValue29.internalField0194 * localValue37, localValue12, localValue14
                  )
               );
            }
         }

         List localValue51 = internalMethod06685(localValue27, localValue6.internalMethod08689());
         internalMethod04305(localValue51);
         localValue23 = localValue51;
         localValue24 += localValue26;
      }

      ArrayList localValue50 = new ArrayList(localValue23.size());
      TargetTrajectoryPredictor.InternalType0129 localValue52 = null;

      for (TargetTrajectoryPredictor.InternalType0129 localValue55 : (Iterable<TargetTrajectoryPredictor.InternalType0129>)(Iterable<?>)localValue23) {
         if (localValue52 == null || localValue55.internalField0194 > localValue52.internalField0194) {
            localValue52 = localValue55;
         }
      }

      ArrayList localValue54 = new ArrayList(localValue23.size());
      double[] localValue56 = new double[localValue23.size()];
      Vec3d localValue57 = localValue8;

      for (int localValue31 = 0; localValue31 < localValue23.size(); localValue31++) {
         TargetTrajectoryPredictor.InternalType0129 localValue59 = (TargetTrajectoryPredictor.InternalType0129)localValue23.get(localValue31);
         Box localValue33 = internalMethod03149(localValue59.internalField0110.internalField0283, localValue12, localValue14);
         boolean localValue61 = localValue59 == localValue52;
         localValue50.add(new TargetTrajectoryPredictor.InternalType0131(localValue59.internalField0416, localValue59.internalField0110.internalField0283, localValue33, localValue59.internalField0194, localValue61));
         localValue54.add(localValue33);
         localValue56[localValue31] = localValue59.internalField0194;
         if (localValue61) {
            localValue57 = localValue33.getCenter();
         }
      }

      Vec3d localValue58 = localValue57;
      double localValue60 = -1.0;
      double localValue62 = Double.MAX_VALUE;

      for (Box localValue65 : (Iterable<Box>)(Iterable<?>)localValue54) {
         Vec3d localValue38 = localValue65.getCenter();
         double localValue67 = internalMethod03015(localValue3, localValue38, localValue54, localValue56, localValue6.internalMethod03923());
         double localValue69 = localValue38.squaredDistanceTo(localValue57);
         if (localValue67 > localValue60 + 1.0E-6 || Math.abs(localValue67 - localValue60) <= 1.0E-6 && localValue69 < localValue62) {
            localValue60 = localValue67;
            localValue62 = localValue69;
            localValue58 = localValue38;
         }
      }

      double localValue64 = internalMethod05757(localValue4);
      Vec3d localValue66 = localValue57.add(localValue58.subtract(localValue57).multiply(localValue64));
      double localValue68 = internalMethod03015(localValue3, localValue66, localValue54, localValue56, localValue6.internalMethod03923());
      return new TargetTrajectoryPredictor.InternalType0130(localValue50, localValue57, localValue58, localValue66, localValue68, localValue54.size(), localValue6.internalMethod03924(), localValue3);
   }

   public static Vec3d internalMethod01475(int localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      if (localValue2 == null || localValue1.world == null) {
         return null;
      } else if (localValue0 <= 0) {
         return localValue2.getEyePos();
      } else {
         Vec3d localValue3 = new Vec3d(localValue2.getX() - localValue2.lastX, localValue2.getY() - localValue2.lastY, localValue2.getZ() - localValue2.lastZ);
         double localValue4 = Math.hypot(localValue3.x, localValue3.z);
         boolean localValue6 = localValue4 > 0.048;
         float localValue7 = localValue6 ? (float)Math.toDegrees(Math.atan2(-localValue3.x, localValue3.z)) : localValue2.getYaw();
         double localValue8 = localValue2.getEyePos().y - localValue2.getY();
         PlayerMovementSimulator localValue10 = new PlayerMovementSimulator(localValue2.getEntityPos(), localValue3, localValue2.isOnGround(), localValue2.isSprinting(), localValue2.isSneaking(), localValue7, localValue1.world);
         PlayerMovementSimulator.InternalType0155 localValue11 = localValue6
            ? PlayerMovementSimulator.InternalType0155.internalField0285
            : PlayerMovementSimulator.InternalType0155.internalField1108;

         for (int localValue12 = 0; localValue12 < localValue0; localValue12++) {
            localValue10.internalMethod00156(localValue11);
         }

         return localValue10.internalField0283.add(0.0, localValue8, 0.0);
      }
   }

   private static TargetTrajectoryPredictor.InternalType0129 internalMethod02747(
      TargetTrajectoryPredictor.InternalType0129 localValue0, int localValue1, float localValue2, TargetTrajectoryPredictor.InternalType0030 localValue3, double localValue4, double localValue6, double localValue8
   ) {
      TargetTrajectoryPredictor.InternalType0129 localValue10 = localValue0.internalMethod07131();
      localValue10.internalField0194 = localValue4;
      localValue10.internalField0205 = localValue2;
      localValue10.internalField0110.internalField0205 = localValue2;

      for (int localValue11 = 0; localValue11 < localValue1; localValue11++) {
         PlayerMovementSimulator.InternalType0155 localValue12 = switch (localValue3) {
            case internalField0522 -> PlayerMovementSimulator.InternalType0155.internalField1108;
            case internalField0523 -> PlayerMovementSimulator.InternalType0155.internalField0285;
            case internalField1190 -> localValue11 == 0 && localValue10.internalField0110.internalField0277
               ? PlayerMovementSimulator.InternalType0155.internalField0286
               : PlayerMovementSimulator.InternalType0155.internalField0285;
         };
         localValue10.internalField0110.internalMethod00156(localValue12);
         localValue10.internalField0416.add(localValue10.internalField0110.internalField0283);
      }

      return localValue10;
   }

   private static double internalMethod03015(Vec3d localValue0, Vec3d localValue1, List<Box> localValue2, double[] localValue3, double localValue4) {
      Vec3d localValue6 = localValue1.subtract(localValue0);
      if (localValue6.lengthSquared() < 1.0E-9) {
         return 0.0;
      } else {
         Vec3d localValue7 = localValue0.add(localValue6.normalize().multiply(localValue4));
         double localValue8 = 0.0;

         for (int localValue10 = 0; localValue10 < localValue2.size(); localValue10++) {
            if (((Box)localValue2.get(localValue10)).raycast(localValue0, localValue7).isPresent()) {
               localValue8 += localValue3[localValue10];
            }
         }

         return localValue8;
      }
   }

   private static List<TargetTrajectoryPredictor.InternalType0129> internalMethod06685(List<TargetTrajectoryPredictor.InternalType0129> localValue0, int localValue1) {
      if (localValue0.size() <= localValue1) {
         return localValue0;
      } else {
         localValue0.sort(Comparator.<TargetTrajectoryPredictor.InternalType0129>comparingDouble(localValue0x -> localValue0x.internalField0194).reversed());
         return new ArrayList<>(localValue0.subList(0, localValue1));
      }
   }

   private static void internalMethod04305(List<TargetTrajectoryPredictor.InternalType0129> localValue0) {
      double localValue1 = 0.0;

      for (TargetTrajectoryPredictor.InternalType0129 localValue4 : localValue0) {
         localValue1 += localValue4.internalField0194;
      }

      if (!(localValue1 <= 0.0)) {
         for (TargetTrajectoryPredictor.InternalType0129 localValue6 : localValue0) {
            localValue6.internalField0194 /= localValue1;
         }
      }
   }

   private static Box internalMethod03149(Vec3d localValue0, double localValue1, double localValue3) {
      double localValue5 = localValue1 / 2.0;
      return new Box(localValue0.x - localValue5, localValue0.y, localValue0.z - localValue5, localValue0.x + localValue5, localValue0.y + localValue3, localValue0.z + localValue5);
   }

   private static double internalMethod05757(double localValue0) {
      return localValue0 < 0.0 ? 0.0 : Math.min(localValue0, 1.0);
   }

   public static final class InternalType0029 {
      private final int internalField0227;
      private final int internalField0228;
      private final int internalField1053;
      private final boolean internalField0277;
      private final double internalField0194;
      private final int internalField1055;

      public InternalType0029(int localValue1, int localValue2, int localValue3, boolean localValue4, double localValue5, int localValue7) {
         this.internalField0227 = localValue1;
         this.internalField0228 = localValue2;
         this.internalField1053 = localValue3;
         this.internalField0277 = localValue4;
         this.internalField0194 = localValue5;
         this.internalField1055 = localValue7;
      }

      public static TargetTrajectoryPredictor.InternalType0029 internalMethod06684() {
         return new TargetTrajectoryPredictor.InternalType0029(10, 8, 4, true, 64.0, 96);
      }

      @Override
      public final String toString() {
         return "InternalType0029[ticks=" + this.internalField0227 + ", directions=" + this.internalField0228 + ", branchInterval=" + this.internalField1053 + ", allowJump=" + this.internalField0277 + ", rayRange=" + this.internalField0194 + ", maxLeaves=" + this.internalField1055 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1053);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1055);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TargetTrajectoryPredictor.InternalType0029 other = (TargetTrajectoryPredictor.InternalType0029) localValue1;
         return java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1053, other.internalField1053)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField1055, other.internalField1055);
      }

      public int internalMethod03924() {
         return this.internalField0227;
      }

      public int internalMethod03928() {
         return this.internalField0228;
      }

      public int internalMethod08688() {
         return this.internalField1053;
      }

      public boolean internalMethod03925() {
         return this.internalField0277;
      }

      public double internalMethod03923() {
         return this.internalField0194;
      }

      public int internalMethod08689() {
         return this.internalField1055;
      }
   }

   static enum InternalType0030 {
      internalField0522,
      internalField0523,
      internalField1190;
   }

   public static final class InternalType0128 {
      private static final int internalField0227 = 20;
      private static final double internalField0194 = 0.02;
      private int internalField0228 = -1;
      private final Deque<Vec3d> internalField0796 = new ArrayDeque<>();
      private Vec3d internalField0283 = Vec3d.ZERO;
      private int internalField1053 = 0;

      public void internalMethod00730(Entity localValue1) {
         if (localValue1 == null) {
            this.internalMethod06620();
         } else {
            if (localValue1.getId() != this.internalField0228) {
               this.internalField0228 = localValue1.getId();
               this.internalField0796.clear();
               this.internalField1053 = 0;
               this.internalField0283 = Vec3d.ZERO;
            }

            Vec3d localValue2 = localValue1.getEntityPos();
            if (!this.internalField0796.isEmpty()) {
               this.internalField0283 = localValue2.subtract(this.internalField0796.peekLast());
               double localValue3 = Math.hypot(this.internalField0283.x, this.internalField0283.z);
               if (localValue3 < 0.02) {
                  this.internalField1053++;
               } else {
                  this.internalField1053 = 0;
               }
            }

            this.internalField0796.addLast(localValue2);

            while (this.internalField0796.size() > 20) {
               this.internalField0796.removeFirst();
            }
         }
      }

      public void internalMethod06620() {
         this.internalField0228 = -1;
         this.internalField0796.clear();
         this.internalField0283 = Vec3d.ZERO;
         this.internalField1053 = 0;
      }

      public Vec3d internalMethod02782() {
         return this.internalField0283;
      }

      public Vec3d internalMethod05393() {
         if (this.internalField0796.size() < 2) {
            return this.internalField0283;
         } else {
            Vec3d[] localValue1 = this.internalField0796.toArray(new Vec3d[0]);
            int localValue2 = localValue1.length;
            int localValue3 = Math.min(5, localValue2 - 1);
            Vec3d localValue4 = Vec3d.ZERO;

            for (int localValue5 = localValue2 - localValue3; localValue5 < localValue2; localValue5++) {
               localValue4 = localValue4.add(localValue1[localValue5].subtract(localValue1[localValue5 - 1]));
            }

            return localValue4.multiply(1.0 / localValue3);
         }
      }

      public int internalMethod06619() {
         return this.internalField1053;
      }
   }

   static final class InternalType0129 {
      PlayerMovementSimulator internalField0110;
      double internalField0194;
      float internalField0205;
      List<Vec3d> internalField0416;

      InternalType0129(PlayerMovementSimulator localValue1, double localValue2, float localValue4, List<Vec3d> localValue5) {
         this.internalField0110 = localValue1;
         this.internalField0194 = localValue2;
         this.internalField0205 = localValue4;
         this.internalField0416 = localValue5;
      }

      TargetTrajectoryPredictor.InternalType0129 internalMethod07131() {
         return new TargetTrajectoryPredictor.InternalType0129(
            this.internalField0110.internalMethod05479(), this.internalField0194, this.internalField0205, new ArrayList<>(this.internalField0416)
         );
      }
   }

   public static final class InternalType0130 {
      private final List<TargetTrajectoryPredictor.InternalType0131> internalField0416;
      private final Vec3d internalField0283;
      private final Vec3d internalField0282;
      private final Vec3d internalField1104;
      private final double internalField0194;
      private final int internalField0227;
      private final int internalField0228;
      private final Vec3d internalField1106;

      public InternalType0130(List<TargetTrajectoryPredictor.InternalType0131> localValue1, Vec3d localValue2, Vec3d localValue3, Vec3d localValue4, double localValue5, int localValue7, int localValue8, Vec3d localValue9) {
         this.internalField0416 = localValue1;
         this.internalField0283 = localValue2;
         this.internalField0282 = localValue3;
         this.internalField1104 = localValue4;
         this.internalField0194 = localValue5;
         this.internalField0227 = localValue7;
         this.internalField0228 = localValue8;
         this.internalField1106 = localValue9;
      }

      public double internalMethod03416() {
         return this.internalField0194 * 100.0;
      }

      @Override
      public final String toString() {
         return "InternalType0130[trajectories=" + this.internalField0416 + ", mostLikelyPoint=" + this.internalField0283 + ", maxCoveragePoint=" + this.internalField0282 + ", aimPoint=" + this.internalField1104 + ", coverage=" + this.internalField0194 + ", variants=" + this.internalField0227 + ", ticks=" + this.internalField0228 + ", eyePos=" + this.internalField1106 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0282);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1104);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1106);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TargetTrajectoryPredictor.InternalType0130 other = (TargetTrajectoryPredictor.InternalType0130) localValue1;
         return java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0282, other.internalField0282)
            && java.util.Objects.equals(this.internalField1104, other.internalField1104)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228)
            && java.util.Objects.equals(this.internalField1106, other.internalField1106);
      }

      public List<TargetTrajectoryPredictor.InternalType0131> internalMethod03491() {
         return this.internalField0416;
      }

      public Vec3d internalMethod06541() {
         return this.internalField0283;
      }

      public Vec3d internalMethod03590() {
         return this.internalField0282;
      }

      public Vec3d internalMethod07844() {
         return this.internalField1104;
      }

      public double internalMethod03420() {
         return this.internalField0194;
      }

      public int internalMethod03417() {
         return this.internalField0227;
      }

      public int internalMethod03421() {
         return this.internalField0228;
      }

      public Vec3d internalMethod08835() {
         return this.internalField1106;
      }
   }

   public static final class InternalType0131 {
      private final List<Vec3d> internalField0416;
      private final Vec3d internalField0283;
      private final Box internalField0681;
      private final double internalField0194;
      private final boolean internalField0277;

      public InternalType0131(List<Vec3d> localValue1, Vec3d localValue2, Box localValue3, double localValue4, boolean localValue6) {
         this.internalField0416 = localValue1;
         this.internalField0283 = localValue2;
         this.internalField0681 = localValue3;
         this.internalField0194 = localValue4;
         this.internalField0277 = localValue6;
      }

      @Override
      public final String toString() {
         return "InternalType0131[path=" + this.internalField0416 + ", endPos=" + this.internalField0283 + ", endBox=" + this.internalField0681 + ", probability=" + this.internalField0194 + ", mostLikely=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0416);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0681);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         TargetTrajectoryPredictor.InternalType0131 other = (TargetTrajectoryPredictor.InternalType0131) localValue1;
         return java.util.Objects.equals(this.internalField0416, other.internalField0416)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283)
            && java.util.Objects.equals(this.internalField0681, other.internalField0681)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public List<Vec3d> internalMethod00026() {
         return this.internalField0416;
      }

      public Vec3d internalMethod02693() {
         return this.internalField0283;
      }

      public Box internalMethod04240() {
         return this.internalField0681;
      }

      public double internalMethod05262() {
         return this.internalField0194;
      }

      public boolean internalMethod05263() {
         return this.internalField0277;
      }
   }
}
