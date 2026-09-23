package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;

public final class PlayerMovementSimulator {
   public static final double internalField0194 = 0.3;
   public static final double internalField0193 = 1.8;
   public Vec3d internalField0283;
   public Vec3d internalField0282;
   public boolean internalField0277;
   public boolean internalField0276;
   public boolean internalField1099;
   public float internalField0205;
   private final World internalField0520;

   public PlayerMovementSimulator(Vec3d localValue1, Vec3d localValue2, boolean localValue3, boolean localValue4, boolean localValue5, float localValue6, World localValue7) {
      this.internalField0283 = localValue1;
      this.internalField0282 = localValue2;
      this.internalField0277 = localValue3;
      this.internalField0276 = localValue4;
      this.internalField1099 = localValue5;
      this.internalField0205 = localValue6;
      this.internalField0520 = localValue7;
   }

   public static PlayerMovementSimulator internalMethod07518(float localValue0) {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      ClientPlayerEntity localValue2 = localValue1.player;
      return localValue2 != null && localValue1.world != null
         ? new PlayerMovementSimulator(localValue2.getEntityPos(), localValue2.getVelocity(), localValue2.isOnGround(), localValue2.isSprinting(), localValue2.isSneaking(), localValue0, localValue1.world)
         : new PlayerMovementSimulator(Vec3d.ZERO, Vec3d.ZERO, false, false, false, localValue0, null);
   }

   public Box internalMethod04519() {
      return new Box(
         this.internalField0283.x - 0.3,
         this.internalField0283.y,
         this.internalField0283.z - 0.3,
         this.internalField0283.x + 0.3,
         this.internalField0283.y + 1.8,
         this.internalField0283.z + 0.3
      );
   }

   public void internalMethod00156(PlayerMovementSimulator.InternalType0155 localValue1) {
      if (this.internalField0520 != null) {
         if (this.internalField0276 && localValue1.internalField0194 <= 0.0) {
            this.internalField0276 = false;
         }

         if (!this.internalField0276 && localValue1.internalField0276 && localValue1.internalField0194 > 0.0 && !this.internalField1099) {
            this.internalField0276 = true;
         }

         this.internalField1099 = localValue1.internalField1099;
         if (localValue1.internalField0277 && this.internalField0277) {
            this.internalField0282 = new Vec3d(this.internalField0282.x, 0.42, this.internalField0282.z);
            if (this.internalField0276) {
               double localValue2 = Math.toRadians(this.internalField0205);
               this.internalField0282 = this.internalField0282.add(-Math.sin(localValue2) * 0.2, 0.0, Math.cos(localValue2) * 0.2);
            }

            this.internalField0277 = false;
         }

         double localValue24 = localValue1.internalField0193;
         double localValue4 = localValue1.internalField0194;
         double localValue6 = localValue24 * localValue24 + localValue4 * localValue4;
         if (localValue6 > 1.0E-6) {
            double localValue8 = 1.0 / Math.sqrt(localValue6);
            localValue24 *= localValue8;
            localValue4 *= localValue8;
            double localValue10 = this.internalField0277 ? (this.internalField0276 ? 0.13 : 0.1) : (this.internalField0276 ? 0.026 : 0.02);
            localValue24 *= localValue10;
            localValue4 *= localValue10;
            double localValue12 = Math.toRadians(this.internalField0205);
            double localValue14 = Math.sin(localValue12);
            double localValue16 = Math.cos(localValue12);
            double localValue18 = localValue24 * localValue16 - localValue4 * localValue14;
            double localValue20 = localValue4 * localValue16 + localValue24 * localValue14;
            this.internalField0282 = this.internalField0282.add(localValue18, 0.0, localValue20);
         }

         this.internalField0282 = new Vec3d(this.internalField0282.x, this.internalField0282.y - 0.08, this.internalField0282.z);
         Vec3d localValue29 = this.internalField0282;
         Box localValue9 = this.internalMethod04519();
         double localValue33 = localValue29.y;
         Box localValue34 = localValue9.stretch(0.0, localValue33, 0.0);

         for (VoxelShape localValue36 : this.internalField0520.getBlockCollisions(null, localValue34)) {
            localValue33 = localValue36.calculateMaxDistance(Axis.Y, localValue9, localValue33);
         }

         localValue9 = localValue9.offset(0.0, localValue33, 0.0);
         boolean localValue35 = localValue33 != localValue29.y;
         double localValue37 = localValue29.x;
         Box localValue38 = localValue9.stretch(localValue37, 0.0, 0.0);

         for (VoxelShape localValue40 : this.internalField0520.getBlockCollisions(null, localValue38)) {
            localValue37 = localValue40.calculateMaxDistance(Axis.X, localValue9, localValue37);
         }

         localValue9 = localValue9.offset(localValue37, 0.0, 0.0);
         boolean localValue39 = localValue37 != localValue29.x;
         double localValue41 = localValue29.z;
         Box localValue42 = localValue9.stretch(0.0, 0.0, localValue41);

         for (VoxelShape localValue22 : this.internalField0520.getBlockCollisions(null, localValue42)) {
            localValue41 = localValue22.calculateMaxDistance(Axis.Z, localValue9, localValue41);
         }

         localValue9 = localValue9.offset(0.0, 0.0, localValue41);
         boolean localValue43 = localValue41 != localValue29.z;
         this.internalField0283 = new Vec3d((localValue9.minX + localValue9.maxX) * 0.5, localValue9.minY, (localValue9.minZ + localValue9.maxZ) * 0.5);
         if (localValue35) {
            if (localValue29.y < 0.0) {
               this.internalField0277 = true;
            }

            this.internalField0282 = new Vec3d(this.internalField0282.x, 0.0, this.internalField0282.z);
         } else {
            this.internalField0277 = false;
         }

         if (localValue39) {
            this.internalField0282 = new Vec3d(0.0, this.internalField0282.y, this.internalField0282.z);
         }

         if (localValue43) {
            this.internalField0282 = new Vec3d(this.internalField0282.x, this.internalField0282.y, 0.0);
         }

         double localValue44 = this.internalField0277 ? 0.546 : 0.91;
         this.internalField0282 = new Vec3d(this.internalField0282.x * localValue44, this.internalField0282.y * 0.98, this.internalField0282.z * localValue44);
      }
   }

