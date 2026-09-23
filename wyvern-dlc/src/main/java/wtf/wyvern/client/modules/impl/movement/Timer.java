package wtf.wyvern.client.modules.impl.movement;

import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.utility.game.client.TimerService;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "Timer", category = Category.MOVEMENT, description = "Изменяет скорость игровых тиков")
@FastNative
public final class Timer extends Module {
   public static final Timer INSTANCE = new Timer();
   private final SliderSetting speed = new SliderSetting("Скорость", 1.0F, 0.1F, 5.0F, 0.05F);

   @EventTarget
   public void onTick(EventTick event) {
      TimerService.setSpeed(speed.getCurrent());
   }

   @Override
   public void onEnable() {
      TimerService.setSpeed(speed.getCurrent());
      super.onEnable();
   }

   @Override
   public void onDisable() {
      TimerService.resetSpeed();
      super.onDisable();
   }
}
