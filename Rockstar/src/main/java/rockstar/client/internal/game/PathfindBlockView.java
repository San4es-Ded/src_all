package rockstar.client.internal.game;



import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.util.math.Direction.Axis;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

public final class PathfindBlockView {
   public static final double internalField0194 = Double.NaN;
   @Nullable
   private final World internalField0520;
   private final BlockView internalField0268;
   private final PathfindBlockView.InternalType0259 internalField0708;
   private final boolean internalField0277;
   private final Mutable internalField0232 = new Mutable();
   private final Long2ObjectOpenHashMap<BlockState> internalField0461 = new Long2ObjectOpenHashMap(4096);
   private final Long2ByteOpenHashMap internalField0427 = new Long2ByteOpenHashMap(4096);
   private final Long2DoubleOpenHashMap internalField0767 = new Long2DoubleOpenHashMap(4096);

   public PathfindBlockView() {
      ClientWorld localValue1 = MinecraftClient.getInstance().world;
      if (localValue1 == null) {
         throw new IllegalStateException("Pathfinder cannot run without a loaded world");
      } else {
         this.internalField0520 = localValue1;
         this.internalField0268 = localValue1;
         Mutable localValue2 = new Mutable();
         this.internalField0708 = (localValue2x, localValue3, localValue4) -> localValue1.getBlockState(localValue2.set(localValue2x, localValue3, localValue4));
         this.internalField0277 = true;
         this.internalField0767.defaultReturnValue(Double.MAX_VALUE);
      }
   }

   public PathfindBlockView(PathfindBlockView.InternalType0259 localValue1) {
      this.internalField0520 = null;
      this.internalField0268 = EmptyBlockView.INSTANCE;
      this.internalField0708 = localValue1;
      this.internalField0277 = false;
      this.internalField0767.defaultReturnValue(Double.MAX_VALUE);
   }

   public boolean internalMethod06691() {
      return this.internalField0277;
   }

   @Nullable
   public World internalMethod04700() {
      return this.internalField0520;
   }

   public BlockState internalMethod05943(int localValue1, int localValue2, int localValue3) {
      long localValue4 = BlockPos.asLong(localValue1, localValue2, localValue3);
      BlockState localValue6 = (BlockState)this.internalField0461.get(localValue4);
      if (localValue6 != null) {
         return localValue6;
      } else {
         BlockState localValue7 = this.internalField0708.state(localValue1, localValue2, localValue3);
         this.internalField0461.put(localValue4, localValue7);
         return localValue7;
      }
   }

   private byte internalMethod05955(int localValue1, int localValue2, int localValue3) {
      long localValue4 = BlockPos.asLong(localValue1, localValue2, localValue3);
      byte localValue6 = this.internalField0427.get(localValue4);
      if ((localValue6 & 1) != 0) {
         return localValue6;
      } else {
         BlockState localValue7 = this.internalMethod05943(localValue1, localValue2, localValue3);
         localValue6 = 1;
         FluidState localValue8 = localValue7.getFluidState();
         Fluid localValue9 = localValue8.getFluid();
         boolean localValue10 = localValue9 == Fluids.WATER || localValue9 == Fluids.FLOWING_WATER;
         boolean localValue11 = localValue9 == Fluids.LAVA || localValue9 == Fluids.FLOWING_LAVA;
         if (localValue10) {
            localValue6 = (byte)(localValue6 | 8);
         }

         if (internalMethod04894(localValue7)) {
            localValue6 = (byte)(localValue6 | 16);
         }

         if (localValue11
            || localValue7.isOf(Blocks.LAVA)
            || localValue7.isOf(Blocks.FIRE)
            || localValue7.isOf(Blocks.SOUL_FIRE)
            || localValue7.isOf(Blocks.MAGMA_BLOCK)
            || localValue7.isOf(Blocks.CACTUS)
            || localValue7.isOf(Blocks.CAMPFIRE)
            || localValue7.isOf(Blocks.SOUL_CAMPFIRE)) {
            localValue6 = (byte)(localValue6 | 32);
         }

         if (localValue7.isOf(Blocks.COBWEB) || localValue7.isOf(Blocks.POWDER_SNOW) || localValue7.isOf(Blocks.SWEET_BERRY_BUSH) || localValue7.isOf(Blocks.WITHER_ROSE)) {
            localValue6 = (byte)(localValue6 | 64);
         }

         this.internalField0232.set(localValue1, localValue2, localValue3);
         VoxelShape localValue12 = localValue7.getCollisionShape(this.internalField0268, this.internalField0232);
         if (localValue12.isEmpty()) {
            localValue6 = (byte)(localValue6 | 2);
         } else if (Block.isShapeFullCube(localValue12)) {
            localValue6 = (byte)(localValue6 | 4);
         }

         this.internalField0427.put(localValue4, localValue6);
         return localValue6;
      }
   }

