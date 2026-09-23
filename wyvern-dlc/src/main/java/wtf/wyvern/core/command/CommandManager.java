package wtf.wyvern.core.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import lombok.Generated;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommandSource;
import net.minecraft.command.CommandSource;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.core.command.impl.ClipCommand;
import wtf.wyvern.core.command.impl.BindCommand;
import wtf.wyvern.core.command.impl.ConfigCommand;
import wtf.wyvern.core.command.impl.FriendCommand;
import wtf.wyvern.core.command.impl.GPSCommand;
import wtf.wyvern.core.command.impl.HelpCommand;
import wtf.wyvern.core.command.impl.MacroCommand;
import wtf.wyvern.core.command.impl.PanicCommand;
import wtf.wyvern.core.command.impl.TargetCommand;
import wtf.wyvern.core.command.impl.RCTCommand;
import wtf.wyvern.utility.game.other.MessageUtil;

public class CommandManager {
   private String prefix = ".";
   private final CommandDispatcher<CommandSource> dispatcher = new CommandDispatcher();
   private final List<CommandAbstract> commands = new ArrayList();

   public CommandManager() {
      this.register();
   }

   private void register() {
      this.registerCommand(new FriendCommand());
      this.registerCommand(new MacroCommand());
      this.registerCommand(new ClipCommand());
      this.registerCommand(new ConfigCommand());
      this.registerCommand(new RCTCommand());
      this.registerCommand(new GPSCommand());
      this.registerCommand(new BindCommand());
      this.registerCommand(new PanicCommand());
      this.registerCommand(new HelpCommand());
      this.registerCommand(new TargetCommand());
   }

   public void registerCommand(CommandAbstract command) {
      if (command != null) {
         command.register(this.dispatcher);
         this.commands.add(command);
      }
   }

   @Generated
   public String getPrefix() {
      return this.prefix;
   }

   @Generated
   public CommandDispatcher<CommandSource> getDispatcher() {
      return this.dispatcher;
   }

   public CommandSource getSource() {
      MinecraftClient client = MinecraftClient.getInstance();
      return new ClientCommandSource(client.getNetworkHandler(), client);
   }

   public void execute(String input) {
      String command = input == null ? "" : input.trim();
      if (command.isEmpty()) {
         MessageUtil.displayError("Введите команду после " + this.prefix);
         return;
      }
      int separator = command.indexOf(' ');
      String root = separator < 0 ? command : command.substring(0, separator);
      command = root.toLowerCase(Locale.ROOT) + (separator < 0 ? "" : command.substring(separator));
      try {
         this.dispatcher.execute(command, this.getSource());
      } catch (CommandSyntaxException exception) {
         MessageUtil.displayError(exception.getMessage());
      } catch (RuntimeException exception) {
         String message = exception.getMessage();
         MessageUtil.displayError(message == null || message.isBlank()
                 ? "Ошибка выполнения команды"
                 : message);
      }
   }

   @Generated
   public List<CommandAbstract> getCommands() {
      return this.commands;
   }
}