package aethereal.ui.screen;

import aethereal.config.ThemeInfo;
import aethereal.core.Action;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.util.InventoryUtil;
import com.mojang.blaze3d.systems.RenderSystem;
import lombok.Generated;
import net.minecraft.class_10142;
import net.minecraft.class_1799;
import net.minecraft.class_286;
import net.minecraft.class_287;
import net.minecraft.class_289;
import net.minecraft.class_290;
import net.minecraft.class_332;
import net.minecraft.class_5611;
import net.minecraft.class_293.class_5596;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class RadialScreen implements Interface {
   private RadialScreen.a[] b;
   private final float c;
   private final float d;
   private int e = -1;
   private final int f;

   @Generated
   public int b() {
      return this.e;
   }

   @Generated
   public void e(int selectedSlot) {
      this.e = selectedSlot;
   }

   public RadialScreen(int count, float inner, float outer) {
      this.b = new RadialScreen.a[count];
      this.f = count;
      this.c = inner;
      this.d = outer;
   }

   public int a() {
      return this.b.length;
   }

   public void a(int slot) {
      RadialScreen.a[] next = new RadialScreen.a[this.b.length - 1];
      System.arraycopy(this.b, 0, next, 0, slot);
      System.arraycopy(this.b, slot + 1, next, slot, this.b.length - slot - 1);
      this.b = next;
      if (this.e >= this.b.length) {
         this.e = -1;
      }
   }

   public void b(int count) {
      RadialScreen.a[] next = new RadialScreen.a[count];
      System.arraycopy(this.b, 0, next, 0, Math.min(this.b.length, count));
      this.b = next;
   }

   public void a(int index, class_1799 icon, Action action, boolean editable) {
      this.b[index] = new RadialScreen.a(icon, null, action, editable);
   }

   public void a(int index, class_1799 icon, String label, Action action, boolean editable) {
      this.b[index] = new RadialScreen.a(icon, label, action, editable);
   }

   public boolean a(double mouseX, double mouseY, int button, class_5611 center) {
      int slot = this.a(mouseX, mouseY, center);
      if (slot < 0) {
         return false;
      } else {
         RadialScreen.a segment = this.b[slot];
         if (segment != null) {
            if (button == 1 && segment.d) {
               if (!segment.b.method_7960()) {
                  segment.b = class_1799.field_8037;
                  return true;
               }

               if (this.b.length - 1 >= this.f) {
                  this.a(slot);
                  this.e = -1;
                  return true;
               }

               return true;
            }

            if (button == 0 && segment.e != null) {
               segment.e.execute();
            }
         }

         this.e = slot;
         return true;
      }
   }

   public void a(class_332 context, int mouseX, int mouseY, class_5611 center) {
      float cx = center.method_32118();
      float cy = center.method_32119();
      float iconRadius = (this.c + this.d) / 2.0F;
      int count = this.b.length;

      for (int slot = 0; slot < count; slot++) {
         double startAngle = -1.5707963285412472 + 6.2831872368967865 / count * slot;
         double endAngle = startAngle + 6.2831872368967865 / count;
         double drawStart = startAngle + (endAngle - startAngle) * 0.010000000036845655 / 2.0;
         double drawEnd = endAngle - (endAngle - startAngle) * 0.010000000036845655 / 2.0;
         boolean isSelected = slot == this.e && !(aM_.field_1755 instanceof AssistantScreen);
         RadialScreen.a segment = this.b[slot];
         if (segment != null) {
            segment.a().a(0.0F, 1.0F, 0.3F, EasingList.g, aM_.method_61966().method_60637(false));
            segment.a().a(slot == this.a(mouseX, mouseY, center));
            int primary = ColorUtil.a(Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), 80);
            int hoverColor = ColorUtil.a(ColorUtil.a(255, 255, 255, 80), primary, segment.a().c());
            boolean assistant = aM_.field_1755 instanceof AssistantScreen;
            int amount = assistant && !segment.b.method_7960() ? InventoryUtil.c(segment.b, false) : -1;
            int fillColor = (isSelected || amount == 0) && !segment.b.method_7960() ? ColorUtil.a(255, 128, 128, 80) : hoverColor;
            double midAngle = (startAngle + endAngle) / 2.0;
            float lift = segment.a().c() * 4.0F;
            float offsetX = (float)Math.cos(midAngle) * lift;
            float offsetY = (float)Math.sin(midAngle) * lift;
            this.a(context.method_51448().method_23760().method_23761(), cx + offsetX, cy + offsetY, this.c, this.d, drawStart, drawEnd, fillColor);
            float iconX = cx + offsetX + (float)Math.cos(midAngle) * iconRadius;
            float iconY = cy + offsetY + (float)Math.sin(midAngle) * iconRadius;
            if (!segment.b.method_7960()) {
               Westra.h().d().j().a(context, segment.b, iconX - 8.0F, iconY - 8.0F, 0, 1.0F, 1.0F, false);
               if (assistant && amount > 0) {
                  String label = String.valueOf(amount);
                  Fonts.e
                     .a(
                        context.method_51448(),
                        label,
                        iconX + 8.0F - Fonts.e.a(label, 10.0F),
                        iconY + 10.0F - Fonts.e.a(10.0F),
                        10.0F,
                        ColorUtil.a(255, 255, 255, amount > 0 ? 235 : 140)
                     );
               }
            } else {
               Fonts.e
                  .a(context.method_51448(), "✗", iconX - Fonts.e.a("✗", 14.0F) / 2.0F, iconY - Fonts.e.a(14.0F) / 2.0F, 14.0F, ColorUtil.a(255, 255, 255, 255));
            }
         }
      }

      int hovered = this.a(mouseX, mouseY, center);
      float baseY = cy + this.d + 8.0F;
      boolean hasSegment = hovered >= 0 && this.b[hovered] != null && !this.b[hovered].b.method_7960();
      String string;
      if (hasSegment) {
         string = this.b[hovered].c != null ? this.b[hovered].c : this.b[hovered].b.method_7964().getString();
      } else {
         string = null;
      }

      if (hasSegment) {
         Fonts.e.a(context.method_51448(), string, cx - Fonts.e.a(string, 10.0F) / 2.0F, baseY, 10.0F, ColorUtil.a(255, 255, 255, 255));
         if (aM_.field_1755 instanceof AssistantScreen) {
            Fonts.d
               .a(
                  context.method_51448(),
                  "СКМ – добавление слота",
                  cx - Fonts.d.a("СКМ – добавление слота", 7.0F) / 2.0F,
                  baseY + Fonts.d.a(10.0F) + 3.0F,
                  7.0F,
                  ColorUtil.a(255, 255, 255, 125)
               );
         }
      } else {
         if (aM_.field_1755 instanceof AssistantScreen) {
            Fonts.d
               .a(
                  context.method_51448(),
                  "СКМ – добавление слота",
                  cx - Fonts.d.a("СКМ – добавление слота", 7.0F) / 2.0F,
                  baseY,
                  7.0F,
                  ColorUtil.a(255, 255, 255, 125)
               );
         }
      }
   }

   public class_1799 c(int slot) {
      RadialScreen.a segment = this.b[Math.floorMod(slot, this.b.length)];
      return segment != null ? segment.b : class_1799.field_8037;
   }

   public void a(int slot, Action action) {
      RadialScreen.a segment = this.b[Math.floorMod(slot, this.b.length)];
      if (segment != null) {
         segment.e = action;
      }
   }

   public Action d(int slot) {
      RadialScreen.a segment = this.b[Math.floorMod(slot, this.b.length)];
      return segment != null ? segment.e : null;
   }

   public void a(int slot, class_1799 stack) {
      RadialScreen.a segment = this.b[Math.floorMod(slot, this.b.length)];
      if (segment != null) {
         segment.b = stack.method_7960() ? class_1799.field_8037 : stack.method_7972();
      }
   }

   public void a(int from, int to) {
      RadialScreen.a a2 = this.b[Math.floorMod(from, this.b.length)];
      RadialScreen.a b = this.b[Math.floorMod(to, this.b.length)];
      if (a2 != null && b != null && a2 != b) {
         class_1799 icon = a2.b;
         String label = a2.c;
         a2.b = b.b;
         a2.c = b.c;
         b.b = icon;
         b.c = label;
      }
   }

   public int a(double mouseX, double mouseY, class_5611 center) {
      double distance = Math.hypot(mouseX - center.method_32118(), mouseY - center.method_32119());
      if (distance < this.c * 0.25) {
         return -1;
      } else {
         double angle = Math.atan2(mouseY - center.method_32119(), mouseX - center.method_32118()) + 1.5707964162115484;
         if (angle < 0.0) {
            angle += 6.2831872368967865;
         }

         return Math.min((int)(angle / (6.2831872368967865 / this.b.length)), this.b.length - 1);
      }
   }

   private void a(Matrix4f matrix, float cx, float cy, float innerR, float outerR, double start, double end, int fillColor) {
      float[] cFill = ColorUtil.a(fillColor);
      RenderSystem.enableBlend();
      RenderSystem.defaultBlendFunc();
      RenderSystem.setShader(class_10142.field_53876);
      class_287 fillBuffer = class_289.method_1348().method_60827(class_5596.field_27380, class_290.field_1576);
      double fillStep = (end - start) / 64.0;

      for (int i = 0; i <= 64; i++) {
         double angle = start + fillStep * i;
         float cos = (float)Math.cos(angle);
         float sin = (float)Math.sin(angle);
         fillBuffer.method_22918(matrix, cx + cos * outerR, cy + sin * outerR, 0.0F).method_22915(cFill[0], cFill[1], cFill[2], cFill[3]);
         fillBuffer.method_22918(matrix, cx + cos * innerR, cy + sin * innerR, 0.0F).method_22915(cFill[0], cFill[1], cFill[2], cFill[3]);
      }

      class_286.method_43433(fillBuffer.method_60800());
      int outlineColor = ColorUtil.a(fillColor, 0.8627451F);
      float[] cOutline = ColorUtil.a(outlineColor);
      GL11.glEnable(2848);
      GL11.glHint(3154, 4354);
      class_287 outlineBuffer = class_289.method_1348().method_60827(class_5596.field_29344, class_290.field_1576);
      double step = (end - start) / 48.0;

      for (int i2 = 0; i2 < 48; i2++) {
         double a1 = start + step * i2;
         double a2 = start + step * (i2 + 1);
         outlineBuffer.method_22918(matrix, cx + (float)Math.cos(a1) * outerR, cy + (float)Math.sin(a1) * outerR, 0.0F)
            .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
         outlineBuffer.method_22918(matrix, cx + (float)Math.cos(a2) * outerR, cy + (float)Math.sin(a2) * outerR, 0.0F)
            .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
      }

      for (int i3 = 0; i3 < 48; i3++) {
         double a3 = start + step * i3;
         double a4 = start + step * (i3 + 1);
         outlineBuffer.method_22918(matrix, cx + (float)Math.cos(a3) * innerR, cy + (float)Math.sin(a3) * innerR, 0.0F)
            .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
         outlineBuffer.method_22918(matrix, cx + (float)Math.cos(a4) * innerR, cy + (float)Math.sin(a4) * innerR, 0.0F)
            .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
      }

      outlineBuffer.method_22918(matrix, cx + (float)Math.cos(start) * innerR, cy + (float)Math.sin(start) * innerR, 0.0F)
         .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
      outlineBuffer.method_22918(matrix, cx + (float)Math.cos(start) * outerR, cy + (float)Math.sin(start) * outerR, 0.0F)
         .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
      outlineBuffer.method_22918(matrix, cx + (float)Math.cos(end) * innerR, cy + (float)Math.sin(end) * innerR, 0.0F)
         .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
      outlineBuffer.method_22918(matrix, cx + (float)Math.cos(end) * outerR, cy + (float)Math.sin(end) * outerR, 0.0F)
         .method_22915(cOutline[0], cOutline[1], cOutline[2], cOutline[3]);
      class_286.method_43433(outlineBuffer.method_60800());
      GL11.glDisable(2848);
      RenderSystem.disableBlend();
   }

   public static class a {
      private final AnimationUtil a = new AnimationUtil();
      class_1799 b;
      String c;
      final boolean d;
      Action e;

      @Generated
      public AnimationUtil a() {
         return this.a;
      }

      public a(class_1799 icon, String label, Action action, boolean editable) {
         this.b = icon;
         this.c = label;
         this.e = action;
         this.d = editable;
      }
   }
}
