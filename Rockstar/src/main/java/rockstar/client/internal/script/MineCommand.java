package rockstar.client.internal.script;








import rockstar.client.rotation.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.List;
import net.minecraft.block.Block;

public final class MineCommand {
   public CommandNode internalMethod07072() {
      return CommandBuilder.internalMethod07482(
            "mine",
            localValue1 -> localValue1.internalMethod06148("commands.mine.description")
               .internalMethod01539(
                  "block",
                  localValue1x -> localValue1x.internalMethod00776(
                     new CommandValidator() {
                        @Override
                        public OperationResult validate(String localValue1) {
                           Block localValue2 = BlockSuggestionProvider.internalMethod05431(localValue1);
                           return (OperationResult)(localValue2 == null
                              ? OperationResult.internalMethod05941("\u0411\u043b\u043e\u043a \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: " + localValue1)
                              : OperationResult.internalMethod00116(localValue2));
                        }

                        @Override
                        public List<String> suggestions(String localValue1) {
                           return BlockSuggestionProvider.internalMethod06324(localValue1);
                        }
                     }
                  )
               )
               .internalMethod00262(this::internalMethod03883)
         )
         .internalMethod04146();
   }

   private void internalMethod03883(ParsedCommand localValue1) {
      Block localValue2 = (Block)localValue1.internalMethod02266().get(0);
      NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new BlockQuarryTask(localValue2));
      ChatMessageHelper.internalMethod00196("\u0418\u0434\u0451\u043c \u043a\u043e\u043f\u0430\u0442\u044c " + localValue2);
   }
}
