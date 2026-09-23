package aethereal.module.render;

import aethereal.config.ThemeInfo;
import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ColorSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import com.mojang.blaze3d.platform.GlStateManager.class_4534;
import com.mojang.blaze3d.platform.GlStateManager.class_4535;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import net.minecraft.class_10142;
import net.minecraft.class_243;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_3532;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;

@ModuleRegister(
   a = "Trails",
   b = "Оставляет светящийся след за игроком",
   c = Category.Render
)
public class Trails extends Module {
   private final ModeSetting b = new ModeSetting("Тип следа", "Сплошной", "Сплошной", "Пунктир", "Затухающий");
   private final SliderSetting c = new SliderSetting("Длина следа", 3.0F, 0.5F, 10.0F, 0.5F);
   private final SliderSetting d = new SliderSetting("Ширина следа", 0.1F, 0.05F, 0.5F, 0.01F);
   private final BooleanSetting e = new BooleanSetting("Показывать от первого лица", false);
   private final BooleanSetting f = new BooleanSetting("Цвет из темы", true);
   private final ColorSetting g = new ColorSetting("Свой цвет", ColorUtil.a(255, 111, 181, 255)).a(() -> !this.f.c());
   private final List<Trails.a> h = new ArrayList<>();
   private class_243 i;

   public Trails() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e, this.f, this.g});
   }

   @Override
   public void c() {
      super.c();
      this.h.clear();
      this.i = null;
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && aM_.field_1687 != null && aM_.field_1724 != null) {
         long now = System.currentTimeMillis();
         float maxAge = this.c.c() * 1000.0F;
         float delta = event.g();
         class_243 position = new class_243(
            class_3532.method_16436(delta, aM_.field_1724.field_6038, aM_.field_1724.method_23317()),
            class_3532.method_16436(delta, aM_.field_1724.field_5971, aM_.field_1724.method_23318()),
            class_3532.method_16436(delta, aM_.field_1724.field_5989, aM_.field_1724.method_23321())
         );
         if (this.i == null || this.i.method_1022(position) > 0.03) {
            this.h.add(new Trails.a(position.method_1031(0.0, 0.05, 0.0), now));
            this.i = position;
         }

         Iterator<Trails.a> it = this.h.iterator();

         while (it.hasNext()) {
            if ((float)(now - it.next().b()) > maxAge) {
               it.remove();
            }
         }

         if (this.h.size() >= 2) {
            if (!aM_.field_1690.method_31044().method_31034() || this.e.c()) {
               this.a(event.h().method_23760().method_23761(), now, maxAge);
            }
         }
      }
   }

   private void a(Matrix4f matrix, long now, float maxAge) {
      class_243 camera = aM_.method_1561().field_4686.method_19326();
      float height = this.d.c() * 3.0F;
      boolean faded = this.b.l("Затухающий");
      boolean dotted = this.b.l("Пунктир");
      int color = this.f.c() ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : this.g.c();
      List<Trails.a> points = new ArrayList<>(this.h);
      RenderSystem.enableBlend();
      RenderSystem.disableCull();
      RenderSystem.disableDepthTest();
      RenderSystem.blendFunc(class_4535.SRC_ALPHA, class_4534.ONE_MINUS_SRC_ALPHA);
      RenderSystem.setShader(class_10142.field_53876);
      class_287 buffer = class_289.method_1348().method_60827(class_5596.field_27382, class_290.field_1576);

      for (int index = 0; index + 1 < points.size(); index++) {
         if (!dotted || index % 2 != 1) {
            Trails.a first = points.get(index);
            Trails.a second = points.get(index + 1);
            float age1 = class_3532.method_15363((float)(now - first.b()) / maxAge, 0.0F, 1.0F);
            float age2 = class_3532.method_15363((float)(now - second.b()) / maxAge, 0.0F, 1.0F);
            int color1 = ColorUtil.a(color, (1.0F - age1) * (faded ? 1.0F - age1 : 1.0F) * 0.7F);
            int color2 = ColorUtil.a(color, (1.0F - age2) * (faded ? 1.0F - age2 : 1.0F) * 0.7F);
            float x1 = (float)(first.a().field_1352 - camera.field_1352);
            float y1 = (float)(first.a().field_1351 - camera.field_1351);
            float z1 = (float)(first.a().field_1350 - camera.field_1350);
            float x2 = (float)(second.a().field_1352 - camera.field_1352);
            float y2 = (float)(second.a().field_1351 - camera.field_1351);
            float z2 = (float)(second.a().field_1350 - camera.field_1350);
            buffer.method_22918(matrix, x1, y1, z1).method_39415(color1);
            buffer.method_22918(matrix, x2, y2, z2).method_39415(color2);
            buffer.method_22918(matrix, x2, y2 + height, z2).method_39415(color2);
            buffer.method_22918(matrix, x1, y1 + height, z1).method_39415(color1);
            buffer.method_22918(matrix, x1, y1 + height, z1).method_39415(color1);
            buffer.method_22918(matrix, x2, y2 + height, z2).method_39415(color2);
            buffer.method_22918(matrix, x2, y2, z2).method_39415(color2);
            buffer.method_22918(matrix, x1, y1, z1).method_39415(color1);
         }
      }

      class_286.method_43433(buffer.method_60800());
      RenderSystem.enableDepthTest();
      RenderSystem.enableCull();
      RenderSystem.disableBlend();
   }

   private static final class a {
      private final class_243 a;
      private final long b;

      a(class_243 position, long time) {
         this.a = position;
         this.b = time;
      }

      class_243 a() {
         return this.a;
      }

      long b() {
         return this.b;
      }
   }
}
