package wtf.wyvern.core.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.Iterator;
import net.minecraft.command.CommandSource;
import net.minecraft.util.Formatting;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.core.command.impl.args.CommandArgumentType;
import wtf.wyvern.core.command.impl.args.MacroArgumentType;
import wtf.wyvern.core.command.impl.args.MacroRemoveArgumentType;
import wtf.wyvern.core.macro.Macro;
import wtf.wyvern.utility.game.other.MessageUtil;
import wtf.wyvern.render.display.Keyboard;

public class MacroCommand extends CommandAbstract {
   public MacroCommand() {
      super("macro");
   }

   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      builder.then(literal("add").then(arg("bind", MacroArgumentType.create()).then(arg("text", CommandArgumentType.create()).executes((context) -> {
         String bind = (String)context.getArgument("bind", String.class);
         String text = (String)context.getArgument("text", String.class);
         String var10000;
         if (Keyboard.getKeyCode(bind) != -1) {
            var10000 = String.valueOf(Formatting.GRAY);
            MessageUtil.displayInfo(var10000 + "Для клавиши " + String.valueOf(Formatting.WHITE) + bind.toUpperCase() + String.valueOf(Formatting.GRAY) + " добавлен макрос с текстом " + String.valueOf(Formatting.WHITE) + text);
            Wyvern.getInstance().getMacroManager().add(new Macro(Keyboard.getKeyCode(bind), text));
         } else {
            var10000 = String.valueOf(Formatting.GRAY);
            MessageUtil.displayInfo(var10000 + "Клавиша " + String.valueOf(Formatting.WHITE) + bind.toUpperCase() + String.valueOf(Formatting.GRAY) + " не найдена");
         }

         return 1;
      }))));
      builder.then(literal("remove").then(arg("bind", MacroRemoveArgumentType.create()).executes((context) -> {
         String bind = (String)context.getArgument("bind", String.class);
         int keyCode = Keyboard.getKeyCode(bind);
         String var10000;
         if (keyCode == -1) {
            var10000 = String.valueOf(Formatting.GRAY);
            MessageUtil.displayInfo(var10000 + "Клавиша " + String.valueOf(Formatting.WHITE) + bind.toUpperCase() + String.valueOf(Formatting.GRAY) + " не найдена");
            return 1;
         }

         // removeIf avoids ConcurrentModificationException from mutating
         // the macro collection while streaming over it.
         boolean removed = Wyvern.getInstance().getMacroManager().getItems()
               .removeIf((macro) -> macro.getBind() == keyCode);
         if (removed) {
            var10000 = String.valueOf(Formatting.GRAY);
            MessageUtil.displayInfo(var10000 + "С клавиши " + String.valueOf(Formatting.WHITE) + bind.toUpperCase() + String.valueOf(Formatting.GRAY) + " удален макрос");
         } else {
            var10000 = String.valueOf(Formatting.GRAY);
            MessageUtil.displayInfo(var10000 + "Макроса привязанного к клавише " + String.valueOf(Formatting.WHITE) + bind + String.valueOf(Formatting.GRAY) + " не существует");
         }

         return 1;
      })));
      builder.then(literal("list").executes((commandContext) -> {
         StringBuilder stringBuilder = new StringBuilder();
         Iterator var2 = Wyvern.getInstance().getMacroManager().getItems().iterator();

         while(var2.hasNext()) {
            Macro macro = (Macro)var2.next();
            String var10001 = String.valueOf(Formatting.GRAY);
            stringBuilder.append("\n" + var10001 + macro.getText()).append(String.valueOf(Formatting.WHITE) + " [").append(Keyboard.getKeyName(macro.getBind())).append("]");
         }

         if (stringBuilder.isEmpty()) {
            MessageUtil.displayInfo(String.valueOf(Formatting.GRAY) + "Нема");
         } else {
            MessageUtil.displayInfo(stringBuilder);
         }

         return 1;
      }));
   }
}