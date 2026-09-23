package ru.prism.module.impl.render;

import com.mojang.blaze3d.pipeline.BlendFunction;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import com.mojang.blaze3d.platform.DepthTestFunction;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderSetup;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.BufferAllocator;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import ru.prism.Client;
import ru.prism.manager.event_impl.EventRender3D;
import ru.prism.manager.event_impl.EventTick;
import ru.prism.manager.event_impl.SoundPlayEvent;
import ru.prism.manager.events.orbit.EventHandler;
import ru.prism.module.api.Category;
import ru.prism.module.api.Module;
import ru.prism.module.api.ModuleInfo;
import ru.prism.module.api.settings.impl.BooleanSetting;
import ru.prism.module.api.settings.impl.ColorSetting;
import ru.prism.module.api.settings.impl.DelimiterSetting;
import ru.prism.module.api.settings.impl.SliderSetting;
import ru.prism.theme.ThemeColor;
import ru.prism.utils.colors.ColorUtil;

import java.util.ArrayList;
import java.util.List;

@ModuleInfo(
        name = "FT Helper",
        desc = "Показывает зоны донат-предметов: трапки, пласта, дезориентации, пыли и заморозки.",
        category = Category.VISUALS
)
public class FtHelper extends Module {

    private static final int GREEN = ColorUtil.getColor(0, 255, 0);
    private static final int FILL_ALPHA = 35;
    private static final float OUTLINE_GLOW = 0.035F;
    private static final int CIRCLE_SEGMENTS = 64;
    private static final int SNOWBALL_STEPS = 200;

    public final BooleanSetting trap = new BooleanSetting(this, "Трапка", true);
    public final BooleanSetting plast = new BooleanSetting(this, "Пласт", true);
    public final BooleanSetting disorientation = new BooleanSetting(this, "Дезориентация", true);
    public final BooleanSetting dust = new BooleanSetting(this, "Пыль", true);
    public final BooleanSetting freeze = new BooleanSetting(this, "Заморозка", true);
    public final DelimiterSetting trapGroup = new DelimiterSetting(this, "Настройки трапки");
    public final BooleanSetting actionTimer = new BooleanSetting(this, "Таймер действия", true);
    public final BooleanSetting dragonTrapZone = new BooleanSetting(this, "Зона драконьей трапки", false);
    public final DelimiterSetting freezeGroup = new DelimiterSetting(this, "Настройки снежка");
    public final SliderSetting lineWidth = new SliderSetting(this, "Толщина линий", 2.5F, 0.5F, 10.0F, 0.5F);
    public final DelimiterSetting colorGroup = new DelimiterSetting(this, "Цвет");
    public final BooleanSetting clientColor = new BooleanSetting(this, "Цвет клиента", true);
    public final ColorSetting customColor = new ColorSetting(this, "Кастомный цвет", ColorUtil.WHITE)
            .setVisible(() -> !clientColor.getValue());

    private final BufferAllocator allocator = new BufferAllocator(1 << 20);

    private boolean timerActive = false;
    private long timerStart = 0L;
    private long lastGrowl = 0L;
    private boolean wasCoolingDown = false;
    private boolean dragonTimer = false;

    @Override
    protected void onDisable() {
        timerActive = false;
        wasCoolingDown = false;
        dragonTimer = false;
    }

    @EventHandler
    public void onTick(EventTick event) {
        if (mc.player == null || mc.world == null) return;

        if (actionTimer.getValue()) {
            boolean cooling = mc.player.getItemCooldownManager().isCoolingDown(Items.NETHERITE_SCRAP.getDefaultStack());

            if (cooling && !wasCoolingDown) {
                long now = System.currentTimeMillis();
                timerActive = true;
                timerStart = now;
                dragonTimer = now - lastGrowl < 500L;
            }

            wasCoolingDown = cooling;
        }

        if (timerActive && actionTimer.getValue()) {
            updateTimer();
        }
    }

    @EventHandler
    public void onSound(SoundPlayEvent event) {
        if (event.getSound().getId().toString().equals("minecraft:entity.ender_dragon.growl")) {
            lastGrowl = System.currentTimeMillis();
        }
    }

