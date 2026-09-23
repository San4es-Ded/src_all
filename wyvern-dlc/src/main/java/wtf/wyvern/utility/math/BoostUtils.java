package wtf.wyvern.utility.math;

import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@FastNative
public final class BoostUtils implements IMinecraft {
   private static final float[] YAW_TABLE = new float[]{
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.62F,
           1.62F,
           1.62F,
           1.63F,
           1.63F,
           1.64F,
           1.65F,
           1.65F,
           1.66F,
           1.67F,
           1.68F,
           1.69F,
           1.7F,
           1.71F,
           1.72F,
           1.73F,
           1.73F,
           1.75F,
           1.76F,
           1.78F,
           1.79F,
           1.81F,
           1.83F,
           1.85F,
           1.87F,
           1.89F,
           1.91F,
           1.93F,
           1.95F,
           1.98F,
           2.01F,
           2.03F,
           2.06F,
           2.09F,
           2.12F,
           2.16F,
           2.19F,
           2.23F,
           2.27F,
           2.31F,
           2.35F,
           2.31F,
           2.27F,
           2.23F,
           2.19F,
           2.16F,
           2.12F,
           2.09F,
           2.06F,
           2.03F,
           2.01F,
           1.98F,
           1.95F,
           1.93F,
           1.89F,
           1.87F,
           1.85F,
           1.83F,
           1.81F,
           1.79F,
           1.78F,
           1.76F,
           1.75F,
           1.73F,
           1.72F,
           1.71F,
           1.7F,
           1.69F,
           1.68F,
           1.67F,
           1.66F,
           1.65F,
           1.64F,
           1.63F,
           1.63F,
           1.63F,
           1.62F,
           1.62F,
           1.62F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F
   };
   private static final float[] PITCH_TABLE = new float[]{
           1.61F,
           1.61F,
           1.61F,
           1.62F,
           1.62F,
           1.62F,
           1.63F,
           1.63F,
           1.64F,
           1.65F,
           1.65F,
           1.66F,
           1.67F,
           1.68F,
           1.69F,
           1.7F,
           1.71F,
           1.72F,
           1.73F,
           1.73F,
           1.75F,
           1.76F,
           1.78F,
           1.79F,
           1.81F,
           1.83F,
           1.85F,
           1.87F,
           1.89F,
           1.91F,
           1.93F,
           1.95F,
           1.98F,
           2.01F,
           2.03F,
           2.06F,
           2.09F,
           2.12F,
           2.16F,
           2.19F,
           2.23F,
           2.24F,
           2.21F,
           2.21F,
           2.21F,
           2.23F,
           2.23F,
           2.19F,
           2.16F,
           2.12F,
           2.09F,
           2.06F,
           2.03F,
           2.01F,
           1.98F,
           1.95F,
           1.93F,
           1.89F,
           1.87F,
           1.85F,
           1.83F,
           1.81F,
           1.79F,
           1.78F,
           1.76F,
           1.75F,
           1.73F,
           1.72F,
           1.71F,
           1.7F,
           1.69F,
           1.68F,
           1.67F,
           1.66F,
           1.65F,
           1.64F,
           1.63F,
           1.63F,
           1.63F,
           1.62F,
           1.62F,
           1.62F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F,
           1.61F
   };

   public static Vec3d getBoost(LivingEntity entity) {
      float speed = getRageSpeed(entity);
      Vec3d vec3d = entity.getRotationVector();
      Vec3d oldVelocity = Vec3d.fromPolar(entity.getPitch(), entity.getYaw()).multiply(speed);
      float f = entity.getPitch() * (float) (Math.PI / 180.0);
      double d = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
      double e = oldVelocity.horizontalLength();
      boolean bl = entity.getVelocity().y <= 0.0;
      double g = bl && entity.hasStatusEffect(StatusEffects.SLOW_FALLING) ? Math.min(entity.getFinalGravity(), 0.01) : entity.getFinalGravity();
      double h = MathHelper.square(Math.cos(f));
      oldVelocity = oldVelocity.add(0.0, g * (-1.0 + h * 0.75), 0.0);
      if (oldVelocity.y < 0.0 && d > 0.0) {
         double i = oldVelocity.y * -0.1 * h;
         oldVelocity = oldVelocity.add(vec3d.x * i / d, i, vec3d.z * i / d);
      }

      if (f < 0.0F && d > 0.0) {
         double i = e * -MathHelper.sin(f) * 0.04;
         oldVelocity = oldVelocity.add(-vec3d.x * i / d, i * 3.2, -vec3d.z * i / d);
      }

      if (d > 0.0) {
         oldVelocity = oldVelocity.add((vec3d.x / d * e - oldVelocity.x) * 0.1, 0.0, (vec3d.z / d * e - oldVelocity.z) * 0.1);
      }

      double length = oldVelocity.length();
      float speedXZ = resolveRocketSpeedXZ((float)(length * 0.99D), entity.getPitch());
      float speedY = resolveRocketSpeedY((float)(length * 0.98D));
      return new Vec3d(speedXZ, speedY, speedXZ);
   }

