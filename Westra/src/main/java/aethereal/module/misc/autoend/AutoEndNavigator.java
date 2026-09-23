package aethereal.module.misc.autoend;

import aethereal.core.Interface;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadLocalRandom;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_243;
import net.minecraft.class_3532;

public final class AutoEndNavigator {
   private static final ExecutorService PLANNER = Executors.newSingleThreadExecutor(task -> {
      Thread thread = new Thread(task, "Westra-AutoEnd-Planner");
      thread.setDaemon(true);
      return thread;
   });
   private static final class_2350[] HORIZONTAL = new class_2350[]{
      class_2350.field_11043, class_2350.field_11035, class_2350.field_11039, class_2350.field_11034
   };
   private static final long PRIMARY_TIMEOUT_MS = 1000L;
   private static final long FAILURE_TIMEOUT_MS = 2000L;
   private static final long WATCHDOG_MS = 3000L;
   private static final int STUCK_TICKS = 45;
   private static final int OFF_PATH_TICKS = 3;
   private static final int MAX_REPLANS = 2;
   private static final int GOAL_CONFIRM_TICKS = 3;
   private static final double WAYPOINT_REACHED_SQ = 0.18;
   private List<class_2338> path = List.of();
   private Set<class_2338> exactGoals = Set.of();
   private class_2338 requestedGoal;
   private int horizontalTolerance;
   private int waypointIndex;
   private int stuckTicks;
   private int offPathTicks;
   private int replans;
   private int goalConfirmTicks;
   private class_243 lastPosition;
   private boolean active;
   private boolean failed;
   private boolean arrived;
   private float yawSpeed = 24.0F;
   private float yawSpeedTarget = 24.0F;
   private float pitchSpeed = 5.0F;
   private float pitchSpeedTarget = 5.0F;
   private float yawVelocity;
   private float pitchVelocity;
   private float yawBias;
   private float yawBiasTarget;
   private float pitchBias;
   private float pitchBiasTarget;
   private int turnProfileTicks;
   private int sharpTurnTicks;
   private double lateralOffset;
   private double lateralOffsetTarget;
   private int wanderTicks;
   private int lateralWaypointIndex = -1;
   private Future<AutoEndNavigator.RoutePlan> pendingPlan;
   private long planGeneration;
   private long planningStartedAt;
   private boolean planning;

   public boolean navigate(class_2338 goal, int tolerance, boolean force) {
      if (goal != null && Interface.aM_.field_1724 != null && Interface.aM_.field_1687 != null) {
         int clamped = Math.max(0, tolerance);
         boolean same = this.exactGoals.isEmpty() && goal.equals(this.requestedGoal) && clamped == this.horizontalTolerance;
         if (force || !same || !this.active && !this.planning) {
            this.requestedGoal = goal.method_10062();
            this.exactGoals = Set.of();
            this.horizontalTolerance = clamped;
            this.failed = false;
            this.arrived = false;
            if (!same || force) {
               this.replans = 0;
            }

            return this.plan();
         } else {
            return true;
         }
      } else {
         this.stop();
         return false;
      }
   }

   public boolean navigateToAny(List<class_2338> goals, boolean force) {
      if (goals != null && !goals.isEmpty() && Interface.aM_.field_1724 != null && Interface.aM_.field_1687 != null) {
         Set<class_2338> unique = new HashSet<>();

         for (class_2338 goal : goals) {
            if (goal != null) {
               unique.add(goal.method_10062());
            }
         }

         if (unique.isEmpty()) {
            this.stop();
            this.failed = true;
            return false;
         } else {
            boolean same = unique.equals(this.exactGoals);
            if (force || !same || !this.active && !this.planning) {
               this.exactGoals = Set.copyOf(unique);
               this.requestedGoal = this.exactGoals.iterator().next();
               this.horizontalTolerance = 0;
               this.failed = false;
               this.arrived = false;
               if (!same || force) {
                  this.replans = 0;
               }

               return this.plan();
            } else {
               return true;
            }
         }
      } else {
         this.stop();
         this.failed = true;
         return false;
      }
   }

