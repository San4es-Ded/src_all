package wtf.wyvern.core.command.impl;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.command.CommandSource;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.command.api.CommandAbstract;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.utility.game.other.MessageUtil;

public final class PanicCommand extends CommandAbstract {
    private final Set<Module> savedModules = new HashSet<>();

    public PanicCommand() {
        super("panic");
    }

    @Override
    public void execute(LiteralArgumentBuilder<CommandSource> builder) {
        builder.executes(context -> {
            savedModules.clear();
            savedModules.addAll(Wyvern.getInstance().getModuleManager().getActiveModules());
            savedModules.forEach(Module::toggle);
            MessageUtil.displayWarning("Все модули выключены. Используй .panic restore для возврата.");
            return 1;
        });
        builder.then(literal("restore").executes(context -> {
            if (savedModules.isEmpty()) {
                MessageUtil.displayInfo("Нет состояния для восстановления");
                return 1;
            }
            int restored = 0;
            for (Module module : savedModules) {
                if (!module.isEnabled()) {
                    module.toggle();
                    restored++;
                }
            }
            savedModules.clear();
            MessageUtil.displayInfo("Восстановлено модулей: " + restored);
            return 1;
        }));
    }
}
