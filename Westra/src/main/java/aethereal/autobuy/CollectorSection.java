package aethereal.autobuy;

import aethereal.config.Condition;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Westra;
import aethereal.module.misc.Collector_2;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScissorUtil;
import aethereal.ui.element.Section;
import aethereal.util.MathUtil;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import org.joml.Vector4f;

public class CollectorSection extends Section {
   private final Vector4f a = new Vector4f();
   private final Vector4f b = new Vector4f();
   private Collector_2.b c;
   private Collector_2.b d;
   private Condition e;
   private int f;
   private float g;

   public CollectorSection() {
      super("S", "collector section");
   }

   @Override
   public void a(class_332 context, Vector4f frame, int mouseX, int mouseY, float scroll, float delta) {
      float startX = frame.x + 8.0F;
      float top = frame.y;
      float settingsX = startX + 240.0F - 4.0F + 8.0F;
      this.a(context, frame, 20.0F, 4.0F, 10, top, scroll, mouseX, mouseY, delta);
      this.a(context, frame, settingsX, top, mouseX, mouseY, delta);
   }

   private void a(class_332 context, Vector4f frame, float slot, float gap, int columns, float top, float scroll, int mouseX, int mouseY, float delta) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      Draw3DProcessor draw3d = Westra.h().d().j();
      ThemeProcessor theme = Westra.h().d().o();
      float startX = frame.x + 8.0F;
      float bottom = frame.y + frame.w;
      float scale = slot / 16.0F * 0.75F;
      float offset = (slot - 16.0F * scale) / 2.0F;
      List<Collector_2.b> items = Westra.h().d().p().e();
      this.g = 0.0F;
      this.d = null;
      if (this.c == null && !items.isEmpty()) {
         this.c = items.getFirst();
      }

      ScissorUtil.a(matrices, frame.x, top, frame.z, bottom - top);

