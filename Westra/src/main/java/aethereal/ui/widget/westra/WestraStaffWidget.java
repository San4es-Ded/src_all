package aethereal.ui.widget.westra;

import aethereal.config.ThemeInfo;
import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.staff.StaffConstructor;
import java.util.ArrayList;
import java.util.List;

public class WestraStaffWidget extends WestraListWidget implements Interface {
   public WestraStaffWidget() {
      super("Стафф", "Персонал");
   }

   @Override
   protected List<WestraListWidget.a> u() {
      List<WestraListWidget.a> rows = new ArrayList<>();

      for (StaffConstructor staff : Westra.h().d().f().a()) {
         float animation = staff.b().c();
         if (!(animation <= 0.0F)) {
            boolean near = a(staff.a());
            rows.add(
               new WestraListWidget.a(
                  staff.a(),
                  near ? "рядом" : "в сети",
                  -1,
                  near ? Westra.h().d().o().a(ThemeInfo.PRIMARY).a() : Westra.h().d().o().a(ThemeInfo.TEXT_DISABLED).a(),
                  animation
               )
            );
         }
      }

      return rows;
   }

   private static boolean a(String name) {
      return aM_.field_1687 == null
         ? false
         : aM_.field_1687.method_18456().stream().anyMatch(player -> player.method_5477().getString().equalsIgnoreCase(name));
   }

   @Override
   public void a(GlobalEvent event) {
      boolean visible = v();
      if (aM_.method_1562() != null) {
         for (StaffConstructor staff : Westra.h().d().f().a()) {
            boolean online = aM_.method_1562()
               .method_2880()
               .stream()
               .anyMatch(entry -> entry.method_2966() != null && entry.method_2966().getName().equalsIgnoreCase(staff.a()));
            staff.b().a(online);
            if (online) {
               visible = true;
            }
         }
      }

      this.d().a(visible);
      super.a(event);
   }

   @Override
   protected String w() {
      return "L";
   }
}
