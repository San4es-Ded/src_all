package aethereal.command;

import aethereal.util.ChatUtil;
import aethereal.util.NeuroData;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.class_2172;

@Command(
   a = "record"
)
public class RecordCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(this.a("off").executes(context -> {
         NeuroData data = NeuroData.a();
         if (!data.b()) {
            ChatUtil.a("Запись и так не идёт.");
            return 1;
         } else {
            data.f();
            ChatUtil.a("Запись остановлена. Образцов накоплено: &c" + data.c() + "&7. Ставьте наведение &cNeuro&7.");
            return 1;
         }
      }))).then(this.a("clear").executes(context2 -> {
         NeuroData.a().h();
         ChatUtil.a("Записанные образцы удалены.");
         return 1;
      }))).then(this.a("status").executes(context3 -> {
         NeuroData data = NeuroData.a();
         ChatUtil.a("Запись: &c" + (data.b() ? "идёт" : "выключена") + "&7. Образцов: &c" + data.c() + "&7.");
         return 1;
      }))).executes(context4 -> {
         NeuroData data = NeuroData.a();
         if (data.b()) {
            ChatUtil.a("Запись уже идёт. Остановить — .record off");
            return 1;
         } else {
            data.d();
            ChatUtil.a("Запись пошла. Бейте как обычно, потом .record off");
            return 1;
         }
      });
   }
}
