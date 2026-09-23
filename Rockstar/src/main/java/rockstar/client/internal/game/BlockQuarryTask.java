package rockstar.client.internal.game;







import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.Generated;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
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

public final class BlockQuarryTask implements NewtonTask {
   private final Block internalField0792;
   private final Set<Item> internalField0546;
   @Nullable
   private BlockPos internalField0352;
   @Nullable
   private GotoPathTask internalField0494;
   private boolean internalField0277;
   private boolean internalField0276;
   private final BlockFaceRotationHelper internalField0904 = new BlockFaceRotationHelper();
   private boolean internalField1099;
   private boolean internalField1100;
   private int internalField0228;
   private final Set<BlockPos> internalField0545 = new HashSet<>();
   private final Set<Integer> internalField1200 = new HashSet<>();
   @Nullable
   private Integer internalField0586;

   public BlockQuarryTask(Block localValue1) {
      this.internalField0792 = localValue1;
      this.internalField0546 = internalMethod03321(localValue1);
   }

   private static Set<Item> internalMethod03321(Block localValue0) {
      HashSet localValue1 = new HashSet();
      Item localValue2 = localValue0.asItem();
      if (localValue2 != Items.AIR) {
         localValue1.add(localValue2);
      }

      if (localValue0 == Blocks.STONE) {
         localValue1.add(Items.COBBLESTONE);
      } else if (localValue0 == Blocks.DEEPSLATE) {
         localValue1.add(Items.COBBLED_DEEPSLATE);
      } else if (localValue0 == Blocks.GRASS_BLOCK || localValue0 == Blocks.DIRT_PATH || localValue0 == Blocks.FARMLAND) {
         localValue1.add(Items.DIRT);
      } else if (localValue0 == Blocks.COAL_ORE || localValue0 == Blocks.DEEPSLATE_COAL_ORE) {
         localValue1.add(Items.COAL);
      } else if (localValue0 == Blocks.IRON_ORE || localValue0 == Blocks.DEEPSLATE_IRON_ORE) {
         localValue1.add(Items.RAW_IRON);
      } else if (localValue0 == Blocks.COPPER_ORE || localValue0 == Blocks.DEEPSLATE_COPPER_ORE) {
         localValue1.add(Items.RAW_COPPER);
      } else if (localValue0 == Blocks.GOLD_ORE || localValue0 == Blocks.DEEPSLATE_GOLD_ORE) {
         localValue1.add(Items.RAW_GOLD);
      } else if (localValue0 == Blocks.NETHER_GOLD_ORE) {
         localValue1.add(Items.GOLD_NUGGET);
      } else if (localValue0 == Blocks.NETHER_QUARTZ_ORE) {
         localValue1.add(Items.QUARTZ);
      } else if (localValue0 == Blocks.DIAMOND_ORE || localValue0 == Blocks.DEEPSLATE_DIAMOND_ORE) {
         localValue1.add(Items.DIAMOND);
      } else if (localValue0 == Blocks.EMERALD_ORE || localValue0 == Blocks.DEEPSLATE_EMERALD_ORE) {
         localValue1.add(Items.EMERALD);
      } else if (localValue0 == Blocks.LAPIS_ORE || localValue0 == Blocks.DEEPSLATE_LAPIS_ORE) {
         localValue1.add(Items.LAPIS_LAZULI);
      } else if (localValue0 == Blocks.REDSTONE_ORE || localValue0 == Blocks.DEEPSLATE_REDSTONE_ORE) {
         localValue1.add(Items.REDSTONE);
      }

      return localValue1;
   }

   @Override
   public String internalMethod01129() {
      return "mine " + Block.getRawIdFromState(this.internalField0792.getDefaultState());
   }

   @Override
   public String internalMethod05788() {
      if (this.internalField1099) {
         return "\u043f\u0430\u0443\u0437\u0430";
      } else if (this.internalField0276) {
         return "\u043a\u043e\u043f\u0430\u0435\u043c " + this.internalField0352;
      } else if (this.internalField0277) {
         return "\u043f\u043e\u0434\u0431\u0438\u0440\u0430\u0435\u043c \u0434\u0440\u043e\u043f\u044b";
      } else {
         return this.internalField0494 != null
            ? "\u0438\u0434\u0451\u043c \u043a " + this.internalField0352 + " (" + this.internalField0494.internalMethod05788() + ")"
            : "\u0438\u0449\u0435\u043c \u0431\u043b\u043e\u043a";
      }
   }

