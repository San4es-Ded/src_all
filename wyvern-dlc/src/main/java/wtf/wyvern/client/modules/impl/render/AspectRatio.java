package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.render.EventAspectRatio;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "AspectRatio", category = Category.RENDER,
        description = "Изменяет соотношение сторон экрана")
public class AspectRatio extends Module {
    public static final AspectRatio INSTANCE = new AspectRatio();
    private final ModeSetting preset = new ModeSetting("Формат",
            "16:9", "16:10", "4:3", "5:4", "3:2", "21:9", "32:9", "1:1", "Своё");
    private final SliderSetting ratio = new SliderSetting("Своё соотношение", 1.78F, 0.5F, 4.0F, 0.01F,
            () -> preset.is("Своё"));

    private AspectRatio() {
    }

    @FastNative
    @EventTarget
    private void onAspectRatio(EventAspectRatio event) {
        float value = switch (preset.get()) {
            case "16:9" -> 16.0F / 9.0F;       // 1920x1080, 2560x1440
            case "16:10" -> 16.0F / 10.0F;     // 1920x1200, 2560x1600
            case "4:3" -> 4.0F / 3.0F;         // 1600x1200
            case "5:4" -> 5.0F / 4.0F;         // 1280x1024
            case "3:2" -> 3.0F / 2.0F;         // 2160x1440
            case "21:9" -> 21.0F / 9.0F;       // 2560x1080, 3440x1440
            case "32:9" -> 32.0F / 9.0F;       // 5120x1440
            case "1:1" -> 1.0F;
            default -> ratio.getCurrent();
        };
        event.setRatio(value);
        event.setCancelled(true);
    }
}
