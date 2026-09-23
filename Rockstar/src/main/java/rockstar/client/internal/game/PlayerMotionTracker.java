package rockstar.client.internal.game;




import rockstar.client.util.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import lombok.Generated;
import net.minecraft.block.BlockState;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;

public class PlayerMotionTracker implements MinecraftClientAccess {
   private Vec3d internalField0283;
   private Vec3d internalField0282 = Vec3d.ZERO;
   private float internalField1047 = MathUtils.internalMethod05368(0.0, 360.0);
   private float internalField1049;
   private float internalField1046;
   private int internalField0227 = 50;
   private final AnimatedFloatValue internalField0815 = new AnimatedFloatValue();
   private final AnimatedFloatValue internalField0816 = new AnimatedFloatValue();
   private final AnimatedFloatValue internalField1332 = new AnimatedFloatValue();
   private final AnimatedFloatValue internalField1331 = new AnimatedFloatValue();
   private final AnimatedFloatValue internalField1330 = new AnimatedFloatValue();
   private final AnimatedFloatValue internalField1329 = new AnimatedFloatValue();
   private boolean internalField0277;
   private final Stopwatch internalField0519 = new Stopwatch();
   private final Stopwatch internalField0518 = new Stopwatch();
   private boolean internalField0276;
   private boolean internalField1099;
   private final Stopwatch internalField1189 = new Stopwatch();
   public float internalField0205;
   public float internalField0206;
   public float internalField1048;
   private PlayerEntity internalField0457;

   public void internalMethod07574() {
      if (this.internalField0457 != null) {
         Vec3d localValue1 = this.internalField0457.getEntityPos();
         if (this.internalField0283 == null || this.internalField0283.distanceTo(localValue1) > 10.0) {
            this.internalField0283 = localValue1;
            this.internalField0815.internalMethod02407((float)this.internalField0283.x, 1);
            this.internalField0816.internalMethod02407((float)this.internalField0283.y, 1);
            this.internalField1332.internalMethod02407((float)this.internalField0283.z, 1);
         }

         boolean localValue2 = this.internalMethod06751(this.internalField0283.x, this.internalField0283.y - 0.1, this.internalField0283.z);
         boolean localValue3 = this.internalMethod06751(this.internalField0283.x, this.internalField0283.y + 0.11, this.internalField0283.z);
         if (localValue2) {
            this.internalField0282 = new Vec3d(this.internalField0282.x, 0.0, this.internalField0282.z);
            if (localValue3) {
               this.internalField0282 = new Vec3d(this.internalField0282.x, 0.42, this.internalField0282.z);
            }
         } else {
            this.internalField0282 = this.internalField0282.add(0.0, -0.08, 0.0);
         }

         Vec3d localValue4 = this.internalField0283.add(this.internalField0282);
         if (this.internalMethod06751(localValue4.x, localValue4.y - 0.01, localValue4.z) && this.internalField0282.y < 0.0) {
            BlockPos localValue5 = BlockPos.ofFloored(localValue4.x, localValue4.y - 0.1, localValue4.z);
            localValue4 = new Vec3d(localValue4.x, localValue5.getY() + 1, localValue4.z);
            this.internalField0282 = new Vec3d(this.internalField0282.x, 0.0, this.internalField0282.z);
         }

         LivingEntity localValue6 = RockstarClient.getInstance().internalMethod04463().internalMethod04526() instanceof LivingEntity localValue7 ? localValue7 : null;
         this.internalField0276 = localValue6 != null && this.internalField0457 instanceof ClientPlayerEntity;
         if (this.internalField0276) {
            Box localValue12 = new Box(this.internalMethod01179().subtract(0.4, 0.0, 0.4), this.internalMethod01179().add(0.4, 0.4, 0.4));
            Box localValue8 = localValue6.getBoundingBox().expand(-0.1F, 0.0, -0.1F);
            this.internalField0282 = this.internalField0282.add(localValue6.getEntityPos().subtract(localValue4).normalize().multiply(0.3));
            boolean localValue9 = localValue12.maxX > localValue8.minX
               && localValue12.maxY > localValue8.minY
               && localValue12.maxZ > localValue8.minZ
               && localValue12.minX < localValue8.maxX
               && localValue12.minY < localValue8.maxY
               && localValue12.minZ < localValue8.maxZ;
            if (localValue9) {
               this.internalField0282 = this.internalField0282.multiply(-1.0, 1.0, -1.0);
               this.internalField1099 = true;
               this.internalField1189.internalMethod00701();
            }

            if (localValue2 && this.internalField0518.internalMethod02365(400L)) {
               this.internalField0282 = new Vec3d(this.internalField0282.x, 0.35, this.internalField0282.z);
               localValue4 = localValue4.add(0.0, 0.35, 0.0);
               this.internalField0518.internalMethod00701();
            }
         } else if (localValue4.distanceTo(localValue1) > 2.0) {
            this.internalField0282 = this.internalField0282.add(localValue1.subtract(localValue4).normalize().multiply(0.1));
         }

         if (this.internalField1189.internalMethod02365(500L)) {
            this.internalField1099 = false;
         }

         this.internalMethod07181(localValue6);
         this.internalField0283 = localValue4;
         if (this.internalField0283.distanceTo(localValue1) < 0.1F) {
            this.internalField1047 = MathUtils.internalMethod05368(0.0, 360.0);
            double localValue13 = -Math.sin(Math.toRadians(this.internalField1047)) * 0.1;
            double localValue14 = Math.cos(Math.toRadians(this.internalField1047)) * 0.1;
            this.internalField0282 = this.internalField0282.add(localValue13, 0.0, localValue14);
         }

         this.internalField0282 = new Vec3d(this.internalField0282.x * 0.9, this.internalField0282.y, this.internalField0282.z * 0.9);
         this.internalField0227 = 150;
         this.internalField0815.internalMethod02407((float)this.internalField0283.x, this.internalField0227);
         this.internalField0816.internalMethod02407((float)this.internalField0283.y, this.internalField0227);
         this.internalField1332.internalMethod02407((float)this.internalField0283.z, this.internalField0227);
         this.internalMethod07609();
         if (Math.abs(this.internalField0283.x - this.internalField0815.internalMethod01101()) > 0.1F
            || Math.abs(this.internalField0283.z - this.internalField1332.internalMethod01101()) > 0.1F) {
            this.internalField0519.internalMethod00701();
         }

         this.internalField0277 = this.internalField0519.internalMethod02365(1000L);
      }
   }

