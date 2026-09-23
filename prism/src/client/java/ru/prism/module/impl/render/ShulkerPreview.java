package ru.prism.module.impl.render;

import ru.prism.manager.event_impl.EventDisplay;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.utils.other.Instance;

@ModuleInfo(
        name = "Shulker Preview",
        desc = "Подсматривает, что лежит в шалкерах: наведись в инвентаре или глянь на выпавший.",
        category = Category.VISUALS
)
public class ShulkerPreview extends Module {

    public static ShulkerPreview getInstance() {
        return Instance.get(ShulkerPreview.class);
    }

    public final BooleanSetting inventoryPreview = new BooleanSetting(this, "Наведение в инвентаре", true);
    public final BooleanSetting worldPreview = new BooleanSetting(this, "Выпавшие предметы", true);

    @EventHandler
    public void onDisplay(EventDisplay event) {
        if (!isEnabled()) return;
        ShulkerPreviewRenderer.renderWorldTarget(event);
    }
}