   private static float getRageSpeed(LivingEntity entity) {
      float yawAbs = Math.abs(MathHelper.wrapDegrees(entity.getYaw()));
      float yawFolded = foldYaw(yawAbs);
      float pitchAbs = Math.abs(clampPitch(entity.getPitch()));
      if (pitchAbs >= 70.0F && pitchAbs <= 90.0F) {
         return 1.615F;
      } else {
         float yawSpeed = YAW_TABLE[Math.min((int)Math.ceil(yawFolded), 90)];
         int pitchIndex = Math.min((int)Math.ceil(pitchAbs), PITCH_TABLE.length - 1);
         float pitchSpeed = PITCH_TABLE[pitchIndex];
         float speed = pitchAbs >= 75.0F ? pitchSpeed : Math.max(yawSpeed, pitchSpeed);
         return Math.max(speed, pitchAbs >= 75.0F ? 1.5F : 1.61F);
      }
   }

   private static float foldYaw(float yawAbs) {
      float folded180 = yawAbs > 180.0F ? 360.0F - yawAbs : yawAbs;
      return folded180 > 90.0F ? 180.0F - folded180 : folded180;
   }

   private static float clampPitch(float pitch) {
      return Math.max(-90.0F, Math.min(90.0F, pitch));
   }

   public static Vec3d getBoostAntiTarget(LivingEntity entity, float speedSetting) {
      float yaw = Math.abs((entity.getYaw() - 360.0F) % 360.0F);
      float pitch = entity.getPitch();
      float absPitch = Math.abs(pitch);
      float pitchBonus = 0.0F;
      if (absPitch >= 30.0F && absPitch <= 50.0F) {
         pitchBonus = 0.15F;
      } else if (absPitch >= 25.0F && absPitch <= 55.0F) {
         pitchBonus = 0.1F;
      } else if (absPitch >= 20.0F && absPitch <= 60.0F) {
         pitchBonus = 0.05F;
      }

      float speed = speedSetting + pitchBonus;
      float[] centers = new float[]{45.0F, 135.0F, 225.0F, 315.0F};
      float minDiff = 9999.0F;

      for (float c : centers) {
         float diff = Math.abs(yaw - c);
         if (diff < minDiff) {
            minDiff = diff;
         }
      }

      if (minDiff < 15.0F) {
         speed += 0.1F;
      } else if (minDiff < 25.0F) {
         speed += 0.05F;
      }

      speed = Math.min(speed, 2.8F);
      return new Vec3d(speed, speed, speed);
   }

   public static Vec3d getBoostAntiTargetFast(LivingEntity entity) {
      float yaw = Math.abs((entity.getYaw() - 360.0F) % 360.0F);
      float pitch = entity.getPitch();
      float absPitch = Math.abs(pitch);
      float speedXZ = 2.5F;
      float speedY = 2.3F;
      if (absPitch >= 35.0F && absPitch <= 50.0F) {
         speedXZ = 2.7F;
         speedY = 2.5F;
      } else if (absPitch >= 30.0F && absPitch <= 55.0F) {
         speedXZ = 2.6F;
         speedY = 2.4F;
      }

      float[] centers = new float[]{45.0F, 135.0F, 225.0F, 315.0F};
      float minDiff = 9999.0F;

      for (float c : centers) {
         float diff = Math.abs(yaw - c);
         if (diff < minDiff) {
            minDiff = diff;
         }
      }

      if (minDiff < 20.0F) {
         speedXZ += 0.15F;
      }

      return new Vec3d(speedXZ, speedY, speedXZ);
   }

   public static Vec3d getBoostAntiTargetWithAura(LivingEntity entity, float auraRotatePitch, float auraRotateYaw, float speedSetting) {
      float absPitch = Math.abs(auraRotatePitch);
      float speedXZ = speedSetting;
      float speedY;
      if (absPitch >= 38.0F && absPitch <= 52.0F) {
         speedXZ = Math.min(speedSetting + 0.2F, 2.7F);
         speedY = Math.min(speedSetting + 0.15F, 2.5F);
      } else if (absPitch >= 30.0F && absPitch <= 60.0F) {
         speedXZ = Math.min(speedSetting + 0.1F, 2.6F);
         speedY = Math.min(speedSetting + 0.1F, 2.4F);
      } else if (absPitch >= 25.0F && absPitch <= 65.0F) {
         speedY = speedSetting - 0.05F;
      } else {
         speedXZ = speedSetting - 0.1F;
         speedY = speedSetting - 0.15F;
      }

      return new Vec3d(speedXZ, speedY, speedXZ);
   }
   // бро это бравохвхФФААА бустер понимаеш ?? вон там меняй в float speed; себе спид
   public static Vec3d getBoostBravoFFA(LivingEntity entity) {
      float pitch = entity.getPitch();
      float yaw = entity.getYaw();
      float xz = resolveRocketSpeedXZ(Math.max(bravoFFASpeedXZ(pitch, yaw), 2.05F), pitch);
      float y = resolveRocketSpeedY(Math.max(bravoFFASpeedY(pitch), 2.05F));
      return new Vec3d(xz, y, xz);
   }

