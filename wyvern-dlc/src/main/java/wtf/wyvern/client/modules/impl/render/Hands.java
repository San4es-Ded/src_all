package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.render.shader.HandsRenderer;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "Hands",
        category = Category.RENDER,
        description = "Shaders for first-person hands"
)
public final class Hands extends Module {
    public static final Hands INSTANCE = new Hands();

    public final ModeSetting mode = new ModeSetting(
            "Mode", "Fill", "Glass", "Plasma", "Outline", "Halo", "Trail", "Plasma + Trail"
    );
    public final SliderSetting fillOpacity = new SliderSetting(
            "Opacity", 0.35f, 0.0f, 1.0f, 0.01f, this::hasFill
    );
    public final SliderSetting plasmaSpeed = new SliderSetting(
            "Plasma Speed", 1.0f, 0.1f, 5.0f, 0.1f, this::hasPlasma
    );
    public final SliderSetting glassBlur = new SliderSetting(
            "Glass Blur", 19.0f, 0.0f, 60.0f, 1.0f, this::hasGlass
    );
    public final SliderSetting glowRadius = new SliderSetting(
            "Glow Radius", 18.0f, 1.0f, 50.0f, 1.0f, this::hasGlow
    );
    public final SliderSetting glowStrength = new SliderSetting(
            "Glow Strength", 1.0f, 0.0f, 1.0f, 0.01f, this::hasGlow
    );
    public final SliderSetting flameSpeed = new SliderSetting(
            "Trail Speed", 1.1f, 0.1f, 5.0f, 0.1f, this::hasFlame
    );
    public final SliderSetting flameTrail = new SliderSetting(
            "Trail Length", 1.0f, 0.0f, 1.0f, 0.01f, this::hasFlame
    );
    public final SliderSetting outlineThickness = new SliderSetting(
            "Outline Width", 1.5f, 0.5f, 5.0f, 0.1f, this::hasOutline
    );

    private Hands() {
        mode.set("Glass");
    }

    public boolean hasFill() {
        return mode.is("Fill") || mode.is("Glass") || mode.is("Plasma") || mode.is("Plasma + Trail");
    }

    public boolean hasGlass() {
        return mode.is("Glass");
    }

    public boolean hasPlasma() {
        return mode.is("Plasma") || mode.is("Plasma + Trail");
    }

    public boolean hasOutline() {
        return mode.is("Outline");
    }

    public boolean hasGlow() {
        return mode.is("Halo") || mode.is("Trail") || mode.is("Plasma + Trail");
    }

    public boolean hasFlame() {
        return mode.is("Trail") || mode.is("Plasma + Trail");
    }

    @FastNative
    @Override
    public void onDisable() {
        HandsRenderer.getInstance().invalidateState();
        super.onDisable();
    }
}
