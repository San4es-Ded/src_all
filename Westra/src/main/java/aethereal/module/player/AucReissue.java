package aethereal.module.player;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import aethereal.util.ServerUtil;
import it.unimi.dsi.fastutil.ints.Int2ObjectMaps;
import net.minecraft.class_1713;
import net.minecraft.class_1802;
import net.minecraft.class_2813;
import net.minecraft.class_465;
import net.minecraft.class_476;
import net.minecraft.class_7439;

@ModuleRegister(
   a = "Auc Reissue",
   b = "Автоматически перевыставляет предметы на аукционе",
   c = Category.Player
)
public class AucReissue extends Module implements Interface {
   private boolean b;

   @Override
   public void b() {
      super.b();
      this.b = false;
   }

   @Override
   public void c() {
      super.c();
      this.b = false;
   }

   @EventTarget
   public void a(TickEvent event) {
      if (!ServerUtil.e()
         && (ServerUtil.a.d() != -1 || ServerUtil.d.b() != -1)
         && aM_.field_1724.field_6012 >= 220
         && !Westra.h().d().v().g().a()
         && !aM_.field_1724.method_7357().method_7904(class_1802.field_8557.method_7854())) {
         class_465<?> class_465Var = (class_465<?>)aM_.field_1755;
         if (class_465Var instanceof class_465) {
            if (class_465Var instanceof class_476) {
               String title = class_465Var.method_25440().getString();
               if (aM_.field_1724.field_6012 % 5 == 0) {
                  if (title.matches(".*А.*у.*к.*ц.*и.*о.*н.*")) {
                     aM_.field_1724
                        .field_3944
                        .method_52787(
                           new class_2813(
                              class_465Var.method_17577().field_7763,
                              class_465Var.method_17577().method_37421(),
                              46,
                              1,
                              class_1713.field_7790,
                              class_465Var.method_17577().method_34255().method_7972(),
                              Int2ObjectMaps.emptyMap()
                           )
                        );
                  } else if (title.matches(".*Х.*р.*а.*н.*и.*л.*и.*щ.*е.*")) {
                     aM_.field_1724
                        .field_3944
                        .method_52787(
                           new class_2813(
                              class_465Var.method_17577().field_7763,
                              class_465Var.method_17577().method_37421(),
                              52,
                              1,
                              class_1713.field_7790,
                              class_465Var.method_17577().method_34255().method_7972(),
                              Int2ObjectMaps.emptyMap()
                           )
                        );
                  }
               }
            } else if (aM_.field_1724.field_6012 % 20 == 0) {
               aM_.field_1724.field_3944.method_45730("ah");
            }
         } else if (aM_.field_1724.field_6012 % 20 == 0) {
            aM_.field_1724.field_3944.method_45730("ah");
         }
      }

      if (this.b && aM_.field_1755 instanceof class_476) {
         aM_.field_1724.method_7346();
         this.b = false;
      }
   }

   @EventTarget
   public void a(PacketEvent eventPacket) {
      if (!ServerUtil.e()
         && (ServerUtil.a.d() != -1 || ServerUtil.d.b() != -1)
         && aM_.field_1724.field_6012 >= 220
         && eventPacket.c()
         && eventPacket.d() instanceof class_7439 class_7439VarD) {
         String msg = class_7439VarD.comp_763().getString();
         if (msg.equals("Данная команда недоступна в режиме AFK")) {
            Westra.h().d().v().g().a(10);
         }

         if (msg.equals("[☃] В хранилище отсутствуют предметы для перевыставления.")) {
            ChatUtil.a("Авто-выключение: в хранилище отсутствуют предметы для перевыставления");
            this.a();
         }

         if (msg.contains("[☃] Предметы успешно перевыставлены ") || msg.contains("[✔] Предметы успешно перевыставлены!")) {
            aM_.field_1724.method_7357().method_62835(class_1802.field_8557.method_7854(), 1200);
            this.b = true;
         }

         if (msg.contains("[☃] Вы можете переставлять предметы раз в минуту! Подождите ")) {
            int seconds = Integer.parseInt(msg.replaceAll(".*Подождите (\\d+) сек\\..*", "$1"));
            aM_.field_1724.method_7357().method_62835(class_1802.field_8557.method_7854(), seconds * 20 + 20);
            this.b = true;
         }
      }
   }
}