   public void tick() {
      this.collectPlan();
      if (this.planning || !this.active || Interface.aM_.field_1724 == null || Interface.aM_.field_1687 == null || Interface.aM_.field_1690 == null) {
         this.releaseKeys();
      } else if (this.atGoal()) {
         this.releaseKeys();
         this.goalConfirmTicks = Interface.aM_.field_1724.method_24828() ? this.goalConfirmTicks + 1 : 0;
         if (this.goalConfirmTicks >= 3) {
            this.finish();
         }
      } else {
         this.goalConfirmTicks = 0;
         this.advanceWaypoints();
         if (this.active && this.waypointIndex < this.path.size()) {
            class_2338 waypoint = this.path.get(this.waypointIndex);
            int feetY = class_3532.method_15357(Interface.aM_.field_1724.method_23318() + 0.01);
            if (Interface.aM_.field_1724.method_24828() && waypoint.method_10264() > feetY + 1) {
               this.releaseKeys();
               if (++this.offPathTicks >= 3) {
                  this.offPathTicks = 0;
                  if (this.replans++ < 2 && this.plan()) {
                     return;
                  }

                  this.active = false;
                  this.failed = true;
               }
            } else {
               this.offPathTicks = 0;
               float severity = this.turnSeverity(waypoint);
               this.updateLateralOffset(waypoint, feetY);
               class_243 target = this.lookAheadPoint(waypoint, feetY, severity);
               double dx = target.field_1352 - Interface.aM_.field_1724.method_23317();
               double dz = target.field_1350 - Interface.aM_.field_1724.method_23321();
               double flat = Math.sqrt(dx * dx + dz * dz);
               float targetYaw = (float)Math.toDegrees(Math.atan2(dz, dx)) - 90.0F;
               float targetPitch = (float)(-Math.toDegrees(Math.atan2(target.field_1351 - Interface.aM_.field_1724.method_23318(), Math.max(0.001, flat))));
               if (Interface.aM_.field_1724.method_24828()) {
                  this.updateTurnProfile();
                  this.yawBias = this.yawBias + class_3532.method_15363(this.yawBiasTarget - this.yawBias, -0.18F, 0.18F);
                  this.pitchBias = this.pitchBias + class_3532.method_15363(this.pitchBiasTarget - this.pitchBias, -0.09F, 0.09F);
                  float yawError = class_3532.method_15393(targetYaw + this.yawBias - Interface.aM_.field_1724.method_36454());
                  float pitchError = class_3532.method_15363(targetPitch + this.pitchBias - Interface.aM_.field_1724.method_36455(), -30.0F, 30.0F);
                  float maxYaw = this.yawSpeed * class_3532.method_16439(severity, 0.92F, 1.12F);
                  float wantedYaw = approach(yawError, maxYaw, 48.0F);
                  float wantedPitch = approach(pitchError, this.pitchSpeed, 25.0F);
                  float yawAcceleration = this.sharpTurnTicks > 0 ? 10.0F : 5.25F;
                  this.yawVelocity = this.yawVelocity + class_3532.method_15363(wantedYaw - this.yawVelocity, -yawAcceleration, yawAcceleration);
                  this.pitchVelocity = this.pitchVelocity + class_3532.method_15363(wantedPitch - this.pitchVelocity, -1.35F, 1.35F);
                  float yawStep = Math.abs(this.yawVelocity) > Math.abs(yawError) ? yawError : this.yawVelocity;
                  float pitchStep = Math.abs(this.pitchVelocity) > Math.abs(pitchError) ? pitchError : this.pitchVelocity;
                  Interface.aM_.field_1724.method_36456(Interface.aM_.field_1724.method_36454() + yawStep);
                  Interface.aM_.field_1724.method_36457(class_3532.method_15363(Interface.aM_.field_1724.method_36455() + pitchStep, -35.0F, 35.0F));
                  if (yawStep == yawError) {
                     this.yawVelocity *= 0.35F;
                  }

                  if (pitchStep == pitchError) {
                     this.pitchVelocity *= 0.35F;
                  }
               }

               boolean climbing = waypoint.method_10264() > feetY;
               float facing = class_3532.method_15393(targetYaw - Interface.aM_.field_1724.method_36454());
               boolean aligned = Math.abs(facing) <= (climbing ? 52.0F : 42.0F);
               Interface.aM_.field_1690.field_1894.method_23481(aligned);
               Interface.aM_.field_1690.field_1881.method_23481(false);
               Interface.aM_.field_1690.field_1913.method_23481(false);
               Interface.aM_.field_1690.field_1849.method_23481(false);
               Interface.aM_.field_1690.field_1867.method_23481(aligned);
               Interface.aM_.field_1690.field_1903.method_23481(Interface.aM_.field_1724.method_24828() && aligned && climbing);
               this.checkStuck();
            }
         } else {
            if (this.atGoal()) {
               this.finish();
            } else if (this.replans++ < 2) {
               this.plan();
            } else {
               this.active = false;
               this.failed = true;
               this.releaseKeys();
            }
         }
      }
   }

