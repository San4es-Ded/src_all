package aethereal.command;

import aethereal.config.ModuleProcessor;
import aethereal.core.Module;
import aethereal.core.Westra;
import aethereal.setting.Setting;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.suggestion.SuggestionProvider;
import java.io.File;
import java.util.Arrays;
import java.util.stream.Stream;
import net.minecraft.class_156;
import net.minecraft.class_2172;

@Command(
   a = "cfg"
)
public class ConfigCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      ModuleProcessor processor = Westra.h().d().t();
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(
                           ((LiteralArgumentBuilder)this.a("save").executes(context -> {
                              ChatUtil.a("Использование: .cfg save <имя>");
                              return 1;
                           })).then(this.b("имя").executes(context2 -> {
                              String configName = this.a(context2, "имя");
                              processor.b(configName);
                              ChatUtil.a("Конфиг " + configName + " был успешно сохранен.");
                              return 1;
                           }))
                        ))
                        .then(((LiteralArgumentBuilder)this.a("load").executes(context3 -> {
                           ChatUtil.a("Использование: .cfg load <имя>");
                           return 1;
                        })).then(this.b("имя").suggests(this.c()).executes(context4 -> {
                           String configName = this.a(context4, "имя");
                           if (processor.c(configName)) {
                              ChatUtil.a("Конфиг " + configName + " был успешно загружен.");
                              return 1;
                           } else {
                              ChatUtil.a("Конфиг " + configName + " не найден.");
                              return 1;
                           }
                        }))))
                     .then(this.a("list").executes(context5 -> {
                        File configDir = processor.d();
                        File[] files = configDir.listFiles((dir, name) -> name.endsWith(".westra") || name.endsWith(".json"));
                        if (files != null && files.length != 0) {
                           ChatUtil.a("Список конфигов (" + files.length + "):");

                           for (File file : files) {
                              ChatUtil.a("  - " + file.getName());
                           }

                           return 1;
                        } else {
                           ChatUtil.a("Список конфигов пуст.");
                           return 1;
                        }
                     })))
                  .then(this.a("reset").executes(context6 -> {
                     for (Module module : processor.e()) {
                        module.a(false);
                        module.a(-1);

                        for (Setting<?> setting : module.e()) {
                           if (setting.g() != null) {
                              resetSettingValue(setting);
                           }
                        }
                     }

                     ChatUtil.a("Все модули были сброшены в состояние по умолчанию.");
                     return 1;
                  })))
               .then(((LiteralArgumentBuilder)this.a("remove").executes(context7 -> {
                  ChatUtil.a("Использование: .cfg remove <имя>");
                  return 1;
               })).then(this.b("имя").suggests(this.c()).executes(context8 -> {
                  String configName = this.a(context8, "имя");
                  if (processor.d(configName)) {
                     ChatUtil.a("Конфиг " + configName + " был успешно удален.");
                     return 1;
                  } else {
                     ChatUtil.a("Конфиг " + configName + " не найден.");
                     return 1;
                  }
               }))))
            .then(this.a("dir").executes(context9 -> {
               class_156.method_668().method_672(processor.d());
               return 1;
            })))
         .executes(context10 -> {
            ChatUtil.a("Использование: .cfg <load|save|list|reset|remove|dir>");
            return 1;
         });
   }

   private SuggestionProvider<class_2172> c() {
      return (context, builder) -> {
         ModuleProcessor processor = Westra.h().d().t();
         File configDir = processor.d();
         File[] files;
         if (configDir.exists() && (files = configDir.listFiles((dir, name) -> name.endsWith(".westra") || name.endsWith(".json"))) != null) {
            Stream map = Arrays.stream(files).map(v0 -> v0.getName()).map(name2 -> {
               String ext = ".westra";
               if (name2.endsWith(ext)) {
                  return name2.substring(0, name2.length() - ext.length());
               } else {
                  return name2.endsWith(".json") ? name2.substring(0, name2.length() - 5) : name2;
               }
            });
            map.forEach(s -> builder.suggest((String)s));
         }

         return builder.buildFuture();
      };
   }

   private static <T> void resetSettingValue(Setting<?> setting) {
      ((Setting<Object>)setting).a(setting.g());
   }
}
