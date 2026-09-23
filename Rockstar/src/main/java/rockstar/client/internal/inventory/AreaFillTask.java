package rockstar.client.internal.inventory;







import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ExperienceOrbEntity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import org.jetbrains.annotations.Nullable;

public final class AreaFillTask implements NewtonTask {
   private static final Direction[] internalField0470 = new Direction[]{
      Direction.DOWN, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.UP
   };
   private final BlockPos internalField0352;
   private final BlockPos internalField0351;
   private final Block internalField0792;
   private final Item internalField0152;
   private final int internalField0227;
   private int internalField0228;
   @Nullable
   private BlockPos internalField1131;
   @Nullable
   private GotoPathTask internalField0494;
   private final BlockFaceRotationHelper internalField0904 = new BlockFaceRotationHelper();
   private boolean internalField0277;
   private boolean internalField0276;
   @Nullable
   private String internalField0248;
   private int internalField1053;
   private final Set<BlockPos> internalField0546 = new HashSet<>();
   @Nullable
   private Rotation internalField0118;
   private int internalField1055;

   public AreaFillTask(BlockPos localValue1, BlockPos localValue2, Block localValue3) {
      this.internalField0352 = localValue1;
      this.internalField0351 = localValue2;
      this.internalField0792 = localValue3;
      this.internalField0152 = localValue3.asItem();
      this.internalField0227 = this.internalMethod03616();
   }

