package wtf.wyvern.client.modules.impl.movement;

import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.impl.combat.Aura;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;

import java.util.List;
import java.util.function.Supplier;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "ElytraBooster",
        category = Category.MOVEMENT,
        description = "Ускоряет ваш Фейерверк на элитрах"
)
public class   ElytraBooster extends Module {
   public static final ElytraBooster INSTANCE = new ElytraBooster();

   @FastNative
   public ModeSetting getMode() {
      return mode;
   }
   public static final ModeSetting mode = new ModeSetting("Сервер", "Custom", "LonyGrief", "BravoHVHFFA", "ReallyWorld");
   private final SliderSetting yaw0_5 = new SliderSetting("yaw 0 - 5", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw5_10 = new SliderSetting("yaw 5 - 10", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw10_15 = new SliderSetting("yaw 10 - 15", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw15_20 = new SliderSetting("yaw 15 - 20", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw20_25 = new SliderSetting("yaw 20 - 25", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw25_30 = new SliderSetting("yaw 25 - 30", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw30_35 = new SliderSetting("yaw 30 - 35", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw35_40 = new SliderSetting("yaw 35 - 40", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting yaw40_45 = new SliderSetting("yaw 40 - 45", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });

   private final SliderSetting pitch0_5 = new SliderSetting("pitch 0 - 5", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch5_10 = new SliderSetting("pitch 5 - 10", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch10_15 = new SliderSetting("pitch 10 - 15", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch15_20 = new SliderSetting("pitch 15 - 20", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch20_25 = new SliderSetting("pitch 20 - 25", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch25_30 = new SliderSetting("pitch 25 - 30", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch30_35 = new SliderSetting("pitch 30 - 35", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch35_40 = new SliderSetting("pitch 35 - 40", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });
   private final SliderSetting pitch40_45 = new SliderSetting("pitch 40 - 45", 1.5F, 1.5F, 2.5F, 0.01F, new Supplier<Boolean>() {
      @Override
      public Boolean get() {
         return mode.is("Custom");
      }
   });

   private final List<SliderSetting> yawSettings = List.of(
           yaw0_5, yaw5_10, yaw10_15, yaw15_20, yaw20_25, yaw25_30, yaw30_35, yaw35_40, yaw40_45
   );

   private final List<SliderSetting> pitchSettings = List.of(
           pitch0_5, pitch5_10, pitch10_15, pitch15_20, pitch20_25, pitch25_30, pitch30_35, pitch35_40, pitch40_45
   );

   private static final float[] BRAVO_YAW_SPEEDS = {
           1.67F, 1.67F, 1.72F, 1.74F, 1.86F, 1.89F, 2.04F, 2.04F, 2.05F
   };

   private static final float[] BRAVO_PITCH_SPEEDS = {
           1.65F, 1.70F, 1.72F, 1.74F, 1.82F, 1.90F, 2.01F, 2.04F, 2.04F
   };

   @FastNative
   public Vec2f getBoostV2() {
      float[] yawSpeeds = new float[this.yawSettings.size()];
      float[] pitchSpeeds = new float[this.pitchSettings.size()];

      for (int i = 0; i < this.yawSettings.size(); i++) {
         yawSpeeds[i] = this.yawSettings.get(i).getCurrent();
      }

      for (int i = 0; i < this.pitchSettings.size(); i++) {
         pitchSpeeds[i] = this.pitchSettings.get(i).getCurrent();
      }

      return this.resolveBoostSpeeds(yawSpeeds, pitchSpeeds);
   }

   @FastNative
   public Vec2f getBravoBoostV2() {
      return this.resolveBoostSpeeds(BRAVO_YAW_SPEEDS, BRAVO_PITCH_SPEEDS);
   }

@FastNative
    public float getEffectiveYaw() {
       float yaw = mc.player != null ? mc.player.getYaw() : 0.0F;
       Aura aura = Aura.INSTANCE;
       if (aura != null && aura.isEnabled() && aura.getTarget() != null
               && !aura.rotationMode.is("None")) {
          yaw = aura.lastYaw;
       }
       return yaw;
    }

    @FastNative
    public float getEffectivePitch() {
       float pitch = mc.player != null ? mc.player.getPitch() : 0.0F;
       Aura aura = Aura.INSTANCE;
       if (aura != null && aura.isEnabled() && aura.getTarget() != null
               && !aura.rotationMode.is("None")) {
          pitch = aura.lastPitch;
       }
       return pitch;
    }

   @FastNative
   private Vec2f resolveBoostSpeeds(float[] yawSpeeds, float[] pitchSpeeds) {
      float yaw = this.getEffectiveYaw();
      float pitch = this.getEffectivePitch();

      float normalizedYaw = this.convertValToRange(MathHelper.wrapDegrees(yaw));
      float normalizedPitch = Math.min(Math.abs(MathHelper.clamp(pitch, -90.0F, 90.0F)), 45.0F);
      int yawIndex = this.getRangeIndex(normalizedYaw, yawSpeeds.length);
      int pitchIndex = this.getRangeIndex(normalizedPitch, pitchSpeeds.length);
      float yawSpeed = yawSpeeds[yawIndex];
      float pitchSpeed = pitchSpeeds[pitchIndex];
      if (pitchSpeed > yawSpeed) {
         yawSpeed = pitchSpeed;
      }

      return new Vec2f(yawSpeed, pitchSpeed);
   }

   @FastNative
   private int getRangeIndex(float value, int length) {
      return Math.min((int)(value / 5.0F), length - 1);
   }

   @FastNative
   private float convertValToRange(float value) {
      float result = Math.abs(value);
      if (result > 90.0F) {
         result = 180.0F - result;
      }

      if (result > 45.0F) {
         result = 90.0F - result;
      }

      return result;
   }
}
