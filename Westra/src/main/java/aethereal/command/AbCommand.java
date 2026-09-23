package aethereal.command;

import aethereal.module.misc.AutoBuy;
import aethereal.util.ChatUtil;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.List;
import net.minecraft.class_2172;

@Command(
   a = "ab"
)
public class AbCommand extends BaseCommand {
   @Override
   public void a(LiteralArgumentBuilder<class_2172> builder) {
      builder.then(this.a("add").then(this.e("anarchy").executes(ctx -> {
         int anarchy = this.b(ctx, "anarchy");
         if (AutoBuy.getSwapAnarchies().contains(anarchy)) {
            ChatUtil.sendMessage("&7[AB] &cАнархия &f" + anarchy + "&c уже в списке");
            return 1;
         } else {
            AutoBuy.addSwapAnarchy(anarchy);
            ChatUtil.sendMessage("&7[AB] &aАнархия &f" + anarchy + "&a добавлена в свап (" + AutoBuy.getSwapAnarchies().size() + " шт.)");
            return 1;
         }
      })));
      builder.then(this.a("del").then(this.e("anarchy").executes(ctx -> {
         int anarchy = this.b(ctx, "anarchy");
         if (!AutoBuy.getSwapAnarchies().contains(anarchy)) {
            ChatUtil.sendMessage("&7[AB] &cАнархии &f" + anarchy + "&c нет в списке");
            return 1;
         } else {
            AutoBuy.delSwapAnarchy(anarchy);
            ChatUtil.sendMessage("&7[AB] &aАнархия &f" + anarchy + "&a удалена из свапа");
            return 1;
         }
      })));
      builder.then(this.a("clear").executes(ctx -> {
         AutoBuy.clearSwapAnarchies();
         ChatUtil.sendMessage("&7[AB] &aСписок анархий очищен");
         return 1;
      }));
      builder.then(this.a("list").executes(ctx -> {
         List<Integer> anarchyList = AutoBuy.getSwapAnarchies();
         if (anarchyList.isEmpty()) {
            ChatUtil.sendMessage("&7[AB] &cСписок пуст. Добавь: &f*ab add <номер анархии>");
            return 1;
         } else {
            StringBuilder sb = new StringBuilder("&7[AB] &fАнархии для свапа:");

            for (int anarchy : anarchyList) {
               sb.append(" &f").append(anarchy).append("&7,");
            }

            sb.setLength(sb.length() - 2);
            ChatUtil.sendMessage(sb.toString());
            return 1;
         }
      }));
   }
}