   private void updateTurnProfile() {
      if (this.turnProfileTicks-- <= 0) {
         ThreadLocalRandom random = ThreadLocalRandom.current();
         if (random.nextFloat() < 0.16F) {
            this.yawSpeedTarget = random.nextFloat(42.0F, 56.0F);
            this.sharpTurnTicks = random.nextInt(2, 5);
            this.turnProfileTicks = this.sharpTurnTicks;
         } else {
            this.yawSpeedTarget = random.nextFloat(24.0F, 36.0F);
            this.sharpTurnTicks = 0;
            this.turnProfileTicks = random.nextInt(8, 19);
         }

         this.pitchSpeedTarget = random.nextFloat(6.0F, 10.0F);
         this.yawBiasTarget = random.nextFloat(-1.65F, 1.65F);
         this.pitchBiasTarget = random.nextFloat(-0.7F, 0.7F);
      } else if (this.sharpTurnTicks > 0) {
         this.sharpTurnTicks--;
      }

      float limit = this.sharpTurnTicks > 0 ? 7.0F : 1.25F;
      this.yawSpeed = this.yawSpeed + class_3532.method_15363(this.yawSpeedTarget - this.yawSpeed, -limit, limit);
      this.pitchSpeed = this.pitchSpeed + class_3532.method_15363(this.pitchSpeedTarget - this.pitchSpeed, -0.35F, 0.35F);
   }

   private void updateLateralOffset(class_2338 waypoint, int feetY) {
      if (waypoint.method_10264() != feetY) {
         this.lateralOffsetTarget = 0.0;
      } else if (this.lateralWaypointIndex != this.waypointIndex || this.wanderTicks-- <= 0) {
         ThreadLocalRandom random = ThreadLocalRandom.current();
         double offset = random.nextDouble(-0.18, 0.18);
         if (Math.abs(offset) < 0.065) {
            offset = Math.copySign(0.065, offset == 0.0 ? random.nextDouble() - 0.5 : offset);
         }

         this.lateralOffsetTarget = offset;
         this.wanderTicks = random.nextInt(18, 39);
         this.lateralWaypointIndex = this.waypointIndex;
      }

      this.lateralOffset = this.lateralOffset + class_3532.method_15350(this.lateralOffsetTarget - this.lateralOffset, -0.018, 0.018);
   }

   private class_243 lookAheadPoint(class_2338 waypoint, int feetY, float severity) {
      class_243 cursor = new class_243(
         Interface.aM_.field_1724.method_23317(), Interface.aM_.field_1724.method_23318(), Interface.aM_.field_1724.method_23321()
      );
      if (waypoint.method_10264() != feetY) {
         return new class_243(waypoint.method_10263() + 0.5, waypoint.method_10264(), waypoint.method_10260() + 0.5);
      } else {
         double remaining = class_3532.method_16436(severity, 1.65, 0.82);
         int level = waypoint.method_10264();

         for (int index = this.waypointIndex; index < this.path.size(); index++) {
            class_2338 node = this.path.get(index);
            if (index > this.waypointIndex && node.method_10264() != level) {
               return cursor;
            }

            class_243 point = this.offsetPoint(index, node);
            class_243 segment = point.method_1020(cursor);
            double length = segment.method_1033();
            if (length >= remaining && length > 1.0E-4) {
               return cursor.method_1019(segment.method_1021(remaining / length));
            }

            remaining -= length;
            cursor = point;
         }

         return cursor;
      }
   }

