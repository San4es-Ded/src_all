package aethereal.module.combat;

import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.event.TickEvent;
import aethereal.handler.BaseHandler;
import aethereal.handler.Handler_2;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.util.ProjectUtil;
import lombok.Generated;
import net.minecraft.class_1309;
import net.minecraft.class_243;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_7833;
import org.joml.Vector2f;

@Handler_2
public class AimHandler extends BaseHandler implements Interface {
   private final AnimationUtil b = new AnimationUtil();
   private class_1309 c;

   @Generated
   public AnimationUtil a() {
      return this.b;
   }

   @EventTarget
   public void a(DrawEvent event) {
      this.b.a(0.0F, 1.0F, 0.25F, EasingList.g, event.g());
      float alpha = this.b.c();
      if (event.b() && this.c != null && alpha > 0.0F) {
         class_243 real = this.a(this.c, event.g());
         Vector2f screen = ProjectUtil.a(real.field_1352, real.field_1351, real.field_1350);
         if (!ProjectUtil.a(screen)) {
            return;
         }

         float distance = (float)aM_.field_1724.method_33571().method_1022(real);
         float size = (float)Math.max(28.0, 40.0 - distance * 0.7000002488091963) * (1.2F - 0.2F * alpha);
         event.h().method_22903();
         event.h().method_46416(screen.x(), screen.y(), 0.0F);
         event.h().method_22907(class_7833.field_40718.rotationDegrees((float)Math.sin(System.currentTimeMillis() / 820.0) * 350.0F));
         event.d()
            .a(event.h(), class_2960.method_60655("westra", "pictures/marker.png"), -size / 2.0F, -size / 2.0F, size, size, 0.0F, ColorUtil.a(-1, alpha * 0.8F));
         event.h().method_22909();
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      ProjectileHelper projectile = Westra.h().d().t().D();
      class_1309 current = null;
      if (projectile.m() && projectile.r()) {
         current = projectile.q();
      }

      boolean visible = current != null;
      if (visible) {
         this.c = current;
      }

      this.b.a(visible);
      if (!visible && this.b.a() <= 0.0F) {
         this.c = null;
      }
   }

   private class_243 a(class_1309 entity, float delta) {
      return new class_243(
         class_3532.method_16436(delta, entity.field_6014, entity.method_23317()),
         class_3532.method_16436(delta, entity.field_6036, entity.method_23318()) + entity.method_17682() / 2.0,
         class_3532.method_16436(delta, entity.field_5969, entity.method_23321())
      );
   }
}
