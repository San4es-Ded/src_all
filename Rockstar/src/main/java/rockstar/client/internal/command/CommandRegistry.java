package rockstar.client.internal.command;









import rockstar.client.util.*;
import rockstar.client.i18n.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.network.*;
import rockstar.client.internal.auth.*;
import rockstar.client.*;
import com.mojang.brigadier.context.StringRange;
import com.mojang.brigadier.suggestion.Suggestion;
import com.mojang.brigadier.suggestion.Suggestions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import lombok.Generated;
import net.minecraft.text.Text;
import rockstar.profile.Profile;
import rockstar.profile.Role;

public class CommandRegistry {
   private final List<CommandNode> internalField0416 = new ArrayList<>();
   private String internalField0248 = ".";

   public final void internalMethod01107(CommandNode localValue1) {
      this.internalField0416.add(localValue1);
   }

   public final boolean internalMethod01108(CommandNode localValue1) {
      return this.internalField0416.remove(localValue1);
   }

   public final void internalMethod03368() {
      this.internalMethod01107(new CatImageCommand().internalMethod01374());
      this.internalMethod01107(new AutopilotCommandHandler().internalMethod02061());
      this.internalMethod01107(new ConfigCommandHandler().internalMethod06280());
      this.internalMethod01107(new FakePlayerCommandHandler().internalMethod06472());
      this.internalMethod01107(new FriendCommandHandler().internalMethod07227());
      this.internalMethod01107(new HelpCommandHandler().internalMethod04111());
      this.internalMethod01107(new InventorySaveCommandHandler().internalMethod04314());
      this.internalMethod01107(new ExchangeCommandHandler().internalMethod01559());
      this.internalMethod01107(new PrefixCommand().internalMethod05847());
      this.internalMethod01107(new ServerReconnectManager().internalMethod06043());
      this.internalMethod01107(new TargetCommand().internalMethod05187());
      this.internalMethod01107(new VClipCommand().internalMethod05383());
      this.internalMethod01107(new MacroCommandHandler().internalMethod03009());
      this.internalMethod01107(new AutoAuthCommand().internalMethod01341());
      this.internalMethod01107(new InvseeCommandHandler().internalMethod07426());
      this.internalMethod01107(new ToggleCommand().internalMethod00353());
      this.internalMethod01107(new PluginsCommandHandler().internalMethod00890());
      this.internalMethod01107(new WaypointCommandHandler().internalMethod06075());
      this.internalMethod01107(new BindCommandHandler().internalMethod02253());
      this.internalMethod01107(new BaseFinderCommandHandler().internalMethod07005());
      this.internalMethod01107(new PlayerParseCommandHandler().internalMethod00083());
      this.internalMethod01107(new PythonScriptCommand().internalMethod01089());
      this.internalMethod01107(new BotCommandHandler().internalMethod07197());
      this.internalMethod01107(new StaffCommand().internalMethod00113());
      this.internalMethod01107(new NeuroCommandHandler().internalMethod03205());
      this.internalMethod01107(new NewtonCommand().internalMethod03956());
      this.internalMethod01107(new McpCommandHandler().internalMethod05701());
   }

   public final List<CommandNode> internalMethod05967() {
      return Collections.unmodifiableList(this.internalField0416);
   }

   public final boolean internalMethod01800(CommandNode localValue1) {
      List localValue2 = localValue1.internalMethod08474();
      if (localValue2 != null && !localValue2.isEmpty()) {
         Role localValue3 = Profile.getRole();

         for (Role localValue5 : (Iterable<Role>)(Iterable<?>)localValue2) {
            if (localValue5 == localValue3) {
               return true;
            }
         }

         return false;
      } else {
         return true;
      }
   }

