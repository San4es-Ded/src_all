package haron.modules.visuals;

import com.mojang.blaze3d.systems.RenderSystem;
import haron.events.BlockOutlineEvent;
import haron.module.ModuleManager;
import haron.module.ModuleInfo;
import haron.module.ModuleCategory;
import haron.module.HaronModule;
import haron.render.shader.DefaultShaders;
import haron.render.shader.ShaderProgram;
import haron.settings.NumberSetting;
import haron.settings.ColorSetting;
import haron.settings.SettingGroup;
import haron.settings.ModeSetting;
import haron.settings.BooleanSetting;
import java.awt.Color;
import java.util.Objects;
import java.util.Optional;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.ShaderProgramKey;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.BuiltBuffer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import org.joml.Matrix4f;
import org.joml.Vector4f;
import org.lwjgl.opengl.GL11;

@ModuleInfo(a="Block Overlay", b="Красиво выделяет блок, на который наведен игрок", c=ModuleCategory.VISUALS)
public class BlockOverlay
extends HaronModule {
    private static final String FILL_NORMAL = "Обычная";
    private static final String FILL_SHADER = "Шейдер";
    private static final String ANIMATION_NONE = "Нет";
    private static final String ANIMATION_PULSE = "Пульсация";
    private static final String ANIMATION_WAVE = "Волна";
    private static final String SHADER_NEBULA = "Небула";
    private static final String SHADER_STARS = "Звёзды";
    private static final String SHADER_WEB = "Паутина";
    private static final String SHADER_PLASMA = "Плазма";
    private final SettingGroup outlineGroup = new SettingGroup("Обводка");
    private final BooleanSetting outlineEnabled = new BooleanSetting("Обводка", true);
    private final NumberSetting lineWidth;
    private final SettingGroup fillGroup;
    private final BooleanSetting fillEnabled;
    private final ModeSetting fillType;
    private final NumberSetting fillAlpha;
    private final ModeSetting shaderType;
    private final NumberSetting shaderSpeed;
    private final NumberSetting shaderAlpha;
    private final SettingGroup animationGroup;
    private final ModeSetting animationMode;
    private final SettingGroup colorGroup;
    private final BooleanSetting useClientColor;
    private final ColorSetting customColor;
    private final long startTime = System.currentTimeMillis();

    private Box applyPrimaryAnimation(Box box) {
        if (!this.animationMode.b("Пульсация")) {
            return box;
        }
        float f = 1.0f + (float)((Math.sin((double)System.currentTimeMillis() / 260.0) + 1.0) * 0.5) * 0.04f;
        return BlockOverlay.scaleBox(box, f);
    }

    private void shaderQuad(Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12) {
        this.shaderTriangle(matrix4f, f, f2, f3, 0.0f, 0.0f, f4, f5, f6, 1.0f, 0.0f, f7, f8, f9, 1.0f, 1.0f);
        this.shaderTriangle(matrix4f, f, f2, f3, 0.0f, 0.0f, f7, f8, f9, 1.0f, 1.0f, f10, f11, f12, 0.0f, 1.0f);
    }

    private String shaderName() {
        switch (this.shaderType.d()) {
            case "Звёзды": {
                return "block_starfield";
            }
            case "Паутина": {
                return "block_cobweb";
            }
            case "Плазма": {
                return "block_plasma";
            }
        }
        return "block_nebula";
    }

    private static void vertex(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, Color color, float f4) {
        bufferBuilder.vertex(matrix4f, f, f2, f3).color((float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, (float)color.getAlpha() / 255.0f * f4);
    }

    private static void quad(BufferBuilder bufferBuilder, Matrix4f matrix4f, Color color, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13) {
        BlockOverlay.vertex(bufferBuilder, matrix4f, f2, f3, f4, color, f);
        BlockOverlay.vertex(bufferBuilder, matrix4f, f5, f6, f7, color, f);
        BlockOverlay.vertex(bufferBuilder, matrix4f, f8, f9, f10, color, f);
        BlockOverlay.vertex(bufferBuilder, matrix4f, f11, f12, f13, color, f);
    }

    private void renderWave(MatrixStack matrixStack, Box box, Color color) {
        float f = (float)(System.currentTimeMillis() % 1000L) / 1000.0f;
        Box box2 = BlockOverlay.scaleBox(box, 1.0f + f * 0.18f);
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthFunc((int)519);
        this.drawOutline(matrixStack, box2, color, 1.0f - f, Math.max(1.0f, this.lineWidth.a() - 0.5f));
        RenderSystem.depthFunc((int)515);
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    private static Box scaleBox(Box box, float f) {
        Vec3d vec3d = box.getCenter();
        double d = (box.maxX - box.minX) * 0.5 * (double)f;
        double d2 = (box.maxY - box.minY) * 0.5 * (double)f;
        double d3 = (box.maxZ - box.minZ) * 0.5 * (double)f;
        return new Box(vec3d.x - d, vec3d.y - d2, vec3d.z - d3, vec3d.x + d, vec3d.y + d2, vec3d.z + d3);
    }

    private void renderOverlay(MatrixStack matrixStack, Box box, Color color) {
        matrixStack.push();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableCull();
        RenderSystem.depthMask((boolean)false);
        RenderSystem.depthFunc((int)519);
        if (this.fillEnabled.a()) {
            if (this.fillType.b("Шейдер")) {
                this.drawShaderFill(matrixStack, box, color);
            } else {
                this.drawPlainFill(matrixStack, box, color);
            }
        }
        if (this.outlineEnabled.a()) {
            this.drawOutline(matrixStack, box, color, 1.0f, this.lineWidth.a());
        }
        RenderSystem.depthFunc((int)515);
        RenderSystem.depthMask((boolean)true);
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        matrixStack.pop();
    }

    private void drawPlainFill(MatrixStack matrixStack, Box box, Color color) {
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        BlockOverlay.addBoxQuads(bufferBuilder, matrixStack.peek().getPositionMatrix(), box, color, this.fillAlpha.a());
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
    }

    private void drawOutline(MatrixStack matrixStack, Box box, Color color, float f, float f2) {
        GL11.glEnable((int)2848);
        RenderSystem.setShader((ShaderProgramKey)ShaderProgramKeys.POSITION_COLOR);
        RenderSystem.lineWidth((float)f2);
        BufferBuilder bufferBuilder = Tessellator.getInstance().begin(VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        BlockOverlay.addBoxLines(bufferBuilder, matrixStack.peek().getPositionMatrix(), box, color, f);
        BufferRenderer.drawWithGlobalProgram((BuiltBuffer)bufferBuilder.end());
        RenderSystem.lineWidth((float)1.0f);
        GL11.glDisable((int)2848);
    }

    @EventHandler
    public void onBlockOutline(BlockOutlineEvent feh2nk2) {
        feh2nk2.b();
        BlockState blockState = feh2nk2.blockState();
        if (blockState.isAir()) {
            return;
        }
        Box box = blockState.getOutlineShape((BlockView)BlockOverlay.c.world, feh2nk2.blockPos()).getBoundingBox().offset((double)feh2nk2.blockPos().getX() - feh2nk2.cameraX(), (double)feh2nk2.blockPos().getY() - feh2nk2.cameraY(), (double)feh2nk2.blockPos().getZ() - feh2nk2.cameraZ());
        this.renderOverlay(feh2nk2.matrices(), this.applyPrimaryAnimation(box), this.overlayColor());
        if (this.animationMode.b("Волна")) {
            this.renderWave(feh2nk2.matrices(), box, this.overlayColor());
        }
    }

    private static void addBoxQuads(BufferBuilder bufferBuilder, Matrix4f matrix4f, Box box, Color color, float f) {
        float f2 = (float)box.minX;
        float f3 = (float)box.minY;
        float f4 = (float)box.minZ;
        float f5 = (float)box.maxX;
        float f6 = (float)box.maxY;
        float f7 = (float)box.maxZ;
        BlockOverlay.quad(bufferBuilder, matrix4f, color, f, f2, f3, f7, f5, f3, f7, f5, f6, f7, f2, f6, f7);
        BlockOverlay.quad(bufferBuilder, matrix4f, color, f, f2, f6, f4, f5, f6, f4, f5, f3, f4, f2, f3, f4);
        BlockOverlay.quad(bufferBuilder, matrix4f, color, f, f2, f3, f4, f2, f3, f7, f2, f6, f7, f2, f6, f4);
        BlockOverlay.quad(bufferBuilder, matrix4f, color, f, f5, f6, f4, f5, f6, f7, f5, f3, f7, f5, f3, f4);
        BlockOverlay.quad(bufferBuilder, matrix4f, color, f, f2, f6, f4, f2, f6, f7, f5, f6, f7, f5, f6, f4);
        BlockOverlay.quad(bufferBuilder, matrix4f, color, f, f2, f3, f4, f5, f3, f4, f5, f3, f7, f2, f3, f7);
    }

    private static void addBoxLines(BufferBuilder bufferBuilder, Matrix4f matrix4f, Box box, Color color, float f) {
        float f2 = (float)box.minX;
        float f3 = (float)box.minY;
        float f4 = (float)box.minZ;
        float f5 = (float)box.maxX;
        float f6 = (float)box.maxY;
        float f7 = (float)box.maxZ;
        BlockOverlay.line(bufferBuilder, matrix4f, f2, f3, f4, f5, f3, f4, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f2, f3, f4, f2, f6, f4, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f2, f3, f4, f2, f3, f7, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f5, f6, f7, f2, f6, f7, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f5, f6, f7, f5, f3, f7, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f5, f6, f7, f5, f6, f4, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f2, f6, f4, f5, f6, f4, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f5, f3, f4, f5, f3, f7, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f2, f3, f7, f5, f3, f7, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f2, f6, f7, f2, f6, f4, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f2, f6, f7, f2, f3, f7, color, f);
        BlockOverlay.line(bufferBuilder, matrix4f, f5, f6, f4, f5, f3, f4, color, f);
    }

    private Color overlayColor() {
        return this.useClientColor.a() ? ModuleManager.CLIENT_COLOR.n() : this.customColor.a();
    }

    private void drawShaderFill(MatrixStack matrixStack, Box box, Color color) {
        Optional<ShaderProgram> optional = DefaultShaders.getRegistry().find(this.shaderName());
        if (optional.isEmpty() || !optional.get().b()) {
            this.drawPlainFill(matrixStack, box, color);
            return;
        }
        ShaderProgram s5pbng2 = optional.get();
        Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
        float f = this.shaderAlpha.a();
        float f2 = (float)((System.currentTimeMillis() - this.startTime) % 100000L) / 1000.0f * this.shaderSpeed.a();
        s5pbng2.d();
        s5pbng2.a("time", f2);
        s5pbng2.a("screenSize", (float)c.getWindow().getFramebufferWidth(), (float)c.getWindow().getFramebufferHeight());
        s5pbng2.a("baseColor", (float)color.getRed() / 255.0f, (float)color.getGreen() / 255.0f, (float)color.getBlue() / 255.0f, 1.0f);
        s5pbng2.a("alpha", f * 1.5f);
        RenderSystem.blendFunc((int)770, (int)1);
        float f3 = (float)box.minX;
        float f4 = (float)box.minY;
        float f5 = (float)box.minZ;
        float f6 = (float)box.maxX;
        float f7 = (float)box.maxY;
        float f8 = (float)box.maxZ;
        this.shaderQuad(matrix4f, f3, f4, f8, f6, f4, f8, f6, f7, f8, f3, f7, f8);
        this.shaderQuad(matrix4f, f6, f4, f5, f3, f4, f5, f3, f7, f5, f6, f7, f5);
        this.shaderQuad(matrix4f, f3, f4, f5, f3, f4, f8, f3, f7, f8, f3, f7, f5);
        this.shaderQuad(matrix4f, f6, f4, f8, f6, f4, f5, f6, f7, f5, f6, f7, f8);
        this.shaderQuad(matrix4f, f3, f7, f8, f6, f7, f8, f6, f7, f5, f3, f7, f5);
        this.shaderQuad(matrix4f, f3, f4, f5, f6, f4, f5, f6, f4, f8, f3, f4, f8);
        s5pbng2.e();
        RenderSystem.defaultBlendFunc();
    }

    private void shaderTriangle(Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, float f7, float f8, float f9, float f10, float f11, float f12, float f13, float f14, float f15) {
        Vector4f vector4f = matrix4f.transform(new Vector4f(f, f2, f3, 1.0f));
        Vector4f vector4f2 = matrix4f.transform(new Vector4f(f6, f7, f8, 1.0f));
        Vector4f vector4f3 = matrix4f.transform(new Vector4f(f11, f12, f13, 1.0f));
        float[] fArray = new float[]{vector4f.x, vector4f.y, vector4f.z, f4, f5, vector4f2.x, vector4f2.y, vector4f2.z, f9, f10, vector4f3.x, vector4f3.y, vector4f3.z, f14, f15};
        ShaderProgram.a(fArray, 3);
    }

    public BlockOverlay() {
        NumberSetting by6erl2 = new NumberSetting("Толщина линий", 2.0f, 1.0f, 5.0f, 0.5f);
        BooleanSetting xcv91t2 = this.outlineEnabled;
        Objects.requireNonNull(xcv91t2);
        this.lineWidth = by6erl2.a(xcv91t2::a);
        this.fillGroup = new SettingGroup("Заливка");
        this.fillEnabled = new BooleanSetting("Заливка", true);
        ModeSetting s82syr2 = new ModeSetting("Тип заливки", new String[]{"Обычная", "Шейдер"}, "Обычная");
        BooleanSetting xcv91t3 = this.fillEnabled;
        Objects.requireNonNull(xcv91t3);
        this.fillType = s82syr2.a(xcv91t3::a);
        this.fillAlpha = new NumberSetting("Прозрачность заливки", 0.3f, 0.1f, 1.0f, 0.05f).a(() -> this.fillEnabled.a() && this.fillType.b("Обычная"));
        this.shaderType = new ModeSetting("Шейдер", new String[]{"Небула", "Звёзды", "Паутина", "Плазма"}, "Небула").a(() -> this.fillEnabled.a() && this.fillType.b("Шейдер"));
        this.shaderSpeed = new NumberSetting("Скорость анимации", 1.0f, 0.1f, 3.0f, 0.1f).a(() -> this.fillEnabled.a() && this.fillType.b("Шейдер"));
        this.shaderAlpha = new NumberSetting("Прозрачность", 1.0f, 0.1f, 1.0f, 0.05f).a(() -> this.fillEnabled.a() && this.fillType.b("Шейдер"));
        this.animationGroup = new SettingGroup("Анимация");
        this.animationMode = new ModeSetting("Режим анимации", new String[]{"Нет", "Пульсация", "Волна"}, "Нет");
        this.colorGroup = new SettingGroup("Цвет");
        this.useClientColor = new BooleanSetting("Цвет клиента", true);
        this.customColor = new ColorSetting("Кастомный цвет", Color.WHITE).a(() -> {
            return !this.useClientColor.a();
        });
    }

    private static void line(BufferBuilder bufferBuilder, Matrix4f matrix4f, float f, float f2, float f3, float f4, float f5, float f6, Color color, float f7) {
        BlockOverlay.vertex(bufferBuilder, matrix4f, f, f2, f3, color, f7);
        BlockOverlay.vertex(bufferBuilder, matrix4f, f4, f5, f6, color, f7);
    }
}

