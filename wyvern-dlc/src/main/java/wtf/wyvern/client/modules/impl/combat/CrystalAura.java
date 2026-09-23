package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.ShapeContext;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gl.ShaderProgramKeys;
import net.minecraft.client.render.BufferBuilder;
import net.minecraft.client.render.BufferRenderer;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.VertexFormat;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import org.joml.Matrix4f;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.animations.base.Animation;
import wtf.wyvern.core.animations.base.Easing;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.render.EventRender3D;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.combat.ExplosionUtility;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.render.level.Render3DUtil;

import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.ArrayList;
import java.util.HashMap;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "CrystallAura",
        category = Category.COMBAT,
        description = "Автоматически ставит и ломает кристаллы"
)
public class CrystalAura extends Module {

    public static final CrystalAura INSTANCE = new CrystalAura();
    private static final int ROTATION_PRIORITY = 999;
    private static final float TARGET_RANGE = 10.0F;
    private static final double PLACE_RANGE = 4.5D;
    private static final double BREAK_RANGE = 4.5D;
    private static final double DIG_RANGE = 4.5D;

    private static final double MIN_DAMAGE = 4.0D;
    private static final double MIN_BREAK_DAMAGE = 4.0D;
    private static final double MAX_SELF_DAMAGE = 8.0D;
    private static final double MIN_ADVANTAGE = 0.0D;
    private static final double MAX_FRIEND_DAMAGE = 2.0D;
    private static final float MIN_DIG_SPEED = 0.5F;

    private static final int PREDICT_TICKS = 3;
    private static final int PREDICT_FALL_MAX_TICKS = 20;
    private static final long BREAK_DELAY = 30L;
    private static final long PLACE_DELAY = 40L;
    private static final long OBSIDIAN_DELAY = 65L;
    private static final long OBSIDIAN_CONFIRM_TIMEOUT = 600L;
    private static final long MINING_TIMEOUT = 8000L;
    private static final long PLACE_MEMORY = 150L;
    private static final long PLACE_FAILURE_MEMORY = 500L;
    private static final long DIG_FAILURE_MEMORY = 1000L;
    private static final long CRYSTAL_SPACE_TIMEOUT = 750L;
    private enum DigPhase { MINE, OBSIDIAN, CRYSTAL }

    private enum VisualKind {
        DIG(0xFFFF4058),
        OBSIDIAN(0xFF40B9FF),
        PLACE(0xFFAF55FF);

        final int color;

        VisualKind(int color) {
            this.color = color;
        }
    }

    private enum OverlayPhase { DIG, OBSIDIAN, PLACE }

    /** Точка работы подкопа: base — куда ляжет обсидиан (и что копаем, если блок твёрдый). */
    private record DigSpot(BlockPos base, int toolSlot, Direction obsidianSide,
    double damage, double selfDamage, double score, boolean needDig) {}

    private record PlaceSpot(BlockPos base, double damage, double selfDamage) {}

    private record BreakSpot(EndCrystalEntity crystal, double damage, double selfDamage) {}

    private LivingEntity activeTarget;
    private long targetLockTime;
    private DigSpot activeDig;
    private DigPhase digPhase = DigPhase.MINE;
    private long digStartTime;
    private long lastObsidianTime;
    private long crystalWaitStartTime;
    private BlockPos miningPos;
    private boolean miningStarted;
    private int previousSlot = -1;
    private int lastSetSlot = -1;
    private int swappedInvSlot = -1;
    private int swappedHotbarSlot = -1;
    private long pendingObsidianTime;
    private long lastBreakTime;
    private long lastPlaceTime;
    private final Map<BlockPos, Long> recentPlaces = new HashMap<>();
    private final Map<BlockPos, Long> failedPlaceSpots = new HashMap<>();
    private final Map<BlockPos, Long> failedDigSpots = new HashMap<>();
    private BlockPos overlayBase;
    private BlockPos overlayCrystal;
    private double overlayDamage;
    private double overlaySelf;
    private OverlayPhase overlayPhase;
    private final ArrayList<VisualBox> visuals = new ArrayList<>();
    private static final class VisualBox {
        final BlockPos pos;
        final VisualKind kind;
        final Animation animation = new Animation(650, 0.0F, Easing.CUBIC_OUT);

        VisualBox(BlockPos pos, VisualKind kind) {
            this.pos = pos;
            this.kind = kind;
            this.animation.setValue(1.0F);
        }
    }

    private void pushVisual(BlockPos pos, VisualKind kind) {
        if (pos == null) return;
        visuals.removeIf(v -> Objects.equals(v.pos, pos) && v.kind != kind);
        for (VisualBox v : visuals) {
            if (Objects.equals(v.pos, pos) && v.kind == kind) {
                v.animation.setValue(1.0F);
                return;
            }
        }
        visuals.add(new VisualBox(pos.toImmutable(), kind));
    }

    private CrystalAura() {}
    @FastNative
    @Override
    public void onDisable() {
        super.onDisable();
        if (mc.player != null && swappedInvSlot != -1
                && mc.player.currentScreenHandler.syncId != 0) {
            mc.player.closeHandledScreen();
        }
        restoreSlot();
        clearDig();
        activeTarget = null;
        targetLockTime = 0L;
        recentPlaces.clear();
        failedPlaceSpots.clear();
        failedDigSpots.clear();
        visuals.clear();
    }

    @FastNative
    private boolean nullCheck() {
        return mc.player == null || mc.world == null || mc.interactionManager == null;
    }

    @FastNative
    @EventTarget
    private void onTick(EventTick event) {
        overlayBase = null;
        overlayCrystal = null;
        overlayDamage = 0.0D;
        overlaySelf = 0.0D;
        overlayPhase = null;

        if (nullCheck()) {
            restoreSlot();
            return;
        }
        if (mc.player.currentScreenHandler.syncId != 0) {
            stopMining();
            return;
        }

        cleanupRecentPlaces();
        cleanupFailedPlaceSpots();
        cleanupFailedDigSpots();

        LivingEntity target = findTarget();
        activeTarget = target;
        if (target == null) {
            targetLockTime = 0L;
            clearDig();
            restoreSlot();
            return;
        }

        if (canBreakNow()) {
            BreakSpot bs = findBestBreak(target);
            if (bs != null) {
                restoreSlot();
                setOverlay(bs.crystal().getBlockPos().down(), bs.damage(), bs.selfDamage(), OverlayPhase.OBSIDIAN);
                if (breakCrystal(bs)) {
                    clearDig();
                    return;
                }
            }
        }

        if (canPlaceNow()) {
            PlaceSpot ps = findBestPlace(target);
            if (ps != null) {
                if (activeDig != null && activeDig.base().equals(ps.base())) {
                    confirmObsidian(ps.base());
                }
                setOverlay(ps.base(), ps.damage(), ps.selfDamage(), OverlayPhase.PLACE);
                if (placeCrystalOnBase(ps.base(), ps.damage(), ps.selfDamage())) {
                    clearDig();
                    return;
                }
            }
        }

        if (handleDig(target)) {
            return;
        }

        clearDig();
        restoreSlot();
    }

