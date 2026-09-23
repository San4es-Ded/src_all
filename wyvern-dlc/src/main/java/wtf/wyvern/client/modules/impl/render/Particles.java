package wtf.wyvern.client.modules.impl.render;

import wtf.wyvern.core.eventbus.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.entity.projectile.thrown.EnderPearlEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.opengl.GL11;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.utility.interfaces.IMinecraft;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "Particles", category = Category.RENDER, description = "Красивые частицы при различных действиях")
public class Particles extends Module implements IMinecraft {

    public static Particles INSTANCE = new Particles();
    private static final Identifier SPARK_1_TEXTURE = Identifier.of("wyvern", "icons/spark_1.png");
    private static final Identifier SPARK_2_TEXTURE = Identifier.of("wyvern", "icons/spark_2.png");
    private static final Identifier SPARK_3_TEXTURE = Identifier.of("wyvern", "icons/spark_3.png");
    private static final Identifier SPARKLE_TEXTURE = Identifier.of("wyvern", "icons/sparkle.png");
    private static final Identifier DOLLAR_TEXTURE = Identifier.of("wyvern", "icons/dollar.png");
    private static final Identifier BLOOM_TEXTURE = Identifier.of("wyvern", "icons/bloom.png");
    private static final Identifier GLOW_TEXTURE = Identifier.of("wyvern", "icons/glow.png");
    private static final Identifier SNOW_TEXTURE = Identifier.of("wyvern", "icons/snow.png");
    private static final Identifier STAR_TEXTURE = Identifier.of("wyvern", "icons/star.png");
    private static final int COMET_TRAIL_POINTS = 22;
    private static final long COMET_TRAIL_SAMPLE_NS = 22_000_000L;

    private final ModeSetting type = new ModeSetting("Тип частиц", "Звезда", "Снежинка", "Свечение", "Сияние", "Кометы");

    private final MultiBooleanSetting reason = new MultiBooleanSetting("Добавлять при",
            MultiBooleanSetting.Value.of("Бездействии", false),
            MultiBooleanSetting.Value.of("Беге", false),
            MultiBooleanSetting.Value.of("Ударе", true),
            MultiBooleanSetting.Value.of("Падении перла", false),
            MultiBooleanSetting.Value.of("Падении трезубца", false),
            MultiBooleanSetting.Value.of("Сносе тотема", true));

    private final SliderSetting count = new SliderSetting("Количество", 10f, 2f, 40f, 1f);

    private final SliderSetting size = new SliderSetting("Размер", 0.3f, 0.1f, 2.0f, 0.05f);

    private final BooleanSetting glow = new BooleanSetting("Свечение", true);

    private final ArrayList<ParticleData> particles = new ArrayList<>();
    private final ArrayList<ParticleData> renderParticles = new ArrayList<>();
    private final Random rnd = new Random();

    public Particles() {
    }

    @Override
    public void onDisable() {
        synchronized (particles) {
            particles.clear();
        }
        super.onDisable();
    }

    @FastNative
    private Identifier getTexture() {
        return switch (type.get()) {
            case "Сияние" -> SPARKLE_TEXTURE;
            case "Свечение" -> GLOW_TEXTURE;
            case "Снежинка" -> SNOW_TEXTURE;
            case "Звезда" -> STAR_TEXTURE;
            case "Кометы" -> BLOOM_TEXTURE;
            default -> STAR_TEXTURE;
        };
    }