   private static boolean internalMethod04894(BlockState localValue0) {
      if (!localValue0.isOf(Blocks.LADDER)
         && !localValue0.isOf(Blocks.VINE)
         && !localValue0.isOf(Blocks.SCAFFOLDING)
         && !localValue0.isOf(Blocks.TWISTING_VINES)
         && !localValue0.isOf(Blocks.TWISTING_VINES_PLANT)
         && !localValue0.isOf(Blocks.WEEPING_VINES)
         && !localValue0.isOf(Blocks.WEEPING_VINES_PLANT)
         && !localValue0.isOf(Blocks.CAVE_VINES)
         && !localValue0.isOf(Blocks.CAVE_VINES_PLANT)) {
         try {
            return localValue0.isIn(BlockTags.CLIMBABLE);
         } catch (Throwable localValue2) {
            return false;
         }
      } else {
         return true;
      }
   }

   public boolean internalMethod05958(int localValue1, int localValue2, int localValue3) {
      byte localValue4 = this.internalMethod05955(localValue1, localValue2, localValue3);
      if ((localValue4 & 2) == 0) {
         return false;
      } else {
         return (localValue4 & 96) != 0 ? false : (localValue4 & 8) == 0 && this.internalMethod05943(localValue1, localValue2, localValue3).getFluidState().isEmpty();
      }
   }

   public boolean internalMethod02776(int localValue1, int localValue2, int localValue3) {
      byte localValue4 = this.internalMethod05955(localValue1, localValue2, localValue3);
      return (localValue4 & 2) != 0 ? false : !this.internalMethod05943(localValue1, localValue2, localValue3).isOf(Blocks.LAVA);
   }

   public boolean internalMethod07797(int localValue1, int localValue2, int localValue3) {
      return (this.internalMethod05955(localValue1, localValue2, localValue3) & 8) != 0;
   }

   public boolean internalMethod08763(int localValue1, int localValue2, int localValue3) {
      if ((this.internalMethod05955(localValue1, localValue2, localValue3) & 8) == 0) {
         return false;
      } else {
         return (this.internalMethod05955(localValue1, localValue2 + 1, localValue3) & 8) != 0 ? true : this.internalMethod05943(localValue1, localValue2, localValue3).getFluidState().getLevel() >= 7;
      }
   }

   public boolean internalMethod07759(int localValue1, int localValue2, int localValue3) {
      return (this.internalMethod05955(localValue1, localValue2, localValue3) & 16) != 0;
   }

   public boolean internalMethod08739(int localValue1, int localValue2, int localValue3) {
      return (this.internalMethod05955(localValue1, localValue2, localValue3) & 32) != 0;
   }

   public boolean internalMethod09423(int localValue1, int localValue2, int localValue3) {
      return (this.internalMethod05955(localValue1, localValue2, localValue3) & 64) != 0;
   }

   public boolean internalMethod09927(int localValue1, int localValue2, int localValue3) {
      byte localValue4 = this.internalMethod05955(localValue1, localValue2, localValue3);
      return (localValue4 & 96) != 0 ? false : (localValue4 & 10) != 0 || (localValue4 & 16) != 0;
   }

   public double internalMethod05956(int localValue1, int localValue2, int localValue3) {
      long localValue4 = BlockPos.asLong(localValue1, localValue2, localValue3);
      double localValue6 = this.internalField0767.get(localValue4);
      if (localValue6 != Double.MAX_VALUE) {
         return localValue6;
      } else {
         double localValue8 = this.internalMethod07796(localValue1, localValue2, localValue3);
         this.internalField0767.put(localValue4, localValue8);
         return localValue8;
      }
   }

