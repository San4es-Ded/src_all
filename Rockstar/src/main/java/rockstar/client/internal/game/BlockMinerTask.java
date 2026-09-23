package rockstar.client.internal.game;




import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Position;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import rockstar.client.internal.core.BotConfig;
import rockstar.client.internal.core.BotBehavior;
import rockstar.client.bot.BotTargetManager;

public class BlockMinerTask
implements BotBehavior {
    private final Set<Block> internalField0546 = new HashSet<Block>();
    private BlockPos internalField0352;
    private Direction internalField0150 = Direction.UP;
    private long internalField0229;
    private int internalField0227;

    public void internalMethod05516(Block block) {
        if (block != null) {
            this.internalField0546.add(block);
        }
    }

    public void internalMethod04028() {
        this.internalField0546.clear();
        this.internalField0352 = null;
    }

    @Override
    public void internalMethod07229(BotTargetManager typedValue055) {
        if (typedValue055 == null || this.internalField0546.isEmpty()) {
            return;
        }
        if (this.internalField0352 == null || !this.internalMethod02642(typedValue055, this.internalField0352)) {
            this.internalField0352 = this.internalMethod07044(typedValue055);
        }
        if (this.internalField0352 == null) {
            typedValue055.internalMethod09733();
            return;
        }
        Vec3d vec3d = this.internalField0352.toCenterPos();
        typedValue055.internalMethod05389(vec3d.x, vec3d.y, vec3d.z);
        double d = typedValue055.internalMethod06688().internalMethod06297().add(0.0, typedValue055.internalMethod06687().internalMethod10123(), 0.0).distanceTo(vec3d);
        if (d > typedValue055.internalMethod06687().internalMethod09938()) {
            typedValue055.internalMethod01196(vec3d, typedValue055.internalMethod06687().internalMethod09938() * 0.8);
            return;
        }
        typedValue055.internalMethod09733();
        this.internalField0150 = this.internalMethod01931(typedValue055, this.internalField0352);
        if (!this.internalMethod02996(typedValue055, vec3d)) {
            return;
        }
        long l = System.currentTimeMillis();
        if (l - this.internalField0229 < typedValue055.internalMethod06687().internalMethod07719()) {
            return;
        }
        typedValue055.internalMethod05233(new BlockHitResult(vec3d, this.internalField0150, this.internalField0352, false));
        this.internalField0229 = l;
    }

    private BlockPos internalMethod07044(BotTargetManager typedValue055) {
        if (this.internalField0227 > 0) {
            --this.internalField0227;
            return this.internalField0352;
        }
        this.internalField0227 = Math.max(1, typedValue055.internalMethod06687().internalMethod07714());
        BotConfig typedParameter029 = typedValue055.internalMethod06687();
        BlockPos blockPos = BlockPos.ofFloored((Position)typedValue055.internalMethod06688().internalMethod06297());
        int n = Math.max(1, typedParameter029.internalMethod04128());
        int n2 = Math.max(1, typedParameter029.internalMethod04161());
        BlockPos blockPos2 = null;
        double d = Double.MAX_VALUE;
        for (int i = -n; i <= n; ++i) {
            for (int j = -n2; j <= n2; ++j) {
                for (int k = -n; k <= n; ++k) {
                    double d2;
                    BlockPos blockPos3 = blockPos.add(i, j, k);
                    if (!this.internalMethod02642(typedValue055, blockPos3) || !((d2 = blockPos3.getSquaredDistance((Vec3i)blockPos)) < d)) continue;
                    blockPos2 = blockPos3.toImmutable();
                    d = d2;
                }
            }
        }
        return blockPos2;
    }

    private boolean internalMethod02642(BotTargetManager typedValue055, BlockPos blockPos) {
        if (typedValue055 == null || blockPos == null) {
            return false;
        }
        if (!typedValue055.internalMethod00273().internalMethod03187(blockPos) && !typedValue055.internalMethod00273().internalMethod04617(blockPos)) {
            return false;
        }
        BlockState blockState = typedValue055.internalMethod00273().internalMethod00820(blockPos);
        return !blockState.isAir() && this.internalField0546.contains(blockState.getBlock());
    }

    private Direction internalMethod01931(BotTargetManager typedValue055, BlockPos blockPos) {
        Vec3d vec3d = typedValue055.internalMethod06688().internalMethod06297().add(0.0, typedValue055.internalMethod06687().internalMethod10123(), 0.0);
        Vec3d vec3d2 = blockPos.toCenterPos();
        return Direction.getFacing((double)(vec3d.x - vec3d2.x), (double)(vec3d.y - vec3d2.y), (double)(vec3d.z - vec3d2.z));
    }

    private boolean internalMethod02996(BotTargetManager typedValue055, Vec3d vec3d) {
        Vec3d vec3d2 = typedValue055.internalMethod06688().internalMethod06297().add(0.0, typedValue055.internalMethod06687().internalMethod10123(), 0.0);
        double d = vec3d.x - vec3d2.x;
        double d2 = vec3d.y - vec3d2.y;
        double d3 = vec3d.z - vec3d2.z;
        double d4 = Math.sqrt(d * d + d3 * d3);
        float f = (float)Math.toDegrees(Math.atan2(-d, d3));
        float f2 = (float)Math.toDegrees(-Math.atan2(d2, d4));
        double d5 = Math.abs(MathHelper.wrapDegrees((float)(f - typedValue055.internalMethod06688().internalMethod02808())));
        double d6 = Math.abs(f2 - typedValue055.internalMethod06688().internalMethod02817());
        return d5 <= typedValue055.internalMethod06687().internalMethod09972() && d6 <= typedValue055.internalMethod06687().internalMethod09972();
    }

    @Override
    public String internalMethod06553() {
        return "Mining " + this.internalField0546.size();
    }
}

