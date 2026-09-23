package wtf.wyvern.client.modules.impl.movement;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "Flight", category = Category.MOVEMENT, description = "Позволяет летать")
public final class Flight extends Module {
   public static final Flight INSTANCE = new Flight();
   private final ModeSetting mode = new ModeSetting("Режим", "Скольжение", "Прыжки", "Обычный");
   private final SliderSetting speed = new SliderSetting("Скорость", 1.5F, 0.1F, 10.0F, 0.1F, () -> !mode.is("Прыжки"));
   private final SliderSetting verticalSpeed = new SliderSetting("Скорость Y", 1.5F, 0.1F, 10.0F, 0.1F, () -> !mode.is("Прыжки"));

   @FastNative
   @EventTarget
   public void onUpdate(EventUpdate event) {
      if (mc.player == null) return;
      if (mode.is("Прыжки")) {
         if (mc.options.jumpKey.isPressed()) mc.player.jump();
         return;
      }
      double y = mode.is("Скольжение") ? -0.005D : 0.0D;
      if (mc.options.sneakKey.isPressed()) y = -verticalSpeed.getCurrent();
      else if (mc.options.jumpKey.isPressed()) y = verticalSpeed.getCurrent();
      if (mc.player.isOnGround()) mc.player.jump();
      mc.player.setVelocity(mc.player.getVelocity().x, y, mc.player.getVelocity().z);
      if (MovingUtil.hasPlayerMovement()) MovingUtil.strafe(speed.getCurrent());
   }
}
