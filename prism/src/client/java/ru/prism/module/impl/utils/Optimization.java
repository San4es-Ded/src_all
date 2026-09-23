package ru.prism.module.impl.utils;

import net.minecraft.client.MinecraftClient;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.SliderSetting;

@ModuleInfo(
        name = "Optimization",
        desc = "Выжимает фпс: подкручивает настройки Minecraft, режет лишние частицы и сбрасывает лимит кадров, когда окно свёрнуто.",
        category = Category.UTILITIES
)
public class Optimization extends Module {

    private static volatile Optimization instance;

    public final BooleanSetting minecraftSettings = new BooleanSetting(this, "Настройки Minecraft", true);
    public final SliderSetting chunkDistance = new SliderSetting(this, "Прогрузка чанков", 8.0F, 2.0F, 32.0F, 1.0F)
            .setVisible(minecraftSettings::getValue);
    public final BooleanSetting fastGraphics = new BooleanSetting(this, "Быстрая графика", true)
            .setVisible(minecraftSettings::getValue);
    public final BooleanSetting dynamicFps = new BooleanSetting(this, "Динамический FPS", true);
    public final SliderSetting bgFps = new SliderSetting(this, "FPS в фоне", 10.0F, 5.0F, 60.0F, 5.0F)
            .setVisible(dynamicFps::getValue);
    public final BooleanSetting limitParticles = new BooleanSetting(this, "Лимит частиц", true);
    public final SliderSetting particleDistance = new SliderSetting(this, "Дистанция частиц", 32.0F, 8.0F, 128.0F, 4.0F)
            .setVisible(limitParticles::getValue);
    public final SliderSetting particleCap = new SliderSetting(this, "Потолок частиц", 1500.0F, 100.0F, 5000.0F, 100.0F)
            .setVisible(limitParticles::getValue);

    private int tickCounter;
    private int currentParticleCount;
    private int originalViewDistance = -1;
    private boolean originalAo = true;
    private int originalMaxFps = -1;
    private boolean wasUnfocused;

    @Override
    protected void onEnable() {
        if (mc.options == null) return;

        instance = this;
        originalViewDistance = mc.options.getViewDistance().getValue();
        originalAo = mc.options.getAo().getValue();
        originalMaxFps = mc.options.getMaxFps().getValue();
        wasUnfocused = false;
        tickCounter = 0;
        applyMinecraftSettings();
    }

    @Override
    protected void onDisable() {
        instance = null;
        if (mc.options == null) return;

        if (originalViewDistance > 0) {
            mc.options.getViewDistance().setValue(originalViewDistance);
        }
        mc.options.getAo().setValue(originalAo);
        if (originalMaxFps > 0) {
            mc.options.getMaxFps().setValue(originalMaxFps);
        }
        originalViewDistance = -1;
        originalMaxFps = -1;
        wasUnfocused = false;
    }

    @EventHandler
    public void onTick(EventTick event) {
        tickCounter++;
        currentParticleCount = 0;

        if (tickCounter % 20 == 0) {
            applyMinecraftSettings();
        }

        if (dynamicFps.getValue() && mc.options != null) {
            if (!mc.isWindowFocused()) {
                if (!wasUnfocused) {
                    if (originalMaxFps == -1) {
                        originalMaxFps = mc.options.getMaxFps().getValue();
                    }
                    mc.options.getMaxFps().setValue(rounded(bgFps));
                    wasUnfocused = true;
                }
            } else if (wasUnfocused) {
                if (originalMaxFps > 0) {
                    mc.options.getMaxFps().setValue(originalMaxFps);
                }
                wasUnfocused = false;
            }
        }
    }

    private void applyMinecraftSettings() {
        if (!minecraftSettings.getValue() || mc.options == null) return;

        int targetDistance = rounded(chunkDistance);
        if (mc.options.getViewDistance().getValue() != targetDistance) {
            mc.options.getViewDistance().setValue(targetDistance);
        }

        if (fastGraphics.getValue() && mc.options.getAo().getValue()) {
            mc.options.getAo().setValue(false);
        }
    }

    private static int rounded(SliderSetting setting) {
        return Math.round(setting.getValue());
    }

    public static boolean shouldSpawnParticle(double x, double y, double z) {
        Optimization opt = instance;
        if (opt == null || !opt.isEnabled() || !opt.limitParticles.getValue()) return true;

        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return true;

        double dx = client.player.getX() - x;
        double dy = client.player.getY() - y;
        double dz = client.player.getZ() - z;
        double max = opt.particleDistance.getValue();
        if (dx * dx + dy * dy + dz * dz > max * max) return false;

        if (opt.currentParticleCount >= rounded(opt.particleCap)) return false;

        opt.currentParticleCount++;
        return true;
    }
}
