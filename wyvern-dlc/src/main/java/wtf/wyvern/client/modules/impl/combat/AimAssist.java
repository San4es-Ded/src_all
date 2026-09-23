package wtf.wyvern.client.modules.impl.combat;

import java.util.List;
import java.util.Random;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec2f;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.eventbus.types.Priority;
import wtf.wyvern.core.events.impl.other.EventGameUpdate;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.game.player.rotation.RotationUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AimAssist",
        category = Category.COMBAT,
        description = "Помощь в визуальной наводке на цель"
)
@FastNative
public final class AimAssist extends Module {

    public static final AimAssist INSTANCE = new AimAssist();

    public final ModeSetting mode = new ModeSetting("Режим", "Плавная", "Резкая", "Вспомогательная");
    public final SliderSetting speed = new SliderSetting("Скорость наводки", 5.0F, 0.5F, 20.0F, 0.5F);
    public final SliderSetting fov = new SliderSetting("FOV", 60.0F, 5.0F, 180.0F, 5.0F);
    public final SliderSetting distance = new SliderSetting("Дистанция", 4.5F, 1.0F, 10.0F, 0.5F);
    public final BooleanSetting onlyOnClick = new BooleanSetting("Только при нажатии", false);
    public final BooleanSetting pitchAim = new BooleanSetting("Наводить по вертикали", true);
    public final BooleanSetting multipoint = new BooleanSetting("Рандомизация Multipoint", true);
    private final MultiBooleanSetting targetTypeSetting = MultiBooleanSetting.create(
            "Атаковать",
            List.of("Игроков", "Мобов", "Животных", "Друзей", "Голых", "Невидимых")
    );

    private final Random random = new Random();
    private LivingEntity trackedTarget;
    private Vec3d pointStartOffset = Vec3d.ZERO;
    private Vec3d pointControlOffset = Vec3d.ZERO;
    private Vec3d currentPointOffset = Vec3d.ZERO;
    private Vec3d targetPointOffset = Vec3d.ZERO;
    private int pointTravelTick;
    private int pointTravelDuration;
    private float yawVelocity;
    private float pitchVelocity;
    private float yawMouseRemainder;
    private float pitchMouseRemainder;
    private float cadencePhase;
    private float cadenceMultiplier = 1.0F;
    private float targetCadenceMultiplier = 1.0F;
    private int cadenceTicks;

    private AimAssist() {
    }

