package rockstar.modules.player;



import rockstar.client.module.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;

import java.util.HashSet;
import java.util.Set;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.ModuleInfo;
import rockstar.client.internal.game.BlockEntityCache;
import rockstar.client.module.Module;

@ModuleInfo(name="Click Through", category=ModuleCategory.PLAYER)
public class ClickThroughModule
extends Module {
    private final Set<BlockPos> internalField0546 = new HashSet<BlockPos>();

    @Override
    public void internalMethod08229() {
        if (ClickThroughModule.internalField0149.player == null || ClickThroughModule.internalField0149.world == null || ClickThroughModule.internalField0149.interactionManager == null) {
            return;
        }
        if (!ClickThroughModule.internalField0149.options.useKey.isPressed() || ClickThroughModule.internalField0149.options.sneakKey.isPressed()) {
            return;
        }
        this.internalField0546.clear();
        for (int i = 1; i < 16; ++i) {
            Vec3d vec3d = ClickThroughModule.internalField0149.player.getRotationVec(1.0f);
            Vec3d vec3d2 = this.internalMethod04525(internalField0149.getRenderTickCounter().getTickProgress(true)).add(vec3d.multiply((double)i * 0.25));
            BlockPos blockPos = BlockPos.ofFloored((Position)vec3d2);
            if (this.internalField0546.contains(blockPos)) continue;
            this.internalField0546.add(blockPos);
            if (ClickThroughModule.internalField0149.player.getEntityPos().distanceTo(Vec3d.ofCenter((Vec3i)blockPos)) > 4.25 || BlockEntityCache.internalMethod01024(blockPos) == null) continue;
            Vec3d vec3d3 = this.internalMethod04525(internalField0149.getRenderTickCounter().getTickProgress(true));
            Direction direction = Direction.getFacing((double)(vec3d3.x - (double)blockPos.getX()), (double)(vec3d3.y - (double)blockPos.getY()), (double)(vec3d3.z - (double)blockPos.getZ()));
            if (direction == Direction.UP || direction == Direction.DOWN) {
                direction = Direction.NORTH;
            }
            BlockHitResult blockHitResult = new BlockHitResult(Vec3d.ofCenter((Vec3i)blockPos), direction, blockPos, true);
            ClickThroughModule.internalField0149.interactionManager.interactBlock(ClickThroughModule.internalField0149.player, Hand.MAIN_HAND, blockHitResult);
            ClickThroughModule.internalField0149.player.swingHand(Hand.MAIN_HAND);
            return;
        }
    }

    public Vec3d internalMethod04525(float f) {
        if (ClickThroughModule.internalField0149.player == null) {
            return Vec3d.ZERO;
        }
        double d = MathHelper.lerp((double)f, (double)ClickThroughModule.internalField0149.player.lastX, (double)ClickThroughModule.internalField0149.player.getX());
        double d2 = MathHelper.lerp((double)f, (double)ClickThroughModule.internalField0149.player.lastY, (double)ClickThroughModule.internalField0149.player.getY()) + (double)ClickThroughModule.internalField0149.player.getEyeHeight(ClickThroughModule.internalField0149.player.getPose());
        double d3 = MathHelper.lerp((double)f, (double)ClickThroughModule.internalField0149.player.lastZ, (double)ClickThroughModule.internalField0149.player.getZ());
        return new Vec3d(d, d2, d3);
    }
}
