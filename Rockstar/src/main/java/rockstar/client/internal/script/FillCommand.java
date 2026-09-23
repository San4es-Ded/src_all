package rockstar.client.internal.script;









import rockstar.client.rotation.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.List;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.item.Items;
import net.minecraft.util.math.BlockPos;

public final class FillCommand {
   static final Set<String> internalField0546 = Set.of("stop", "cancel", "\u0441\u0442\u043e\u043f", "\u043e\u0442\u043c\u0435\u043d\u0430");

   public CommandNode internalMethod01405() {
      return CommandBuilder.internalMethod07482(
            "fill",
            localValue1 -> localValue1.internalMethod06148("commands.fill.description")
               .internalMethod05325("\u0437\u0430\u043f\u043e\u043b\u043d\u0438\u0442\u044c")
               .internalMethod01539(
                  "block",
                  localValue1x -> localValue1x.internalMethod00776(
                     new CommandValidator() {
                        @Override
                        public OperationResult validate(String localValue1) {
                           if (FillCommand.internalField0546.contains(localValue1.toLowerCase())) {
                              return OperationResult.internalMethod00116(null);
                           } else {
                              Block localValue2 = BlockSuggestionProvider.internalMethod05431(localValue1);
                              if (localValue2 == null) {
                                 return OperationResult.internalMethod05941(
                                    "\u0411\u043b\u043e\u043a \u043d\u0435 \u043d\u0430\u0439\u0434\u0435\u043d: " + localValue1
                                 );
                              } else {
                                 return (OperationResult)(localValue2.asItem() == Items.AIR
                                    ? OperationResult.internalMethod05941(
                                       "\u042d\u0442\u043e\u0442 \u0431\u043b\u043e\u043a \u043d\u0435\u043b\u044c\u0437\u044f \u043f\u043e\u0441\u0442\u0430\u0432\u0438\u0442\u044c: "
                                          + localValue1
                                    )
                                    : OperationResult.internalMethod00116(localValue2));
                              }
                           }
                        }

                        @Override
                        public List<String> suggestions(String localValue1) {
                           return BlockSuggestionProvider.internalMethod06324(localValue1, "stop");
                        }
                     }
                  )
               )
               .internalMethod00262(this::internalMethod04639)
         )
         .internalMethod04146();
   }

   private void internalMethod04639(ParsedCommand localValue1) {
      Block localValue2 = (Block)localValue1.internalMethod02266().get(0);
      if (localValue2 == null) {
         NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03476();
         ChatMessageHelper.internalMethod00196(
            "\u0417\u0430\u043f\u043e\u043b\u043d\u0435\u043d\u0438\u0435 \u043e\u0441\u0442\u0430\u043d\u043e\u0432\u043b\u0435\u043d\u043e"
         );
      } else {
         RegionSelection localValue3 = RegionSelection.internalMethod00889();
         if (!localValue3.internalMethod05225()) {
            ChatMessageHelper.internalMethod06835(
               "\u0421\u043d\u0430\u0447\u0430\u043b\u0430 \u0432\u044b\u0434\u0435\u043b\u0438 \u043e\u0431\u043b\u0430\u0441\u0442\u044c: \u0441\u043c\u043e\u0442\u0440\u0438 \u043d\u0430 \u0431\u043b\u043e\u043a \u0438 \u0436\u043c\u0438 .newton sel (\u043d\u0443\u0436\u043d\u043e 2 \u0443\u0433\u043b\u0430)"
            );
         } else {
            BlockPos localValue4 = localValue3.internalMethod08346();
            BlockPos localValue5 = localValue3.internalMethod09055();
            NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new AreaFillTask(localValue4, localValue5, localValue2));
            ChatMessageHelper.internalMethod00196(
               "\u0417\u0430\u043f\u043e\u043b\u043d\u044f\u0435\u043c \u043e\u0431\u043b\u0430\u0441\u0442\u044c "
                  + internalMethod07461(localValue4)
                  + " \u2014 "
                  + internalMethod07461(localValue5)
                  + " \u0431\u043b\u043e\u043a\u043e\u043c "
                  + localValue2
            );
         }
      }
   }

   private static String internalMethod07461(BlockPos localValue0) {
      return localValue0.getX() + ", " + localValue0.getY() + ", " + localValue0.getZ();
   }
}
