package rockstar.client.internal.script;








import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.List;
import java.util.Locale;
import net.minecraft.text.ClickEvent;
import net.minecraft.text.HoverEvent;
import net.minecraft.text.MutableText;
import net.minecraft.text.Style;
import net.minecraft.text.Text;
import net.minecraft.text.TextColor;
import net.minecraft.text.ClickEvent.Action;
import net.minecraft.util.Formatting;
import pyrock.utility.render.ColorRGBA;

public class MacroCommandHandler {
   public final CommandNode internalMethod03009() {
      return CommandBuilder.internalMethod07482("macro", localValue0 -> localValue0.internalMethod05325("macros").internalMethod06148("commands.macro.description"))
         .internalMethod01539("action", localValue0 -> {
            localValue0.internalMethod04818("add", "remove", "delete", "list", "clear");
            localValue0.internalMethod07138("add", "remove", "list", "clear");
         })
         .internalMethod01539("arguments", localValue0 -> localValue0.internalMethod06921().internalMethod00125().internalMethod00776(OperationResult::internalMethod00116))
         .internalMethod00262(this::internalMethod00634)
         .internalMethod04146();
   }

   private void internalMethod00634(ParsedCommand localValue1) {
      String localValue2 = (String)localValue1.internalMethod02266().getFirst();
      List localValue3 = localValue1.internalMethod02266().size() > 1 ? (List)localValue1.internalMethod02266().get(1) : List.of();
      ChatMacroManager localValue4 = RockstarClient.getInstance().internalMethod05155();
      String localValue5 = localValue2.toLowerCase(Locale.ROOT);
      switch (localValue5) {
         case "add":
            this.internalMethod03812(localValue4, localValue3);
            break;
         case "remove":
         case "delete":
            this.internalMethod05370(localValue4, localValue3);
            break;
         case "list":
            this.internalMethod01730(localValue4);
            break;
         case "clear":
            this.internalMethod02377(localValue4);
      }
   }

   private void internalMethod03812(ChatMacroManager localValue1, List<String> localValue2) {
      if (localValue2.size() < 2) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.macro.error_arguments")));
      } else {
         MacroCommandHandler.InternalType0186 localValue3 = this.internalMethod04477(localValue2);
         if (localValue3 == null) {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.macro.error_key")));
         } else {
            localValue1.internalMethod01066(localValue3.internalMethod00986(), localValue3.internalMethod00284());
            RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
            ClientMessages.internalMethod01809(
               Text.of(
                  LanguageManager.internalMethod00160(
                     "commands.macro.added", localValue3.internalMethod00986(), TextUtils.internalMethod04982(localValue3.internalMethod00284())
                  )
               )
            );
         }
      }
   }

   private void internalMethod05370(ChatMacroManager localValue1, List<String> localValue2) {
      if (localValue2.isEmpty()) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.macro.error_arguments")));
      } else {
         MacroCommandHandler.InternalType0186 localValue3 = this.internalMethod04477(localValue2);
         boolean localValue4;
         if (localValue3 != null) {
            localValue4 = localValue1.internalMethod01067(localValue3.internalMethod00986(), localValue3.internalMethod00284());
            if (localValue4) {
               ClientMessages.internalMethod01809(
                  Text.of(
                     LanguageManager.internalMethod00160(
                        "commands.macro.removed_specific", localValue3.internalMethod00986(), TextUtils.internalMethod04982(localValue3.internalMethod00284())
                     )
                  )
               );
            }
         } else {
            String localValue5 = String.join(" ", localValue2);
            int localValue6 = KeybindParser.internalMethod06543(localValue5);
            if (localValue6 != -1) {
               localValue4 = localValue1.internalMethod03189(localValue6);
               if (localValue4) {
                  ClientMessages.internalMethod01809(
                     Text.of(LanguageManager.internalMethod00160("commands.macro.removed_key", TextUtils.internalMethod04982(localValue6)))
                  );
               }
            } else {
               localValue4 = localValue1.internalMethod04540(localValue5);
               if (localValue4) {
                  ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod00160("commands.macro.removed_command", localValue5)));
               }
            }
         }

         if (!localValue4) {
            ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.macro.not_found")));
         } else {
            RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
         }
      }
   }

   private void internalMethod01730(ChatMacroManager localValue1) {
      List localValue2 = localValue1.internalMethod06721();
      if (localValue2.isEmpty()) {
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.macro.list_empty")));
      } else {
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.macro.list_header")));
         int localValue3 = 1;

         for (ChatMacroEntry localValue5 : (Iterable<ChatMacroEntry>)(Iterable<?>)localValue2) {
            String localValue6 = localValue5.internalMethod07169();
            String localValue7 = ".macro remove " + localValue6;
            MutableText localValue8 = Text.literal("[" + localValue3++ + "] ")
               .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY)))
               .append(
                  Text.literal(localValue6 + " ")
                     .setStyle(
                        Style.EMPTY
                           .withColor(TextColor.fromRgb(new ColorRGBA(87.0F, 126.0F, 255.0F).getRGB()))
                           .withClickEvent(new ClickEvent.RunCommand(localValue7))
                           .withHoverEvent(new HoverEvent.ShowText(Text.literal(LanguageManager.internalMethod07214("macro.hover_delete"))))
                     )
               )
               .append(Text.literal("(" + TextUtils.internalMethod04982(localValue5.internalMethod03890()) + ")"))
               .setStyle(Style.EMPTY.withColor(TextColor.fromFormatting(Formatting.GRAY)));
            ClientMessages.internalMethod07664(localValue8);
         }
      }
   }

   private void internalMethod02377(ChatMacroManager localValue1) {
      if (localValue1.internalMethod06721().isEmpty()) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.macro.list_empty")));
      } else {
         localValue1.internalMethod05229();
         RockstarClient.getInstance().internalMethod03371().internalMethod05165("client");
         ClientMessages.internalMethod01809(Text.of(LanguageManager.internalMethod07214("commands.macro.cleared")));
      }
   }

   private MacroCommandHandler.InternalType0186 internalMethod04477(List<String> localValue1) {
      if (localValue1.isEmpty()) {
         return null;
      } else {
         int localValue2 = KeybindParser.internalMethod06543((String)localValue1.getFirst());
         if (localValue2 != -1 && localValue1.size() > 1) {
            String localValue3 = String.join(" ", localValue1.subList(1, localValue1.size())).trim();
            if (!localValue3.isEmpty()) {
               return new MacroCommandHandler.InternalType0186(localValue3, localValue2);
            }
         }

         int localValue5 = KeybindParser.internalMethod06543((String)localValue1.getLast());
         if (localValue5 != -1 && localValue1.size() > 1) {
            String localValue4 = String.join(" ", localValue1.subList(0, localValue1.size() - 1)).trim();
            if (!localValue4.isEmpty()) {
               return new MacroCommandHandler.InternalType0186(localValue4, localValue5);
            }
         }

         return null;
      }
   }

   static final class InternalType0186 {
      private final String internalField0248;
      private final int internalField0227;

      InternalType0186(String localValue1, int localValue2) {
         this.internalField0248 = localValue1;
         this.internalField0227 = localValue2;
      }

      @Override
      public final String toString() {
         return "InternalType0186[command=" + this.internalField0248 + ", keyCode=" + this.internalField0227 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         MacroCommandHandler.InternalType0186 other = (MacroCommandHandler.InternalType0186) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227);
      }

      public String internalMethod00986() {
         return this.internalField0248;
      }

      public int internalMethod00284() {
         return this.internalField0227;
      }
   }
}
