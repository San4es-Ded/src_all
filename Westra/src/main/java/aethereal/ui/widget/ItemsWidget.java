package aethereal.ui.widget;

import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.module.misc.ServerAssistant;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
import aethereal.render.Fonts;
import aethereal.ui.element.DragInfo;
import aethereal.util.InventoryUtil;
import aethereal.util.KeyUtil;
import aethereal.util.MathUtil;
import java.util.List;
import java.util.Locale;
import net.minecraft.class_2960;
import net.minecraft.class_408;
import net.minecraft.class_7923;
import platform.inject.accessors.ItemCooldownEntryAccessor;
import platform.inject.accessors.ItemCooldownManagerAccessor;

public class ItemsWidget extends Widget implements Interface {
   public ItemsWidget() {
      super(new DragInfo("Предметы", 0.0F, 0.0F, 0.0F, 0.0F));
      this.j().a(this);
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      ServerAssistant assistant = Westra.h().d().t().aj();
      List<ServerAssistant.b> providers = assistant.q();
      boolean active = false;

      for (ServerAssistant.b provider : providers) {
         provider.a().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
         active |= provider.a().c() > 0.0F;
      }

      boolean example = !active && this.a() > 0.0F;
      float y = this.j().b();
      float x = this.j().a();
      float contentX = x;
      ItemCooldownManagerAccessor accessor = (ItemCooldownManagerAccessor)aM_.field_1724.method_7357();

      for (int i = 0; i < providers.size(); i++) {
         ServerAssistant.b provider2 = providers.get(i);
         float animation = example ? (i < 3 ? this.a() : 0.0F) : provider2.a().c() * this.a();
         if (animation > 0.0F) {
            class_2960 itemId = class_7923.field_41178.method_10221(provider2.c());
            Object entry = accessor.getEntries().get(itemId);
            int remaining = entry != null ? Math.max(((ItemCooldownEntryAccessor)entry).getEndTick() - accessor.getTick(), 0) : 0;
            String label = remaining > 0 ? String.format(Locale.US, "%.1f", remaining / 20.0F) : KeyUtil.b(provider2.b().c());
            float width = 19.5F + Fonts.e.a(label, 6.5F) + 5.0F;
            float textY = y + (this.d - Fonts.e.a(6.5F)) / 2.0F - 0.5F;
            this.a(event, contentX, y, width, this.d, true, animation);
            Westra.h().d().j().a(event.i(), provider2.c().method_7854(), contentX + 3.0F, y + (this.d - 16.0F) / 2.0F + 3.0F, 200, animation, 0.6F, false);
            this.a(event, contentX + 15.5F, y, this.d, animation);
            Fonts.e.a(event.h(), label, contentX + 19.5F, textY, 6.5F, ColorUtil.a(-1, animation));
            contentX += (width + 2.0F) * animation;
         }
      }

      this.j().c(MathUtil.c(this.j().f(), Math.max(0.0F, contentX - x - 2.0F), 0.5F));
      this.j().d(!active && !example ? 0.0F : this.d);
      super.a(event);
   }

   @Override
   public void a(GlobalEvent event) {
      ServerAssistant assistant = Westra.h().d().t().aj();
      boolean visible = aM_.field_1755 instanceof class_408;

      for (ServerAssistant.b provider : assistant.q()) {
         provider.a().a(assistant.m() && provider.b().c() != -1 && provider.b().e().get() && InventoryUtil.b(provider.c()) != -1);
         if (provider.a().c() > 0.0F) {
            visible = true;
         }
      }

      this.d().a(visible);
      super.a(event);
   }
}