   public final boolean internalMethod04610(String localValue1) {
      if (localValue1.startsWith(this.internalField0248) && !RockstarClient.internalField0240.internalMethod06896()) {
         String localValue2 = localValue1.substring(this.internalField0248.length()).trim();
         if (localValue2.isEmpty()) {
            return true;
         } else {
            String[] localValue3 = internalMethod01327(localValue2);
            if (localValue3.length == 0) {
               return true;
            } else {
               List localValue4 = Arrays.asList(localValue3);
               CommandRegistry.InternalType0373 localValue5 = this.internalMethod06398(localValue4, null, 0, true);
               if (localValue5 == null) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.unknown")));
                  return false;
               } else if (!localValue5.internalMethod02835()) {
                  ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.no_access")));
                  return false;
               } else {
                  CommandNode localValue6 = localValue5.internalMethod04976();
                  int localValue7 = localValue5.internalMethod02834();
                  if (!localValue6.internalMethod03507()) {
                     ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.not_executable")));
                     return false;
                  } else {
                     List localValue8 = this.internalMethod06342(localValue6, localValue3, localValue7);
                     if (localValue8 == null) {
                        return true;
                     } else {
                        localValue6.internalMethod00835().execute(new ParsedCommand(localValue6, localValue8));
                        return true;
                     }
                  }
               }
            }
         }
      } else {
         return false;
      }
   }

   private CommandRegistry.InternalType0373 internalMethod06398(List<String> localValue1, CommandNode localValue2, int localValue3, boolean localValue4) {
      List localValue5 = localValue2 == null ? this.internalField0416 : localValue2.internalMethod08937();
      if (localValue3 >= localValue1.size()) {
         return this.internalMethod06819(localValue2, localValue3 - 1, localValue4);
      } else {
         String localValue6 = (String)localValue1.get(localValue3);

         for (CommandNode localValue8 : (Iterable<CommandNode>)(Iterable<?>)localValue5) {
            for (String localValue10 : localValue8.internalMethod03764()) {
               if (localValue10.equalsIgnoreCase(localValue6)) {
                  boolean localValue11 = localValue4 && this.internalMethod01800(localValue8);
                  CommandRegistry.InternalType0373 localValue12 = this.internalMethod06398(localValue1, localValue8, localValue3 + 1, localValue11);
                  if (localValue12 != null) {
                     return localValue12;
                  }

                  return new CommandRegistry.InternalType0373(localValue8, localValue3, localValue11);
               }
            }
         }

         return this.internalMethod06819(localValue2, localValue3 - 1, localValue4);
      }
   }

   private CommandRegistry.InternalType0373 internalMethod06819(CommandNode localValue1, int localValue2, boolean localValue3) {
      return localValue1 != null ? new CommandRegistry.InternalType0373(localValue1, localValue2, localValue3) : null;
   }

   private List<Object> internalMethod06342(CommandNode localValue1, String[] localValue2, int localValue3) {
      List localValue4 = localValue1.internalMethod01430();
      ArrayList localValue5 = new ArrayList();
      int localValue6 = localValue3 + 1;
      int localValue7 = localValue2.length;

      for (CommandParameter localValue9 : (Iterable<CommandParameter>)(Iterable<?>)localValue4) {
         if (localValue9.internalMethod06683()) {
            ArrayList localValue13 = new ArrayList();

            for (int localValue11 = localValue6; localValue11 < localValue7; localValue11++) {
               OperationResult localValue12 = localValue9.internalMethod05200().validate(localValue2[localValue11]);
               if (localValue12 instanceof OperationResult.InternalType0447) {
                  return null;
               }

               localValue13.add(((OperationResult.InternalType0448)localValue12).internalMethod06555());
            }

            if (localValue9.internalMethod06679() && localValue13.isEmpty()) {
               OperationResult.internalMethod05941("Missing value for argument '" + localValue9.internalMethod01708() + "'");
               return null;
            }

            localValue5.add(localValue13);
            return localValue5;
         }

         if (localValue6 >= localValue7) {
            if (localValue9.internalMethod06679()) {
               OperationResult.internalMethod05941("Missing value for argument '" + localValue9.internalMethod01708() + "'");
               return null;
            }

            localValue5.add(null);
         } else {
            OperationResult localValue10 = localValue9.internalMethod05200().validate(localValue2[localValue6]);
            if (localValue10 instanceof OperationResult.InternalType0447) {
               return null;
            }

            localValue5.add(((OperationResult.InternalType0448)localValue10).internalMethod06555());
            localValue6++;
         }
      }

      if (localValue6 < localValue7) {
         ClientMessages.internalMethod09025(Text.of(LanguageManager.internalMethod07214("commands.too_many_args")));
         return null;
      } else {
         return localValue5;
      }
   }

   private static String[] internalMethod01327(String localValue0) {
      List localValue1 = internalMethod03914(localValue0, 0);
      String[] localValue2 = new String[localValue1.size()];

      for (int localValue3 = 0; localValue3 < localValue2.length; localValue3++) {
         localValue2[localValue3] = ((CommandRegistry.InternalType0374)localValue1.get(localValue3)).internalMethod00839();
      }

      return localValue2;
   }

   private static List<CommandRegistry.InternalType0374> internalMethod03914(String localValue0, int localValue1) {
      ArrayList localValue2 = new ArrayList();
      StringBuilder localValue3 = new StringBuilder();
      int localValue4 = -1;
      boolean localValue5 = false;

      for (int localValue6 = 0; localValue6 < localValue0.length(); localValue6++) {
         char localValue7 = localValue0.charAt(localValue6);
         if (localValue5) {
            if (localValue7 == '\\' && localValue6 + 1 < localValue0.length() && localValue0.charAt(localValue6 + 1) == '"') {
               localValue3.append('"');
               localValue6++;
            } else if (localValue7 == '"') {
               localValue5 = false;
            } else {
               localValue3.append(localValue7);
            }
         } else if (localValue7 == '"') {
            localValue5 = true;
            if (localValue4 < 0) {
               localValue4 = localValue6;
            }
         } else if (Character.isWhitespace(localValue7)) {
            if (localValue4 >= 0) {
               localValue2.add(new CommandRegistry.InternalType0374(localValue3.toString(), localValue1 + localValue4, localValue1 + localValue6));
               localValue3.setLength(0);
               localValue4 = -1;
            }
         } else {
            localValue3.append(localValue7);
            if (localValue4 < 0) {
               localValue4 = localValue6;
            }
         }
      }

      if (localValue4 >= 0) {
         localValue2.add(new CommandRegistry.InternalType0374(localValue3.toString(), localValue1 + localValue4, localValue1 + localValue0.length()));
      }

      return localValue2;
   }

   public final CompletableFuture<Suggestions> internalMethod05798(String localValue1, int localValue2) {
      if (localValue1.startsWith(this.internalField0248) && localValue2 >= this.internalField0248.length() && !RockstarClient.internalField0240.internalMethod06896()) {
         String localValue3 = localValue1.substring(0, Math.min(localValue2, localValue1.length()));
         List localValue4 = internalMethod03914(localValue3.substring(this.internalField0248.length()), this.internalField0248.length());
         CommandRegistry.InternalType0374 localValue5 = !localValue4.isEmpty() && ((CommandRegistry.InternalType0374)localValue4.getLast()).internalMethod04647() >= localValue3.length()
            ? (CommandRegistry.InternalType0374)localValue4.getLast()
            : null;
         boolean localValue6 = localValue5 == null;
         List localValue7 = this.internalField0416;
         CommandNode localValue8 = null;
         int localValue9 = 0;

         for (int localValue10 = 0; localValue10 < localValue4.size(); localValue10++) {
            CommandNode localValue11 = this.internalMethod00524(localValue7, ((CommandRegistry.InternalType0374)localValue4.get(localValue10)).internalMethod00839());
            if (localValue11 == null) {
               break;
            }

            localValue8 = localValue11;
            localValue9 = localValue10 + 1;
            localValue7 = localValue11.internalMethod08937();
            if (localValue7.isEmpty()) {
               break;
            }
         }

         int localValue21 = localValue4.size() - localValue9;
         String localValue22 = localValue5 != null ? localValue5.internalMethod00839() : "";
         int localValue12 = localValue5 != null ? localValue5.internalMethod04645() : localValue2;
         StringRange localValue13 = StringRange.between(localValue12, localValue2);
         ArrayList localValue14 = new ArrayList();
         if (localValue8 == null) {
            String localValue15 = localValue22.toLowerCase();

            for (CommandNode localValue17 : (Iterable<CommandNode>)(Iterable<?>)localValue7) {
               if (this.internalMethod01800(localValue17)) {
                  String localValue18 = localValue17.internalMethod03764().getFirst();
                  if (localValue18.toLowerCase().startsWith(localValue15)) {
                     internalMethod01260(localValue14, localValue13, localValue18);
                  }
               }
            }
         } else if (!localValue7.isEmpty() && (localValue21 == 0 || localValue21 == 1 && !localValue6)) {
            String localValue24 = localValue22.toLowerCase();

            for (CommandNode localValue28 : (Iterable<CommandNode>)(Iterable<?>)localValue7) {
               if (this.internalMethod01800(localValue28)) {
                  String localValue30 = localValue28.internalMethod03764().getFirst();
                  if (localValue30.toLowerCase().startsWith(localValue24)) {
                     internalMethod01260(localValue14, localValue13, localValue30);
                  }
               }
            }
         } else {
            if (localValue21 == 0 && !localValue6) {
               return Suggestions.empty();
            }

            List localValue23 = localValue8.internalMethod01430();
            int localValue25 = localValue21 - (localValue6 ? 0 : 1);
            if (localValue25 < 0) {
               localValue25 = 0;
            }

            CommandParameter localValue27 = null;
            if (localValue25 >= localValue23.size()) {
               if (!localValue23.isEmpty() && ((CommandParameter)localValue23.getLast()).internalMethod06683()) {
                  localValue27 = (CommandParameter)localValue23.getLast();
               }
            } else {
               localValue27 = (CommandParameter)localValue23.get(localValue25);
            }

            if (localValue27 != null && localValue27.internalMethod05200() != null) {
               String localValue29 = localValue22.toLowerCase();

               for (String localValue20 : localValue27.internalMethod05200().suggestions(localValue29)) {
                  internalMethod01260(localValue14, localValue13, localValue20);
               }
            }
         }

         return !localValue14.isEmpty() ? CompletableFuture.completedFuture(new Suggestions(localValue13, localValue14)) : Suggestions.empty();
      } else {
         return Suggestions.empty();
      }
   }

   private CommandNode internalMethod00524(List<CommandNode> localValue1, String localValue2) {
      for (CommandNode localValue4 : localValue1) {
         if (this.internalMethod01800(localValue4)) {
            for (String localValue6 : localValue4.internalMethod03764()) {
               if (localValue6.equalsIgnoreCase(localValue2)) {
                  return localValue4;
               }
            }
         }
      }

      return null;
   }

   private static void internalMethod01260(List<Suggestion> localValue0, StringRange localValue1, String localValue2) {
      String localValue3 = internalMethod06556(localValue2);

      for (Suggestion localValue5 : localValue0) {
         if (localValue5.getText().equalsIgnoreCase(localValue3)) {
            return;
         }
      }

      localValue0.add(new Suggestion(localValue1, localValue3));
   }

   private static String internalMethod06556(String localValue0) {
      boolean localValue1 = false;

      for (int localValue2 = 0; localValue2 < localValue0.length(); localValue2++) {
         char localValue3 = localValue0.charAt(localValue2);
         if (Character.isWhitespace(localValue3) || localValue3 == '"') {
            localValue1 = true;
            break;
         }
      }

      return !localValue1 ? localValue0 : "\"" + localValue0.replace("\"", "\\\"") + "\"";
   }

   @Generated
   public String internalMethod03606() {
      return this.internalField0248;
   }

   @Generated
   public void internalMethod04609(String localValue1) {
      this.internalField0248 = localValue1;
   }

   static final class InternalType0373 {
      private final CommandNode internalField0418;
      private final int internalField0227;
      private final boolean internalField0277;

      InternalType0373(CommandNode localValue1, int localValue2, boolean localValue3) {
         this.internalField0418 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0277 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0373[command=" + this.internalField0418 + ", index=" + this.internalField0227 + ", accessible=" + this.internalField0277 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0418);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0277);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         CommandRegistry.InternalType0373 other = (CommandRegistry.InternalType0373) localValue1;
         return java.util.Objects.equals(this.internalField0418, other.internalField0418)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0277, other.internalField0277);
      }

      public CommandNode internalMethod04976() {
         return this.internalField0418;
      }

      public int internalMethod02834() {
         return this.internalField0227;
      }

      public boolean internalMethod02835() {
         return this.internalField0277;
      }
   }

   static final class InternalType0374 {
      private final String internalField0248;
      private final int internalField0227;
      private final int internalField0228;

      InternalType0374(String localValue1, int localValue2, int localValue3) {
         this.internalField0248 = localValue1;
         this.internalField0227 = localValue2;
         this.internalField0228 = localValue3;
      }

      @Override
      public final String toString() {
         return "InternalType0374[text=" + this.internalField0248 + ", start=" + this.internalField0227 + ", end=" + this.internalField0228 + "]";
      }

      @Override
      public final int hashCode() {
         int result = 0;
         result = 31 * result + java.util.Objects.hashCode(this.internalField0248);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0227);
         result = 31 * result + java.util.Objects.hashCode(this.internalField0228);
         return result;
      }

      @Override
      public final boolean equals(Object localValue1) {
         if (this == localValue1) return true;
         if (localValue1 == null || getClass() != localValue1.getClass()) return false;
         CommandRegistry.InternalType0374 other = (CommandRegistry.InternalType0374) localValue1;
         return java.util.Objects.equals(this.internalField0248, other.internalField0248)
            && java.util.Objects.equals(this.internalField0227, other.internalField0227)
            && java.util.Objects.equals(this.internalField0228, other.internalField0228);
      }

      public String internalMethod00839() {
         return this.internalField0248;
      }

      public int internalMethod04645() {
         return this.internalField0227;
      }

      public int internalMethod04647() {
         return this.internalField0228;
      }
   }
}