   private double internalMethod07796(int localValue1, int localValue2, int localValue3) {
      byte localValue4 = this.internalMethod05955(localValue1, localValue2, localValue3);
      if ((localValue4 & 96) != 0) {
         return Double.NaN;
      } else {
         boolean localValue7 = (localValue4 & 2) == 0 && (localValue4 & 16) == 0;
         double localValue5;
         if (localValue7) {
            double localValue8 = this.internalMethod08762(localValue1, localValue2, localValue3);
            if (localValue8 > 0.6) {
               return Double.NaN;
            }

            localValue5 = localValue2 + localValue8;
         } else {
            byte localValue12 = this.internalMethod05955(localValue1, localValue2 - 1, localValue3);
            if ((localValue12 & 32) != 0) {
               return Double.NaN;
            }

            if ((localValue12 & 2) != 0 || (localValue12 & 16) != 0) {
               return Double.NaN;
            }

            double localValue9;
            if ((localValue12 & 4) != 0) {
               localValue9 = 1.0;
            } else {
               localValue9 = this.internalMethod08762(localValue1, localValue2 - 1, localValue3);
               if (localValue9 <= 0.6 || localValue9 > 1.05) {
                  return Double.NaN;
               }
            }

            localValue5 = localValue2 - 1 + Math.min(localValue9, 1.0);
         }

         double localValue13 = localValue1 + 0.5;
         double localValue10 = localValue3 + 0.5;
         return !this.internalMethod05932(new Box(localValue13 - 0.3, localValue5 + 0.02, localValue10 - 0.3, localValue13 + 0.3, localValue5 + 1.8, localValue10 + 0.3)) ? Double.NaN : localValue5;
      }
   }

   private double internalMethod08762(int localValue1, int localValue2, int localValue3) {
      this.internalField0232.set(localValue1, localValue2, localValue3);
      VoxelShape localValue4 = this.internalMethod05943(localValue1, localValue2, localValue3).getCollisionShape(this.internalField0268, this.internalField0232);
      return localValue4.isEmpty() ? 0.0 : localValue4.getMax(Axis.Y);
   }

   public boolean internalMethod09396(int localValue1, int localValue2, int localValue3) {
      return !Double.isNaN(this.internalMethod05956(localValue1, localValue2, localValue3));
   }

   public boolean internalMethod09918(int localValue1, int localValue2, int localValue3) {
      if (!this.internalMethod07759(localValue1, localValue2, localValue3)) {
         return false;
      } else {
         double localValue4 = localValue1 + 0.5;
         double localValue6 = localValue3 + 0.5;
         return this.internalMethod05932(new Box(localValue4 - 0.3, localValue2 + 0.02, localValue6 - 0.3, localValue4 + 0.3, localValue2 + 1.8, localValue6 + 0.3));
      }
   }

   public PathfindBlockView.InternalType0260 internalMethod03832(int localValue1, int localValue2, int localValue3) {
      if (this.internalMethod09396(localValue1, localValue2, localValue3)) {
         return PathfindBlockView.InternalType0260.internalField0709;
      } else if (this.internalMethod09918(localValue1, localValue2, localValue3)) {
         return PathfindBlockView.InternalType0260.internalField1282;
      } else {
         return this.internalMethod08763(localValue1, localValue2, localValue3)
            ? PathfindBlockView.InternalType0260.internalField1281
            : PathfindBlockView.InternalType0260.internalField0710;
      }
   }

   public boolean internalMethod05932(Box localValue1) {
      int localValue2 = internalMethod01317(localValue1.minX);
      int localValue3 = internalMethod01317(localValue1.minY);
      int localValue4 = internalMethod01317(localValue1.minZ);
      int localValue5 = internalMethod01382(localValue1.maxX);
      int localValue6 = internalMethod01382(localValue1.maxY);
      int localValue7 = internalMethod01382(localValue1.maxZ);

      for (int localValue8 = localValue2; localValue8 <= localValue5; localValue8++) {
         for (int localValue9 = localValue3; localValue9 <= localValue6; localValue9++) {
            for (int localValue10 = localValue4; localValue10 <= localValue7; localValue10++) {
               byte localValue11 = this.internalMethod05955(localValue8, localValue9, localValue10);
               if ((localValue11 & 96) != 0) {
                  return false;
               }

               if ((localValue11 & 2) == 0 && (localValue11 & 16) == 0) {
                  if ((localValue11 & 4) != 0) {
                     return false;
                  }

                  this.internalField0232.set(localValue8, localValue9, localValue10);
                  BlockState localValue12 = this.internalMethod05943(localValue8, localValue9, localValue10);

                  for (Box localValue14 : localValue12.getCollisionShape(this.internalField0268, this.internalField0232).getBoundingBoxes()) {
                     if (localValue14.offset(localValue8, localValue9, localValue10).intersects(localValue1)) {
                        return false;
                     }
                  }
               }
            }
         }
      }

      return true;
   }

