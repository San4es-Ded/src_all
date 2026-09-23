package haron.modules.visuals;

import haron.events.WorldRenderPostEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.render.SkyShaderProgram;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.render.Camera;
import ru.haron.mixin.accessor.GameRendererAccessor;

@ModuleInfo(a="Sky Shader", b="Replaces sky with procedural shader effects", c=ModuleCategory.VISUALS)
public class SkyShader
extends HaronModule {
    private final ModeSetting mode = new ModeSetting("Режим", new String[]{"Облака", "Магма"}, "Облака");
    private final NumberSetting speed = new NumberSetting("Скорость", 0.15f, 0.0f, 2.0f, 0.01f);
    private final NumberSetting density = new NumberSetting("Плотность", 0.55f, 0.0f, 1.0f, 0.01f);
    private final NumberSetting scale = new NumberSetting("Масштаб", 3.0f, 0.5f, 10.0f, 0.1f);
    private final BooleanSetting useClientColor = new BooleanSetting("Цвет клиента", true);
    private final ColorSetting skyColor = new ColorSetting("Кастомный цвет", new Color(45, 55, 65)).a(() -> {
        return !this.useClientColor.get();
    });
    private final BooleanSetting hideVanillaSky = new BooleanSetting("Скрыть ванильное небо", false);

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

    public boolean isHideVanillaSky() {
        int n = 157;
        return this.hideVanillaSky.get();
    }

    @EventHandler
    public void onWorldRender(WorldRenderPostEvent kvprd92) {
        if (SkyShader.c.player == null || SkyShader.c.gameRenderer == null) {
            return;
        }
        Camera camera = SkyShader.c.gameRenderer.getCamera();
        float f = camera.getYaw();
        float f2 = camera.getPitch();
        float f3 = ((GameRendererAccessor)SkyShader.c.gameRenderer).haron$getFov(camera, c.getRenderTickCounter().getTickDelta(true), true);
        SkyShaderProgram.get().render(this.getSpeed(), this.getDensity(), this.getScale(), this.getSkyColor(), f, f2, f3, this.getSkyMode());
    }
}

