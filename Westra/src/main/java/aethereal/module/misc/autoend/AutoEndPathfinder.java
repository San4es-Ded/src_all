package aethereal.module.misc.autoend;

import aethereal.core.Interface;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import net.minecraft.class_2338;
import net.minecraft.class_2680;
import net.minecraft.class_638;

final class AutoEndPathfinder {
   static final int MAX_FALL = 20;
   private static final int MAX_NODES = 60000;
   private static final int[][] DIRECTIONS = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}, {1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

   private AutoEndPathfinder() {
   }

   static List<class_2338> find(class_2338 start, Set<class_2338> goals, long primaryMs, long failureMs) {
      class_638 world = Interface.aM_.field_1687;
      if (world != null && !goals.isEmpty() && walkable(world, start)) {
         long startedAt = System.nanoTime();
         PriorityQueue<AutoEndPathfinder.Node> open = new PriorityQueue<>();
         Map<class_2338, class_2338> parents = new HashMap<>();
         Map<class_2338, Double> costs = new HashMap<>();
         open.add(new AutoEndPathfinder.Node(start, 0.0, heuristic(start, goals)));
         costs.put(start, 0.0);
         class_2338 best = start;
         double bestEstimate = heuristic(start, goals);
         int expanded = 0;

         while (!open.isEmpty()) {
            if (Thread.currentThread().isInterrupted()) {
               return List.of();
            }

            long elapsedMs = (System.nanoTime() - startedAt) / 1000000L;
            if (elapsedMs > primaryMs && best != start || elapsedMs > failureMs || expanded > 60000) {
               break;
            }

            AutoEndPathfinder.Node current = open.poll();
            Double known = costs.get(current.pos);
            if (known == null || !(current.cost > known + 1.0E-6)) {
               if (goals.contains(current.pos)) {
                  return rebuild(parents, current.pos);
               }

               expanded++;
               if (current.estimate < bestEstimate) {
                  bestEstimate = current.estimate;
                  best = current.pos;
               }

               for (int[] direction : DIRECTIONS) {
                  expand(world, current, direction[0], direction[1], goals, open, parents, costs);
               }
            }
         }

         return best == start ? List.of() : rebuild(parents, best);
      } else {
         return List.of();
      }
   }

   private static void expand(
      class_638 world,
      AutoEndPathfinder.Node current,
      int dx,
      int dz,
      Set<class_2338> goals,
      PriorityQueue<AutoEndPathfinder.Node> open,
      Map<class_2338, class_2338> parents,
      Map<class_2338, Double> costs
   ) {
      class_2338 from = current.pos;
      boolean diagonal = dx != 0 && dz != 0;
      double horizontal = diagonal ? 1.4142 : 1.0;
      if (!diagonal || clear(world, from.method_10069(dx, 0, 0)) && clear(world, from.method_10069(0, 0, dz))) {
         class_2338 level = from.method_10069(dx, 0, dz);
         if (walkable(world, level)) {
            push(level, current, current.cost + horizontal, goals, open, parents, costs);
         } else {
            if (!diagonal) {
               class_2338 up = from.method_10069(dx, 1, dz);
               if (walkable(world, up) && passable(world, from.method_10086(2))) {
                  push(up, current, current.cost + horizontal + 0.5, goals, open, parents, costs);
                  return;
               }
            }

            if (clear(world, level)) {
               for (int depth = 1; depth <= 20; depth++) {
                  class_2338 landing = level.method_10087(depth);
                  if (walkable(world, landing)) {
                     push(landing, current, current.cost + horizontal + depth * 0.2, goals, open, parents, costs);
                     return;
                  }

                  if (!passable(world, landing)) {
                     return;
                  }
               }
            }
         }
      }
   }

   private static void push(
      class_2338 next,
      AutoEndPathfinder.Node current,
      double cost,
      Set<class_2338> goals,
      PriorityQueue<AutoEndPathfinder.Node> open,
      Map<class_2338, class_2338> parents,
      Map<class_2338, Double> costs
   ) {
      Double known = costs.get(next);
      if (known == null || !(known <= cost)) {
         costs.put(next, cost);
         parents.put(next, current.pos);
         open.add(new AutoEndPathfinder.Node(next, cost, heuristic(next, goals)));
      }
   }

   private static List<class_2338> rebuild(Map<class_2338, class_2338> parents, class_2338 end) {
      List<class_2338> path = new ArrayList<>();
      class_2338 cursor = end;

      while (cursor != null) {
         path.add(cursor);
         cursor = parents.get(cursor);
      }

      Collections.reverse(path);
      return path;
   }

   private static double heuristic(class_2338 pos, Set<class_2338> goals) {
      double best = Double.MAX_VALUE;

      for (class_2338 goal : goals) {
         double dx = goal.method_10263() - pos.method_10263();
         double dy = goal.method_10264() - pos.method_10264();
         double dz = goal.method_10260() - pos.method_10260();
         best = Math.min(best, Math.sqrt(dx * dx + dz * dz) + Math.abs(dy) * 0.5);
      }

      return best;
   }

   static boolean walkable(class_638 world, class_2338 feet) {
      if (passable(world, feet) && passable(world, feet.method_10084())) {
         class_2338 below = feet.method_10074();
         class_2680 state = world.method_8320(below);
         return !state.method_26220(world, below).method_1110() && state.method_26227().method_15769();
      } else {
         return false;
      }
   }

   private static boolean clear(class_638 world, class_2338 feet) {
      return passable(world, feet) && passable(world, feet.method_10084());
   }

   static boolean passable(class_638 world, class_2338 pos) {
      if (!world.method_8393(pos.method_10263() >> 4, pos.method_10260() >> 4)) {
         return false;
      } else {
         class_2680 state = world.method_8320(pos);
         return state.method_26220(world, pos).method_1110() && state.method_26227().method_15769();
      }
   }

   private static final class Node implements Comparable<AutoEndPathfinder.Node> {
      final class_2338 pos;
      final double cost;
      final double estimate;

      Node(class_2338 pos, double cost, double estimate) {
         this.pos = pos;
         this.cost = cost;
         this.estimate = estimate;
      }

      public int compareTo(AutoEndPathfinder.Node other) {
         return Double.compare(this.cost + this.estimate, other.cost + other.estimate);
      }
   }
}
