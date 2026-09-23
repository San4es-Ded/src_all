package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.entity.EventEntityColor;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "AntiInvisible", category = Category.RENDER,
        description = "Показывает невидимых игроков")
@FastNative
public final class AntiInvisible extends Module {
    public static final AntiInvisible INSTANCE = new AntiInvisible();
    private final SliderSetting alpha = new SliderSetting("Прозрачность", 0.5F, 0.05F, 1.0F, 0.05F);

    private AntiInvisible() {
    }

    @EventTarget
    public void onEntityColor(EventEntityColor event) {
        event.setColor(new ColorRGBA(event.getColor()).withAlpha((int) (alpha.getCurrent() * 255.0F)).getRGB());
        event.cancel();
    }
}
