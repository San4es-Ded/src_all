package rockstar.client.internal.game;







import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.FluidBlock;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import net.minecraft.world.RaycastContext;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.rotation.SilentRotationHelper;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.script.MovementInputController;
import rockstar.client.internal.inventory.BestToolSelector;
import rockstar.client.internal.rotation.BlockFaceRotationHelper;
import rockstar.client.internal.core.ChatMessageHelper;
import rockstar.client.internal.game.RadiusGoal;
import rockstar.client.internal.rotation.GotoPathTask;
import rockstar.client.internal.core.NewtonTask;

public final class ExcavationTask
implements NewtonTask {
    private final BlockPos internalField0352;
    private final BlockPos internalField0351;
    @Nullable
    private final Block internalField0792;
    private final int internalField0227;
    private int internalField0228;
    @Nullable
    private BlockPos internalField1131;
    @Nullable
    private GotoPathTask internalField0494;
    private boolean internalField0277;
    private final BlockFaceRotationHelper internalField0904 = new BlockFaceRotationHelper();
    private boolean internalField0276;
    private boolean internalField1099;
    private int internalField1053;
    private final Set<BlockPos> internalField0546 = new HashSet<BlockPos>();

    public ExcavationTask(BlockPos blockPos, BlockPos blockPos2, @Nullable Block block) {
        this.internalField0352 = blockPos;
        this.internalField0351 = blockPos2;
        this.internalField0792 = block;
        this.internalField0227 = this.internalMethod01927();
    }

    private int internalMethod01927() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.world == null) {
            return 0;
        }
        int n = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = this.internalField0352.getY(); i <= this.internalField0351.getY(); ++i) {
            for (int j = this.internalField0352.getX(); j <= this.internalField0351.getX(); ++j) {
                for (int k = this.internalField0352.getZ(); k <= this.internalField0351.getZ(); ++k) {
                    mutable.set(j, i, k);
                    if (!this.internalMethod04312(minecraftClient, (BlockPos)mutable)) continue;
                    ++n;
                }
            }
        }
        return n;
    }

    private boolean internalMethod04312(MinecraftClient minecraftClient, BlockPos blockPos) {
        if (minecraftClient.world == null) {
            return false;
        }
        BlockState blockState = minecraftClient.world.getBlockState(blockPos);
        if (blockState.isAir()) {
            return false;
        }
        if (blockState.getBlock() instanceof FluidBlock) {
            return false;
        }
        if (blockState.getHardness((BlockView)minecraftClient.world, blockPos) < 0.0f) {
            return false;
        }
        return this.internalField0792 == null || blockState.getBlock() == this.internalField0792;
    }

    @Override
    public String internalMethod01129() {
        return "excavate";
    }

    @Override
    public String internalMethod05788() {
        if (this.internalField0276) {
            return "\u043f\u0430\u0443\u0437\u0430";
        }
        if (this.internalField1099) {
            return "\u0433\u043e\u0442\u043e\u0432\u043e";
        }
        String string = this.internalField0228 + "/" + this.internalField0227;
        if (this.internalField0277) {
            return "\u043a\u043e\u043f\u0430\u0435\u043c " + String.valueOf(this.internalField1131) + " (" + string + ")";
        }
        if (this.internalField0494 != null) {
            return "\u0438\u0434\u0451\u043c \u043a " + String.valueOf(this.internalField1131) + " (" + string + ")";
        }
        return "\u0440\u0430\u0441\u043a\u043e\u043f " + string;
    }

    @Override
    public boolean internalMethod04087() {
        boolean bl;
        if (this.internalField0276) {
            return false;
        }
        if (this.internalField1099) {
            return true;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.player == null || minecraftClient.world == null || minecraftClient.interactionManager == null) {
            return false;
        }
        SilentRotationHelper.internalMethod06440();
        if (this.internalField0277 && this.internalField1131 != null) {
            if (!this.internalMethod04312(minecraftClient, this.internalField1131)) {
                if (minecraftClient.world.getBlockState(this.internalField1131).isAir()) {
                    ++this.internalField0228;
                }
                this.internalField0277 = false;
                this.internalField1131 = null;
                this.internalMethod08315();
                return false;
            }
            Vec3d vec3d = Vec3d.ofCenter((Vec3i)this.internalField1131);
            double d = minecraftClient.player.getEyePos().distanceTo(vec3d);
            if (d > 5.0) {
                this.internalField0277 = false;
                this.internalMethod08315();
                this.internalField0494 = new GotoPathTask(new RadiusGoal(this.internalField1131, 2));
                return false;
            }
            if (!ExcavationTask.internalMethod06410(minecraftClient, this.internalField1131)) {
                this.internalField0277 = false;
                this.internalMethod08315();
                ++this.internalField1053;
                if (this.internalField1053 >= 3) {
                    this.internalField0546.add(this.internalField1131);
                    this.internalField1131 = null;
                    this.internalField1053 = 0;
                    return false;
                }
                this.internalField0494 = new GotoPathTask(new RadiusGoal(this.internalField1131, 1));
                return false;
            }
            this.internalField1053 = 0;
            BestToolSelector.internalMethod04395(minecraftClient.world.getBlockState(this.internalField1131));
            Direction direction = this.internalMethod00328(minecraftClient, this.internalField1131);
            if (!this.internalField0904.internalMethod05481(this.internalField1131, direction)) {
                return false;
            }
            minecraftClient.interactionManager.updateBlockBreakingProgress(this.internalField1131, direction);
            minecraftClient.player.swingHand(minecraftClient.player.getActiveHand());
            return false;
        }
        if (this.internalField0494 != null) {
            boolean bl2 = this.internalField0494.internalMethod04087();
            if (bl2) {
                this.internalField0494 = null;
                if (this.internalField1131 != null) {
                    double d = minecraftClient.player.getEyePos().distanceTo(Vec3d.ofCenter((Vec3i)this.internalField1131));
                    if (d > 5.0) {
                        this.internalField0546.add(this.internalField1131);
                        this.internalField1131 = null;
                    } else {
                        this.internalField0277 = true;
                    }
                }
            }
            return false;
        }
        BlockPos blockPos = this.internalMethod03736(minecraftClient);
        if (blockPos == null) {
            ChatMessageHelper.internalMethod00196("\u0420\u0430\u0441\u043a\u043e\u043f \u0437\u0430\u0432\u0435\u0440\u0448\u0451\u043d (" + this.internalField0228 + " \u0431\u043b\u043e\u043a\u043e\u0432)");
            this.internalMethod04086();
            this.internalField1099 = true;
            return true;
        }
        this.internalField1131 = blockPos;
        Vec3d vec3d = Vec3d.ofCenter((Vec3i)blockPos);
        boolean bl3 = bl = minecraftClient.player.getEyePos().distanceTo(vec3d) <= 4.5;
        if (bl && ExcavationTask.internalMethod06410(minecraftClient, blockPos)) {
            this.internalField0277 = true;
        } else {
            this.internalField0494 = new GotoPathTask(new RadiusGoal(blockPos, 2));
        }
        return false;
    }

    @Nullable
    private BlockPos internalMethod03736(MinecraftClient minecraftClient) {
        if (minecraftClient.player == null) {
            return null;
        }
        double d = minecraftClient.player.getX();
        double d2 = minecraftClient.player.getZ();
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int i = this.internalField0351.getY(); i >= this.internalField0352.getY(); --i) {
            BlockPos blockPos = null;
            double d3 = Double.MAX_VALUE;
            for (int j = this.internalField0352.getX(); j <= this.internalField0351.getX(); ++j) {
                for (int k = this.internalField0352.getZ(); k <= this.internalField0351.getZ(); ++k) {
                    double d4;
                    double d5;
                    double d6;
                    mutable.set(j, i, k);
                    if (this.internalField0546.contains(mutable) || !this.internalMethod04312(minecraftClient, (BlockPos)mutable) || !((d6 = (d5 = (double)j + 0.5 - d) * d5 + (d4 = (double)k + 0.5 - d2) * d4) < d3)) continue;
                    d3 = d6;
                    blockPos = mutable.toImmutable();
                }
            }
            if (blockPos == null) continue;
            return blockPos;
        }
        return null;
    }

    private static boolean internalMethod06410(MinecraftClient minecraftClient, BlockPos blockPos) {
        Vec3d[] vec3dArray;
        if (minecraftClient.world == null || minecraftClient.player == null) {
            return false;
        }
        Vec3d vec3d = minecraftClient.player.getEyePos();
        Vec3d vec3d2 = Vec3d.ofCenter((Vec3i)blockPos);
        for (Vec3d vec3d3 : vec3dArray = new Vec3d[]{vec3d2, vec3d2.add(0.49, 0.0, 0.0), vec3d2.add(-0.49, 0.0, 0.0), vec3d2.add(0.0, 0.49, 0.0), vec3d2.add(0.0, -0.49, 0.0), vec3d2.add(0.0, 0.0, 0.49), vec3d2.add(0.0, 0.0, -0.49)}) {
            RaycastContext raycastContext = new RaycastContext(vec3d, vec3d3, RaycastContext.ShapeType.COLLIDER, RaycastContext.FluidHandling.NONE, (Entity)minecraftClient.player);
            BlockHitResult blockHitResult = minecraftClient.world.raycast(raycastContext);
            if (blockHitResult.getType() != HitResult.Type.BLOCK) {
                return true;
            }
            if (!blockHitResult.getBlockPos().equals((Object)blockPos)) continue;
            return true;
        }
        return false;
    }

    private Direction internalMethod00328(MinecraftClient minecraftClient, BlockPos blockPos) {
        Vec3d vec3d = minecraftClient.player.getEyePos();
        Direction direction = Direction.UP;
        double d = -1.0;
        for (Direction direction2 : Direction.values()) {
            Vec3d vec3d2 = Vec3d.ofCenter((Vec3i)blockPos).add((double)direction2.getOffsetX() * 0.5, (double)direction2.getOffsetY() * 0.5, (double)direction2.getOffsetZ() * 0.5);
            Vec3d vec3d3 = vec3d2.subtract(vec3d).normalize();
            double d2 = vec3d3.x * (double)direction2.getOffsetX() + vec3d3.y * (double)direction2.getOffsetY() + vec3d3.z * (double)direction2.getOffsetZ();
            double d3 = -d2;
            if (!(d3 > d)) continue;
            d = d3;
            direction = direction2;
        }
        return direction;
    }

    private void internalMethod08315() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.interactionManager != null) {
            minecraftClient.interactionManager.cancelBlockBreaking();
        }
    }

    @Override
    public void internalMethod04086() {
        if (this.internalField0494 != null) {
            this.internalField0494.internalMethod04086();
            this.internalField0494 = null;
        }
        this.internalMethod08315();
        MovementInputController typedValue290 = NewtonCoreManager.internalMethod00114().internalMethod00183();
        typedValue290.internalMethod01287();
        this.internalField1131 = null;
        this.internalField0277 = false;
    }

    @Override
    public void internalMethod04089() {
        this.internalField0276 = true;
        if (this.internalField0494 != null) {
            this.internalField0494.internalMethod04089();
        }
        this.internalMethod08315();
        NewtonCoreManager.internalMethod00114().internalMethod00183().internalMethod01287();
    }

    @Override
    public void internalMethod08146() {
        this.internalField0276 = false;
        if (this.internalField0494 != null) {
            this.internalField0494.internalMethod08146();
        }
    }

    @Override
    public boolean internalMethod04090() {
        return this.internalField0276;
    }

    @Override
    public boolean internalMethod08147() {
        return this.internalField1099;
    }
}