    @FastNative
    @EventTarget(Priority.LOWEST)
    public void onGameUpdate(EventGameUpdate e) {
        if (mc.player == null || mc.world == null || mc.currentScreen != null || !isEnabled()) {
            resetAimState();
            return;
        }
        if (onlyOnClick.isEnabled() && !mc.options.attackKey.isPressed()) {
            resetMotion();
            return;
        }

        LivingEntity target = getBestTarget();
        if (target == null) {
            resetAimState();
            return;
        }

        Vec3d eyePos = mc.player.getEyePos();
        Vec3d targetPos = multipoint.isEnabled()
                ? getSmoothMultipoint(target)
                : target.getBoundingBox().getCenter();
        Rotation targetRot = RotationUtil.fromVec3d(targetPos.subtract(eyePos));

        float deltaYaw = MathHelper.wrapDegrees(targetRot.getYaw() - mc.player.getYaw());
        float deltaPitch = targetRot.getPitch() - mc.player.getPitch();

        float currentSpeed = this.speed.getCurrent();
        float angularError = (float) Math.hypot(deltaYaw, pitchAim.isEnabled() ? deltaPitch : 0.0F);
        float yawStep = 0.0F;
        float pitchStep = 0.0F;

        if (this.mode.is("Резкая")) {
            float maxStep = currentSpeed * 2.5F;
            yawStep = nonLinearAxisStep(deltaYaw, angularError, maxStep, 0.72F);
            pitchStep = pitchAim.isEnabled()
                    ? nonLinearAxisStep(deltaPitch, angularError, maxStep * 0.91F, 0.78F)
                    : 0.0F;
        } else if (this.mode.is("Плавная")) {
            yawStep = nonLinearAxisStep(deltaYaw, angularError, currentSpeed, 0.88F);
            pitchStep = pitchAim.isEnabled()
                    ? nonLinearAxisStep(deltaPitch, angularError, currentSpeed * 0.82F, 0.96F)
                    : 0.0F;
        } else if (this.mode.is("Вспомогательная")) {
            float maxFovHalf = Math.max(0.1F, this.fov.getCurrent() / 2.0F);
            float currentFov = RotationUtil.calculateFov(
                    mc.player.getYaw(), mc.player.getPitch(),
                    targetRot.getYaw(), targetRot.getPitch()
            );

            // Normalized distance: 0.0 at target center, 1.0 at FOV edge
            float normalizedDist = MathHelper.clamp(currentFov / maxFovHalf, 0.0F, 1.0F);

            // Non-linear Smoothstep curve: strongest magnet near target, smooth decay at FOV edge
            float curve = 1.0F - (normalizedDist * normalizedDist * (3.0F - 2.0F * normalizedDist));

            float maxStep = Math.max(0.2F, currentSpeed * 0.4F);
            float assistStrength = 0.18F + curve * 0.82F;
            yawStep = nonLinearAxisStep(deltaYaw, angularError, maxStep, 0.82F) * assistStrength;
            pitchStep = pitchAim.isEnabled()
                    ? nonLinearAxisStep(deltaPitch, angularError, maxStep * 0.72F, 0.94F) * assistStrength
                    : 0.0F;
        }

        updateCadence(currentSpeed);
        float pulse = 0.965F + (float) Math.sin(this.cadencePhase) * 0.035F;
        yawStep *= this.cadenceMultiplier * pulse;
        pitchStep *= this.cadenceMultiplier * (2.0F - pulse) * 0.94F;

        float errorRatio = MathHelper.clamp(angularError / 45.0F, 0.0F, 1.0F);
        float accelerationBase = this.mode.is("Резкая") ? 0.56F
                : this.mode.is("Плавная") ? 0.26F : 0.18F;
        float accelerationCurve = accelerationBase
                * (0.62F + 0.58F * (float) Math.pow(errorRatio, 0.63D));
        float yawShare = angularError > 0.0001F ? Math.abs(deltaYaw) / angularError : 0.0F;
        float pitchShare = angularError > 0.0001F ? Math.abs(deltaPitch) / angularError : 0.0F;
        float yawAcceleration = MathHelper.clamp(accelerationCurve * (0.91F + yawShare * 0.17F), 0.05F, 0.78F);
        float pitchAcceleration = MathHelper.clamp(accelerationCurve * (0.84F + pitchShare * 0.14F), 0.04F, 0.70F);
        this.yawVelocity += (yawStep - this.yawVelocity) * yawAcceleration;
        this.pitchVelocity += (pitchStep - this.pitchVelocity) * pitchAcceleration;

        if (Math.signum(this.yawVelocity) != Math.signum(deltaYaw)) {
            this.yawVelocity = 0.0F;
        }
        if (Math.signum(this.pitchVelocity) != Math.signum(deltaPitch)) {
            this.pitchVelocity = 0.0F;
        }

        this.yawVelocity = MathHelper.clamp(this.yawVelocity, -Math.abs(deltaYaw), Math.abs(deltaYaw));
        this.pitchVelocity = pitchAim.isEnabled()
                ? MathHelper.clamp(this.pitchVelocity, -Math.abs(deltaPitch), Math.abs(deltaPitch))
                : 0.0F;

        float gcd = Rotation.gcd();
        float quantizedYaw = quantizeMouseStep(this.yawVelocity, gcd, true, deltaYaw);
        float quantizedPitch = pitchAim.isEnabled()
                ? quantizeMouseStep(this.pitchVelocity, gcd, false, deltaPitch)
                : 0.0F;

        if (Math.abs(quantizedYaw) > 0.0001F || Math.abs(quantizedPitch) > 0.0001F) {
            Vec2f previous = new Vec2f(mc.player.getYaw(), mc.player.getPitch());
            Vec2f requested = new Vec2f(
                    previous.x + quantizedYaw,
                    MathHelper.clamp(previous.y + quantizedPitch, -90.0F, 90.0F)
            );
            Vec2f rot = RotationComponent.applyVanillaSensitivityPatch(requested, previous);
            mc.player.setYaw(rot.x);
            mc.player.setPitch(rot.y);
            mc.player.setHeadYaw(rot.x);
        }
    }

    private float nonLinearAxisStep(float delta, float totalError, float maxStep, float axisExponent) {
        float absoluteDelta = Math.abs(delta);
        if (absoluteDelta < 0.0001F || totalError < 0.0001F) {
            return 0.0F;
        }

        float axisShare = MathHelper.clamp(absoluteDelta / totalError, 0.0F, 1.0F);
        float directionCurve = (float) Math.pow(axisShare, axisExponent);
        float distanceCurve = 1.0F - (float) Math.exp(-Math.pow(totalError / 11.5F, 1.27D));
        float closeControl = MathHelper.clamp(absoluteDelta / 2.5F, 0.08F, 1.0F);
        float magnitude = maxStep * directionCurve * distanceCurve * closeControl;
        return Math.copySign(Math.min(absoluteDelta, magnitude), delta);
    }

