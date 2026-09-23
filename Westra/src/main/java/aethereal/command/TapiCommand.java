package aethereal.command;

import aethereal.telegram.TelegramBot;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.class_2172;

@Command(
   a = "tapi"
)
public class TapiCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      builder.then(this.a("add").then(this.d("name").then(this.d("token").then(this.d("chatId").executes(ctx -> {
         String name = this.a(ctx, "name");
         String token = this.a(ctx, "token");
         String chatId = this.a(ctx, "chatId");
         if (TelegramBot.hasBot(name)) {
            ChatUtil.sendMessage("&7[Tapi] &cБот &f" + name + "&c уже существует");
            return 1;
         } else {
            TelegramBot.addBot(name, token, chatId);
            ChatUtil.sendMessage("&7[Tapi] &aБот &f" + name + "&a добавлен. Подключить: &f*tapi start " + name);
            return 1;
         }
      })))));
      builder.then(this.a("list").executes(ctx -> {
         List<String> bots = TelegramBot.listBots();
         if (bots.isEmpty()) {
            ChatUtil.sendMessage("&7[Tapi] &cБотов нет. Добавь: &f*tapi add <имя> <апи-ключ> <тг-id>");
            return 1;
         } else {
            StringBuilder sb = new StringBuilder("&7[Tapi] &fБоты:");

            for (String bot : bots) {
               sb.append("\n&7 - &f").append(bot);
               if (bot.equals(TelegramBot.activeName())) {
                  sb.append(" &a[активен]");
               }
            }

            ChatUtil.sendMessage(sb.toString());
            return 1;
         }
      }));
      builder.then(this.a("start").then(this.d("name").executes(ctx -> {
         String name = this.a(ctx, "name");
         if (!TelegramBot.hasBot(name)) {
            ChatUtil.sendMessage("&7[Tapi] &cБот &f" + name + "&c не найден");
            return 1;
         } else {
            TelegramBot.startBot(name);
            ChatUtil.sendMessage("&7[Tapi] &aБот &f" + name + "&a подключён. Команды в ТГ: &f/warden, /autobuy, /module list, /screenshot");
            return 1;
         }
      })));
      builder.then(this.a("del").then(this.d("name").executes(ctx -> {
         String name = this.a(ctx, "name");
         if (!TelegramBot.hasBot(name)) {
            ChatUtil.sendMessage("&7[Tapi] &cБот &f" + name + "&c не найден");
            return 1;
         } else {
            TelegramBot.delBot(name);
            ChatUtil.sendMessage("&7[Tapi] &aБот &f" + name + "&a удалён");
            return 1;
         }
      })));
   }
}
