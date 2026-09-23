package wtf.wyvern.core.command.impl;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.utility.game.other.MessageUtil;

public final class TargetCommand extends CommandAbstract {
    public TargetCommand() {
        super("target");
    }

    @Override
    public void execute(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("player", StringArgumentType.word()).executes(context -> {
            String name = context.getArgument("player", String.class);
            if (Wyvern.getInstance().getTargetManager().isTarget(name)) {
                MessageUtil.displayInfo("Игрок уже в приоритетных целях: " + name);
            } else {
                Wyvern.getInstance().getTargetManager().add(name);
                Wyvern.getInstance().getTargetManager().save();
                MessageUtil.displayInfo("Добавлена приоритетная цель: " + name);
            }
            return 1;
        })));
        builder.then(literal("remove").then(arg("player", StringArgumentType.word()).executes(context -> {
            String name = context.getArgument("player", String.class);
            boolean removed = Wyvern.getInstance().getTargetManager().getItems().removeIf(target -> target.equalsIgnoreCase(name));
            if (removed) {
                Wyvern.getInstance().getTargetManager().save();
                MessageUtil.displayInfo("Удалена приоритетная цель: " + name);
            } else {
                MessageUtil.displayInfo("Игрока нет в списке целей: " + name);
            }
            return 1;
        })));
        builder.then(literal("list").executes(context -> {
            if (Wyvern.getInstance().getTargetManager().getItems().isEmpty()) {
                MessageUtil.displayInfo("Приоритетных целей нет");
            } else {
                MessageUtil.displayInfo("Цели: " + String.join(", ", Wyvern.getInstance().getTargetManager().getItems()));
            }
            return 1;
        }));
    }
}
