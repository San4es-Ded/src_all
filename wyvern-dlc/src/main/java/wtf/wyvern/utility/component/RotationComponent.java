package wtf.wyvern.utility.component;

import wtf.wyvern.core.eventbus.EventManager;
import wtf.wyvern.core.eventbus.EventTarget;
import lombok.Generated;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.wyvern.utility.game.player.rotation.GCDFixer;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.interfaces.IClient;
import wtf.astroguard.J2C.FastNative;

@FastNative
public class RotationComponent implements IClient {
   public static RotationComponent instance = new RotationComponent();
   private RotationTask currentTask;
   private float currentYawSpeed;
   private float currentPitchSpeed;
   private float currentYawReturnSpeed;
   private float currentPitchReturnSpeed;
   private int currentPriority;
   private int currentTimeout;
   private int idleTicks;
   private Rotation targetRotation;

   public RotationComponent() {
      this.currentTask = RotationTask.IDLE;
      this.targetRotation = new Rotation(0.0F, 0.0F);
      EventManager.register(this);
      new FreeLookComponent();
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

   @EventTarget
   public void onInput(EventMoveInput event) {
      if (this.isRotating() && !Aura.INSTANCE.shouldControlMovementCorrection()) {
         float cameraYaw = mc.gameRenderer.getCamera().getYaw();
         if (mc.options.getPerspective() == Perspective.THIRD_PERSON_FRONT) {
            cameraYaw -= 180.0F;
         }
         MovingUtil.fixMovementFocus(event, MathHelper.wrapDegrees(cameraYaw));
      }

   }

   private void resetRotation() {
      Rotation targetRotation = new Rotation(FreeLookComponent.getFreeYaw(), FreeLookComponent.getFreePitch());
      if (this.updateRotation(targetRotation, this.currentYawReturnSpeed(), this.currentPitchReturnSpeed())) {
         this.stopRotation();
      }

   }

   @EventTarget
   public void onEventTick(EventUpdate event) {
      if (this.currentTask().equals(RotationTask.AIM) && this.idleTicks() > this.currentTimeout()) {
         this.currentTask(RotationTask.RESET);
      }

      if (this.currentTask().equals(RotationTask.RESET)) {
         this.resetRotation();
      }

      ++this.idleTicks;
   }

   public static Vec2f applySensitivityPatch(Vec2f rotation, Vec2f previousRotation) {
      float yaw = previousRotation.x + GCDFixer.getFixRotate(rotation.x - previousRotation.x);
      float pitch = previousRotation.y + GCDFixer.getFixRotate(rotation.y - previousRotation.y);
      return new Vec2f(yaw, pitch);
   }

   public static Vec2f applyVanillaSensitivityPatch(Vec2f rotation, Vec2f previousRotation) {
      float gcd = Rotation.gcd();
      if (!(gcd > 0.0F) || !Float.isFinite(gcd)) {
         return rotation;
      }

      float yaw = previousRotation.x
              + Math.round((rotation.x - previousRotation.x) / gcd) * gcd;
      float pitch = previousRotation.y
              + Math.round((rotation.y - previousRotation.y) / gcd) * gcd;
      return new Vec2f(yaw, pitch);
   }

   public static void update(Rotation target, float yawSpeed, float pitchSpeed, float yawReturnSpeed, float pitchReturnSpeed, int timeout, int priority, boolean clientRotation) {
      updateInternal(target, yawSpeed, pitchSpeed, yawReturnSpeed, pitchReturnSpeed,
              timeout, priority, clientRotation, false);
   }

   public static void updateVanillaGcd(Rotation target, float yawSpeed, float pitchSpeed,
                                       float yawReturnSpeed, float pitchReturnSpeed,
                                       int timeout, int priority, boolean clientRotation) {
      updateInternal(target, yawSpeed, pitchSpeed, yawReturnSpeed, pitchReturnSpeed,
              timeout, priority, clientRotation, true);
   }

   private static void updateInternal(Rotation target, float yawSpeed, float pitchSpeed,
                                      float yawReturnSpeed, float pitchReturnSpeed,
                                      int timeout, int priority, boolean clientRotation,
                                      boolean vanillaGcd) {
      RotationComponent instance = RotationComponent.instance;
      if (instance.currentPriority() <= priority) {
         if (instance.currentTask().equals(RotationTask.IDLE) && !clientRotation) {
            FreeLookComponent.setActive(true);
         }

         instance.currentYawSpeed(yawSpeed);
         instance.currentPitchSpeed(pitchSpeed);
         instance.currentYawReturnSpeed(yawReturnSpeed);
         instance.currentPitchReturnSpeed(pitchReturnSpeed);
         instance.currentTimeout(timeout);
         instance.currentPriority(priority);
         instance.currentTask(RotationTask.AIM);
         instance.targetRotation(target);
         instance.updateRotation(target, yawSpeed, pitchSpeed, vanillaGcd);
      }
   }

   public static void update(Rotation targetRotation, float turnSpeed, float returnSpeed, int timeout, int priority) {
      update(targetRotation, turnSpeed, turnSpeed, returnSpeed, returnSpeed, timeout, priority, false);
   }

   public static void update(Rotation targetRotation, float yawSpeed, float pitchSpeed, float returnSpeed, int timeout, int priority) {
      update(targetRotation, yawSpeed, pitchSpeed, returnSpeed, returnSpeed, timeout, priority, false);
   }

   private boolean updateRotation(Rotation rotation, float turnYawSpeed, float turnPitchSpeed) {
      return this.updateRotation(rotation, turnYawSpeed, turnPitchSpeed, false);
   }

   private boolean updateRotation(Rotation rotation, float turnYawSpeed, float turnPitchSpeed,
                                  boolean vanillaGcd) {
      if (mc.player == null) {
         return false;
      } else {
         Rotation currentRotation = new Rotation(mc.player.getYaw(), mc.player.getPitch());
         float yawDelta = MathHelper.wrapDegrees(rotation.getYaw() - currentRotation.getYaw());
         float pitchDelta = rotation.getPitch() - currentRotation.getPitch();
         float totalDelta = Math.abs(yawDelta) + Math.abs(pitchDelta);
         float yawSpeed = totalDelta == 0.0F ? 0.0F : Math.abs(yawDelta / totalDelta) * turnYawSpeed;
         float pitchSpeed = totalDelta == 0.0F ? 0.0F : Math.abs(pitchDelta / totalDelta) * turnPitchSpeed;
         Vec2f requested = new Vec2f(
                 mc.player.getYaw() + MathHelper.clamp(yawDelta, -yawSpeed, yawSpeed),
                 MathHelper.clamp(mc.player.getPitch()
                         + MathHelper.clamp(pitchDelta, -pitchSpeed, pitchSpeed), -90.0F, 90.0F));
         Vec2f previous = new Vec2f(mc.player.getYaw(), mc.player.getPitch());
         Vec2f rot = vanillaGcd
                 ? applyVanillaSensitivityPatch(requested, previous)
                 : applySensitivityPatch(requested, previous);
         mc.player.setYaw(rot.x);
         mc.player.setPitch(rot.y);
         Rotation finalRotation = new Rotation(mc.player.getYaw(), mc.player.getPitch());
         this.idleTicks(0);
         return (double)finalRotation.getDelta(rotation) < (this.currentTask.equals(RotationTask.RESET) ? Math.hypot((double)this.currentYawReturnSpeed, (double)this.currentPitchReturnSpeed) : Math.hypot((double)this.currentYawSpeed, (double)this.currentPitchSpeed));
      }
   }

   public void stopRotation() {
      this.currentTask(RotationTask.IDLE);
      this.currentPriority(0);
      FreeLookComponent.setActive(false);
   }

   public boolean isRotating() {
      return !this.currentTask.equals(RotationTask.IDLE);
   }

   @Generated
   public RotationTask currentTask() {
      return this.currentTask;
   }

   @Generated
   public float currentYawSpeed() {
      return this.currentYawSpeed;
   }

   @Generated
   public float currentPitchSpeed() {
      return this.currentPitchSpeed;
   }

   @Generated
   public float currentYawReturnSpeed() {
      return this.currentYawReturnSpeed;
   }

   @Generated
   public float currentPitchReturnSpeed() {
      return this.currentPitchReturnSpeed;
   }

   @Generated
   public int currentPriority() {
      return this.currentPriority;
   }

   @Generated
   public int currentTimeout() {
      return this.currentTimeout;
   }

   @Generated
   public int idleTicks() {
      return this.idleTicks;
   }

   @Generated
   public Rotation targetRotation() {
      return this.targetRotation;
   }

   @Generated
   public RotationComponent currentTask(RotationTask currentTask) {
      this.currentTask = currentTask;
      return this;
   }

   @Generated
   public RotationComponent currentYawSpeed(float currentYawSpeed) {
      this.currentYawSpeed = currentYawSpeed;
      return this;
   }

   @Generated
   public RotationComponent currentPitchSpeed(float currentPitchSpeed) {
      this.currentPitchSpeed = currentPitchSpeed;
      return this;
   }

   @Generated
   public RotationComponent currentYawReturnSpeed(float currentYawReturnSpeed) {
      this.currentYawReturnSpeed = currentYawReturnSpeed;
      return this;
   }

   @Generated
   public RotationComponent currentPitchReturnSpeed(float currentPitchReturnSpeed) {
      this.currentPitchReturnSpeed = currentPitchReturnSpeed;
      return this;
   }

   @Generated
   public RotationComponent currentPriority(int currentPriority) {
      this.currentPriority = currentPriority;
      return this;
   }

   @Generated
   public RotationComponent currentTimeout(int currentTimeout) {
      this.currentTimeout = currentTimeout;
      return this;
   }

   @Generated
   public RotationComponent idleTicks(int idleTicks) {
      this.idleTicks = idleTicks;
      return this;
   }

   @Generated
   public RotationComponent targetRotation(Rotation targetRotation) {
      this.targetRotation = targetRotation;
      return this;
   }

   public static enum RotationTask {
      AIM,
      RESET,
      IDLE;

      private static RotationTask[] $values() {
         return new RotationTask[]{AIM, RESET, IDLE};
      }
   }
}
