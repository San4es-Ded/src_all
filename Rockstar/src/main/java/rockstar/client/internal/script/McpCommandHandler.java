package rockstar.client.internal.script;






import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.command.*;
import rockstar.client.internal.network.*;
import rockstar.client.*;
import java.util.Locale;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;

public final class McpCommandHandler {
   public CommandNode internalMethod05701() {
      return CommandBuilder.internalMethod07482("mcp", localValue1 -> localValue1.internalMethod06148("commands.mcp.description").internalMethod01539("action", localValue0 -> {
         localValue0.internalMethod06921();
         localValue0.internalMethod04818("status", "url", "token", "restart", "stop", "start");
         localValue0.internalMethod07138("status", "url", "token", "restart", "stop", "start");
      }).internalMethod00262(this::internalMethod00831)).internalMethod04146();
   }

   private void internalMethod00831(ParsedCommand localValue1) {
      String localValue2 = !localValue1.internalMethod02266().isEmpty() && localValue1.internalMethod02266().getFirst() != null
         ? String.valueOf(localValue1.internalMethod02266().getFirst()).toLowerCase(Locale.ROOT)
         : "status";
      McpHttpServer localValue3 = McpHttpServer.internalMethod07531();
      switch (localValue2) {
         case "stop":
            localValue3.internalMethod08318();
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.mcp.stopped")));
            break;
         case "start":
         case "restart":
            try {
               localValue3.internalMethod08320();
               this.internalMethod05167(localValue3);
            } catch (Exception localValue7) {
               ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod00160("commands.mcp.error", String.valueOf(localValue7.getMessage()))));
            }
            break;
         case "token":
            String localValue6 = localValue3.internalMethod08162();
            ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.mcp.token_rotated")));
            this.internalMethod02607(LanguageManager.internalMethod07214("commands.mcp.token"), localValue6);
            break;
         case "url":
            this.internalMethod02607(LanguageManager.internalMethod07214("commands.mcp.url"), localValue3.internalMethod07430());
            break;
         default:
            this.internalMethod05167(localValue3);
      }
   }

   private void internalMethod05167(McpHttpServer localValue1) {
      if (!localValue1.internalMethod01545()) {
         ClientMessages.internalMethod03058(Text.of(LanguageManager.internalMethod07214("commands.mcp.offline")));
      } else {
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.mcp.online", localValue1.internalMethod01543())));
         this.internalMethod02607(LanguageManager.internalMethod07214("commands.mcp.url"), localValue1.internalMethod07430());
         this.internalMethod02607(LanguageManager.internalMethod07214("commands.mcp.token"), localValue1.internalMethod02728());
         ClientMessages.internalMethod01809(
            Text.of(LanguageManager.internalMethod00160("commands.mcp.file", McpHttpServer.internalMethod03768().getAbsolutePath()))
         );
      }
   }

   private void internalMethod02607(String localValue1, String localValue2) {
      ClientMessages.internalMethod07664(
         Text.literal(localValue1 + ": ")
            .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY)))
            .append(
               Text.literal(localValue2)
                  .setStyle(
                     Style.EMPTY
                        .withColor(TextColor.fromFormatting(Formatting.AQUA))
                        .withClickEvent(new ClickEvent.CopyToClipboard(localValue2))
                        .withHoverEvent(new HoverEvent.ShowText(Text.literal(LanguageManager.internalMethod07214("commands.mcp.copy"))))
                  )
            )
      );
   }
}