   public boolean internalMethod03056(int localValue1, int localValue2, int localValue3, int localValue4, double localValue5, double localValue7) {
      double localValue9 = localValue1 + 0.5;
      double localValue11 = localValue2 + 0.5;
      double localValue13 = localValue3 + 0.5;
      double localValue15 = localValue4 + 0.5;
      return this.internalMethod05932(
         new Box(Math.min(localValue9, localValue13) - 0.3, localValue5, Math.min(localValue11, localValue15) - 0.3, Math.max(localValue9, localValue13) + 0.3, localValue7, Math.max(localValue11, localValue15) + 0.3)
      );
   }

   public double internalMethod02775(int localValue1, int localValue2, int localValue3) {
      for (int localValue4 = -1; localValue4 <= 1; localValue4++) {
         for (int localValue5 = -1; localValue5 <= 1; localValue5++) {
            if ((localValue4 != 0 || localValue5 != 0)
               && (this.internalMethod08739(localValue1 + localValue4, localValue2, localValue3 + localValue5) || this.internalMethod08739(localValue1 + localValue4, localValue2 - 1, localValue3 + localValue5))) {
               return 8.0;
            }
         }
      }

      return !this.internalMethod08739(localValue1, localValue2 - 1, localValue3) && !this.internalMethod08739(localValue1, localValue2 - 2, localValue3) ? 0.0 : 8.0;
   }

   public boolean internalMethod09854(int localValue1, int localValue2, int localValue3) {
      for (int localValue4 = -1; localValue4 <= 1; localValue4++) {
         for (int localValue5 = -1; localValue5 <= 1; localValue5++) {
            for (int localValue6 = -1; localValue6 <= 1; localValue6++) {
               if ((localValue4 != 0 || localValue5 != 0 || localValue6 != 0) && this.internalMethod08739(localValue1 + localValue4, localValue2 + localValue5, localValue3 + localValue6)) {
                  return true;
               }
            }
         }
      }

      return false;
   }

   public int internalMethod05957(int localValue1, int localValue2, int localValue3) {
      if (!this.internalField0277) {
         return Integer.MAX_VALUE;
      } else {
         BlockState localValue4 = this.internalMethod05943(localValue1, localValue2, localValue3);
         if (localValue4.isAir()) {
            return 0;
         } else if (this.internalMethod08739(localValue1, localValue2, localValue3)) {
            return Integer.MAX_VALUE;
         } else if (localValue4.isReplaceable() && localValue4.getFluidState().isEmpty()) {
            return 0;
         } else if (!localValue4.getFluidState().isEmpty()) {
            return Integer.MAX_VALUE;
         } else {
            this.internalField0232.set(localValue1, localValue2, localValue3);
            if (localValue4.getCollisionShape(this.internalField0268, this.internalField0232).isEmpty() && !this.internalMethod09423(localValue1, localValue2, localValue3)) {
               return 0;
            } else {
               int localValue5 = BlockBreakTimeEstimator.internalMethod05452(localValue4);
               return localValue5 > 200 ? Integer.MAX_VALUE : localValue5;
            }
         }
      }
   }

