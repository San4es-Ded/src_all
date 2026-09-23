package aethereal.autobuy;

import aethereal.command.AHCommand;
import aethereal.config.ThemeInfo;
import aethereal.config.ThemeProcessor;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.Draw2DProcessor;
import aethereal.render.Draw3DProcessor;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.render.ScissorUtil;
import aethereal.ui.element.Section;
import aethereal.ui.element.TextField;
import aethereal.util.ChatUtil;
import aethereal.util.MathUtil;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_332;
import net.minecraft.class_4587;
import net.minecraft.class_5611;
import org.joml.Vector4f;

public class AutoBuySection extends Section implements Interface {
   private final TextField b = new TextField(TextField.a.GUI_SETTING, true);
   private final AnimationUtil c = new AnimationUtil();
   private final Vector4f d = new Vector4f();
   private final Vector4f e = new Vector4f();
   private final Vector4f f = new Vector4f();
   private final Vector4f g = new Vector4f();
   private AutoBuyEntry h;
   private AutoBuyEntry i;
   private float j;
   private long lastClickTime;
   private AutoBuyEntry lastClickedItem;

   public AutoBuySection() {
      super("h", "auto-buy section");
      this.b.a("Цена предмета");
   }

   @Override
   public void a(class_332 context, Vector4f frame, int mouseX, int mouseY, float scroll, float delta) {
      float startX = frame.x + 8.0F;
      float top = frame.y;
      float settingsX = startX + 240.0F - 4.0F + 8.0F;
      this.a(context, frame, 20.0F, 4.0F, 10, top, scroll, mouseX, mouseY, delta);
      this.a(context, frame, settingsX, top, mouseX, mouseY, delta);
   }

   private void a(AutoBuyEntry item) {
      this.h = item;
      this.b.g().setLength(0);
      this.b.a(false);
      if (item != null && item.k() > 0.0) {
         this.b.g().append((long)item.k());
      }
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
      List<AutoBuyEntry> items = Westra.h().d().q().e();
      this.j = 0.0F;
      this.i = null;
      if (this.h == null && !items.isEmpty()) {
         this.a(items.getFirst());
      }

      ScissorUtil.a(matrices, frame.x, top, frame.z, bottom - top);

      for (int i = 0; i < items.size(); i++) {
         AutoBuyEntry item = items.get(i);
         float slotX = startX + i % columns * (slot + gap);
         float slotY = top + i / columns * (slot + gap) + scroll;
         this.j = Math.max(this.j, (i / columns + 1) * (slot + gap) - gap);
         if (slotY + slot >= top && slotY <= bottom) {
            if (MathUtil.a(mouseX, mouseY, slotX, slotY, slot, slot)) {
               this.i = item;
            }

            item.j().a(item.l());
            item.j().a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
            draw.a(
               matrices,
               slotX,
               slotY,
               slot,
               slot,
               4.0F,
               ColorUtil.a(ColorUtil.a(255, 255, 255, 255), 0.023529412F * (item == this.h ? 1.0F : (this.i == item ? 0.5F : 0.0F)))
            );
            draw.a(matrices, slotX, slotY, slot, slot, 4.0F, 0.5F, ColorUtil.a(theme.a(ThemeInfo.OUTLINE_SMALL).a(), theme.a(ThemeInfo.OUTLINE_SMALL).b()));
            float on = item.j().c();
            draw3d.a(context, item.a(), slotX + offset, slotY + offset, 0, 0.5F + 0.5F * on, scale, false);
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
      if (this.h != null) {
         float rowX = settingsX + 8.0F;
         float rowW = width - 16.0F;
         float center = y + 10.0F;
         this.h.j().a(this.h.l());
         this.h.j().a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
         float active = this.h.j().c();
         this.g.set(rowX + rowW - 6.0F - 14.0F, center - 4.25F, 14.0F, 8.5F);
         this.d.set(this.g.x - 4.0F - 10.0F, center - 5.0F, 10.0F, 10.0F);
         float nameX = rowX + 20.0F;
         draw3d.a(context, this.h.a(), rowX, center - 6.0F, 0, 0.35F + 0.65F * active, 0.75F, false);
         Fonts.c
            .c(
               matrices,
               this.h.b(),
               nameX,
               center - Fonts.c.a(7.5F) / 2.0F - 0.75F,
               7.5F,
               ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), active),
               this.d.x - 6.0F - nameX
            );
         this.c.a(MathUtil.a(mouseX, mouseY, this.d.x, this.d.y, this.d.z, this.d.w));
         this.c.a(0.0F, 1.0F, 0.4F, EasingList.i, delta);
         float searchValue = this.c.c();
         Fonts.a
            .a(
               matrices,
               "G",
               this.d.x + (this.d.z - Fonts.a.b("G", 8.0F + searchValue)) / 2.0F,
               this.d.y + (this.d.w - Fonts.a.a(8.0F + searchValue)) / 2.0F,
               8.0F + searchValue,
               ColorUtil.a(theme.a(ThemeInfo.TEXT_DISABLED).a(), theme.a(ThemeInfo.TEXT).a(), active)
            );
         draw.a(matrices, this.g.x, this.g.y, this.g.z, this.g.w, 3.25F, ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), active));
         draw.a(matrices, this.g.x, this.g.y, this.g.z, this.g.w, 3.25F, 0.3F, theme.a(ThemeInfo.OUTLINE_SMALL).a());
         draw.a(
            matrices,
            this.g.x + 1.5F + 5.5F * active,
            this.g.y + 1.5F,
            5.5F,
            5.5F,
            1.75F,
            ColorUtil.a(ColorUtil.a(150, 150, 155, 255), ColorUtil.a(255, 255, 255, 255), active)
         );
         this.f.set(rowX + rowW - 14.0F, y + 20.0F + 8.0F, 14.0F, 14.0F);
         this.e.set(this.f.x - 4.0F - 14.0F, y + 20.0F + 8.0F, 14.0F, 14.0F);
         String digits = this.b.g().toString().replace(",", "");
         String str;
         if (digits.isEmpty()) {
            str = "";
         } else {
            Locale locale = Locale.US;
            Object[] objArr = new Object[]{Long.parseLong(digits.length() > 15 ? digits.substring(0, 15) : digits)};
            str = String.format(locale, "%,d", objArr);
         }

