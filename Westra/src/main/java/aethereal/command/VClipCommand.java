package aethereal.command;

import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_124;
import net.minecraft.class_2172;
import net.minecraft.class_2246;
import net.minecraft.class_2338;
import net.minecraft.class_2828.class_2829;

@Command(
   a = "vclip"
)
public class VClipCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(this.a("up").executes(context -> {
         float offset = this.a(true);
         if (offset != 0.0F) {
            this.a(offset);
            ChatUtil.a("Вы были успешно подняты по Y");
            return 1;
         } else {
            return 1;
         }
      }))).then(this.a("down").executes(context2 -> {
         float offset = this.a(false);
         if (offset != 0.0F) {
            this.a(offset);
            ChatUtil.a("Вы были успешно опущены по Y");
            return 1;
         } else {
            return 1;
         }
      }))).then(this.f("число").executes(context3 -> {
         float offset = this.c(context3, "число");
         this.a(offset);
         ChatUtil.a("Вы были успешно перемещены на " + offset + " по Y");
         return 1;
      }))).executes(context4 -> {
         ChatUtil.a("Использование: .vclip <число|up|down>");
         return 1;
      });
   }

   private void a(float yOffset) {
      double x = aM_.field_1724.method_23317();
      double y = aM_.field_1724.method_23318() + yOffset;
      double z = aM_.field_1724.method_23321();
      aM_.field_1724.field_3944.method_52787(new class_2829(x, y, z, aM_.field_1724.method_24828(), aM_.field_1724.field_5976));
      aM_.field_1724.method_5814(x, y, z);
   }

   private float a(boolean up) {
      class_2338 playerPos = aM_.field_1724.method_24515();
      int startY = up ? 25 : -1;
      int endY = up ? 255 : -255;
      int step = up ? 1 : -1;

      for (int i = startY; i != endY; i += step) {
         class_2338 targetPos = playerPos.method_10069(0, i, 0);
         class_2338 nextPos = playerPos.method_10069(0, i + step, 0);
         if (aM_.field_1687.method_8320(targetPos).method_26215() && aM_.field_1687.method_8320(nextPos).method_26215()) {
            return i + (up ? 1.0F : -1.0F);
         }

         if (!up && aM_.field_1687.method_8320(targetPos).method_27852(class_2246.field_9987)) {
            ChatUtil.a(class_124.field_1080 + "Телепортация в данное место невозможно");
            return 0.0F;
         }
      }

      return 0.0F;
   }
}