   private class_243 offsetPoint(int index, class_2338 node) {
      double dirX = 0.0;
      double dirZ = 0.0;
      if (index + 1 < this.path.size() && this.path.get(index + 1).method_10264() == node.method_10264()) {
         class_2338 next = this.path.get(index + 1);
         dirX = next.method_10263() - node.method_10263();
         dirZ = next.method_10260() - node.method_10260();
      } else if (index > 0 && this.path.get(index - 1).method_10264() == node.method_10264()) {
         class_2338 previous = this.path.get(index - 1);
         dirX = node.method_10263() - previous.method_10263();
         dirZ = node.method_10260() - previous.method_10260();
      }

      double length = Math.sqrt(dirX * dirX + dirZ * dirZ);
      if (length < 1.0E-4) {
         return new class_243(node.method_10263() + 0.5, node.method_10264(), node.method_10260() + 0.5);
      } else {
         long seed = node.method_10063() * 31L + index * -7046029254386353131L;
         double jitter = ((seed >>> 24 & 255L) / 255.0 - 0.5) * 0.055;
         double offset = class_3532.method_15350(this.lateralOffset + jitter, -0.18, 0.18);
         double normalX = -dirZ / length;
         double normalZ = dirX / length;
         return new class_243(node.method_10263() + 0.5 + normalX * offset, node.method_10264(), node.method_10260() + 0.5 + normalZ * offset);
      }
   }

   private float turnSeverity(class_2338 waypoint) {
      if (this.waypointIndex + 1 >= this.path.size()) {
         return 0.0F;
      } else {
         class_2338 next = this.path.get(this.waypointIndex + 1);
         if (next.method_10264() != waypoint.method_10264()) {
            return 1.0F;
         } else {
            double toX = waypoint.method_10263() + 0.5 - Interface.aM_.field_1724.method_23317();
            double toZ = waypoint.method_10260() + 0.5 - Interface.aM_.field_1724.method_23321();
            double alongX = next.method_10263() - waypoint.method_10263();
            double alongZ = next.method_10260() - waypoint.method_10260();
            double toLength = Math.sqrt(toX * toX + toZ * toZ);
            double alongLength = Math.sqrt(alongX * alongX + alongZ * alongZ);
            if (!(toLength < 1.0E-4) && !(alongLength < 1.0E-4)) {
               double cosine = class_3532.method_15350((toX * alongX + toZ * alongZ) / (toLength * alongLength), -1.0, 1.0);
               return (float)class_3532.method_15350(Math.acos(cosine) / (Math.PI / 2), 0.0, 1.0);
            } else {
               return 0.0F;
            }
         }
      }
   }

   private static float approach(float error, float maxSpeed, float slowAngle) {
      float absolute = Math.abs(error);
      if (absolute < 0.001F) {
         return 0.0F;
      } else {
         float t = class_3532.method_15363(absolute / slowAngle, 0.0F, 1.0F);
         float eased = t * t * (3.0F - 2.0F * t);
         float speed = Math.max(absolute * 0.3F, maxSpeed * eased);
         return Math.copySign(Math.min(absolute, Math.min(maxSpeed, speed)), error);
      }
   }

   private void resetMotion() {
      this.yawVelocity = 0.0F;
      this.pitchVelocity = 0.0F;
      this.yawBias = 0.0F;
      this.yawBiasTarget = 0.0F;
      this.pitchBias = 0.0F;
      this.pitchBiasTarget = 0.0F;
      this.turnProfileTicks = 0;
      this.sharpTurnTicks = 0;
      this.lateralOffset = 0.0;
      this.lateralOffsetTarget = 0.0;
      this.wanderTicks = 0;
      this.lateralWaypointIndex = -1;
   }

   public void stop() {
      this.cancelPlan();
      this.active = false;
      this.failed = false;
      this.arrived = false;
      this.path = List.of();
      this.waypointIndex = 0;
      this.requestedGoal = null;
      this.exactGoals = Set.of();
      this.stuckTicks = 0;
      this.offPathTicks = 0;
      this.goalConfirmTicks = 0;
      this.replans = 0;
      this.lastPosition = null;
      this.resetMotion();
      this.releaseKeys();
   }

   public void pauseMovement() {
      this.releaseKeys();
      this.stuckTicks = 0;
      this.offPathTicks = 0;
      if (Interface.aM_.field_1724 != null) {
         this.lastPosition = new class_243(
            Interface.aM_.field_1724.method_23317(), Interface.aM_.field_1724.method_23318(), Interface.aM_.field_1724.method_23321()
         );
      }
   }

   public boolean isWorking() {
      return this.active || this.planning;
   }

