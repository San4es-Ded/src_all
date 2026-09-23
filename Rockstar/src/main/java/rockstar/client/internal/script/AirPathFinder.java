package rockstar.client.internal.script;


import rockstar.client.*;
import it.unimi.dsi.fastutil.longs.Long2ByteOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2LongOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.BlockPos.Mutable;
import net.minecraft.world.World;

public final class AirPathFinder {
   private static final ExecutorService internalField0124 = Executors.newSingleThreadExecutor(localValue0x -> {
      Thread localValue1x = new Thread(localValue0x, "Newton-AirPlanner");
      localValue1x.setDaemon(true);
      return localValue1x;
   });
   private static final int[][] internalField0040;

   private AirPathFinder() {
   }

   public static CompletableFuture<List<Vec3d>> internalMethod01945(World localValue0, Vec3d localValue1, Vec3d localValue2) {
      return CompletableFuture.supplyAsync(() -> internalMethod00926(localValue0, localValue1, localValue2), internalField0124);
   }

   public static List<Vec3d> internalMethod00926(World localValue0, Vec3d localValue1, Vec3d localValue2) {
      long localValue3 = System.nanoTime() + 800000000L;
      Long2ByteOpenHashMap localValue5 = new Long2ByteOpenHashMap(8192);
      Mutable localValue6 = new Mutable();
      int localValue7 = MathHelper.floor(localValue1.x) >> 1;
      int localValue8 = MathHelper.floor(localValue1.y) >> 1;
      int localValue9 = MathHelper.floor(localValue1.z) >> 1;
      int localValue10 = MathHelper.floor(localValue2.x) >> 1;
      int localValue11 = MathHelper.floor(localValue2.y) >> 1;
      int localValue12 = MathHelper.floor(localValue2.z) >> 1;
      int localValue13 = -1;

      for (int localValue14 = 0; localValue14 <= 2; localValue14++) {
         if (internalMethod00073(localValue0, localValue5, localValue6, localValue7, localValue8 + localValue14, localValue9)) {
            localValue13 = localValue8 + localValue14;
            break;
         }
      }

      if (localValue13 < 0) {
         return List.of();
      } else {
         long localValue49 = internalMethod01704(localValue7, localValue13, localValue9);
         Long2DoubleOpenHashMap localValue16 = new Long2DoubleOpenHashMap(4096);
         localValue16.defaultReturnValue(Double.POSITIVE_INFINITY);
         Long2LongOpenHashMap localValue17 = new Long2LongOpenHashMap(4096);
         localValue17.defaultReturnValue(Long.MIN_VALUE);
         PriorityQueue<double[]> localValue18 = new PriorityQueue<>((localValue0x, localValue1x) -> Double.compare(localValue0x[0], localValue1x[0]));
         localValue16.put(localValue49, 0.0);
         localValue18.add(new double[]{internalMethod03373(localValue7, localValue13, localValue9, localValue10, localValue11, localValue12) * 1.2, 0.0, localValue7, localValue13, localValue9});
         long localValue19 = localValue49;
         double localValue21 = internalMethod03373(localValue7, localValue13, localValue9, localValue10, localValue11, localValue12);
         int localValue23 = 0;

         while (!localValue18.isEmpty()) {
            double[] localValue24 = (double[])localValue18.poll();
            int localValue25 = (int)localValue24[2];
            int localValue26 = (int)localValue24[3];
            int localValue27 = (int)localValue24[4];
            long localValue28 = internalMethod01704(localValue25, localValue26, localValue27);
            double localValue30 = localValue16.get(localValue28);
            if (!(localValue24[1] > localValue30 + 1.0E-9)) {
               if (Math.max(Math.abs(localValue25 - localValue10), Math.max(Math.abs(localValue26 - localValue11), Math.abs(localValue27 - localValue12))) <= 1) {
                  localValue19 = localValue28;
                  break;
               }

               localValue23++;
               if (localValue23 > 40000 || (localValue23 & 127) == 0 && System.nanoTime() > localValue3) {
                  break;
               }

               for (int[] localValue35 : internalField0040) {
                  int localValue36 = localValue25 + localValue35[0];
                  int localValue37 = localValue26 + localValue35[1];
                  int localValue38 = localValue27 + localValue35[2];
                  if (internalMethod00073(localValue0, localValue5, localValue6, localValue36, localValue37, localValue38)) {
                     double localValue39 = Math.sqrt(localValue35[0] * localValue35[0] + localValue35[1] * localValue35[1] + localValue35[2] * localValue35[2]);
                     double localValue41 = localValue35[1] > 0 ? localValue39 * 2.5 : localValue39;
                     double localValue43 = localValue30 + localValue41;
                     long localValue45 = internalMethod01704(localValue36, localValue37, localValue38);
                     if (localValue43 < localValue16.get(localValue45)) {
                        localValue16.put(localValue45, localValue43);
                        localValue17.put(localValue45, localValue28);
                        double localValue47 = internalMethod03373(localValue36, localValue37, localValue38, localValue10, localValue11, localValue12);
                        if (localValue47 < localValue21 - 1.0E-9) {
                           localValue21 = localValue47;
                           localValue19 = localValue45;
                        }

                        localValue18.add(new double[]{localValue43 + localValue47 * 1.2, localValue43, localValue36, localValue37, localValue38});
                     }
                  }
               }
            }
         }

         return localValue19 == localValue49 ? List.of() : internalMethod02283(localValue17, localValue49, localValue19);
      }
   }

