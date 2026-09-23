package wtf.wyvern.client.modules.impl.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.utility.interfaces.IMinecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@ModuleAnnotation(
        name = "Svetych",
        category = Category.RENDER,
        description = "Светящиеся летающие кубы с физикой и отражением от блоков"
)
public final class Svetych extends Module implements IMinecraft {
    public static final Svetych INSTANCE = new Svetych();

    private static final Identifier OUTER_GLOW = Wyvern.id("trail/dashtrail/dashbloom.png");
    private static final Identifier INNER_GLOW = Wyvern.id("trail/dashtrail/dashbloomsample.png");
    private static final long SPAWN_INTERVAL = 200L;
    private static final long VISIBLE_LIFETIME = 7000L;
    private static final long FADE_DURATION = 1200L;

    private final SliderSetting cubeCount = new SliderSetting(
            "Кол кубиков", 100.0F, 50.0F, 300.0F, 1.0F);
    private final List<Particle> particles = new ArrayList<>();
    private long lastSpawn;

    private Svetych() {
    }

    @Override
    public void onEnable() {
        lastSpawn = System.currentTimeMillis();
        super.onEnable();
    }

    @Override
    public void onDisable() {
        particles.clear();
        super.onDisable();
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) {
            particles.clear();
            return;
        }

        long now = System.currentTimeMillis();
        if (particles.size() < Math.round(cubeCount.getCurrent()) && now - lastSpawn >= SPAWN_INTERVAL) {
            particles.add(new Particle(mc.player.getPos(), mc.player.getHeight(), now));
            lastSpawn = now;
        }

