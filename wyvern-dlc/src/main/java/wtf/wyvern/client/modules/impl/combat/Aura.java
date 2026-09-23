// топовiй перегон от trfxcv!!
package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.*;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.ArmorStandEntity;
import net.minecraft.entity.mob.AmbientEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.passive.FishEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;

import net.minecraft.client.network.PlayerListEntry;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.player.EventMotion;
import wtf.wyvern.core.events.impl.player.EventMoveInput;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.impl.combat.other.ElytraTargetRotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.client.modules.impl.movement.AirStuck;
import wtf.wyvern.client.modules.impl.combat.rotation.ShardRotation;
import wtf.wyvern.client.modules.impl.combat.rotation.LegitSnapRotation;
import wtf.wyvern.client.modules.impl.combat.rotation.FunTimeRotation;
import wtf.wyvern.client.modules.impl.combat.rotation.Spooky;
import wtf.wyvern.client.modules.impl.combat.rotation.HolyWorldRotation;
import wtf.wyvern.client.modules.impl.combat.rotation.ReallyWorldRotation;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.player.RaytracingUtil;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.game.player.rotation.RotationUtil;
import wtf.wyvern.utility.math.MultipointUtils;
import wtf.wyvern.utility.math.Timer;
import wtf.wyvern.render.level.Render3DUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "AttackAura", category = Category.COMBAT, description = "Автоматически бьет цель")
public final class Aura extends Module {
    public static final Aura INSTANCE = new Aura();

    private final MultiBooleanSetting targetTypeSetting = MultiBooleanSetting.create("Атаковать", List.of("Игроков", "Мобов", "Животных", "Друзей", "Голых", "Невидимых"));
    private final ModeSetting sortMode = new ModeSetting("Сортировка", "Авто", "Дистанция", "Здоровье", "Броня", "Всё сразу");

    public final ModeSetting rotationMode = new ModeSetting("Ротация", "ReallyWorld", "HolyWorld", "Shard", "FunTime", "LegitSnap", "Spooky", "TriggerBot", "None");
    public final SliderSetting distance = new SliderSetting("Дистанция удара", 3.0F, 0.5F, 6.0F, 0.1F, "Дистанция атаки");
    public final SliderSetting legitSnapFov = new SliderSetting("FOV LegitSnap", 90.0F, 10.0F, 180.0F, 1.0F, () -> rotationMode.is("LegitSnap"));
    private final SliderSetting distanceRotation = new SliderSetting("Дистанция наводки", 0.1F, 0.0F, 100.0F, 0.1F);
    private final SliderSetting rotateElytra = new SliderSetting("Дистанция на элитрах", 30.0f, 0.0f, 100.0f, 1f);
    public final BooleanSetting chaseElytraTarget = new BooleanSetting("Перегонять противника", true);
    public final SliderSetting chaseTicks = new SliderSetting("Тики перегона", 3.0F, 0.0F, 10.0F, 1.0F, chaseElytraTarget::isEnabled);
    private final ModeSetting correctionMode = new ModeSetting("Коррекция", "Свободная", "Таргетированная");
    private final BooleanSetting ignoreWalls = new BooleanSetting("Бить через стены RW", false);
    private final BooleanSetting onlyOnAim = new BooleanSetting("Только при наведении", false);
    private final BooleanSetting smartCriticals = new BooleanSetting("Умные Криты", false);
    private final BooleanSetting tpsSync = new BooleanSetting("Синхронизация TPS", false);
    private final BooleanSetting shieldBreak = new BooleanSetting("Ломать щит", true);
    private final BooleanSetting legitSwap = new BooleanSetting("Легитно ломать", true, shieldBreak::isEnabled);
    private LivingEntity target;
    private final Timer hurtTimer;
    public float lastYaw;
    public float lastPitch;
    private boolean attacking;

    private final ShardRotation shardRotation = new ShardRotation();
    private final LegitSnapRotation legitSnapRotation = new LegitSnapRotation();
    private final FunTimeRotation funTimeRotation = new FunTimeRotation();
    private final Spooky spookyRotation = new Spooky();
    private final HolyWorldRotation holyWorldRotation = new HolyWorldRotation();
    private final ReallyWorldRotation reallyWorldRotation = new ReallyWorldRotation();
    private int rotationAttackTicks;
    private int hitCount = 0;
    private int criticalSprintTicks;
    private LivingEntity pendingCriticalTarget;
    private int elytraPredictionTargetId = Integer.MIN_VALUE;
    private Vec3d elytraPredictionOffset = Vec3d.ZERO;

    private Aura() {
        this.target = null;
        this.hurtTimer = new Timer();
    }

    public static float otvodkaYaw = 0.0f;
    public static float otvodkaPitch = 0.0f;

