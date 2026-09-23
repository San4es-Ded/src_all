package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.HeadFeatureEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.ColorSetting;
import aethereal.setting.Setting;
import aethereal.ui.shader.GradientUtil;
import net.minecraft.class_1657;
import net.minecraft.class_1921;
import net.minecraft.class_3532;
import net.minecraft.class_4587;
import net.minecraft.class_4588;
import net.minecraft.class_572;
import net.minecraft.class_7833;
import net.minecraft.class_4597.class_4598;
import org.joml.Matrix4f;

@ModuleRegister(
   a = "China Hat",
   b = "Надевает китайскую шляпу на голову вашего персонажа",
   c = Category.Render
)
public class ChinaHat extends Module {
   private final ColorSetting b = new ColorSetting("Цвет визуализации шляпы", ColorUtil.a(255, 111, 181, 255));

   public ChinaHat() {
      this.a(new Setting[]{this.b});
   }

   @EventTarget
   public void a(HeadFeatureEvent event) {
      class_572<?> class_572VarE = (class_572<?>)event.e();
      if (class_572VarE instanceof class_572) {
         boolean friend = Westra.h().d().e().d(event.d().method_5477().getString());
         if (event.d() == aM_.field_1724 || friend) {
            class_1657 player = event.d();
            class_4587 matrices = event.b();
            double radius = player.method_5829().field_1320 - player.method_5829().field_1323;
            float verticalOffset = !player.method_31548().method_5438(39).method_7960() ? 0.48F : 0.45F;
            matrices.method_22903();
            class_572VarE.field_3398.method_22703(matrices);
            matrices.method_46416(0.0F, -verticalOffset, 0.0F);
            matrices.method_22907(class_7833.field_40717.rotationDegrees(180.0F));
            matrices.method_22907(class_7833.field_40716.rotationDegrees(90.0F));
            Matrix4f matrix = matrices.method_23760().method_23761();
            class_4588 buffer = event.c().getBuffer(class_1921.method_49042());
            long time = System.currentTimeMillis();
            int postColor = event.d() == aM_.field_1724 ? this.b.c() : (friend ? ColorUtil.a(0, 155, 0, 255) : 0);

            for (int i = 0; i < 360; i++) {
               float angle1 = i * 0.06981317F;
               float angle2 = (i + 1) * 0.06981317F;
               int gradientAngle = i * 8;
               int col = GradientUtil.a(3, gradientAngle, postColor, ColorUtil.b(postColor, 0.5F), time);
               float x1 = class_3532.method_15374(angle1) * (float)radius;
               float z1 = class_3532.method_15362(angle1) * (float)radius;
               float x2 = class_3532.method_15374(angle2) * (float)radius;
               float z2 = class_3532.method_15362(angle2) * (float)radius;
               this.a(buffer, matrix, x1, 0.0F, z1, col);
               this.a(buffer, matrix, x2, 0.0F, z2, col);
               this.a(buffer, matrix, 0.0F, 0.3F, 0.0F, postColor);
               this.a(buffer, matrix, 0.0F, 0.3F, 0.0F, postColor);
            }

            float radiusOuter = (float)radius + 0.01F;
            float radiusInner = (float)radius - 0.01F;
            float prevOuterX = class_3532.method_15374(0.0F) * radiusOuter;
            float prevOuterZ = class_3532.method_15362(0.0F) * radiusOuter;
            float prevInnerX = class_3532.method_15374(0.0F) * radiusInner;
            float prevInnerZ = class_3532.method_15362(0.0F) * radiusInner;

            for (int i2 = 1; i2 <= 180; i2++) {
               float angle = i2 * 0.06981317F;
               int gradientAngle2 = (i2 - 1) * 8;
               int col2 = GradientUtil.a(3, gradientAngle2, postColor, ColorUtil.b(postColor, 0.5F), time);
               float outerX = class_3532.method_15374(angle) * radiusOuter;
               float outerZ = class_3532.method_15362(angle) * radiusOuter;
               float innerX = class_3532.method_15374(angle) * radiusInner;
               float innerZ = class_3532.method_15362(angle) * radiusInner;
               this.a(buffer, matrix, prevOuterX, -0.01F, prevOuterZ, col2);
               this.a(buffer, matrix, prevOuterX, 0.0F, prevOuterZ, col2);
               this.a(buffer, matrix, outerX, 0.0F, outerZ, col2);
               this.a(buffer, matrix, outerX, -0.01F, outerZ, col2);
               this.a(buffer, matrix, prevInnerX, 0.0F, prevInnerZ, col2);
               this.a(buffer, matrix, innerX, 0.0F, innerZ, col2);
               this.a(buffer, matrix, outerX, 0.0F, outerZ, col2);
               this.a(buffer, matrix, prevOuterX, 0.0F, prevOuterZ, col2);
               prevOuterX = outerX;
               prevOuterZ = outerZ;
               prevInnerX = innerX;
               prevInnerZ = innerZ;
            }

            matrices.method_22909();
            if (event.c() instanceof class_4598 immediate) {
               immediate.method_22993();
            }
         }
      }
   }

   private void a(class_4588 buffer, Matrix4f matrix, float x, float y, float z, int rgba) {
      int[] unpack = ColorUtil.b(rgba);
      buffer.method_22918(matrix, x, y, z).method_1336(unpack[0], unpack[1], unpack[2], unpack[3]);
   }
}
