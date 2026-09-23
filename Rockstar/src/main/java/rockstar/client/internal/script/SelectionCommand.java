package rockstar.client.internal.script;






import rockstar.client.core.*;
import rockstar.client.command.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.List;
import java.util.Set;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;

public final class SelectionCommand {
   static final Set<String> internalField0546 = Set.of("clear", "reset", "\u0441\u0431\u0440\u043e\u0441", "\u043e\u0447\u0438\u0441\u0442\u0438\u0442\u044c");
   static final Object internalField0290 = new Object();
   public CommandNode internalMethod07275() {
      return CommandBuilder.internalMethod07482(
            "sel",
            localValue1 -> localValue1.internalMethod06148("commands.sel.description")
               .internalMethod05325("pos", "\u0432\u044b\u0434\u0435\u043b\u0438\u0442\u044c")
               .internalMethod01539("args", localValue1x -> localValue1x.internalMethod00125().internalMethod06921().internalMethod00776(new CommandValidator() {
                  @Override
                  public OperationResult validate(String localValue1) {
                     if (SelectionCommand.internalField0546.contains(localValue1.toLowerCase())) {
                        return OperationResult.internalMethod00116(SelectionCommand.internalField0290);
                     } else {
                        try {
                           return OperationResult.internalMethod00116(Integer.parseInt(localValue1));
                        } catch (NumberFormatException localValue3) {
                           return OperationResult.internalMethod05941("'" + localValue1 + "' \u043d\u0435 \u0447\u0438\u0441\u043b\u043e \u0438 \u043d\u0435 'clear'");
                        }
                     }
                  }

                  @Override
                  public List<String> suggestions(String localValue1) {
                     return "clear".startsWith(localValue1.toLowerCase()) ? List.of("clear") : List.of();
                  }
               }))
               .internalMethod00262(this::internalMethod01729)
         )
         .internalMethod04146();
   }

   private void internalMethod01729(ParsedCommand localValue1) {
      List localValue2 = (List)localValue1.internalMethod02266().get(0);
      RegionSelection localValue3 = RegionSelection.internalMethod00889();
      if (localValue2 != null && localValue2.stream().anyMatch(localValue0 -> localValue0 == internalField0290)) {
         localValue3.internalMethod05224();
         ChatMessageHelper.internalMethod00196("\u0412\u044b\u0434\u0435\u043b\u0435\u043d\u0438\u0435 \u0441\u0431\u0440\u043e\u0448\u0435\u043d\u043e");
      } else {
         BlockPos localValue4;
         if (localValue2 != null && !localValue2.isEmpty()) {
            List localValue5 = localValue2.stream().filter(Integer.class::isInstance).map(Integer.class::cast).toList();
            if (localValue5.size() != 3) {
               ChatMessageHelper.internalMethod06835(
                  "\u0418\u0441\u043f\u043e\u043b\u044c\u0437\u043e\u0432\u0430\u043d\u0438\u0435: .newton sel | .newton sel <x> <y> <z> | .newton sel clear"
               );
               return;
            }

            localValue4 = new BlockPos((Integer)localValue5.get(0), (Integer)localValue5.get(1), (Integer)localValue5.get(2));
         } else {
            localValue4 = internalMethod05297();
            if (localValue4 == null) {
               ChatMessageHelper.internalMethod06835("\u041d\u0435 \u0441\u043c\u043e\u0442\u0440\u0438\u0448\u044c \u043d\u0430 \u0431\u043b\u043e\u043a");
               return;
            }
         }

         int localValue10 = localValue3.internalMethod04566(localValue4);
         if (localValue3.internalMethod05225()) {
            BlockPos localValue6 = localValue3.internalMethod08346();
            BlockPos localValue7 = localValue3.internalMethod09055();
            long localValue8 = (long)(localValue7.getX() - localValue6.getX() + 1) * (localValue7.getY() - localValue6.getY() + 1) * (localValue7.getZ() - localValue6.getZ() + 1);
            ChatMessageHelper.internalMethod00196(
               "\u0423\u0433\u043e\u043b "
                  + localValue10
                  + ": "
                  + internalMethod00019(localValue4)
                  + " \u2014 \u043e\u0431\u043b\u0430\u0441\u0442\u044c \u0432\u044b\u0434\u0435\u043b\u0435\u043d\u0430 ("
                  + localValue8
                  + " \u0431\u043b\u043e\u043a\u043e\u0432). .newton cleararea / .newton fill"
            );
         } else {
            ChatMessageHelper.internalMethod00196(
               "\u0423\u0433\u043e\u043b "
                  + localValue10
                  + ": "
                  + internalMethod00019(localValue4)
                  + " \u2014 \u043f\u043e\u0441\u0442\u0430\u0432\u044c \u0432\u0442\u043e\u0440\u043e\u0439 \u0443\u0433\u043e\u043b"
            );
         }
      }
   }

   private static String internalMethod00019(BlockPos localValue0) {
      return localValue0.getX() + ", " + localValue0.getY() + ", " + localValue0.getZ();
   }

   private static BlockPos internalMethod05297() {
      MinecraftClient localValue0 = MinecraftClient.getInstance();
      if (localValue0.player != null && localValue0.world != null) {
         Vec3d localValue1 = localValue0.player.getEyePos();
         Vec3d localValue2 = localValue0.player.getRotationVec(1.0F);
         Vec3d localValue3 = localValue1.add(localValue2.multiply(256.0));
         RaycastContext localValue4 = new RaycastContext(localValue1, localValue3, ShapeType.OUTLINE, FluidHandling.NONE, localValue0.player);
         BlockHitResult localValue5 = localValue0.world.raycast(localValue4);
         return localValue5.getType() != Type.BLOCK ? null : localValue5.getBlockPos();
      } else {
         return null;
      }
   }
}