    public static int getWhiteRiseTicksToAttack() {
        if (mc.player == null) return 0;
        float progress = mc.player.getAttackCooldownProgress(0.5f);
        return progress >= 1.0f ? 0 : 1;
    }

    public float yaw;
    public float pitch;

    @FastNative
    private void attackTarget() {
        if (shieldBreak.isEnabled() && isShieldBlocking(target) && attackWithAxe()) return;
        performAttack();
    }

    @FastNative
    private boolean isShieldBlocking(LivingEntity entity) {
        return entity != null && entity.isBlocking()
                && entity.isUsingItem()
                && entity.getActiveItem().isOf(Items.SHIELD);
    }

    private boolean attackWithAxe() {
        // Prefer a hotbar axe. The old 0..35 reverse search selected an
        // inventory axe even when one was already available in the hotbar,
        // needlessly replacing the sword and making restoration less reliable.
        int axeSlot = this.findAxeSlot(0, 8);
        if (axeSlot == -1) {
            axeSlot = this.findAxeSlot(9, 35);
        }
        if (axeSlot == -1) return false;

        int previousSlot = mc.player.getInventory().selectedSlot;
        boolean inventorySwap = axeSlot > 8;
        int sourceScreenSlot = inventorySwap ? axeSlot : -1;
        ItemStack originalHeldStack = mc.player.getInventory().getStack(previousSlot);

        if (inventorySwap) {
            // A player-inventory slot id is only valid while handler 0 is
            // active. Do not risk moving an unrelated container slot.
            if (mc.player.currentScreenHandler.syncId != mc.player.playerScreenHandler.syncId) {
                return false;
            }
            this.swapAxeWithHotbar(sourceScreenSlot, previousSlot);
            if (!this.isAxe(mc.player.getMainHandStack())) {
                return false;
            }
            axeSlot = previousSlot;
        }

        try {
            this.selectHotbarSlot(axeSlot);
            performAttack();
        } finally {
            if (inventorySwap) {
                this.swapAxeWithHotbar(sourceScreenSlot, previousSlot);

                // A cancelled/failed bypass click used to leave the axe in the
                // selected slot. Retry once with the regular click path when
                // the original held item was not restored locally.
                if (!mc.player.getMainHandStack().isOf(originalHeldStack.getItem())) {
                    mc.interactionManager.clickSlot(mc.player.playerScreenHandler.syncId,
                            sourceScreenSlot, previousSlot, SlotActionType.SWAP, mc.player);
                }
            }
            this.selectHotbarSlot(previousSlot);
        }
        return true;
    }

    private int findAxeSlot(int start, int end) {
        for (int slot = end; slot >= start; slot--) {
            if (this.isAxe(mc.player.getInventory().getStack(slot))) {
                return slot;
            }
        }
        return -1;
    }

    private boolean isAxe(ItemStack stack) {
        return stack.isOf(Items.WOODEN_AXE)
                || stack.isOf(Items.STONE_AXE)
                || stack.isOf(Items.IRON_AXE)
                || stack.isOf(Items.GOLDEN_AXE)
                || stack.isOf(Items.DIAMOND_AXE)
                || stack.isOf(Items.NETHERITE_AXE);
    }

    private void swapAxeWithHotbar(int sourceScreenSlot, int hotbarSlot) {
        Runnable swap = () -> mc.interactionManager.clickSlot(
                mc.player.playerScreenHandler.syncId,
                sourceScreenSlot,
                hotbarSlot,
                SlotActionType.SWAP,
                mc.player
        );
        if (this.legitSwap.isEnabled()) {
            swap.run();
        } else {
            PlayerInventoryUtil.swapWithBypassGrim(swap);
        }
    }

    private void selectHotbarSlot(int slot) {
        if (slot < 0 || slot > 8 || mc.player.getInventory().selectedSlot == slot) {
            return;
        }
        mc.player.getInventory().selectedSlot = slot;
        mc.interactionManager.syncSelectedSlot();
    }

    @FastNative
    private void performAttack() {
        attacking = true;
        try {
            this.resetSprintForAttack();
            mc.interactionManager.attackEntity(mc.player, target);
            mc.player.swingHand(Hand.MAIN_HAND);

            if (ignoreWalls.isEnabled()) {
                mc.interactionManager.attackEntity(mc.player, target);
                mc.player.swingHand(Hand.MAIN_HAND);
            }

            if (rotationMode.is("HolyWorld")) {
                rotationAttackTicks = holyWorldRotation.onAttack();
            } else if (rotationMode.is("Shard")) {
                rotationAttackTicks = shardRotation.onAttack();
            }
        } finally {
            attacking = false;
        }
    }

    @FastNative
    public boolean isAttacking() {
        return attacking;
    }

