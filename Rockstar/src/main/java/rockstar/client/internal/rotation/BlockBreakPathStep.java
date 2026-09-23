package rockstar.client.internal.rotation;






import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.BlockView;
import rockstar.client.rotation.Rotation;
import rockstar.client.internal.rotation.SilentRotationHelper;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.script.MovementInputController;
import rockstar.client.internal.inventory.BestToolSelector;
import rockstar.client.internal.rotation.BlockFaceRotationHelper;
import rockstar.client.internal.game.PathfindBlockView;
import rockstar.client.internal.game.PathNode;
import rockstar.client.internal.rotation.AbstractPathStep;

public class BlockBreakPathStep
extends AbstractPathStep {
    private boolean internalField0277;
    private BlockPos internalField0352;
    private final BlockFaceRotationHelper internalField0904 = new BlockFaceRotationHelper();
    private double internalField1044 = Double.POSITIVE_INFINITY;
    private int internalField0227;

    public BlockBreakPathStep(PathNode typedValue296, PathNode typedValue297) {
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
        int n;
        if (this.internalField0924.internalMethod02949() != this.internalField0923.internalMethod02949()) {
            return false;
        }
        int n2 = this.internalField0924.internalMethod02945() - this.internalField0923.internalMethod02945();
        int n3 = this.internalField0924.internalMethod07945() - this.internalField0923.internalMethod07945();
        if (Math.abs(n2) + Math.abs(n3) != 1) {
            return false;
        }
        if (!typedValue292.internalMethod02776(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() - 1, this.internalField0924.internalMethod07945())) {
            return false;
        }
        if (typedValue292.internalMethod08739(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() - 1, this.internalField0924.internalMethod07945())) {
            return false;
        }
        int n4 = typedValue292.internalMethod03057(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945(), this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        int n5 = typedValue292.internalMethod03057(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945(), this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945());
        if (n4 == Integer.MAX_VALUE || n5 == Integer.MAX_VALUE) {
            return false;
        }
        if (n4 == 0 && n5 == 0) {
            return false;
        }
        this.internalField0227 = n = n4 + n5;
        int n6 = (n4 > 0 ? 1 : 0) + (n5 > 0 ? 1 : 0);
        double d = (double)n / 20.0;
        this.internalField1044 = 1.0 + d * 1.5 + (double)n6 * 0.4;
        return true;
    }

    @Override
    public AbstractPathStep.InternalType0058 internalMethod04014() {
        BlockState blockState;
        boolean bl;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
        if (clientPlayerEntity == null || minecraftClient.world == null || minecraftClient.interactionManager == null) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        BlockPos blockPos = new BlockPos(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949() + 1, this.internalField0924.internalMethod07945());
        BlockPos blockPos2 = new BlockPos(this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        BlockState blockState2 = minecraftClient.world.getBlockState(blockPos);
        BlockState blockState3 = minecraftClient.world.getBlockState(blockPos2);
        boolean bl2 = !PathfindBlockView.internalMethod05828((BlockView)minecraftClient.world, blockState2, blockPos, this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        boolean bl3 = bl = !PathfindBlockView.internalMethod05828((BlockView)minecraftClient.world, blockState3, blockPos2, this.internalField0924.internalMethod02945(), this.internalField0924.internalMethod02949(), this.internalField0924.internalMethod07945());
        if (!bl2 && !bl) {
            return this.internalMethod02973();
        }
        MovementInputController typedValue290 = NewtonCoreManager.internalMethod00114().internalMethod00183();
        typedValue290.internalMethod01281();
        typedValue290.internalMethod03508(false);
        typedValue290.internalMethod03557(false);
        typedValue290.internalMethod08033(false);
        typedValue290.internalMethod08045(false);
        typedValue290.internalMethod08359(false);
        typedValue290.internalMethod08371(false);
        typedValue290.internalMethod09358(false);
        BlockPos blockPos3 = bl2 ? blockPos : blockPos2;
        BlockState blockState4 = blockState = bl2 ? blockState2 : blockState3;
        if (this.internalField0352 != null && !this.internalField0352.equals((Object)blockPos3)) {
            minecraftClient.interactionManager.cancelBlockBreaking();
            this.internalField0904.internalMethod06793();
        }
        this.internalField0352 = blockPos3;
        this.internalField0277 = true;
        double d = Math.hypot(clientPlayerEntity.getX() - ((double)this.internalField0923.internalMethod02945() + 0.5), clientPlayerEntity.getZ() - ((double)this.internalField0923.internalMethod07945() + 0.5));
        if (d > 1.5) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        BestToolSelector.internalMethod04395(blockState);
        Direction direction = BlockBreakPathStep.internalMethod01942(clientPlayerEntity, blockPos3);
        if (!this.internalField0904.internalMethod05481(blockPos3, direction)) {
            return AbstractPathStep.InternalType0058.internalField0453;
        }
        minecraftClient.interactionManager.updateBlockBreakingProgress(blockPos3, direction);
        clientPlayerEntity.swingHand(clientPlayerEntity.getActiveHand());
        return AbstractPathStep.InternalType0058.internalField0453;
    }

    private AbstractPathStep.InternalType0058 internalMethod02973() {
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        ClientPlayerEntity clientPlayerEntity = minecraftClient.player;
        if (clientPlayerEntity == null) {
            return AbstractPathStep.InternalType0058.internalField1160;
        }
        if (this.internalField0277) {
            minecraftClient.interactionManager.cancelBlockBreaking();
            this.internalField0277 = false;
            this.internalField0352 = null;
            this.internalField0904.internalMethod06793();
        }
        if (this.internalMethod05161(0.4) && Math.abs(clientPlayerEntity.getY() - (double)this.internalField0924.internalMethod02949()) < 0.6 && clientPlayerEntity.isOnGround()) {
            return AbstractPathStep.InternalType0058.internalField0454;
        }
        Vec3d vec3d = new Vec3d((double)this.internalField0924.internalMethod02945() + 0.5, (double)this.internalField0924.internalMethod02949(), (double)this.internalField0924.internalMethod07945() + 0.5);
        double d = vec3d.x - clientPlayerEntity.getX();
        double d2 = vec3d.z - clientPlayerEntity.getZ();
        float f = (float)Math.toDegrees(Math.atan2(d2, d)) - 90.0f;
        SilentRotationHelper.internalMethod05978(new Rotation(f, 0.0f));
        MovementInputController typedValue290 = NewtonCoreManager.internalMethod00114().internalMethod00183();
        typedValue290.internalMethod01281();
        typedValue290.internalMethod03508(true);
        typedValue290.internalMethod03557(false);
        typedValue290.internalMethod08033(false);
        typedValue290.internalMethod08045(false);
        typedValue290.internalMethod08359(false);
        typedValue290.internalMethod08371(false);
        typedValue290.internalMethod09358(false);
        return AbstractPathStep.InternalType0058.internalField0453;
    }

    public static boolean internalMethod04846(BlockState blockState) {
        return BlockBreakPathStep.internalMethod00979(blockState);
    }

    private static boolean internalMethod00979(BlockState blockState) {
        if (blockState.isAir()) {
            return true;
        }
        if (blockState.isReplaceable()) {
            return true;
        }
        if (!blockState.getFluidState().isEmpty()) {
            return false;
        }
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.world == null) {
            return false;
        }
        return blockState.getCollisionShape((BlockView)minecraftClient.world, BlockPos.ORIGIN).isEmpty();
    }

    private static Direction internalMethod01942(ClientPlayerEntity clientPlayerEntity, BlockPos blockPos) {
        Vec3d vec3d = clientPlayerEntity.getEyePos();
        Direction direction = Direction.UP;
        double d = -1.7976931348623157E308;
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

