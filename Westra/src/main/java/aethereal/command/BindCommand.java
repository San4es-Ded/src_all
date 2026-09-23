package aethereal.command;

import aethereal.config.ModuleProcessor;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.setting.BindSetting;
import aethereal.util.ChatUtil;
import aethereal.util.KeyUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import java.util.List;
import java.util.stream.Collectors;
import net.minecraft.class_2172;

@Command(
   a = "bind"
)
public class BindCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ModuleProcessor processor = Westra.h().d().t();
      LiteralArgumentBuilder literalArgumentBuilderExecutes = (LiteralArgumentBuilder)this.a("add").executes(context -> {
         ChatUtil.a("Использование: .bind add <название модуля> <клавиша>");
         return 1;
      });
      RequiredArgumentBuilder<class_2172, String> requiredArgumentBuilderC = this.c("аргументы");
      ((LiteralArgumentBuilder)builder.then(
            literalArgumentBuilderExecutes.then(requiredArgumentBuilderC.suggests(this.a(processor::e, v0 -> v0.j())).executes(context2 -> {
               String args = this.a(context2, "аргументы");
               if (args != null && !args.trim().isEmpty()) {
                  String[] parts = args.trim().split("\\s+");
                  if (parts.length < 2) {
                     ChatUtil.a("Использование: .bind add <название модуля> <клавиша>");
                     return 1;
                  } else {
                     String keyName = parts[parts.length - 1];
                     String moduleName = args.substring(0, args.length() - keyName.length()).trim();
                     KeyUtil key = KeyUtil.a(keyName);
                     if (key == KeyUtil.UNKNOWN) {
                        ChatUtil.a("Клавиша " + keyName + " не найдена.");
                        return 1;
                     } else {
                        Module module = processor.e().stream().filter(m -> m.j().equalsIgnoreCase(moduleName)).findFirst().orElse(null);
                        if (module == null) {
                           ChatUtil.a("Модуль " + moduleName + " не найден.");
                           return 1;
                        } else {
                           module.a(key.a());
                           processor.unSetup();
                           ChatUtil.a("Модуль " + moduleName + " был успешно привязан к клавише " + key.b() + ".");
                           return 1;
                        }
                     }
                  }
               } else {
                  ChatUtil.a("Использование: .bind add <название модуля> <клавиша>");
                  return 1;
               }
            }))
         ))
         .then(
            this.a("list")
               .executes(
                  context3 -> {
                     List<Module> modules = processor.e()
                        .stream()
                        .filter(module -> module.p() != -1 || module.e().stream().anyMatch(setting -> setting instanceof BindSetting bind && bind.c() != -1))
                        .toList();
                     if (modules.isEmpty()) {
                        ChatUtil.a("Список модулей с привязанными клавишами пуст.");
                        return 1;
                     } else {
                        ChatUtil.a("Список модулей с привязанными клавишами (" + modules.size() + "):");
                        modules.forEach(
                           module2 -> {
                              String binds = module2.e()
                                 .stream()
                                 .filter(setting -> setting instanceof BindSetting bind && bind.c() != -1)
                                 .map(setting2 -> setting2.i() + " &8→&7 " + KeyUtil.a(((BindSetting)setting2).c()).b())
                                 .collect(Collectors.joining(", "));
                              ChatUtil.a(
                                 " &c"
                                    + module2.j()
                                    + (module2.p() != -1 ? " &8→&7 " + KeyUtil.a(module2.p()).b() : "")
                                    + (binds.isEmpty() ? "" : " &8[&7" + binds + "&8]")
                              );
                           }
                        );
                        return 1;
                     }
                  }
               )
         )
         .then(this.a("clear").executes(context4 -> {
            for (Module module : processor.e()) {
               module.a(-1);
            }

            processor.unSetup();
            ChatUtil.a("Привязанные клавиши были успешно очищены у всех модулей.");
            return 1;
         }))
         .then(((LiteralArgumentBuilder)this.a("remove").executes(context5 -> {
            ChatUtil.a("Использование: .bind remove <клавиша>");
            return 1;
         })).then(this.d("клавиша").suggests(this.b()).executes(context6 -> {
            String name = this.a(context6, "клавиша");
            KeyUtil key = KeyUtil.a(name);
            if (key == KeyUtil.UNKNOWN) {
               ChatUtil.a("Клавиша " + name + " не найдена.");
               return 1;
            } else {
               List<Module> modules = processor.e().stream().filter(module -> module.p() == key.a()).toList();
               if (modules.isEmpty()) {
                  ChatUtil.a("Нет модулей, привязанных к клавише " + key.b() + ".");
                  return 1;
               } else {
                  modules.forEach(module2 -> module2.a(-1));
                  processor.unSetup();
                  ChatUtil.a("Клавиша " + key.b() + " была успешно удалена из " + modules.size() + " модулей.");
                  return 1;
               }
            }
         })))
         .executes(context7 -> {
            ChatUtil.a("Использование: .bind <add|list|clear|remove>");
            return 1;
         });
   }
}