    @FastNative
    public boolean shouldHoldSprintForCritical() {
        return this.isEnabled() && this.criticalSprintTicks > 0;
    }

    @FastNative
    private void resetSprintForAttack() {
        boolean criticalWindow = this.isFallingCriticalWindow();
        if (criticalWindow) {
            this.criticalSprintTicks = Math.max(this.criticalSprintTicks, 1);
        }

        if (!criticalWindow
                || !mc.player.isSprinting()
                || mc.player.isSwimming()
                || mc.player.isGliding()) {
            return;
        }

        mc.player.setSprinting(false);
        mc.player.sendSprintingPacket();
    }

    @FastNative
    private void clearPendingCriticalAttack() {
        this.pendingCriticalTarget = null;
        this.criticalSprintTicks = 0;
    }

    @FastNative
    private boolean deferCriticalAttack() {
        if (this.pendingCriticalTarget != null) {
            return true;
        }
        if (!this.isFallingCriticalWindow() || !mc.player.isSprinting()) {
            return false;
        }
        this.pendingCriticalTarget = this.target;
        this.criticalSprintTicks = 2;
        mc.player.setSprinting(false);
        return true;
    }

    private int sneakTicks = 0;

    @FastNative
    @EventTarget
    public void onTick(EventTick e) {
        if (mc.player == null || mc.world == null) {
            return;
        }
        if (this.criticalSprintTicks > 0) {
            this.criticalSprintTicks--;
        }
        if (this.rotationAttackTicks > 0) {
            this.rotationAttackTicks--;
        }
        if (this.target == null || !this.isValid(this.target)) {
            this.target = this.updateTarget();
            this.sneakTicks = 0;
        }
        this.updateElytraPrediction();
        if (this.target != null) {
            long attackDelay = this.tpsSync.isEnabled()
                    ? TpsSync.getAdjustedCooldown(458L, true)
                    : 458L;
            boolean moduleDelayReady = this.usesHumanAttackTiming() || this.hurtTimer.finished(attackDelay);
            if (this.pendingCriticalTarget != null) {
                if (this.pendingCriticalTarget != this.target || !this.isFallingCriticalWindow()) {
                    this.clearPendingCriticalAttack();
                } else if (!FastCriticals.INSTANCE.shouldDeferAuraAttack()
                        && this.isCanAttack() && moduleDelayReady) {
                    this.attackTarget();
                    this.pendingCriticalTarget = null;
                    this.hitCount++;
                    this.hurtTimer.reset();
                    return;
                } else {
                    return;
                }
            }
            if (!FastCriticals.INSTANCE.shouldDeferAuraAttack()
                    && this.isCanAttack() && moduleDelayReady) {
                if (this.deferCriticalAttack()) {
                    return;
                }
                this.attackTarget();
                this.hitCount++;
                this.hurtTimer.reset();
            }
        } else {
            this.clearPendingCriticalAttack();
            this.hitCount = 0;
        }
    }

    @FastNative
    @EventTarget
    public void onRender3D(EventRender3D e) {
        if (this.isEnabled() && this.target != null && chaseElytraTarget.isEnabled()
                && mc.player.isGliding() && target.isGliding()) {
            Box predictedBox = target.getBoundingBox().offset(getElytraHitboxOffset(target));
            int themeColor = Wyvern.getInstance().getThemeManager().getCurrentTheme().getColor().getRGB();
            Render3DUtil.drawBox(predictedBox, themeColor, 1.0F, true, true, false);
        }
    }

    @FastNative
    @EventTarget
    public void eventRotate(EventMotion e) {
        if (mc.player == null || this.rotationMode.is("None")) return;
        if (this.target == null) {
            if (this.rotationMode.is("Shard")) this.shardRotation.reset();
            if (this.rotationMode.is("LegitSnap")) this.legitSnapRotation.reset();
            if (this.rotationMode.is("FunTime")) this.funTimeRotation.reset();
            return;
        }

        Rotation angle = RotationUtil.fromVec3d(getVec3d(this.target).subtract(mc.player.getEyePos()));
        if (mc.player.isGliding()) {
            ElytraTargetRotation.rotation(angle);
            e.setYaw(angle.getYaw());
            e.setPitch(angle.getPitch());
            return;
        }
        this.applySelectedRotation(angle, false);
        e.setYaw(this.lastYaw);
        e.setPitch(this.lastPitch);
    }

    @FastNative
    public void applyElytraModeRotation(Rotation angle) {
        this.applySelectedRotation(angle, true);
    }

