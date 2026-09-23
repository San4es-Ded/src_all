package wtf.wyvern.client.modules.impl.combat;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.screen.slot.SlotActionType;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import wtf.astroguard.J2C.FastNative;
import wtf.wyvern.Wyvern;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.player.rotation.Rotation;

import java.util.ArrayList;
import java.util.List;

/**
 * WebTrap — застраивает цель паутиной. Рейдж (постоянный): берёт ближайшего
 * игрока в радиусе и вкруговую ставит паутину вокруг его ног и над головой,
 * с поворотом камеры на точку постановки.
 */
@ModuleAnnotation(
        name = "WebTrap",
        category = Category.COMBAT,
        description = "Застраивает цель паутиной"
)
public final class WebTrap extends Module {
    public static final WebTrap INSTANCE = new WebTrap();

    private static final double PLACE_RANGE = 4.9D;

    private final ModeSetting mode = new ModeSetting("Режим", "Рейдж", "Легит");
    private final SliderSetting range = new SliderSetting("Дальность", 6.0F, 1.0F, 12.0F, 0.5F);
    private final SliderSetting placeDelay = new SliderSetting("Задержка постановки", 0.0F, 0.0F, 500.0F, 10.0F);
    private final BooleanSetting rotate = new BooleanSetting("Ротация", true);
    private final BooleanSetting sides = new BooleanSetting("Стороны", true);
    private final BooleanSetting above = new BooleanSetting("Сверху", true);

    private PlayerEntity target;
    private long lastPlaceTime;
    private int spotIndex;

    // Слоты (паттерн CrystalAura)
    private int previousSlot = -1;
    private int lastSetSlot = -1;
    private int swappedInvSlot = -1;
    private int swappedHotbarSlot = -1;

    @FastNative
    @EventTarget
    private void onTick(EventTick event) {
        if (mc.player == null || mc.world == null || mc.player.isGliding()) {
            return;
        }
        updateTarget();
        if (target == null) {
            return;
        }
        if (mode.is("Легит") && !isTargetInFov()) {
            return;
        }
        if (System.currentTimeMillis() - lastPlaceTime < placeDelay.getCurrent()) {
            return;
        }

        List<BlockPos> spots = getWebSpots();
        if (spots.isEmpty()) {
            return;
        }
        BlockPos pos = spots.get(spotIndex % spots.size());
        spotIndex++;
        if (placeWeb(pos)) {
            lastPlaceTime = System.currentTimeMillis();
        }
    }

    @FastNative
    private List<BlockPos> getWebSpots() {
        List<BlockPos> spots = new ArrayList<>();
        if (target == null) {
            return spots;
        }
        BlockPos feet = target.getBlockPos();
        if (sides.isEnabled()) {
            spots.add(feet.north());
            spots.add(feet.south());
            spots.add(feet.east());
            spots.add(feet.west());
        }
        if (above.isEnabled()) {
            spots.add(feet.up());
        }
        return spots;
    }

    @FastNative
    private boolean placeWeb(BlockPos pos) {
        BlockState state = mc.world.getBlockState(pos);
        if (state.isOf(Blocks.COBWEB)) {
            return false;
        }
        if (!isReplaceable(state)) {
            return false;
        }
        BlockHitResult hitResult = webHitResult(pos);
        if (hitResult == null) {
            restoreSlot();
            return false;
        }
        Hand hand = selectWeb();
        if (hand == null) {
            restoreSlot();
            return false;
        }
        if (rotate.isEnabled()) {
            rotateTo(hitResult.getPos());
        }
        ActionResult result = mc.interactionManager.interactBlock(mc.player, hand, hitResult);
        mc.player.swingHand(hand);
        restoreSlot();
        return result.isAccepted();
    }

    @FastNative
    private BlockHitResult webHitResult(BlockPos pos) {
        Direction[] order = {Direction.DOWN, Direction.NORTH, Direction.SOUTH,
                Direction.EAST, Direction.WEST, Direction.UP};
        Vec3d eye = mc.player.getEyePos();
        BlockHitResult best = null;
        double bestDistance = Double.MAX_VALUE;

        // Кликаем по грани твёрдого соседа, смотрящей на pos — паутина ложится ровно в pos.
        for (Direction side : order) {
            BlockPos neighbor = pos.offset(side);
            if (!isSolid(neighbor)) continue;
            Direction clickFace = side.getOpposite();
            Vec3d hit = facePoint(neighbor, clickFace);
            if (eye.distanceTo(hit) > PLACE_RANGE + 0.35D) continue;
            if (!canReachFace(neighbor, clickFace)) continue;

            double distance = eye.squaredDistanceTo(hit);
            if (distance < bestDistance) {
                bestDistance = distance;
                best = new BlockHitResult(hit, clickFace, neighbor, false);
            }
        }
        return best;
    }

