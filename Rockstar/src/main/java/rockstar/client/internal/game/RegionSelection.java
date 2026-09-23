package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.jetbrains.annotations.Nullable;

public final class RegionSelection {
    private static final RegionSelection internalField0773 = new RegionSelection();
    @Nullable
    private BlockPos internalField0352;
    @Nullable
    private BlockPos internalField0351;

    public static RegionSelection internalMethod00889() {
        return internalField0773;
    }

    private RegionSelection() {
    }

    public int internalMethod04566(BlockPos blockPos) {
        if (this.internalField0352 == null) {
            this.internalField0352 = blockPos;
            return 1;
        }
        if (this.internalField0351 == null) {
            this.internalField0351 = blockPos;
            return 2;
        }
        this.internalField0352 = blockPos;
        this.internalField0351 = null;
        return 1;
    }

    public void internalMethod04567(BlockPos blockPos) {
        this.internalField0352 = blockPos;
    }

    public void internalMethod05870(BlockPos blockPos) {
        this.internalField0351 = blockPos;
    }

    public void internalMethod05224() {
        this.internalField0352 = null;
        this.internalField0351 = null;
    }

    @Nullable
    public BlockPos internalMethod02184() {
        return this.internalField0352;
    }

    @Nullable
    public BlockPos internalMethod05863() {
        return this.internalField0351;
    }

    public boolean internalMethod05225() {
        return this.internalField0352 != null && this.internalField0351 != null;
    }

    public BlockPos internalMethod08346() {
        return new BlockPos(Math.min(this.internalField0352.getX(), this.internalField0351.getX()), Math.min(this.internalField0352.getY(), this.internalField0351.getY()), Math.min(this.internalField0352.getZ(), this.internalField0351.getZ()));
    }

    public BlockPos internalMethod09055() {
        return new BlockPos(Math.max(this.internalField0352.getX(), this.internalField0351.getX()), Math.max(this.internalField0352.getY(), this.internalField0351.getY()), Math.max(this.internalField0352.getZ(), this.internalField0351.getZ()));
    }

    @Nullable
    public Box internalMethod03550() {
        if (this.internalField0352 == null) {
            return null;
        }
        BlockPos blockPos = this.internalField0352;
        BlockPos blockPos2 = this.internalField0351 != null ? this.internalField0351 : this.internalField0352;
        int n = Math.min(blockPos.getX(), blockPos2.getX());
        int n2 = Math.min(blockPos.getY(), blockPos2.getY());
        int n3 = Math.min(blockPos.getZ(), blockPos2.getZ());
        int n4 = Math.max(blockPos.getX(), blockPos2.getX());
        int n5 = Math.max(blockPos.getY(), blockPos2.getY());
        int n6 = Math.max(blockPos.getZ(), blockPos2.getZ());
        return new Box((double)n, (double)n2, (double)n3, (double)(n4 + 1), (double)(n5 + 1), (double)(n6 + 1));
    }
}