    private LivingEntity findTarget() {
        if (isActiveTargetValid()) {
            return activeTarget;
        }

        LivingEntity best = null;
        double bestScore = -Double.MAX_VALUE;
        Box area = mc.player.getBoundingBox().expand(TARGET_RANGE);
        for (LivingEntity e : mc.world.getEntitiesByClass(LivingEntity.class, area, this::isValidTarget)) {
            double score = targetScore(e);
            if (score > bestScore) {
                bestScore = score;
                best = e;
            }
        }
        if (best != null) {
            targetLockTime = System.currentTimeMillis();
        }
        return best;
    }

    @FastNative
    private boolean isActiveTargetValid() {
        if (activeTarget == null || !activeTarget.isAlive() || activeTarget.isRemoved()) return false;
        if (!(activeTarget instanceof PlayerEntity p)) return false;
        if (p.isSpectator() || p.isCreative()) return false;
        if (Wyvern.getInstance().getFriendManager().isFriend(p.getName().getString())) return false;
        if (mc.player.distanceTo(activeTarget) > TARGET_RANGE + 0.75F) return false;
        return System.currentTimeMillis() - targetLockTime < 350L;
    }

    @FastNative
    private double targetScore(LivingEntity target) {
        double distanceScore = TARGET_RANGE - mc.player.distanceTo(target);
        double healthScore = 40.0D - combinedHealth(target);
        double visibilityScore = mc.player.canSee(target) ? 3.0D : 0.0D;
        double airborneScore = !target.isOnGround() ? 1.25D : 0.0D;
        return distanceScore + healthScore + visibilityScore + airborneScore;
    }

    @FastNative
    private boolean isValidTarget(LivingEntity e) {
        if (e == mc.player || !e.isAlive() || e.isRemoved()) return false;
        if (!(e instanceof PlayerEntity p)) return false;
        if (p.isSpectator() || p.isCreative()) return false;
        if (Wyvern.getInstance().getFriendManager().isFriend(p.getName().getString())) return false;
        return mc.player.distanceTo(e) <= TARGET_RANGE;
    }

    private BreakSpot findBestBreak(LivingEntity target) {
        BreakSpot best = null;
        double bestScore = -Double.MAX_VALUE;
        Box area = mc.player.getBoundingBox().expand(BREAK_RANGE + 1.0D);
        for (EndCrystalEntity crystal : mc.world.getEntitiesByClass(EndCrystalEntity.class, area, Entity::isAlive)) {
            if (!isWithinRange(crystal.getBoundingBox().getCenter(), BREAK_RANGE)) continue;
            if (!canAttackCrystal(crystal)) continue;

            BlockPos base = crystal.getBlockPos().down();
            double dmg = explosionDamage(base, target, true);
            double self = explosionDamage(base, mc.player, true);
            if (dmg < MIN_BREAK_DAMAGE && !isLethal(dmg, target)) continue;
            if (!selfSafe(self) || !friendSafe(base, true)) continue;

            double score = scoreBreak(crystal, base, dmg, self, target);
            score -= crystal.squaredDistanceTo(target) * 0.03D;
            if (score > bestScore) {
                bestScore = score;
                best = new BreakSpot(crystal, dmg, self);
            }
        }
        return best;
    }

    @FastNative
    private boolean breakCrystal(BreakSpot bs) {
        EndCrystalEntity crystal = bs.crystal();
        if (!crystal.isAlive() || !canAttackCrystal(crystal)) return false;
        rotateTo(crystal.getBoundingBox().getCenter());
        pushVisual(crystal.getBlockPos(), VisualKind.PLACE);
        mc.interactionManager.attackEntity(mc.player, crystal);
        mc.player.swingHand(Hand.MAIN_HAND);
        lastBreakTime = System.currentTimeMillis();
        return true;
    }

    @FastNative
    private boolean canAttackCrystal(EndCrystalEntity crystal) {
        return mc.player.distanceTo(crystal) <= BREAK_RANGE + 0.35F;
    }

