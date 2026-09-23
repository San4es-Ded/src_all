package aethereal.ui.widget.system;

import aethereal.core.GlobalEvent;
import aethereal.core.Interface;
import aethereal.core.Westra;
import aethereal.render.ColorUtil;
import aethereal.staff.StaffConstructor;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_408;

public class SystemStaffWidget extends SystemListWidget implements Interface {
   public SystemStaffWidget() {
      super("Стафф", "Staff List");
   }

   @Override
   protected List<SystemListWidget.a> q() {
      List<SystemListWidget.a> rows = new ArrayList<>();

      for (StaffConstructor staff : Westra.h().d().f().a()) {
         float animation = staff.b().c();
         if (!(animation <= 0.0F)) {
            boolean near = a(staff.a());
            rows.add(
               new SystemListWidget.a(
                  staff.a(), near ? "Near" : "Online", -1, near ? ColorUtil.a(255, 111, 181, 255) : ColorUtil.a(160, 160, 170, 255), animation
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
      boolean visible = aM_.field_1755 instanceof class_408;
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
}