   public int internalMethod03057(int localValue1, int localValue2, int localValue3, int localValue4, int localValue5, int localValue6) {
      if (!this.internalField0277) {
         return Integer.MAX_VALUE;
      } else {
         BlockState localValue7 = this.internalMethod05943(localValue4, localValue5, localValue6);
         if (localValue7.isAir()) {
            return 0;
         } else if (this.internalMethod08739(localValue4, localValue5, localValue6)) {
            return Integer.MAX_VALUE;
         } else if (localValue7.isReplaceable() && localValue7.getFluidState().isEmpty()) {
            return 0;
         } else if (!localValue7.getFluidState().isEmpty()) {
            return Integer.MAX_VALUE;
         } else {
            this.internalField0232.set(localValue4, localValue5, localValue6);
            if (!internalMethod07238(this.internalField0520, localValue7, this.internalField0232, localValue1, localValue2, localValue3) && !this.internalMethod09423(localValue4, localValue5, localValue6)) {
               return 0;
            } else {
               int localValue8 = BlockBreakTimeEstimator.internalMethod05452(localValue7);
               return localValue8 > 200 ? Integer.MAX_VALUE : localValue8;
            }
         }
      }
   }

   public boolean internalMethod09539(int localValue1, int localValue2, int localValue3) {
      Box localValue4 = internalMethod07561(localValue1, localValue2, localValue3);
      int localValue5 = internalMethod01317(localValue4.minX);
      int localValue6 = internalMethod01317(localValue4.minY);
      int localValue7 = internalMethod01317(localValue4.minZ);
      int localValue8 = internalMethod01382(localValue4.maxX);
      int localValue9 = internalMethod01382(localValue4.maxY);
      int localValue10 = internalMethod01382(localValue4.maxZ);

      for (int localValue11 = localValue5; localValue11 <= localValue8; localValue11++) {
         for (int localValue12 = localValue6; localValue12 <= localValue9; localValue12++) {
            for (int localValue13 = localValue7; localValue13 <= localValue10; localValue13++) {
               this.internalField0232.set(localValue11, localValue12, localValue13);
               BlockState localValue14 = this.internalMethod05943(localValue11, localValue12, localValue13);
               if (!localValue14.isAir() && !localValue14.isReplaceable()) {
                  if (!localValue14.getFluidState().isEmpty()) {
                     return false;
                  }

                  if (internalMethod06431(this.internalField0268, localValue14, this.internalField0232, localValue4)) {
                     return false;
                  }
               }
            }
         }
      }

      return true;
   }

   public static boolean internalMethod05828(BlockView localValue0, BlockState localValue1, BlockPos localValue2, int localValue3, int localValue4, int localValue5) {
      if (localValue1.isAir()) {
         return true;
      } else if (localValue1.isReplaceable()) {
         return true;
      } else {
         return !localValue1.getFluidState().isEmpty() ? false : !internalMethod07238(localValue0, localValue1, localValue2, localValue3, localValue4, localValue5);
      }
   }

   public static boolean internalMethod07238(BlockView localValue0, BlockState localValue1, BlockPos localValue2, int localValue3, int localValue4, int localValue5) {
      return internalMethod06431(localValue0, localValue1, localValue2, internalMethod07561(localValue3, localValue4, localValue5));
   }

   private static boolean internalMethod06431(BlockView localValue0, BlockState localValue1, BlockPos localValue2, Box localValue3) {
      for (Box localValue5 : localValue1.getCollisionShape(localValue0, localValue2).getBoundingBoxes()) {
         if (localValue5.offset(localValue2).intersects(localValue3)) {
            return true;
         }
      }

      return false;
   }

   private static Box internalMethod07561(int localValue0, int localValue1, int localValue2) {
      double localValue3 = localValue0 + 0.5;
      double localValue5 = localValue2 + 0.5;
      return new Box(localValue3 - 0.3, localValue1, localValue5 - 0.3, localValue3 + 0.3, localValue1 + 1.8, localValue5 + 0.3);
   }

   private static int internalMethod01317(double localValue0) {
      return (int)Math.floor(localValue0);
   }

   private static int internalMethod01382(double localValue0) {
      return (int)Math.floor(localValue0 - 1.0E-7);
   }

   public interface InternalType0259 {
      BlockState state(int localValue1, int localValue2, int localValue3);
   }

   public static enum InternalType0260 {
      internalField0710,
      internalField0709,
      internalField1282,
      internalField1281;

      public static PathfindBlockView.InternalType0260[] internalMethod05390() {
         return values();
      }

      public static PathfindBlockView.InternalType0260 internalMethod02956(String localValue0) {
         return Enum.valueOf(PathfindBlockView.InternalType0260.class, localValue0);
      }
   }
}