   public boolean hasFailed() {
      return this.failed;
   }

   public List<class_2338> getRemainingPath() {
      if (this.active && !this.path.isEmpty()) {
         int from = class_3532.method_15340(this.waypointIndex, 0, this.path.size());
         return from >= this.path.size() ? List.of() : List.copyOf(this.path.subList(from, this.path.size()));
      } else {
         return List.of();
      }
   }

   public boolean isWalkableNode(class_2338 pos) {
      return pos != null && Interface.aM_.field_1687 != null && AutoEndPathfinder.walkable(Interface.aM_.field_1687, pos);
   }

   private boolean plan() {
      this.cancelPlan();
      this.releaseKeys();
      this.active = false;
      this.path = List.of();
      this.waypointIndex = 0;
      this.stuckTicks = 0;
      this.offPathTicks = 0;
      this.goalConfirmTicks = 0;
      this.lastPosition = null;
      this.resetMotion();
      class_2338 start = this.findStart(Interface.aM_.field_1724.method_24515());
      if (start == null) {
         this.failed = true;
         return false;
      } else if (this.isGoal(start)) {
         if (this.atGoal()) {
            this.arrived = true;
            return true;
         } else {
            this.path = List.of(start.method_10062());
            this.waypointIndex = 0;
            this.active = true;
            this.failed = false;
            return true;
         }
      } else {
         Set<class_2338> goals = this.buildGoals();
         if (goals.isEmpty()) {
            this.failed = true;
            return false;
         } else {
            long generation = ++this.planGeneration;
            class_2338 immutableStart = start.method_10062();
            this.planning = true;
            this.planningStartedAt = System.nanoTime();
            this.failed = false;
            this.pendingPlan = PLANNER.submit(
               () -> new AutoEndNavigator.RoutePlan(generation, immutableStart, AutoEndPathfinder.find(immutableStart, goals, 1000L, 2000L))
            );
            return true;
         }
      }
   }

   private void collectPlan() {
      Future<AutoEndNavigator.RoutePlan> future = this.pendingPlan;
      if (this.planning && future != null) {
         if (!future.isDone()) {
            long elapsed = (System.nanoTime() - this.planningStartedAt) / 1000000L;
            if (elapsed >= 3000L) {
               future.cancel(true);
               this.pendingPlan = null;
               this.planning = false;
               this.active = false;
               this.failed = true;
               this.releaseKeys();
            }
         } else {
            this.pendingPlan = null;

            AutoEndNavigator.RoutePlan plan;
            try {
               plan = future.get();
            } catch (CancellationException var4) {
               this.planning = false;
               return;
            } catch (InterruptedException var5) {
               Thread.currentThread().interrupt();
               this.planning = false;
               this.failed = true;
               return;
            } catch (ExecutionException var6) {
               this.planning = false;
               this.failed = true;
               return;
            }

            this.planning = false;
            if (plan.generation() == this.planGeneration) {
               if (Interface.aM_.field_1724 != null && Interface.aM_.field_1687 != null && !plan.positions().isEmpty() && this.validRoute(plan.positions())) {
                  this.path = this.simplify(plan.positions());
                  this.waypointIndex = !this.path.isEmpty() && this.path.getFirst().equals(plan.start()) ? Math.min(1, this.path.size()) : 0;
                  this.active = this.waypointIndex < this.path.size();
                  this.arrived = !this.active && this.atGoal();
                  this.failed = false;
                  this.stuckTicks = 0;
                  this.goalConfirmTicks = 0;
                  this.lastPosition = new class_243(
                     Interface.aM_.field_1724.method_23317(), Interface.aM_.field_1724.method_23318(), Interface.aM_.field_1724.method_23321()
                  );
                  this.resetMotion();
               } else {
                  this.failed = true;
               }
            }
         }
      }
   }

   private void cancelPlan() {
      this.planGeneration++;
      Future<AutoEndNavigator.RoutePlan> future = this.pendingPlan;
      this.pendingPlan = null;
      this.planning = false;
      this.planningStartedAt = 0L;
      if (future != null && !future.isDone()) {
         future.cancel(true);
      }
   }

