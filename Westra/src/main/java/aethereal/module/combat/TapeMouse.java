package aethereal.module.combat;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.CounterUtil;
import lombok.Generated;
import platform.inject.invokers.MinecraftClientInvoker;

@ModuleRegister(
   a = "Tape Mouse",
   b = "Автоматически кликает выбранной кнопкой мыши через заданные промежутки времени",
   c = Category.Combat
)
public class TapeMouse extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Задержка между кликами", 1000.0F, 10.0F, 5000.0F, 10.0F);
   private final BooleanSetting c = new BooleanSetting("Не кликать во время еды", true);
   private final ModeSetting d = new ModeSetting("Кнопка мыши", "Правая", "Правая", "Левая");
   private final CounterUtil e = new CounterUtil();

   @Generated
   public SliderSetting q() {
      return this.b;
   }

   @Generated
   public BooleanSetting r() {
      return this.c;
   }

   @Generated
   public ModeSetting s() {
      return this.d;
   }

   @Generated
   public CounterUtil t() {
      return this.e;
   }

   public TapeMouse() {
      this.a(new Setting[]{this.d, this.c, this.b});
   }

   @EventTarget
   public void a(TickEvent event) {
      if ((!this.c.c() || !aM_.field_1724.method_6115()) && this.e.a(this.b.c().intValue())) {
         String var2 = this.d.c();
         switch (var2) {
            case "Правая":
               ((MinecraftClientInvoker)aM_).invokeDoItemUse();
               break;
            case "Левая":
               ((MinecraftClientInvoker)aM_).invokeDoAttack();
         }

         this.e.b();
      }
   }
}
