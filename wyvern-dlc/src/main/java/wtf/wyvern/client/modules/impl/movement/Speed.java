package wtf.wyvern.client.modules.impl.movement;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Box;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.core.events.impl.player.EventOnMovePost;
import wtf.wyvern.core.events.impl.player.EventPostMotion;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.utility.game.client.TimerService;
import wtf.wyvern.utility.game.other.NetworkUtils;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "Speed",
        category = Category.MOVEMENT,
        description = "Позволяет бустится от колизии"
)
public class Speed extends Module implements IMinecraft {

    public static final Speed INSTANCE = new Speed();

    private static final float TIMER_SLOW_SPEED = 0.65F;
    private static final float TIMER_FAST_SPEED = 1.35F;
    private static final int TIMER_SLOW_TICKS = 2;
    private static final int TIMER_FAST_BURST_TICKS = 3;
    private static final int TIMER_RECOVERY_TICKS = 20;
    // A pronounced one-second charge phase: movement is almost frozen before the boost.
    private static final float TIMER2_SLOW_SPEED = 0.10F;
    private static final float TIMER2_FAST_SPEED = 1.35F;
    private static final long TIMER2_SLOW_TIME_MS = 1_650L;
    private static final long TIMER2_FAST_TIME_MS = 2_200L;

    private final ModeSetting mode = new ModeSetting("Mode",
            "Grim Timer", "Grim Timer2", "LonyGrief", "Collision", "HolyWorld", "Vanilla");

    private final SliderSetting holyWorldSpeed = new SliderSetting("Скорость", 8.0F, 1.0F, 20.0F, 0.1F,
            () -> mode.is("HolyWorld"));
    private final SliderSetting holyWorldRange = new SliderSetting("Дальность", 3.0F, 0.5F, 10.0F, 0.1F,
            () -> mode.is("HolyWorld"));
    private final SliderSetting holyWorldExpand = new SliderSetting("Расширение", 0.5F, 0.1F, 2.0F, 0.1F,
            () -> mode.is("HolyWorld"));
    private final BooleanSetting holyWorldOnlyPlayers = new BooleanSetting("Только игроки", true,
            () -> mode.is("HolyWorld"));
    private final BooleanSetting holyWorldRequireMoving = new BooleanSetting("Требуется движение", true,
            () -> mode.is("HolyWorld"));

    private final SliderSetting vanillaSpeed = new SliderSetting("Скорость", 10.0F, 1.0F, 30.0F, 0.5F,
            () -> mode.is("Vanilla"));

    private int ticks;
    private int groundTicks;
    private int timerTicks;
    private int timerRecoveryTicks;
    private boolean timerModeActive;
    private boolean timer2FastPhase;
    private long timer2PhaseStartedAt;
    private String activeMode = "";

    final Map<Entity, Vec3d> previousPositions = new HashMap<>();

    @FastNative
    @EventTarget
    private void onUpdate(EventUpdate event) {
        syncSelectedMode();

        if (mode.is("Grim Timer")) {
            onTimerUpdate();
            return;
        }
        if (mode.is("Grim Timer2")) {
            onTimer2Update();
            return;
        }
        if (timerModeActive) {
            resetTimerState(true);
        }

        if (mode.is("Collision")) {
            onCollisionUpdate();
            return;
        }
        if (mode.is("HolyWorld")) {
            onHolyWorldEntityUpdate();
            return;
        }
        if (mode.is("Vanilla")) {
            onVanillaUpdate();
        }
    }

    /**
     * Vanilla-подобные стрейфы: постоянная горизонтальная скорость без накопления.
     * При отпускании клавиш движения горизонтальная скорость мгновенно гасится.
     */
    @FastNative
    private void onVanillaUpdate() {
        if (mc.player == null || mc.world == null || mc.getNetworkHandler() == null
                || mc.player.hasVehicle() || mc.player.getAbilities().flying) return;

        double motionY = mc.player.getVelocity().y;

        if (!MovingUtil.hasPlayerMovement()) {
            mc.player.setVelocity(0.0D, motionY, 0.0D);
            return;
        }

        // Целевая скорость в блоках/тик (переводим из блоков/сек)
        double targetSpeed = vanillaSpeed.getCurrent() / 20.0D;

        // Направление движения
        double yaw = MovingUtil.direction(mc.player.getYaw(),
                mc.player.input.movementForward, mc.player.input.movementSideways);
        double xt = -Math.sin(yaw);
        double zt = Math.cos(yaw);

        // Устанавливаем горизонтальную скорость
        mc.player.setVelocity(xt * targetSpeed, motionY, zt * targetSpeed);
    }