   private Set<class_2338> buildGoals() {
      Set<class_2338> goals = new HashSet<>();
      if (!this.exactGoals.isEmpty()) {
         goals.addAll(this.exactGoals);
      } else if (this.requestedGoal != null) {
         for (int dy = -4; dy <= 4; dy++) {
            for (int dx = -this.horizontalTolerance; dx <= this.horizontalTolerance; dx++) {
               for (int dz = -this.horizontalTolerance; dz <= this.horizontalTolerance; dz++) {
                  class_2338 candidate = this.requestedGoal.method_10069(dx, dy, dz);
                  if (this.isWalkableNode(candidate)) {
                     goals.add(candidate.method_10062());
                  }
               }
            }
         }

         if (goals.isEmpty() && this.isWalkableNode(this.requestedGoal)) {
            goals.add(this.requestedGoal);
         }
      }

      return goals;
   }

   private boolean validRoute(List<class_2338> route) {
      if (route.isEmpty()) {
         return false;
      } else {
         for (int index = 1; index < route.size(); index++) {
            class_2338 previous = route.get(index - 1);
            class_2338 current = route.get(index);
            int dx = Math.abs(current.method_10263() - previous.method_10263());
            int dz = Math.abs(current.method_10260() - previous.method_10260());
            int dy = current.method_10264() - previous.method_10264();
            if (dx > 1 || dz > 1 || dy > 1 || dy < -20 || dx == 0 && dz == 0 && dy == 0) {
               return false;
            }

            if (!this.isWalkableNode(current)) {
               return false;
            }
         }

         return true;
      }
   }

   private class_2338 findStart(class_2338 around) {
      if (this.isWalkableNode(around)) {
         return around.method_10062();
      } else {
         for (int depth = 1; depth <= 4; depth++) {
            class_2338 below = around.method_10087(depth);
            if (this.isWalkableNode(below)) {
               return below.method_10062();
            }
         }

         for (class_2350 direction : HORIZONTAL) {
            class_2338 side = around.method_10093(direction);
            if (this.isWalkableNode(side)) {
               return side.method_10062();
            }
         }

         return null;
      }
   }

   private boolean isGoal(class_2338 pos) {
      if (this.requestedGoal == null) {
         return false;
      } else if (!this.exactGoals.isEmpty()) {
         return this.exactGoals.contains(pos);
      } else {
         int dx = Math.abs(pos.method_10263() - this.requestedGoal.method_10263());
         int dz = Math.abs(pos.method_10260() - this.requestedGoal.method_10260());
         return Math.max(dx, dz) <= this.horizontalTolerance && Math.abs(pos.method_10264() - this.requestedGoal.method_10264()) <= 4;
      }
   }

   private boolean atGoal() {
      return Interface.aM_.field_1724 != null && this.isGoal(Interface.aM_.field_1724.method_24515());
   }

   private List<class_2338> simplify(List<class_2338> raw) {
      if (raw.size() < 3) {
         return List.copyOf(raw);
      } else {
         List<class_2338> result = new ArrayList<>();
         result.add(raw.getFirst());

         for (int index = 1; index < raw.size() - 1; index++) {
            class_2338 previous = raw.get(index - 1);
            class_2338 current = raw.get(index);
            class_2338 next = raw.get(index + 1);
            int inX = Integer.signum(current.method_10263() - previous.method_10263());
            int inZ = Integer.signum(current.method_10260() - previous.method_10260());
            int outX = Integer.signum(next.method_10263() - current.method_10263());
            int outZ = Integer.signum(next.method_10260() - current.method_10260());
            if (inX != outX || inZ != outZ || previous.method_10264() != current.method_10264() || current.method_10264() != next.method_10264()) {
               result.add(current);
            }
         }

         result.add(raw.getLast());
         return List.copyOf(result);
      }
   }

   private void advanceWaypoints() {
      int before;
      for (before = this.waypointIndex; this.waypointIndex < this.path.size(); this.waypointIndex++) {
         class_2338 waypoint = this.path.get(this.waypointIndex);
         double dx = Interface.aM_.field_1724.method_23317() - (waypoint.method_10263() + 0.5);
         double dz = Interface.aM_.field_1724.method_23321() - (waypoint.method_10260() + 0.5);
         boolean inside = Interface.aM_.field_1724.method_24515().equals(waypoint);
         boolean reached = dx * dx + dz * dz <= 0.18 && Math.abs(Interface.aM_.field_1724.method_23318() - waypoint.method_10264()) <= 1.1;
         if (!reached && !inside && !this.passedWaypoint(waypoint)) {
            return;
         }
      }

      if (this.waypointIndex != before) {
         this.stuckTicks = 0;
         this.lastPosition = new class_243(
            Interface.aM_.field_1724.method_23317(), Interface.aM_.field_1724.method_23318(), Interface.aM_.field_1724.method_23321()
         );
      }
   }

