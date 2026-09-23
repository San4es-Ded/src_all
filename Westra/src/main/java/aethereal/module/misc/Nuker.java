package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.core.Westra;
import aethereal.event.DrawEvent;
import aethereal.event.InputEvent;
import aethereal.event.TickEvent;
import aethereal.render.ColorUtil;
import aethereal.setting.BooleanSetting;
import aethereal.setting.ModeSetting;
import aethereal.setting.Setting;
import aethereal.setting.SliderSetting;
import aethereal.util.ChatUtil;
import aethereal.util.InventoryUtil;
import aethereal.util.Look;
import aethereal.util.MathUtil;
import aethereal.util.MoveUtil;
import aethereal.util.Rotation;
import aethereal.util.ServerUtil;
import net.minecraft.class_1268;
import net.minecraft.class_1799;
import net.minecraft.class_1810;
import net.minecraft.class_2338;
import net.minecraft.class_2350;
import net.minecraft.class_238;
import net.minecraft.class_243;
import net.minecraft.class_2680;
import net.minecraft.class_3532;
import net.minecraft.class_3959;
import net.minecraft.class_3959.class_242;
import net.minecraft.class_3959.class_3960;

@ModuleRegister(
   a = "Nuker",
   b = "Автоматически разрушает блоки в радиусе досягаемости",
   c = Category.Misc
)
public class Nuker extends Module {
   private final ModeSetting b = new ModeSetting("Режим копания территории", "Шахта ФанТайм", "Шахта ФанТайм", "Общий");
   private final SliderSetting c = new SliderSetting("Дистанция копания", 4.0F, 1.0F, 6.0F, 0.5F);
   private final SliderSetting d = new SliderSetting("Скорость копания", 1.0F, 1.0F, 5.0F, 1.0F);
   private final BooleanSetting e = new BooleanSetting("Не копать под себя", true);
   private class_2338 f;

   public Nuker() {
      this.a(new Setting[]{this.b, this.c, this.d, this.e});
   }

   @Override
   public void b() {
      super.b();
      this.f = null;
   }

   @Override
   public void c() {
      super.c();
      this.f = null;
   }

   @EventTarget
   public void a(TickEvent event) {
      MineAssistant assistant = Westra.h().d().t().ak();
      this.f = null;
      class_1799 tool = aM_.field_1724.method_6047();
      if (tool.method_7963() && tool.method_7936() - tool.method_7919() < 50) {
         ChatUtil.a("Работа прекращена во избежание поломки кирки.");
         this.a();
      } else {
         if (this.b.l("Шахта ФанТайм") && ServerUtil.a.a()) {
            assistant.q();
         }

         boolean pickaxe = tool.method_7909() instanceof class_1810;
         int minY = this.e.c() ? aM_.field_1724.method_31478() + (pickaxe && InventoryUtil.a(tool, "Бульдозер") ? 1 : 0) : Integer.MIN_VALUE;
         float range = this.c.h();
         class_238 scan = aM_.field_1724.method_5829().method_1014(range + 1.0F);
         class_243 eye = aM_.field_1724.method_33571();
         class_243 look = aM_.field_1724.method_5828(1.0F);
         double bestScore = 1.7976922776554427E308;
         class_2350 face = null;

         for (class_2338 pos : class_2338.method_10097(
            class_2338.method_49637(scan.field_1323, scan.field_1322, scan.field_1321),
            class_2338.method_49637(scan.field_1320, scan.field_1325, scan.field_1324)
         )) {
            if (pos.method_10264() >= minY && this.a(pos, pickaxe, assistant.r())) {
               class_243 center = pos.method_46558();
               class_243 diff = center.method_1020(eye);
               double along = diff.method_1026(look);
               class_243 hit = along > 0.0 ? (class_243)new class_238(pos).method_992(eye, center).orElse(null) : null;
               if (hit != null && eye.method_1025(hit) <= range * range && this.a(eye, pos)) {
                  double score = diff.method_1020(look.method_1021(along)).method_1027();
                  if (score < bestScore) {
                     bestScore = score;
                     this.f = pos.method_10062();
                     face = class_2350.method_58251(hit.method_1020(center));
                  }
               }
            }
         }

         if (this.f != null) {
            Rotation base = Rotation.a(eye, this.f.method_46558());
            Westra.h()
               .d()
               .k()
               .a(
                  new Rotation(
                     class_3532.method_15393(base.c() + MathUtil.a(-3.0F, 3.0F)), class_3532.method_15363(base.d() + MathUtil.a(-3.0F, 3.0F), -90.0F, 90.0F)
                  ),
                  180.0F,
                  1,
                  1
               );
            if (Rotation.b().a(base) <= 20.0) {
               for (int i = 0; i < this.d.h().intValue(); i++) {
                  aM_.field_1761.method_2902(this.f, face);
               }

               aM_.field_1724.method_6104(class_1268.field_5808);
            }
         }
      }
   }

   @EventTarget
   public void a(InputEvent event) {
      if (this.f != null) {
         MoveUtil.a(event, Look.b(), 5);
      }
   }

   @EventTarget
   public void a(DrawEvent event) {
      if (event.c() && this.f != null) {
         event.e().a(event.h(), new class_238(this.f), ColorUtil.a(255, 0, 0, 200), 2.0F);
      }
   }

   private boolean a(class_2338 pos, boolean pickaxe, class_238 mineBox) {
      class_2680 state = aM_.field_1687.method_8320(pos);
      return state.method_26215()
         ? false
         : !this.b.l("Шахта ФанТайм") || pickaxe && mineBox.method_1006(pos.method_46558()) && state.method_26165(aM_.field_1724, aM_.field_1687, pos) >= 1.0F;
   }

   private boolean a(class_243 eye, class_2338 pos) {
      class_238 box = new class_238(pos).method_1011(0.05000000385685581);

      for (int i = -1; i < 8; i++) {
         class_243 point = i < 0
            ? box.method_1005()
            : new class_243(
               (i & 1) == 0 ? box.field_1323 : box.field_1320, (i & 2) == 0 ? box.field_1322 : box.field_1325, (i & 4) == 0 ? box.field_1321 : box.field_1324
            );
         if (aM_.field_1687.method_17742(new class_3959(eye, point, class_3960.field_17558, class_242.field_1348, aM_.field_1724)).method_17777().equals(pos)) {
            return true;
         }
      }

      return false;
   }
}