      for (int i = 0; i < items.size(); i++) {
         Collector_2.b item = items.get(i);
         float slotX = startX + i % columns * (slot + gap);
         float slotY = top + i / columns * (slot + gap) + scroll;
         this.g = Math.max(this.g, (i / columns + 1) * (slot + gap) - gap);
         if (slotY + slot >= top && slotY <= bottom) {
            if (MathUtil.a(mouseX, mouseY, slotX, slotY, slot, slot)) {
               this.d = item;
            }

            item.h().a(item.k());
            item.h().a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
            float on = item.h().c();
            draw.a(
               matrices,
               slotX,
               slotY,
               slot,
               slot,
               4.0F,
               ColorUtil.a(ColorUtil.a(255, 255, 255, 255), 0.023529412F * (item == this.c ? 1.0F : (this.d == item ? 0.5F : 0.0F)))
            );
            draw.a(matrices, slotX, slotY, slot, slot, 4.0F, 0.5F, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b()));
            draw3d.a(context, item.c(), slotX + offset, slotY + offset, 0, 0.5F + 0.5F * on, scale, false);
            if (item.m() > 1) {
               String count = String.valueOf(item.m());
               Fonts.c
                  .a(
                     matrices,
                     count,
                     slotX + slot - 2.0F - Fonts.c.a(count, 6.5F),
                     slotY + slot - 8.0F,
                     6.5F,
                     ColorUtil.a(theme.a(ThemeInfo.TEXT).a(), 0.25F + 0.75F * item.h().c())
                  );
            }
         }
      }

      ScissorUtil.a(matrices);
   }

   private void a(class_332 context, Vector4f frame, float settingsX, float y, int mouseX, int mouseY, float delta) {
      class_4587 matrices = context.method_51448();
      Draw2DProcessor draw = Westra.h().d().i();
      Draw3DProcessor draw3d = Westra.h().d().j();
      ThemeProcessor theme = Westra.h().d().o();
      float width = frame.x + frame.z - 8.0F - settingsX;
      this.e = null;
      if (this.c != null) {
         float rowX = settingsX + 8.0F;
         float rowW = width - 16.0F;
         float center = y + 10.0F;
         this.c.h().a(this.c.k());
         this.c.h().a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
         float active = this.c.h().c();
         float fade = 0.35F + 0.65F * active;
         this.a.set(rowX + rowW - 6.0F - 14.0F, center - 4.25F, 14.0F, 8.5F);
         float nameX = rowX + 20.0F;
         float nameLimit = (this.c.a() ? this.a.x - 6.0F - 40.0F : this.a.x) - 6.0F;
         draw3d.a(context, this.c.c(), rowX, center - 6.0F, 0, fade, 0.75F, false);
         Fonts.c
            .c(
               matrices,
               this.c.j(),
               nameX,
               center - Fonts.c.a(7.5F) / 2.0F - 0.75F,
               7.5F,
               ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), active),
               nameLimit - nameX
            );
         draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 3.25F, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), active));
         draw.a(matrices, this.a.x, this.a.y, this.a.z, this.a.w, 3.25F, 0.3F, theme.a(ThemeInfo.OUTLINE_SMALL).a());
         draw.a(
            matrices,
            this.a.x + 1.5F + 5.5F * active,
            this.a.y + 1.5F,
            5.5F,
            5.5F,
            1.75F,
            ColorUtil.a(ColorUtil.a(150, 150, 155, 255), ColorUtil.a(255, 255, 255, 255), active)
         );
         this.b.set(0.0F, 0.0F, 0.0F, 0.0F);
         if (this.c.a()) {
            this.b.set(this.a.x - 6.0F - 32.0F, center - 5.0F, 32.0F, 10.0F);
            Fonts.c.b(matrices, "✓", this.b.x + 6.0F, center - Fonts.c.a(11.0F) / 2.0F - 2.0F, 11.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), fade));
            Fonts.c
               .b(
                  matrices,
                  String.valueOf(this.c.m()),
                  this.b.x + this.b.z / 2.0F,
                  center - Fonts.c.a(6.75F) / 2.0F - 0.75F,
                  6.75F,
                  ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), active)
               );
            Fonts.c
               .b(
                  matrices,
                  "✗",
                  this.b.x + this.b.z - 6.0F,
                  center - Fonts.c.a(11.0F) / 2.0F - 1.25F,
                  11.0F,
                  ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), fade)
               );
         }

         List<Condition> conditions = Stream.concat(
               this.c.e() == null ? Stream.empty() : this.c.e().b().stream(), this.c.f() == null ? Stream.empty() : this.c.f().b().stream()
            )
            .toList();
         float enchantY = y + 20.0F + 8.0F;

         for (Condition condition : conditions) {
            if (!condition.d()) {
               condition.a().a(condition.b());
               condition.a().a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
               float value = condition.a().c() * active;
               float centerY = enchantY + 5.0F;
               float switchX = rowX + rowW - 6.0F - 13.0F;
               float switchY = centerY - 3.8475F;
               float levelX = switchX - 6.0F - 28.0F;
               Fonts.c
                  .a(
                     matrices,
                     condition.e(),
                     rowX,
                     centerY - Fonts.c.a(6.75F) / 2.0F - 0.75F,
                     6.75F,
                     ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), value)
                  );
               if (condition.c()) {
                  draw.a(
                     context,
                     levelX + 13.625F,
                     centerY - 2.5F,
                     0.75F,
                     5.0F,
                     ColorUtil.a(theme.a(ThemeInfo.OUTLINE_MEDIUM).a(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * fade)
                  );
                  Fonts.c
                     .b(matrices, "✗", levelX + 7.0F, centerY - Fonts.c.a(11.0F) / 2.0F - 1.25F, 11.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), fade));
                  Fonts.c
                     .b(matrices, "✓", levelX + 21.0F, centerY - Fonts.c.a(11.0F) / 2.0F - 2.0F, 11.0F, ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), fade));
               }

               draw.a(matrices, switchX, switchY, 13.0F, 7.695F, 3.078F, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), value));
               draw.a(
                  matrices,
                  switchX,
                  switchY,
                  13.0F,
                  7.695F,
                  3.078F,
                  0.5F,
                  ColorUtil.a(theme.a(ThemeInfo.OUTLINE_MEDIUM).a(), theme.a(ThemeInfo.OUTLINE_MEDIUM).b() * fade)
               );
               draw.a(
                  matrices,
                  switchX + 1.496F + 5.216F * value,
                  switchY + 1.496F,
                  4.703F,
                  4.703F,
                  1.539F,
                  ColorUtil.a(ColorUtil.a(150, 150, 155, 255), ColorUtil.a(255, 255, 255, 255), value)
               );
               if (MathUtil.a(mouseX, mouseY, switchX, switchY, 13.0F, 7.695F)) {
                  this.e = condition;
                  this.f = 0;
               } else if (condition.c() && MathUtil.a(mouseX, mouseY, levelX, centerY - 5.0F, 14.0F, 10.0F)) {
                  this.e = condition;
                  this.f = 1;
               } else if (condition.c() && MathUtil.a(mouseX, mouseY, levelX + 14.0F, centerY - 5.0F, 14.0F, 10.0F)) {
                  this.e = condition;
                  this.f = 2;
               }

               enchantY += 14.0F;
            }
         }
      }
   }

   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      if (this.c != null && MathUtil.a(mouseX, mouseY, this.a.x, this.a.y, this.a.z, this.a.w)) {
         this.c.a(!this.c.k());
         return true;
      } else if (this.c != null && MathUtil.a(mouseX, mouseY, this.b.x, this.b.y, this.b.z / 2.0F, this.b.w)) {
         this.c.b(Math.max(1, this.c.m() - 1));
         return true;
      } else if (this.c != null && MathUtil.a(mouseX, mouseY, this.b.x + this.b.z / 2.0F, this.b.y, this.b.z / 2.0F, this.b.w)) {
         this.c.b(Math.min(this.c.b(), this.c.m() + 1));
         return true;
      } else if (this.e != null) {
         switch (this.f) {
            case 0:
               this.e.a(!this.e.b());
               return true;
            case 1:
               this.e.b(1);
               return true;
            case 2:
               this.e.b(-1);
               return true;
            default:
               return true;
         }
      } else if (this.d != null) {
         this.c = this.d;
         return true;
      } else {
         return false;
      }
   }

   @Override
   public float a(Vector4f frame) {
      return this.g;
   }
}
