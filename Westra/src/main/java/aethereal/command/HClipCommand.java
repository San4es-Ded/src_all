package aethereal.command;

import aethereal.util.ChatUtil;
import aethereal.util.Rotation;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_243;
import net.minecraft.class_2828.class_2829;

@Command(
   a = "hclip"
)
public class HClipCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(this.a("forward").executes(context -> {
         class_243 dir = c().method_1029();
         float distance = this.a(dir);
         if (distance != 0.0F) {
            this.a(dir, distance);
            ChatUtil.a("Вы были успешно перемещены вперёд по горизонтали взгляда");
            return 1;
         } else {
            return 1;
         }
      }))).then(this.a("back").executes(context2 -> {
         class_243 dir = c().method_1029().method_22882();
         float distance = this.a(dir);
         if (distance != 0.0F) {
            this.a(dir, distance);
            ChatUtil.a("Вы были успешно перемещены назад по горизонтали взгляда");
            return 1;
         } else {
            return 1;
         }
      }))).then(this.f("число").executes(context3 -> {
         float distance = this.c(context3, "число");
         class_243 forward = c().method_1029();
         this.a(forward, distance);
         ChatUtil.a("Вы успешно сдвинулись на " + distance + " блоков по горизонтали взгляда");
         return 1;
      }))).executes(context4 -> {
         ChatUtil.a("Использование: .hclip <число|forward|back>");
         return 1;
      });
   }

   private static class_243 c() {
      return class_243.method_1030(0.0F, Rotation.a().c());
   }

   private void a(class_243 horizontalDirection, float distance) {
      class_243 delta = horizontalDirection.method_1021(distance);
      double x = aM_.field_1724.method_23317() + delta.field_1352;
      double y = aM_.field_1724.method_23318();
      double z = aM_.field_1724.method_23321() + delta.field_1350;
      aM_.field_1724.field_3944.method_52787(new class_2829(x, y, z, aM_.field_1724.method_24828(), aM_.field_1724.field_5976));
      aM_.field_1724.method_5814(x, y, z);
   }

   private float a(class_243 direction) {
      class_243 unit = direction.method_1029();
      class_243 origin = aM_.field_1724.method_19538();

      for (int blocks = 1; blocks <= 255; blocks++) {
         class_2338 here = class_2338.method_49638(origin.method_1019(unit.method_1021(blocks)));
         class_2338 ahead = class_2338.method_49638(origin.method_1019(unit.method_1021(blocks + 1)));
         if (aM_.field_1687.method_8320(here).method_26215() && aM_.field_1687.method_8320(ahead).method_26215()) {
            return blocks + 1;
         }

         if (aM_.field_1687.method_8320(here).method_27852(class_2246.field_9987)) {
            ChatUtil.a(class_124.field_1080 + "Телепортация в данное место невозможно");
            return 0.0F;
         }
      }

      return 0.0F;
   }
}
