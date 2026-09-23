package haron.modules.visuals;

import haron.events.HudRenderPostEvent;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.modules.visuals.ShulkerPreviewRenderer;
import haron.settings.BooleanSetting;
import meteordevelopment.orbit.EventHandler;
import ru.haron.Haron;

@ModuleInfo(a="ShulkerPreview", b="Просмотр содержимого шалкеров без открытия (в инвентаре при наводении и когда шалкер дропнут).", c=ModuleCategory.VISUALS)
public class ShulkerPreview
extends HaronModule {
    public final BooleanSetting inventoryPreview = new BooleanSetting("Наведение на инвентарь", true);
    public final BooleanSetting worldPreview = new BooleanSetting("Выброшенные предметы", true);

    @EventHandler
    public void onHudRenderPost(HudRenderPostEvent f3zcs42) {
        ShulkerPreviewRenderer.renderWorldTarget(c, f3zcs42.a(), Haron.getInstance().getRender());
    }

    public ShulkerPreview() {
        this.collectSettings();
    }
}

