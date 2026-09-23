package wtf.wyvern.client.modules.impl.render;

import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.GlProgram;
import net.minecraft.client.render.VertexFormats;

@ModuleAnnotation(
        name = "Chams",
        category = Category.RENDER,
        description = "Рисует игроков моделькой как в Totem Angel"
)
public final class Chams extends Module {
    private static final String MODE_NORMAL = "Обычный";
    private static final String MODE_PLASMA = "Plasma";
    private static final GlProgram PLASMA_SHADER =
            new GlProgram(Wyvern.id("chams/plasma"), VertexFormats.POSITION);

    public static final Chams INSTANCE = new Chams();

    public final ModeSetting mode = new ModeSetting("Режим", MODE_NORMAL, MODE_PLASMA);
    public final SliderSetting alpha = new SliderSetting(
            "Прозрачность", 0.8F, 0.1F, 1.0F, 0.05F);
    public final SliderSetting brightness = new SliderSetting(
            "Яркость", 1.0F, 0.1F, 1.0F, 0.05F, () -> mode.is(MODE_NORMAL));
    public final SliderSetting lineWidth = new SliderSetting(
            "Толщина линий", 1.0F, 0.1F, 3.0F, 0.05F);
    public final BooleanSetting throughWalls = new BooleanSetting("Сквозь стены", true);
    public final BooleanSetting glow = new BooleanSetting("Свечение", true);
    public final SliderSetting glowIntensity = new SliderSetting(
            "Сила свечения", 1.5F, 0.5F, 4.0F, 0.1F, glow::isEnabled);
    public final SliderSetting glowLayers = new SliderSetting(
            "Слои свечения", 3.0F, 1.0F, 6.0F, 1.0F, glow::isEnabled);
    public final SliderSetting plasmaSpeed = new SliderSetting(
            "Скорость Plasma", 1.0F, 0.1F, 5.0F, 0.1F, () -> mode.is(MODE_PLASMA));
    public final SliderSetting plasmaScale = new SliderSetting(
            "Масштаб Plasma", 5.0F, 1.0F, 15.0F, 0.5F, () -> mode.is(MODE_PLASMA));

    private long plasmaStartNanos = System.nanoTime();

    private Chams() {
    }

    @Override
    public void onEnable() {
        plasmaStartNanos = System.nanoTime();
        super.onEnable();
    }

    public boolean isPlasma() {
        return mode.is(MODE_PLASMA);
    }

    public GlProgram getPlasmaShader() {
        return PLASMA_SHADER;
    }

    public float getPlasmaTime() {
        return (System.nanoTime() - plasmaStartNanos) / 1_000_000_000.0F * plasmaSpeed.getCurrent();
    }

    @FastNative
    public ColorRGBA getChamsColor() {
        ColorRGBA theme = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
        float multiplier = brightness.getCurrent();
        return new ColorRGBA(
                Math.round(theme.getRed() * multiplier),
                Math.round(theme.getGreen() * multiplier),
                Math.round(theme.getBlue() * multiplier),
                255
        );
    }
}
