package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.gl.GlUniform;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.GlProgram;
import wtf.wyvern.render.level.Render3DUtil;

@ModuleAnnotation(
        name = "BlockOverlay",
        category = Category.RENDER,
        description = "Подсвечивает выбранный блок плазмой"
)
public final class BlockOverlay extends Module {
    public static final BlockOverlay INSTANCE = new BlockOverlay();

    private static final GlProgram PLASMA_SHADER =
            new GlProgram(Wyvern.id("block_overlay/plasma"), VertexFormats.POSITION);

    private final BooleanSetting fill = new BooleanSetting("Заливка", true);
    private final BooleanSetting outline = new BooleanSetting("Контур", false);
    private final BooleanSetting throughWalls = new BooleanSetting("Через стены", true);
    private final SliderSetting alpha = new SliderSetting("Прозрачность", 0.76F, 0.00F, 1F, 0.05F);
    private final SliderSetting lineWidth = new SliderSetting("Толщина контура", 1.5F, 0.0F, 5.0F, 0.1F);
    private final SliderSetting animationSpeed = new SliderSetting("Плавность", 20.0F, 2.0F, 20.0F, 0.5F);
    private final SliderSetting plasmaSpeed = new SliderSetting("Скорость плазмы", 1.0F, 0.1F, 5.0F, 0.1F);
    private final SliderSetting plasmaScale = new SliderSetting("Масштаб плазмы", 5.0F, 1.0F, 15.0F, 0.5F);

    private Box animatedBox;
    private Box targetBox;
    private long lastFrameNanos;
    private long animationStartNanos;
    private float visibility;

    private BlockOverlay() {
    }

    @Override
    public void onEnable() {
        resetAnimation();
        animationStartNanos = System.nanoTime();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        resetAnimation();
        super.onDisable();
    }

    public boolean replacesVanillaOutline() {
        return isEnabled() && (fill.isEnabled() || outline.isEnabled());
    }

    @EventTarget
    public void onRender3D(EventRender3D event) {
        if (mc.world == null || mc.player == null) {
            resetAnimation();
            return;
        }

        long now = System.nanoTime();
        float deltaSeconds = lastFrameNanos == 0L
                ? 1.0F / 60.0F
                : Math.min((now - lastFrameNanos) / 1_000_000_000.0F, 0.1F);
        lastFrameNanos = now;

        Box nextTarget = getTargetBox();
        float interpolation = 1.0F - (float)Math.exp(-animationSpeed.getCurrent() * deltaSeconds);
        float fadeInterpolation = 1.0F - (float)Math.exp(-animationSpeed.getCurrent() * 0.8F * deltaSeconds);

        if (nextTarget != null) {
            targetBox = nextTarget;
            if (animatedBox == null) {
                Vec3d center = nextTarget.getCenter();
                animatedBox = new Box(center.x, center.y, center.z, center.x, center.y, center.z);
            }
            visibility = MathHelper.lerp(fadeInterpolation, visibility, 1.0F);
        } else {
            visibility = MathHelper.lerp(fadeInterpolation, visibility, 0.0F);
        }

        if (animatedBox != null && targetBox != null) {
            animatedBox = lerpBox(animatedBox, targetBox, interpolation);
        }

        if (animatedBox == null || visibility <= 0.01F) {
            if (nextTarget == null) {
                animatedBox = null;
                targetBox = null;
            }
            return;
        }

        Box renderBox = animatedBox.expand(0.002D);
        if (fill.isEnabled() && PLASMA_SHADER.isLoaded()) {
            renderPlasma(event, renderBox, visibility);
        }
        if (outline.isEnabled()) {
            ColorRGBA color = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
            int outlineColor = color.mulAlpha(MathHelper.clamp(visibility, 0.0F, 1.0F)).getRGB();
            Render3DUtil.drawBox(
                    renderBox,
                    outlineColor,
                    lineWidth.getCurrent(),
                    true,
                    false,
                    !throughWalls.isEnabled()
            );
        }
    }