    @FastNative
    private PlaceSpot findBestPlace(LivingEntity target) {
        if (!hasCrystal()) return null;

        PlaceSpot best = null;
        double bestScore = -Double.MAX_VALUE;
        BlockPos center = target.getBlockPos();
        int r = MathHelper.ceil(PLACE_RANGE);

        for (int x = -r; x <= r; x++) {
            for (int y = -3; y <= 2; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos base = center.add(x, y, z);
                    BlockState s = mc.world.getBlockState(base);
                    if (!s.isOf(Blocks.OBSIDIAN) && !s.isOf(Blocks.BEDROCK)) continue;
                    if (recentlyPlaced(base)) continue;
                    if (recentlyFailedPlace(base)) continue;
                    if (!isWithinRange(crystalCenter(base), PLACE_RANGE)) continue;
                    if (target.squaredDistanceTo(crystalCenter(base)) > 12.0D) continue;
                    if (!canHoldCrystal(base.up())) continue;

                    double dmg = explosionDamage(base, target, true);
                    double self = explosionDamage(base, mc.player, true);
                    if (!valid(dmg, self, base, target, true)) continue;

                    double score = scorePlace(base, dmg, self, target, true);
                    if (target.squaredDistanceTo(crystalCenter(base)) <= 2.25D) score += 3.0D;
                    score -= target.squaredDistanceTo(base.getX() + 0.5D, base.getY() + 1.0D, base.getZ() + 0.5D) * 0.5D;
                    if (score > bestScore) {
                        bestScore = score;
                        best = new PlaceSpot(base, dmg, self);
                    }
                }
            }
        }
        return best;
    }

    @FastNative
    private boolean placeCrystalOnBase(BlockPos base, double dmg, double self) {
        if (!canHoldCrystal(base.up())) {
            markPlaceFailed(base);
            restoreSlot();
            return false;
        }
        BlockHitResult hitResult = crystalHitResult(base);
        if (hitResult == null) {
            markPlaceFailed(base);
            restoreSlot();
            return false;
        }
        Hand hand = selectCrystal();
        if (hand == null) {
            markPlaceFailed(base);
            restoreSlot();
            return false;
        }
        Vec3d hit = hitResult.getPos();
        rotateTo(hit);
        ActionResult result = mc.interactionManager.interactBlock(mc.player, hand, hitResult);
        mc.player.swingHand(hand);

        boolean accepted = result.isAccepted();
        if (accepted) {
            failedPlaceSpots.remove(base);
            recentPlaces.put(base.toImmutable(), System.currentTimeMillis());
            pushVisual(base.up(), VisualKind.PLACE);
            setOverlay(base, dmg, self, OverlayPhase.PLACE);
            lastPlaceTime = System.currentTimeMillis();
        } else {
            markPlaceFailed(base);
        }
        restoreSlot();
        return accepted;
    }

    @FastNative
    private boolean handleDig(LivingEntity target) {
        if (activeDig == null || isDigStale(target)) {
            DigSpot spot = scanDig(digOrigin(target), target);
            if (spot == null) {
                clearDig();
                return false;
            }
            startDig(spot);
        }

        DigSpot spot = activeDig;
        BlockPos base = spot.base();
        BlockState state = mc.world.getBlockState(base);
        if (state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.BEDROCK)) {
            stopMining();
            confirmObsidian(base);
            if (!canHoldCrystal(base.up())) {
                setOverlay(base, spot.damage(), spot.selfDamage(), OverlayPhase.OBSIDIAN);
                restoreSlot();
                if (System.currentTimeMillis() - crystalWaitStartTime >= CRYSTAL_SPACE_TIMEOUT) {
                    clearDig();
                    return false;
                }
                return true;
            }
            if (!hasCrystal()) {
                clearDig();
                restoreSlot();
                return false;
            }
            setOverlay(base, spot.damage(), spot.selfDamage(), OverlayPhase.PLACE);
            if (recentlyFailedPlace(base)) {
                restoreSlot();
                if (System.currentTimeMillis() - crystalWaitStartTime >= CRYSTAL_SPACE_TIMEOUT) {
                    clearDig();
                    return false;
                }
                return true;
            }
            if (canPlaceNow()) {
                if (placeCrystalOnBase(base, spot.damage(), spot.selfDamage())) {
                    clearDig();
                } else if (System.currentTimeMillis() - crystalWaitStartTime >= CRYSTAL_SPACE_TIMEOUT) {
                    clearDig();
                    return false;
                }
            }
            return true;
        }

        if (isReplaceable(state)) {
            stopMining();
            setOverlay(base, spot.damage(), spot.selfDamage(), OverlayPhase.OBSIDIAN);
            if (isObsidianPending()) {
                return true;
            }
            if (!canPlaceObsidianAt(base)) {
                if (pendingObsidianTime != 0L) {
                    markDigFailed(base);
                }
                clearDig();
                return false;
            }
            digPhase = DigPhase.OBSIDIAN;
            placeObsidian(spot);
            return true;
        }

        if (isUnbreakable(base)) {
            clearDig();
            return false;
        }

        digPhase = DigPhase.MINE;
        setOverlay(base, spot.damage(), spot.selfDamage(), OverlayPhase.DIG);
        mineBlock(spot);
        return true;
    }

    @FastNative
    private void startDig(DigSpot spot) {
        stopMining();
        activeDig = spot;
        digPhase = spot.needDig() ? DigPhase.MINE : DigPhase.OBSIDIAN;
        digStartTime = System.currentTimeMillis();
        pendingObsidianTime = 0L;
        miningStarted = false;
        miningPos = null;
    }

    @FastNative
    private boolean isDigStale(LivingEntity target) {
        if (activeDig == null) return true;
        BlockPos base = activeDig.base();
        BlockState state = mc.world.getBlockState(base);
        if (!isWithinRange(crystalCenter(base), DIG_RANGE + 1.5D)) return true;
        if (isUnbreakable(base) && !state.isOf(Blocks.BEDROCK)) return true;
        if (isReplaceable(state) && !canPlaceObsidianAt(base) && !isObsidianPending()) return true;

        boolean obsidianExists = state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.BEDROCK);
        double damage = explosionDamage(base, target, obsidianExists);
        double self = explosionDamage(base, mc.player, obsidianExists);
        return !valid(damage, self, base, target, obsidianExists);
    }

    @FastNative
    private void mineBlock(DigSpot spot) {
        BlockPos base = spot.base();
        BlockState state = mc.world.getBlockState(base);
        if (isReplaceable(state)) {
            stopMining();
            restoreSlot();
            return;
        }
        if (isUnbreakable(base)) {
            clearDig();
            restoreSlot();
            return;
        }

        long now = System.currentTimeMillis();
        if (now - digStartTime > MINING_TIMEOUT) {
            markDigFailed(base);
            clearDig();
            restoreSlot();
            return;
        }

        int tool = spot.toolSlot() >= 0 ? spot.toolSlot() : findBestToolSlot(base);
        if (tool >= 0) {
            selectSlot(tool);
        }
        Direction side = bestMineFace(base);
        rotateTo(Vec3d.ofCenter(base));

        if (miningPos == null || !miningPos.equals(base) || !miningStarted) {
            stopMining();
            boolean started = mc.interactionManager.attackBlock(base, side);
            mc.player.swingHand(Hand.MAIN_HAND);
            if (!started) {
                restoreSlot();
                return;
            }
            miningPos = base.toImmutable();
            miningStarted = true;
            pushVisual(base, VisualKind.DIG);
            return;
        }

        boolean ok = mc.interactionManager.updateBlockBreakingProgress(base, side);
        BlockState updatedState = mc.world.getBlockState(base);
        if (isReplaceable(updatedState)) {
            stopMining();
            if (canPlaceObsidianAt(base)) {
                digPhase = DigPhase.OBSIDIAN;
                placeObsidian(spot);
            } else {
                clearDig();
                restoreSlot();
            }
            return;
        }
        if (!ok) {
            stopMining();
            restoreSlot();
            return;
        }
        if (mc.player.handSwingTicks == 0) {
            mc.player.swingHand(Hand.MAIN_HAND);
        }
    }

    @FastNative
    private void stopMining() {
        if (miningPos != null) {
            mc.interactionManager.cancelBlockBreaking();
            miningPos = null;
            miningStarted = false;
        }
    }

    @FastNative
    private void placeObsidian(DigSpot spot) {
        if (System.currentTimeMillis() - lastObsidianTime < OBSIDIAN_DELAY) {
            restoreSlot();
            return;
        }

        int slot = findItemSlot(Items.OBSIDIAN);
        if (slot == -1) {
            clearDig();
            restoreSlot();
            return;
        }

        BlockPos base = spot.base();
        BlockState state = mc.world.getBlockState(base);
        if (state.isOf(Blocks.OBSIDIAN) || state.isOf(Blocks.BEDROCK)) {
            confirmObsidian(base);
            restoreSlot();
            return;
        }
        if (!isReplaceable(state) || !canPlaceObsidianAt(base)) {
            clearDig();
            restoreSlot();
            return;
        }

        Direction side = spot.obsidianSide();
        if (side == null || !isSolid(base.offset(side))) {
            side = findObsidianSide(base);
        }
        if (side == null) {
            markDigFailed(base);
            clearDig();
            restoreSlot();
            return;
        }

        BlockPos neighbor = base.offset(side);
        Direction clickFace = side.getOpposite();
        Vec3d hit = facePoint(neighbor, clickFace);

        rotateTo(hit);
        selectSlot(slot);
        if (!mc.player.getMainHandStack().isOf(Items.OBSIDIAN)) {
            markDigFailed(base);
            clearDig();
            restoreSlot();
            return;
        }
        ActionResult result = mc.interactionManager.interactBlock(mc.player, Hand.MAIN_HAND,
                new BlockHitResult(hit, clickFace, neighbor, false));
        mc.player.swingHand(Hand.MAIN_HAND);
        restoreSlot();

        if (!result.isAccepted()) {
            markDigFailed(base);
            clearDig();
            return;
        }

        lastObsidianTime = System.currentTimeMillis();
        pendingObsidianTime = System.currentTimeMillis();
        digPhase = DigPhase.OBSIDIAN;
        BlockState placedState = mc.world.getBlockState(base);
        if (placedState.isOf(Blocks.OBSIDIAN) || placedState.isOf(Blocks.BEDROCK)) {
            confirmObsidian(base);
        } else if (System.currentTimeMillis() - pendingObsidianTime > OBSIDIAN_CONFIRM_TIMEOUT) {
            markDigFailed(base);
            clearDig();
        }
    }

    @FastNative
    private void confirmObsidian(BlockPos base) {
        failedDigSpots.remove(base);
        if (digPhase != DigPhase.CRYSTAL) {
            pushVisual(base, VisualKind.OBSIDIAN);
            crystalWaitStartTime = System.currentTimeMillis();
        }
        pendingObsidianTime = 0L;
        digPhase = DigPhase.CRYSTAL;
    }

    @FastNative
    private boolean isObsidianPending() {
        return pendingObsidianTime != 0L
                && System.currentTimeMillis() - pendingObsidianTime < OBSIDIAN_CONFIRM_TIMEOUT;
    }

    @FastNative
    private void clearDig() {
        stopMining();
        activeDig = null;
        digPhase = DigPhase.MINE;
        pendingObsidianTime = 0L;
        crystalWaitStartTime = 0L;
    }

    @FastNative
    private DigSpot scanDig(BlockPos feet, LivingEntity target) {
        if (findItemSlot(Items.OBSIDIAN) == -1 || !hasCrystal()) {
            return null;
        }

        DigSpot best = null;
        double bestScore = -Double.MAX_VALUE;
        int r = MathHelper.ceil(DIG_RANGE);

        for (int x = -r; x <= r; x++) {
            for (int y = -2; y <= 0; y++) {
                for (int z = -r; z <= r; z++) {
                    BlockPos base = feet.add(x, y, z);
                    BlockPos crystalPos = base.up();
                    if (recentlyPlaced(base)) continue;
                    if (recentlyFailedPlace(base)) continue;
                    if (isDigCoolingDown(base)) continue;
                    if (!isWithinRange(crystalCenter(base), DIG_RANGE)) continue;
                    if (!canHoldCrystal(crystalPos)) continue;

                    BlockState s = mc.world.getBlockState(base);
                    boolean needDig;
                    int tool = -1;

                    if (s.isOf(Blocks.OBSIDIAN) || s.isOf(Blocks.BEDROCK)) {
                        continue;
                    } else if (canPlaceObsidianAt(base)) {
                        needDig = false;
                    } else if (isReplaceable(s)) {
                        continue;
                    } else {
                        if (isUnbreakable(base)) continue;
                        tool = findBestToolSlot(base);
                        if (tool == -1) continue;
                        needDig = true;
                    }

                    Direction side = findObsidianSide(base);
                    if (side == null) continue;

                    double dmg = explosionDamage(base, target, false);
                    double self = explosionDamage(base, mc.player, false);
                    if (!valid(dmg, self, base, target, false)) continue;

                    double score = scorePlace(base, dmg, self, target, false);
                    if (target.squaredDistanceTo(crystalCenter(base)) <= 2.25D) score += 3.0D;
                    if (!needDig) score += 2.0D;
                    if (isCorner(base, feet)) score += 0.6D;
                    score -= target.squaredDistanceTo(base.getX() + 0.5D, base.getY() + 1.0D, base.getZ() + 0.5D) * 0.5D;

                    if (score > bestScore) {
                        bestScore = score;
                        best = new DigSpot(base.toImmutable(), tool, side, dmg, self, score, needDig);
                    }
                }
            }
        }
        return best;
    }

    @FastNative
    private BlockPos digOrigin(LivingEntity target) {
        if (target.isOnGround()) return target.getBlockPos();
        Vec3d vel = target.getVelocity();
        if (vel.y >= 0.0D) return target.getBlockPos();

        BlockPos landing = predictLanding(target);
        return landing != null ? landing : target.getBlockPos();
    }

    @FastNative
    private BlockPos predictLanding(LivingEntity target) {
        double x = target.getX();
        double y = target.getY();
        double z = target.getZ();
        Vec3d vel = target.getVelocity();
        double vy = vel.y;

        for (int t = 0; t < PREDICT_FALL_MAX_TICKS; t++) {
            vy = (vy - 0.08D) * 0.98D;
            y += vy;
            x += vel.x * 0.91D;
            z += vel.z * 0.91D;
            BlockPos feet = BlockPos.ofFloored(x, y, z);
            BlockState floor = mc.world.getBlockState(feet.down());
            if (!floor.isAir() && !floor.getCollisionShape(mc.world, feet.down()).isEmpty()) {
                return feet;
            }
        }
        return null;
    }

    @FastNative
    private double explosionDamage(BlockPos base, LivingEntity entity, boolean obsidianExists) {
        if (!(entity instanceof PlayerEntity player)) return 0.0D;
        Vec3d explosion = crystalCenter(base);
        int ticks = 0;
        if (obsidianExists) {
            return ExplosionUtility.getExplosionDamagePredict(explosion, player, ticks, false, false, false);
        }
        return ExplosionUtility.getDamageOfGhostBlockPredict(explosion, player, base, ticks, false, false);
    }

    @FastNative
    private boolean valid(double dmg, double self, BlockPos base, LivingEntity target, boolean obsidianExists) {
        if (dmg <= 0.05D) return false;
        boolean lethal = isLethal(dmg, target);
        boolean facePlacing = shouldFacePlace(target);
        if (!facePlacing && dmg < MIN_DAMAGE && !lethal) return false;
        if (facePlacing && dmg < 1.0D && !lethal) return false;
        if (!selfSafe(self)) return false;
        if (!lethal && dmg - self < MIN_ADVANTAGE) return false;
        return friendSafe(base, obsidianExists);
    }

    @FastNative
    private boolean isLethal(double dmg, LivingEntity target) {
        return dmg >= combinedHealth(target) - 0.5D;
    }

    @FastNative
    private boolean selfSafe(double self) {
        return self <= MAX_SELF_DAMAGE && self < combinedHealth(mc.player) - 0.5D;
    }

    @FastNative
    private boolean shouldFacePlace(LivingEntity target) {
        return false;
    }

    @FastNative
    private double scoreBreak(EndCrystalEntity crystal, BlockPos base, double dmg, double self, LivingEntity target) {
        double score = dmg - self * 0.6D;
        if (isLethal(dmg, target)) {
            score += 6.0D;
        }
        if (shouldFacePlace(target)) {
            score += 2.5D;
        }
        if (canSee(crystal.getBoundingBox().getCenter())) {
            score += 1.6D;
        }
        score -= mc.player.getEyePos().squaredDistanceTo(crystal.getBoundingBox().getCenter()) * 0.02D;
        if (!friendSafe(base, true)) {
            score -= 1000.0D;
        }
        return score;
    }

    @FastNative
    private double scorePlace(BlockPos base, double dmg, double self, LivingEntity target, boolean obsidianExists) {
        double score = dmg - self * 0.6D;
        if (isLethal(dmg, target)) {
            score += 4.0D;
        }
        if (shouldFacePlace(target)) {
            score += 2.0D;
        }
        if (canSee(crystalCenter(base))) {
            score += 1.2D;
        }
        if (obsidianExists) {
            score += 0.8D;
        }
        score -= mc.player.getEyePos().squaredDistanceTo(crystalCenter(base)) * 0.015D;
        return score;
    }

    private boolean friendSafe(BlockPos base, boolean obsidianExists) {
        double range = TARGET_RANGE + 8.0D;
        for (PlayerEntity p : mc.world.getEntitiesByClass(PlayerEntity.class,
                mc.player.getBoundingBox().expand(range), Entity::isAlive)) {
            if (p == mc.player || !Wyvern.getInstance().getFriendManager().isFriend(p.getName().getString())) continue;
            if (explosionDamage(base, p, obsidianExists) > MAX_FRIEND_DAMAGE) return false;
        }
        return true;
    }

    @FastNative
    private double combinedHealth(LivingEntity e) {
        return e.getHealth() + e.getAbsorptionAmount();
    }

    @FastNative
    private Vec3d crystalCenter(BlockPos base) {
        return new Vec3d(base.getX() + 0.5D, base.getY() + 1.0D, base.getZ() + 0.5D);
    }

    @FastNative
    private boolean canHoldCrystal(BlockPos pos) {
        if (!mc.world.isAir(pos)) return false;

        Box box = new Box(
                pos.getX(), pos.getY(), pos.getZ(),
                pos.getX() + 1.0D, pos.getY() + 2.0D, pos.getZ() + 1.0D
        );
        return mc.world.getOtherEntities(null, box).isEmpty();
    }

    @FastNative
    private boolean canPlaceObsidianAt(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        if (!isReplaceable(state)) return false;
        return mc.world.canPlace(Blocks.OBSIDIAN.getDefaultState(), pos, ShapeContext.absent());
    }

    @FastNative
    private boolean isReplaceable(BlockState state) {
        return state.isAir() || state.isReplaceable();
    }

    @FastNative
    private boolean isSolid(BlockPos p) {
        BlockState s = mc.world.getBlockState(p);
        return !s.isAir() && !s.isReplaceable() && !s.getCollisionShape(mc.world, p).isEmpty();
    }

    @FastNative
    private boolean isUnbreakable(BlockPos p) {
        return mc.world.getBlockState(p).getHardness(mc.world, p) < 0.0F;
    }

    @FastNative
    private Direction findObsidianSide(BlockPos base) {
        Direction[] order = {Direction.DOWN, Direction.NORTH, Direction.SOUTH,
                Direction.EAST, Direction.WEST, Direction.UP};
        Direction fallback = null;
        for (Direction d : order) {
            if (!isSolid(base.offset(d))) continue;
            if (canReachFace(base.offset(d), d.getOpposite())) {
                return d;
            }
            if (fallback == null) fallback = d;
        }
        return fallback;
    }

    @FastNative
    private Direction bestMineFace(BlockPos base) {
        Vec3d eye = mc.player.getEyePos();
        Direction best = Direction.UP;
        double bestDist = Double.MAX_VALUE;
        for (Direction d : Direction.values()) {
            BlockState ns = mc.world.getBlockState(base.offset(d));
            if (!ns.isAir() && !ns.isReplaceable()) continue;
            Vec3d fp = facePoint(base, d);
            if (!canReachFace(base, d)) continue;
            double dist = eye.squaredDistanceTo(fp);
            if (dist < bestDist) {
                bestDist = dist;
                best = d;
            }
        }
        if (bestDist < Double.MAX_VALUE) return best;

        bestDist = Double.MAX_VALUE;
        for (Direction d : Direction.values()) {
            BlockState ns = mc.world.getBlockState(base.offset(d));
            if (!ns.isAir() && !ns.isReplaceable()) continue;
            double dist = eye.squaredDistanceTo(facePoint(base, d));
            if (dist < bestDist) {
                bestDist = dist;
                best = d;
            }
        }
        return best;
    }

    @FastNative
    private Vec3d facePoint(BlockPos pos, Direction face) {
        return new Vec3d(
                pos.getX() + 0.5D + face.getOffsetX() * 0.5D,
                pos.getY() + 0.5D + face.getOffsetY() * 0.5D,
                pos.getZ() + 0.5D + face.getOffsetZ() * 0.5D
        );
    }

    @FastNative
    private Vec3d topFacePoint(BlockPos pos) {
        Vec3d eye = mc.player.getEyePos();
        double x = MathHelper.clamp(eye.x, pos.getX() + 0.05D, pos.getX() + 0.95D);
        double z = MathHelper.clamp(eye.z, pos.getZ() + 0.05D, pos.getZ() + 0.95D);
        return new Vec3d(x, pos.getY() + 1.0D, z);
    }

    @FastNative
    private BlockHitResult crystalHitResult(BlockPos base) {
        Direction[] order = {Direction.UP, Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST, Direction.DOWN};
        Vec3d eye = mc.player.getEyePos();
        BlockHitResult best = null;
        double bestDistance = Double.MAX_VALUE;

        for (Direction face : order) {
            Vec3d hit = face == Direction.UP ? topFacePoint(base) : facePoint(base, face);
            if (eye.distanceTo(hit) > PLACE_RANGE + 0.35D) continue;
            if (!canReachFace(base, face)) continue;

            double distance = eye.squaredDistanceTo(hit);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = new BlockHitResult(hit, face, base, false);
            }
        }

        if (best != null) {
            return best;
        }

        Vec3d fallback = topFacePoint(base);
        return eye.distanceTo(fallback) <= PLACE_RANGE + 0.35D
                ? new BlockHitResult(fallback, Direction.UP, base, false)
                : null;
    }

    @FastNative
    private boolean canReachFace(BlockPos pos, Direction face) {
        Vec3d from = mc.player.getEyePos();
        Vec3d to = facePoint(pos, face);
        HitResult hit = mc.world.raycast(new RaycastContext(from, to,
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
        if (hit.getType() == HitResult.Type.MISS) {
            return true;
        }
        if (hit instanceof BlockHitResult blockHit) {
            return blockHit.getBlockPos().equals(pos) && blockHit.getSide() == face;
        }
        return false;
    }

    @FastNative
    private boolean isCorner(BlockPos base, BlockPos feet) {
        return base.getY() == feet.getY() - 1
                && Math.abs(base.getX() - feet.getX()) == 1
                && Math.abs(base.getZ() - feet.getZ()) == 1;
    }

    @FastNative
    private boolean isWithinRange(Vec3d point, double range) {
        return mc.player.getEyePos().distanceTo(point) <= range;
    }

    @FastNative
    @SuppressWarnings("unused")
    private boolean canSee(Vec3d to) {
        Vec3d from = mc.player.getEyePos();
        HitResult hit = mc.world.raycast(new RaycastContext(from, to,
                RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, mc.player));
        return hit.getType() == HitResult.Type.MISS || hit.getPos().squaredDistanceTo(to) < 1.0E-4D;
    }

    @FastNative
    private int findItemSlot(Item item) {
        for (int i = 0; i < 36; i++) {
            if (mc.player.getInventory().getStack(i).isOf(item)) return i;
        }
        return -1;
    }

    @FastNative
    private boolean hasCrystal() {
        return mc.player.getOffHandStack().isOf(Items.END_CRYSTAL)
                || findItemSlot(Items.END_CRYSTAL) != -1;
    }

    @FastNative
    private int findBestToolSlot(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        int bestSlot = -1;
        float bestSpeed = MIN_DIG_SPEED;
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.isEmpty()) continue;
            float speed = stack.getMiningSpeedMultiplier(state);
            if (speed > bestSpeed) {
                bestSpeed = speed;
                bestSlot = i;
            }
        }
        return bestSlot;
    }

    @FastNative
    private Hand selectCrystal() {
        if (mc.player.getOffHandStack().isOf(Items.END_CRYSTAL)) {
            return Hand.OFF_HAND;
        }
        int slot = findItemSlot(Items.END_CRYSTAL);
        if (slot == -1) return null;
        selectSlot(slot);
        return mc.player.getMainHandStack().isOf(Items.END_CRYSTAL) ? Hand.MAIN_HAND : null;
    }

    private void selectSlot(int slot) {
        if (slot < 0 || slot > 35) return;
        if (slot > 8 && mc.player.currentScreenHandler.syncId != 0) return;

        int current = mc.player.getInventory().selectedSlot;
        if (previousSlot == -1 || current != lastSetSlot) {
            previousSlot = current;
        }

        if (slot <= 8) {
            if (current != slot) {
                restoreSwappedIfNeeded();
                mc.player.getInventory().selectedSlot = slot;
                syncSlot();
            }
            lastSetSlot = slot;
            return;
        }

        int hotbar = (previousSlot >= 0 && previousSlot <= 8)
                ? previousSlot : current;

        if (swappedInvSlot == slot && swappedHotbarSlot == hotbar) {
            if (mc.player.getInventory().selectedSlot != hotbar) {
                mc.player.getInventory().selectedSlot = hotbar;
                syncSlot();
            }
            return;
        }

        restoreSwappedIfNeeded();

        int fSlot = slot, fHotbar = hotbar;
        PlayerInventoryUtil.swapWithBypassGrim(() ->
                mc.interactionManager.clickSlot(0, fSlot, fHotbar, SlotActionType.SWAP, mc.player));

        if (mc.player.getInventory().selectedSlot != hotbar) {
            mc.player.getInventory().selectedSlot = hotbar;
            syncSlot();
        }
        swappedInvSlot = slot;
        swappedHotbarSlot = hotbar;
        lastSetSlot = hotbar;
    }

    @FastNative
    private void restoreSlot() {
        if (mc.player == null) {
            resetSlotState();
            return;
        }
        if (!restoreSwappedIfNeeded()) return;
        if (previousSlot >= 0 && previousSlot <= 8
                && mc.player.getInventory().selectedSlot != previousSlot) {
            mc.player.getInventory().selectedSlot = previousSlot;
            syncSlot();
        }
        resetSlotState();
    }

    private boolean restoreSwappedIfNeeded() {
        if (swappedInvSlot < 9 || swappedInvSlot > 35 || swappedHotbarSlot < 0 || swappedHotbarSlot > 8) {
            swappedInvSlot = -1;
            swappedHotbarSlot = -1;
            return true;
        }
        if (mc.interactionManager == null || mc.player.currentScreenHandler.syncId != 0) return false;

        int fSlot = swappedInvSlot, fHotbar = swappedHotbarSlot;
        PlayerInventoryUtil.swapWithBypassGrim(() ->
                mc.interactionManager.clickSlot(0, fSlot, fHotbar, SlotActionType.SWAP, mc.player));
        swappedInvSlot = -1;
        swappedHotbarSlot = -1;
        return true;
    }

    @FastNative
    private void resetSlotState() {
        previousSlot = -1;
        lastSetSlot = -1;
        swappedInvSlot = -1;
        swappedHotbarSlot = -1;
    }

    @FastNative
    private void syncSlot() {
        if (mc.player != null && mc.getNetworkHandler() != null) {
            mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(mc.player.getInventory().selectedSlot));
        }
    }

    @FastNative
    private void rotateTo(Vec3d target) {
        RotationComponent.update(Rotation.getRotations(target), 360, 360, 360, 360, 0, ROTATION_PRIORITY, false);
    }

    @FastNative
    private boolean canBreakNow() {
        return System.currentTimeMillis() - lastBreakTime >= BREAK_DELAY;
    }

    @FastNative
    private boolean canPlaceNow() {
        return System.currentTimeMillis() - lastPlaceTime >= PLACE_DELAY;
    }

    @FastNative
    private boolean recentlyPlaced(BlockPos base) {
        Long t = recentPlaces.get(base);
        return t != null && System.currentTimeMillis() - t < PLACE_MEMORY;
    }

    private void cleanupRecentPlaces() {
        long now = System.currentTimeMillis();
        recentPlaces.entrySet().removeIf(e -> now - e.getValue() > PLACE_MEMORY);
    }

    @FastNative
    private void markPlaceFailed(BlockPos base) {
        failedPlaceSpots.put(base.toImmutable(), System.currentTimeMillis());
    }

    @FastNative
    private boolean recentlyFailedPlace(BlockPos base) {
        Long failedAt = failedPlaceSpots.get(base);
        return failedAt != null && System.currentTimeMillis() - failedAt < PLACE_FAILURE_MEMORY;
    }

    private void cleanupFailedPlaceSpots() {
        long now = System.currentTimeMillis();
        failedPlaceSpots.entrySet().removeIf(e -> now - e.getValue() >= PLACE_FAILURE_MEMORY);
    }

    @FastNative
    private void markDigFailed(BlockPos base) {
        failedDigSpots.put(base.toImmutable(), System.currentTimeMillis());
    }

    @FastNative
    private boolean isDigCoolingDown(BlockPos base) {
        Long failedAt = failedDigSpots.get(base);
        return failedAt != null && System.currentTimeMillis() - failedAt < DIG_FAILURE_MEMORY;
    }

    private void cleanupFailedDigSpots() {
        long now = System.currentTimeMillis();
        failedDigSpots.entrySet().removeIf(e -> now - e.getValue() >= DIG_FAILURE_MEMORY);
    }

    @FastNative
    private void setOverlay(BlockPos base, double dmg, double self, OverlayPhase phase) {
        overlayBase = base;
        overlayCrystal = base.up();
        overlayDamage = dmg;
        overlaySelf = self;
        overlayPhase = phase;
    }

    @EventTarget
    private void onWorldRender(EventRender3D event) {
        if (nullCheck()) return;
        boolean extended = false;
        MatrixStack matrices = event.getMatrix();
        visuals.removeIf(v -> v.animation.update(0.0F) <= 0.02F);
        for (VisualBox v : visuals) {
            float a = v.animation.getValue();
            if (a <= 0.02F) continue;
            float size = MathHelper.lerp(a, 1.02F, 0.1F);
            double expand = (size - 1.0D) / 2.0D;
            drawActionBox(matrices, new Box(v.pos).expand(expand), v.kind.color, 0.76F * a);
        }

        if (!extended) return;
        if (activeTarget != null && activeTarget.isAlive()) {
            drawOutline(activeTarget.getBoundingBox().expand(0.06D), argb(150, 255, 255, 255), 1.4F);
        }

        if (overlayCrystal != null && overlayDamage > 0.05D) {
            Box box = new Box(overlayCrystal);
            Vec3d center = box.getCenter();
            Vec3d top = new Vec3d(center.x, box.maxY + 0.25D, center.z);
            int color = damageColor(overlayDamage, overlaySelf);
            Render3DUtil.drawLine(center, top, color, 1.4F, false);
            drawDamageText(matrices, top.add(0.0D, 0.06D, 0.0D), overlayDamage, overlaySelf);
        }
    }

    @FastNative
    private void drawActionBox(MatrixStack matrices, Box box, int color, float alpha) {
        Vec3d cam = mc.gameRenderer.getCamera().getPos();
        Matrix4f matrix = matrices.peek().getPositionMatrix();

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.disableDepthTest();
        RenderSystem.disableCull();
        RenderSystem.setShader(ShaderProgramKeys.POSITION_COLOR);

        BufferBuilder fill = Tessellator.getInstance().begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_COLOR);
        addShaderBox(fill, matrix, box, cam, color, color, alpha, 0.0F);
        BufferRenderer.drawWithGlobalProgram(fill.end());

        RenderSystem.enableDepthTest();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    @FastNative
    private void addShaderBox(BufferBuilder buffer, Matrix4f matrix, Box box, Vec3d cam,
                              int first, int second, float alpha, float time) {
        float x1 = (float) (box.minX - cam.x), y1 = (float) (box.minY - cam.y), z1 = (float) (box.minZ - cam.z);
        float x2 = (float) (box.maxX - cam.x), y2 = (float) (box.maxY - cam.y), z2 = (float) (box.maxZ - cam.z);

        addShaderQuad(buffer, matrix, x1, y1, z1, x2, y1, z1, x2, y1, z2, x1, y1, z2, first, second, alpha, time);
        addShaderQuad(buffer, matrix, x1, y2, z1, x1, y2, z2, x2, y2, z2, x2, y2, z1, first, second, alpha, time);
        addShaderQuad(buffer, matrix, x1, y1, z1, x1, y2, z1, x2, y2, z1, x2, y1, z1, first, second, alpha, time);
        addShaderQuad(buffer, matrix, x2, y1, z1, x2, y2, z1, x2, y2, z2, x2, y1, z2, first, second, alpha, time);
        addShaderQuad(buffer, matrix, x1, y1, z2, x2, y1, z2, x2, y2, z2, x1, y2, z2, first, second, alpha, time);
        addShaderQuad(buffer, matrix, x1, y1, z1, x1, y1, z2, x1, y2, z2, x1, y2, z1, first, second, alpha, time);
    }

    @FastNative
    private void addShaderQuad(BufferBuilder buffer, Matrix4f matrix,
       float x1, float y1, float z1, float x2, float y2, float z2,
       float x3, float y3, float z3, float x4, float y4, float z4,
       int first, int second, float alpha, float time) {
        putVertex(buffer, matrix, x1, y1, z1, shaderColorAt(x1, y1, z1, first, second, alpha, time));
        putVertex(buffer, matrix, x2, y2, z2, shaderColorAt(x2, y2, z2, first, second, alpha, time));
        putVertex(buffer, matrix, x3, y3, z3, shaderColorAt(x3, y3, z3, first, second, alpha, time));
        putVertex(buffer, matrix, x4, y4, z4, shaderColorAt(x4, y4, z4, first, second, alpha, time));
    }

    @FastNative
    private int shaderColorAt(float x, float y, float z, int first, int second, float alpha, float time) {
        float pulse = 0.5F + 0.5F * (float) Math.sin((x * 1.8F + y * 1.1F + z * 1.5F) * 0.5F + time * 2.2F);
        int mixed = mix(first, second, pulse);
        int a = MathHelper.clamp((int) (alpha * 255.0F), 0, 255);
        return (mixed & 0x00FFFFFF) | (a << 24);
    }

    @FastNative
    private void putVertex(BufferBuilder buffer, Matrix4f matrix, float x, float y, float z, int color) {
        float r = ((color >> 16) & 255) / 255.0F;
        float g = ((color >> 8) & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        float a = ((color >> 24) & 255) / 255.0F;
        buffer.vertex(matrix, x, y, z).color(r, g, b, a);
    }

    @FastNative
    private void drawOutline(Box box, int color, float width) {
        Vec3d a = new Vec3d(box.minX, box.minY, box.minZ);
        Vec3d b = new Vec3d(box.maxX, box.minY, box.minZ);
        Vec3d c = new Vec3d(box.maxX, box.minY, box.maxZ);
        Vec3d d = new Vec3d(box.minX, box.minY, box.maxZ);
        Vec3d e = new Vec3d(box.minX, box.maxY, box.minZ);
        Vec3d f = new Vec3d(box.maxX, box.maxY, box.minZ);
        Vec3d g = new Vec3d(box.maxX, box.maxY, box.maxZ);
        Vec3d h = new Vec3d(box.minX, box.maxY, box.maxZ);
        Render3DUtil.drawLine(a, b, color, width, true);
        Render3DUtil.drawLine(b, c, color, width, true);
        Render3DUtil.drawLine(c, d, color, width, true);
        Render3DUtil.drawLine(d, a, color, width, true);
        Render3DUtil.drawLine(e, f, color, width, true);
        Render3DUtil.drawLine(f, g, color, width, true);
        Render3DUtil.drawLine(g, h, color, width, true);
        Render3DUtil.drawLine(h, e, color, width, true);
        Render3DUtil.drawLine(a, e, color, width, true);
        Render3DUtil.drawLine(b, f, color, width, true);
        Render3DUtil.drawLine(c, g, color, width, true);
        Render3DUtil.drawLine(d, h, color, width, true);
    }

    @FastNative
    private void drawDamageText(MatrixStack matrices, Vec3d pos, double dmg, double self) {
        String text = String.format(Locale.ROOT, "%.1f / %.1f", dmg, self);
        Vec3d cam = mc.gameRenderer.getCamera().getPos();
        VertexConsumerProvider.Immediate immediate = mc.getBufferBuilders().getEntityVertexConsumers();

        matrices.push();
        matrices.translate(pos.x - cam.x, pos.y - cam.y, pos.z - cam.z);
        matrices.multiply(mc.getEntityRenderDispatcher().getRotation());
        matrices.scale(-0.025F, -0.025F, 0.025F);

        float x = -mc.textRenderer.getWidth(text) / 2.0F;
        mc.textRenderer.draw(text, x, 0.0F, 0xFFFFFFFF, false,
                matrices.peek().getPositionMatrix(), immediate,
                TextRenderer.TextLayerType.SEE_THROUGH, 0, 0x00F000F0);
        immediate.draw();
        matrices.pop();
    }

    @FastNative
    private int damageColor(double dmg, double self) {
        double hp = Math.max(1.0D, combinedHealth(activeTarget != null ? activeTarget : mc.player));
        double ratio = MathHelper.clamp(dmg / hp, 0.0D, 1.0D);
        int red = (int) MathHelper.lerp(ratio, 70.0D, 255.0D);
        int green = (int) MathHelper.lerp(ratio, 220.0D, 60.0D);
        int blue = (int) MathHelper.lerp(ratio, 80.0D, 40.0D);
        return argb(255, red, green, blue);
    }

    @FastNative
    private static int argb(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    @FastNative
    private static int mix(int a, int b, float t) {
        t = MathHelper.clamp(t, 0.0F, 1.0F);
        int ar = (a >> 16) & 0xFF, ag = (a >> 8) & 0xFF, ab = a & 0xFF;
        int br = (b >> 16) & 0xFF, bg = (b >> 8) & 0xFF, bb = b & 0xFF;
        int r = (int) (ar + (br - ar) * t);
        int g = (int) (ag + (bg - ag) * t);
        int bl = (int) (ab + (bb - ab) * t);
        return (0xFF << 24) | (r << 16) | (g << 8) | bl;
    }
}
