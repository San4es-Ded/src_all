package wtf.wyvern.core.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.core.command.impl.args.CoordinateArgumentType;
import wtf.wyvern.core.waypoint.Waypoint;
import wtf.wyvern.utility.game.other.MessageUtil;

public class GPSCommand extends CommandAbstract {
   private static final CoordinateArgumentType COORDINATE = CoordinateArgumentType.create();

   public GPSCommand() {
      super("gps");
   }

   public void execute(LiteralArgumentBuilder<CommandSource> builder) {
      var hereName = arg("name", StringArgumentType.word()).executes(context -> {
         if (mc.player == null) return 0;
         Wyvern.getInstance().getWaypointManager().add(context.getArgument("name", String.class), mc.player.getX(), mc.player.getY(), mc.player.getZ());
         MessageUtil.displayInfo("Метка сохранена");
         return 1;
      });
      builder.then(literal("here").then(hereName));

      var addZ = arg("Z", COORDINATE).executes(context -> {
         Wyvern.getInstance().getWaypointManager().add(
               context.getArgument("name", String.class),
               context.getArgument("X", Double.class),
               context.getArgument("Y", Double.class),
               context.getArgument("Z", Double.class));
         MessageUtil.displayInfo("Метка сохранена");
         return 1;
      });
      var addY = arg("Y", COORDINATE).then(addZ);
      var addX = arg("X", COORDINATE).then(addY);
      var addName = arg("name", StringArgumentType.word()).then(addX);
      builder.then(literal("add").then(addName));

      var shortZ = arg("Z", COORDINATE).executes(context -> {
         double x = context.getArgument("X", Double.class);
         double z = context.getArgument("Z", Double.class);
         Wyvern.getInstance().getWaypointManager().set(new Waypoint(x, z));
         MessageUtil.displayInfo("GPS создан и указывает на XZ: %s, %s".formatted(x, z));
         return 1;
      });
      builder.then(arg("X", COORDINATE).then(shortZ));

      var shortZWithY = arg("Z", COORDINATE).executes(context -> {
         double x = context.getArgument("X", Double.class);
         double y = context.getArgument("Y", Double.class);
         double z = context.getArgument("Z", Double.class);
         Wyvern.getInstance().getWaypointManager().set(new Waypoint("GPS", x, y, z, ""));
         MessageUtil.displayInfo("GPS создан и указывает на XYZ: %s, %s, %s".formatted(x, y, z));
         return 1;
      });
      builder.then(arg("X", COORDINATE).then(arg("Y", COORDINATE).then(shortZWithY)));

      var playerZ = arg("Z", COORDINATE).executes(context -> {
         String name = context.getArgument("name", String.class);
         double x = context.getArgument("X", Double.class);
         double z = context.getArgument("Z", Double.class);
         Wyvern.getInstance().getWaypointManager().setPlayerWaypoint(new Waypoint(name, x, z));
         MessageUtil.displayInfo("GPS создан и указывает на XZ: %s, %s".formatted(x, z));
         return 1;
      });
      builder.then(literal("player").then(arg("name", StringArgumentType.word())
            .then(arg("X", COORDINATE).then(playerZ))));

      builder.then(literal("player").then(literal("remove").executes(context -> {
         if (!Wyvern.getInstance().getWaypointManager().isEmptyPlayerWaypoint()) {
            Wyvern.getInstance().getWaypointManager().clearPlayerWaypoint();
            MessageUtil.displayInfo("GPS удален");
         } else {
            MessageUtil.displayInfo("Нет активного GPS");
         }
         return 1;
      })));

      builder.then(literal("remove").executes(context -> {
         if (!Wyvern.getInstance().getWaypointManager().isEmpty()) {
            Wyvern.getInstance().getWaypointManager().clear();
            MessageUtil.displayInfo("GPS удален");
         } else {
            MessageUtil.displayInfo("Нет активного GPS");
         }
         return 1;
      }));
   }
}
