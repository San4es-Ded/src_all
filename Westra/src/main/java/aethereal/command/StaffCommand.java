package aethereal.command;

import aethereal.core.Westra;
import aethereal.staff.StaffConstructor;
import aethereal.staff.StaffProcessor;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.class_2172;

@Command(
   a = "staff"
)
public class StaffCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      StaffProcessor processor = Westra.h().d().f();
      LiteralArgumentBuilder literalArgumentBuilderThen = (LiteralArgumentBuilder)builder.then(((LiteralArgumentBuilder)this.a("add").executes(context -> {
         ChatUtil.a("Использование: .staff add <ник>");
         return 1;
      })).then(this.b("ник").suggests(this.a()).executes(context2 -> {
         String name = this.a(context2, "ник");
         if (processor.d(name)) {
            ChatUtil.a("Стафф " + name + " уже находится в списке стаффа.");
            return 1;
         } else {
            processor.b(name);
            processor.unSetup();
            ChatUtil.a("Стафф " + name + " был успешно добавлен в список стаффа.");
            return 1;
         }
      })));
      LiteralArgumentBuilder literalArgumentBuilderExecutes = (LiteralArgumentBuilder)this.a("remove").executes(context3 -> {
         ChatUtil.a("Использование: .staff remove <ник>");
         return 1;
      });
      RequiredArgumentBuilder<class_2172, String> requiredArgumentBuilderB = this.b("ник");
      literalArgumentBuilderThen.then(
            literalArgumentBuilderExecutes.then(requiredArgumentBuilderB.suggests(this.a(processor::a, v0 -> v0.a())).executes(context4 -> {
               String name = this.a(context4, "ник");
               if (!processor.d(name)) {
                  ChatUtil.a("Стафф " + name + " не найден в списке стаффа.");
                  return 1;
               } else {
                  processor.c(name);
                  processor.unSetup();
                  ChatUtil.a("Стафф " + name + " был успешно удален из списка стаффа.");
                  return 1;
               }
            }))
         )
         .then(this.a("list").executes(context5 -> {
            if (processor.a().isEmpty()) {
               ChatUtil.a("Список стаффа пуст.");
               return 1;
            } else {
               ChatUtil.a("Список стаффа (" + processor.a().size() + "):");

               for (StaffConstructor staffConstructor : processor.a()) {
                  ChatUtil.a("  - " + staffConstructor.a());
               }

               return 1;
            }
         }))
         .then(this.a("clear").executes(context6 -> {
            ChatUtil.a("Было успешно удалено стаффа из списка: " + processor.a().size());
            processor.f();
            processor.unSetup();
            return 1;
         }))
         .executes(context7 -> {
            ChatUtil.a("Использование: .staff <add|remove|list|clear>");
            return 1;
         });
   }
}
