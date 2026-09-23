package wtf.wyvern.core.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.arguments.StringArgumentType;
import java.util.List;
import net.minecraft.command.CommandSource;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.utility.game.other.MessageUtil;
import wtf.wyvern.render.display.Keyboard;

public final class BindCommand extends CommandAbstract {
    public BindCommand() {
        super("bind");
    }

    @Override
    public void execute(LiteralArgumentBuilder<CommandSource> builder) {
        builder.then(literal("add").then(arg("module", StringArgumentType.word()).then(arg("key", StringArgumentType.word()).executes(context -> {
            Module module = Wyvern.getInstance().getModuleManager().getModule(context.getArgument("module", String.class));
            int key = Keyboard.getKeyCode(context.getArgument("key", String.class));
            if (module == null) {
                MessageUtil.displayError("Модуль не найден");
            } else if (key == -1) {
                MessageUtil.displayError("Клавиша не найдена");
            } else {
                module.setKeyCode(key);
                MessageUtil.displayInfo("Бинд " + module.getName() + ": " + Keyboard.getKeyName(key));
            }
            return 1;
        }))));
        builder.then(literal("remove").then(arg("module", StringArgumentType.word()).executes(context -> {
            Module module = Wyvern.getInstance().getModuleManager().getModule(context.getArgument("module", String.class));
            if (module == null) {
                MessageUtil.displayError("Модуль не найден");
            } else {
                module.setKeyCode(-1);
                MessageUtil.displayInfo("Бинд снят: " + module.getName());
            }
            return 1;
        })));
        builder.then(literal("list").executes(context -> {
            List<Module> bound = Wyvern.getInstance().getModuleManager().getModules().stream()
                    .filter(module -> module.getKeyCode() != -1)
                    .toList();
            if (bound.isEmpty()) {
                MessageUtil.displayInfo("Назначенных биндов нет");
            } else {
                String text = bound.stream()
                        .map(module -> module.getName() + " §7[" + Keyboard.getKeyName(module.getKeyCode()) + "]")
                        .reduce((first, second) -> first + ", " + second)
                        .orElse("");
                MessageUtil.displayInfo("Бинды: " + text);
            }
            return 1;
        }));
    }
}
