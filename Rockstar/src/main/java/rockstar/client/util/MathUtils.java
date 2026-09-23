package rockstar.client.util;


import rockstar.client.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import java.lang.reflect.Field;
import java.security.SecureRandom;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.TrapdoorBlock;
import net.minecraft.client.gui.hud.BossBarHud;
import net.minecraft.client.gui.hud.ClientBossBar;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class MathUtils implements MinecraftClientAccess {
   public static SecureRandom internalField0858 = new SecureRandom();
   private static final double[] internalField0612 = new double[65536];
   private static final DecimalFormatSymbols internalField0301;
   private static final DecimalFormat internalField0560;

   public static double internalMethod04857(double localValue0) {
      int localValue2 = (int)(localValue0 * 10430.378350470453) & 65535;
      return internalField0612[localValue2];
   }

   public static double internalMethod04929(double localValue0) {
      int localValue2 = (int)(localValue0 * 10430.378350470453 + 16384.0) & 65535;
      return internalField0612[localValue2];
   }

   public static float internalMethod05369(float localValue0, float localValue1) {
      float localValue2 = internalField0858.nextFloat();
      float localValue3 = internalField0858.nextFloat();
      return localValue0 + (localValue1 - localValue0) * ((localValue2 + localValue3) / 2.0F);
   }

   public static float internalMethod01703(float localValue0, float localValue1) {
      double localValue2 = internalField0858.nextDouble();
      double localValue4 = internalField0858.nextDouble();
      double localValue6 = internalField0858.nextGaussian() * 0.02F;
      double localValue8 = Math.pow(localValue2, 1.0 + internalField0858.nextDouble() * 0.7);
      double localValue10 = (localValue4 * 0.8 + 0.1) * (Math.log1p(localValue2 * 3.0) * 0.5 + 0.5);
      return (float)(localValue0 + (localValue1 - localValue0) * localValue8 * localValue10 + localValue6);
   }

   public static float internalMethod08503(float localValue0, float localValue1) {
      return localValue0 + (localValue1 - localValue0) * (float)Math.random();
   }

   public static float internalMethod08851(float localValue0, float localValue1) {
      return localValue0 + (localValue1 - localValue0) * internalField0858.nextFloat();
   }

   public static float internalMethod07919(float localValue0, float localValue1) {
      switch (internalField0858.nextInt(4)) {
         case 0:
            return internalMethod05369(localValue0, localValue1);
         case 1:
            return internalMethod01703(localValue0, localValue1);
         case 2:
            return internalMethod08503(localValue0, localValue1);
         default:
            return internalMethod08851(localValue0, localValue1);
      }
   }

   public static float internalMethod08251(float localValue0, float localValue1) {
      float localValue2 = (float)internalField0858.nextGaussian() * localValue1 + localValue0;
      float localValue3 = (float)internalField0858.nextGaussian() * localValue1 + localValue0;
      return (localValue2 + localValue3) / 2.0F;
   }

   public static float internalMethod09450(float localValue0, float localValue1) {
      double localValue2 = internalField0858.nextGaussian() * localValue1 + localValue0;
      double localValue4 = internalField0858.nextGaussian();
      double localValue6 = internalField0858.nextGaussian() * 0.02;
      double localValue8 = Math.pow(Math.abs(localValue4), 1.0 + internalField0858.nextDouble() * 0.7);
      double localValue10 = (Math.abs(localValue4) * 0.8 + 0.1) * (Math.log1p(Math.abs(localValue2 - localValue0) * 3.0) * 0.5 + 0.5);
      return (float)(localValue0 + localValue1 * localValue8 * localValue10 + localValue6);
   }

   public static float internalMethod09614(float localValue0, float localValue1) {
      return (float)internalField0858.nextGaussian() * localValue1 + localValue0;
   }

   public static float internalMethod09410(float localValue0, float localValue1) {
      return (float)internalField0858.nextGaussian() * localValue1 + localValue0;
   }

   public static float internalMethod09297(float localValue0, float localValue1) {
      switch (internalField0858.nextInt(4)) {
         case 0:
            return internalMethod08251(localValue0, localValue1);
         case 1:
            return internalMethod09450(localValue0, localValue1);
         case 2:
            return internalMethod09614(localValue0, localValue1);
         default:
            return internalMethod09410(localValue0, localValue1);
      }
   }

   public static float internalMethod05368(double localValue0, double localValue2) {
      return (float)(localValue0 + (localValue2 - localValue0) * Math.random());
   }

   public static double internalMethod03549(double localValue0, double localValue2, double localValue4, double localValue6, double localValue8) {
      return Math.pow(1.0 - localValue0, 3.0) * localValue2
         + 3.0 * localValue0 * Math.pow(1.0 - localValue0, 2.0) * localValue4
         + 3.0 * Math.pow(localValue0, 2.0) * (1.0 - localValue0) * localValue6
         + Math.pow(localValue0, 3.0) * localValue8;
   }

   public static boolean internalMethod06610(Vec3d localValue0) {
      return internalField0149.world
            .raycast(new RaycastContext(internalField0149.player.getEyePos(), localValue0, ShapeType.COLLIDER, FluidHandling.NONE, internalField0149.player))
            .getType()
         == Type.MISS;
   }

   public static boolean internalMethod05475(Vec3d localValue0) {
      Vec3d localValue1 = internalField0149.player.getEyePos();
      Vec3d localValue2 = localValue0.subtract(localValue1);
      double localValue3 = localValue2.length();
      localValue2 = localValue2.normalize();
      HashSet localValue5 = new HashSet();
      int localValue6 = 0;
      double localValue7 = 0.25;

      for (double localValue9 = 0.0; localValue9 <= localValue3; localValue9 += localValue7) {
         Vec3d localValue11 = localValue1.add(localValue2.multiply(localValue9));
         BlockPos localValue12 = BlockPos.ofFloored(localValue11);
         if (!localValue5.contains(localValue12)) {
            localValue5.add(localValue12);
            BlockState localValue13 = internalField0149.world.getBlockState(localValue12);
            if (!localValue13.isAir()) {
               Block localValue14 = localValue13.getBlock();
               if (!localValue13.isOf(Blocks.GLASS) && !localValue13.isOf(Blocks.GLASS_PANE) && !(localValue13.getBlock() instanceof TrapdoorBlock)) {
                  VoxelShape localValue15 = localValue13.getCollisionShape(internalField0149.world, localValue12);
                  if (!localValue15.isEmpty()) {
                     localValue6++;
                  }
               }
            }
         }
      }

      AtomicBoolean localValue20 = new AtomicBoolean(false);
      BossBarHud localValue10 = internalField0149.inGameHud.getBossBarHud();
      if (localValue10 != null) {
         Class<BossBarHud> localValue21 = BossBarHud.class;

         try {
            Field localValue22 = localValue21.getField("bossBars");
            Map localValue23 = (Map)localValue22.get(localValue10);

            for (UUID localValue25 : (Iterable<UUID>)(Iterable<?>)localValue23.keySet()) {
               ClientBossBar localValue16 = (ClientBossBar)localValue23.get(localValue25);
               List localValue17 = localValue16.getName().getSiblings();
               localValue17.stream().allMatch(localValue1x -> {
                  if (((net.minecraft.text.Text)localValue1x).getString().contains("\ub8f3\ua223\ua203\ub8f2\ua223\ua205")) {
                     localValue20.set(true);
                  }

                  return true;
               });
            }
         } catch (Exception localValue18) {
         }
      }

      return localValue6 <= (localValue20.get() ? 3 : (internalField0149.player.getInventory().getSelectedSlot() == 0 ? 2 : 1));
   }

   public static int internalMethod07594(String localValue0, String localValue1) {
      int localValue2 = localValue0.length();
      int localValue3 = localValue1.length();
      int[] localValue4 = new int[localValue3 + 1];
      int localValue5 = 0;

      while (localValue5 <= localValue3) {
         localValue4[localValue5] = localValue5++;
      }

      for (int localValue10 = 1; localValue10 <= localValue2; localValue10++) {
         int localValue6 = localValue4[0];
         localValue4[0] = localValue10;

         for (int localValue7 = 1; localValue7 <= localValue3; localValue7++) {
            int localValue8 = localValue4[localValue7];
            int localValue9 = localValue0.charAt(localValue10 - 1) == localValue1.charAt(localValue7 - 1) ? 0 : 1;
            localValue4[localValue7] = Math.min(Math.min(localValue4[localValue7] + 1, localValue4[localValue7 - 1] + 1), localValue6 + localValue9);
            localValue6 = localValue8;
         }
      }

      return localValue4[localValue3];
   }

   public static float internalMethod02587(double localValue0, double localValue2, double localValue4) {
      return (float)(localValue0 + (localValue2 - localValue0) * localValue4);
   }

   public static HitResult internalMethod01143(double localValue0, float localValue2, float localValue3, Entity localValue4) {
      Vec3d localValue5 = internalField0149.player.getCameraPosVec(1.0F);
      Vec3d localValue6 = internalMethod06554(localValue3, localValue2);
      Vec3d localValue7 = localValue5.add(localValue6.x * localValue0, localValue6.y * localValue0, localValue6.z * localValue0);
      return internalField0149.world.raycast(new RaycastContext(localValue5, localValue7, ShapeType.OUTLINE, FluidHandling.NONE, localValue4));
   }

   private static boolean internalMethod01764(Vec3d localValue0, Vec3d localValue1, Entity localValue2, Entity localValue3) {
      Box localValue4 = localValue2.getBoundingBox().offset(-localValue2.getX(), -localValue2.getY(), -localValue2.getZ()).offset(EntityPositionTracker.internalMethod01255(localValue2));
      Box localValue5 = localValue2.getBoundingBox();
      return localValue4.raycast(localValue0, localValue1).isPresent()
            && RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class).internalMethod06377().isSelected()
         ? true
         : localValue5.raycast(localValue0, localValue1).isPresent();
   }

   public static BlockHitResult internalMethod03097(Vec3d localValue0, Vec3d localValue1, Entity localValue2, BlockTraceFilter localValue3) {
      if (internalField0149.world != null && !localValue3.internalMethod04282()) {
         Vec3d localValue4 = localValue1.subtract(localValue0);
         double localValue5 = localValue4.lengthSquared();
         if (localValue5 < 1.0E-8) {
            return null;
         } else {
            BlockHitResult localValue7 = internalMethod02180(localValue0, localValue2, localValue3);
            if (localValue7 != null) {
               return localValue7;
            } else {
               Vec3d localValue8 = localValue4.normalize().multiply(0.01);
               Vec3d localValue9 = localValue0;

               for (int localValue10 = 0; localValue10 < 40; localValue10++) {
                  BlockHitResult localValue11 = internalField0149.world.raycast(new RaycastContext(localValue9, localValue1, localValue3.internalMethod07638(), FluidHandling.NONE, localValue2));
                  if (localValue11 == null || localValue11.getType() != Type.BLOCK) {
                     return null;
                  }

                  BlockHitResult localValue12 = localValue11;
                  BlockPos localValue13 = localValue12.getBlockPos();
                  if (!localValue3.internalMethod01474(internalField0149.world, localValue13, internalField0149.world.getBlockState(localValue13))) {
                     return localValue12;
                  }

                  localValue9 = localValue12.getPos().add(localValue8);
                  if (localValue9.squaredDistanceTo(localValue0) >= localValue5) {
                     return null;
                  }
               }

               return null;
            }
         }
      } else {
         return null;
      }
   }

   private static BlockHitResult internalMethod02180(Vec3d localValue0, Entity localValue1, BlockTraceFilter localValue2) {
      if (localValue2.internalMethod07638() != ShapeType.OUTLINE) {
         return null;
      } else {
         BlockPos localValue3 = BlockPos.ofFloored(localValue0);
         BlockState localValue4 = internalField0149.world.getBlockState(localValue3);
         if (!localValue4.isAir() && !localValue2.internalMethod01474(internalField0149.world, localValue3, localValue4)) {
            VoxelShape localValue5 = localValue4.getOutlineShape(internalField0149.world, localValue3, ShapeContext.of(localValue1));
            return localValue5.isEmpty() ? null : new BlockHitResult(localValue0, Direction.UP, localValue3, true);
         } else {
            return null;
         }
      }
   }

   public static boolean internalMethod03785(Vec3d localValue0, Vec3d localValue1, Entity localValue2, BlockTraceFilter localValue3) {
      return internalMethod03097(localValue0, localValue1, localValue2, localValue3) == null;
   }

   public static boolean internalMethod04490(double localValue0, float localValue2, float localValue3, Entity localValue4, Entity localValue5, BlockTraceFilter localValue6) {
      if (localValue5 != null && localValue4 != null && internalField0149.world != null) {
         float localValue7 = internalField0149.getRenderTickCounter().getTickProgress(false);
         Vec3d localValue8 = localValue4.getCameraPosVec(localValue7);
         Vec3d localValue9 = internalMethod06554(localValue3, localValue2);
         Vec3d localValue10 = localValue8.add(localValue9.multiply(localValue0));
         if (!localValue6.internalMethod04282()) {
            Box localValue11 = localValue5.getBoundingBox().offset(-localValue5.getX(), -localValue5.getY(), -localValue5.getZ()).offset(EntityPositionTracker.internalMethod01255(localValue5));
            Box localValue12 = localValue5.getBoundingBox();
            if (localValue12.contains(internalField0149.player.getEyePos())) {
               return true;
            }

            if (localValue11.contains(internalField0149.player.getEyePos())
               && RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class).internalMethod06377().isSelected()) {
               return true;
            }

            BlockHitResult localValue13 = internalMethod03097(localValue8, localValue10, localValue4, localValue6);
            if (localValue13 != null) {
               double localValue14 = localValue13.getPos().distanceTo(localValue8);
               double localValue16 = localValue5.getEyePos().distanceTo(localValue8);
               if (localValue14 < localValue16) {
                  return false;
               }
            }
         } else {
            BlockHitResult localValue18 = internalField0149.world.raycast(new RaycastContext(localValue8, localValue10, ShapeType.COLLIDER, FluidHandling.NONE, localValue4));
            if (localValue18 != null && localValue18.getType() == Type.BLOCK) {
               double localValue19 = localValue18.getPos().distanceTo(localValue8);
               double localValue20 = localValue5.getEyePos().distanceTo(localValue8);
               if (localValue19 < localValue20) {
               }
            }
         }

         return internalMethod01764(localValue8, localValue10, localValue5, internalField0149.player);
      } else {
         return false;
      }
   }

   public static Vec3d internalMethod06554(float localValue0, float localValue1) {
      float localValue2 = -localValue1 * (float) (Math.PI / 180.0) - (float) Math.PI;
      float localValue3 = -localValue0 * (float) (Math.PI / 180.0);
      float localValue4 = MathHelper.cos(localValue2);
      float localValue5 = MathHelper.sin(localValue2);
      float localValue6 = -MathHelper.cos(localValue3);
      float localValue7 = MathHelper.sin(localValue3);
      return new Vec3d(localValue5 * localValue6, localValue7, localValue4 * localValue6);
   }

   public static float internalMethod09441(float localValue0, float localValue1) {
      float localValue2 = (localValue0 - localValue1) % 360.0F;
      if (localValue2 < -180.0F) {
         localValue2 += 360.0F;
      } else if (localValue2 > 180.0F) {
         localValue2 -= 360.0F;
      }

      return localValue2;
   }

   public static String internalMethod07216(String localValue0) {
      localValue0 = localValue0.replaceAll("\\s+", "");
      if (localValue0.isEmpty()) {
         return "";
      } else {
         try {
            double localValue1 = new ExpressionBuilder(localValue0).internalMethod05680().internalMethod06442();
            return String.valueOf(localValue1);
         } catch (IllegalArgumentException localValue3) {
            localValue3.printStackTrace();
            return localValue0;
         }
      }
   }

   public static float internalMethod04858(float localValue0) {
      int localValue1 = (int)Math.floor(localValue0) & 0xFF;
      float localValue2 = localValue0 - (float)Math.floor(localValue0);
      float localValue3 = internalMethod04930(localValue2);
      float localValue4 = internalMethod04859(localValue1);
      float localValue5 = internalMethod04859(localValue1 + 1);
      return internalMethod02589(localValue4, localValue5, localValue3);
   }

   public static int internalMethod05371(int localValue0, int localValue1) {
      if (localValue0 > localValue1) {
         throw new IllegalArgumentException("min must be less than or equal to max");
      } else {
         return localValue0 + internalField0858.nextInt(localValue1 - localValue0 + 1);
      }
   }

   private static float internalMethod04930(float localValue0) {
      return localValue0 * localValue0 * localValue0 * (localValue0 * (localValue0 * 6.0F - 15.0F) + 10.0F);
   }

   public static float internalMethod02589(float localValue0, float localValue1, float localValue2) {
      return localValue0 + (localValue1 - localValue0) * localValue2;
   }

   private static float internalMethod04859(int localValue0) {
      localValue0 = localValue0 ^ 61 ^ localValue0 >> 16;
      localValue0 += localValue0 << 3;
      localValue0 ^= localValue0 >> 4;
      localValue0 *= 668265261;
      localValue0 ^= localValue0 >> 15;
      return (localValue0 & 2147483647) / 2.1474836E9F * 2.0F - 1.0F;
   }

   public static String internalMethod00005(long localValue0) {
      return internalField0560.format(localValue0) + "$";
   }

   @Generated
   private MathUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }

   static {
      for (int localValue0 = 0; localValue0 < 65536; localValue0++) {
         internalField0612[localValue0] = Math.sin(localValue0 * (Math.PI * 2) / 65536.0);
      }

      internalField0301 = new DecimalFormatSymbols(Locale.US);
      internalField0560 = new DecimalFormat("#,###", internalField0301);
   }
}