    private void updateCadence(float currentSpeed) {
        if (--this.cadenceTicks <= 0) {
            this.targetCadenceMultiplier = 0.86F + this.random.nextFloat() * 0.28F;
            this.cadenceTicks = 5 + this.random.nextInt(10);
        }
        float cadenceEase = 0.12F + this.random.nextFloat() * 0.05F;
        this.cadenceMultiplier += (this.targetCadenceMultiplier - this.cadenceMultiplier) * cadenceEase;
        this.cadencePhase += 0.22F + MathHelper.clamp(currentSpeed / 20.0F, 0.0F, 1.0F) * 0.19F;
    }

    private Vec3d getSmoothMultipoint(LivingEntity target) {
        if (this.trackedTarget != target) {
            this.trackedTarget = target;
            this.currentPointOffset = Vec3d.ZERO;
            chooseNextPoint(target);
        } else if (this.pointTravelTick >= this.pointTravelDuration) {
            chooseNextPoint(target);
        }

        float t = MathHelper.clamp(++this.pointTravelTick / (float) Math.max(1, this.pointTravelDuration), 0.0F, 1.0F);
        float eased = smootherStep(t);
        double inverse = 1.0D - eased;
        this.currentPointOffset = this.pointStartOffset.multiply(inverse * inverse)
                .add(this.pointControlOffset.multiply(2.0D * inverse * eased))
                .add(this.targetPointOffset.multiply(eased * eased));
        return target.getBoundingBox().getCenter().add(this.currentPointOffset);
    }

    private void chooseNextPoint(LivingEntity target) {
        double width = target.getBoundingBox().getLengthX();
        double height = target.getBoundingBox().getLengthY();
        double depth = target.getBoundingBox().getLengthZ();

        this.pointStartOffset = this.currentPointOffset;
        double x = (this.random.nextDouble() * 2.0D - 1.0D) * width * 0.28D;
        double y = (this.random.nextDouble() * 0.20D - 0.02D) * height;
        double z = (this.random.nextDouble() * 2.0D - 1.0D) * depth * 0.28D;
        this.targetPointOffset = new Vec3d(x, y, z);

        Vec3d midpoint = this.pointStartOffset.lerp(this.targetPointOffset, 0.5D);
        double curveX = (this.random.nextDouble() * 2.0D - 1.0D) * width * 0.14D;
        double curveY = (this.random.nextDouble() * 2.0D - 1.0D) * height * 0.07D;
        double curveZ = (this.random.nextDouble() * 2.0D - 1.0D) * depth * 0.14D;
        this.pointControlOffset = new Vec3d(
                MathHelper.clamp(midpoint.x + curveX, -width * 0.34D, width * 0.34D),
                MathHelper.clamp(midpoint.y + curveY, -height * 0.10D, height * 0.24D),
                MathHelper.clamp(midpoint.z + curveZ, -depth * 0.34D, depth * 0.34D)
        );
        float speedFactor = MathHelper.clamp(this.speed.getCurrent() / 20.0F, 0.0F, 1.0F);
        this.pointTravelDuration = 24 - Math.round(speedFactor * 9.0F) + this.random.nextInt(12);
        this.pointTravelTick = 0;
    }

    private float smootherStep(float value) {
        float t = MathHelper.clamp(value, 0.0F, 1.0F);
        return t * t * t * (t * (t * 6.0F - 15.0F) + 10.0F);
    }

    private float quantizeMouseStep(float step, float gcd, boolean yawAxis, float remainingDelta) {
        if (!(gcd > 0.0F) || !Float.isFinite(gcd)) {
            return step;
        }
        if (Math.abs(remainingDelta) < gcd * 0.55F) {
            if (yawAxis) {
                this.yawMouseRemainder = 0.0F;
                this.yawVelocity = 0.0F;
            } else {
                this.pitchMouseRemainder = 0.0F;
                this.pitchVelocity = 0.0F;
            }
            return 0.0F;
        }

        float remainder = yawAxis ? this.yawMouseRemainder : this.pitchMouseRemainder;
        float accumulated = step + remainder;
        float quantized = Math.round(accumulated / gcd) * gcd;
        if (Math.abs(quantized) > Math.abs(remainingDelta) + gcd * 0.5F) {
            quantized = (float) ((int) (remainingDelta / gcd)) * gcd;
        }

        float nextRemainder = MathHelper.clamp(accumulated - quantized, -gcd, gcd);
        if (yawAxis) {
            this.yawMouseRemainder = nextRemainder;
        } else {
            this.pitchMouseRemainder = nextRemainder;
        }
        return quantized;
    }

