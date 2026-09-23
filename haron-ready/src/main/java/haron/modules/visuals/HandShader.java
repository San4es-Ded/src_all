package haron.modules.visuals;

import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.render.HandShaderProgram;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;

@ModuleInfo(a="Hand Shader", b="Applies procedural shader effect on held items/hand", c=ModuleCategory.VISUALS)
public class HandShader
extends HaronModule {
    private final ModeSetting mode = new ModeSetting("Режим", new String[]{"Облака", "Магма"}, "Облака");
    private final NumberSetting speed = new NumberSetting("Скорость", 0.15f, 0.0f, 2.0f, 0.01f);
    private final NumberSetting density = new NumberSetting("Плотность", 0.55f, 0.0f, 1.0f, 0.01f);
    private final NumberSetting scale = new NumberSetting("Масштаб", 3.0f, 0.5f, 10.0f, 0.1f);
    private final NumberSetting opacity = new NumberSetting("Прозрачность", 0.55f, 0.0f, 1.0f, 0.01f);
    private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting skyColor = new ColorSetting("Кастомный цвет", new Color(45, 55, 65)).a(() -> {
        return !this.useClientColor.get();
    });

    public float getOpacity() {
        return this.opacity.a();
    }

    public float getDensity() {
        return this.density.a();
    }

    public float getScale() {
        return this.scale.a();
    }

    public float getSpeed() {
        return this.speed.a();
    }

    public int getSkyMode() {
        return this.mode.d().equals("Магма") ? 1 : 0;
    }

    public Color getSkyColor() {
        if (this.useClientColor.get()) {
            return ModuleManager.CLIENT_COLOR.n();
        }
        return this.skyColor.getColor();
    }

    public void renderShader() {
        HandShaderProgram.get().render(this.getSpeed(), this.getDensity(), this.getScale(), this.getSkyColor(), this.getSkyMode(), this.getOpacity());
    }
}

