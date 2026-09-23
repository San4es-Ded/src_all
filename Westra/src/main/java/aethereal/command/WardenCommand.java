package aethereal.command;

import aethereal.core.Westra;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.class_2172;

@Command(
   a = "warden"
)
public class WardenCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      List<Integer> anarchies = Westra.h().d().t().aU().q();
      ((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)builder.then(
                     ((LiteralArgumentBuilder)this.a("add").executes(context -> {
                        ChatUtil.a("Использование: .warden add <анархия>");
                        return 1;
                     })).then(this.e("анархия").executes(context2 -> {
                        int anarchy = this.b(context2, "анархия");
                        if (anarchy < 1 || anarchy > 999) {
                           ChatUtil.a("Анархия должна быть от 1 до 999.");
                           return 1;
                        } else if (!anarchies.contains(anarchy)) {
                           if (anarchies.size() < 10) {
                              anarchies.add(anarchy);
                              ChatUtil.a("Анархия " + anarchy + " добавлена.");
                              return 1;
                           } else {
                              ChatUtil.a("Можно добавить максимум 10 анархий.");
                              return 1;
                           }
                        } else {
                           ChatUtil.a("Анархия " + anarchy + " уже в списке.");
                           return 1;
                        }
                     }))
                  ))
                  .then(((LiteralArgumentBuilder)this.a("remove").executes(context3 -> {
                     ChatUtil.a("Использование: .warden remove <анархия>");
                     return 1;
                  })).then(this.e("анархия").executes(context4 -> {
                     int anarchy = this.b(context4, "анархия");
                     if (!anarchies.remove(Integer.valueOf(anarchy))) {
                        ChatUtil.a("Анархия " + anarchy + " не найдена.");
                        return 1;
                     } else {
                        ChatUtil.a("Анархия " + anarchy + " удалена.");
                        return 1;
                     }
                  }))))
               .then(this.a("list").executes(context5 -> {
                  if (!anarchies.isEmpty()) {
                     ChatUtil.a("Анархии (" + anarchies.size() + "): " + anarchies);
                     return 1;
                  } else {
                     ChatUtil.a("Список анархий пуст.");
                     return 1;
                  }
               })))
            .then(this.a("clear").executes(context6 -> {
               anarchies.clear();
               ChatUtil.a("Список анархий очищен.");
               return 1;
            })))
         .executes(context7 -> {
            ChatUtil.a("Использование: .warden <add|remove|list|clear>");
            return 1;
         });
   }
}