    private void resetMotion() {
        this.yawVelocity = 0.0F;
        this.pitchVelocity = 0.0F;
        this.yawMouseRemainder = 0.0F;
        this.pitchMouseRemainder = 0.0F;
    }

    private void resetAimState() {
        this.trackedTarget = null;
        this.pointStartOffset = Vec3d.ZERO;
        this.pointControlOffset = Vec3d.ZERO;
        this.currentPointOffset = Vec3d.ZERO;
        this.targetPointOffset = Vec3d.ZERO;
        this.pointTravelTick = 0;
        this.pointTravelDuration = 0;
        this.cadencePhase = 0.0F;
        this.cadenceMultiplier = 1.0F;
        this.targetCadenceMultiplier = 1.0F;
        this.cadenceTicks = 0;
        resetMotion();
    }

    private LivingEntity getBestTarget() {
        LivingEntity best = null;
        double bestFov = Double.POSITIVE_INFINITY;

        for (Entity entity : mc.world.getEntities()) {
            if (entity instanceof LivingEntity living && isValid(living)) {
                Vec3d eyePos = mc.player.getEyePos();
                Vec3d targetPos = living.getBoundingBox().getCenter();
                Rotation targetRot = RotationUtil.fromVec3d(targetPos.subtract(eyePos));
                float fovDiff = RotationUtil.calculateFov(
                        mc.player.getYaw(), mc.player.getPitch(),
                        targetRot.getYaw(), targetRot.getPitch()
                );
                if (fovDiff < bestFov) {
                    bestFov = fovDiff;
                    best = living;
                }
            }
        }

        return best;
    }

    public boolean isValid(LivingEntity entity) {
        if (entity == mc.player || !entity.isAlive() || entity.getHealth() <= 0.0F) {
            return false;
        }
        if (mc.player == null || !mc.player.isAlive() || mc.player.getHealth() <= 0.0F) {
            return false;
        }
        if (entity instanceof ArmorStandEntity) {
            return false;
        }

        if (entity instanceof PlayerEntity player) {
            boolean invisible = player.isInvisible();
            boolean naked = isNaked(player);

            if (invisible) {
                if (!this.targetTypeSetting.isEnable("Невидимых")) return false;
                if (naked && !this.targetTypeSetting.isEnable("Голых")) return false;
            } else if (naked) {
                if (!this.targetTypeSetting.isEnable("Голых")) return false;
            } else if (!this.targetTypeSetting.isEnable("Игроков")) {
                return false;
            }

            if (!this.targetTypeSetting.isEnable("Друзей")
                    && Wyvern.INSTANCE.getFriendManager().isFriend(entity.getName().getString())) {
                return false;
            }

            if (AntiBot.INSTANCE.isBot(player)) {
                return false;
            }
        } else if (entity instanceof PassiveEntity || entity instanceof FishEntity) {
            if (!this.targetTypeSetting.isEnable("Животных") || Wyvern.INSTANCE.getServerHandler().isPvp()) {
                return false;
            }
        } else if (entity instanceof HostileEntity || entity instanceof AmbientEntity) {
            if (!this.targetTypeSetting.isEnable("Мобов") || Wyvern.INSTANCE.getServerHandler().isPvp()) {
                return false;
            }
        } else {
            return false;
        }

        Vec3d eyePos = mc.player.getEyePos();
        Vec3d targetPos = entity.getBoundingBox().getCenter();
        double dist = eyePos.distanceTo(targetPos);
        if (dist > this.distance.getCurrent()) {
            return false;
        }

        Rotation targetRot = RotationUtil.fromVec3d(targetPos.subtract(eyePos));
        float fovDiff = RotationUtil.calculateFov(
                mc.player.getYaw(), mc.player.getPitch(),
                targetRot.getYaw(), targetRot.getPitch()
        );

        return !(fovDiff > this.fov.getCurrent() / 2.0F);
    }

    private boolean isNaked(PlayerEntity player) {
        for (ItemStack stack : player.getArmorItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void onDisable() {
        resetAimState();
        super.onDisable();
    }
}
