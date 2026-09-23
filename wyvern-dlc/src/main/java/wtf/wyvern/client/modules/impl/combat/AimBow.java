package wtf.wyvern.client.modules.impl.combat;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.wyvern.core.events.impl.render.EventRender2D;
import wtf.wyvern.render.display.base.color.ColorRGBA;
import wtf.wyvern.render.display.shader.DrawUtil;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.Rotation;

import java.util.Comparator;

@ModuleAnnotation(
        name = "AimBow",
        category = Category.COMBAT,
        description = "Баллистическое наведение лука с предиктом"
)
public final class AimBow extends Module {
    public static final AimBow INSTANCE = new AimBow();

    private static final double ARROW_DRAG = 0.99D;
    private static final double ARROW_GRAVITY = 0.05D;

    private final SliderSetting range = new SliderSetting("Дистанция", 60.0F, 8.0F, 120.0F, 1.0F);
    private final SliderSetting fov = new SliderSetting("FOV", 120.0F, 10.0F, 180.0F, 1.0F);
    private final SliderSetting rotationSpeed = new SliderSetting("Скорость ротации", 90.0F, 10.0F, 180.0F, 1.0F);
    private final SliderSetting prediction = new SliderSetting("Сила предикта", 1.0F, 0.5F, 1.5F, 0.05F);
    private final BooleanSetting pingCompensation = new BooleanSetting("Компенсация пинга", true);
    private final BooleanSetting throughWalls = new BooleanSetting("Через препятствия", false);
    private final BooleanSetting displayFov = new BooleanSetting("Отображать FOV", true);

    private PlayerEntity target;

    private AimBow() {
    }

    @EventTarget
    private void onUpdate(EventGameUpdate event) {
        if (mc.player == null || mc.world == null || !mc.player.isUsingItem()
                || !mc.player.getActiveItem().isOf(Items.BOW)) {
            target = null;
            return;
        }

        float arrowSpeed = bowVelocity(mc.player.getItemUseTime());
        if (arrowSpeed < 0.25F) {
            return;
        }

        AimSolution solution = solutionForLockedTarget(arrowSpeed);
        if (solution == null) {
            solution = mc.world.getPlayers().stream()
                    .filter(this::isValidTarget)
                    .map(player -> solve(player, arrowSpeed))
                    .filter(candidate -> candidate != null && inFov(candidate))
                    .filter(candidate -> throughWalls.isEnabled() || trajectoryClear(candidate, arrowSpeed))
                    .min(Comparator.comparingDouble(this::score))
                    .orElse(null);
            target = solution == null ? null : solution.target();
        }

        if (solution == null) {
            return;
        }

        float speed = rotationSpeed.getCurrent();
        RotationComponent.updateVanillaGcd(
                new Rotation(solution.yaw(), solution.pitch()),
                speed,
                speed,
                Math.min(speed, 70.0F),
                Math.min(speed, 70.0F),
                2,
                30,
                false
        );
    }

    @EventTarget
    private void onRender2D(EventRender2D event) {
        if (!displayFov.isEnabled() || mc.player == null
                || (!mc.player.getMainHandStack().isOf(Items.BOW)
                && !mc.player.getOffHandStack().isOf(Items.BOW))) {
            return;
        }

        float width = event.getContext().getScaledWindowWidth();
        float height = event.getContext().getScaledWindowHeight();
        double cameraFov = mc.options.getFov().getValue();
        double radius = Math.tan(Math.toRadians(fov.getCurrent() * 0.5D))
                / Math.tan(Math.toRadians(cameraFov * 0.5D))
                * height * 0.5D;
        radius = MathHelper.clamp(radius, 4.0D, Math.hypot(width, height) * 0.5D);

        ColorRGBA color = Wyvern.getInstance().getThemeManager().getCurrentTheme()
                .getColor().withAlpha(135);
        float centerX = width * 0.5F;
        float centerY = height * 0.5F;
        int segments = 72;
        for (int i = 0; i < segments; i++) {
            double first = Math.PI * 2.0D * i / segments;
            double second = Math.PI * 2.0D * (i + 1) / segments;
            DrawUtil.drawLine(
                    event.getContext().getMatrices(),
                    new Vec2f(
                            centerX + (float) (Math.cos(first) * radius),
                            centerY + (float) (Math.sin(first) * radius)
                    ),
                    new Vec2f(
                            centerX + (float) (Math.cos(second) * radius),
                            centerY + (float) (Math.sin(second) * radius)
                    ),
                    color
            );
        }
    }

    @Override
    public void onDisable() {
        target = null;
        super.onDisable();
    }

    private AimSolution solutionForLockedTarget(float arrowSpeed) {
        if (!isValidTarget(target)) {
            return null;
        }
        AimSolution solution = solve(target, arrowSpeed);
        if (solution == null || !inFov(solution)) {
            return null;
        }
        return throughWalls.isEnabled() || trajectoryClear(solution, arrowSpeed) ? solution : null;
    }