   public void internalMethod05281(int localValue1, PlayerMovementSimulator.InternalType0155 localValue2) {
      for (int localValue3 = 0; localValue3 < localValue1; localValue3++) {
         this.internalMethod00156(localValue2);
      }
   }

   public PlayerMovementSimulator internalMethod05479() {
      return new PlayerMovementSimulator(
         this.internalField0283, this.internalField0282, this.internalField0277, this.internalField0276, this.internalField1099, this.internalField0205, this.internalField0520
      );
   }

   public static final class InternalType0155 {
      final double internalField0194;
      final double internalField0193;
      final boolean internalField0277;
      final boolean internalField0276;
      final boolean internalField1099;
      public static final PlayerMovementSimulator.InternalType0155 internalField0285 = new PlayerMovementSimulator.InternalType0155(1.0, 0.0, false, true, false);
      public static final PlayerMovementSimulator.InternalType0155 internalField0286 = new PlayerMovementSimulator.InternalType0155(1.0, 0.0, true, true, false);
      public static final PlayerMovementSimulator.InternalType0155 internalField1107 = new PlayerMovementSimulator.InternalType0155(1.0, 0.0, false, false, false);
      public static final PlayerMovementSimulator.InternalType0155 internalField1108 = new PlayerMovementSimulator.InternalType0155(0.0, 0.0, false, false, false);

      public InternalType0155(double localValue1, double localValue3, boolean localValue5, boolean localValue6, boolean localValue7) {
         this.internalField0194 = localValue1;
         this.internalField0193 = localValue3;
         this.internalField0277 = localValue5;
         this.internalField0276 = localValue6;
         this.internalField1099 = localValue7;
      }

      @Override
      public final String toString() {
         return "InternalType0155[forward=" + this.internalField0194 + ", strafe=" + this.internalField0193 + ", jump=" + this.internalField0277 + ", sprintKey=" + this.internalField0276 + ", sneak=" + this.internalField1099 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0193);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0276);
         result = 31 * result + java.util.Objects.hashCode(this.internalField1099);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         PlayerMovementSimulator.InternalType0155 other = (PlayerMovementSimulator.InternalType0155) localValue1;
         return java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0193, other.internalField0193)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277)
            && java.util.Objects.equals(this.internalField0276, other.internalField0276)
            && java.util.Objects.equals(this.internalField1099, other.internalField1099);
      }

      public double internalMethod04710() {
         return this.internalField0194;
      }

      public double internalMethod04714() {
         return this.internalField0193;
      }

      public boolean internalMethod04711() {
         return this.internalField0277;
      }

      public boolean internalMethod04715() {
         return this.internalField0276;
      }

      public boolean internalMethod08452() {
         return this.internalField1099;
      }
   }
}