   private static float bravoFFASpeedXZ(float pitch, float yaw) {
      float absPitch = Math.abs(pitch);
      float absYaw = Math.abs(MathHelper.wrapDegrees(yaw) % 90.0F);

      float speed;
      if (absPitch >= 38.0F && absPitch <= 52.0F) speed = 2.0F;
      else if (absPitch >= 32.0F && absPitch <= 58.0F) speed = 1.96F; // бро да я поменял скорость
      else if (absPitch >= 28.0F && absPitch <= 62.0F) speed = 1.95F; // бро ты поменял скорость реально
      else if ((absYaw >= 29.0F && absYaw <= 61.0F) || (absPitch >= 29.0F && absPitch <= 61.0F)) speed = 2.0575F; // бро это по диагонали
      else if ((absYaw >= 28.0F && absYaw <= 60.0F) || (absPitch >= 28.0F && absPitch <= 60.0F)) speed = 1.965F; // бро это всего +1 добавь скорости
      else if ((absYaw >= 26.0F && absYaw <= 64.0F) || (absPitch >= 26.0F && absPitch <= 64.0F)) speed = 1.865F; // бро почти диагональ
      else if ((absYaw >= 24.0F && absYaw <= 66.0F) || (absPitch >= 24.0F && absPitch <= 66.0F)) speed = 1.84F; // бро поменяй скорость тут
      else if ((absYaw >= 15.0F && absYaw <= 75.0F) || (absPitch >= 15.0F && absPitch <= 75.0F)) speed = 1.79F; // бро тут также
      else if ((absYaw >= 13.0F && absYaw <= 77.0F) || (absPitch >= 13.0F && absPitch <= 77.0F)) speed = 1.76F; // бро "череп"
      else if ((absYaw >= 12.0F && absYaw <= 78.0F) || (absPitch >= 12.0F && absPitch <= 78.0F)) speed = 1.73F; // бро скорость такая же
      else if ((absYaw >= 8.0F && absYaw <= 82.0F) || (absPitch >= 11.0F && absPitch <= 79.0F)) speed = 1.71F; // бро чуть дальше прямой
      else if ((absYaw >= 5.0F && absYaw <= 85.0F) || (absPitch >= 8.0F && absPitch <= 82.0F)) speed = 1.67F; // бро это по прямой
      else speed = 1.71F;

      return pitch > 15.0F ? speed - 0.068F : speed;
   }