   private static List<Vec3d> internalMethod02283(Long2LongOpenHashMap localValue0, long localValue1, long localValue3) {
      ArrayList localValue5 = new ArrayList();
      long localValue6 = localValue3;
      int localValue8 = 262144;

      while (localValue6 != localValue1) {
         if (--localValue8 <= 0) {
            break;
         }

         localValue5.add(internalMethod03967(localValue6));
         localValue6 = localValue0.get(localValue6);
         if (localValue6 == Long.MIN_VALUE) {
            return List.of();
         }
      }

      Collections.reverse(localValue5);
      return internalMethod04786(localValue5);
   }

   private static List<Vec3d> internalMethod04786(List<Vec3d> localValue0) {
      if (localValue0.size() <= 2) {
         return localValue0;
      } else {
         ArrayList localValue1 = new ArrayList(localValue0.size() / 2 + 2);
         localValue1.add((Vec3d)localValue0.get(0));

         for (int localValue2 = 1; localValue2 < localValue0.size() - 1; localValue2++) {
            Vec3d localValue3 = (Vec3d)localValue1.get(localValue1.size() - 1);
            Vec3d localValue4 = (Vec3d)localValue0.get(localValue2);
            Vec3d localValue5 = (Vec3d)localValue0.get(localValue2 + 1);
            Vec3d localValue6 = localValue4.subtract(localValue3).normalize();
            Vec3d localValue7 = localValue5.subtract(localValue4).normalize();
            if (localValue6.dotProduct(localValue7) < 0.999 || localValue4.subtract(localValue3).length() > 14.0) {
               localValue1.add(localValue4);
            }
         }

         localValue1.add((Vec3d)localValue0.get(localValue0.size() - 1));
         return localValue1;
      }
   }

   private static boolean internalMethod00073(World localValue0, Long2ByteOpenHashMap localValue1, Mutable localValue2, int localValue3, int localValue4, int localValue5) {
      long localValue6 = internalMethod01704(localValue3, localValue4, localValue5);
      byte localValue8 = localValue1.get(localValue6);
      if (localValue8 != 0) {
         return localValue8 == 1;
      } else {
         boolean localValue9 = internalMethod00204(localValue0, localValue2, localValue3, localValue4, localValue5);
         localValue1.put(localValue6, (byte)(localValue9 ? 1 : 2));
         return localValue9;
      }
   }

   private static boolean internalMethod00204(World localValue0, Mutable localValue1, int localValue2, int localValue3, int localValue4) {
      int localValue5 = localValue2 * 2;
      int localValue6 = localValue3 * 2;
      int localValue7 = localValue4 * 2;
      if (localValue6 > localValue0.getBottomY() + 1 && localValue6 + 2 < localValue0.getTopYInclusive()) {
         if (localValue0.isPosLoaded(localValue5, localValue7) && localValue0.isPosLoaded(localValue5 + 2 - 1, localValue7 + 2 - 1)) {
            for (int localValue8 = localValue5; localValue8 < localValue5 + 2; localValue8++) {
               for (int localValue9 = localValue6; localValue9 < localValue6 + 2; localValue9++) {
                  for (int localValue10 = localValue7; localValue10 < localValue7 + 2; localValue10++) {
                     localValue1.set(localValue8, localValue9, localValue10);
                     BlockState localValue11 = localValue0.getBlockState(localValue1);
                     if (!localValue11.getFluidState().isEmpty()) {
                        return false;
                     }

                     if (localValue11.isOf(Blocks.FIRE) || localValue11.isOf(Blocks.SOUL_FIRE) || localValue11.isOf(Blocks.MAGMA_BLOCK)) {
                        return false;
                     }

                     if (!localValue11.isAir() && !localValue11.isReplaceable() && !localValue11.getCollisionShape(localValue0, localValue1).isEmpty()) {
                        return false;
                     }
                  }
               }
            }

            return true;
         } else {
            return false;
         }
      } else {
         return false;
      }
   }

   private static long internalMethod01704(int localValue0, int localValue1, int localValue2) {
      return BlockPos.asLong(localValue0, localValue1, localValue2);
   }

   private static Vec3d internalMethod03967(long localValue0) {
      int localValue2 = BlockPos.unpackLongX(localValue0);
      int localValue3 = BlockPos.unpackLongY(localValue0);
      int localValue4 = BlockPos.unpackLongZ(localValue0);
      return new Vec3d(localValue2 * 2 + 1.0, localValue3 * 2 + 1.0, localValue4 * 2 + 1.0);
   }

   private static double internalMethod03373(int localValue0, int localValue1, int localValue2, int localValue3, int localValue4, int localValue5) {
      double localValue6 = localValue0 - localValue3;
      double localValue8 = localValue1 - localValue4;
      double localValue10 = localValue2 - localValue5;
      return Math.sqrt(localValue6 * localValue6 + localValue8 * localValue8 + localValue10 * localValue10);
   }

   static {
      ArrayList<int[]> localValue0 = new ArrayList<>(24);

      for (int localValue1 = -1; localValue1 <= 1; localValue1++) {
         for (int localValue2 = -1; localValue2 <= 1; localValue2++) {
            for (int localValue3 = -1; localValue3 <= 1; localValue3++) {
               if (localValue1 != 0 || localValue3 != 0) {
                  localValue0.add(new int[]{localValue1, localValue2, localValue3});
               }
            }
         }
      }

      internalField0040 = localValue0.toArray(new int[0][]);
   }
}
