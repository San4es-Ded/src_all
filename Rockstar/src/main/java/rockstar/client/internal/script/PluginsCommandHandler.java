package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.command.*;
import rockstar.client.*;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import net.minecraft.network.packet.c2s.play.RequestCommandCompletionsC2SPacket;
import net.minecraft.network.packet.s2c.play.CommandSuggestionsS2CPacket;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import pyrock.events.network.ReceivePacketEvent;

public class PluginsCommandHandler implements MinecraftClientAccess {
   private final Pattern internalField0293 = Pattern.compile("[A-Z0-9]\\w+");
   private final Stopwatch internalField0519 = new Stopwatch();
   private boolean internalField0277;
   private final EventListener<ReceivePacketEvent> internalField0157 = localValue1 -> {
      if (this.internalField0277) {
         if (this.internalField0519.internalMethod02365(10000L)) {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.plugins.error")));
            this.internalField0277 = false;
         } else {
            if (localValue1.getPacket() instanceof CommandSuggestionsS2CPacket localValue2) {
               Suggestions localValue5 = localValue2.getSuggestions();
               this.internalField0277 = false;
               if (localValue5.getList().isEmpty()) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.plugins.empty")));
                  return;
               }

               Set localValue4 = this.internalMethod05744(localValue5);
               if (localValue4.isEmpty()) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.plugins.client_error")));
                  return;
               }

               ClientMessages.internalMethod01809(
                  Text.of(
                     LanguageManager.internalMethod07214("commands.plugins.counts")
                        + " "
                        + localValue4.size()
                        + " "
                        + LanguageManager.internalMethod07214("commands.plugins.find")
                  )
               );
               ClientMessages.internalMethod01809(Text.of(String.join(", ", localValue4)));
            }
         }
      }
   };

   public PluginsCommandHandler() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   public CommandNode internalMethod00890() {
      return CommandBuilder.internalMethod07482(
            "plugins",
            localValue1 -> localValue1.internalMethod05325("plugin", "pl")
               .internalMethod06148("commands.plugins.description")
               .internalMethod01539("command", CommandParameterBuilder::internalMethod06921)
               .internalMethod00262(this::internalMethod05825)
         )
         .internalMethod04146();
   }

   private void internalMethod05825(ParsedCommand localValue1) {
      String localValue2 = "";
      if (!localValue1.internalMethod02266().isEmpty()) {
         localValue2 = (String)localValue1.internalMethod02266().getFirst();
      }

      String localValue3 = "/";
      if (localValue2 != null && !localValue2.isEmpty()) {
         localValue3 = localValue3 + localValue2;
      }

      this.internalField0519.internalMethod00701();
      this.internalField0277 = true;
      internalField0149.getNetworkHandler().sendPacket(new RequestCommandCompletionsC2SPacket(0, localValue3));
   }

   @NotNull
   private Set<String> internalMethod05744(Suggestions localValue1) {
      HashSet localValue2 = new HashSet();

      for (Suggestion localValue4 : localValue1.getList()) {
         String localValue5 = localValue4.getText();
         if (localValue5.contains(":")) {
            String[] localValue6 = localValue5.split(":");
            String localValue7 = localValue6[0].replaceAll("\\s*", "").replace("/", "");
            if (!localValue7.isEmpty() && !localValue7.equals("minecraft")) {
               localValue2.add(localValue7);
            }
         } else if (localValue5.matches(this.internalField0293.pattern()) && !localValue5.startsWith("/")) {
            localValue2.add(localValue5);
         }
      }

      return localValue2;
   }
}