   private int internalMethod03616() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.world == null) {
         return 0;
      } else {
         int localValue2 = 0;
         Mutable localValue3 = new Mutable();

         for (int localValue4 = this.internalField0352.getY(); localValue4 <= this.internalField0351.getY(); localValue4++) {
            for (int localValue5 = this.internalField0352.getX(); localValue5 <= this.internalField0351.getX(); localValue5++) {
               for (int localValue6 = this.internalField0352.getZ(); localValue6 <= this.internalField0351.getZ(); localValue6++) {
                  localValue3.set(localValue5, localValue4, localValue6);
                  if (localValue1.world.getBlockState(localValue3).getBlock() != this.internalField0792) {
                     localValue2++;
                  }
               }
            }
         }

         return localValue2;
      }
   }

   private boolean internalMethod03573(MinecraftClient localValue1, BlockPos localValue2, BlockState localValue3) {
      if (localValue3.getBlock() == this.internalField0792) {
         return false;
      } else {
         return !localValue3.isAir() && !localValue3.isReplaceable() ? localValue3.getHardness(localValue1.world, localValue2) >= 0.0F : false;
      }
   }

   private boolean internalMethod05381(MinecraftClient localValue1, BlockPos localValue2) {
      BlockState localValue3 = localValue1.world.getBlockState(localValue2);
      if (localValue3.getBlock() == this.internalField0792) {
         return false;
      } else {
         return this.internalMethod03573(localValue1, localValue2, localValue3) ? true : (localValue3.isAir() || localValue3.isReplaceable()) && !this.internalMethod08334(localValue1, localValue2);
      }
   }

   @Override
   public String internalMethod01129() {
      return "fill";
   }

   @Override
   public String internalMethod05788() {
      if (this.internalField0277) {
         return "\u043f\u0430\u0443\u0437\u0430";
      } else if (this.internalField0276) {
         return "\u0433\u043e\u0442\u043e\u0432\u043e";
      } else {
         String localValue1 = this.internalField0228 + "/" + this.internalField0227;
         if (this.internalField1131 != null && this.internalField0494 == null) {
            return "\u0440\u0430\u0431\u043e\u0442\u0430\u0435\u043c " + this.internalField1131 + " (" + localValue1 + ")";
         } else {
            return this.internalField0494 != null
               ? "\u0438\u0434\u0451\u043c \u043a " + this.internalField1131 + " (" + localValue1 + ")"
               : "\u0437\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u0435 " + localValue1;
         }
      }
   }

   @Override
   public boolean internalMethod04087() {
      if (this.internalField0277) {
         return false;
      } else if (this.internalField0276) {
         return true;
      } else {
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         if (localValue1.player != null && localValue1.world != null && localValue1.interactionManager != null) {
            SilentRotationHelper.internalMethod06440();
            if (this.internalField0494 != null) {
               boolean localValue5 = this.internalField0494.internalMethod04087();
               if (localValue5) {
                  this.internalField0494 = null;
                  if (this.internalField1131 != null) {
                     double localValue3 = localValue1.player.getEyePos().distanceTo(Vec3d.ofCenter(this.internalField1131));
                     if (localValue3 > 5.0) {
                        this.internalField0546.add(this.internalField1131);
                        this.internalField1131 = null;
                     }
                  }
               }

               return false;
            } else if (this.internalField1131 != null) {
               return this.internalMethod07429(localValue1, this.internalField1131);
            } else {
               BlockPos localValue2 = this.internalMethod04575(localValue1);
               if (localValue2 == null) {
                  ChatMessageHelper.internalMethod00196(
                     "\u0417\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u0435 \u0437\u0430\u0432\u0435\u0440\u0448\u0435\u043d\u043e ("
                        + this.internalField0228
                        + " \u0431\u043b\u043e\u043a\u043e\u0432)"
                  );
                  this.internalMethod04086();
                  this.internalField0276 = true;
                  return true;
               } else {
                  this.internalField1131 = localValue2;
                  this.internalMethod08587();
                  return false;
               }
            }
         } else {
            return false;
         }
      }
   }

   private boolean internalMethod07429(MinecraftClient localValue1, BlockPos localValue2) {
      Vec3d localValue3 = Vec3d.ofCenter(localValue2);
      double localValue4 = localValue1.player.getEyePos().distanceTo(localValue3);
      if (localValue4 > 5.0) {
         this.internalMethod08603();
         this.internalMethod08587();
         this.internalMethod05380(localValue1, localValue2);
         return false;
      } else {
         BlockState localValue6 = localValue1.world.getBlockState(localValue2);
         if (localValue6.getBlock() == this.internalField0792) {
            this.internalField0228++;
            this.internalField1131 = null;
            this.internalMethod08603();
            this.internalMethod08587();
            return false;
         } else if (this.internalMethod03573(localValue1, localValue2, localValue6)) {
            return this.internalMethod00781(localValue1, localValue2, localValue6);
         } else if (this.internalMethod08334(localValue1, localValue2)) {
            this.internalField1131 = null;
            this.internalMethod08603();
            this.internalMethod08587();
            return false;
         } else if (this.internalMethod08750(localValue1, localValue2)) {
            this.internalMethod08603();
            this.internalMethod08587();
            BlockPos localValue9 = this.internalMethod05120(localValue1, localValue2);
            this.internalField1053++;
            if (localValue9 != null && this.internalField1053 < 4) {
               this.internalField0494 = new GotoPathTask(new BlockPositionGoal(localValue9));
               return false;
            } else {
               this.internalField0546.add(localValue2);
               this.internalField1131 = null;
               this.internalField1053 = 0;
               return false;
            }
         } else {
            AreaFillTask.InternalType0245 localValue7 = this.internalMethod04172(localValue1, localValue2);
            if (localValue7 == null) {
               this.internalField1053++;
               if (this.internalField1053 >= 3) {
                  this.internalField0546.add(localValue2);
                  this.internalField1131 = null;
                  this.internalField1053 = 0;
                  return false;
               } else {
                  this.internalField0494 = new GotoPathTask(new RadiusGoal(localValue2, 1));
                  return false;
               }
            } else {
               this.internalField1053 = 0;
               if (!this.internalMethod00800(localValue1)) {
                  ChatMessageHelper.internalMethod06835(
                     "\u041d\u0435\u0442 \u0431\u043b\u043e\u043a\u0430 "
                        + this.internalField0792
                        + " \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435"
                  );
                  this.internalField0248 = "\u043d\u0435\u0442 \u0431\u043b\u043e\u043a\u0430 \u0432 \u0438\u043d\u0432\u0435\u043d\u0442\u0430\u0440\u0435";
                  this.internalMethod04086();
                  this.internalField0276 = true;
                  return true;
               } else if (!this.internalMethod04339(localValue7.internalField0283)) {
                  return false;
               } else {
                  BlockHitResult localValue8 = new BlockHitResult(localValue7.internalField0283, localValue7.internalField0150, localValue7.internalField0352, false);
                  localValue1.interactionManager.interactBlock(localValue1.player, Hand.MAIN_HAND, localValue8);
                  localValue1.player.swingHand(Hand.MAIN_HAND);
                  return false;
               }
            }
         }
      }
   }

   private boolean internalMethod00781(MinecraftClient localValue1, BlockPos localValue2, BlockState localValue3) {
      if (!internalMethod09814(localValue1, localValue2)) {
         this.internalMethod08603();
         this.internalField1053++;
         if (this.internalField1053 >= 3) {
            this.internalField0546.add(localValue2);
            this.internalField1131 = null;
            this.internalField1053 = 0;
            return false;
         } else {
            this.internalField0494 = new GotoPathTask(new RadiusGoal(localValue2, 1));
            return false;
         }
      } else {
         this.internalField1053 = 0;
         BestToolSelector.internalMethod04395(localValue3);
         Direction localValue4 = this.internalMethod06445(localValue1, localValue2);
         if (!this.internalField0904.internalMethod05481(localValue2, localValue4)) {
            return false;
         } else {
            localValue1.interactionManager.updateBlockBreakingProgress(localValue2, localValue4);
            localValue1.player.swingHand(localValue1.player.getActiveHand());
            return false;
         }
      }
   }

   @Nullable
   private BlockPos internalMethod04575(MinecraftClient localValue1) {
      if (localValue1.player == null) {
         return null;
      } else {
         double localValue2 = localValue1.player.getX();
         double localValue4 = localValue1.player.getZ();
         Mutable localValue6 = new Mutable();

         for (int localValue7 = this.internalField0352.getY(); localValue7 <= this.internalField0351.getY(); localValue7++) {
            BlockPos localValue8 = null;
            double localValue9 = Double.MAX_VALUE;

            for (int localValue11 = this.internalField0352.getX(); localValue11 <= this.internalField0351.getX(); localValue11++) {
               for (int localValue12 = this.internalField0352.getZ(); localValue12 <= this.internalField0351.getZ(); localValue12++) {
                  localValue6.set(localValue11, localValue7, localValue12);
                  if (!this.internalField0546.contains(localValue6) && this.internalMethod05381(localValue1, localValue6)) {
                     double localValue13 = localValue11 + 0.5 - localValue2;
                     double localValue15 = localValue12 + 0.5 - localValue4;
                     double localValue17 = localValue13 * localValue13 + localValue15 * localValue15;
                     if (localValue17 < localValue9) {
                        localValue9 = localValue17;
                        localValue8 = localValue6.toImmutable();
                     }
                  }
               }
            }

            if (localValue8 != null) {
               return localValue8;
            }
         }

         return null;
      }
   }

   @Nullable
   private AreaFillTask.InternalType0245 internalMethod04172(MinecraftClient localValue1, BlockPos localValue2) {
      if (localValue1.world != null && localValue1.player != null) {
         Vec3d localValue3 = localValue1.player.getEyePos();

         for (Direction localValue7 : internalField0470) {
            BlockPos localValue8 = localValue2.offset(localValue7);
            Direction localValue9 = localValue7.getOpposite();
            BlockState localValue10 = localValue1.world.getBlockState(localValue8);
            if (localValue10.isSideSolidFullSquare(localValue1.world, localValue8, localValue9)) {
               Vec3d localValue11 = Vec3d.ofCenter(localValue8).add(localValue9.getOffsetX() * 0.5, localValue9.getOffsetY() * 0.5, localValue9.getOffsetZ() * 0.5);
               if (!(localValue3.distanceTo(localValue11) > 5.0) && internalMethod03965(localValue1, localValue11, localValue8)) {
                  return new AreaFillTask.InternalType0245(localValue8, localValue9, localValue11);
               }
            }
         }

         return null;
      } else {
         return null;
      }
   }

   private void internalMethod05380(MinecraftClient localValue1, BlockPos localValue2) {
      BlockPos localValue3 = this.internalMethod05120(localValue1, localValue2);
      this.internalField0494 = localValue3 != null ? new GotoPathTask(new BlockPositionGoal(localValue3)) : new GotoPathTask(new RadiusGoal(localValue2, 2));
   }

   private boolean internalMethod08334(MinecraftClient localValue1, BlockPos localValue2) {
      if (localValue1.world == null) {
         return false;
      } else {
         Box localValue3 = new Box(localValue2);

         for (Entity localValue5 : localValue1.world.getOtherEntities(localValue1.player, localValue3)) {
            if (localValue5.isAlive() && !localValue5.isSpectator() && !(localValue5 instanceof ItemEntity) && !(localValue5 instanceof ExperienceOrbEntity)) {
               return true;
            }
         }

         return false;
      }
   }

   private boolean internalMethod08750(MinecraftClient localValue1, BlockPos localValue2) {
      return localValue1.player == null ? false : localValue1.player.getBoundingBox().intersects(new Box(localValue2));
   }

   @Nullable
   private BlockPos internalMethod05120(MinecraftClient localValue1, BlockPos localValue2) {
      if (localValue1.player != null && localValue1.world != null) {
         Vec3d localValue3 = localValue1.player.getEntityPos();
         BlockPos localValue4 = null;
         double localValue5 = Double.MAX_VALUE;
         Direction[] localValue7 = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};

         for (Direction localValue11 : localValue7) {
            BlockPos localValue12 = localValue2.offset(localValue11);

            for (BlockPos localValue16 : new BlockPos[]{localValue12, localValue12.up()}) {
               if (this.internalMethod08472(localValue1, localValue16)) {
                  double localValue17 = localValue16.getSquaredDistance(localValue3.x, localValue3.y, localValue3.z);
                  if (localValue17 < localValue5) {
                     localValue5 = localValue17;
                     localValue4 = localValue16;
                  }
               }
            }
         }

         return localValue4;
      } else {
         return null;
      }
   }

   private boolean internalMethod08472(MinecraftClient localValue1, BlockPos localValue2) {
      if (localValue1.world == null) {
         return false;
      } else if (this.internalMethod08883(localValue1, localValue2) && this.internalMethod08883(localValue1, localValue2.up())) {
         BlockPos localValue3 = localValue2.down();
         return localValue1.world.getBlockState(localValue3).isSideSolidFullSquare(localValue1.world, localValue3, Direction.UP);
      } else {
         return false;
      }
   }

   private boolean internalMethod08883(MinecraftClient localValue1, BlockPos localValue2) {
      return localValue1.world.getBlockState(localValue2).getCollisionShape(localValue1.world, localValue2).isEmpty();
   }

   private static boolean internalMethod03965(MinecraftClient localValue0, Vec3d localValue1, BlockPos localValue2) {
      if (localValue0.world != null && localValue0.player != null) {
         RaycastContext localValue3 = new RaycastContext(localValue0.player.getEyePos(), localValue1, ShapeType.COLLIDER, FluidHandling.NONE, localValue0.player);
         BlockHitResult localValue4 = localValue0.world.raycast(localValue3);
         return localValue4.getType() != Type.BLOCK || localValue4.getBlockPos().equals(localValue2);
      } else {
         return false;
      }
   }

   private static boolean internalMethod09814(MinecraftClient localValue0, BlockPos localValue1) {
      if (localValue0.world != null && localValue0.player != null) {
         Vec3d localValue2 = localValue0.player.getEyePos();
         Vec3d localValue3 = Vec3d.ofCenter(localValue1);
         Vec3d[] localValue4 = new Vec3d[]{
            localValue3,
            localValue3.add(0.49, 0.0, 0.0),
            localValue3.add(-0.49, 0.0, 0.0),
            localValue3.add(0.0, 0.49, 0.0),
            localValue3.add(0.0, -0.49, 0.0),
            localValue3.add(0.0, 0.0, 0.49),
            localValue3.add(0.0, 0.0, -0.49)
         };

         for (Vec3d localValue8 : localValue4) {
            RaycastContext localValue9 = new RaycastContext(localValue2, localValue8, ShapeType.COLLIDER, FluidHandling.NONE, localValue0.player);
            BlockHitResult localValue10 = localValue0.world.raycast(localValue9);
            if (localValue10.getType() != Type.BLOCK) {
               return true;
            }

            if (localValue10.getBlockPos().equals(localValue1)) {
               return true;
            }
         }

         return false;
      } else {
         return false;
      }
   }

   private Direction internalMethod06445(MinecraftClient localValue1, BlockPos localValue2) {
      Vec3d localValue3 = localValue1.player.getEyePos();
      Direction localValue4 = Direction.UP;
      double localValue5 = -1.0;

      for (Direction localValue10 : Direction.values()) {
         Vec3d localValue11 = Vec3d.ofCenter(localValue2).add(localValue10.getOffsetX() * 0.5, localValue10.getOffsetY() * 0.5, localValue10.getOffsetZ() * 0.5);
         Vec3d localValue12 = localValue11.subtract(localValue3).normalize();
         double localValue13 = localValue12.x * localValue10.getOffsetX() + localValue12.y * localValue10.getOffsetY() + localValue12.z * localValue10.getOffsetZ();
         double localValue15 = -localValue13;
         if (localValue15 > localValue5) {
            localValue5 = localValue15;
            localValue4 = localValue10;
         }
      }

      return localValue4;
   }

   private boolean internalMethod04339(Vec3d localValue1) {
      Rotation localValue2 = RotationUtils.internalMethod05580(localValue1);
      if (this.internalField0118 == null || this.internalField0118.internalMethod00735(localValue2) > 0.5F) {
         this.internalField0118 = localValue2;
         this.internalField1055 = 0;
      }

      NewtonCoreManager.internalMethod00114()
         .internalMethod03241()
         .internalMethod00418(localValue2, RotationBehavior.internalField1003, 180.0F, 180.0F, 180.0F, RotationPriority.internalField1012);
      Rotation localValue3 = NewtonCoreManager.internalMethod00114().internalMethod03241().internalMethod09074();
      if (localValue3.internalMethod00735(localValue2) <= 1.0F) {
         this.internalField1055++;
         return this.internalField1055 >= 1;
      } else {
         return false;
      }
   }

   private void internalMethod08587() {
      this.internalField0118 = null;
      this.internalField1055 = 0;
   }

   private void internalMethod08603() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.interactionManager != null) {
         localValue1.interactionManager.cancelBlockBreaking();
      }
   }

   private boolean internalMethod00800(MinecraftClient localValue1) {
      ClientPlayerEntity localValue2 = localValue1.player;
      if (localValue2 != null && this.internalField0152 != Items.AIR) {
         PlayerInventory localValue3 = localValue2.getInventory();
         if (localValue3.getStack(localValue3.getSelectedSlot()).getItem() == this.internalField0152) {
            return true;
         } else {
            for (int localValue4 = 0; localValue4 < 9; localValue4++) {
               if (localValue3.getStack(localValue4).getItem() == this.internalField0152) {
                  BestToolSelector.internalMethod00041(localValue4);
                  return true;
               }
            }

            for (int localValue5 = 9; localValue5 < 36; localValue5++) {
               if (localValue3.getStack(localValue5).getItem() == this.internalField0152) {
                  localValue1.interactionManager.clickSlot(localValue2.currentScreenHandler.syncId, localValue5, localValue3.getSelectedSlot(), SlotActionType.SWAP, localValue2);
                  return true;
               }
            }

            return false;
         }
      } else {
         return false;
      }
   }

   @Override
   public void internalMethod04086() {
      if (this.internalField0494 != null) {
         this.internalField0494.internalMethod04086();
         this.internalField0494 = null;
      }

      this.internalMethod08603();
      MovementInputController localValue1 = NewtonCoreManager.internalMethod00114().internalMethod00183();
      localValue1.internalMethod01287();
      this.internalField1131 = null;
      this.internalMethod08587();
   }

   @Override
   public void internalMethod04089() {
      this.internalField0277 = true;
      if (this.internalField0494 != null) {
         this.internalField0494.internalMethod04089();
      }

      this.internalMethod08603();
      NewtonCoreManager.internalMethod00114().internalMethod00183().internalMethod01287();
   }

   @Override
   public void internalMethod08146() {
      this.internalField0277 = false;
      if (this.internalField0494 != null) {
         this.internalField0494.internalMethod08146();
      }
   }

   @Override
   public boolean internalMethod04090() {
      return this.internalField0277;
   }

   @Override
   public boolean internalMethod08147() {
      return this.internalField0276 && this.internalField0248 == null;
   }

   @Nullable
   @Override
   public String internalMethod09095() {
      return this.internalField0248;
   }

   static final class InternalType0245 {
      final BlockPos internalField0352;
      final Direction internalField0150;
      final Vec3d internalField0283;

      InternalType0245(BlockPos localValue1, Direction localValue2, Vec3d localValue3) {
         this.internalField0352 = localValue1;
         this.internalField0150 = localValue2;
         this.internalField0283 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0245[support=" + this.internalField0352 + ", clickFace=" + this.internalField0150 + ", hitVec=" + this.internalField0283 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0352);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0150);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0283);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         AreaFillTask.InternalType0245 other = (AreaFillTask.InternalType0245) localValue1;
         return java.util.Objects.equals(this.internalField0352, other.internalField0352)
            && java.util.Objects.equals(this.internalField0150, other.internalField0150)
            && java.util.Objects.equals(this.internalField0283, other.internalField0283);
      }

      public BlockPos internalMethod07442() {
         return this.internalField0352;
      }

      public Direction internalMethod07109() {
         return this.internalField0150;
      }

      public Vec3d internalMethod06173() {
         return this.internalField0283;
      }
   }
}
