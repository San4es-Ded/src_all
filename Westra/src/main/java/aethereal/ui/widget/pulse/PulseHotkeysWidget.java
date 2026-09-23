package aethereal.ui.widget.pulse;

import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.EasingList;
import aethereal.util.KeyUtil;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_408;

public class PulseHotkeysWidget extends PulseListWidget implements Interface {
   public PulseHotkeysWidget() {
      super("Клавиши", "Hotkeys");
   }

   @Override
   public void a(DrawEvent event) {
      for (Module module : Westra.h().d().t().e()) {
         module.f().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      }

      super.a(event);
   }

   @Override
   protected List<PulseListWidget.a> q() {
      List<PulseListWidget.a> rows = new ArrayList<>();

      for (Module module : Westra.h().d().t().e()) {
         if (module.p() != -1) {
            float animation = module.f().c();
            if (animation > 0.0F) {
               rows.add(new PulseListWidget.a(module.j(), KeyUtil.b(module.p()), -1, PulseCard.a, animation));
            }
         }
      }

      return rows;
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = aM_.field_1755 instanceof class_408;

      for (Module module : Westra.h().d().t().e()) {
         if (module.p() != -1 && module.f().c() > 0.0F) {
            visible = true;
            break;
         }
      }

      this.d().a(visible);
      super.a(event);
   }
}
