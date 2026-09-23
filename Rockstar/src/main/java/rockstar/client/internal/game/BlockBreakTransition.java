package rockstar.client.internal.game;






import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.script.MovementInputController;
import rockstar.client.internal.inventory.BestToolSelector;
import rockstar.client.internal.rotation.BlockFaceRotationHelper;
import rockstar.client.internal.game.PathfindBlockView;
import rockstar.client.internal.game.PathNode;
import rockstar.client.internal.rotation.AbstractPathStep;
import rockstar.client.internal.rotation.BlockBreakPathStep;

public class BlockBreakTransition
extends AbstractPathStep {
    private double internalField1044 = Double.POSITIVE_INFINITY;
    private int internalField0227;
    private BlockPos internalField0352;
    private boolean internalField0277;
    private final BlockFaceRotationHelper internalField0904 = new BlockFaceRotationHelper();

    public BlockBreakTransition(PathNode typedValue296, PathNode typedValue297) {
        super(typedValue296, typedValue297);
    }

    @Override
    public double internalMethod01349() {
        return this.internalField1044;
    }

    @Override
    public int internalMethod01346() {
        return Math.min(600, this.internalField0227 * 2 + 120);
    }

    @Override
    public boolean internalMethod04946(PathfindBlockView typedValue292) {
        if (this.internalField0924.internalMethod02945() != this.internalField0923.internalMethod02945() || this.internalField0924.internalMethod07945() != this.internalField0923.internalMethod07945()) {
            return false;
        }
        if (this.internalField0924.internalMethod02949() != this.internalField0923.internalMethod02949() - 1) {
            return false;
        }
        int n = typedValue292.internalMethod05957(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() - 1, this.internalField0923.internalMethod07945());
        if (n == Integer.MAX_VALUE || n == 0) {
            return false;
        }
        if (!typedValue292.internalMethod02776(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() - 2, this.internalField0923.internalMethod07945())) {
            return false;
        }
        if (typedValue292.internalMethod08739(this.internalField0923.internalMethod02945(), this.internalField0923.internalMethod02949() - 2, this.internalField0923.internalMethod07945())) {
            return false;
        }
        this.internalField0227 = n;
        double d = (double)n / 20.0;
        this.internalField1044 = 1.0 + d * 1.5 + 0.4;
        return true;
    }

    @Override
    public AbstractPathStep.InternalType0058 internalMethod04014() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
        if (clientPlayerEntity == null || minecraftClient.world == null || minecraftClient.interactionManager == null) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        BlockPos blockPos = new BlockPos(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        BlockState blockState = minecraftClient.world.getBlockState(blockPos);
        boolean bl = !BlockBreakPathStep.internalMethod04846(blockState);
        MovementInputController typedValue290 = NewtonCoreManager.internalMethod00114().internalMethod00183();
        typedValue290.internalMethod01281();
        typedValue290.internalMethod03508(false);
        typedValue290.internalMethod03557(false);
        typedValue290.internalMethod08033(false);
        typedValue290.internalMethod08045(false);
        typedValue290.internalMethod08359(false);
        typedValue290.internalMethod08371(false);
        typedValue290.internalMethod09358(false);
        if (!bl) {
            if (this.internalField0277) {
                minecraftClient.interactionManager.cancelBlockBreaking();
                this.internalField0277 = false;
                this.internalField0352 = null;
                this.internalField0904.internalMethod06793();
            }
            if (clientPlayerEntity.isOnGround() && Math.abs(clientPlayerEntity.getY() - (double)this.internalField0924.internalMethod02949()) < 0.1) {
                return AbstractPathStep.InternalType0058.internalField0454;
            }
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        BestToolSelector.internalMethod04395(blockState);
        if (this.internalField0352 != null && !this.internalField0352.equals((Object)blockPos)) {
            minecraftClient.interactionManager.cancelBlockBreaking();
            this.internalField0904.internalMethod06793();
        }
        this.internalField0352 = blockPos;
        this.internalField0277 = true;
        if (!this.internalField0904.internalMethod05481(blockPos, Direction.UP)) {
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        minecraftClient.interactionManager.updateBlockBreakingProgress(blockPos, Direction.UP);
        clientPlayerEntity.swingHand(clientPlayerEntity.getActiveHand());
        return AbstractPathStep.InternalType0058.internalField0453;
    }

    @Override
    public void internalMethod01347() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.interactionManager != null && this.internalField0277) {
            minecraftClient.interactionManager.cancelBlockBreaking();
        }
        this.internalField0277 = false;
        this.internalField0352 = null;
        this.internalField0904.internalMethod06793();
        MovementInputController typedValue290 = NewtonCoreManager.internalMethod00114().internalMethod00183();
        typedValue290.internalMethod03508(false);
        typedValue290.internalMethod09358(false);
    }
}

