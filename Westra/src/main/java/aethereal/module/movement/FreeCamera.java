package aethereal.module.movement;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.CameraPositionEvent;
import aethereal.event.CrosshairTargetEvent;
import aethereal.event.InputEvent;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.Look;
import net.minecraft.class_243;
import net.minecraft.class_2848;
import net.minecraft.class_2851;
import net.minecraft.class_3675;
import net.minecraft.class_3959;
import net.minecraft.class_5498;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

@ModuleRegister(
   a = "Free Camera",
   b = "Позволяет свободно перемещать камеру, пока игрок остаётся на месте",
   c = Category.Movement
)
public class FreeCamera extends Module implements Interface {
   private final SliderSetting b = new SliderSetting("Скорость движения XZ", 1.0F, 0.1F, 5.0F, 0.1F);
   private final SliderSetting c = new SliderSetting("Скорость движения Y", 1.0F, 0.1F, 5.0F, 0.1F);
   private final BooleanSetting d = new BooleanSetting("Замораживать пакеты в полете", true);
   private class_243 e;
   private class_243 f;
   private class_243 g;
   private float h;
   private float i;
   private boolean j;
   private boolean k;

   public FreeCamera() {
      this.a(new Setting[]{this.d, this.b, this.c});
   }

   @Override
   public void b() {
      super.b();
      if (aM_.field_1724 == null) {
         this.d(true);
         this.a();
      } else {
         this.f = aM_.field_1724.method_33571();
         this.e = aM_.field_1724.method_33571();
         if (this.d.c()) {
            this.g = aM_.field_1724.method_19538();
         }

         this.d(false);
      }
   }

   @Override
   public void c() {
      super.c();
      this.d(true);
   }

   @EventTarget
   public void a(CameraPositionEvent event) {
      if (this.e != null && aM_.field_1724.method_5805()) {
         if (aM_.field_1690.method_31044() != class_5498.field_26664) {
            aM_.field_1690.method_31043(class_5498.field_26664);
         }

         class_243 basePrev = this.f != null ? this.f : this.e;
         class_243 interpolated = new class_243(
            basePrev.field_1352 + (this.e.field_1352 - basePrev.field_1352) * aM_.method_61966().method_60637(false),
            basePrev.field_1351 + (this.e.field_1351 - basePrev.field_1351) * aM_.method_61966().method_60637(false),
            basePrev.field_1350 + (this.e.field_1350 - basePrev.field_1350) * aM_.method_61966().method_60637(false)
         );
         event.a(interpolated);
         event.a(true);
      }
   }

   @EventTarget
   public void a(CrosshairTargetEvent event) {
      if (this.e != null && aM_.field_1724.method_5805()) {
         event.a(
            aM_.field_1687
               .method_17742(
                  new class_3959(
                     this.e,
                     this.e.method_1019(aM_.field_1724.method_5828(event.b()).method_1021(aM_.field_1724.method_55754())),
                     class_3960.field_17559,
                     class_242.field_1348,
                     aM_.field_1724
                  )
               )
         );
         event.a(true);
      }
   }

   @EventTarget
   public void a(TickEvent eventTick) {
      if (this.e != null && aM_.field_1724.method_5805()) {
         if (aM_.field_1755 == null) {
            float f;
            if (aM_.field_1690.field_1894.method_1434()) {
               f = 1.0F;
            } else {
               f = aM_.field_1690.field_1881.method_1434() ? -1.0F : 0.0F;
            }

            this.h = f;
            float f2;
            if (class_3675.method_15987(aM_.method_22683().method_4490(), 65)) {
               f2 = 1.0F;
            } else {
               f2 = class_3675.method_15987(aM_.method_22683().method_4490(), 68) ? -1.0F : 0.0F;
            }

            this.i = f2;
            this.j = aM_.field_1690.field_1903.method_1434();
            this.k = aM_.field_1690.field_1832.method_1434();
         } else {
            this.d(false);
         }

         if (this.d.c() && !aM_.field_1724.method_24828()) {
            aM_.field_1724.method_18800(0.0, 0.0, 0.0);
            if (this.g != null) {
               aM_.field_1724.method_5814(this.g.field_1352, this.g.field_1351, this.g.field_1350);
            }
         }

         this.f = this.e;
         this.e = this.e
            .method_1031(
               (this.h * -Math.sin(Math.toRadians(Look.b())) + this.i * Math.cos(Math.toRadians(Look.b()))) * this.b.c().floatValue(),
               (this.j ? this.c.c().floatValue() : 0.0) - (this.k ? this.c.c().floatValue() : 0.0),
               (this.h * Math.cos(Math.toRadians(Look.b())) + this.i * Math.sin(Math.toRadians(Look.b()))) * this.b.c().floatValue()
            );
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (this.e != null && aM_.field_1724.method_5805()) {
         if (aM_.field_1755 != null) {
            event.a(0.0F);
            event.b(0.0F);
            event.b(false);
            event.c(false);
            return;
         }

         float f;
         if (class_3675.method_15987(aM_.method_22683().method_4490(), 265)) {
            f = 1.0F;
         } else {
            f = class_3675.method_15987(aM_.method_22683().method_4490(), 264) ? -1.0F : 0.0F;
         }

         event.a(f);
         float f2;
         if (class_3675.method_15987(aM_.method_22683().method_4490(), 262)) {
            f2 = -1.0F;
         } else {
            f2 = class_3675.method_15987(aM_.method_22683().method_4490(), 263) ? 1.0F : 0.0F;
         }

         event.b(f2);
         event.b(false);
         event.c(false);
      }
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.b()
         && this.d.c()
         && this.e != null
         && !aM_.field_1724.method_24828()
         && aM_.field_1724.method_5805()
         && (event.d() instanceof class_2851 || event.d() instanceof class_2848)) {
         event.a(true);
      }
   }

   private void d(boolean clearPositions) {
      if (clearPositions) {
         this.e = null;
         this.f = null;
         this.g = null;
      } else {
         this.i = 0.0F;
         this.h = 0.0F;
         this.k = false;
         this.j = false;
      }
   }
}