    private void updateTimer() {
        long duration = dragonTimer ? 30000L : 15000L;
        long elapsed = System.currentTimeMillis() - timerStart;

        if (elapsed >= duration) {
            timerActive = false;
            dragonTimer = false;
            return;
        }

        mc.inGameHud.setOverlayMessage(
                Text.of(String.format("%s закончится через §b%.1f§f сек.",
                        dragonTimer ? "Драконья трапка" : "Трапка", (duration - elapsed) / 1000.0)),
                false
        );
    }

    @EventHandler
    public void onRender3D(EventRender3D event) {
        if (mc.player == null || mc.world == null || mc.gameRenderer == null) return;

        Item main = mc.player.getMainHandStack().getItem();
        Item off = mc.player.getOffHandStack().getItem();

        boolean scrap = trap.getValue() && (main == Items.NETHERITE_SCRAP || off == Items.NETHERITE_SCRAP);
        boolean kelp = plast.getValue() && (main == Items.DRIED_KELP || off == Items.DRIED_KELP);
        boolean eye = disorientation.getValue() && (main == Items.ENDER_EYE || off == Items.ENDER_EYE);
        boolean sugar = dust.getValue() && (main == Items.SUGAR || off == Items.SUGAR);
        boolean snowball = freeze.getValue() && (main == Items.SNOWBALL || off == Items.SNOWBALL);

        if (!scrap && !kelp && !eye && !sugar && !snowball) return;

        MatrixStack stack = event.getMatrixStack();
        VertexConsumerProvider.Immediate immediate = VertexConsumerProvider.immediate(allocator);
        Draw draw = new Draw(
                stack.peek().getPositionMatrix(),
                mc.gameRenderer.getCamera().getCameraPos()
        );

        float delta = event.getTickDelta();
        List<Vec3d> trajectory = snowball ? simulateSnowball(delta) : List.of();

        // ВАЖНО: immediate с одним аллокатором держит активным только один слой —
        // при getBuffer() другого слоя предыдущий флушится. Поэтому рендер идёт
        // проходами: сначала все заливки, потом все линии.
        draw.fill = immediate.getBuffer(FILL_LAYER);

        if (scrap) renderScrap(draw);
        if (kelp) renderKelp(draw);
        if (eye) renderCircle(draw, lerpedPlayer(delta));
        if (sugar) renderCircle(draw, lerpedPlayer(delta));
        if (snowball) renderLandingZone(draw, trajectory);

        draw.fill = null;
        draw.line = immediate.getBuffer(RenderLayers.linesTranslucent());

        if (scrap) renderScrap(draw);
        if (kelp) renderKelp(draw);
        if (eye) renderCircle(draw, lerpedPlayer(delta));
        if (sugar) renderCircle(draw, lerpedPlayer(delta));
        if (snowball) renderTrajectory(draw, trajectory);

        immediate.draw();
    }

    private void renderScrap(Draw draw) {
        BlockPos pos = mc.player.getBlockPos();
        Box box;

        if (dragonTrapZone.getValue()) {
            box = new Box(
                    pos.getX() - 3 + 0.01, pos.getY() + 0.01, pos.getZ() - 3 + 0.01,
                    pos.getX() + 4 - 0.01, pos.getY() + 6 - 0.01, pos.getZ() + 4 - 0.01
            );
        } else {
            box = new Box(
                    pos.getX() - 2 + 0.01, pos.getY() + 0.01, pos.getZ() - 2 + 0.01,
                    pos.getX() + 3 - 0.01, pos.getY() + 4 - 0.01, pos.getZ() + 3 - 0.01
            );
        }

        drawBox(draw, box, hasEnemy(box) ? GREEN : color());
    }

