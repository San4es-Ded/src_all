package aethereal.ui.widget;

import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.event.DrawEvent;
import aethereal.mixin.IItemCooldownManager;
import aethereal.render.AnimationUtil;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.element.DragInfo;
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

public class CooldownsWidget extends Widget implements Interface {
   public CooldownsWidget() {
      super(new DragInfo("Задержки", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float x = this.j().a();
      float y = this.j().b();
      float targetWidth = 14.5F + Fonts.e.a("Cooldowns", this.e) + 5.0F + 2.0F;
      float contentY = y + this.d + 3.0F;
      ItemCooldownManagerAccessor accessor = (ItemCooldownManagerAccessor)aM_.field_1724.method_7357();
      boolean active = false;

      for (Entry<class_2960, Object> entry : accessor.getEntries().entrySet()) {
         IItemCooldownManager cooldown = (IItemCooldownManager)entry.getValue();
         ItemCooldownEntryAccessor end = (ItemCooldownEntryAccessor)entry.getValue();
         if (cooldown.getAnimation().c() > 0.0F) {
            class_1792 item = (class_1792)class_7923.field_41178.method_63535(entry.getKey());
            active = true;
            int remaining = Math.max(end.getEndTick() - accessor.getTick(), 0);
            targetWidth = Math.max(
               targetWidth,
               19.0F + Fonts.e.a(item.method_63680().getString(), 6.5F) + 8.0F + Fonts.e.a(String.format("%.1fс", remaining / 20.0F), 6.5F) + 5.0F + 2.0F
            );
         }
      }

      float width = MathUtil.c(this.j().f(), targetWidth, 0.5F);
      this.j().c(width);
      if (this.a() > 0.0F) {
         this.a(event, "d", "Cooldowns", width, this.a());
      }

      for (Entry<class_2960, Object> entry2 : accessor.getEntries().entrySet()) {
         IItemCooldownManager cooldown2 = (IItemCooldownManager)entry2.getValue();
         ItemCooldownEntryAccessor end2 = (ItemCooldownEntryAccessor)entry2.getValue();
         AnimationUtil animationUtil = cooldown2.getAnimation();
         animationUtil.a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         float animation = animationUtil.c() * this.a();
         if (animation > 0.0F) {
            class_1792 item2 = (class_1792)class_7923.field_41178.method_63535(entry2.getKey());
            int remaining2 = Math.max(end2.getEndTick() - accessor.getTick(), 0);
            String time = String.format(Locale.US, "%.1fс", remaining2 / 20.0F);
            float offsetX = -8.0F * (1.0F - animation);
            float offsetY = -(1.0F - animation);
            float drawY = contentY + offsetY;
            float timeWidth = Fonts.e.a(time, 6.5F);
            float textY = drawY + (11.5F - Fonts.e.a(6.5F)) / 2.0F - 0.5F;
            this.a(event, x + offsetX, drawY, width, 11.5F, false, animation);
            this.a(event, x + offsetX + 15.0F, drawY, 11.5F, animation);
            event.e().a(event.i(), item2.method_7854(), x + offsetX + 5.0F, drawY + 2.0F, 0, animation, 0.45F, false);
            Fonts.e.a(event.h(), item2.method_63680().getString(), x + offsetX + 19.0F, textY, 6.5F, ColorUtil.a(-1, animation));
            Fonts.e.a(event.h(), time, x + offsetX + width - 5.0F - timeWidth - 1.0F, textY, 6.5F, ColorUtil.a(-1, 0.55F * animation));
            contentY += 13.5F * animation;
         }
      }

      this.j().d(active ? contentY - y - 2.0F : this.d);
      super.a(event);
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = aM_.field_1755 instanceof class_408;
      ItemCooldownManagerAccessor accessor = (ItemCooldownManagerAccessor)aM_.field_1724.method_7357();
      int tick = accessor.getTick();

      for (Entry<class_2960, Object> entry : accessor.getEntries().entrySet()) {
         IItemCooldownManager cooldown = (IItemCooldownManager)entry.getValue();
         ItemCooldownEntryAccessor end = (ItemCooldownEntryAccessor)entry.getValue();
         boolean has = InventoryUtil.b((class_1792)class_7923.field_41178.method_63535(entry.getKey())) != -1;
         cooldown.getAnimation().a(has && end.getEndTick() - 5 > tick);
         if (has && cooldown.getAnimation().c() > 0.0) {
            visible = true;
         }
      }

      this.d().a(visible);
      super.a(event);
   }
}
