package aethereal.module.render;

import aethereal.core.Category;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.setting.BooleanSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.ui.recode.RecodeKit;
import net.minecraft.class_1306;
import net.minecraft.class_332;
import net.minecraft.class_4587;

@ModuleRegister(
   a = "Custom Hotbar",
   b = "Заменяет ванильный хотбар подложкой клиента со скользящим выделением слота",
   c = Category.Render
)
public class CustomHotbar extends Module implements Interface {
   private static final float b = 182.0F;
   private static final float c = 22.0F;
   private static final float d2 = 20.0F;
   private final SliderSetting e = new SliderSetting("Скругление", 11.0F, 0.0F, 11.0F, 0.5F);
   private final BooleanSetting f2 = new BooleanSetting("Свечение", true);
   private final BooleanSetting g2 = new BooleanSetting("Подложка второй руки", true);
   private final AnimationUtil h2 = new AnimationUtil();
   private float i2 = -1.0F;

   public CustomHotbar() {
      this.a(new Setting[]{this.e, this.f2, this.g2});
   }

   @Override
   public void c() {
      this.i2 = -1.0F;
      super.c();
   }

   public void a(class_332 context) {
      if (this.m() && context != null && aM_.field_1724 != null) {
         class_4587 matrices = context.method_51448();
         Draw2DProcessor draw = Westra.h().d().i();
         int accent = RecodeKit.accent();
         int shade = RecodeKit.accentShade();
         float centerX = context.method_51421() * 0.5F - 1.0F;
         float y = context.method_51443() - 22.0F;
         float x = centerX - 91.0F;
         float radius = Math.min(this.e.c(), 11.0F);
         RecodeKit.glass(matrices, x, y, 182.0F, 22.0F, radius, 1.0F, 0.82F, 0.12F, this.f2.c() ? 0.16F : 0.0F);
         RecodeKit.shimmer(matrices, x + radius, y + 0.25F, 182.0F - radius * 2.0F, 0.5F, RecodeKit.time() * 0.8F, 0.45F);

         for (int slot = 1; slot < 9; slot++) {
            float dotX = centerX - 90.0F + slot * 20.0F - 0.5F;
            draw.a(matrices, dotX, y + 11.0F - 0.5F, 1.0F, 1.0F, 0.5F, ColorUtil.a(-1, 0.12F));
         }

         float selected = aM_.field_1724.method_31548().field_7545;
         if (this.i2 < 0.0F) {
            this.i2 = selected;
         }

         float delta = aM_.method_61966().method_60636();
         this.i2 = this.i2 + (selected - this.i2) * Math.min(1.0F, 0.3F * delta);
         float slotX = centerX - 90.0F + this.i2 * 20.0F;
         float slotY = y + 1.0F;
         float slotRadius = Math.min(radius, 10.0F) - 0.5F;
         int top = ColorUtil.a(accent, 0.34F);
         int bottom = ColorUtil.a(shade, 0.12F);
         draw.a(matrices, slotX, slotY, 20.0F, 20.0F, slotRadius, top, top, bottom, bottom);
         draw.a(matrices, slotX, slotY, 20.0F, 20.0F, slotRadius, 0.5F, ColorUtil.a(accent, 0.7F));
         float lineWidth = 8.0F;
         draw.a(matrices, slotX + (20.0F - lineWidth) / 2.0F - 1.0F, slotY + 20.0F - 2.25F, lineWidth + 2.0F, 2.0F, 1.0F, ColorUtil.a(accent, 0.35F));
         draw.a(matrices, slotX + (20.0F - lineWidth) / 2.0F, slotY + 20.0F - 2.0F, lineWidth, 1.25F, 0.6F, ColorUtil.a(ColorUtil.a(accent, -1, 0.35F), 1.0F));
         if (this.g2.c() && !aM_.field_1724.method_6079().method_7960()) {
            boolean left = aM_.field_1724.method_6068().method_5928() == class_1306.field_6182;
            float offX = left ? centerX - 120.0F : centerX + 97.0F;
            float offY = y - 1.0F;
            float offRadius = Math.min(radius + 1.0F, 12.0F);
            RecodeKit.glass(matrices, offX, offY, 24.0F, 24.0F, offRadius, 1.0F, 0.82F, 0.12F);
         }
      }
   }
}
