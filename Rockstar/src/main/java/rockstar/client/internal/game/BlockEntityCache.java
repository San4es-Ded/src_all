package rockstar.client.internal.game;


import rockstar.client.*;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import lombok.Generated;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public final class BlockEntityCache {
    private static final Map<BlockPos, BlockEntity> internalField0543 = new ConcurrentHashMap<BlockPos, BlockEntity>();

    public static void internalMethod04676(BlockEntity blockEntity) {
        if (blockEntity != null) {
            internalField0543.put(blockEntity.getPos(), blockEntity);
        }
    }

    public static void internalMethod07047(BlockPos blockPos) {
        if (blockPos != null) {
            internalField0543.remove(blockPos);
        }
    }

    public static BlockEntity internalMethod01024(BlockPos blockPos) {
        return internalField0543.get(blockPos);
    }

    public static Collection<BlockEntity> internalMethod06956() {
        return internalField0543.values();
    }

    public static void internalMethod04034() {
        internalField0543.clear();
    }

    @Generated
    private BlockEntityCache() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }
}