    private void renderKelp(Draw draw) {
        BlockPos pos = mc.player.getBlockPos();
        float yaw = mc.player.getYaw();
        float pitch = mc.player.getPitch();
        int base = color();
        boolean big = dragonTrapZone.getValue();

        if (Math.abs(pitch) > 45.0F) {
            Box box;

            if (pitch < -45.0F) {
                if (big) {
                    box = new Box(
                            pos.getX() - 3 + 0.01, pos.getY() + 3 + 0.01, pos.getZ() - 3 + 0.01,
                            pos.getX() + 4 - 0.01, pos.getY() + 7 - 0.01, pos.getZ() + 4 - 0.01
                    );
                } else {
                    box = new Box(
                            pos.getX() - 2 + 0.01, pos.getY() + 3 + 0.01, pos.getZ() - 2 + 0.01,
                            pos.getX() + 3 - 0.01, pos.getY() + 5 - 0.01, pos.getZ() + 3 - 0.01
                    );
                }
            } else if (big) {
                box = new Box(
                        pos.getX() - 3 + 0.01, pos.getY() - 3 + 0.01, pos.getZ() - 3 + 0.01,
                        pos.getX() + 4 - 0.01, pos.getY() - 1 - 0.01, pos.getZ() + 4 - 0.01
                );
            } else {
                box = new Box(
                        pos.getX() - 2 + 0.01, pos.getY() - 3 + 0.01, pos.getZ() - 2 + 0.01,
                        pos.getX() + 3 - 0.01, pos.getY() - 1 - 0.01, pos.getZ() + 3 - 0.01
                );
            }

            drawBox(draw, box, hasEnemy(box) ? GREEN : base);
            return;
        }

        float f = (yaw % 360.0F + 360.0F) % 360.0F;

        if (Math.abs(f - 45.0F) < 22.0F || Math.abs(f - 135.0F) < 22.0F
                || Math.abs(f - 225.0F) < 22.0F || Math.abs(f - 315.0F) < 22.0F) {
            renderKelpDiagonal(draw, pos, f, base, big);
            return;
        }

        boolean axisX;
        BlockPos center;

        if (f >= 315.0F || f < 45.0F) {
            center = pos.add(0, -1, 3);
            axisX = true;
        } else if (f >= 45.0F && f < 135.0F) {
            center = pos.add(-3, -1, 0);
            axisX = false;
        } else if (f >= 135.0F && f < 225.0F) {
            center = pos.add(0, -1, -3);
            axisX = true;
        } else {
            center = pos.add(3, -1, 0);
            axisX = false;
        }

        Box box;

        if (axisX) {
            if (big) {
                box = new Box(
                        center.getX() - 3 + 0.01, center.getY() + 0.01, center.getZ() - 1 + 0.01,
                        center.getX() + 4 - 0.01, center.getY() + 7 - 0.01, center.getZ() + 2 - 0.01
                );
            } else {
                box = new Box(
                        center.getX() - 2 + 0.01, center.getY() + 0.01, center.getZ() - 1 + 0.01,
                        center.getX() + 3 - 0.01, center.getY() + 5 - 0.01, center.getZ() + 2 - 0.01
                );
            }
        } else if (big) {
            box = new Box(
                    center.getX() - 1 + 0.01, center.getY() + 0.01, center.getZ() - 3 + 0.01,
                    center.getX() + 2 - 0.01, center.getY() + 7 - 0.01, center.getZ() + 4 - 0.01
            );
        } else {
            box = new Box(
                    center.getX() - 1 + 0.01, center.getY() + 0.01, center.getZ() - 2 + 0.01,
                    center.getX() + 2 - 0.01, center.getY() + 5 - 0.01, center.getZ() + 3 - 0.01
            );
        }

        drawBox(draw, box, hasEnemy(box) ? GREEN : base);
    }

    private void renderKelpDiagonal(Draw draw, BlockPos pos, float f, int base, boolean big) {
        float d45 = Math.abs(f - 45.0F);
        float d135 = Math.abs(f - 135.0F);
        float d225 = Math.abs(f - 225.0F);
        float d315 = Math.abs(f - 315.0F);
        float min = Math.min(Math.min(d45, d135), Math.min(d225, d315));
        int near = min == d45 ? 45 : (min == d135 ? 135 : (min == d225 ? 225 : 315));
        int offsetX = near != 45 && near != 135 ? 2 : -3;
        int offsetZ = near != 45 && near != 315 ? -2 : 2;
        BlockPos center = pos.add(offsetX, -1, offsetZ);

        List<Box> boxes = new ArrayList<>();
        int lo = big ? -3 : -2;
        int hi = big ? 3 : 2;
        int height = big ? 7 : 5;

        if (near != 45 && near != 225) {
            for (int x = lo; x <= hi; x++) {
                for (int y = 0; y < height; y++) {
                    boxes.add(new Box(center.add(x, y, -x)));
                    boxes.add(new Box(center.add(x + 1, y, -x)));
                }
            }
        } else {
            for (int x = lo; x <= hi; x++) {
                for (int y = 0; y < height; y++) {
                    boxes.add(new Box(center.add(x, y, x)));
                    boxes.add(new Box(center.add(x + 1, y, x)));
                }
            }
        }

        boolean anyInTrap = false;

        for (Box box : boxes) {
            if (hasEnemy(box)) {
                anyInTrap = true;
                break;
            }
        }

        int finalColor = anyInTrap ? GREEN : base;

        for (Box box : boxes) {
            drawBox(draw, box, finalColor);
        }
    }