   private static float bravoFFASpeedY(float pitch) {
      float absPitch = Math.abs(pitch);
      if (absPitch >= 37.0F && absPitch <= 38.0F) return 2.055F;
      if (absPitch >= 25.0F && absPitch <= 30.0F) return 2.0F;
      if (absPitch >= 35.0F && absPitch <= 45.0F) return 1.99F;
      if (absPitch >= 40.0F && absPitch <= 50.0F) return 1.97F;
      if (absPitch >= 50.0F && absPitch <= 60.0F) return 1.96F;
      if (absPitch >= 51.0F && absPitch <= 61.0F) return 1.85F;
      if (absPitch >= 52.0F && absPitch <= 65.0F) return 1.8F;
      return 1.71F;
   }
   // под рв типоrrr бустер понимаешь???? вон там return getBoostCustom(entity,36.0F); меняй цифорку в 36.0F и будет быстрее лететь $$$
   public static Vec3d getBoostrw(LivingEntity entity) {
      return getBoostCustom(entity, 36.0F);
   }
   // бро кастомный бустер это кароче 0-5 5-10 5-15 вроде как если не забываю этот уже в самом ЕлитраБУСТЕР менRть надо
   public static Vec3d getBoostCustom(LivingEntity entity, float targetBps) {
      float maxSpeed = targetBps / 20.0F;
      float yaw = Math.abs((entity.getYaw() - 360.0F) % 360.0F);
      float pitch = entity.getPitch();
      float minSpeed = Math.min(maxSpeed * 0.7F, 1.67F);
      float[] centers = new float[]{45.0F, 135.0F, 225.0F, 315.0F};
      float minDiff = 9999.0F;

      for (float c : centers) {
         float diff = Math.abs(yaw - c);
         if (diff < minDiff) {
            minDiff = diff;
         }
      }

      float yawFactor = 1.0F - minDiff / 45.0F;
      yawFactor = Math.max(0.0F, Math.min(1.0F, yawFactor));
      float pitchFactor = getPitchFactor(pitch);
      float combinedFactor = yawFactor * pitchFactor;
      float speed = minSpeed + (maxSpeed - minSpeed) * combinedFactor;
      Vec3d vec3d = entity.getRotationVector();
      Vec3d oldVelocity = Vec3d.fromPolar(pitch, entity.getYaw()).multiply(speed);
      float f = pitch * (float) (Math.PI / 180.0);
      double d = Math.sqrt(vec3d.x * vec3d.x + vec3d.z * vec3d.z);
      double e = oldVelocity.horizontalLength();
      boolean bl = entity.getVelocity().y <= 0.0;
      double g = bl && entity.hasStatusEffect(StatusEffects.SLOW_FALLING) ? Math.min(entity.getFinalGravity(), 0.01) : entity.getFinalGravity();
      double h = MathHelper.square(Math.cos(f));
      oldVelocity = oldVelocity.add(0.0, g * (-1.0 + h * 0.75), 0.0);
      if (oldVelocity.y < 0.0 && d > 0.0) {
         double i = oldVelocity.y * -0.1 * h;
         oldVelocity = oldVelocity.add(vec3d.x * i / d, i, vec3d.z * i / d);
      }

      if (f < 0.0F && d > 0.0) {
         double i = e * -MathHelper.sin(f) * 0.04;
         oldVelocity = oldVelocity.add(-vec3d.x * i / d, i * 3.2, -vec3d.z * i / d);
      }

      if (d > 0.0) {
         oldVelocity = oldVelocity.add((vec3d.x / d * e - oldVelocity.x) * 0.1, 0.0, (vec3d.z / d * e - oldVelocity.z) * 0.1);
      }

      double length = oldVelocity.length();
      float speedXZ = resolveRocketSpeedXZ((float)(length * 0.99D), pitch);
      float speedY = resolveRocketSpeedY((float)(length * 0.98D));
      return new Vec3d(speedXZ, speedY, speedXZ);
   }

   public static Vec3d getBoostFixedBps(LivingEntity entity, float targetBps) {
      float speed = targetBps / 20.0F;
      float speedXZ = resolveRocketSpeedXZ(speed * 0.99F, entity.getPitch());
      float speedY = resolveRocketSpeedY(speed * 0.98F);
      return new Vec3d(speedXZ, speedY, speedXZ);
   }

   public static Vec3d fromSliderSettings(LivingEntity entity, float yawSpeed, float pitchSpeed) {
      return fromSliderSettings(entity, yawSpeed, pitchSpeed, entity.getPitch());
   }

   public static Vec3d fromSliderSettings(LivingEntity entity, float yawSpeed, float pitchSpeed, float pitch) {
      float speedXZ = resolveRocketSpeedXZ(yawSpeed, pitch);
      float speedY = resolveRocketSpeedY(pitchSpeed);
      return new Vec3d(speedXZ, speedY, speedXZ);
   }

   private static float resolveRocketSpeedXZ(float yawSpeed, float pitch) {
      return compensateRocketSpeed(yawSpeed * 0.99F, pitch);
   }

   /** Y uses a fixed scale and a Grim-safe cap; horizontal boost stays in {@link #resolveRocketSpeedXZ}. */
   private static float resolveRocketSpeedY(float pitchSpeed) {
      return Math.min(pitchSpeed * 0.98F, 1.575F);
   }

   private static float compensateRocketSpeed(float speed, float pitch) {
      float absPitch = Math.abs(pitch);
      if (absPitch >= 40.0F) {
         return speed;
      }
      float horizontalDirection = (float)Math.cos(Math.toRadians(absPitch));
      return (speed + 0.2F) / Math.max(horizontalDirection, 0.6F) - 0.2F;
   }

   private static float getPitchFactor(float pitch) {
      float absPitch = Math.abs(pitch);
      if (absPitch <= 5.0F) {
         return 1.0F;
      } else if (absPitch <= 15.0F) {
         return 0.95F;
      } else if (absPitch <= 25.0F) {
         return 0.85F;
      } else if (absPitch <= 35.0F) {
         return 0.75F;
      } else if (absPitch <= 45.0F) {
         return 0.65F;
      } else if (absPitch <= 55.0F) {
         return 0.55F;
      } else if (absPitch <= 65.0F) {
         return 0.45F;
      } else {
         return absPitch <= 75.0F ? 0.35F : 0.25F;
      }
   }

   private BoostUtils() {
      throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
   }
}
