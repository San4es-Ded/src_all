package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "MotionBlur", category = Category.RENDER, description = "Размытие в движении")
public final class MotionBlur extends Module {
    public static final MotionBlur INSTANCE = new MotionBlur();

    public final SliderSetting strength = new SliderSetting("Сила", 0.85F, 0.1F, 1.0F, 0.05F);
    public final SliderSetting samples = new SliderSetting("Сэмплы", 14.0F, 4.0F, 16.0F, 1.0F);
    public final BooleanSetting camera = new BooleanSetting("Камера", true);
    public final BooleanSetting movement = new BooleanSetting("Движение", true);

    private MotionBlur() {
    }

    @FastNative
    public float getStrength() {
        return strength.getCurrent();
    }

    @FastNative
    public float getSamples() {
        return samples.getCurrent();
    }

    @FastNative
    public boolean useCamera() {
        return camera.isEnabled();
    }

    @FastNative
    public boolean useMovement() {
        return movement.isEnabled();
    }

    @Override
    public void onDisable() {
        wtf.wyvern.render.shader.MotionBlurRenderer.getInstance().reset();
        super.onDisable();
    }
}
