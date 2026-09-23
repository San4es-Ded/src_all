package aethereal.render;

import aethereal.api.Compile;
import aethereal.autobuy.BatchProcessor;
import aethereal.config.BaseProcessor;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.NativeMethodLookup;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.class_1058;
import net.minecraft.class_1799;
import net.minecraft.class_1921;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Matrix4f;

public class Draw3DProcessor extends BaseProcessor implements Interface {
   @Compile
   @Override
   public void setup() {
   }

   @Override
   public void unSetup() {
   }

   @EventTarget(
      a = 4
   )
   public void a(DrawEvent event) {
      if (event.c()) {
         Westra.h().d().l().b();
      } else if (event.b()) {
         Westra.h().d().l().a();
      }
   }

   public void a(class_4587 matrices, class_238 box, int color, float width) {
      Westra.h().d().l().a(BatchProcessor.b.a(matrices, box, color, width));
   }

   public void a(
      Matrix4f matrix, float minX, float minY, float maxX, float maxY, int color, boolean corners, boolean healthBar, float healthPercent, int healthColor
   ) {
      Westra.h().d().l().a(new BatchProcessor.a(matrix, minX, minY, maxX, maxY, color, corners, healthBar, healthPercent, healthColor));
   }

   public void a(class_4587 matrices, class_243 start, class_243 end, class_243 control, int color, float width) {
      Westra.h().d().l().a(BatchProcessor.b.a(matrices, start, end, control, color, width));
   }

   public void a(class_332 context, class_1799 stack, float x, float y, int z, float alpha, float scale, boolean overlay) {
      if (!stack.method_7960()) {
         context.method_51448().method_22903();
         context.method_51448().method_46416(x, y, z);
         context.method_51448().method_22905(scale, scale, 1.0F);
         RenderSystem.setShaderColor(alpha, alpha, alpha, alpha);
         context.method_51427(stack, 0, 0);
         if (overlay) {
            context.method_51431(aM_.field_1772, stack, 0, 0);
         }

         RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
         context.method_51448().method_22909();
      }
   }

   public void a(class_332 context, class_1058 sprite, float x, float y, float z, float scale, float alpha) {
      context.method_51448().method_22903();
      context.method_51448().method_46416(x, y, z);
      context.method_51448().method_22905(scale, scale, 1.0F);
      context.method_52710(class_1921::method_62277, sprite, 0, 0, 18, 18, ColorUtil.a(-1, alpha));
      context.method_51448().method_22909();
   }

   static {
      NativeMethodLookup.lookup(Draw3DProcessor.class, 29);
   }
}