   @Override
   public boolean internalMethod04087() {
      if (this.internalField1099) {
         return false;
      } else if (this.internalField1100) {
         return true;
      } else {
         MinecraftClient localValue1 = MinecraftClient.getInstance();
         if (localValue1.player != null && localValue1.world != null && localValue1.interactionManager != null) {
            SilentRotationHelper.internalMethod06440();
            if (this.internalField0276 && this.internalField0352 != null) {
               if (localValue1.world.getBlockState(this.internalField0352).getBlock() != this.internalField0792) {
                  this.internalField0276 = false;
                  this.internalField0352 = null;
                  this.internalMethod08564();
                  return false;
               } else {
                  Vec3d localValue7 = Vec3d.ofCenter(this.internalField0352);
                  double localValue11 = localValue1.player.getEyePos().distanceTo(localValue7);
                  if (localValue11 > 5.0) {
                     this.internalField0276 = false;
                     this.internalMethod08564();
                     this.internalField0494 = new GotoPathTask(new RadiusGoal(this.internalField0352, 2));
                     this.internalField0277 = false;
                     return false;
                  } else if (!internalMethod04497(localValue1, this.internalField0352)) {
                     this.internalField0276 = false;
                     this.internalMethod08564();
                     this.internalField0228++;
                     if (this.internalField0228 >= 3) {
                        this.internalField0545.add(this.internalField0352);
                        this.internalField0352 = null;
                        this.internalField0228 = 0;
                        return false;
                     } else {
                        this.internalField0494 = new GotoPathTask(new RadiusGoal(this.internalField0352, 1));
                        this.internalField0277 = false;
                        return false;
                     }
                  } else {
                     this.internalField0228 = 0;
                     BestToolSelector.internalMethod04395(localValue1.world.getBlockState(this.internalField0352));
                     Direction localValue13 = this.internalMethod01879(localValue1, this.internalField0352);
                     if (!this.internalField0904.internalMethod05481(this.internalField0352, localValue13)) {
                        return false;
                     } else {
                        localValue1.interactionManager.updateBlockBreakingProgress(this.internalField0352, localValue13);
                        localValue1.player.swingHand(localValue1.player.getActiveHand());
                        return false;
                     }
                  }
               }
            } else if (this.internalField0494 != null) {
               boolean localValue6 = this.internalField0494.internalMethod04087();
               if (localValue6) {
                  this.internalField0494 = null;
                  if (this.internalField0277) {
                     if (this.internalField0586 != null && localValue1.world.getEntityById(this.internalField0586) instanceof ItemEntity localValue12 && localValue12.isAlive()) {
                        this.internalField1200.add(this.internalField0586);
                     }

                     this.internalField0586 = null;
                     this.internalField0277 = false;
                  } else if (this.internalField0352 != null) {
                     double localValue10 = localValue1.player.getEyePos().distanceTo(Vec3d.ofCenter(this.internalField0352));
                     if (localValue10 > 5.0) {
                        this.internalField0545.add(this.internalField0352);
                        this.internalField0352 = null;
                     } else {
                        this.internalField0276 = true;
                     }
                  }
               }

               return false;
            } else {
               ItemEntity localValue2 = this.internalMethod05504(localValue1);
               if (localValue2 != null) {
                  BlockPos localValue8 = localValue2.getBlockPos();
                  this.internalField0494 = new GotoPathTask(new RadiusGoal(localValue8, 1));
                  this.internalField0277 = true;
                  this.internalField0586 = localValue2.getId();
                  return false;
               } else {
                  BlockPos localValue3 = this.internalMethod05617(localValue1);
                  if (localValue3 == null) {
                     ChatMessageHelper.internalMethod00196(
                        "\u0411\u043b\u043e\u043a "
                           + this.internalField0792
                           + " \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d \u043f\u043e\u0431\u043b\u0438\u0437\u043e\u0441\u0442\u0438"
                     );
                     this.internalMethod04086();
                     this.internalField1100 = true;
                     return true;
                  } else {
                     this.internalField0352 = localValue3;
                     Vec3d localValue4 = Vec3d.ofCenter(localValue3);
                     boolean localValue5 = localValue1.player.getEyePos().distanceTo(localValue4) <= 4.5;
                     if (localValue5 && internalMethod04497(localValue1, localValue3)) {
                        this.internalField0276 = true;
                     } else {
                        this.internalField0494 = new GotoPathTask(new RadiusGoal(localValue3, 2));
                     }

                     return false;
                  }
               }
            }
         } else {
            return false;
         }
      }
   }

