package rockstar.client.internal.script;








import rockstar.client.rotation.*;
import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;

public final class GotoCommand {
   private static final Set<String> internalField0546 = Set.of(
      "elytra",
      "fly",
      "\u044d\u043b\u0438\u0442\u0440\u0430",
      "\u044d\u043b\u0438\u0442\u0440\u0443",
      "\u043f\u043e\u043b\u0451\u0442",
      "\u043f\u043e\u043b\u0435\u0442"
   );
   private static final Object internalField0290 = new Object();

   public CommandNode internalMethod06327() {
      return CommandBuilder.internalMethod07482(
            "goto",
            localValue1 -> localValue1.internalMethod06148("commands.goto.description")
               .internalMethod01539("coords", localValue0 -> localValue0.internalMethod00125().internalMethod00776(localValue0x -> {
                  if (internalField0546.contains(localValue0x.toLowerCase())) {
                     return OperationResult.internalMethod00116(internalField0290);
                  } else {
                     try {
                        return OperationResult.internalMethod00116(Integer.parseInt(localValue0x));
                     } catch (NumberFormatException localValue2) {
                        return OperationResult.internalMethod05941("'" + localValue0x + "' \u043d\u0435 \u0447\u0438\u0441\u043b\u043e \u0438 \u043d\u0435 'elytra'");
                     }
                  }
               }))
               .internalMethod00262(this::internalMethod04853)
         )
         .internalMethod04146();
   }

   private void internalMethod04853(ParsedCommand localValue1) {
      List localValue2 = (List)localValue1.internalMethod02266().get(0);
      if (localValue2 != null && !localValue2.isEmpty()) {
         boolean localValue3 = false;
         ArrayList localValue4 = new ArrayList();

         for (Object localValue6 : localValue2) {
            if (localValue6 == internalField0290) {
               localValue3 = true;
            } else if (localValue6 instanceof Number localValue7) {
               localValue4.add(localValue7.intValue());
            }
         }

         if (localValue4.size() != 2 && localValue4.size() != 3) {
            this.internalMethod01414();
         } else {
            MinecraftClient localValue9 = MinecraftClient.getInstance();
            if (localValue9.player == null || localValue9.world == null) {
               ChatMessageHelper.internalMethod06835("\u041d\u0435\u0442 \u043c\u0438\u0440\u0430/\u0438\u0433\u0440\u043e\u043a\u0430");
            } else if (localValue3) {
               int localValue11 = (Integer)localValue4.get(0);
               int localValue12 = (Integer)localValue4.get(localValue4.size() - 1);
               int localValue8 = localValue4.size() == 3 ? (Integer)localValue4.get(1) : (int)Math.round(localValue9.player.getY());
               NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new ElytraFlightTask(localValue11, localValue8, localValue12, localValue4.size() == 3));
               ChatMessageHelper.internalMethod00196(
                  "\u041b\u0435\u0442\u0438\u043c \u043d\u0430 \u044d\u043b\u0438\u0442\u0440\u0435 \u043a "
                     + localValue11
                     + ", "
                     + (localValue4.size() == 3 ? localValue8 : "?")
                     + ", "
                     + localValue12
               );
            } else {
               Object localValue10;
               if (localValue4.size() == 2) {
                  localValue10 = new ColumnGoal((Integer)localValue4.get(0), (Integer)localValue4.get(1));
               } else {
                  localValue10 = new BlockPositionGoal(new BlockPos((Integer)localValue4.get(0), (Integer)localValue4.get(1), (Integer)localValue4.get(2)));
               }

               NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new GotoPathTask((PathGoal)localValue10));
               ChatMessageHelper.internalMethod00196(
                  "\u0418\u0434\u0451\u043c \u043a "
                     + (localValue4.size() == 2 ? localValue4.get(0) + ", ?, " + localValue4.get(1) : localValue4.get(0) + ", " + localValue4.get(1) + ", " + localValue4.get(2))
               );
            }
         }
      } else {
         this.internalMethod01414();
      }
   }

   private void internalMethod01414() {
      ChatMessageHelper.internalMethod06835(
         "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .goto <x> <z> | <x> <y> <z> [elytra]"
      );
   }
}