        for (Particle particle : particles) {
            particle.update(now);
        }
        particles.removeIf(Particle::shouldRemove);
    }

    @EventTarget
    private void onRender3D(EventRender3D event) {
        if (particles.isEmpty() || mc.world == null) return;

        MatrixStack matrices = event.getMatrix();
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        float partialTicks = event.getPartialTicks();
        long now = System.currentTimeMillis();
        float globalRotation = (now % 9000L) / 9000.0F * 360.0F;
        ColorRGBA baseColor = Wyvern.getInstance().getThemeManager().getClientColor(0);

        RenderSystem.enableBlend();
        RenderSystem.blendFunc(770, 1);
        RenderSystem.disableCull();
        RenderSystem.enableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        BufferBuilder cubes = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        for (Particle particle : particles) {
            float alpha = particle.alpha();
            if (alpha <= 0.001F) continue;
            matrices.push();
            matrices.translate(particle.renderX(partialTicks) - camera.x,
                    particle.renderY(partialTicks) - camera.y,
                    particle.renderZ(partialTicks) - camera.z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(globalRotation + particle.phase));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(globalRotation * 0.5F));
            drawCube(cubes, matrices.peek().getPositionMatrix(),
                    baseColor.withAlpha((int) (51.0F * alpha)).getRGB(), 0.26F);
            matrices.pop();
        }
        BufferRenderer.drawWithGlobalProgram(cubes.end());

        BufferBuilder lines = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.DEBUG_LINES, VertexFormats.POSITION_COLOR);
        for (Particle particle : particles) {
            float alpha = particle.alpha();
            if (alpha <= 0.001F) continue;
            matrices.push();
            matrices.translate(particle.renderX(partialTicks) - camera.x,
                    particle.renderY(partialTicks) - camera.y,
                    particle.renderZ(partialTicks) - camera.z);
            matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(globalRotation + particle.phase));
            matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(globalRotation * 0.5F));
            drawLines(lines, matrices.peek().getPositionMatrix(),
                    baseColor.withAlpha((int) (102.0F * alpha)).getRGB(), 0.26F);
            matrices.pop();
        }
        BufferRenderer.drawWithGlobalProgram(lines.end());

        drawGlowPass(matrices, camera, baseColor, OUTER_GLOW, 1.56F, 80.0F, partialTicks);
        drawGlowPass(matrices, camera, baseColor, INNER_GLOW, 0.52F, 140.0F, partialTicks);

        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void drawGlowPass(MatrixStack matrices, Vec3d camera, ColorRGBA baseColor,
                              Identifier texture, float size, float alphaScale, float partialTicks) {
        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        RenderSystem.setShaderTexture(0, texture);
        BufferBuilder glow = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        float half = size * 0.5F;

        for (Particle particle : particles) {
            float alpha = particle.alpha();
            if (alpha <= 0.001F) continue;
            matrices.push();
            matrices.translate(particle.renderX(partialTicks) - camera.x,
                    particle.renderY(partialTicks) - camera.y,
                    particle.renderZ(partialTicks) - camera.z);
            matrices.multiply(mc.gameRenderer.getCamera().getRotation());
            Matrix4f matrix = matrices.peek().getPositionMatrix();
            int color = baseColor.withAlpha((int) (alphaScale * alpha)).getRGB();
            glow.vertex(matrix, -half, half, 0.0F).texture(0.0F, 1.0F).color(color);
            glow.vertex(matrix, half, half, 0.0F).texture(1.0F, 1.0F).color(color);
            glow.vertex(matrix, half, -half, 0.0F).texture(1.0F, 0.0F).color(color);
            glow.vertex(matrix, -half, -half, 0.0F).texture(0.0F, 0.0F).color(color);
            matrices.pop();
        }
        BufferRenderer.drawWithGlobalProgram(glow.end());
    }

    private static void drawCube(BufferBuilder buffer, Matrix4f matrix, int color, float size) {
        float half = size * 0.5F;
        buffer.vertex(matrix, -half, half, -half).color(color);
        buffer.vertex(matrix, -half, half, half).color(color);
        buffer.vertex(matrix, half, half, half).color(color);
        buffer.vertex(matrix, half, half, -half).color(color);
        buffer.vertex(matrix, -half, -half, -half).color(color);
        buffer.vertex(matrix, half, -half, -half).color(color);
        buffer.vertex(matrix, half, -half, half).color(color);
        buffer.vertex(matrix, -half, -half, half).color(color);
        buffer.vertex(matrix, -half, half, half).color(color);
        buffer.vertex(matrix, -half, -half, half).color(color);
        buffer.vertex(matrix, half, -half, half).color(color);
        buffer.vertex(matrix, half, half, half).color(color);
        buffer.vertex(matrix, -half, half, -half).color(color);
        buffer.vertex(matrix, half, half, -half).color(color);
        buffer.vertex(matrix, half, -half, -half).color(color);
        buffer.vertex(matrix, -half, -half, -half).color(color);
        buffer.vertex(matrix, -half, half, -half).color(color);
        buffer.vertex(matrix, -half, -half, -half).color(color);
        buffer.vertex(matrix, -half, -half, half).color(color);
        buffer.vertex(matrix, -half, half, half).color(color);
        buffer.vertex(matrix, half, half, -half).color(color);
        buffer.vertex(matrix, half, half, half).color(color);
        buffer.vertex(matrix, half, -half, half).color(color);
        buffer.vertex(matrix, half, -half, -half).color(color);
    }

    private static void drawLines(BufferBuilder buffer, Matrix4f matrix, int color, float size) {
        float half = size * 0.5F;
        line(buffer, matrix, -half, -half, -half, half, -half, -half, color);
        line(buffer, matrix, half, -half, -half, half, -half, half, color);
        line(buffer, matrix, half, -half, half, -half, -half, half, color);
        line(buffer, matrix, -half, -half, half, -half, -half, -half, color);
        line(buffer, matrix, -half, half, -half, half, half, -half, color);
        line(buffer, matrix, half, half, -half, half, half, half, color);
        line(buffer, matrix, half, half, half, -half, half, half, color);
        line(buffer, matrix, -half, half, half, -half, half, -half, color);
        line(buffer, matrix, -half, -half, -half, -half, half, -half, color);
        line(buffer, matrix, half, -half, -half, half, half, -half, color);
        line(buffer, matrix, half, -half, half, half, half, half, color);
        line(buffer, matrix, -half, -half, half, -half, half, half, color);
    }

    private static void line(BufferBuilder buffer, Matrix4f matrix, float x1, float y1, float z1,
                             float x2, float y2, float z2, int color) {
        buffer.vertex(matrix, x1, y1, z1).color(color);
        buffer.vertex(matrix, x2, y2, z2).color(color);
    }

    private static final class Particle {
        private double x;
        private double y;
        private double z;
        private double previousX;
        private double previousY;
        private double previousZ;
        private double velocityX;
        private double velocityY;
        private double velocityZ;
        private final long bornAt;
        private final float phase;
        private final Animation animation = new Animation(FADE_DURATION, Easing.CUBIC_OUT);
        private boolean fading;

        private Particle(Vec3d playerPos, float playerHeight, long now) {
            ThreadLocalRandom random = ThreadLocalRandom.current();
            double radius = 2.0D + random.nextDouble() * 3.0D;
            double angle = random.nextDouble() * Math.PI * 2.0D;
            this.x = playerPos.x + Math.cos(angle) * radius;
            this.z = playerPos.z + Math.sin(angle) * radius;
            this.y = playerPos.y + 2.0D + random.nextDouble() * (playerHeight + 2.0D);
            this.previousX = this.x;
            this.previousY = this.y;
            this.previousZ = this.z;
            this.velocityX = (random.nextDouble() - 0.5D) * 0.06D;
            this.velocityY = (random.nextDouble() - 0.5D) * 0.06D;
            this.velocityZ = (random.nextDouble() - 0.5D) * 0.06D;
            this.bornAt = now;
            this.phase = random.nextFloat() * 100.0F;
            this.animation.update(1.0F);
        }

        private void update(long now) {
            previousX = x;
            previousY = y;
            previousZ = z;
            moveWithCollision();
            velocityX *= 0.99D;
            velocityY *= 0.99D;
            velocityZ *= 0.99D;
            if (!fading && now - bornAt > VISIBLE_LIFETIME) {
                fading = true;
            }
            animation.update(fading ? 0.0F : 1.0F);
        }

        private double renderX(float partialTicks) {
            return previousX + (x - previousX) * partialTicks;
        }

        private double renderY(float partialTicks) {
            return previousY + (y - previousY) * partialTicks;
        }

        private double renderZ(float partialTicks) {
            return previousZ + (z - previousZ) * partialTicks;
        }

        private void moveWithCollision() {
            if (isSolid(x + velocityX, y, z)) velocityX *= -0.8D;
            else x += velocityX;
            if (isSolid(x, y + velocityY, z)) velocityY *= -0.8D;
            else y += velocityY;
            if (isSolid(x, y, z + velocityZ)) velocityZ *= -0.8D;
            else z += velocityZ;
        }

        private boolean isSolid(double x, double y, double z) {
            if (mc.world == null) return false;
            BlockPos pos = BlockPos.ofFloored(x, y, z);
            return mc.world.getBlockState(pos).isFullCube(mc.world, pos);
        }

        private float alpha() {
            return animation.getValue();
        }

        private boolean shouldRemove() {
            return fading && animation.getValue() <= 0.001F;
        }
    }
}