   private static boolean internalMethod04497(MinecraftClient localValue0, BlockPos localValue1) {
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

   @Nullable
   private ItemEntity internalMethod05504(MinecraftClient localValue1) {
      if (localValue1.world == null || localValue1.player == null) {
         return null;
      } else if (this.internalField0546.isEmpty()) {
         return null;
      } else {
         Vec3d localValue2 = localValue1.player.getEntityPos();
         Box localValue3 = Box.of(localValue2, 12.0, 12.0, 12.0);
         List localValue4 = localValue1.world
            .getEntitiesByClass(
               ItemEntity.class,
               localValue3,
               localValue1x -> localValue1x.isAlive()
                  && !localValue1x.cannotPickup()
                  && !this.internalField1200.contains(localValue1x.getId())
                  && this.internalField0546.contains(localValue1x.getStack().getItem())
            );
         if (localValue4.isEmpty()) {
            return null;
         } else {
            localValue4.sort(Comparator.comparingDouble(localValue1x -> ((net.minecraft.entity.Entity)localValue1x).getEntityPos().squaredDistanceTo(localValue2)));
            return (ItemEntity)localValue4.get(0);
         }
      }
   }

   private Direction internalMethod01879(MinecraftClient localValue1, BlockPos localValue2) {
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

   private void internalMethod08564() {
      MinecraftClient localValue1 = MinecraftClient.getInstance();
      if (localValue1.interactionManager != null) {
         localValue1.interactionManager.cancelBlockBreaking();
      }
   }

   @Nullable
   private BlockPos internalMethod05617(MinecraftClient localValue1) {
      if (localValue1.world != null && localValue1.player != null) {
         BlockPos localValue2 = localValue1.player.getBlockPos();
         BlockPos localValue3 = localValue2.down();
         if (localValue1.world.getBlockState(localValue3).getBlock() == this.internalField0792 && !this.internalField0545.contains(localValue3)) {
            return localValue3;
         } else {
            Mutable localValue4 = new Mutable();
            ArrayList<BlockPos> localValue5 = new ArrayList<>();

            for (int localValue6 = -48; localValue6 <= 48; localValue6++) {
               for (int localValue7 = -48; localValue7 <= 48; localValue7++) {
                  for (int localValue8 = -24; localValue8 <= 24; localValue8++) {
                     localValue4.set(localValue2.getX() + localValue6, localValue2.getY() + localValue8, localValue2.getZ() + localValue7);
                     if (localValue1.world.getBlockState(localValue4).getBlock() == this.internalField0792 && !this.internalField0545.contains(localValue4)) {
                        localValue5.add(localValue4.toImmutable());
                     }
                  }
               }
            }

            if (localValue5.isEmpty()) {
               return null;
            } else {
               Vec3d localValue9 = localValue1.player.getEyePos();
               localValue5.sort(Comparator.comparingDouble(localValue1x -> Vec3d.ofCenter(localValue1x).squaredDistanceTo(localValue9)));
               return (BlockPos)localValue5.get(0);
            }
         }
      } else {
         return null;
      }
   }

   @Override
   public void internalMethod04086() {
      if (this.internalField0494 != null) {
         this.internalField0494.internalMethod04086();
         this.internalField0494 = null;
      }

      this.internalMethod08564();
      MovementInputController localValue1 = NewtonCoreManager.internalMethod00114().internalMethod00183();
      localValue1.internalMethod01287();
      this.internalField0352 = null;
      this.internalField0276 = false;
      this.internalField0277 = false;
   }

   @Override
   public void internalMethod04089() {
      this.internalField1099 = true;
      if (this.internalField0494 != null) {
         this.internalField0494.internalMethod04089();
      }

      this.internalMethod08564();
      NewtonCoreManager.internalMethod00114().internalMethod00183().internalMethod01287();
   }

   @Override
   public void internalMethod08146() {
      this.internalField1099 = false;
      if (this.internalField0494 != null) {
         this.internalField0494.internalMethod08146();
      }
   }

   @Override
   public boolean internalMethod04090() {
      return this.internalField1099;
   }

   @Override
   public boolean internalMethod08147() {
      return this.internalField1100;
   }

   @Generated
   public Block internalMethod06304() {
      return this.internalField0792;
   }
}