    @FastNative
    private boolean isSolid(BlockPos p) {
        BlockState s = mc.world.getBlockState(p);
        return !s.isAir() && !s.isReplaceable() && !s.getCollisionShape(mc.world, p).isEmpty();
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
    private Vec3d facePoint(BlockPos pos, Direction face) {
        return new Vec3d(
                pos.getX() + 0.5D + face.getOffsetX() * 0.5D,
                pos.getY() + 0.5D + face.getOffsetY() * 0.5D,
                pos.getZ() + 0.5D + face.getOffsetZ() * 0.5D
        );
    }

    @FastNative
    private boolean isReplaceable(BlockState state) {
        return state.isAir() || state.isReplaceable();
    }

    @FastNative
    private void updateTarget() {
        PlayerEntity best = null;
        double bestSq = Double.MAX_VALUE;
        double rangeSq = range.getCurrent() * range.getCurrent();
        for (Entity entity : mc.world.getEntities()) {
            if (!(entity instanceof PlayerEntity player)) continue;
            if (player == mc.player || !player.isAlive() || player.getHealth() <= 0.0F) continue;
            if (player.isInvisible()) continue;
            if (Wyvern.INSTANCE.getFriendManager().isFriend(player.getName().getString())) continue;
            if (AntiBot.INSTANCE.isBot(player)) continue;
            double dSq = mc.player.squaredDistanceTo(player);
            if (dSq <= rangeSq && dSq < bestSq) {
                bestSq = dSq;
                best = player;
            }
        }
        target = best;
    }

    @FastNative
    private boolean isTargetInFov() {
        Rotation rotation = Rotation.getRotations(target.getBoundingBox().getCenter());
        float dYaw = Math.abs(MathHelper.wrapDegrees(rotation.getYaw() - mc.player.getYaw()));
        float dPitch = Math.abs(rotation.getPitch() - mc.player.getPitch());
        return dYaw <= 70.0F && dPitch <= 70.0F;
    }

    // ===================== ПАУТИНА / СЛОТЫ =====================
    @FastNative
    private int findWebSlot() {
        for (int i = 0; i < 36; i++) {
            ItemStack stack = mc.player.getInventory().getStack(i);
            if (stack.getItem() instanceof BlockItem blockItem
                    && blockItem.getBlock() == Blocks.COBWEB) {
                return i;
            }
        }
        return -1;
    }

    @FastNative
    private boolean hasWeb() {
        return findWebSlot() != -1;
    }

    /** Паутина в offhand — используем её; иначе свапаем в правую руку. */
    @FastNative
    private Hand selectWeb() {
        ItemStack offHand = mc.player.getOffHandStack();
        if (offHand.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() == Blocks.COBWEB) {
            return Hand.OFF_HAND;
        }
        int slot = findWebSlot();
        if (slot == -1) {
            return null;
        }
        selectSlot(slot);
        ItemStack main = mc.player.getMainHandStack();
        return main.getItem() instanceof BlockItem blockItem
                && blockItem.getBlock() == Blocks.COBWEB ? Hand.MAIN_HAND : null;
    }

    /** Делает нужный слот активным в правой руке. Слоты 9-35 тихо свапаются в хотбар. */
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

    // ===================== ВРАЩЕНИЕ =====================
    /**
     * Тихая ротация: серверу уходит пакет LookAndOnGround с доворотом на
     * точку постановки, локальная камера игрока не двигается (без client look).
     */
    @FastNative
    private void rotateTo(Vec3d target) {
        if (mc.player == null || mc.getNetworkHandler() == null) return;
        Rotation rotation = Rotation.getRotations(target);
        mc.getNetworkHandler().sendPacket(new PlayerMoveC2SPacket.LookAndOnGround(
                rotation.getYaw(), rotation.getPitch(),
                mc.player.isOnGround(), mc.player.horizontalCollision));
    }

    @FastNative
    @Override
    public void onEnable() {
        this.target = null;
        this.spotIndex = 0;
        super.onEnable();
    }

    @FastNative
    @Override
    public void onDisable() {
        restoreSlot();
        this.target = null;
        super.onDisable();
    }
}