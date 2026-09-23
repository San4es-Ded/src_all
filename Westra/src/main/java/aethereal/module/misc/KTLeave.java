package aethereal.module.misc;

import aethereal.core.Category;
import aethereal.core.EventTarget;
import aethereal.core.Interface;
import aethereal.core.Module;
import aethereal.core.ModuleRegister;
import aethereal.event.TickEvent;
import aethereal.util.ChatUtil;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

@ModuleRegister(
   a = "KT Leave",
   b = "Передаёт ник во временный файл для внешнего лаунчера",
   c = Category.Misc
)
public class KTLeave extends Module implements Interface {
   private int b;

   @Override
   public void b() {
      super.b();
      this.b = 0;
      this.q();
   }

   private void q() {
      if (aM_.field_1724 == null) {
         ChatUtil.a("Нужно быть в игре — ник берётся из профиля.");
      } else {
         String name = aM_.field_1724.method_7334().getName();
         String directory = System.getenv("TEMP");
         if (directory == null || directory.isEmpty()) {
            directory = System.getProperty("java.io.tmpdir");
         }

         try {
            Path temporary = Path.of(directory, "ktleave.txt.tmp");
            Path target = Path.of(directory, "ktleave.txt");
            Files.writeString(temporary, name, StandardCharsets.UTF_8);
            Files.move(temporary, target, StandardCopyOption.REPLACE_EXISTING);
            ChatUtil.a("Ник &c" + name + "&7 передан лаунчеру.");
         } catch (IOException var5) {
            ChatUtil.a("Не удалось записать файл: &c" + var5.getMessage());
         }
      }
   }

   @EventTarget
   public void a(TickEvent event) {
      this.b++;
      if (this.b >= 20) {
         this.a(false);
      }
   }
}