    @FastNative
    private boolean isPositionInBlock(Vec3d position) {
        if (mc.world == null || mc.player == null) return true;
        BlockPos blockPos = BlockPos.ofFloored(position);
        if (mc.world.getBlockState(blockPos).isSolidBlock(mc.world, blockPos)) {
            return true;
        }
        RaycastContext context = new RaycastContext(
                new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getStandingEyeHeight(), mc.player.getZ()),
                position,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                mc.player
        );
        BlockHitResult result = mc.world.raycast(context);
        return result.getType() == HitResult.Type.BLOCK;
    }

    @FastNative
    private float random(float min, float max) {
        return min + rnd.nextFloat() * (max - min);
    }

    @FastNative
    private boolean isMoving() {
        return mc.player != null && (mc.player.input.movementForward != 0 || mc.player.input.movementSideways != 0);
    }

    @FastNative
    @EventTarget
    public void onAttack(EventAttack event) {
        if (mc.player == null || mc.world == null) return;
        if (reason.isEnable("Ударе")) {

            Entity target = event.getTarget();
            if (target != null) {
                for (int i = 0; i < 35; i++) {
                    double targetX = target.getX() + random(-0.4f, 0.4f);
                    double targetY = target.getY() + random(-0.4f, (float) target.getHeight() + 0.4f);
                    double targetZ = target.getZ() + random(-0.4f, 0.4f);

                    if (isPositionInBlock(new Vec3d(targetX, targetY, targetZ))) continue;

                    float baseMx = random(-0.8f, 0.8f) * 2.0f;
                    float baseMy = random(-0.25f, 1.4f);
                    float baseMz = random(-0.8f, 0.8f) * 2.0f;

                    Vec3d velocity = new Vec3d(baseMx * 0.075f, baseMy * 0.075f, baseMz * 0.075f);
                    long life = (long) random(1000, 1200);

                    int color = Wyvern.getInstance().getThemeManager().getClientColor(0).getRGB();
                    addParticle(targetX, targetY, targetZ, velocity, color, size.getCurrent(), life, 0.5f, 0.0007f);
                }
            }
        }
    }

    @FastNative
    @EventTarget
    public void onPacket(EventPacket e) {
        if (mc.world == null || mc.player == null) return;
        if (!reason.isEnable("Сносе тотема")) return;

        if (e.getPacket() instanceof EntityStatusS2CPacket packet) {
            if (packet.getStatus() == 35) {
                Entity entity = packet.getEntity(mc.world);
                if (entity != null) {
                    double centerX = entity.getX();
                    double centerY = entity.getY() + entity.getHeight() / 2.0;
                    double centerZ = entity.getZ();

                    for (int i = 0; i < 50; i++) {
                        double theta = rnd.nextDouble() * 2.0 * Math.PI;
                        double phi = rnd.nextDouble() * Math.PI;
                        double speed = (rnd.nextDouble() * 0.5 + 0.5) * 0.1;

                        double vx = Math.sin(phi) * Math.cos(theta) * speed;
                        double vy = Math.sin(phi) * Math.sin(theta) * speed;
                        double vz = Math.cos(phi) * speed;

                        double spawnX = centerX + random(-0.3f, 0.3f);
                        double spawnY = centerY + random(-0.3f, 0.3f);
                        double spawnZ = centerZ + random(-0.3f, 0.3f);

                        if (isPositionInBlock(new Vec3d(spawnX, spawnY, spawnZ))) continue;

                        int color = rnd.nextDouble() < 0.7 ? 0xFF00FF00 : 0xFFFFFF00;
                        long life = (long) random(1500, 2000);

                        addParticle(spawnX, spawnY, spawnZ, new Vec3d(vx, vy, vz), color, size.getCurrent(), life, 2.0f, 0.00005f);
                    }
                }
            }
        }
    }

    @EventTarget
    public void onUpdate(EventUpdate e) {
        if (mc.player == null || mc.world == null) return;

        int particleCount = (int) count.getCurrent();

        if (reason.isEnable("Бездействии")) {
            Vec3d base = new Vec3d(mc.player.getX(), mc.player.getY() + mc.player.getHeight() / 2.0, mc.player.getZ());

            for (int i = 0; i < particleCount; i++) {
                double distance = random(7, 35);
                double angle = Math.toRadians(random(0, 360));
                double height = random(-7, 25);

                double spawnX = base.x + Math.cos(angle) * distance;
                double spawnY = base.y + height;
                double spawnZ = base.z + Math.sin(angle) * distance;

                Vec3d spawnPos = new Vec3d(spawnX, spawnY, spawnZ);
                if (isPositionInBlock(spawnPos)) continue;

                long life = (long) random(1500, 2000);
                double speed = rnd.nextDouble() < 0.8 ? random(0.015f, 0.03f) : 0.125f;
                double phi = Math.toRadians(random(0, 360));

                Vec3d velocity = new Vec3d(
                        Math.cos(phi) * speed,
                        random((float) (-speed * 0.1f), (float) (speed * 0.1f)),
                        Math.sin(phi) * speed
                );

                int color = Wyvern.getInstance().getThemeManager().getClientColor(0).getRGB();
                addParticle(spawnX, spawnY, spawnZ, velocity, color, size.getCurrent(), life, 3.0f, 0.00005f);
            }
        }

        if (reason.isEnable("Беге") && isMoving()) {
            Vec3d motion = mc.player.getVelocity();
            double speed = Math.sqrt(motion.x * motion.x + motion.z * motion.z);

            Vec3d direction;
            if (speed < 0.01) {
                direction = mc.player.getRotationVector().multiply(-1);
            } else if (mc.player.isGliding()) {
                direction = motion.normalize().multiply(-1);
            } else {
                direction = new Vec3d(-motion.x / speed, 0, -motion.z / speed);
            }

            double distanceBehind = (mc.player.isGliding() ? 1.2 : 0.5) + (speed > 0.1 ? speed * 1.5 : 0);
            double offsetX = random(-0.35f, 0.35f);
            double offsetZ = random(-0.35f, 0.35f);

            double posX = mc.player.getX() + direction.x * distanceBehind + offsetX;
            double posY = mc.player.isGliding()
                    ? mc.player.getY() + mc.player.getHeight() / 2.0 + direction.y * distanceBehind + random(-0.35f, 0.35f)
                    : mc.player.getY() + random(0.2f, (float) mc.player.getHeight() + 0.1f);
            double posZ = mc.player.getZ() + direction.z * distanceBehind + offsetZ;

            if (!isPositionInBlock(new Vec3d(posX, posY, posZ))) {
                double baseSpeed = 0.075;
                Vec3d velocity = direction.multiply(baseSpeed).add(
                        random(-0.01f, 0.01f),
                        random(-0.05f, 0.01f),
                        random(-0.01f, 0.01f)
                ).multiply(0.1);

                long life = (long) random(1500, 2000);
                int color = Wyvern.getInstance().getThemeManager().getClientColor(0).getRGB();
                addParticle(posX, posY, posZ, velocity, color, size.getCurrent(), life, 3.0f, 0.00005f);
            }
        }

        boolean trackPearls = reason.isEnable("Падении перла");
        boolean trackTridents = reason.isEnable("Падении трезубца");
        if (trackPearls || trackTridents) {
            Box searchBox = mc.player.getBoundingBox().expand(100);
            List<Entity> entities = mc.world.getOtherEntities(null, searchBox, e2 -> true);

            for (Entity entity : entities) {
                if (trackPearls && entity instanceof EnderPearlEntity pearl) {
                    if (!pearl.isOnGround()) {
                        createProjectileParticles(pearl.getPos(), 1);
                    }
                }

                if (trackTridents && entity instanceof TridentEntity trident) {
                    if (trident.getVelocity().lengthSquared() > 0.01) {
                        createProjectileParticles(trident.getPos(), 1);
                    }
                }
            }
        }
    }

    @FastNative
    private void createProjectileParticles(Vec3d position, int cnt) {
        int particleColor = Wyvern.getInstance().getThemeManager().getClientColor(0).getRGB();

        for (int i = 0; i < cnt * 2.5; i++) {
            double dy = random(0.1f, 0.35f);
            Vec3d particlePos = new Vec3d(position.x, position.y + dy, position.z);

            if (isPositionInBlock(particlePos)) continue;

            float speedMin = random(0.015f, 0.0375f);
            float speedMax = random(0.05f, 0.075f);
            double speedFinal = random(speedMin, speedMax);
            double speedFinalY = speedFinal * 0.4;

            double angleVel = Math.toRadians(random(0, 360));

            Vec3d velocity = new Vec3d(
                    Math.cos(angleVel) * speedFinal,
                    random((float) -speedFinalY, (float) speedFinalY),
                    Math.sin(angleVel) * speedFinal
            );

            long life = (long) random(2400, 2800);
            addParticle(particlePos.x, particlePos.y, particlePos.z, velocity, particleColor, size.getCurrent(), life, 2.0f, 0.00005f);
        }
    }

    private void addParticle(double x, double y, double z, Vec3d velocity, int color, float size, long lifeTime, float smooth, double gravity) {
        if (ParticleData.checkCollision(x, y, z, size, mc)) {
            synchronized (particles) {
                particles.add(new ParticleData(x, y, z, velocity, color, size, lifeTime, smooth, gravity));
            }
        }
    }

    @EventTarget
    public void onRender3D(EventRender3D e) {
        if (mc.player == null || mc.world == null) return;

        long nowMs = System.currentTimeMillis();
        synchronized (particles) {
            particles.removeIf(particle -> particle.isDead(nowMs));
        }

        if (particles.isEmpty()) return;

        MatrixStack matrices = e.getMatrix();
        Vec3d camera = mc.gameRenderer.getCamera().getPos();
        boolean renderComets = type.is("Кометы");

        RenderSystem.enableBlend();
        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.disableCull();

        if (renderComets || glow.isEnabled()) {
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE);
        } else {
            RenderSystem.defaultBlendFunc();
        }

        synchronized (particles) {
            renderParticles.clear();
            renderParticles.addAll(particles);
        }

        Quaternionf cameraYaw = RotationAxis.POSITIVE_Y.rotationDegrees(-mc.gameRenderer.getCamera().getYaw());
        Quaternionf cameraPitch = RotationAxis.POSITIVE_X.rotationDegrees(mc.gameRenderer.getCamera().getPitch());
        long nowNs = System.nanoTime();
        for (ParticleData particle : renderParticles) {
            particle.update(mc, nowMs, nowNs, renderComets);
        }

        RenderSystem.setShader(ShaderProgramKeys.POSITION_TEX_COLOR);
        if (renderComets) {
            renderComets(matrices, camera, cameraYaw, cameraPitch, nowMs);
        } else {
            RenderSystem.setShaderTexture(0, getTexture());
            BufferBuilder buffer = Tessellator.getInstance().begin(
                    VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            for (ParticleData particle : renderParticles) {
                addBillboardQuad(buffer, matrices, camera, cameraYaw, cameraPitch,
                        new Vec3d(particle.x, particle.y, particle.z), particle.size,
                        particle.color, particle.alpha);
            }

            BufferRenderer.drawWithGlobalProgram(buffer.end());
        }

        RenderSystem.enableCull();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableBlend();
    }

    private void renderComets(MatrixStack matrices, Vec3d camera, Quaternionf cameraYaw,
                              Quaternionf cameraPitch, long nowMs) {
        // Soft additive pass: a round head followed by a continuous, tapered trail.
        RenderSystem.setShaderTexture(0, GLOW_TEXTURE);
        BufferBuilder glowBuffer = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (ParticleData particle : renderParticles) {
            Vec3d head = new Vec3d(particle.x, particle.y, particle.z);
            float headSize = MathHelper.clamp(particle.size * 0.82F, 0.14F, 0.38F);
            addBillboardQuad(glowBuffer, matrices, camera, cameraYaw, cameraPitch,
                    head, headSize * 2.75F, particle.color, particle.alpha * 0.24F);
            addBillboardQuad(glowBuffer, matrices, camera, cameraYaw, cameraPitch,
                    head, headSize * 1.55F, particle.color, particle.alpha * 0.58F);

            int points = particle.trail.size();
            if (points < 2) {
                Vec3d tail = particle.fallbackTail(head, headSize);
                addBillboardQuad(glowBuffer, matrices, camera, cameraYaw, cameraPitch,
                        tail, headSize * 0.28F, particle.color, particle.alpha * 0.22F);
            }
            for (int i = 1; i < points; i++) {
                float progress = i / (float) Math.max(points - 1, 1);
                float tail = 1.0F - progress;
                float size = headSize * (0.25F + tail * 0.68F);
                float alpha = particle.alpha * (float) Math.pow(tail, 1.45D) * 0.48F;
                addBillboardQuad(glowBuffer, matrices, camera, cameraYaw, cameraPitch,
                        particle.trail.get(i), size, particle.color, alpha);
            }
        }
        BufferRenderer.drawWithGlobalProgram(glowBuffer.end());

        // Crisp bloom pass: white-hot cores and small sparks falling from the tail.
        RenderSystem.setShaderTexture(0, BLOOM_TEXTURE);
        BufferBuilder bloomBuffer = Tessellator.getInstance().begin(
                VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        for (ParticleData particle : renderParticles) {
            Vec3d head = new Vec3d(particle.x, particle.y, particle.z);
            float headSize = MathHelper.clamp(particle.size * 0.82F, 0.14F, 0.38F);
            int hotColor = brighten(particle.color, 0.72F);
            addBillboardQuad(bloomBuffer, matrices, camera, cameraYaw, cameraPitch,
                    head, headSize * 0.92F, hotColor, particle.alpha * 0.92F);
            addBillboardQuad(bloomBuffer, matrices, camera, cameraYaw, cameraPitch,
                    head, headSize * 0.38F, 0xFFFFFFFF, particle.alpha);

            int points = particle.trail.size();
            if (points < 2) {
                Vec3d tail = particle.fallbackTail(head, headSize);
                addBillboardQuad(bloomBuffer, matrices, camera, cameraYaw, cameraPitch,
                        tail, headSize * 0.14F, hotColor, particle.alpha * 0.32F);
            }
            for (int i = 4; i < points; i += 4) {
                Vec3d source = particle.trail.get(i);
                double cycle = positiveFraction(nowMs * 0.00042D + particle.cometSeed + i * 0.173D);
                double sway = Math.sin(nowMs * 0.0021D + particle.cometSeed * 11.0D + i) * 0.09D * cycle;
                double drift = Math.cos(nowMs * 0.0017D + particle.cometSeed * 7.0D + i) * 0.07D * cycle;
                Vec3d fallingSpark = source.add(sway, -cycle * (0.38D + i * 0.018D), drift);
                float tailFade = 1.0F - i / (float) Math.max(points, 1) * 0.45F;
                float sparkAlpha = particle.alpha * (float) (1.0D - cycle) * tailFade * 0.72F;
                float sparkSize = headSize * (0.12F + (float) (1.0D - cycle) * 0.10F);
                addBillboardQuad(bloomBuffer, matrices, camera, cameraYaw, cameraPitch,
                        fallingSpark, sparkSize, hotColor, sparkAlpha);
            }
        }
        BufferRenderer.drawWithGlobalProgram(bloomBuffer.end());
    }

    private static void addBillboardQuad(BufferBuilder buffer, MatrixStack matrices, Vec3d camera,
                                         Quaternionf cameraYaw, Quaternionf cameraPitch, Vec3d position,
                                         float size, int color, float alpha) {
        if (alpha <= 0.001F || size <= 0.001F) return;

        matrices.push();
        matrices.translate(position.x - camera.x, position.y - camera.y, position.z - camera.z);
        matrices.multiply(cameraYaw);
        matrices.multiply(cameraPitch);
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        float half = size * 0.5F;
        int a = MathHelper.clamp((int) (alpha * 255.0F), 0, 255);
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;
        buffer.vertex(matrix, -half, -half, 0).texture(0, 1).color(r, g, b, a);
        buffer.vertex(matrix, -half, half, 0).texture(0, 0).color(r, g, b, a);
        buffer.vertex(matrix, half, half, 0).texture(1, 0).color(r, g, b, a);
        buffer.vertex(matrix, half, -half, 0).texture(1, 1).color(r, g, b, a);
        matrices.pop();
    }

    @FastNative
    private static int brighten(int color, float amount) {
        int r = color >> 16 & 255;
        int g = color >> 8 & 255;
        int b = color & 255;
        r += Math.round((255 - r) * amount);
        g += Math.round((255 - g) * amount);
        b += Math.round((255 - b) * amount);
        return 0xFF000000 | r << 16 | g << 8 | b;
    }

    @FastNative
    private static double positiveFraction(double value) {
        return value - Math.floor(value);
    }

    static class ParticleData {
        double x;
        double y;
        double z;
        double velocityX;
        double velocityY;
        double velocityZ;
        int color;
        float size;
        long lifeTime;
        long birthTime;
        float alpha = 1.0f;
        float smoothFactor;
        long lastUpdateNs;
        double gravity;
        final ArrayList<Vec3d> trail = new ArrayList<>(COMET_TRAIL_POINTS);
        final double cometSeed;
        long lastTrailSampleNs;
        boolean wasComet;
        private final BlockPos.Mutable collisionPos = new BlockPos.Mutable();

        ParticleData(double x, double y, double z, Vec3d velocity, int color, float size, long lifeTime, float smooth, double gravity) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.velocityX = velocity.x;
            this.velocityY = velocity.y;
            this.velocityZ = velocity.z;
            this.color = color;
            this.size = size;
            this.lifeTime = lifeTime;
            this.birthTime = System.currentTimeMillis();
            this.lastUpdateNs = System.nanoTime();
            this.lastTrailSampleNs = this.lastUpdateNs;
            this.smoothFactor = smooth;
            this.gravity = gravity;
            this.cometSeed = positiveFraction(x * 0.173D + y * 0.317D + z * 0.619D + birthTime * 0.0001D);
            this.trail.add(new Vec3d(x, y, z));
        }

        boolean isDead(long nowMs) {
            return nowMs - birthTime >= lifeTime;
        }

        void update(net.minecraft.client.MinecraftClient mc, long nowMs, long nowNs, boolean comet) {
            double deltaSec = Math.min((nowNs - lastUpdateNs) / 1_000_000_000.0, 0.05D);
            lastUpdateNs = nowNs;

            float progress = Math.min(1.0f, (float) (nowMs - birthTime) / lifeTime);
            double factor = Math.pow(1.0 - progress, smoothFactor);

            double vx = velocityX;
            double vy = velocityY;
            double vz = velocityZ;

            double newX = x;
            double newY = y;
            double newZ = z;

            if (comet) {
                double ageSeconds = (nowMs - birthTime) * 0.001D;
                double frameScale = deltaSec * 60.0D;
                // A tiny perpendicular drift makes comets float instead of following
                // perfectly straight, mechanical paths.
                newX += Math.sin(ageSeconds * 1.35D + cometSeed * MathHelper.TAU) * 0.0007D * frameScale;
                newY += Math.cos(ageSeconds * 1.10D + cometSeed * 5.0D) * 0.00045D * frameScale;
                newZ += Math.cos(ageSeconds * 1.25D + cometSeed * MathHelper.TAU) * 0.0007D * frameScale;
            }

            newX += vx * factor * (deltaSec * 60);
            if (!checkCollision(newX, y, z, size, mc, collisionPos)) {
                vx = -vx * 0.8;
                newX = x;
            }

            newY += vy * factor * (deltaSec * 60);
            if (!checkCollision(newX, newY, z, size, mc, collisionPos)) {
                vy = -vy * 1.5;
                newY = y;
            }

            newZ += vz * factor * (deltaSec * 60);
            if (!checkCollision(newX, newY, newZ, size, mc, collisionPos)) {
                vz = -vz * 0.8;
                newZ = z;
            }

            x = newX;
            y = newY;
            z = newZ;
            velocityX = vx * 0.9999;
            velocityY = vy * 0.9999 - gravity;
            velocityZ = vz * 0.9999;
            alpha = 1.0f - progress;

            updateCometTrail(nowNs, comet);
        }

        private void updateCometTrail(long nowNs, boolean comet) {
            if (!comet) {
                if (wasComet) {
                    trail.clear();
                    trail.add(new Vec3d(x, y, z));
                }
                wasComet = false;
                lastTrailSampleNs = nowNs;
                return;
            }

            if (!wasComet) {
                trail.clear();
                trail.add(new Vec3d(x, y, z));
                lastTrailSampleNs = nowNs;
                wasComet = true;
                return;
            }

            Vec3d newest = trail.isEmpty() ? null : trail.get(0);
            double distanceSquared = newest == null ? Double.MAX_VALUE
                    : newest.squaredDistanceTo(x, y, z);
            if (nowNs - lastTrailSampleNs >= COMET_TRAIL_SAMPLE_NS || distanceSquared >= 0.0036D) {
                trail.add(0, new Vec3d(x, y, z));
                while (trail.size() > COMET_TRAIL_POINTS) {
                    trail.remove(trail.size() - 1);
                }
                lastTrailSampleNs = nowNs;
            }
        }

        private Vec3d fallbackTail(Vec3d head, float headSize) {
            Vec3d velocity = new Vec3d(velocityX, velocityY, velocityZ);
            if (velocity.lengthSquared() < 1.0E-6D) {
                velocity = new Vec3d(0.0D, -0.1D, 0.0D);
            }
            double length = Math.max(0.08D, headSize * 1.35D);
            return head.subtract(velocity.normalize().multiply(length));
        }

        static boolean checkCollision(double x, double y, double z, float size, net.minecraft.client.MinecraftClient mc) {
            return checkCollision(x, y, z, size, mc, new BlockPos.Mutable());
        }

        private static boolean checkCollision(double x, double y, double z, float size, net.minecraft.client.MinecraftClient mc, BlockPos.Mutable pos) {
            if (mc.world == null) return false;
            double half = size * 0.5;
            int minX = MathHelper.floor(x - half);
            int maxX = MathHelper.floor(x + half);
            int minY = MathHelper.floor(y - half);
            int maxY = MathHelper.floor(y + half);
            int minZ = MathHelper.floor(z - half);
            int maxZ = MathHelper.floor(z + half);

            for (int bx = minX; bx <= maxX; bx++) {
                for (int by = minY; by <= maxY; by++) {
                    for (int bz = minZ; bz <= maxZ; bz++) {
                        pos.set(bx, by, bz);
                        BlockState state = mc.world.getBlockState(pos);
                        if (!state.isAir() && state.isSolidBlock(mc.world, pos)) {
                            return false;
                        }
                    }
                }
            }
            return true;
        }
    }
}