    private AimSolution solve(PlayerEntity player, float arrowSpeed) {
        Vec3d origin = mc.player.getEyePos().add(0.0D, -0.1D, 0.0D);
        Vec3d shooterVelocity = mc.player.getVelocity();
        Vec3d inherited = new Vec3d(
                shooterVelocity.x,
                mc.player.isOnGround() ? 0.0D : shooterVelocity.y,
                shooterVelocity.z
        );
        double latencyTicks = pingCompensation.isEnabled() ? latencyTicks(player) : 0.0D;

        AimSolution best = null;
        double bestError = Double.MAX_VALUE;
        double maxTicks = Math.min(120.0D, Math.max(20.0D, range.getCurrent() / Math.max(arrowSpeed, 0.1F) * 2.5D));

        for (double ticks = 1.0D; ticks <= maxTicks; ticks += 0.25D) {
            Vec3d future = predictTarget(player, ticks + latencyTicks);
            Vec3d delta = future.subtract(origin);
            double dragSum = (1.0D - Math.pow(ARROW_DRAG, ticks)) / (1.0D - ARROW_DRAG);
            double gravityOffset = ARROW_GRAVITY / (1.0D - ARROW_DRAG) * (ticks - dragSum);
            Vec3d required = new Vec3d(
                    delta.x / dragSum,
                    (delta.y + gravityOffset) / dragSum,
                    delta.z / dragSum
            ).subtract(inherited);
            double requiredSpeed = required.length();
            if (requiredSpeed < 1.0E-5D) {
                continue;
            }

            double error = Math.abs(requiredSpeed - arrowSpeed) + ticks * 0.00035D;
            if (error >= bestError) {
                continue;
            }

            Vec3d direction = required.multiply(1.0D / requiredSpeed);
            float yaw = MathHelper.wrapDegrees((float) Math.toDegrees(Math.atan2(direction.z, direction.x)) - 90.0F);
            float pitch = MathHelper.clamp(
                    (float) -Math.toDegrees(Math.atan2(direction.y, Math.hypot(direction.x, direction.z))),
                    -90.0F,
                    90.0F
            );
            bestError = error;
            best = new AimSolution(player, origin, direction, inherited, yaw, pitch, ticks);
        }

        return bestError <= Math.max(0.08D, arrowSpeed * 0.08D) ? best : null;
    }

    private Vec3d predictTarget(PlayerEntity player, double ticks) {
        Vec3d center = player.getBoundingBox().getCenter();
        Vec3d velocity = player.getVelocity();
        double multiplier = prediction.getCurrent();
        double velocityX = MathHelper.clamp(velocity.x * multiplier, -1.8D, 1.8D);
        double velocityZ = MathHelper.clamp(velocity.z * multiplier, -1.8D, 1.8D);
        double x = center.x + velocityX * ticks;
        double z = center.z + velocityZ * ticks;
        double y = center.y;

        if (!player.isOnGround()) {
            if (player.isGliding()) {
                y += MathHelper.clamp(velocity.y * multiplier, -2.5D, 2.5D) * ticks;
            } else {
                double verticalVelocity = velocity.y * multiplier;
                int wholeTicks = (int) ticks;
                for (int i = 0; i < wholeTicks; i++) {
                    y += verticalVelocity;
                    verticalVelocity = (verticalVelocity - 0.08D) * 0.98D;
                }
                y += verticalVelocity * (ticks - wholeTicks);
            }
        }
        return new Vec3d(x, y, z);
    }

    private boolean trajectoryClear(AimSolution solution, float arrowSpeed) {
        Vec3d position = solution.origin();
        Vec3d velocity = solution.direction().multiply(arrowSpeed).add(solution.inheritedVelocity());
        int steps = Math.max(1, (int) Math.ceil(solution.flightTicks()));

        for (int i = 0; i < steps; i++) {
            Vec3d next = position.add(velocity);
            HitResult hit = mc.world.raycast(new RaycastContext(
                    position,
                    next,
                    RaycastContext.ShapeType.COLLIDER,
                    RaycastContext.FluidHandling.NONE,
                    mc.player
            ));
            if (hit.getType() == HitResult.Type.BLOCK) {
                return false;
            }
            position = next;
            velocity = new Vec3d(
                    velocity.x * ARROW_DRAG,
                    velocity.y * ARROW_DRAG - ARROW_GRAVITY,
                    velocity.z * ARROW_DRAG
            );
        }
        return true;
    }

    private boolean isValidTarget(PlayerEntity player) {
        return player != null
                && player != mc.player
                && player.isAlive()
                && !player.isRemoved()
                && !player.isSpectator()
                && !player.isCreative()
                && mc.player.distanceTo(player) <= range.getCurrent()
                && !Wyvern.getInstance().getFriendManager().isFriend(player.getName().getString());
    }

    private boolean inFov(AimSolution solution) {
        float yawDelta = MathHelper.wrapDegrees(solution.yaw() - mc.gameRenderer.getCamera().getYaw());
        float pitchDelta = solution.pitch() - mc.gameRenderer.getCamera().getPitch();
        return Math.hypot(yawDelta, pitchDelta) <= fov.getCurrent() * 0.5F;
    }

    private double score(AimSolution solution) {
        float yawDelta = MathHelper.wrapDegrees(solution.yaw() - mc.gameRenderer.getCamera().getYaw());
        float pitchDelta = solution.pitch() - mc.gameRenderer.getCamera().getPitch();
        return Math.hypot(yawDelta, pitchDelta) + mc.player.distanceTo(solution.target()) * 0.08D;
    }

    private double latencyTicks(PlayerEntity player) {
        if (mc.getNetworkHandler() == null) {
            return 0.0D;
        }
        var entry = mc.getNetworkHandler().getPlayerListEntry(player.getUuid());
        return entry == null ? 0.0D : Math.min(6.0D, entry.getLatency() / 100.0D + 1.0D);
    }

    private float bowVelocity(int useTicks) {
        float charge = useTicks / 20.0F;
        charge = (charge * charge + charge * 2.0F) / 3.0F;
        return Math.min(charge, 1.0F) * 3.0F;
    }

    private record AimSolution(
            PlayerEntity target,
            Vec3d origin,
            Vec3d direction,
            Vec3d inheritedVelocity,
            float yaw,
            float pitch,
            double flightTicks
    ) {
    }
}
