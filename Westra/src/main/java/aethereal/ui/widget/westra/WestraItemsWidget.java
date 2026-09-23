package aethereal.ui.widget.westra;

import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.module.misc.ServerAssistant;
import aethereal.render.ColorUtil;
import aethereal.render.EasingList;
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

public class WestraItemsWidget extends WestraWidget implements Interface {
   private static final float E = 4.0F;
   private static final float F = 3.0F;

   public WestraItemsWidget() {
      super("Предметы");
   }

   @Override
   public void a(DrawEvent event) {
      this.d().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      float animation = this.a();
      if (aM_.field_1724 == null) {
         super.a(event);
      } else {
         ServerAssistant assistant = Westra.h().d().t().aj();
         List<ServerAssistant.b> providers = assistant.q();
         boolean active = false;

         for (ServerAssistant.b provider : providers) {
            provider.a().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
            active |= provider.a().c() > 0.0F;
         }

         boolean example = !active && animation > 0.0F;
         float x = this.j().a();
         float y = this.j().b();
         float cursor = x;
         ItemCooldownManagerAccessor accessor = (ItemCooldownManagerAccessor)aM_.field_1724.method_7357();

         for (int index = 0; index < providers.size(); index++) {
            ServerAssistant.b provider = providers.get(index);
            float value = example ? (index < 3 ? animation : 0.0F) : provider.a().c() * animation;
            if (!(value <= 0.002F)) {
               class_2960 id = class_7923.field_41178.method_10221(provider.c());
               Object entry = accessor.getEntries().get(id);
               int left = entry != null ? Math.max(((ItemCooldownEntryAccessor)entry).getEndTick() - accessor.getTick(), 0) : 0;
               String label = left > 0 ? String.format(Locale.US, "%.1f", left / 20.0F) : KeyUtil.b(provider.b().c());
               float width = 17.0F + s("", label) + 4.0F;
               this.q(event, cursor, y, width, 12.0F, value, false);
               event.e().a(event.i(), provider.c().method_7854(), cursor + 4.0F, y + 1.0F, 0, value, 0.625F, false);
               this.r(event, cursor + 4.0F + 10.0F + 3.0F, y, 12.0F, "", label, ColorUtil.a(255, 255, 255, 255), value);
               cursor += (width + 3.0F) * value;
            }
         }

         this.j().c(MathUtil.c(this.j().f(), Math.max(0.0F, cursor - x - 3.0F), 0.4F));
         this.j().d(!active && !example ? 0.0F : 12.0F);
         super.a(event);
      }
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
