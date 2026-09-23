package aethereal.command;

import aethereal.core.EventManager;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.util.ChatUtil;
import aethereal.util.UnhookUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.class_2172;
import net.minecraft.class_2561;
import net.minecraft.class_437;

@Command(
   a = "anhook"
)
public class AnhookCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      builder.executes(context -> {
         ChatUtil.a("Снимаю клиент с игры. Обратно только с новым файлом мода.");
         q();
         return 1;
      });
   }

   private static void q() {
      for (Module module : Westra.h().d().t().e()) {
         if (module.m()) {
            module.a(false);
         }
      }

      class_437 screen = aM_.field_1755;
      if (screen != null && screen.getClass().getName().startsWith("aethereal.")) {
         aM_.method_1507(null);
      }

      EventManager.c();
      if (aM_.field_1705 != null) {
         aM_.field_1705.method_1743().method_1808(true);
      }

      List<Path> targets = new ArrayList<>(UnhookUtil.a());
      targets.addAll(UnhookUtil.b());
      List<Path> left = new ArrayList<>();

      for (Path target : targets) {
         if (!UnhookUtil.a(target)) {
            left.add(target);
         }
      }

      if (!left.isEmpty()) {
         UnhookUtil.a(left);
      }

      if (targets.isEmpty()) {
         r("Клиент отключён. Файлов для удаления не найдено.");
      } else {
         r("Клиент отключён. Мод и логи будут удалены сразу после выхода из игры.");
      }
   }

   private static void r(String message) {
      if (aM_.field_1705 != null) {
         aM_.field_1705.method_1743().method_1812(class_2561.method_43470(message));
      }
   }
}
