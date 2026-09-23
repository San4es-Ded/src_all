package rockstar.client.internal.game;


import rockstar.client.*;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.game.PathfindBlockView;
import rockstar.client.internal.game.PathNode;

public final class PathStartLocator {
    private PathStartLocator() {
    }

    public static PathNode internalMethod05963(MinecraftClient minecraftClient) {
        PathfindBlockView typedValue292;
        if (minecraftClient.player == null) {
            return new PathNode(0, 0, 0);
        }
        BlockPos blockPos = minecraftClient.player.getBlockPos();
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        try {
            typedValue292 = new PathfindBlockView();
        }
        catch (IllegalStateException illegalStateException) {
            return new PathNode(n, n2, n3);
        }
        if (minecraftClient.player.isTouchingWater() && typedValue292.internalMethod07797(n, n2, n3) && !typedValue292.internalMethod09396(n, n2, n3)) {
            return new PathNode(n, n2, n3);
        }
        if (minecraftClient.player.isClimbing() && !typedValue292.internalMethod09396(n, n2, n3)) {
            return new PathNode(n, n2, n3);
        }
        if (typedValue292.internalMethod02776(n, n2, n3) && typedValue292.internalMethod09396(n, n2 + 1, n3)) {
            return new PathNode(n, n2 + 1, n3);
        }
        if (typedValue292.internalMethod09396(n, n2, n3)) {
            return new PathNode(n, n2, n3);
        }
        PathNode typedValue296 = PathStartLocator.internalMethod01937(typedValue292, minecraftClient, n2);
        if (typedValue296 != null) {
            return typedValue296;
        }
        PathNode typedValue297 = PathStartLocator.internalMethod01937(typedValue292, minecraftClient, n2 + 1);
        return typedValue297 != null ? typedValue297 : new PathNode(n, n2, n3);
    }

    @Nullable
    public static PathNode internalMethod02372(MinecraftClient minecraftClient) {
        PathfindBlockView typedValue292;
        if (minecraftClient.player == null) {
            return null;
        }
        BlockPos blockPos = minecraftClient.player.getBlockPos();
        int n = blockPos.getX();
        int n2 = blockPos.getY();
        int n3 = blockPos.getZ();
        try {
            typedValue292 = new PathfindBlockView();
        }
        catch (IllegalStateException illegalStateException) {
            return null;
        }
        if (typedValue292.internalMethod02776(n, n2, n3) && typedValue292.internalMethod09396(n, n2 + 1, n3)) {
            return null;
        }
        if (typedValue292.internalMethod09396(n, n2, n3)) {
            return null;
        }
        PathNode typedValue296 = PathStartLocator.internalMethod01937(typedValue292, minecraftClient, n2);
        return typedValue296 != null ? typedValue296 : PathStartLocator.internalMethod01937(typedValue292, minecraftClient, n2 + 1);
    }

    @Nullable
    private static PathNode internalMethod01937(PathfindBlockView typedValue292, MinecraftClient minecraftClient, int n) {
        double d = minecraftClient.player.getX();
        double d2 = minecraftClient.player.getZ();
        double d3 = (double)minecraftClient.player.getWidth() * 0.5;
        int n2 = (int)Math.floor(d - d3);
        int n3 = (int)Math.floor(d + d3);
        int n4 = (int)Math.floor(d2 - d3);
        int n5 = (int)Math.floor(d2 + d3);
        PathNode typedValue296 = null;
        double d4 = 0.0;
        for (int i = n2; i <= n3; ++i) {
            for (int j = n4; j <= n5; ++j) {
                double d5;
                double d6;
                double d7;
                if (!typedValue292.internalMethod09396(i, n, j) || !((d7 = (d6 = Math.min((double)(i + 1), d + d3) - Math.max((double)i, d - d3)) * (d5 = Math.min((double)(j + 1), d2 + d3) - Math.max((double)j, d2 - d3))) > d4)) continue;
                d4 = d7;
                typedValue296 = new PathNode(i, n, j);
            }
        }
        return typedValue296;
    }
}