    /** Full port of the supplied Entity speed mode, adapted to this setting API. */
    @FastNative
    private void onHolyWorldEntityUpdate() {
        if (mc.player == null || mc.world == null) return;

        // Single pass over the world entities: count collisions and track the nearest
        // candidate at the same time. The nearest result is only consumed after the
        // same collisions/speed gates as before, so behavior is unchanged.
        int collisions = 0;
        Box expandedBox = mc.player.getBoundingBox().expand(holyWorldExpand.getCurrent());
        Entity nearest = null;
        double bestSq = Double.MAX_VALUE;
        double maxRangeSq = holyWorldRange.getCurrent() * holyWorldRange.getCurrent();
        for (Entity entity : mc.world.getEntities()) {
            if (!isHolyWorldEntity(entity)) continue;
            if (expandedBox.intersects(entity.getBoundingBox())) collisions++;
            double dx = entity.getX() - mc.player.getX();
            double dz = entity.getZ() - mc.player.getZ();
            double distanceSq = dx * dx + dz * dz;
            if (distanceSq <= maxRangeSq && distanceSq < bestSq) {
                bestSq = distanceSq;
                nearest = entity;
            }
        }

        double finalSpeed = holyWorldSpeed.getCurrent() * 0.01D * collisions;
        if (finalSpeed <= 0.0D || collisions <= 0) return;
        if (holyWorldRequireMoving.isEnabled() && !MovingUtil.hasPlayerMovement()) return;

        if (nearest != null) {
            double[] direction = directionToPoint(mc.player.getPos(), nearest.getPos(), finalSpeed);
            mc.player.addVelocity(direction[0], 0.0D, direction[1]);
        }
    }

    @FastNative
    private boolean isHolyWorldEntity(Entity entity) {
        if (entity == mc.player) return false;
        if (holyWorldOnlyPlayers.isEnabled() && !(entity instanceof PlayerEntity)) return false;
        return entity instanceof LivingEntity || entity instanceof BoatEntity;
    }

    @FastNative
    private static double[] directionToPoint(Vec3d from, Vec3d to, double speed) {
        double dx = to.x - from.x;
        double dz = to.z - from.z;
        double length = Math.sqrt(dx * dx + dz * dz);
        if (length == 0.0D) return new double[]{0.0D, 0.0D};
        return new double[]{dx / length * speed, dz / length * speed};
    }

    @FastNative
    private void onTimerUpdate() {
        timerModeActive = true;
        if (!canUseTimer()) {
            resetTimerCycle(true);
            return;
        }

        if (timerRecoveryTicks > 0) {
            TimerService.resetSpeed();
            timerRecoveryTicks--;
            return;
        }

        // Two short boosts separated by vanilla-speed ticks. The complete
        // cycle is time-balanced, so repeated cycles do not get ahead of the
        // server and the end of a boost never transitions straight into one.
        int cycleLength = TIMER_SLOW_TICKS + TIMER_FAST_BURST_TICKS * 2 + 2;
        int cycleTick = timerTicks % cycleLength;
        if (cycleTick < TIMER_SLOW_TICKS) {
            TimerService.setSpeed(TIMER_SLOW_SPEED);
        } else {
            int burstTick = cycleTick - TIMER_SLOW_TICKS;
            boolean normalTick = burstTick == TIMER_FAST_BURST_TICKS
                    || burstTick == TIMER_FAST_BURST_TICKS * 2 + 1;
            TimerService.setSpeed(normalTick ? 1.0F : TIMER_FAST_SPEED);
        }
        timerTicks++;
    }

    @FastNative
    private void onTimer2Update() {
        timerModeActive = true;
        if (!canUseTimer()) {
            resetTimer2Cycle(true);
            return;
        }

        long now = System.currentTimeMillis();
        if (timer2PhaseStartedAt == 0L) {
            timer2PhaseStartedAt = now;
            timer2FastPhase = false;
        }

        long phaseDuration = timer2FastPhase ? TIMER2_FAST_TIME_MS : TIMER2_SLOW_TIME_MS;
        if (now - timer2PhaseStartedAt >= phaseDuration) {
            timer2FastPhase = !timer2FastPhase;
            timer2PhaseStartedAt = now;
        }

        TimerService.setSpeed(timer2FastPhase ? TIMER2_FAST_SPEED : TIMER2_SLOW_SPEED);
    }

