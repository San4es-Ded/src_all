package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.MultiModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.MathUtil;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_10142;
import net.minecraft.class_1297;
import net.minecraft.class_1657;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_2960;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_7833;
import net.minecraft.class_9801;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

@ModuleRegister(
   a = "Pointers",
   b = "Указывает лучами направление к игрокам",
   c = Category.Render
)
public class Pointers extends Module {
   private final MultiModeSetting b = new MultiModeSetting(
      "Визуальные настройки",
      new BooleanSetting("Фильтр по друзьям", false),
      new BooleanSetting("Трассировка до игрока", true),
      new BooleanSetting("Навигационная стрелка", true)
   );
   private final SliderSetting c = new SliderSetting("Размер стрелки", 7.0F, 5.0F, 15.0F, 1.0F);
   private final SliderSetting d = new SliderSetting("Отступ от центра", 30.0F, 20.0F, 50.0F, 1.0F);
   private float e;

   public Pointers() {
      this.a(new Setting[]{this.b, this.c, this.d});
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && this.b.a("Трассировка до игрока").c()) {
         class_243 cam = aM_.method_1561().field_4686.method_19326();
         class_243 start = new class_243(0.0, 0.0, 27.0)
            .method_1037((float)(-Math.toRadians(aM_.method_1561().field_4686.method_19329())))
            .method_1024((float)(-Math.toRadians(aM_.method_1561().field_4686.method_19330())))
            .method_1019(cam);
         Matrix4f matrix = event.h().method_23760().method_23761();
         class_287 buffer = this.q();
         boolean any = false;

         for (class_1297 _e : aM_.field_1687.method_18112()) {
            if (_e instanceof class_1657 class_746Var && class_746Var != aM_.field_1724 && class_746Var.method_5805()) {
               class_243 pos = MathUtil.a(class_746Var, event.g()).method_1031(0.0, class_746Var.method_17682() / 2.0F, 0.0);
               boolean isFriend = Westra.h().d().e().d(class_746Var.method_5477().getString());
               if (!this.b.a("Фильтр по друзьям").c() || isFriend) {
                  buffer.method_22918(
                        matrix,
                        (float)(start.method_10216() - cam.field_1352),
                        (float)(start.method_10214() - cam.field_1351),
                        (float)(start.method_10215() - cam.field_1350)
                     )
                     .method_22915(isFriend ? 0.0F : 1.0F, 1.0F, isFriend ? 0.0F : 1.0F, 1.0F);
                  buffer.method_22918(
                        matrix,
                        (float)(pos.method_10216() - cam.field_1352),
                        (float)(pos.method_10214() - cam.field_1351),
                        (float)(pos.method_10215() - cam.field_1350)
                     )
                     .method_22915(isFriend ? 0.0F : 1.0F, 1.0F, isFriend ? 0.0F : 1.0F, 1.0F);
                  any = true;
               }
            }
         }

         this.a(buffer, any);
      }

      if (event.b() && this.b.a("Навигационная стрелка").c()) {
         float cameraYaw = aM_.field_1773.method_19418().method_19330();
         this.e = MathUtil.c(this.e, this.e + class_3532.method_15393(cameraYaw - this.e), 12.0F);

         for (class_1297 _ex : aM_.field_1687.method_18112()) {
            if (_ex instanceof class_1657 class_746Var2 && class_746Var2 != aM_.field_1724 && class_746Var2.method_5805()) {
               boolean isFriend2 = Westra.h().d().e().d(class_746Var2.method_5477().getString());
               if (!this.b.a("Фильтр по друзьям").c() || isFriend2) {
                  class_243 pos2 = MathUtil.a(class_746Var2, event.g());
                  class_243 eye = MathUtil.a(aM_.field_1724, event.g());
                  float angle = class_3532.method_15393(
                     (float)Math.toDegrees(Math.atan2(eye.field_1352 - pos2.method_10216(), pos2.method_10215() - eye.field_1350)) - this.e
                  );
                  float radians = (float)Math.toRadians(angle);
                  class_4587 stack = event.h();
                  stack.method_22903();
                  stack.method_46416(
                     aM_.method_22683().method_4486() / 2.0F + (float)Math.sin(radians) * this.d.c(),
                     aM_.method_22683().method_4502() / 2.0F - (float)Math.cos(radians) * this.d.c(),
                     0.0F
                  );
                  stack.method_22907(class_7833.field_40718.rotationDegrees(angle));
                  event.d()
                     .a(
                        stack,
                        class_2960.method_60655("westra", "pictures/pointer.png"),
                        -this.c.c() / 2.0F,
                        -this.c.c() / 2.0F,
                        this.c.c(),
                        this.c.c(),
                        0.0F,
                        isFriend2 ? ColorUtil.a(85, 255, 85, 178) : ColorUtil.a(255, 255, 255, 178)
                     );
                  stack.method_22909();
               }
            }
         }
      }
   }

   private class_287 q() {
      RenderSystem.enableBlend();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA);
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.setShader(class_10142.field_53876);
      return class_289.method_1348().method_60827(class_5596.field_29344, class_290.field_1576);
   }

   private void a(class_287 buffer, boolean draw) {
      class_9801 built = buffer.method_60794();
      if (built != null) {
         if (draw) {
            class_286.method_43433(built);
         } else {
            built.close();
         }
      }

      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }
}
