package aethereal.command;

import aethereal.core.Westra;
import aethereal.friend.FriendConstructor;
import aethereal.friend.FriendProcessor;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.minecraft.class_2172;

@Command(
   a = "friend"
)
public class FriendCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      FriendProcessor processor = Westra.h().d().e();
      LiteralArgumentBuilder literalArgumentBuilderThen = (LiteralArgumentBuilder)builder.then(((LiteralArgumentBuilder)this.a("add").executes(context -> {
         ChatUtil.a("Использование: .friend add <ник>");
         return 1;
      })).then(this.b("ник").suggests(this.a()).executes(context2 -> {
         String name = this.a(context2, "ник");
         if (processor.d(name)) {
            ChatUtil.a("Друг " + name + " уже находится в списке друзей.");
            return 1;
         } else {
            processor.b(name);
            processor.unSetup();
            ChatUtil.a("Друг " + name + " был успешно добавлен в список друзей.");
            return 1;
         }
      })));
      LiteralArgumentBuilder literalArgumentBuilderExecutes = (LiteralArgumentBuilder)this.a("remove").executes(context3 -> {
         ChatUtil.a("Использование: .friend remove <ник>");
         return 1;
      });
      RequiredArgumentBuilder<class_2172, String> requiredArgumentBuilderB = this.b("ник");
      literalArgumentBuilderThen.then(
            literalArgumentBuilderExecutes.then(requiredArgumentBuilderB.suggests(this.a(processor::a, v0 -> v0.a())).executes(context4 -> {
               String name = this.a(context4, "ник");
               if (!processor.d(name)) {
                  ChatUtil.a("Друг " + name + " не найден в списке друзей.");
                  return 1;
               } else {
                  processor.c(name);
                  processor.unSetup();
                  ChatUtil.a("Друг " + name + " был успешно удален из списка друзей.");
                  return 1;
               }
            }))
         )
         .then(this.a("list").executes(context5 -> {
            if (processor.a().isEmpty()) {
               ChatUtil.a("Список друзей пуст.");
               return 1;
            } else {
               ChatUtil.a("Список друзей (" + processor.a().size() + "):");

               for (FriendConstructor friend : processor.a()) {
                  ChatUtil.a("  - " + friend.a());
               }

               return 1;
            }
         }))
         .then(this.a("clear").executes(context6 -> {
            ChatUtil.a("Было успешно удалено друзей из списка: " + processor.a().size());
            processor.f();
            processor.unSetup();
            return 1;
         }))
         .executes(context7 -> {
            ChatUtil.a("Использование: .friend <add|remove|list|clear>");
            return 1;
         });
   }
}