    @FastNative
    private void applySelectedRotation(Rotation angle, boolean elytraRotation) {
        float attackDist = this.distance.getCurrent();

        // TriggerBot leaves the camera rotation to the player/AimAssist. Aura
        // only validates that the resulting visible rotation is on target.
        if (this.rotationMode.is("TriggerBot")) {
            this.lastYaw = mc.player.getYaw();
            this.lastPitch = mc.player.getPitch();
            return;
        }

        if (this.rotationMode.is("Shard")) {
            shardRotation.setYaw(this.lastYaw);
            shardRotation.setPitch(this.lastPitch);
            shardRotation.update(this.target, angle, elytraRotation, this.rotationAttackTicks);
            this.lastYaw = shardRotation.getYaw();
            this.lastPitch = shardRotation.getPitch();
            return;
        }
        if (this.rotationMode.is("FunTime")) {
            boolean attack = this.shouldSnapSlothRotation(this.target) || this.isAttacking();
            funTimeRotation.setYaw(this.lastYaw);
            funTimeRotation.setPitch(this.lastPitch);
            funTimeRotation.update(this.target, attack, attackDist);
            this.lastYaw = funTimeRotation.getYaw();
            this.lastPitch = funTimeRotation.getPitch();
            return;
        }
        if (this.rotationMode.is("LegitSnap")) {
            boolean attack = this.shouldSnapSlothRotation(this.target) || this.isAttacking();
            legitSnapRotation.setYaw(this.lastYaw);
            legitSnapRotation.setPitch(this.lastPitch);
            legitSnapRotation.update(this.target, attack);
            this.lastYaw = legitSnapRotation.getYaw();
            this.lastPitch = legitSnapRotation.getPitch();
            return;
        }
        if (this.rotationMode.is("Spooky")) {
            spookyRotation.setYaw(this.lastYaw);
            spookyRotation.setPitch(this.lastPitch);
            spookyRotation.update(this.target, angle, elytraRotation);
            this.lastYaw = spookyRotation.getYaw();
            this.lastPitch = spookyRotation.getPitch();
            return;
        }
        if (this.rotationMode.is("HolyWorld")) {
            holyWorldRotation.setYaw(this.lastYaw);
            holyWorldRotation.setPitch(this.lastPitch);
            holyWorldRotation.update(this.target, angle, elytraRotation, this.rotationAttackTicks);
            this.lastYaw = holyWorldRotation.getYaw();
            this.lastPitch = holyWorldRotation.getPitch();
            return;
        }
        if (this.rotationMode.is("ReallyWorld")) {
            reallyWorldRotation.setYaw(this.lastYaw);
            reallyWorldRotation.setPitch(this.lastPitch);
            reallyWorldRotation.update(angle, elytraRotation);
            this.lastYaw = reallyWorldRotation.getYaw();
            this.lastPitch = reallyWorldRotation.getPitch();
            return;
        }

        float targetYaw = angle.getYaw();
        float targetPitch = MathHelper.clamp(angle.getPitch(), -90.0F, 90.0F);
        float gcd = Rotation.gcd();
        if (gcd > 0.0F) {
            targetYaw -= (targetYaw - this.lastYaw) % gcd;
            targetPitch -= (targetPitch - this.lastPitch) % gcd;
        }
        float speed = elytraRotation ? 1440.0F : 360.0F;
        int priority = elytraRotation ? 3 : 1;
        RotationComponent.update(new Rotation(targetYaw, targetPitch),
                speed, speed, 30.0F, 25.5F, 0, priority, false);
        this.lastYaw = targetYaw;
        this.lastPitch = targetPitch;
    }

    @FastNative
    public Vec3d getVec3d(LivingEntity entity) {
        if (entity == null) return null;

        double scanDistance = mc.player != null && mc.player.isGliding()
                ? Math.max(this.rotateElytra.getCurrent(),
                this.distance.getCurrent() + this.distanceRotation.getCurrent())
                : this.distance.getCurrent();
        Vec3d point;
        if (this.shouldPredictElytraTarget(entity)) {
            point = entity.getBoundingBox().getCenter();
        } else {
            point = MultipointUtils.getNearestPoint(entity, scanDistance);
        }
        if (this.shouldPredictElytraTarget(entity)) {
            point = point.add(this.getElytraHitboxOffset(entity));
        }
        return point;
    }

    @FastNative
    private boolean shouldPredictElytraTarget(LivingEntity entity) {
        return mc.player != null
                && mc.player.isGliding()
                && entity instanceof PlayerEntity
                && entity.isGliding()
                && this.chaseElytraTarget.isEnabled();
    }

    @FastNative
    private Vec3d getElytraHitboxOffset(LivingEntity entity) {
        if (!this.shouldPredictElytraTarget(entity)) {
            return Vec3d.ZERO;
        }
        this.ensureElytraPrediction(entity);
        return this.elytraPredictionOffset;
    }

    @FastNative
    private void ensureElytraPrediction(LivingEntity entity) {
        if (entity != null && this.elytraPredictionTargetId != entity.getId()) {
            this.initializeElytraPrediction(entity);
        }
    }

