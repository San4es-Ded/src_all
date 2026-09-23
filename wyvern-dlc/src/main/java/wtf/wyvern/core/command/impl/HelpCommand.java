package wtf.wyvern.core.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import net.minecraft.command.CommandSource;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.utility.game.other.MessageUtil;

public final class HelpCommand extends CommandAbstract {
    public HelpCommand() {
        super("help");
    }

    @Override
    public void execute(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            MessageUtil.displayInfo("Команды: .bind list, .cfg list/save/load/dir, .friend add/remove/list, .macro add/remove/list, .gps gui/here/add/remove, .panic [restore], .clip, .rct, .tp player/X Y Z");
            return 1;
        });
    }
}