    @FastNative
    private void onCollisionUpdate() {
        if (MovingUtil.hasPlayerMovement()) {
            double scanRadius = 1.25;

            List<Entity> nearbyEntities = mc.world.getOtherEntities(
                    mc.player,
                    mc.player.getBoundingBox().expand(scanRadius)
            );

            for (Entity entity : nearbyEntities) {
                if (entity != mc.player && entity instanceof PlayerEntity) {

                    double distanceX = Math.abs(mc.player.getX() - entity.getX());
                    double distanceZ = Math.abs(mc.player.getZ() - entity.getZ());
                    double activationDistanceX = 2.25;
                    double activationDistanceZ = 1.5;

                    if (distanceX > activationDistanceX || distanceZ > activationDistanceZ) {
                        continue;
                    }

                    double entitySpeed = getEntitySpeed(entity);
                    double boostAmount;

                    if (entitySpeed < 5) {
                        boostAmount = 0.02;
                        List<Entity> collisionEntities = mc.world.getOtherEntities(
                                mc.player,
                                mc.player.getBoundingBox().expand(0.1)
                        );

                        int collisions = 0;
                        for (Entity collisionEntity : collisionEntities) {
                            if (collisionEntity != mc.player) {
                                collisions++;
                            }
                        }

                        if (collisions > 0) {
                            double[] motion = forward(boostAmount);
                            addVelocity(motion[0], 0.0, motion[1]);
                        }
                    } else {
                        boostAmount = 0.0335;
                        double checkRadius = 1.25;
                        List<Entity> potentialCollisions = mc.world.getOtherEntities(
                                mc.player,
                                mc.player.getBoundingBox().expand(checkRadius)
                        );

                        int collisions = 0;
                        for (Entity collisionEntity : potentialCollisions) {
                            if (collisionEntity != mc.player) {

                                double distToCollision = mc.player.distanceTo(collisionEntity);
                                if (distToCollision <= checkRadius) {
                                    collisions++;
                                }
                            }
                        }

                        if (collisions > 0) {
                            double[] motion = forward(boostAmount);
                            addVelocity(motion[0], 0.0, motion[1]);
                        }
                    }
                    break;
                }
            }
        }
    }

    @FastNative
    @EventTarget
    private void onMovePost(EventOnMovePost event) {
        if (mode.is("LonyGrief")) {
            if (!canUseMovementSpeed()) {
                resetMovementState(true);
                return;
            }

            TimerService.setSpeed(1.825F);

            if (ticks > 3) {
                double bst = 0.03;
                if (ticks % 2 == 0) {
                    addVelocity(0.0D, 0.03D, 0.0D);
                    if (mc.player.isOnGround()) {
                        bst = 0.085;
                    } else {
                        bst = 0.03;
                    }
                }

                double yaw = MovingUtil.direction(mc.player.getYaw(), mc.player.input.movementForward, mc.player.input.movementSideways);
                double xt = -Math.sin(yaw);
                double zt = Math.cos(yaw);
                if (!MovingUtil.hasPlayerMovement()) {
                    xt = 0.0;
                    zt = 0.0;
                }
                addVelocity(xt * bst, 0.0D, zt * bst);
            }

            ticks++;
        }
    }

    @FastNative
    @EventTarget
    private void onMoveInput(EventMoveInput event) {
        if (mode.is("Grim Timer")) {
            if (canUseTimer() && mc.player.isOnGround()) {
                mc.player.jump();
            }
            return;
        }

        if (mode.is("Grim Timer2")) {
            if (timer2FastPhase && canUseTimer() && mc.player.isOnGround()) {
                mc.player.jump();
            }
            return;
        }

        if (mode.is("LonyGrief")) {
            if (!canUseMovementSpeed()) {
                return;
            }

            if (mc.player.verticalCollision) groundTicks++;
            else groundTicks = 0;

            if (groundTicks >= 1) mc.player.jump();
            return;
        }
    }