         if (!str.contentEquals(this.b.g())) {
            this.b.g().setLength(0);
            this.b.g().append(str);
            this.b.a(this.b.j());
         }

         float fieldWidth = this.e.x - 4.0F - rowX;
         this.b.b(new class_5611(fieldWidth, 14.0F));
         this.b.a(new class_5611(rowX, y + 20.0F + 8.0F));
         this.b.a(context, mouseX, mouseY, delta, 1.0F);
         this.h.a(digits.isEmpty() ? 0.0 : Double.parseDouble(digits));
         boolean saveHover = MathUtil.a(mouseX, mouseY, this.e.x, this.e.y, this.e.z, this.e.w);
         boolean loadHover = MathUtil.a(mouseX, mouseY, this.f.x, this.f.y, this.f.z, this.f.w);
         draw.a(
            matrices,
            this.e.x,
            this.e.y,
            this.e.z,
            this.e.w,
            4.0F,
            ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), (10.0F + 20.0F * (saveHover ? 1.0F : 0.0F)) / 255.0F)
         );
         draw.a(matrices, this.e.x, this.e.y, this.e.z, this.e.w, 4.0F, 0.5F, theme.a(ThemeInfo.OUTLINE_SMALL).a());
         Fonts.a
            .a(matrices, "N", this.e.x + (14.0F - Fonts.a.b("N", 8.0F)) / 2.0F, this.e.y + (14.0F - Fonts.a.a(8.0F)) / 2.0F, 8.0F, theme.a(ThemeInfo.TEXT).a());
         draw.a(
            matrices,
            this.f.x,
            this.f.y,
            this.f.z,
            this.f.w,
            4.0F,
            ColorUtil.a(theme.a(ThemeInfo.PRIMARY).a(), (10.0F + 20.0F * (loadHover ? 1.0F : 0.0F)) / 255.0F)
         );
         draw.a(matrices, this.f.x, this.f.y, this.f.z, this.f.w, 4.0F, 0.5F, theme.a(ThemeInfo.OUTLINE_SMALL).a());
         Fonts.a
            .a(matrices, "A", this.f.x + (14.0F - Fonts.a.b("A", 8.0F)) / 2.0F, this.f.y + (14.0F - Fonts.a.a(8.0F)) / 2.0F, 8.0F, theme.a(ThemeInfo.TEXT).a());
      }
   }

   @Override
   public boolean a(double mouseX, double mouseY, int button) {
      if (button == 0 && MathUtil.a(mouseX, mouseY, this.e.x, this.e.y, this.e.z, this.e.w)) {
         Westra.h().d().q().unSetup();
         ChatUtil.sendMessage("Конфигурация авто-закупки успешно сохранена");
         return true;
      } else if (button == 0 && MathUtil.a(mouseX, mouseY, this.f.x, this.f.y, this.f.z, this.f.w)) {
         Westra.h().d().q().setup();
         ChatUtil.sendMessage("Конфигурация авто-закупки успешно загружена");
         this.a(this.h);
         return true;
      } else if (this.h != null && MathUtil.a(mouseX, mouseY, this.d.x, this.d.y, this.d.z, this.d.w)) {
         aM_.field_1724.field_3944.method_45730("ah search " + AHCommand.cleanSearchQuery(this.h.b()));
         return true;
      } else if (this.h != null && MathUtil.a(mouseX, mouseY, this.g.x, this.g.y, this.g.z, this.g.w)) {
         this.h.a(!this.h.l());
         return true;
      } else {
         if (this.h != null) {
            this.b.a(mouseX, mouseY, button);
            if (this.b.j()) {
               return true;
            }
         }

         if (this.i != null && this.i != this.h) {
            long now = System.currentTimeMillis();
            if (this.lastClickedItem == this.i && now - this.lastClickTime < 300L) {
               this.i.a(!this.i.l());
               this.lastClickTime = 0L;
               this.lastClickedItem = null;
            } else {
               this.a(this.i);
               this.lastClickTime = now;
               this.lastClickedItem = this.i;
            }

            return true;
         } else {
            if (this.i != null && this.i == this.h && button == 0) {
               long now = System.currentTimeMillis();
               if (this.lastClickedItem == this.i && now - this.lastClickTime < 300L) {
                  this.i.a(!this.i.l());
                  this.lastClickTime = 0L;
                  this.lastClickedItem = null;
                  return true;
               }

               this.lastClickTime = now;
               this.lastClickedItem = this.i;
            }

            return false;
         }
      }
   }

   @Override
   public boolean a(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
      this.b.b(mouseX, mouseY, button);
      return this.b.j();
   }

   @Override
   public boolean a(int keyCode, int scanCode, int modifiers) {
      if (this.b.j()) {
         this.b.a(keyCode, scanCode, modifiers);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public boolean a(char chr, int modifiers) {
      if (this.b.j()) {
         this.b.a(chr, modifiers);
         return true;
      } else {
         return false;
      }
   }

   @Override
   public float a(Vector4f frame) {
      return this.j;
   }
}