    @FastNative
    private void initializeElytraPrediction(LivingEntity entity) {
        this.elytraPredictionTargetId = entity.getId();
        this.elytraPredictionOffset = this.calculateElytraPredictionOffset(entity);
    }

    @FastNative
    private void updateElytraPrediction() {
        if (this.target == null || !this.shouldPredictElytraTarget(this.target)) {
            this.resetElytraOvertake();
            return;
        }
        if (this.elytraPredictionTargetId != this.target.getId()) {
            this.initializeElytraPrediction(this.target);
            return;
        }

        this.elytraPredictionOffset = this.smoothElytraPredictionOffset(
                this.calculateElytraPredictionOffset(this.target));
    }

    @FastNative
    private Vec3d smoothElytraPredictionOffset(Vec3d raw) {
        return new Vec3d(
                this.elytraPredictionOffset.x + (raw.x - this.elytraPredictionOffset.x) * 0.35D,
                this.elytraPredictionOffset.y + (raw.y - this.elytraPredictionOffset.y) * 0.35D,
                this.elytraPredictionOffset.z + (raw.z - this.elytraPredictionOffset.z) * 0.35D
        );
    }

    @FastNative
    private Vec3d calculateElytraPredictionOffset(LivingEntity entity) {
        Vec3d movementPerTick = new Vec3d(
                entity.getX() - entity.prevX,
                entity.getY() - entity.prevY,
                entity.getZ() - entity.prevZ
        );
        return movementPerTick.multiply(this.chaseTicks.getCurrent());
    }

    @FastNative
    private void resetElytraOvertake() {
        this.elytraPredictionTargetId = Integer.MIN_VALUE;
        this.elytraPredictionOffset = Vec3d.ZERO;
    }

    /**
     * Compatibility hook for the firework mixin. Overtake only changes the
     * tick-predicted aim point; vanilla/firework velocity must stay untouched.
     */
    @FastNative
    public Vec3d applyMovementOvertake(Vec3d velocity) {
        return velocity;
    }