   private void internalMethod07181(LivingEntity localValue1) {
      Vec3d localValue2 = this.internalField0457.getEntityPos().add(0.0, this.internalField0457.getEyeHeight(this.internalField0457.getPose()), 0.0);
      if (localValue1 != null && this.internalField0457 instanceof ClientPlayerEntity) {
         localValue2 = localValue1.getEntityPos().add(0.0, localValue1.getEyeHeight(localValue1.getPose()), 0.0);
         Vec3d localValue17 = this.internalMethod01179();
         double localValue4 = localValue2.x - localValue17.x;
         double localValue6 = localValue2.z - localValue17.z;
         this.internalField1049 = (float)Math.toDegrees(Math.atan2(-localValue4, localValue6));
      } else if (Math.abs(this.internalField0282.x) > 0.01 || Math.abs(this.internalField0282.z) > 0.01) {
         double localValue3 = Math.toDegrees(Math.atan2(-this.internalField0282.x, this.internalField0282.z));
         this.internalField1049 = (float)localValue3;
      }

      if (this.internalField0457 instanceof ClientPlayerEntity) {
         localValue2 = internalField0149.gameRenderer.getCamera().getCameraPos();
      }

      float localValue18 = MathHelper.wrapDegrees(this.internalField1049 - this.internalField1046);
      this.internalField1046 += localValue18 * 0.3F;
      this.internalField1329.internalMethod02407(this.internalField1046, 80);
      Vec3d localValue19 = this.internalMethod01179();
      double localValue5 = localValue2.x - localValue19.x;
      double localValue7 = localValue2.y - localValue19.y;
      double localValue9 = localValue2.z - localValue19.z;
      double localValue11 = Math.sqrt(localValue5 * localValue5 + localValue9 * localValue9);
      float localValue13 = (float)Math.toDegrees(Math.atan2(-localValue5, localValue9));
      float localValue14 = (float)(-Math.toDegrees(Math.atan2(localValue7, localValue11)));
      float localValue15 = MathHelper.wrapDegrees(localValue13 - this.internalField1329.internalMethod01101());
      float localValue16 = this.internalField0277 ? 90.0F : 70.0F;
      localValue15 = MathHelper.clamp(localValue15, -localValue16, localValue16);
      localValue14 = MathHelper.clamp(localValue14, -30.0F, 30.0F);
      this.internalField1331.internalMethod02407(localValue15, 100);
      this.internalField1330.internalMethod02407(localValue14, 100);
   }

   public void internalMethod07609() {
      this.internalField0205 = this.internalField0206;
      double localValue1 = this.internalField0815.internalMethod01101() - this.internalField0283.x;
      double localValue3 = 0.0;
      double localValue5 = this.internalField1332.internalMethod01101() - this.internalField0283.z;
      float localValue7 = MathHelper.sqrt((float)(localValue1 * localValue1 + localValue3 * localValue3 + localValue5 * localValue5)) * 4.0F;
      if (localValue7 > 1.0F) {
         localValue7 = 1.0F;
      }

      this.internalField0206 = this.internalField0206 + (localValue7 - this.internalField0206) * 0.4F;
      this.internalField1048 = this.internalField1048 + this.internalField0206;
   }

   private boolean internalMethod06751(double localValue1, double localValue3, double localValue5) {
      if (internalField0149.world == null) {
         return false;
      } else {
         BlockPos localValue7 = BlockPos.ofFloored(localValue1, localValue3, localValue5);
         BlockState localValue8 = internalField0149.world.getBlockState(localValue7);
         return localValue8.isAir() ? false : !localValue8.getCollisionShape(internalField0149.world, localValue7).isEmpty();
      }
   }

   public float internalMethod07573() {
      return this.internalField1329.internalMethod01101();
   }

   public float internalMethod07608() {
      return this.internalField1331.internalMethod01101();
   }

   public float internalMethod09042() {
      return this.internalField1330.internalMethod01101();
   }

   public Vec3d internalMethod01179() {
      return new Vec3d(this.internalField0815.internalMethod01101(), this.internalField0816.internalMethod01101(), this.internalField1332.internalMethod01101());
   }

   @Generated
   public boolean internalMethod07575() {
      return this.internalField0277;
   }

   @Generated
   public boolean internalMethod07610() {
      return this.internalField0276;
   }

   @Generated
   public boolean internalMethod09043() {
      return this.internalField1099;
   }

   @Generated
   public void internalMethod00286(PlayerEntity localValue1) {
      this.internalField0457 = localValue1;
   }
}
