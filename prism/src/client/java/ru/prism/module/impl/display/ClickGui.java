package ru.prism.module.impl.display;

import org.lwjgl.glfw.GLFW;
import ru.prism.Client;
import ru.prism.manager.event_impl.EventKey;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.MultiBooleanSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.utils.math.Keyboard;

@ModuleInfo(
        name = "Click Gui",
        category = Category.HUD,
        key = GLFW.GLFW_KEY_RIGHT_SHIFT,
        desc = "Настройка менюшки клиента, чтоб выглядела по-пацански.",
        autoEnabled = true,
        allowDisable = false
)
public class ClickGui extends Module {

    public MultiBooleanSetting effect = new MultiBooleanSetting(this, "Эффекты",
            new BooleanSetting("Серый фон", false),
            new BooleanSetting("Затемнять фон", true),
            new BooleanSetting("Размывать фон", true),
            new BooleanSetting("Шейдер", false),
            new BooleanSetting("Частицы", true),
            new BooleanSetting("Скан линии", true),
            new BooleanSetting("Свечение", true),
            new BooleanSetting("Точки", true));


    public SliderSetting size = new SliderSetting(this,"Размер",1.0F,0.5F,1.5F,0.1F);

    @EventHandler
    public void onKey(EventKey event) {
        if (event.getKey() == getKey()) {
            mc.setScreen(Client.get.clickGuiScreen());
        }
    }
}