    private void renderCircle(Draw draw, Vec3d center) {
        int argb = hasEnemyNear(center, 10.0) ? GREEN : color();
        drawCircle(draw, center, 10.0F, 0.05F, argb);
    }

    private void renderLandingZone(Draw draw, List<Vec3d> points) {
        if (points.size() < 2) return;

        Vec3d land = points.get(points.size() - 1);
        int argb = hasEnemyNear(land, 3.0) ? GREEN : color();
        drawCircle(draw, land, 1.8F, 0.05F, argb);
    }

    private void renderTrajectory(Draw draw, List<Vec3d> points) {
        if (points.size() < 2) return;

        int argb = color();
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        int a = alpha(argb);

        int last = points.size() - 1;
        for (int i = 0; i < last; i++) {
            float t = (i + 1) / (float) last;
            int segmentAlpha = (int) (a * (0.2F + 0.8F * t));
            stroke(draw, points.get(i), points.get(i + 1), r, g, b, segmentAlpha);
        }
    }

    private List<Vec3d> simulateSnowball(float delta) {
        List<Vec3d> points = new ArrayList<>();
        Vec3d pos = mc.player.getCameraPosVec(delta);
        Vec3d velocity = lookVector(mc.player.getYaw(), mc.player.getPitch())
                .multiply(1.5)
                .add(0.0, mc.player.getVelocity().y * 0.5, 0.0);
        points.add(pos);

        for (int i = 0; i < SNOWBALL_STEPS; i++) {
            Vec3d previous = pos;
            pos = pos.add(velocity);

            BlockHitResult hit = mc.world.raycast(new RaycastContext(
                    previous, pos, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));

            if (hit.getType() != HitResult.Type.MISS) {
                points.add(hit.getPos());
                break;
            }

            if (pos.y < mc.world.getBottomY()) {
                points.add(pos);
                break;
            }

            points.add(pos);
            velocity = velocity.multiply(0.99).subtract(0.0, 0.03, 0.0);
        }

        return points;
    }

    private static Vec3d lookVector(float yaw, float pitch) {
        float yawRad = yaw * 0.017453292F;
        float pitchRad = pitch * 0.017453292F;
        float cosPitch = MathHelper.cos(pitchRad);
        return new Vec3d(
                -MathHelper.sin(yawRad) * cosPitch,
                -MathHelper.sin(pitchRad),
                MathHelper.cos(yawRad) * cosPitch
        ).normalize();
    }

    private Vec3d lerpedPlayer(float delta) {
        return new Vec3d(
                MathHelper.lerp(delta, mc.player.lastRenderX, mc.player.getX()),
                MathHelper.lerp(delta, mc.player.lastRenderY, mc.player.getY()),
                MathHelper.lerp(delta, mc.player.lastRenderZ, mc.player.getZ())
        );
    }

    private int color() {
        if (clientColor.getValue()) {
            return ThemeColor.getVisualColor();
        }
        return customColor.getValue();
    }

    private static int alpha(int argb) {
        int a = (argb >>> 24) & 0xFF;
        return a == 0 ? 255 : a;
    }

    private boolean hasEnemy(Box box) {
        Box expanded = box.expand(0.5);

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (Client.get().friendManager().isFriend(player.getName().getString())) continue;
            if (expanded.intersects(player.getBoundingBox())) return true;
        }

