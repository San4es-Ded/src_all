package wtf.wyvern.utility.game.player;

import java.util.Objects;
import lombok.Generated;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.util.PlayerInput;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.utility.component.FreeLookComponent;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@FastNative
public final class MovingUtil implements IMinecraft {
   public static boolean hasPlayerMovement() {
      return mc.player.input.movementForward != 0.0F || mc.player.input.movementSideways != 0.0F;
   }

   public static double[] calculateDirection(double distance) {
      float forward = mc.player.input.movementForward;
      float sideways = mc.player.input.movementSideways;
      float yaw = mc.player.getYaw();
      if (forward != 0.0F) {
         if (sideways > 0.0F) {
            yaw += forward > 0.0F ? -45.0F : 45.0F;
         } else if (sideways < 0.0F) {
            yaw += forward > 0.0F ? 45.0F : -45.0F;
         }

         sideways = 0.0F;
         forward = forward > 0.0F ? 1.0F : -1.0F;
      }

      double sinYaw = Math.sin(Math.toRadians((double)(yaw + 90.0F)));
      double cosYaw = Math.cos(Math.toRadians((double)(yaw + 90.0F)));
      double xMovement = (double)forward * distance * cosYaw + (double)sideways * distance * sinYaw;
      double zMovement = (double)forward * distance * sinYaw - (double)sideways * distance * cosYaw;
      return new double[]{xMovement, zMovement};
   }

   public static double getSpeed() {
      return Math.sqrt(mc.player.getVelocity().x * mc.player.getVelocity().x + mc.player.getVelocity().z * mc.player.getVelocity().z);
   }

   public static void strafe(double speed) {
      if (!hasPlayerMovement()) return;
      double[] dir = calculateDirection(speed);
      mc.player.setVelocity(dir[0], mc.player.getVelocity().y, dir[1]);
   }

   public static double getSpeedSqrt(Entity entity) {
      double dx = entity.getX() - entity.prevX;
      double dy = entity.getY() - entity.prevY;
      double dz = entity.getZ() - entity.prevZ;
      return Math.sqrt(dx * dx + dz * dz + dy * dy);
   }

   public static void setVelocity(double velocity) {
      double[] direction = calculateDirection(velocity);
      ((ClientPlayerEntity)Objects.requireNonNull(mc.player)).setVelocity(direction[0], mc.player.getVelocity().getY(), direction[1]);
   }

   public static void setVelocity(double velocity, double y) {
      double[] direction = calculateDirection(velocity);
      ((ClientPlayerEntity)Objects.requireNonNull(mc.player)).setVelocity(direction[0], y, direction[1]);
   }

   public static double getDegreesRelativeToView(Vec3d positionRelativeToPlayer, float yaw) {
      float optimalYaw = (float)Math.atan2(-positionRelativeToPlayer.x, positionRelativeToPlayer.z);
      double currentYaw = Math.toRadians((double)MathHelper.wrapDegrees(yaw));
      return Math.toDegrees(MathHelper.wrapDegrees((double)optimalYaw - currentYaw));
   }

   public static PlayerInput getDirectionalInputForDegrees(PlayerInput input, double dgs, float deadAngle) {
      boolean forwards = input.forward();
      boolean backwards = input.backward();
      boolean left = input.left();
      boolean right = input.right();
      if (dgs >= (double)(-90.0F + deadAngle) && dgs <= (double)(90.0F - deadAngle)) {
         forwards = true;
      } else if (dgs < (double)(-90.0F - deadAngle) || dgs > (double)(90.0F + deadAngle)) {
         backwards = true;
      }

      if (dgs >= (double)(0.0F + deadAngle) && dgs <= (double)(180.0F - deadAngle)) {
         right = true;
      } else if (dgs >= (double)(-180.0F + deadAngle) && dgs <= (double)(0.0F - deadAngle)) {
         left = true;
      }

      return new PlayerInput(forwards, backwards, left, right, input.jump(), input.sneak(), input.sprint());
   }

   public static void fixMovementFocus(EventMoveInput event, float yaw) {
      if (mc.player == null) return;
      fixMovementFromYaw(event, yaw, mc.player.getYaw());
   }

   public static void fixMovementTowards(EventMoveInput event, float targetYaw) {
      if (mc.player == null || event.getForward() <= 0.0F) return;
      float strength = Math.max(Math.abs(event.getForward()), Math.abs(event.getStrafe()));
      applyMovementForAngle(event, targetYaw, mc.player.getYaw(), strength);
   }

   public static void fixMovementFree(EventMoveInput event) {
      if (mc.player == null) return;
      float inputYaw = mc.player.isGliding() ? mc.player.getYaw() : FreeLookComponent.getFreeYaw();
      fixMovementFromYaw(event, inputYaw, mc.player.getYaw());
   }

   private static void fixMovementFromYaw(EventMoveInput event, float inputYaw, float movementYaw) {
      float forward = event.getForward();
      float strafe = event.getStrafe();
      float strength = Math.max(Math.abs(forward), Math.abs(strafe));
      if (strength <= 0.0F) return;

      float desiredAngle = MathHelper.wrapDegrees((float)Math.toDegrees(direction(inputYaw, forward, strafe)));
      applyMovementForAngle(event, desiredAngle, movementYaw, strength);
   }

   private static void applyMovementForAngle(EventMoveInput event, float desiredAngle,
                                             float movementYaw, float strength) {
      float bestForward = 0.0F;
      float bestStrafe = 0.0F;
      float smallestDifference = Float.MAX_VALUE;

      for (float testForward = -1.0F; testForward <= 1.0F; ++testForward) {
         for (float testStrafe = -1.0F; testStrafe <= 1.0F; ++testStrafe) {
            if (testForward == 0.0F && testStrafe == 0.0F) continue;
            float testAngle = MathHelper.wrapDegrees((float)Math.toDegrees(
                    direction(movementYaw, testForward, testStrafe)));
            float difference = Math.abs(MathHelper.wrapDegrees(desiredAngle - testAngle));
            if (difference < smallestDifference) {
               smallestDifference = difference;
               bestForward = testForward;
               bestStrafe = testStrafe;
            }
         }
      }

      // Keep slowdowns applied by other modules instead of turning every
      // corrected input back into a full-strength key press.
      event.setForward(bestForward * strength);
      event.setStrafe(bestStrafe * strength);
   }

   public static double direction(float rotationYaw, float moveForward, float moveStrafing) {
      if (moveForward < 0.0F) {
         rotationYaw += 180.0F;
      }

      float forward = 1.0F;
      if (moveForward < 0.0F) {
         forward = -0.5F;
      }

      if (moveForward > 0.0F) {
         forward = 0.5F;
      }

      if (moveStrafing > 0.0F) {
         rotationYaw -= 90.0F * forward;
      }

      if (moveStrafing < 0.0F) {
         rotationYaw += 90.0F * forward;
      }

      return Math.toRadians((double)rotationYaw);
   }

   public static PlayerInput getDirectionalInputForDegrees(PlayerInput input, double dgs) {
      return getDirectionalInputForDegrees(input, dgs, 20.0F);
   }

   @Generated
   private MovingUtil() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
