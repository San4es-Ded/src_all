package haron.modules.utilities;

import haron.events.ClientTickEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.settings.NumberSetting;
import haron.settings.TextTokenType;
import haron.settings.ModeSetting;
import haron.settings.ValidatedTextSetting;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.item.ItemStack;

@ModuleInfo(a="Auto Eat", b="Automatically eats food at the selected hunger level.", c=ModuleCategory.UTILITIES)
public class AutoEat
extends HaronModule {
    private final ModeSetting mode = new ModeSetting("Режим", new String[]{"Hand", "Command"}, "Hand");
    private final ValidatedTextSetting command = new ValidatedTextSetting("Command", TextTokenType.COMMAND, "/feed", "").a(() -> {
        return this.mode.b("Command");
    });
    private final NumberSetting hungerLevel = new NumberSetting("Уровень голода", 15.0f, 1.0f, 20.0f, 1.0f);

    @Override
    public void f() {
        super.f();
    }

    private boolean a(ItemStack itemStack) {
        return itemStack != null && itemStack.getComponents().contains(DataComponentTypes.FOOD);
    }

    @EventHandler
    public void a(ClientTickEvent q8krcw2) {
    }
}

