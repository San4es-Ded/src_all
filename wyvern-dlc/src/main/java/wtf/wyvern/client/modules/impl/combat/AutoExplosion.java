package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.block.Blocks;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.decoration.EndCrystalEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import wtf.wyvern.Wyvern;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.SliderSetting;
import wtf.wyvern.utility.component.RotationComponent;
import wtf.wyvern.utility.game.player.PlayerInventoryUtil;
import wtf.wyvern.utility.game.player.rotation.Rotation;
import wtf.wyvern.utility.game.player.rotation.RotationUtil;
import wtf.wyvern.utility.interfaces.IMinecraft;
import wtf.wyvern.utility.math.Timer;

import java.util.*;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "AutoExplosion",
        category = Category.COMBAT,
        description = "Ставит и взрывает кристаллы на обсидиане"
)
public class AutoExplosion extends Module implements IMinecraft {
    public static final AutoExplosion INSTANCE = new AutoExplosion();

    private final SliderSetting range = new SliderSetting("Радиус", 4.5f, 1.0f, 6.0f, 0.1f);
    private static final long ACTION_DELAY_MS = 45L;
    private final BooleanSetting antiSelf = new BooleanSetting("Не бахать Себя", true);
    private final BooleanSetting antiFriend = new BooleanSetting("Не бахать Друзей", true);
    private final BooleanSetting antiItems = new BooleanSetting("Не бахать Ресурсы", true);

    private final Timer breakTimer = new Timer();
    private final Timer placeTimer = new Timer();
    private final Map<BlockPos, Long> activeObsidians = new HashMap<>();
    private final Set<BlockPos> placedOn = new HashSet<>();

    private AutoExplosion() {}

    @FastNative
    @Override
    public void onDisable() {
        activeObsidians.clear();
        placedOn.clear();
        super.onDisable();
    }

    @FastNative
    @EventTarget
    private void onPacket(EventPacket event) {
        if (mc.player == null) return;

        if (event.isSent() && event.getPacket() instanceof PlayerInteractBlockC2SPacket interactPacket) {
            if (mc.player.getStackInHand(interactPacket.getHand()).getItem() == Items.OBSIDIAN) {
                BlockPos pos = interactPacket.getBlockHitResult().getBlockPos();
                Direction side = interactPacket.getBlockHitResult().getSide();
                BlockPos placedPos = pos.offset(side);
                activeObsidians.put(placedPos, System.currentTimeMillis());
            }
        }
    }

    @EventTarget
    private void onUpdate(EventUpdate event) {
        if (mc.player == null || mc.world == null) return;

        long now = System.currentTimeMillis();
        activeObsidians.entrySet().removeIf(entry -> now - entry.getValue() > 5000);

        EndCrystalEntity targetCrystal = findTargetCrystal();
        if (targetCrystal != null && breakTimer.finished(ACTION_DELAY_MS)) {
            handleCrystalExplosion(targetCrystal);
            breakTimer.reset();
        }

        for (BlockPos pos : new ArrayList<>(activeObsidians.keySet())) {
            if (placedOn.contains(pos)) continue;
            if (mc.world.getBlockState(pos).isOf(Blocks.OBSIDIAN)) {
                if (canPlaceCrystal(pos) && placeTimer.finished(ACTION_DELAY_MS)) {
                    int crystalSlot = PlayerInventoryUtil.find(Items.END_CRYSTAL, 0, 8);
                    if (crystalSlot != -1) {
                        handleCrystalPlacement(pos, crystalSlot);
                        placedOn.add(pos);
                        break;
                    }
                }
            }
        }
    }

    private boolean canPlaceCrystal(BlockPos pos) {
        BlockPos up = pos.up();
        if (!mc.world.isAir(up)) return false;

        return mc.world.getOtherEntities(null, new Box(up)).stream()
                .noneMatch(e -> !(e instanceof EndCrystalEntity));
    }

    @FastNative
    private void handleCrystalExplosion(EndCrystalEntity crystal) {
        Vec3d targetPos = crystal.getBoundingBox().getCenter();
        Rotation rot = RotationUtil.calculateAngle(targetPos);

        RotationComponent.update(rot, 360F, 360F, 360F, 360F, 0, 100, false);

        mc.getNetworkHandler().sendPacket(net.minecraft.network.packet.c2s.play.PlayerInteractEntityC2SPacket.attack(crystal, mc.player.isSneaking()));
        mc.player.swingHand(Hand.MAIN_HAND);

        BlockPos obsPos = crystal.getBlockPos().down();
        activeObsidians.remove(obsPos);
        placedOn.remove(obsPos);
    }

    @FastNative
    private void handleCrystalPlacement(BlockPos pos, int crystalSlot) {
        Vec3d center = pos.toCenterPos().add(0, 0.5, 0);
        Rotation rot = RotationUtil.calculateAngle(center);

        RotationComponent.update(rot, 360F, 360F, 360F, 360F, 0, 100, false);

        int prevSlot = mc.player.getInventory().selectedSlot;
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(crystalSlot));
        BlockHitResult hit = new BlockHitResult(center, Direction.UP, pos, false);
        mc.getNetworkHandler().sendPacket(new PlayerInteractBlockC2SPacket(Hand.MAIN_HAND, hit, 0));
        mc.player.swingHand(Hand.MAIN_HAND);
        mc.getNetworkHandler().sendPacket(new UpdateSelectedSlotC2SPacket(prevSlot));
        placeTimer.reset();
    }

    private EndCrystalEntity findTargetCrystal() {
        EndCrystalEntity closest = null;
        double closestDistance = Double.MAX_VALUE;
        Box searchBox = mc.player.getBoundingBox().expand(range.getCurrent());
        for (EndCrystalEntity crystal : mc.world.getEntitiesByClass(EndCrystalEntity.class, searchBox,
                candidate -> activeObsidians.containsKey(candidate.getBlockPos().down()))) {
            if (antiSelf.isEnabled() && mc.player.distanceTo(crystal) < 2.5F) {
                continue;
            }

            boolean blocked = false;
            if (antiFriend.isEnabled() || antiItems.isEnabled()) {
                // The checks only care about entities within 3.5 blocks of the crystal,
                // so query a bounded box instead of iterating the whole world per crystal.
                // Explicit always-true predicate: the two-arg overload silently filters
                // out spectators, which would change the antiFriend result.
                for (net.minecraft.entity.Entity entity : mc.world.getOtherEntities(crystal, crystal.getBoundingBox().expand(3.5D), e -> true)) {
                    if (antiFriend.isEnabled() && entity instanceof PlayerEntity player && player != mc.player
                            && Wyvern.getInstance().getFriendManager().isFriend(player.getName().getString())
                            && player.distanceTo(crystal) < 3.5F) {
                        blocked = true;
                        break;
                    }
                    if (antiItems.isEnabled() && entity instanceof ItemEntity item
                            && item.distanceTo(crystal) < 2.5F) {
                        blocked = true;
                        break;
                    }
                }
            }
            if (blocked) {
                continue;
            }

            double distance = mc.player.getEyePos().squaredDistanceTo(crystal.getBoundingBox().getCenter());
            if (distance < closestDistance) {
                closest = crystal;
                closestDistance = distance;
            }
        }
        return closest;
    }
}
