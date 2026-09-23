package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.core.BooleanCoercion;
import haron.events.HudRenderPostEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.render.ScaledGuiProjection;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.BooleanSetting;
import java.awt.Color;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.option.Perspective;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.joml.Matrix4f;

@ModuleInfo(a="Crosshair", b="Настраивает отображение прицела", c=ModuleCategory.VISUALS)
public class Crosshair
extends HaronModule {
    private final NumberSetting e = new NumberSetting("Длина", 3.0f, 1.0f, 15.0f, 1.0f);
    private final NumberSetting f = new NumberSetting("Толщина", 1.0f, 1.0f, 10.0f, 1.0f);
    private final NumberSetting g = new NumberSetting("Отступ", 0.0f, 0.0f, 10.0f, 1.0f);
    private final SettingGroup h = new SettingGroup("Поведение");
    private final BooleanSetting i = new BooleanSetting("Обводка", true);
    private final BooleanSetting j = new BooleanSetting("Точка", false);
    private final BooleanSetting k = new BooleanSetting("Менять цвет при наводке", true);
    private final BooleanSetting l = new BooleanSetting("Динамический", false);
    private final SettingGroup m = new SettingGroup("Цвет");
    private final BooleanSetting n = new BooleanSetting("Цвет клиента", false);
    private final ColorSetting o = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> {
        return BooleanCoercion.from(this.n.a() ? 0 : 1);
    });
    public static int a;
    public static boolean b;

    private boolean n() {
        HitResult hitResult = Crosshair.c.crosshairTarget;
        if (hitResult == null || hitResult.getType() != HitResult.Type.ENTITY || !(hitResult instanceof EntityHitResult)) {
            return false;
        }
        return ((EntityHitResult)hitResult).getEntity() instanceof LivingEntity;
    }

    private void a(MatrixStack matrixStack, float f, float f2, float f3, float f4, Color color) {
        float f5 = f + f3;
        float f6 = f2 + f4;
        float f7 = (float)color.getRed() / 255.0f;
        float f8 = (float)color.getGreen() / 255.0f;
        float f9 = (float)color.getBlue() / 255.0f;
        float f10 = (float)color.getAlpha() / 255.0f;
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        bufferBuilder.vertex(matrix4f, f, f6, 0.0f).color(f7, f8, f9, f10);
        bufferBuilder.vertex(matrix4f, f5, f6, 0.0f).color(f7, f8, f9, f10);
        bufferBuilder.vertex(matrix4f, f5, f2, 0.0f).color(f7, f8, f9, f10);
        bufferBuilder.vertex(matrix4f, f, f2, 0.0f).color(f7, f8, f9, f10);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.disableBlend();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    @EventHandler(priority=-200)
    private void a(HudRenderPostEvent f3zcs42) {
        if (Crosshair.c.options.getPerspective() != Perspective.FIRST_PERSON || Crosshair.c.player == null) {
            return;
        }
        ScaledGuiProjection.a(2.0);
        try {
            float f = (float)c.getWindow().getWidth() / 4.0f;
            float f2 = (float)c.getWindow().getHeight() / 4.0f;
            float f3 = 1.0f - Crosshair.c.player.getAttackCooldownProgress(f3zcs42.d());
            Color color = this.o();
            if (this.k.a() && this.n()) {
                color = new Color(255, 64, 64);
            }
            this.a(f3zcs42.a(), f, f2, f3, color);
        }
        finally {
            ScaledGuiProjection.a();
        }
    }

    private void a(MatrixStack matrixStack, float f, float f2, float f3, Color color) {
        float f4 = !this.l.a() ? this.g.a() : this.g.a() + 8.0f * f3;
        float f5 = this.f.a();
        float f6 = this.e.a();
        if (this.i.a()) {
            Color color2 = Color.BLACK;
            this.a(matrixStack, f + f4 - 0.5f, f2 - f5 / 2.0f - 0.5f, f6 + 1.0f, f5 + 1.0f, color2);
            this.a(matrixStack, f - f4 - f6 - 0.5f, f2 - f5 / 2.0f - 0.5f, f6 + 1.0f, f5 + 1.0f, color2);
            this.a(matrixStack, f - f5 / 2.0f - 0.5f, f2 - f4 - f6 - 0.5f, f5 + 1.0f, f6 + 1.0f, color2);
            this.a(matrixStack, f - f5 / 2.0f - 0.5f, f2 + f4 - 0.5f, f5 + 1.0f, f6 + 1.0f, color2);
            if (this.j.a()) {
                this.a(matrixStack, f - 1.0f - 0.5f, f2 - 1.0f - 0.5f, 3.0f, 3.0f, color2);
            }
        }
        this.a(matrixStack, f + f4, f2 - f5 / 2.0f, f6, f5, color);
        this.a(matrixStack, f - f4 - f6, f2 - f5 / 2.0f, f6, f5, color);
        this.a(matrixStack, f - f5 / 2.0f, f2 - f4 - f6, f5, f6, color);
        this.a(matrixStack, f - f5 / 2.0f, f2 + f4, f5, f6, color);
        if (this.j.a()) {
            this.a(matrixStack, f - 1.0f, f2 - 1.0f, 2.0f, 2.0f, color);
        }
    }

    private Color o() {
        if (this.n.a()) {
            return ModuleManager.CLIENT_COLOR.n();
        }
        return this.o.a();
    }
}

