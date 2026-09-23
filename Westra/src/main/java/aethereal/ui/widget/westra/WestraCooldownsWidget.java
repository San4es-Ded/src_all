package aethereal.ui.widget.westra;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.mixin.IItemCooldownManager;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.recode.RecodeKit;
import aethereal.util.InventoryUtil;
import aethereal.util.MathUtil;
import java.util.Locale;
import java.util.Map.Entry;
import net.minecraft.class_1792;
import net.minecraft.class_2960;
import net.minecraft.class_408;
import net.minecraft.class_7923;
import platform.inject.accessors.ItemCooldownEntryAccessor;
import platform.inject.accessors.ItemCooldownManagerAccessor;

public class WestraCooldownsWidget extends WestraWidget implements Interface {
   private static final float E = 13.0F;

   public WestraCooldownsWidget() {
      super("Задержки");
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      if (aM_.field_1724 == null) {
         super.a(event);
      } else {
         float header = 15.0F;
         ItemCooldownManagerAccessor accessor = (ItemCooldownManagerAccessor)aM_.field_1724.method_7357();
         int count = 0;
         float target = 20.0F + Fonts.d.a("Задержки", 6.5F) + 6.0F + WestraStyle.f("9") + 5.0F;
         float rows = 0.0F;

         for (Entry<class_2960, Object> entry : accessor.getEntries().entrySet()) {
            AnimationUtil row = ((IItemCooldownManager)entry.getValue()).getAnimation();
            row.a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            if (!(row.c() <= 0.0F)) {
               count++;
               class_1792 item = (class_1792)class_7923.field_41178.method_63535(entry.getKey());
               target = Math.max(target, 21.0F + Fonts.d.a(item.method_63680().getString(), 6.25F) + 8.0F + WestraStyle.f("99.9с") + 5.0F);
               rows += 13.0F * row.c();
            }
         }

         float width = MathUtil.c(this.j().f(), Math.max(80.0F, target), 0.35F);
         float height = header + rows + 3.0F;
         float x = this.j().a();
         float y = this.j().b();
         this.j().c(width);
         this.j().d(height);
         if (animation <= 0.0F) {
            super.a(event);
         } else {
            this.q(event, x, y, width, height, animation, false);
            WestraStyle.e(event, "T", x + 4.0F, y + (header - 9.0F) / 2.0F, 9.0F, animation);
            Fonts.d.a(event.h(), "Задержки", x + 17.0F, Fonts.d.a("Задержки", 6.5F, y + header / 2.0F), 6.5F, ColorUtil.a(-1, animation));
            WestraStyle.f(event, String.valueOf(count), x + width - 4.5F, y + header / 2.0F, -1, animation);
            if (rows > 0.5F) {
               event.d().a(event.h(), x + 5.0F, y + header - 0.25F, width - 10.0F, 0.5F, 0.0F, ColorUtil.a(-1, 0.07F * animation));
            }

            float rowY = y + header;
            int accent = Westra.h().d().o().a(ThemeInfo.PRIMARY).a();

            for (Entry<class_2960, Object> entryx : accessor.getEntries().entrySet()) {
               AnimationUtil row = ((IItemCooldownManager)entryx.getValue()).getAnimation();
               float value = row.c() * animation;
               if (!(value <= 0.002F)) {
                  class_1792 item = (class_1792)class_7923.field_41178.method_63535(entryx.getKey());
                  ItemCooldownEntryAccessor cooldown = (ItemCooldownEntryAccessor)entryx.getValue();
                  int left = Math.max(cooldown.getEndTick() - accessor.getTick(), 0);
                  int total = Math.max(1, cooldown.getEndTick() - cooldown.getStartTick());
                  String time = String.format(Locale.US, "%.1fс", left / 20.0F);
                  float rowHeight = 13.0F * row.c();
                  float shift = (1.0F - EasingList.p.ease(row.c())) * 8.0F;
                  event.h().method_22903();
                  event.h().method_46416(shift, 0.0F, 0.0F);
                  float center = rowY + rowHeight / 2.0F - 0.75F;
                  event.e().a(event.i(), item.method_7854(), x + 7.0F, center - 5.0F, 0, value, 0.625F, false);
                  Fonts.d.a(event.h(), item.method_63680().getString(), x + 7.0F + 10.0F + 4.0F, Fonts.d.a("A", 6.25F, center), 6.25F, ColorUtil.a(-1, value));
                  WestraStyle.f(event, time, x + width - 4.5F, center, -1, value);
                  float barX = x + 7.0F;
                  float barW = width - 11.5F;
                  float barY = rowY + rowHeight - 2.25F;
                  event.d().a(event.h(), barX, barY, barW, 1.0F, 0.5F, ColorUtil.a(-1, 0.06F * value));
                  int barLeft = ColorUtil.a(accent, value);
                  int barRight = ColorUtil.a(RecodeKit.accentShade(), value);
                  event.d()
                     .a(event.h(), barX, barY, Math.max(1.0F, barW * Math.min(1.0F, (float)left / total)), 1.0F, 0.5F, barLeft, barRight, barLeft, barRight);
                  event.h().method_22909();
                  rowY += rowHeight;
               }
            }

            super.a(event);
         }
      }
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = aM_.field_1755 instanceof class_408;
      if (aM_.field_1724 != null) {
         ItemCooldownManagerAccessor accessor = (ItemCooldownManagerAccessor)aM_.field_1724.method_7357();
         int tick = accessor.getTick();

         for (Entry<class_2960, Object> entry : accessor.getEntries().entrySet()) {
            IItemCooldownManager cooldown = (IItemCooldownManager)entry.getValue();
            boolean has = InventoryUtil.b((class_1792)class_7923.field_41178.method_63535(entry.getKey())) != -1;
            cooldown.getAnimation().a(has && ((ItemCooldownEntryAccessor)entry.getValue()).getEndTick() - 5 > tick);
            if (has && cooldown.getAnimation().c() > 0.0F) {
               visible = true;
            }
         }
      }

      this.d().a(visible);
      super.a(event);
   }
}