    @FastNative
    private void spoofWallBypass() {
        try {
            Vec3d eye = mc.player.getEyePos();
            Vec3d aimPoint = this.target.getPos().add(
                    0,
                    this.target.getHeight() / 1.2f + (this.target.getY() - this.target.prevY),
                    0
            );
            Vec3d dirVec = aimPoint.subtract(eye).normalize();
            Vec3d currentStart = eye;

            for (int i = 0; i < 2; i++) {
                HitResult result = mc.world.raycast(new RaycastContext(
                        currentStart,
                        aimPoint,
                        RaycastContext.ShapeType.COLLIDER,
                        RaycastContext.FluidHandling.NONE,
                        mc.player
                ));

                if (result.getType() != HitResult.Type.BLOCK) break;

                BlockHitResult blockHit = (BlockHitResult) result;
                BlockPos pos = blockHit.getBlockPos();
                Direction face = blockHit.getSide();
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.START_DESTROY_BLOCK, pos, face, 0
                ));
                mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                        PlayerActionC2SPacket.Action.ABORT_DESTROY_BLOCK, pos, face, 0
                ));
                Vec3d hit = blockHit.getPos();
                currentStart = new Vec3d(
                        hit.x + dirVec.x * 0.05,
                        hit.y + dirVec.y * 0.05,
                        hit.z + dirVec.z * 0.05
                );

                if (currentStart.squaredDistanceTo(aimPoint) < 0.0025) break;
            }
        } catch (Throwable ignored) {}
    }

    @FastNative
    private boolean isTargetBehindWall() {
        if (this.ignoreWalls.isEnabled()) return false;
        if (this.target == null || mc.player == null || mc.world == null) return false;

        Vec3d eye = mc.player.getEyePos();
        Vec3d targetEye = this.target.getPos().add(
                0,
                this.target.getEyeHeight(this.target.getPose()),
                0
        );

        HitResult result = mc.world.raycast(new RaycastContext(
                eye,
                targetEye,
                RaycastContext.ShapeType.COLLIDER,
                RaycastContext.FluidHandling.NONE,
                mc.player
        ));

        return result.getType() == HitResult.Type.BLOCK;
    }

    @FastNative
    private boolean isCanAttack() {
        boolean humanTiming = this.usesHumanAttackTiming();
        boolean holyWorldTiming = this.rotationMode.is("HolyWorld");
        boolean shardTiming = this.rotationMode.is("Shard");
        float requiredGate = 0.9F;
        if (holyWorldTiming) {
            requiredGate = this.holyWorldRotation.getCooldownGate();
        } else if (shardTiming) {
            requiredGate = this.shardRotation.getCooldownGate();
        }

        if (mc.player.getAttackCooldownProgress(0.5F) < requiredGate) {
            return false;
        }
        if (!this.isNaturalCriticalAttackReady()) {
            return false;
        }
        boolean whiffArmed = (holyWorldTiming && this.holyWorldRotation.isWhiffArmed())
                || (shardTiming && this.shardRotation.isWhiffArmed());
        if ((this.onlyOnAim.isEnabled() || humanTiming || this.rotationMode.is("TriggerBot"))
                && !this.isRotationAimedAtTarget()
                && !whiffArmed) {
            return false;
        }
        if ((holyWorldTiming || shardTiming) && this.rotationAttackTicks > 0) {
            return false;
        }
        if (this.usesHumanAttackTiming()
                && (mc.currentScreen != null || this.isTargetBehindWall())) {
            return false;
        }
        double range = (double) this.distance.getCurrent();
        // A predicted box may steer the flight, but it must never authorize a
        // hit outside the current server-side reach.
        return mc.player.getEyePos().distanceTo(MultipointUtils.getNearestPoint(this.target, range)) <= range;
    }

    public boolean shouldSnapSlothRotation(LivingEntity entity) {
        return entity != null && entity == this.target && this.isCanAttack();
    }

    @FastNative
    private boolean isNaturalCriticalAttackReady() {
        if (mc.player.isGliding()) {
            return true;
        }
        if (AirStuck.INSTANCE.isEnabled() && AirStuck.INSTANCE.frozen) {
            return true;
        }
        if (this.smartCriticals.isEnabled() && mc.player.isOnGround()) {
            boolean jumpStarted = mc.options.jumpKey.isPressed()
                    || mc.player.jumping
                    || mc.player.getVelocity().y > 1.0E-4D;
            return !jumpStarted;
        }
        return isFallingCriticalWindow();
    }

    @FastNative
    private boolean isFallingCriticalWindow() {
        return !mc.player.isOnGround()
                && mc.player.fallDistance > 0.0F
                && mc.player.getVelocity().y < -1.0E-4D
                && !mc.player.isTouchingWater()
                && !mc.player.isInLava()
                && !mc.player.isClimbing()
                && !mc.player.hasVehicle()
                && !mc.player.getAbilities().flying;
    }

    @FastNative
    private boolean isRotationAimedAtTarget() {
        if (this.target == null || mc.player == null) {
            return false;
        }

        Rotation serverRotation = new Rotation(mc.player.getYaw(), mc.player.getPitch());
        double range = mc.player.isGliding()
                ? Math.max(this.rotateElytra.getCurrent(), this.distance.getCurrent() + this.distanceRotation.getCurrent())
                : this.distance.getCurrent() + this.distanceRotation.getCurrent();

        Box currentBox = this.target.getBoundingBox().expand(mc.player.isGliding() ? 0.35D : 0.18D);
        if (RaytracingUtil.rayTrace(serverRotation.toVector(), range + 0.5D, currentBox)) {
            return true;
        }

        return false;
    }

    // Не FastNative: это перебор/фильтрация по сеттингам (не ценная логика для
    // защиты), а j2c не транслирует лямбды/стримы, которые тут могут появиться
    // из зависимостей (isFriend/isBot/getNearestPoint) - каждый такой вызов
    // из нативного кода означает лишний прыжок через JNI. Нативку оставляем
    // на ротациях/таймингах атаки (attackTarget, isRotationAimedAtTarget,
    // eventRotate) - там она реально нужна как защита ценной логики.
    private LivingEntity updateTarget() {
        if (!this.isEnabled()) {
            return null;
        }
        boolean humanTiming = this.usesHumanAttackTiming();
        LivingEntity best = null;
        double bestScore = Double.POSITIVE_INFINITY;
        Iterator<Entity> var2 = mc.world.getEntities().iterator();

        // Single linear pass picking the best candidate directly, instead of
        // collecting into a List and sorting with a lambda Comparator: j2c
        // does not native-transpile invokedynamic/lambda bodies, so every
        // comparator call during a sort was a native<->JVM (JNI) round trip
        // per comparison - this avoids that entirely and drops the cost from
        // O(n log n) crossings to a single O(n) native-only scan.
        while(var2.hasNext()) {
            Entity entity = var2.next();
            if (entity instanceof LivingEntity) {
                LivingEntity living = (LivingEntity)entity;
                if (this.isValid(living)) {
                    double score = this.getSortScore(living, humanTiming);
                    if (score < bestScore) {
                        bestScore = score;
                        best = living;
                    }
                }
            }
        }

        return best;
    }

    /** Чем меньше значение, тем выше приоритет цели. */
    private double getSortScore(LivingEntity entity, boolean humanTiming) {
        if (this.sortMode.is("Дистанция")) {
            return entity.squaredDistanceTo(mc.player);
        }
        if (this.sortMode.is("Здоровье")) {
            return entity.getHealth() + entity.getAbsorptionAmount();
        }
        if (this.sortMode.is("Броня")) {
            // Инвертируем: первым идёт самый упакованный, а не голый.
            return -this.getArmorScore(entity);
        }
        if (this.sortMode.is("Всё сразу")) {
            return this.getMixedScore(entity);
        }

        // "Авто" — прежнее поведение: по дистанции на человечных таймингах,
        // иначе по минимальному довороту.
        if (humanTiming) {
            return entity.squaredDistanceTo(mc.player);
        }
        return this.getAngleScore(entity);
    }

    private double getAngleScore(LivingEntity entity) {
        Rotation vec = Rotation.getRotations(entity.getBoundingBox().getCenter());
        double dy = Math.abs(MathHelper.wrapDegrees(vec.getYaw() - mc.player.getYaw()));
        double dp = Math.abs(MathHelper.wrapDegrees(vec.getPitch() - mc.player.getPitch()));
        return dy + dp;
    }

    /**
     * Взвешенная смесь: ближе, слабее и при этом в броне - выше приоритет.
     * Все составляющие нормируются в 0..1, поэтому веса читаются как проценты.
     */
    private double getMixedScore(LivingEntity entity) {
        double maxRange = Math.max(1.0D, this.distance.getCurrent() + this.distanceRotation.getCurrent());
        double distanceNorm = MathHelper.clamp(
                mc.player.getEyePos().distanceTo(entity.getBoundingBox().getCenter()) / maxRange, 0.0D, 1.0D);
        double healthNorm = MathHelper.clamp(
                (entity.getHealth() + entity.getAbsorptionAmount()) / 36.0D, 0.0D, 1.0D);
        double armorNorm = 1.0D - MathHelper.clamp(this.getArmorScore(entity) / 28.0D, 0.0D, 1.0D);
        double angleNorm = MathHelper.clamp(this.getAngleScore(entity) / 180.0D, 0.0D, 1.0D);

        return distanceNorm * 0.4D + healthNorm * 0.25D + armorNorm * 0.2D + angleNorm * 0.15D;
    }

    /**
     * Очки брони. Атрибут брони у чужих игроков может не успеть приехать, поэтому
     * к нему добавляется вес самих надетых частей - голый гарантированно окажется
     * ниже одетого.
     */
    private float getArmorScore(LivingEntity entity) {
        float score = entity.getArmor();
        for (ItemStack stack : entity.getArmorItems()) {
            if (!stack.isEmpty()) {
                score += 2.0F;
            }
        }
        return score;
    }

    public boolean isValid(LivingEntity entity) {
        if (entity == mc.player) {
            return false;
        } else if (entity.isAlive() && !(entity.getHealth() <= 0.0F)) {
            if (mc.player.isAlive() && !(mc.player.getHealth() <= 0.0F)) {
                if (entity instanceof PlayerEntity) {
                    PlayerEntity player = (PlayerEntity)entity;
                    boolean invisible = player.isInvisible();
                    boolean naked = this.isNaked(player);

                    // Для невидимых решает только галка "Невидимых": под невидимкой
                    // броня почти всегда снята, и старая связка с "Голых" из-за
                    // этого выключала таргет на них.
                    if (invisible) {
                        if (!this.targetTypeSetting.isEnable("Невидимых")) return false;
                    } else if (naked) {
                        if (!this.targetTypeSetting.isEnable("Голых")) return false;
                    } else if (!this.targetTypeSetting.isEnable("Игроков")) {
                        return false;
                    }

                    if (!this.targetTypeSetting.isEnable("Друзей") && Wyvern.INSTANCE.getFriendManager().isFriend(entity.getName().getString())) {
                        return false;
                    }

                    if (AntiBot.INSTANCE.isBot(player)) {
                        return false;
                    }

                }

                if (!(entity instanceof PassiveEntity) && !(entity instanceof FishEntity) || this.targetTypeSetting.isEnable("Животных") && !Wyvern.INSTANCE.getServerHandler().isPvp()) {
                    if (!(entity instanceof HostileEntity) && !(entity instanceof AmbientEntity) || this.targetTypeSetting.isEnable("Мобов") && !Wyvern.INSTANCE.getServerHandler().isPvp()) {
                        double validRange = mc.player.isGliding()
                                ? Math.max(this.rotateElytra.getCurrent(), this.distance.getCurrent() + this.distanceRotation.getCurrent())
                                : this.distance.getCurrent() + this.distanceRotation.getCurrent();
                        // Cheap bounding-box-center distance check for candidate filtering
                        // during the search scan (runs once per world entity, every tick
                        // with no locked target). MultipointUtils.getNearestPoint does a
                        // ~900-raycast grid scan and only memoizes the LAST checked entity,
                        // so calling it here per-candidate defeated the cache entirely and
                        // ran the full scan for every entity in range - the actual precise
                        // nearest-visible-point is still computed (and cached) once a
                        // target is locked, via the other call sites (isRotationAimedAtTarget,
                        // eventRotate, attackTarget) that repeatedly query the same target.
                        if (mc.player.getEyePos().distanceTo(entity.getBoundingBox().getCenter()) > validRange) {
                            return false;
                        } else {
                            return !(entity instanceof ArmorStandEntity);
                        }
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @FastNative
    @EventTarget
    private void setCorrection(EventMoveInput eventMoveInput) {
        if (this.shouldControlMovementCorrection()) {
            if (this.correctionMode.is("Таргетированная")) {
                Rotation targetRotation = RotationUtil.fromVec3d(this.target.getBoundingBox().getCenter().subtract(mc.player.getEyePos()));
                MovingUtil.fixMovementTowards(eventMoveInput, targetRotation.getYaw());
            } else {
                MovingUtil.fixMovementFree(eventMoveInput);
            }
        }
    }

    @FastNative
    public boolean isIgnoreWalls() {
        return this.ignoreWalls.isEnabled();
    }

    @FastNative
    public LivingEntity getTarget() {
        return this.isEnabled() ? this.target : null;
    }

    /**
     * RW2 FastCriticals calls Aura after the player's movement packet. Keeping
     * the complete timing check here prevents a second, unsynchronised attack
     * from the regular tick handler.
     */
    @FastNative
    public boolean tryPostMotionAttack() {
        if (!this.isEnabled() || this.target == null || !this.isValid(this.target)) {
            return false;
        }

        long adjustedDelay = this.tpsSync.isEnabled() ? TpsSync.getAdjustedCooldown(458L, true) : 458L;
        boolean moduleDelayReady = this.usesHumanAttackTiming() || this.hurtTimer.finished(adjustedDelay);
        if (!this.isCanAttack() || !moduleDelayReady) {
            return false;
        }

        if (this.pendingCriticalTarget != null) {
            if (this.pendingCriticalTarget != this.target
                    || !this.isFallingCriticalWindow()) {
                this.clearPendingCriticalAttack();
                return false;
            }
        } else if (this.deferCriticalAttack()) {
            return false;
        }

        this.attackTarget();
        this.pendingCriticalTarget = null;
        this.hitCount++;
        this.hurtTimer.reset();
        return true;
    }

    /** Compatibility hook for the firework mixin; the legacy flight keeps its original velocity. */
    @FastNative
    public Vec3d applyOvertakeSlowdown(Vec3d velocity) {
        return velocity;
    }

    @FastNative
    public boolean shouldControlMovementCorrection() {
        return this.isEnabled()
                && this.target != null
                && mc.player != null
                && !mc.player.isGliding()
                && !this.rotationMode.is("None");
    }

    @FastNative
    private boolean usesHumanAttackTiming() {
        return this.rotationMode.is("HolyWorld");
    }

    @FastNative
    private boolean isNaked(PlayerEntity player) {
        for (ItemStack stack : player.getArmorItems()) {
            if (!stack.isEmpty()) {
                return false;
            }
        }

        return true;
    }

    @FastNative
    public void onEnable() {
        this.target = null;
        this.hitCount = 0;
        this.clearPendingCriticalAttack();
        this.rotationAttackTicks = 0;
        this.shardRotation.reset();
        this.legitSnapRotation.reset();
        this.funTimeRotation.reset();
        this.holyWorldRotation.reset();
        this.reallyWorldRotation.reset();
        this.resetElytraOvertake();
        super.onEnable();
    }

    @FastNative
    public void onDisable() {
        this.hitCount = 0;
        this.clearPendingCriticalAttack();
        this.rotationAttackTicks = 0;
        if (this.usesHumanAttackTiming() && RotationComponent.instance.isRotating()) {
            RotationComponent.instance.currentTask(RotationComponent.RotationTask.RESET);
            RotationComponent.instance.idleTicks(0);
        }
        this.shardRotation.reset();
        this.legitSnapRotation.reset();
        this.funTimeRotation.reset();
        this.spookyRotation.setYaw(0);
        this.spookyRotation.setPitch(0);
        this.holyWorldRotation.reset();
        this.reallyWorldRotation.reset();
        this.resetElytraOvertake();
        super.onDisable();
    }

    @FastNative
    public int getPing() {
        if (mc.getNetworkHandler() != null && mc.player != null) {
            PlayerListEntry entry = mc.getNetworkHandler().getPlayerListEntry(mc.player.getUuid());
            if (entry != null) return entry.getLatency();
        }
        return 50;
    }
}