        return false;
    }

    private boolean hasEnemyNear(Vec3d center, double distance) {
        double squared = distance * distance;

        for (AbstractClientPlayerEntity player : mc.world.getPlayers()) {
            if (player == mc.player) continue;
            if (Client.get().friendManager().isFriend(player.getName().getString())) continue;
            if (player.squaredDistanceTo(center) <= squared) return true;
        }

        return false;
    }

    private void drawBox(Draw draw, Box box, int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        int a = alpha(argb);
        double x1 = box.minX;
        double y1 = box.minY;
        double z1 = box.minZ;
        double x2 = box.maxX;
        double y2 = box.maxY;
        double z2 = box.maxZ;

        // Заливка с градиентом: у земли плотнее, к верху затухает. Нижняя грань не рисуется.
        int top = Math.max(4, (int) (FILL_ALPHA * 0.3F));

        fillQuad(draw, x1, y2, z1, x2, y2, z1, x2, y2, z2, x1, y2, z2, r, g, b, top);
        fillQuadGrad(draw, x1, y1, z1, x2, y1, z1, x2, y2, z1, x1, y2, z1, r, g, b, FILL_ALPHA, top);
        fillQuadGrad(draw, x2, y1, z2, x1, y1, z2, x1, y2, z2, x2, y2, z2, r, g, b, FILL_ALPHA, top);
        fillQuadGrad(draw, x1, y1, z2, x1, y1, z1, x1, y2, z1, x1, y2, z2, r, g, b, FILL_ALPHA, top);
        fillQuadGrad(draw, x2, y1, z1, x2, y1, z2, x2, y2, z2, x2, y2, z1, r, g, b, FILL_ALPHA, top);

        // Обводка: мягкое свечение вокруг контура + чёткая линия поверх.
        outlineGlow(draw, box, r, g, b, Math.max(1, (int) (a * 0.14F)), OUTLINE_GLOW * lineScale());

        stroke(draw, new Vec3d(x1, y1, z1), new Vec3d(x2, y1, z1), r, g, b, a);
        stroke(draw, new Vec3d(x2, y1, z1), new Vec3d(x2, y1, z2), r, g, b, a);
        stroke(draw, new Vec3d(x2, y1, z2), new Vec3d(x1, y1, z2), r, g, b, a);
        stroke(draw, new Vec3d(x1, y1, z2), new Vec3d(x1, y1, z1), r, g, b, a);
        stroke(draw, new Vec3d(x1, y2, z1), new Vec3d(x2, y2, z1), r, g, b, a);
        stroke(draw, new Vec3d(x2, y2, z1), new Vec3d(x2, y2, z2), r, g, b, a);
        stroke(draw, new Vec3d(x2, y2, z2), new Vec3d(x1, y2, z2), r, g, b, a);
        stroke(draw, new Vec3d(x1, y2, z2), new Vec3d(x1, y2, z1), r, g, b, a);
        stroke(draw, new Vec3d(x1, y1, z1), new Vec3d(x1, y2, z1), r, g, b, a);
        stroke(draw, new Vec3d(x2, y1, z1), new Vec3d(x2, y2, z1), r, g, b, a);
        stroke(draw, new Vec3d(x2, y1, z2), new Vec3d(x2, y2, z2), r, g, b, a);
        stroke(draw, new Vec3d(x1, y1, z2), new Vec3d(x1, y2, z2), r, g, b, a);
    }

    private void outlineGlow(Draw draw, Box box, int r, int g, int b, int a, double glow) {
        double x1 = box.minX;
        double y1 = box.minY;
        double z1 = box.minZ;
        double x2 = box.maxX;
        double y2 = box.maxY;
        double z2 = box.maxZ;

        beam(draw, x1, y1, z1, x2, y1, z1, r, g, b, a, glow);
        beam(draw, x2, y1, z1, x2, y1, z2, r, g, b, a, glow);
        beam(draw, x2, y1, z2, x1, y1, z2, r, g, b, a, glow);
        beam(draw, x1, y1, z2, x1, y1, z1, r, g, b, a, glow);
        beam(draw, x1, y2, z1, x2, y2, z1, r, g, b, a, glow);
        beam(draw, x2, y2, z1, x2, y2, z2, r, g, b, a, glow);
        beam(draw, x2, y2, z2, x1, y2, z2, r, g, b, a, glow);
        beam(draw, x1, y2, z2, x1, y2, z1, r, g, b, a, glow);
        beam(draw, x1, y1, z1, x1, y2, z1, r, g, b, a, glow);
        beam(draw, x2, y1, z1, x2, y2, z1, r, g, b, a, glow);
        beam(draw, x2, y1, z2, x2, y2, z2, r, g, b, a, glow);
        beam(draw, x1, y1, z2, x1, y2, z2, r, g, b, a, glow);
    }

    private void beam(Draw draw, double ax, double ay, double az, double bx, double by, double bz,
                      int r, int g, int b, int a, double glow) {
        double minX = Math.min(ax, bx) - glow;
        double minY = Math.min(ay, by) - glow;
        double minZ = Math.min(az, bz) - glow;
        double maxX = Math.max(ax, bx) + glow;
        double maxY = Math.max(ay, by) + glow;
        double maxZ = Math.max(az, bz) + glow;

        fillQuad(draw, minX, minY, minZ, maxX, minY, minZ, maxX, minY, maxZ, minX, minY, maxZ, r, g, b, a);
        fillQuad(draw, minX, maxY, minZ, maxX, maxY, minZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        fillQuad(draw, minX, minY, minZ, maxX, minY, minZ, maxX, maxY, minZ, minX, maxY, minZ, r, g, b, a);
        fillQuad(draw, minX, minY, maxZ, maxX, minY, maxZ, maxX, maxY, maxZ, minX, maxY, maxZ, r, g, b, a);
        fillQuad(draw, minX, minY, minZ, minX, minY, maxZ, minX, maxY, maxZ, minX, maxY, minZ, r, g, b, a);
        fillQuad(draw, maxX, minY, minZ, maxX, minY, maxZ, maxX, maxY, maxZ, maxX, maxY, minZ, r, g, b, a);
    }

    private void drawCircle(Draw draw, Vec3d center, float radius, float yOffset, int argb) {
        int r = (argb >> 16) & 0xFF;
        int g = (argb >> 8) & 0xFF;
        int b = argb & 0xFF;
        int a = alpha(argb);
        double y = center.y + yOffset;
        double k = lineScale();

        drawDisc(draw, center, y, radius * (1.0 - 0.015 * k), r, g, b, Math.max(1, (int) (a * 0.05)));
        drawRing(draw, center, y, radius * (1.0 - 0.10 * k), radius * (1.0 - 0.015 * k), r, g, b, Math.max(1, (int) (a * 0.08)));
        drawRing(draw, center, y, radius * (1.0 - 0.015 * k), radius, r, g, b, Math.max(1, (int) (a * 0.35)));
        drawRing(draw, center, y, radius, radius * (1.0 + 0.10 * k), r, g, b, Math.max(1, (int) (a * 0.16)));
        drawRing(draw, center, y, radius * (1.0 - 0.002 * k), radius * (1.0 + 0.002 * k), r, g, b, a);
    }

    private float lineScale() {
        return Math.max(1.0F, lineWidth.getValue() / 2.5F);
    }

    private void drawDisc(Draw draw, Vec3d center, double y, double radius, int r, int g, int b, int a) {
        for (int i = 0; i < CIRCLE_SEGMENTS; i++) {
            double angle1 = Math.PI * 2.0 * i / CIRCLE_SEGMENTS;
            double angle2 = Math.PI * 2.0 * (i + 1) / CIRCLE_SEGMENTS;
            fillQuad(draw,
                    center.x, y, center.z,
                    center.x + Math.cos(angle1) * radius, y, center.z + Math.sin(angle1) * radius,
                    center.x + Math.cos(angle2) * radius, y, center.z + Math.sin(angle2) * radius,
                    center.x, y, center.z,
                    r, g, b, a);
        }
    }

    private void drawRing(Draw draw, Vec3d center, double y, double inner, double outer, int r, int g, int b, int a) {
        for (int i = 0; i < CIRCLE_SEGMENTS; i++) {
            double angle1 = Math.PI * 2.0 * i / CIRCLE_SEGMENTS;
            double angle2 = Math.PI * 2.0 * (i + 1) / CIRCLE_SEGMENTS;
            double cos1 = Math.cos(angle1);
            double sin1 = Math.sin(angle1);
            double cos2 = Math.cos(angle2);
            double sin2 = Math.sin(angle2);
            fillQuad(draw,
                    center.x + cos1 * inner, y, center.z + sin1 * inner,
                    center.x + cos1 * outer, y, center.z + sin1 * outer,
                    center.x + cos2 * outer, y, center.z + sin2 * outer,
                    center.x + cos2 * inner, y, center.z + sin2 * inner,
                    r, g, b, a);
        }
    }

    private void stroke(Draw draw, Vec3d from, Vec3d to, int r, int g, int b, int a) {
        if (draw.line == null) return;

        double dx = to.x - from.x;
        double dy = to.y - from.y;
        double dz = to.z - from.z;
        double length = Math.sqrt(dx * dx + dy * dy + dz * dz);

        if (length < 1.0e-6) return;

        float nx = (float) (dx / length);
        float ny = (float) (dy / length);
        float nz = (float) (dz / length);
        float width = Math.max(1.0F, lineWidth.getValue());

        draw.line.vertex(draw.matrix, (float) (from.x - draw.cam.x), (float) (from.y - draw.cam.y), (float) (from.z - draw.cam.z))
                .color(r, g, b, a).normal(nx, ny, nz).lineWidth(width);
        draw.line.vertex(draw.matrix, (float) (to.x - draw.cam.x), (float) (to.y - draw.cam.y), (float) (to.z - draw.cam.z))
                .color(r, g, b, a).normal(nx, ny, nz).lineWidth(width);
    }

    private void fillQuad(Draw draw, double x1, double y1, double z1, double x2, double y2, double z2,
                          double x3, double y3, double z3, double x4, double y4, double z4,
                          int r, int g, int b, int a) {
        if (draw.fill == null) return;

        vertex(draw.fill, draw.matrix, draw.cam, x1, y1, z1, r, g, b, a);
        vertex(draw.fill, draw.matrix, draw.cam, x2, y2, z2, r, g, b, a);
        vertex(draw.fill, draw.matrix, draw.cam, x3, y3, z3, r, g, b, a);
        vertex(draw.fill, draw.matrix, draw.cam, x4, y4, z4, r, g, b, a);
    }

    private void fillQuadGrad(Draw draw, double x1, double y1, double z1, double x2, double y2, double z2,
                              double x3, double y3, double z3, double x4, double y4, double z4,
                              int r, int g, int b, int aBottom, int aTop) {
        if (draw.fill == null) return;

        vertex(draw.fill, draw.matrix, draw.cam, x1, y1, z1, r, g, b, aBottom);
        vertex(draw.fill, draw.matrix, draw.cam, x2, y2, z2, r, g, b, aBottom);
        vertex(draw.fill, draw.matrix, draw.cam, x3, y3, z3, r, g, b, aTop);
        vertex(draw.fill, draw.matrix, draw.cam, x4, y4, z4, r, g, b, aTop);
    }

    private static void vertex(VertexConsumer buf, Matrix4f matrix, Vec3d cam, Vec3d point,
                               int r, int g, int b, int a) {
        buf.vertex(matrix, (float) (point.x - cam.x), (float) (point.y - cam.y), (float) (point.z - cam.z))
                .color(r, g, b, a);
    }

    private static void vertex(VertexConsumer buf, Matrix4f matrix, Vec3d cam,
                               double x, double y, double z, int r, int g, int b, int a) {
        buf.vertex(matrix, (float) (x - cam.x), (float) (y - cam.y), (float) (z - cam.z)).color(r, g, b, a);
    }

    private static final class Draw {
        VertexConsumer fill;
        VertexConsumer line;
        final Matrix4f matrix;
        final Vec3d cam;

        Draw(Matrix4f matrix, Vec3d cam) {
            this.matrix = matrix;
            this.cam = cam;
        }
    }

    private static final RenderPipeline FILL_PIPELINE = RenderPipelines.register(
            RenderPipeline.builder(RenderPipelines.POSITION_COLOR_SNIPPET)
                    .withLocation(Identifier.of("prism", "ft_helper_fill"))
                    .withVertexFormat(VertexFormats.POSITION_COLOR, VertexFormat.DrawMode.QUADS)
                    .withCull(false)
                    .withDepthTestFunction(DepthTestFunction.LEQUAL_DEPTH_TEST)
                    .withDepthWrite(false)
                    .withBlend(BlendFunction.TRANSLUCENT)
                    .build()
    );

    private static final RenderLayer FILL_LAYER = RenderLayer.of(
            "prism_ft_helper_fill",
            RenderSetup.builder(FILL_PIPELINE).expectedBufferSize(1 << 17).build()
    );
}
