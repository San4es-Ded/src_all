package aethereal.ui.widget.westra;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.render.EasingList;
import aethereal.util.KeyUtil;
import java.util.ArrayList;
import java.util.List;

public class WestraHotkeysWidget extends WestraListWidget implements Interface {
   public WestraHotkeysWidget() {
      super("Клавиши", "Клавиши");
   }

   @Override
   public void a(DrawEvent event) {
      for (Module module : Westra.h().d().t().e()) {
         module.f().a(0.0F, 1.0F, 0.3F, EasingList.g, event.g());
      }

      super.a(event);
   }

   @Override
   protected List<WestraListWidget.a> u() {
      List<WestraListWidget.a> rows = new ArrayList<>();

      for (Module module : Westra.h().d().t().e()) {
         if (module.p() != -1) {
            float animation = module.f().c();
            if (!(animation <= 0.0F)) {
               rows.add(new WestraListWidget.a(module.j(), KeyUtil.b(module.p()), -1, Westra.h().d().o().a(ThemeInfo.PRIMARY).a(), animation));
            }
         }
      }

      return rows;
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = v();

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