   private boolean passedWaypoint(class_2338 waypoint) {
      if (this.waypointIndex > 0 && !(Math.abs(Interface.aM_.field_1724.method_23318() - waypoint.method_10264()) > 1.1)) {
         double playerX = Interface.aM_.field_1724.method_23317();
         double playerZ = Interface.aM_.field_1724.method_23321();
         double pointX = waypoint.method_10263() + 0.5;
         double pointZ = waypoint.method_10260() + 0.5;
         class_2338 previous = this.path.get(this.waypointIndex - 1);
         if (previous.method_10264() == waypoint.method_10264()
            && beyondSegment(previous.method_10263() + 0.5, previous.method_10260() + 0.5, pointX, pointZ, playerX, playerZ)) {
            return true;
         } else if (this.waypointIndex + 1 >= this.path.size()) {
            return false;
         } else {
            class_2338 next = this.path.get(this.waypointIndex + 1);
            return next.method_10264() == waypoint.method_10264()
               && onSegment(pointX, pointZ, next.method_10263() + 0.5, next.method_10260() + 0.5, playerX, playerZ);
         }
      } else {
         return false;
      }
   }

   private static boolean beyondSegment(double startX, double startZ, double endX, double endZ, double playerX, double playerZ) {
      double segX = endX - startX;
      double segZ = endZ - startZ;
      double lengthSq = segX * segX + segZ * segZ;
      if (lengthSq < 1.0E-4) {
         return false;
      } else {
         double t = ((playerX - startX) * segX + (playerZ - startZ) * segZ) / lengthSq;
         if (t <= 1.02) {
            return false;
         } else {
            double offX = playerX - (startX + segX * t);
            double offZ = playerZ - (startZ + segZ * t);
            return offX * offX + offZ * offZ <= 0.81;
         }
      }
   }

   private static boolean onSegment(double startX, double startZ, double endX, double endZ, double playerX, double playerZ) {
      double segX = endX - startX;
      double segZ = endZ - startZ;
      double lengthSq = segX * segX + segZ * segZ;
      if (lengthSq < 1.0E-4) {
         return false;
      } else {
         double t = ((playerX - startX) * segX + (playerZ - startZ) * segZ) / lengthSq;
         if (!(t <= 0.08) && !(t > 1.15)) {
            double offX = playerX - (startX + segX * t);
            double offZ = playerZ - (startZ + segZ * t);
            return offX * offX + offZ * offZ <= 0.64;
         } else {
            return false;
         }
      }
   }

   private void checkStuck() {
      class_243 position = new class_243(
         Interface.aM_.field_1724.method_23317(), Interface.aM_.field_1724.method_23318(), Interface.aM_.field_1724.method_23321()
      );
      if (this.lastPosition == null || position.method_1025(this.lastPosition) >= 0.006) {
         this.lastPosition = position;
         this.stuckTicks = 0;
      } else if (Interface.aM_.field_1724.method_24828() && ++this.stuckTicks >= 45) {
         this.stuckTicks = 0;
         if (this.replans++ >= 2 || !this.plan()) {
            this.active = false;
            this.failed = true;
            this.releaseKeys();
         }
      }
   }

   private void finish() {
      this.active = false;
      this.arrived = true;
      this.failed = false;
      this.releaseKeys();
   }

   private void releaseKeys() {
      if (Interface.aM_.field_1690 != null) {
         Interface.aM_.field_1690.field_1894.method_23481(false);
         Interface.aM_.field_1690.field_1881.method_23481(false);
         Interface.aM_.field_1690.field_1913.method_23481(false);
         Interface.aM_.field_1690.field_1849.method_23481(false);
         Interface.aM_.field_1690.field_1903.method_23481(false);
         Interface.aM_.field_1690.field_1867.method_23481(false);
      }
   }

   private record RoutePlan(long generation, class_2338 start, List<class_2338> positions) {
   }
}
