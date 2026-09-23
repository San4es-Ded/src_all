package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.AttackEvent;
import aethereal.event.DrawEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import net.minecraft.class_243;
import net.minecraft.class_2596;
import net.minecraft.class_2743;
import net.minecraft.class_2797;
import net.minecraft.class_2813;
import net.minecraft.class_2824;
import net.minecraft.class_2868;
import net.minecraft.class_2879;
import net.minecraft.class_2885;
import net.minecraft.class_2886;
import platform.inject.accessors.ClientConnectionAccessor;

@ModuleRegister(
   a = "Fake Lags",
   b = "Задерживает отправку пакетов, имитируя лаги на сервере",
   c = Category.Player
)
public class FakeLags extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Задержка симуляции", 20.0F, 1.0F, 40.0F, 1.0F);
   private final BooleanSetting c = new BooleanSetting("Отображать серв-позицию", false);
   private final Queue<class_2596<?>> d = new ConcurrentLinkedQueue<>();
   private int e;
   private int f;
   private class_243 g;

   public FakeLags() {
      this.a(new Setting[]{this.b, this.c});
   }

   @Override
   public void b() {
      super.b();
      this.d.clear();
      this.e = 0;
      this.f = 0;
      this.g = null;
   }

   @Override
   public void c() {
      super.c();
      this.q();
      this.g = null;
   }

   @EventTarget
   public void a(AttackEvent event) {
      this.f = 2;
      this.q();
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (aM_.field_1724 != null) {
         if (event.c()) {
            if (event.d() instanceof class_2743 class_2743VarD) {
               if (class_2743VarD.method_11818() == aM_.field_1724.method_5628()) {
                  this.q();
               }
            }
         } else {
            if (event.b()) {
               if (this.f <= 0 && !this.a(event.d())) {
                  this.d.offer(event.d());
                  event.a(true);
               } else {
                  this.q();
               }
            }
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (this.f > 0) {
         this.f--;
      }

      int i = this.e + 1;
      this.e = i;
      if (i >= this.b.c().intValue() && !this.d.isEmpty()) {
         this.q();
         this.e = 0;
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && this.c.c() && this.g != null) {
         event.e()
            .a(event.h(), aM_.field_1724.method_5829().method_997(this.g.method_1020(aM_.field_1724.method_19538())), ColorUtil.a(255, 255, 255, 200), 0.75F);
      }
   }

   private boolean a(class_2596<?> packet) {
      return packet instanceof class_2824
         || packet instanceof class_2797
         || packet instanceof class_2868
         || packet instanceof class_2879
         || packet instanceof class_2885
         || packet instanceof class_2886
         || packet instanceof class_2813;
   }

   private void q() {
      ClientConnectionAccessor connection = (ClientConnectionAccessor)aM_.field_1724.field_3944.method_48296();
      this.d.forEach(packet -> connection.sendWithoutEvent((class_2596<?>)packet, null, true));
      this.d.clear();
      this.g = aM_.field_1724.method_19538();
   }
}
