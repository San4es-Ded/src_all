package rockstar.client.internal.game;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import lombok.Generated;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class HitParticleEmitter {
   private static final double internalField0194 = Math.PI * 2;
   private static final Random internalField0362 = new Random();
   private final List<HitParticleEmitter.InternalType0323> internalField0416 = new ArrayList<>();
   private int internalField0227 = 0;
   private long internalField0229 = 0L;
   private float internalField0205 = 0.12F;
   private HitParticleEmitter.InternalType0431 internalField0864;
   private HitParticleEmitter.InternalType0322 internalField0785;
   private HitParticleEmitter.InternalType0430 internalField0863;
   private final Vec3d[] internalField0724;
   private int internalField0228;
   private int internalField1053;
   private int internalField1055;
   private float internalField0206;
   private boolean internalField0277;
   private int internalField1056;
   private float internalField1048;
   private float internalField1047;

   public HitParticleEmitter() {
      this.internalField0864 = HitParticleEmitter.InternalType0431.internalField0864;
      this.internalField0785 = HitParticleEmitter.InternalType0322.internalField0785;
      this.internalField0863 = HitParticleEmitter.InternalType0430.internalField0862;
      this.internalField0724 = new Vec3d[15];
      this.internalField0228 = 0;
      this.internalField1053 = 0;
      this.internalField1055 = 8;
      this.internalField0206 = 0.6F;
      this.internalField0277 = true;
      this.internalField1056 = 8;
      this.internalField1048 = 0.6F;
      this.internalField1047 = 0.12F;
   }

   public void internalMethod03148(int localValue1, float localValue2, boolean localValue3) {
      this.internalField1055 = localValue1;
      this.internalField0206 = localValue2;
      this.internalField0277 = localValue3;
   }

   public void internalMethod06286(float localValue1) {
      this.internalField0205 = MathHelper.clamp(localValue1, 0.05F, 0.5F);
   }

   public void internalMethod07559(Vec3d localValue1, LivingEntity localValue2, Vec3d localValue3, float localValue4) {
      if (localValue2 != null && localValue1 != null) {
         this.internalField0864 = this.internalMethod04304(localValue2, localValue3);
         this.internalMethod06375(localValue2.getEntityPos());
         this.internalField0785 = this.internalMethod01576();
         this.internalField0863 = this.internalMethod00164(localValue4);
         this.internalMethod06588();
         this.internalField0416.clear();
         this.internalField0227 = 0;
         this.internalField0229 = System.currentTimeMillis();
         this.internalMethod02144(localValue1, localValue2, localValue4);
         this.internalField0416.sort((localValue0, localValue1x) -> Float.compare(localValue1x.internalField0205, localValue0.internalField0205));
      } else {
         this.internalField0416.clear();
         this.internalField1056 = 0;
      }
   }

   private HitParticleEmitter.InternalType0431 internalMethod04304(LivingEntity localValue1, Vec3d localValue2) {
      if (localValue1 == null || localValue1.getEntityWorld() == null) {
         return HitParticleEmitter.InternalType0431.internalField0864;
      } else if (localValue1.isTouchingWater()) {
         return HitParticleEmitter.InternalType0431.internalField1363;
      } else if (localValue1.isInLava() || localValue1.isInLava()) {
         return HitParticleEmitter.InternalType0431.internalField1361;
      } else if (localValue1.isOnGround()) {
         return HitParticleEmitter.InternalType0431.internalField0864;
      } else {
         return localValue1 instanceof PlayerEntity localValue3 && localValue3.isClimbing()
            ? HitParticleEmitter.InternalType0431.internalField1362
            : HitParticleEmitter.InternalType0431.internalField0865;
      }
   }

   private void internalMethod06375(Vec3d localValue1) {
      this.internalField0724[this.internalField0228] = localValue1;
      this.internalField0228 = (this.internalField0228 + 1) % this.internalField0724.length;
      if (this.internalField1053 < this.internalField0724.length) {
         this.internalField1053++;
      }
   }

   private HitParticleEmitter.InternalType0322 internalMethod01576() {
      if (this.internalField1053 < 5) {
         return HitParticleEmitter.InternalType0322.internalField0785;
      } else {
         ArrayList localValue1 = new ArrayList();
         ArrayList localValue2 = new ArrayList();

         for (int localValue3 = 1; localValue3 < Math.min(10, this.internalField1053); localValue3++) {
            int localValue4 = (this.internalField0228 - localValue3 + this.internalField0724.length) % this.internalField0724.length;
            int localValue5 = (this.internalField0228 - localValue3 - 1 + this.internalField0724.length) % this.internalField0724.length;
            if (this.internalField0724[localValue4] != null && this.internalField0724[localValue5] != null) {
               double localValue6 = this.internalField0724[localValue4].distanceTo(this.internalField0724[localValue5]);
               localValue1.add(localValue6);
               if (localValue3 >= 2) {
                  int localValue8 = (this.internalField0228 - localValue3 - 2 + this.internalField0724.length) % this.internalField0724.length;
                  if (this.internalField0724[localValue8] != null) {
                     Vec3d localValue9 = this.internalField0724[localValue5].subtract(this.internalField0724[localValue8]);
                     Vec3d localValue10 = this.internalField0724[localValue4].subtract(this.internalField0724[localValue5]);
                     double localValue11 = Math.atan2(localValue10.z, localValue10.x) - Math.atan2(localValue9.z, localValue9.x);
                     localValue11 = internalMethod06285(localValue11);
                     localValue2.add(Math.abs(localValue11));
                  }
               }
            }
         }

         if (localValue1.isEmpty()) {
            return HitParticleEmitter.InternalType0322.internalField0785;
         } else {
            double localValue13 = localValue1.stream().mapToDouble(value -> ((Double)value).doubleValue()).average().orElse(0.0);
            if (localValue13 < 0.01) {
               return HitParticleEmitter.InternalType0322.internalField0785;
            } else {
               if (localValue2.size() >= 3) {
                  double localValue14 = localValue2.stream().mapToDouble(value -> ((Double)value).doubleValue()).average().orElse(0.0);
                  double localValue7 = localValue2.stream().mapToDouble(localValue2x -> Math.pow((Double)localValue2x - localValue14, 2.0)).average().orElse(1.0);
                  if (localValue7 < 0.15 && localValue14 > 0.05 && localValue14 < 0.5) {
                     return HitParticleEmitter.InternalType0322.internalField1318;
                  }
               }

               if (localValue2.size() >= 3) {
                  double localValue15 = localValue2.stream().mapToDouble(value -> ((Double)value).doubleValue()).average().orElse(0.0);
                  if (localValue15 < 0.1) {
                     return HitParticleEmitter.InternalType0322.internalField0786;
                  }
               }

               double localValue16 = localValue1.stream().mapToDouble(localValue2x -> Math.pow((Double)localValue2x - localValue13, 2.0)).average().orElse(1.0);
               return localValue16 < 0.05 ? HitParticleEmitter.InternalType0322.internalField1319 : HitParticleEmitter.InternalType0322.internalField1320;
            }
         }
      }
   }

   private HitParticleEmitter.InternalType0430 internalMethod00164(float localValue1) {
      if (localValue1 < 3.0F) {
         return HitParticleEmitter.InternalType0430.internalField0863;
      } else {
         return localValue1 < 6.0F ? HitParticleEmitter.InternalType0430.internalField0862 : HitParticleEmitter.InternalType0430.internalField1358;
      }
   }

   private void internalMethod06588() {
      if (!this.internalField0277) {
         this.internalField1056 = this.internalField1055;
         this.internalField1048 = this.internalField0206;
         this.internalField1047 = this.internalField0205;
      } else {
         int localValue1 = this.internalField1055;
         float localValue2 = this.internalField0206;
         float localValue3 = this.internalField0205;
         switch (this.internalField0864) {
            case internalField0865:
               localValue1 = (int)(localValue1 * 1.2);
               localValue2 *= 1.1F;
               localValue3 *= 0.9F;
               break;
            case internalField1363:
               localValue1 = (int)(localValue1 * 0.7);
               localValue2 *= 0.6F;
               localValue3 *= 1.3F;
               break;
            case internalField1361:
               localValue1 = (int)(localValue1 * 0.6);
               localValue2 *= 0.5F;
               localValue3 *= 1.5F;
               break;
            case internalField1362:
               localValue1 = (int)(localValue1 * 0.8);
               localValue2 *= 0.7F;
               localValue3 *= 1.2F;
         }

         switch (this.internalField0785) {
            case internalField0786:
               localValue1 = (int)(localValue1 * 1.1);
               localValue2 *= 0.9F;
               localValue3 *= 0.9F;
               break;
            case internalField1318:
               localValue1 = (int)(localValue1 * 1.4);
               localValue2 *= 1.2F;
               localValue3 *= 0.8F;
               break;
            case internalField1320:
               localValue1 = (int)(localValue1 * 1.3);
               localValue2 *= 1.3F;
               localValue3 *= 0.7F;
               break;
            case internalField1319:
               localValue1 = (int)(localValue1 * 0.8);
               localValue2 *= 0.8F;
               localValue3 *= 1.1F;
         }

         switch (this.internalField0863) {
            case internalField0863:
               localValue1 = (int)(localValue1 * 1.3);
               localValue2 *= 0.7F;
               localValue3 *= 0.8F;
            case internalField0862:
            default:
               break;
            case internalField1358:
               localValue1 = (int)(localValue1 * 0.7);
               localValue2 *= 1.4F;
               localValue3 *= 1.2F;
         }

         this.internalField1056 = MathHelper.clamp(localValue1, 3, 50);
         this.internalField1048 = MathHelper.clamp(localValue2, 0.2F, 2.0F);
         this.internalField1047 = MathHelper.clamp(localValue3, 0.05F, 0.5F);
      }
   }

   private void internalMethod02144(Vec3d localValue1, LivingEntity localValue2, float localValue3) {
      double localValue4 = localValue2.getWidth() / 2.0;
      double localValue6 = localValue2.getHeight();
      float localValue8;
      float localValue9;
      float localValue10;
      float localValue11;
      float localValue12;
      float localValue13;
      switch (this.internalField0864) {
         case internalField0865:
            localValue8 = 0.6F;
            localValue9 = 0.8F;
            localValue10 = 0.4F;
            localValue11 = 0.3F;
            localValue12 = 0.5F;
            localValue13 = 0.2F;
            break;
         case internalField1363:
            localValue8 = 0.9F;
            localValue9 = 0.5F;
            localValue10 = 0.2F;
            localValue11 = 0.5F;
            localValue12 = 0.35F;
            localValue13 = 0.15F;
            break;
         case internalField1361:
         default:
            localValue8 = 0.95F;
            localValue9 = 0.7F;
            localValue10 = 0.35F;
            localValue11 = 0.4F;
            localValue12 = 0.4F;
            localValue13 = 0.2F;
            break;
         case internalField1362:
            localValue8 = 0.85F;
            localValue9 = 0.6F;
            localValue10 = 0.3F;
            localValue11 = 0.45F;
            localValue12 = 0.4F;
            localValue13 = 0.15F;
      }

      switch (this.internalField0785) {
         case internalField0786:
            this.internalMethod02267(localValue1, localValue4, localValue6, this.internalField1048, this.internalField1056, localValue8, localValue9, localValue10, localValue11, localValue12, localValue13);
            break;
         case internalField1318:
            this.internalMethod00248(localValue1, localValue4, localValue6, this.internalField1048, this.internalField1056, localValue8, localValue9, localValue10, localValue11, localValue12, localValue13);
            break;
         case internalField1320:
            this.internalMethod08893(localValue1, localValue4, localValue6, this.internalField1048, this.internalField1056, localValue8, localValue9, localValue10, localValue11, localValue12, localValue13);
            break;
         default:
            this.internalMethod07876(localValue1, localValue4, localValue6, this.internalField1048, this.internalField1056, localValue8, localValue9, localValue10, localValue11, localValue12, localValue13);
      }
   }

   private void internalMethod00248(
      Vec3d localValue1, double localValue2, double localValue4, float localValue6, int localValue7, float localValue8, float localValue9, float localValue10, float localValue11, float localValue12, float localValue13
   ) {
      Vec3d localValue14 = this.internalMethod01077();
      int localValue15 = (int)(localValue7 * localValue11);
      int localValue16 = (int)(localValue7 * localValue12);
      int localValue17 = localValue7 - localValue15 - localValue16;

      for (int localValue18 = 0; localValue18 < localValue15; localValue18++) {
         float localValue19;
         if (localValue14 != null && localValue18 < localValue15 * 0.7) {
            float localValue20 = (float)Math.atan2(localValue14.z, localValue14.x);
            localValue19 = localValue20 + (float)((internalField0362.nextDouble() - 0.5) * 0.6);
         } else {
            localValue19 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         }

         float localValue25 = (float)(localValue2 * localValue6 * (0.3 + internalField0362.nextDouble() * 0.4));
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue19) * localValue25,
                  localValue1.y + localValue4 * (0.75 + internalField0362.nextDouble() * 0.2),
                  localValue1.z + Math.sin(localValue19) * localValue25,
                  localValue8,
                  0.85F,
                  this.internalField0864
               )
            );
      }

      for (int localValue21 = 0; localValue21 < localValue16; localValue21++) {
         float localValue23;
         if (localValue14 != null && localValue21 < localValue16 * 0.6) {
            float localValue26 = (float)Math.atan2(localValue14.z, localValue14.x);
            localValue23 = localValue26 + (float)((internalField0362.nextDouble() - 0.5) * 0.8);
         } else {
            localValue23 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         }

         float localValue27 = (float)(localValue2 * localValue6 * (0.4 + internalField0362.nextDouble() * 0.4));
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue23) * localValue27,
                  localValue1.y + localValue4 * (0.45 + internalField0362.nextDouble() * 0.25),
                  localValue1.z + Math.sin(localValue23) * localValue27,
                  localValue9,
                  0.7F,
                  this.internalField0864
               )
            );
      }

      for (int localValue22 = 0; localValue22 < localValue17; localValue22++) {
         float localValue24 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         float localValue28 = (float)(localValue2 * localValue6 * internalField0362.nextDouble());
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue24) * localValue28,
                  localValue1.y + localValue4 * (0.15 + internalField0362.nextDouble() * 0.25),
                  localValue1.z + Math.sin(localValue24) * localValue28,
                  localValue10,
                  0.4F,
                  this.internalField0864
               )
            );
      }
   }

   private void internalMethod02267(
      Vec3d localValue1, double localValue2, double localValue4, float localValue6, int localValue7, float localValue8, float localValue9, float localValue10, float localValue11, float localValue12, float localValue13
   ) {
      Vec3d localValue14 = this.internalMethod01077();
      if (localValue14 == null) {
         localValue14 = new Vec3d(1.0, 0.0, 0.0);
      }

      localValue14 = localValue14.normalize();
      int localValue15 = (int)(localValue7 * localValue11);
      int localValue16 = (int)(localValue7 * localValue12);

      for (int localValue17 = 0; localValue17 < localValue7; localValue17++) {
         float localValue18 = (float)localValue17 / localValue7;
         float localValue19;
         float localValue20;
         float localValue21;
         if (localValue17 < localValue15) {
            localValue19 = 0.75F + (float)(internalField0362.nextDouble() * 0.2);
            localValue20 = localValue8;
            localValue21 = 0.85F;
         } else if (localValue17 < localValue15 + localValue16) {
            localValue19 = 0.45F + (float)(internalField0362.nextDouble() * 0.25);
            localValue20 = localValue9;
            localValue21 = 0.7F;
         } else {
            localValue19 = 0.15F + (float)(internalField0362.nextDouble() * 0.25);
            localValue20 = localValue10;
            localValue21 = 0.4F;
         }

         float localValue22 = (float)((localValue18 - 0.5F) * localValue2 * localValue6 * 0.5);
         float localValue23 = (float)((internalField0362.nextDouble() - 0.5) * localValue2 * localValue6 * 0.6);
         double localValue24 = localValue1.x + localValue14.x * localValue22 - localValue14.z * localValue23;
         double localValue26 = localValue1.z + localValue14.z * localValue22 + localValue14.x * localValue23;
         this.internalField0416.add(new HitParticleEmitter.InternalType0323(localValue24, localValue1.y + localValue4 * localValue19, localValue26, localValue20, localValue21, this.internalField0864));
      }
   }

   private void internalMethod08893(
      Vec3d localValue1, double localValue2, double localValue4, float localValue6, int localValue7, float localValue8, float localValue9, float localValue10, float localValue11, float localValue12, float localValue13
   ) {
      int localValue14 = (int)(localValue7 * localValue11);
      int localValue15 = (int)(localValue7 * localValue12);
      int localValue16 = localValue7 - localValue14 - localValue15;

      for (int localValue17 = 0; localValue17 < localValue14; localValue17++) {
         float localValue18 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         float localValue19 = (float)(localValue2 * localValue6 * (0.2 + internalField0362.nextDouble() * 0.5));
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue18) * localValue19,
                  localValue1.y + localValue4 * (0.7 + internalField0362.nextDouble() * 0.25),
                  localValue1.z + Math.sin(localValue18) * localValue19,
                  localValue8,
                  0.8F,
                  this.internalField0864
               )
            );
      }

      for (int localValue20 = 0; localValue20 < localValue15; localValue20++) {
         float localValue22 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         float localValue24 = (float)(localValue2 * localValue6 * (0.3 + internalField0362.nextDouble() * 0.5));
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue22) * localValue24,
                  localValue1.y + localValue4 * (0.4 + internalField0362.nextDouble() * 0.3),
                  localValue1.z + Math.sin(localValue22) * localValue24,
                  localValue9,
                  0.65F,
                  this.internalField0864
               )
            );
      }

      for (int localValue21 = 0; localValue21 < localValue16; localValue21++) {
         float localValue23 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         float localValue25 = (float)(localValue2 * localValue6 * internalField0362.nextDouble());
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue23) * localValue25,
                  localValue1.y + localValue4 * (0.1 + internalField0362.nextDouble() * 0.3),
                  localValue1.z + Math.sin(localValue23) * localValue25,
                  localValue10,
                  0.35F,
                  this.internalField0864
               )
            );
      }
   }

   private void internalMethod07876(
      Vec3d localValue1, double localValue2, double localValue4, float localValue6, int localValue7, float localValue8, float localValue9, float localValue10, float localValue11, float localValue12, float localValue13
   ) {
      int localValue14 = (int)(localValue7 * localValue11);
      int localValue15 = (int)(localValue7 * localValue12);
      int localValue16 = localValue7 - localValue14 - localValue15;

      for (int localValue17 = 0; localValue17 < localValue14; localValue17++) {
         float localValue18 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         float localValue19 = (float)(localValue2 * localValue6 * (0.15 + internalField0362.nextDouble() * 0.35));
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue18) * localValue19,
                  localValue1.y + localValue4 * (0.78 + internalField0362.nextDouble() * 0.18),
                  localValue1.z + Math.sin(localValue18) * localValue19,
                  localValue8,
                  0.9F,
                  this.internalField0864
               )
            );
      }

      for (int localValue20 = 0; localValue20 < localValue15; localValue20++) {
         float localValue22 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         float localValue24 = (float)(localValue2 * localValue6 * (0.3 + internalField0362.nextDouble() * 0.5));
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue22) * localValue24,
                  localValue1.y + localValue4 * (0.42 + internalField0362.nextDouble() * 0.28),
                  localValue1.z + Math.sin(localValue22) * localValue24,
                  localValue9,
                  0.7F,
                  this.internalField0864
               )
            );
      }

      for (int localValue21 = 0; localValue21 < localValue16; localValue21++) {
         float localValue23 = (float)(internalField0362.nextDouble() * (Math.PI * 2));
         float localValue25 = (float)(localValue2 * localValue6 * (0.2 + internalField0362.nextDouble() * 0.6));
         this.internalField0416
            .add(
               new HitParticleEmitter.InternalType0323(
                  localValue1.x + Math.cos(localValue23) * localValue25,
                  localValue1.y + localValue4 * (0.12 + internalField0362.nextDouble() * 0.28),
                  localValue1.z + Math.sin(localValue23) * localValue25,
                  localValue10,
                  0.35F,
                  this.internalField0864
               )
            );
      }
   }

   private Vec3d internalMethod01077() {
      if (this.internalField1053 < 2) {
         return null;
      } else {
         Vec3d localValue1 = this.internalField0724[(this.internalField0228 + 1) % this.internalField1053];
         Vec3d localValue2 = this.internalField0724[(this.internalField0228 + this.internalField1053 - 1) % this.internalField1053];
         if (localValue1 != null && localValue2 != null) {
            Vec3d localValue3 = localValue2.subtract(localValue1);
            if (localValue3.lengthSquared() > 0.001) {
               return localValue3.normalize();
            }
         }

         return null;
      }
   }

   public HitParticleEmitter.InternalType0323 internalMethod03713() {
      if (this.internalField0416.isEmpty()) {
         return null;
      } else {
         long localValue1 = System.currentTimeMillis();
         if ((float)(localValue1 - this.internalField0229) < this.internalField1047 * 1000.0F) {
            return this.internalField0227 < this.internalField0416.size() ? this.internalField0416.get(this.internalField0227) : this.internalField0416.get(0);
         } else {
            this.internalField0229 = localValue1;
            if (this.internalField0416.size() > 1) {
               this.internalField0227 = this.internalMethod06587();
            } else {
               this.internalField0227 = 0;
            }

            HitParticleEmitter.InternalType0323 localValue3 = this.internalField0416.get(this.internalField0227);
            localValue3.internalField0227++;
            localValue3.internalField0229 = localValue1;
            return localValue3;
         }
      }
   }

   private int internalMethod06587() {
      float localValue1 = 0.0F;

      for (HitParticleEmitter.InternalType0323 localValue3 : this.internalField0416) {
         localValue1 += localValue3.internalField0205 * localValue3.internalField0206;
      }

      if (localValue1 <= 0.0F) {
         return internalField0362.nextInt(this.internalField0416.size());
      } else {
         float localValue6 = internalField0362.nextFloat() * localValue1;
         float localValue7 = 0.0F;

         for (int localValue4 = 0; localValue4 < this.internalField0416.size(); localValue4++) {
            HitParticleEmitter.InternalType0323 localValue5 = this.internalField0416.get(localValue4);
            localValue7 += localValue5.internalField0205 * localValue5.internalField0206;
            if (localValue6 <= localValue7) {
               return localValue4;
            }
         }

         return this.internalField0416.size() - 1;
      }
   }

   public double[] internalMethod06284() {
      HitParticleEmitter.InternalType0323 localValue1 = this.internalMethod03713();
      return localValue1 == null ? null : new double[]{localValue1.internalField0194, localValue1.internalField0193, localValue1.internalField1045};
   }

   public List<HitParticleEmitter.InternalType0323> internalMethod03750() {
      return new ArrayList<>(this.internalField0416);
   }

   private static double internalMethod06285(double localValue0) {
      localValue0 %= Math.PI * 2;
      if (localValue0 < -Math.PI) {
         localValue0 += Math.PI * 2;
      }

      if (localValue0 > Math.PI) {
         localValue0 -= Math.PI * 2;
      }

      return localValue0;
   }

   public void internalMethod06585() {
      this.internalField0416.clear();
      this.internalField0227 = 0;
      this.internalField0229 = 0L;
      this.internalField0228 = 0;
      this.internalField1053 = 0;
      Arrays.fill(this.internalField0724, null);
      this.internalField0864 = HitParticleEmitter.InternalType0431.internalField0864;
      this.internalField0785 = HitParticleEmitter.InternalType0322.internalField0785;
      this.internalField0863 = HitParticleEmitter.InternalType0430.internalField0862;
      this.internalField1056 = this.internalField1055;
      this.internalField1048 = this.internalField0206;
      this.internalField1047 = this.internalField0205;
   }

   @Generated
   public HitParticleEmitter.InternalType0322 internalMethod03712() {
      return this.internalField0785;
   }

   @Generated
   public HitParticleEmitter.InternalType0430 internalMethod01294() {
      return this.internalField0863;
   }

   @Generated
   public int internalMethod06584() {
      return this.internalField1056;
   }

   @Generated
   public float internalMethod06583() {
      return this.internalField1048;
   }

   public static enum InternalType0322 {
      internalField0785,
      internalField0786,
      internalField1318,
      internalField1320,
      internalField1319;
   }

   public static class InternalType0323 {
      public final double internalField0194;
      public final double internalField0193;
      public final double internalField1045;
      public final float internalField0205;
      public final float internalField0206;
      public final HitParticleEmitter.InternalType0431 internalField0864;
      public int internalField0227 = 0;
      public long internalField0229 = 0L;

      public InternalType0323(double localValue1, double localValue3, double localValue5, float localValue7, float localValue8, HitParticleEmitter.InternalType0431 localValue9) {
         this.internalField0194 = localValue1;
         this.internalField0193 = localValue3;
         this.internalField1045 = localValue5;
         this.internalField0205 = localValue7;
         this.internalField0206 = localValue8;
         this.internalField0864 = localValue9;
      }

      public Vec3d internalMethod01301() {
         return new Vec3d(this.internalField0194, this.internalField0193, this.internalField1045);
      }
   }

   public static enum InternalType0430 {
      internalField0863,
      internalField0862,
      internalField1358,
      internalField1359,
      internalField1360;
   }

   public static enum InternalType0431 {
      internalField0864,
      internalField0865,
      internalField1363,
      internalField1361,
      internalField1362;
   }
}