    private Box getTargetBox() {
        if (!(mc.crosshairTarget instanceof BlockHitResult hitResult)
                || hitResult.getType() != HitResult.Type.BLOCK) {
            return null;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = mc.world.getBlockState(pos);
        if (state.isAir()) {
            return null;
        }

        VoxelShape shape = state.getOutlineShape(mc.world, pos, ShapeContext.of(mc.player));
        if (shape.isEmpty()) {
            return new Box(
                    pos.getX(), pos.getY(), pos.getZ(),
                    pos.getX() + 1.0D, pos.getY() + 1.0D, pos.getZ() + 1.0D
            );
        }
        return shape.getBoundingBox().offset(pos);
    }

    private void renderPlasma(EventRender3D event, Box box, float fade) {
        Camera camera = mc.gameRenderer.getCamera();
        Vec3d cameraPos = camera.getPos();
        Matrix4f matrix = event.getMatrix().peek().getPositionMatrix();
        ColorRGBA first = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor();
        ColorRGBA second = Wyvern.getInstance().getThemeManager().getCurrentTheme().getSecondColor();
        float shaderAlpha = alpha.getCurrent() * MathHelper.clamp(fade, 0.0F, 1.0F);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();
        if (throughWalls.isEnabled()) {
            RenderSystem.disableDepthTest();
        } else {
            RenderSystem.enableDepthTest();
        }

        PLASMA_SHADER.use();
        setUniform(PLASMA_SHADER, "u_Color",
                first.getRed() / 255.0F,
                first.getGreen() / 255.0F,
                first.getBlue() / 255.0F,
                shaderAlpha);
        setUniform(PLASMA_SHADER, "u_Color2",
                second.getRed() / 255.0F,
                second.getGreen() / 255.0F,
                second.getBlue() / 255.0F,
                shaderAlpha);
        setUniform(PLASMA_SHADER, "u_Resolution",
                mc.getWindow().getFramebufferWidth(),
                mc.getWindow().getFramebufferHeight());
        setUniform(PLASMA_SHADER, "u_Scale", plasmaScale.getCurrent());
        setUniform(PLASMA_SHADER, "u_Time",
                (System.nanoTime() - animationStartNanos) / 1_000_000_000.0F * plasmaSpeed.getCurrent());
        setUniform(PLASMA_SHADER, "u_Fov", (float)mc.options.getFov().getValue().intValue());
        setUniform(PLASMA_SHADER, "u_CameraDir",
                (float)Math.toRadians(-camera.getYaw()),
                (float)Math.toRadians(camera.getPitch()));

        BufferBuilder buffer = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION);
        appendBox(buffer, matrix, box, cameraPos);
        BufferRenderer.drawWithGlobalProgram(buffer.end());

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);
    }

    private static void appendBox(BufferBuilder buffer, Matrix4f matrix, Box box, Vec3d camera) {
        float x1 = (float)(box.minX - camera.x);
        float y1 = (float)(box.minY - camera.y);
        float z1 = (float)(box.minZ - camera.z);
        float x2 = (float)(box.maxX - camera.x);
        float y2 = (float)(box.maxY - camera.y);
        float z2 = (float)(box.maxZ - camera.z);

        quad(buffer, matrix, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2);
        quad(buffer, matrix, x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1);
        quad(buffer, matrix, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1);
        quad(buffer, matrix, x1, y1, z2, x2, y1, z2, x2, y2, z2, x1, y2, z2);
        quad(buffer, matrix, x1, y1, z1, x1, y1, z2, x1, y2, z2, x1, y2, z1);
        quad(buffer, matrix, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2);
    }

    private static void quad(
            BufferBuilder buffer,
            Matrix4f matrix,
            float x1, float y1, float z1,
            float x2, float y2, float z2,
            float x3, float y3, float z3,
            float x4, float y4, float z4
    ) {
        buffer.vertex(matrix, x1, y1, z1);
        buffer.vertex(matrix, x2, y2, z2);
        buffer.vertex(matrix, x3, y3, z3);
        buffer.vertex(matrix, x4, y4, z4);
    }

    private static Box lerpBox(Box from, Box to, float delta) {
        return new Box(
                MathHelper.lerp(delta, from.minX, to.minX),
                MathHelper.lerp(delta, from.minY, to.minY),
                MathHelper.lerp(delta, from.minZ, to.minZ),
                MathHelper.lerp(delta, from.maxX, to.maxX),
                MathHelper.lerp(delta, from.maxY, to.maxY),
                MathHelper.lerp(delta, from.maxZ, to.maxZ)
        );
    }

    private static void setUniform(GlProgram program, String name, float value) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(value);
        }
    }

    private static void setUniform(GlProgram program, String name, float x, float y) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(x, y);
        }
    }

    private static void setUniform(GlProgram program, String name, float x, float y, float z, float w) {
        GlUniform uniform = program.findUniform(name);
        if (uniform != null) {
            uniform.set(x, y, z, w);
        }
    }

    private void resetAnimation() {
        animatedBox = null;
        targetBox = null;
        lastFrameNanos = 0L;
        visibility = 0.0F;
    }
}
