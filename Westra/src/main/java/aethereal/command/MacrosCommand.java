package aethereal.command;

import aethereal.core.Westra;
import aethereal.macro.MacrosConstructor;
import aethereal.macro.MacrosProcessor;
import aethereal.util.ChatUtil;
import aethereal.util.KeyUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.List;
import net.minecraft.class_2172;

@Command(
   a = "macros"
)
public class MacrosCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      MacrosProcessor processor = Westra.h().d().d();
      LiteralArgumentBuilder literalArgumentBuilderThen = (LiteralArgumentBuilder)builder.then(((LiteralArgumentBuilder)this.a("add").executes(context -> {
         ChatUtil.a("Использование: .macros add <клавиша> <команда>");
         return 1;
      })).then(this.d("клавиша").suggests(this.b()).then(this.c("команда").executes(context2 -> {
         String key = this.a(context2, "клавиша");
         String command = this.a(context2, "команда");
         KeyUtil keyUtil = KeyUtil.a(key);
         if (keyUtil == KeyUtil.UNKNOWN) {
            ChatUtil.a("Клавиша " + key + " не найдена.");
            return 1;
         } else {
            processor.a(key, command);
            processor.unSetup();
            ChatUtil.a("Макрос " + command + " был успешно добавлен на клавишу " + keyUtil.b() + ".");
            return 1;
         }
      }))));
      LiteralArgumentBuilder literalArgumentBuilderExecutes = (LiteralArgumentBuilder)this.a("remove").executes(context3 -> {
         List<MacrosConstructor> macros = processor.a();
         if (macros.isEmpty()) {
            ChatUtil.a("Список макросов пуст.");
            return 1;
         } else {
            ChatUtil.a("Доступные команды для удаления (" + macros.size() + "):");

            for (MacrosConstructor macro : macros) {
               ChatUtil.a("  " + macro.b() + " (клавиша: " + KeyUtil.a(macro.a()).b() + ")");
            }

            ChatUtil.a("Использование: .macros remove <команда>");
            return 1;
         }
      });
      RequiredArgumentBuilder<class_2172, String> requiredArgumentBuilderC = this.c("команда");
      literalArgumentBuilderThen.then(
            literalArgumentBuilderExecutes.then(requiredArgumentBuilderC.suggests(this.a(processor::a, v0 -> v0.b())).executes(context4 -> {
               String command = this.a(context4, "команда");
               MacrosConstructor macro = processor.a().stream().filter(m -> m.b().equals(command)).findFirst().orElse(null);
               if (macro == null) {
                  ChatUtil.a("Макрос с командой " + command + " не найден в списке макросов.");
                  return 1;
               } else {
                  processor.b(macro.a());
                  processor.unSetup();
                  ChatUtil.a("Макрос " + command + " был успешно удален с клавиши " + KeyUtil.a(macro.a()).b() + ".");
                  return 1;
               }
            }))
         )
         .then(this.a("list").executes(context5 -> {
            List<MacrosConstructor> macros = processor.a();
            if (macros.isEmpty()) {
               ChatUtil.a("Список макросов пуст.");
               return 1;
            } else {
               ChatUtil.a("Список макросов (" + macros.size() + "):");

               for (MacrosConstructor macro : macros) {
                  ChatUtil.a("  " + KeyUtil.a(macro.a()).b() + ": " + macro.b());
               }

               return 1;
            }
         }))
         .then(this.a("clear").executes(context6 -> {
            ChatUtil.a("Было успешно удалено макросов из списка: " + processor.a().size());
            processor.f();
            processor.unSetup();
            return 1;
         }))
         .executes(context7 -> {
            ChatUtil.a("Использование: .macros <add|remove|list|clear>");
            return 1;
         });
   }
}
