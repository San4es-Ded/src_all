package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.PacketEvent;
import aethereal.event.TickEvent;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.setting.StringSetting;
import aethereal.util.CounterUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;
import net.minecraft.class_1703;
import net.minecraft.class_1713;
import net.minecraft.class_640;
import net.minecraft.class_7439;

@ModuleRegister(
   a = "Auto Duel",
   b = "Сам рассылает запросы на дуэль и проходит меню выбора набора",
   c = Category.Misc
)
public class AutoDuel extends Module implements Interface {
   private static final Pattern b = Pattern.compile("^[A-Za-z0-9_]{3,16}$");
   private final ModeSetting c = new ModeSetting(
      "Набор", "Spheres", "Spheres", "Shield", "Spikes 3", "Netherite", "Cheater Paradise", "Bow", "Classic", "Totems", "NoDebuff"
   );
   private final SliderSetting d2 = new SliderSetting("Задержка запроса", 500.0F, 300.0F, 1000.0F, 10.0F);
   private final BooleanSetting e = new BooleanSetting("Игра на деньги", false);
   private final StringSetting f2 = new StringSetting("Ставка", "10000").a(() -> this.e.c());
   private final BooleanSetting g2 = new BooleanSetting("Выключать после старта", true);
   private final List<String> h2 = new ArrayList<>();
   private final CounterUtil i2 = new CounterUtil();
   private final CounterUtil j2 = new CounterUtil();
   private final CounterUtil k2 = new CounterUtil();
   private final CounterUtil l2 = new CounterUtil();
   private double m2;
   private double n2;
   private double o2;

   public AutoDuel() {
      this.a(new Setting[]{this.c, this.d2, this.e, this.f2, this.g2});
   }

   @Override
   public void b() {
      this.h2.clear();
      this.i2.b();
      this.j2.b();
      this.k2.b();
      this.l2.b();
      if (aM_.field_1724 != null) {
         this.m2 = aM_.field_1724.method_23317();
         this.n2 = aM_.field_1724.method_23318();
         this.o2 = aM_.field_1724.method_23321();
      }

      super.b();
   }

   @EventTarget
   public void a(PacketEvent event) {
      if (event.c() && event.d() instanceof class_7439 packet && this.g2.c()) {
         String text = packet.comp_763().getString().toLowerCase(Locale.ROOT);
         boolean starting = text.contains("начало") && text.contains("через") && text.contains("секунд");
         boolean blocked = text.contains("дуэли") && text.contains("запрещено") && text.contains("команд");
         if (starting || blocked) {
            this.a();
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      if (aM_.field_1724 != null && aM_.field_1687 != null && aM_.field_1724.field_3944 != null && aM_.field_1761 != null) {
         this.q();
         this.r();
      }
   }

   private void q() {
      List<String> online = this.s();
      double moved = Math.sqrt(
         Math.pow(this.m2 - aM_.field_1724.method_23317(), 2.0)
            + Math.pow(this.n2 - aM_.field_1724.method_23318(), 2.0)
            + Math.pow(this.o2 - aM_.field_1724.method_23321(), 2.0)
      );
      if (moved > 500.0) {
         this.a();
      } else {
         this.m2 = aM_.field_1724.method_23317();
         this.n2 = aM_.field_1724.method_23318();
         this.o2 = aM_.field_1724.method_23321();
         if (this.j2.a(800L * Math.max(1, online.size()))) {
            this.h2.clear();
            this.j2.b();
         }

         String self = aM_.field_1724.method_7334().getName();

         for (String name : online) {
            if (!this.h2.contains(name) && !name.equals(self) && this.i2.a((long)this.d2.c().floatValue())) {
               this.t(name);
               this.h2.add(name);
               this.i2.b();
            }
         }
      }
   }

   private List<String> s() {
      List<String> names = new ArrayList<>();
      if (aM_.field_1724 != null && aM_.field_1724.field_3944 != null) {
         for (class_640 entry : aM_.field_1724.field_3944.method_2880()) {
            String name = entry.method_2966().getName();
            if (b.matcher(name).matches()) {
               names.add(name);
            }
         }

         return names;
      } else {
         return names;
      }
   }

   private void t(String name) {
      if (this.e.c() && !this.f2.c().isBlank()) {
         aM_.field_1724.field_3944.method_45730("duel " + name + " " + this.f2.c().trim());
      } else {
         aM_.field_1724.field_3944.method_45730("duel " + name);
      }
   }

   private void r() {
      if (aM_.field_1755 != null && aM_.field_1724.field_7512 != null) {
         class_1703 handler = aM_.field_1724.field_7512;
         String title = aM_.field_1755.method_25440().getString().toLowerCase(Locale.ROOT);
         if (title.contains("выбор набора") && this.k2.a(150L)) {
            int slot = this.u();
            if (slot >= 0) {
               aM_.field_1761.method_2906(handler.field_7763, slot, 0, class_1713.field_7794, aM_.field_1724);
            }

            this.k2.b();
         } else {
            if (title.contains("настройка поединка") && this.l2.a(150L)) {
               aM_.field_1761.method_2906(handler.field_7763, 0, 0, class_1713.field_7794, aM_.field_1724);
               this.l2.b();
            }
         }
      }
   }

   private int u() {
      String var1 = this.c.c();

      return switch (var1) {
         case "Shield" -> 0;
         case "Spikes 3" -> 1;
         case "Bow" -> 2;
         case "Totems" -> 3;
         case "NoDebuff" -> 4;
         case "Spheres" -> 5;
         case "Classic" -> 6;
         case "Cheater Paradise" -> 7;
         case "Netherite" -> 8;
         default -> -1;
      };
   }
}
