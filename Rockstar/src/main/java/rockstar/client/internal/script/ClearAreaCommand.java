package rockstar.client.internal.script;








import rockstar.client.rotation.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

public final class ClearAreaCommand {
   private static final Set<String> internalField0546 = Set.of("stop", "cancel", "\u0441\u0442\u043e\u043f", "\u043e\u0442\u043c\u0435\u043d\u0430");

   public CommandNode internalMethod06163() {
      return CommandBuilder.internalMethod07482(
            "cleararea",
            localValue1 -> localValue1.internalMethod06148("commands.cleararea.description")
               .internalMethod05325("excavate", "dig", "\u0440\u0430\u0441\u043a\u043e\u043f")
               .internalMethod01539("arg", localValue1x -> localValue1x.internalMethod06921().internalMethod00776(new CommandValidator() {
                  @Override
                  public OperationResult validate(String localValue1) {
                     return OperationResult.internalMethod00116(localValue1);
                  }

                  @Override
                  public List<String> suggestions(String localValue1) {
                     return BlockSuggestionProvider.internalMethod06324(localValue1, "stop");
                  }
               }))
               .internalMethod00262(this::internalMethod06957)
         )
         .internalMethod04146();
   }

   private void internalMethod06957(ParsedCommand localValue1) {
      String localValue2 = (String)localValue1.internalMethod02266().get(0);
      if (localValue2 != null && internalField0546.contains(localValue2.toLowerCase())) {
         NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03476();
         ChatMessageHelper.internalMethod00196("\u0420\u0430\u0441\u043a\u043e\u043f \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d");
      } else {
         RegionSelection localValue3 = RegionSelection.internalMethod00889();
         if (!localValue3.internalMethod05225()) {
            ChatMessageHelper.internalMethod06835(
               "\u0421\u043d\u0430\u0447\u0430\u043b\u0430 \u0432\u044b\u0434\u0435\u043b\u0438 \u043e\u0431\u043b\u0430\u0441\u0442\u044c: \u0441\u043c\u043e\u0442\u0440\u0438 \u043d\u0430 \u0431\u043b\u043e\u043a \u0438 \u0436\u043c\u0438 .newton sel (\u043d\u0443\u0436\u043d\u043e 2 \u0443\u0433\u043b\u0430)"
            );
         } else {
            Block localValue4 = null;
            if (localValue2 != null && !localValue2.isBlank()) {
               localValue4 = BlockSuggestionProvider.internalMethod05431(localValue2);
               if (localValue4 == null) {
                  ChatMessageHelper.internalMethod06835("\u0411\u043b\u043e\u043a \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: " + localValue2);
                  return;
               }
            }

            BlockPos localValue5 = localValue3.internalMethod08346();
            BlockPos localValue6 = localValue3.internalMethod09055();
            NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new ExcavationTask(localValue5, localValue6, localValue4));
            ChatMessageHelper.internalMethod00196(
               localValue4 != null
                  ? "\u041a\u043e\u043f\u0430\u0435\u043c "
                     + localValue4
                     + " \u0432 \u043e\u0431\u043b\u0430\u0441\u0442\u0438 "
                     + internalMethod07026(localValue5)
                     + " \u2014 "
                     + internalMethod07026(localValue6)
                  : "\u0420\u0430\u0441\u043a\u0430\u043f\u044b\u0432\u0430\u0435\u043c \u043e\u0431\u043b\u0430\u0441\u0442\u044c "
                     + internalMethod07026(localValue5)
                     + " \u2014 "
                     + internalMethod07026(localValue6)
            );
         }
      }
   }

   private static String internalMethod07026(BlockPos localValue0) {
      return localValue0.getX() + ", " + localValue0.getY() + ", " + localValue0.getZ();
   }
}