    @FastNative
    @EventTarget
    private void onPostMotion(EventPostMotion event) {
        if (mode.is("LonyGrief")) {
            if (!canUseMovementSpeed()) {
                return;
            }

            if (ticks % 2 == 0) {
                TimerService.setSpeed(0.3F);
                NetworkUtils.sendSilentPacket(new ClientCommandC2SPacket(mc.player, ClientCommandC2SPacket.Mode.START_FALL_FLYING));
            }
        }
    }

    @FastNative
    @EventTarget
    private void onPacket(EventPacket event) {
        if (mc.player == null || !event.isReceive()) return;

        Packet<?> packet = event.getPacket();
        if (packet instanceof PlayerPositionLookS2CPacket) {
            if (mode.is("Grim Timer")) {
                timerTicks = 0;
                timerRecoveryTicks = TIMER_RECOVERY_TICKS;
                TimerService.resetSpeed();
            }
            if (mode.is("LonyGrief")) {
                if (ticks % 2 == 1) {
                    ticks++;
                }

                TimerService.setSpeed(1.0F);
            }
        }
    }

    @FastNative
    public double getEntitySpeed(Entity entity) {
        Vec3d currentPos = entity.getPos();
        Vec3d previousPos = previousPositions.getOrDefault(entity, currentPos);

        double dx = currentPos.x - previousPos.x;
        double dz = currentPos.z - previousPos.z;
        double speed = Math.sqrt(dx * dx + dz * dz) * 20.0;

        previousPositions.put(entity, currentPos);

        return speed;
    }

    @FastNative
    private double[] forward(double speed) {
        return MovingUtil.calculateDirection(speed);
    }

    @FastNative
    private void addVelocity(double x, double y, double z) {
        Vec3d vel = mc.player.getVelocity();
        mc.player.setVelocity(vel.x + x, vel.y + y, vel.z + z);
    }

    @FastNative
    private boolean canUseMovementSpeed() {
        return mc.player != null
                && mc.world != null
                && mc.getNetworkHandler() != null
                && MovingUtil.hasPlayerMovement()
                && !mc.player.hasVehicle()
                && !mc.player.getAbilities().flying;
    }

    @FastNative
    private boolean canUseTimer() {
        return canUseMovementSpeed()
                && !mc.player.isGliding()
                && !mc.player.isTouchingWater()
                && !mc.player.isInLava()
                && !mc.player.isClimbing();
    }

    @FastNative
    private void syncSelectedMode() {
        String selectedMode = mode.get();
        if (selectedMode.equals(activeMode)) return;

        resetMovementState(false);
        resetTimerState(true);
        activeMode = selectedMode;

        if (mode.is("Grim Timer")) {
            timerModeActive = true;
            TimerService.setSpeed(TIMER_SLOW_SPEED);
        } else if (mode.is("Grim Timer2")) {
            timerModeActive = true;
            timer2PhaseStartedAt = System.currentTimeMillis();
            timer2FastPhase = false;
            TimerService.setSpeed(TIMER2_SLOW_SPEED);
        }
    }

    @FastNative
    private void resetMovementState(boolean resetTimer) {
        ticks = 0;
        groundTicks = 0;
        if (resetTimer) {
            TimerService.resetSpeed();
        }
    }

    @FastNative
    private void resetTimerCycle(boolean resetTimer) {
        timerTicks = 0;
        timerRecoveryTicks = 0;
        if (resetTimer) {
            TimerService.resetSpeed();
        }
    }

    @FastNative
    private void resetTimer2Cycle(boolean resetTimer) {
        timer2FastPhase = false;
        timer2PhaseStartedAt = 0L;
        if (resetTimer) {
            TimerService.resetSpeed();
        }
    }

    @FastNative
    private void resetTimerState(boolean resetTimer) {
        timerModeActive = false;
        resetTimerCycle(false);
        resetTimer2Cycle(false);
        if (resetTimer) {
            TimerService.resetSpeed();
        }
    }

    @FastNative
    @Override
    public void onEnable() {
        activeMode = "";
        resetMovementState(false);
        resetTimerState(true);
        syncSelectedMode();
        super.onEnable();
    }

    @FastNative
    @Override
    public void onDisable() {
        resetMovementState(false);
        resetTimerState(true);
        activeMode = "";
        super.onDisable();
    }
}