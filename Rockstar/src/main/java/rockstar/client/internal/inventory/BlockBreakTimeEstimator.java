package rockstar.client.internal.inventory;


import rockstar.client.*;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import rockstar.client.internal.inventory.BestToolSelector;

public final class BlockBreakTimeEstimator {
    public static final int internalField0227 = 200;

    private BlockBreakTimeEstimator() {
    }

    public static int internalMethod02037(BlockState blockState, BlockPos blockPos) {
        boolean bl;
        float f;
        MinecraftClient minecraftClient = MinecraftClient.getInstance();
        if (minecraftClient.world == null || minecraftClient.player == null) {
            return Integer.MAX_VALUE;
        }
        if (blockState.isAir()) {
            return 0;
        }
        float f2 = blockState.getHardness((BlockView)minecraftClient.world, blockPos);
        if (f2 < 0.0f) {
            return Integer.MAX_VALUE;
        }
        if (f2 == 0.0f) {
            return 1;
        }
        BestToolSelector.InternalType0256 nestedValue0099 = BestToolSelector.internalMethod06939(blockState);
        ItemStack itemStack = nestedValue0099 != null ? minecraftClient.player.getInventory().getStack(nestedValue0099.internalMethod07557()) : ItemStack.EMPTY;
        float f3 = BestToolSelector.internalMethod00662(itemStack, blockState);
        float f4 = f3 / f2 / (f = (bl = BlockBreakTimeEstimator.internalMethod02000(itemStack, blockState)) ? 30.0f : 100.0f);
        if (f4 <= 0.0f) {
            return Integer.MAX_VALUE;
        }
        int n = (int)Math.ceil(1.0 / (double)f4);
        return n;
    }

    public static int internalMethod05452(BlockState blockState) {
        return BlockBreakTimeEstimator.internalMethod02037(blockState, BlockPos.ORIGIN);
    }

    public static boolean internalMethod02000(ItemStack itemStack, BlockState blockState) {
        if (!blockState.isToolRequired()) {
            return true;
        }
        return itemStack.isSuitableFor(blockState);
    }
}

