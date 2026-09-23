package rockstar.client.internal.game;




import rockstar.client.bot.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.BlockView;
import net.minecraft.world.EmptyBlockView;
import rockstar.client.internal.core.BotBehavior;
import rockstar.client.bot.BotTargetManager;

public class InstantRebreakTask
implements BotBehavior {
    private BlockPos internalField0352;
    private Direction internalField0150 = Direction.UP;
    private long internalField0229;
    private boolean internalField0277;

    @Override
    public void internalMethod07229(BotTargetManager typedValue055) {
        long l;
        if (typedValue055 == null || !typedValue055.internalMethod07697()) {
            return;
        }
        BotTargetManager.InternalType0481 nestedValue0169 = typedValue055.internalMethod01916(typedValue055.internalMethod06687().internalMethod09202());
        if (nestedValue0169 == null || !this.internalMethod04996(typedValue055, nestedValue0169.internalMethod04871().getBlockPos())) {
            this.internalField0277 = false;
            typedValue055.internalMethod09733();
            return;
        }
        BlockPos blockPos = nestedValue0169.internalMethod04871().getBlockPos().toImmutable();
        Direction direction = nestedValue0169.internalMethod04871().getSide();
        if (!blockPos.equals((Object)this.internalField0352)) {
            this.internalField0352 = blockPos;
            this.internalField0277 = false;
        }
        this.internalField0150 = direction == null ? this.internalField0150 : direction;
        typedValue055.internalMethod05389(blockPos.toCenterPos().x, blockPos.toCenterPos().y, blockPos.toCenterPos().z);
        typedValue055.internalMethod09733();
        if (!typedValue055.internalMethod01197(blockPos.toCenterPos(), typedValue055.internalMethod06687().internalMethod09974())) {
            return;
        }
        if (!this.internalField0277) {
            typedValue055.internalMethod06497(blockPos, this.internalField0150);
            this.internalField0277 = true;
        }
        if ((l = System.currentTimeMillis()) - this.internalField0229 >= typedValue055.internalMethod06687().internalMethod09842()) {
            typedValue055.internalMethod05595(blockPos, this.internalField0150);
            this.internalField0229 = l;
        }
    }

    private boolean internalMethod04996(BotTargetManager typedValue055, BlockPos blockPos) {
        if (blockPos == null || !typedValue055.internalMethod00273().internalMethod03187(blockPos) && !typedValue055.internalMethod00273().internalMethod04617(blockPos)) {
            return false;
        }
        BlockState blockState = typedValue055.internalMethod00273().internalMethod00820(blockPos);
        return !blockState.isAir() && blockState.getHardness((BlockView)EmptyBlockView.INSTANCE, blockPos) >= 0.0f;
    }

    @Override
    public String internalMethod06553() {
        return this.internalField0352 == null ? "InstantRebreak" : "InstantRebreak " + this.internalField0352.getX() + " " + this.internalField0352.getY() + " " + this.internalField0352.getZ();
    }
}

