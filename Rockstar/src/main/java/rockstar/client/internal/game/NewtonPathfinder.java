package rockstar.client.internal.game;





import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import it.unimi.dsi.fastutil.longs.Long2DoubleOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.PriorityQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public final class NewtonPathfinder {
   private static final double internalField0194 = 1.15;
   private static final ExecutorService internalField0124 = Executors.newSingleThreadExecutor(localValue0 -> {
      Thread localValue1 = new Thread(localValue0, "Newton-Pathfinder");
      localValue1.setDaemon(true);
      return localValue1;
   });

   private NewtonPathfinder() {
   }

   public static CompletableFuture<Optional<ComputedPath>> internalMethod03533(PathNode localValue0, PathGoal localValue1) {
      return internalMethod06525(localValue0, localValue1, 60000, null);
   }

   public static CompletableFuture<Optional<ComputedPath>> internalMethod01302(PathNode localValue0, PathGoal localValue1, int localValue2) {
      return internalMethod06525(localValue0, localValue1, localValue2, null);
   }

   public static CompletableFuture<Optional<ComputedPath>> internalMethod06790(PathNode localValue0, PathGoal localValue1, AtomicBoolean localValue2) {
      return internalMethod06525(localValue0, localValue1, 60000, localValue2);
   }

   public static CompletableFuture<Optional<ComputedPath>> internalMethod06525(
      PathNode localValue0, PathGoal localValue1, int localValue2, @Nullable AtomicBoolean localValue3
   ) {
      return CompletableFuture.supplyAsync(() -> internalMethod01685(localValue0, localValue1, localValue2, localValue3), internalField0124);
   }

   public static Optional<ComputedPath> internalMethod02387(PathNode localValue0, PathGoal localValue1, int localValue2) {
      return internalMethod01685(localValue0, localValue1, localValue2, (AtomicBoolean)null);
   }

   public static Optional<ComputedPath> internalMethod01685(PathNode localValue0, PathGoal localValue1, int localValue2, @Nullable AtomicBoolean localValue3) {
      PathfindBlockView localValue4;
      try {
         localValue4 = new PathfindBlockView();
      } catch (IllegalStateException localValue6) {
         return Optional.empty();
      }

      return internalMethod01803(localValue0, localValue1, localValue2, 3000L, localValue4, localValue3);
   }

   public static Optional<ComputedPath> internalMethod02593(PathNode localValue0, PathGoal localValue1, int localValue2, long localValue3, PathfindBlockView localValue5) {
      return internalMethod01803(localValue0, localValue1, localValue2, localValue3, localValue5, null);
   }

   public static Optional<ComputedPath> internalMethod01803(
      PathNode localValue0, PathGoal localValue1, int localValue2, long localValue3, PathfindBlockView localValue5, @Nullable AtomicBoolean localValue6
   ) {
      long localValue7 = System.nanoTime() + localValue3 * 1000000L;
      Long2DoubleOpenHashMap localValue9 = new Long2DoubleOpenHashMap(4096);
      localValue9.defaultReturnValue(Double.POSITIVE_INFINITY);
      Long2ObjectOpenHashMap localValue10 = new Long2ObjectOpenHashMap(4096);
      PriorityQueue localValue11 = new PriorityQueue();
      long localValue12 = internalMethod00606(localValue0);
      localValue9.put(localValue12, 0.0);
      double localValue14 = localValue1.internalMethod05087(localValue0.internalMethod02945(), localValue0.internalMethod02949(), localValue0.internalMethod07945());
      localValue11.add(new NewtonPathfinder.InternalType0413(localValue0, 0.0, localValue14 * 1.15));
      PathNode localValue16 = localValue0;
      double localValue17 = localValue14;
      double localValue19 = 0.0;
      int localValue21 = 0;

      while (!localValue11.isEmpty()) {
         NewtonPathfinder.InternalType0413 localValue22 = (NewtonPathfinder.InternalType0413)localValue11.poll();
         PathNode localValue23 = localValue22.internalField0923;
         long localValue24 = internalMethod00606(localValue23);
         double localValue26 = localValue9.get(localValue24);
         if (!(localValue22.internalField0194 > localValue26 + 1.0E-9)) {
            if (localValue1.internalMethod05088(localValue23.internalMethod02945(), localValue23.internalMethod02949(), localValue23.internalMethod07945())) {
               return Optional.of(internalMethod01384(localValue10, localValue0, localValue23));
            }

            localValue21++;
            if (localValue21 > localValue2 || (localValue21 & 0xFF) == 0 && (System.nanoTime() > localValue7 || localValue6 != null && localValue6.get())) {
               break;
            }

            for (AbstractPathStep localValue29 : MoveNodeGenerator.internalMethod03577(localValue23, localValue5)) {
               PathNode localValue30 = localValue29.internalMethod02540();
               long localValue31 = internalMethod00606(localValue30);
               double localValue33 = localValue26 + localValue29.internalMethod01344();
               if (localValue33 < localValue9.get(localValue31)) {
                  localValue9.put(localValue31, localValue33);
                  localValue10.put(localValue31, localValue29);
                  double localValue35 = localValue1.internalMethod05087(localValue30.internalMethod02945(), localValue30.internalMethod02949(), localValue30.internalMethod07945());
                  if (localValue35 < localValue17 - 1.0E-9 || localValue35 < localValue17 + 1.0E-9 && localValue33 < localValue19) {
                     localValue17 = localValue35;
                     localValue16 = localValue30;
                     localValue19 = localValue33;
                  }

                  localValue11.add(new NewtonPathfinder.InternalType0413(localValue30, localValue33, localValue33 + localValue35 * 1.15));
               }
            }
         }
      }

      return !localValue16.equals(localValue0) ? Optional.of(internalMethod01384(localValue10, localValue0, localValue16)) : Optional.empty();
   }

   private static long internalMethod00606(PathNode localValue0) {
      return BlockPos.asLong(localValue0.internalMethod02945(), localValue0.internalMethod02949(), localValue0.internalMethod07945());
   }

   private static ComputedPath internalMethod01384(Long2ObjectOpenHashMap<AbstractPathStep> localValue0, PathNode localValue1, PathNode localValue2) {
      ArrayList localValue3 = new ArrayList();
      ArrayList localValue4 = new ArrayList();
      PathNode localValue5 = localValue2;
      int localValue6 = 1048576;

      while (!localValue5.equals(localValue1)) {
         AbstractPathStep localValue7 = (AbstractPathStep)localValue0.get(internalMethod00606(localValue5));
         if (localValue7 == null || --localValue6 <= 0) {
            return new ComputedPath(List.of(localValue1), List.of());
         }

         localValue3.add(localValue5);
         localValue4.add(localValue7);
         localValue5 = localValue7.internalMethod01873();
      }

      localValue3.add(localValue1);
      Collections.reverse(localValue3);
      Collections.reverse(localValue4);
      return new ComputedPath(localValue3, localValue4);
   }

   static final class InternalType0413 implements Comparable<NewtonPathfinder.InternalType0413> {
      final PathNode internalField0923;
      final double internalField0194;
      private final double internalField0193;

      InternalType0413(PathNode localValue1, double localValue2, double localValue4) {
         this.internalField0923 = localValue1;
         this.internalField0194 = localValue2;
         this.internalField0193 = localValue4;
      }

      @Override
      public int compareTo(NewtonPathfinder.InternalType0413 localValue1) {
         int localValue2 = Double.compare(this.internalField0193, localValue1.internalField0193);
         return localValue2 != 0 ? localValue2 : Double.compare(localValue1.internalField0194, this.internalField0194);
      }

      @Override
      public final String toString() {
         return "InternalType0413[node=" + this.internalField0923 + ", g=" + this.internalField0194 + ", f=" + this.internalField0193 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0923);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0194);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0193);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         NewtonPathfinder.InternalType0413 other = (NewtonPathfinder.InternalType0413) localValue1;
         return java.util.Objects.equals(this.internalField0923, other.internalField0923)
            && java.util.Objects.equals(this.internalField0194, other.internalField0194)
            && java.util.Objects.equals(this.internalField0193, other.internalField0193);
      }

      public PathNode internalMethod07446() {
         return this.internalField0923;
      }

      public double internalMethod04429() {
         return this.internalField0194;
      }

      public double internalMethod04435() {
         return this.internalField0193;
      }
   }
}
