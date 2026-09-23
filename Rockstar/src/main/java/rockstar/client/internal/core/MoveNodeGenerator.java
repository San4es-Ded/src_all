package rockstar.client.internal.core;





import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import rockstar.client.internal.game.PathfindBlockView;
import rockstar.client.internal.game.PathNode;
import rockstar.client.internal.rotation.AbstractPathStep;
import rockstar.client.internal.core.AscendMoveNode;
import rockstar.client.internal.rotation.BlockBreakPathStep;
import rockstar.client.internal.rotation.AscendPathStep;
import rockstar.client.internal.rotation.DescendPathStep;
import rockstar.client.internal.game.BlockBreakTransition;
import rockstar.client.internal.game.TraversalTransition;
import rockstar.client.internal.core.DescendMoveNode;
import rockstar.client.internal.core.DiagonalMoveNode;
import rockstar.client.internal.core.FallMoveNode;
import rockstar.client.internal.core.ParkourMoveNode;
import rockstar.client.internal.rotation.BasicMovementPathStep;
import rockstar.client.internal.core.WalkMoveNode;

public final class MoveNodeGenerator {
    private static final int[][] internalField0040 = new int[][]{{1, 0}, {-1, 0}, {0, 1}, {0, -1}};
    private static final int[][] internalField0041 = new int[][]{{1, 1}, {1, -1}, {-1, 1}, {-1, -1}};

    private MoveNodeGenerator() {
    }

    public static List<AbstractPathStep> internalMethod03577(PathNode typedValue296, PathfindBlockView typedValue292) {
        ArrayList<AbstractPathStep> arrayList = new ArrayList<AbstractPathStep>(24);
        boolean bl = typedValue292.internalMethod09396(typedValue296.internalMethod02945(), typedValue296.internalMethod02949(), typedValue296.internalMethod07945());
        boolean bl2 = typedValue292.internalMethod08763(typedValue296.internalMethod02945(), typedValue296.internalMethod02949(), typedValue296.internalMethod07945());
        boolean bl3 = typedValue292.internalMethod07759(typedValue296.internalMethod02945(), typedValue296.internalMethod02949(), typedValue296.internalMethod07945());
        if (bl) {
            MoveNodeGenerator.internalMethod02583(typedValue296, typedValue292, arrayList);
        }
        if (bl3) {
            MoveNodeGenerator.internalMethod04019(typedValue296, typedValue292, arrayList, bl);
        }
        if (bl2) {
            MoveNodeGenerator.internalMethod01096(typedValue296, typedValue292, arrayList, bl);
        }
        if (!(bl || bl2 || bl3)) {
            MoveNodeGenerator.internalMethod02583(typedValue296, typedValue292, arrayList);
            MoveNodeGenerator.internalMethod01096(typedValue296, typedValue292, arrayList, false);
        }
        return arrayList;
    }

    private static void internalMethod02583(PathNode typedValue296, PathfindBlockView typedValue292, List<AbstractPathStep> list) {
        int n;
        int n2;
        int n3 = typedValue296.internalMethod02945();
        double d = typedValue292.internalMethod05956(n3, n2 = typedValue296.internalMethod02949(), n = typedValue296.internalMethod07945());
        if (Double.isNaN(d)) {
            d = n2;
        }
        for (int[] nArray : internalField0040) {
            int n4;
            int n5;
            int n6 = n3 + nArray[0];
            int n7 = n + nArray[1];
            boolean bl = false;
            for (n5 = 1; n5 >= -1; --n5) {
                n4 = n2 + n5;
                double d2 = typedValue292.internalMethod05956(n6, n4, n7);
                if (Double.isNaN(d2)) continue;
                double d3 = d2 - d;
                PathNode typedValue297 = new PathNode(n6, n4, n7);
                if (Math.abs(d3) <= 0.62) {
                    bl |= MoveNodeGenerator.internalMethod07018(new WalkMoveNode(typedValue296, typedValue297), typedValue292, list);
                    continue;
                }
                if (d3 > 0.62 && d3 <= 1.3) {
                    bl |= MoveNodeGenerator.internalMethod07018(new AscendMoveNode(typedValue296, typedValue297), typedValue292, list);
                    continue;
                }
                if (!(d3 < -0.62) || !(d3 >= -1.3)) continue;
                bl |= MoveNodeGenerator.internalMethod07018(new DescendMoveNode(typedValue296, typedValue297), typedValue292, list);
            }
            if (!bl) {
                if (typedValue292.internalMethod06691()) {
                    MoveNodeGenerator.internalMethod07018(new AscendPathStep(typedValue296, new PathNode(n6, n2 + 1, n7)), typedValue292, list);
                    MoveNodeGenerator.internalMethod07018(new BlockBreakPathStep(typedValue296, new PathNode(n6, n2, n7)), typedValue292, list);
                }
                n5 = 0;
                for (n4 = 2; n4 <= 12; ++n4) {
                    if (!MoveNodeGenerator.internalMethod07018(new FallMoveNode(typedValue296, new PathNode(n6, n2 - n4, n7)), typedValue292, list)) continue;
                    n5 = 1;
                    break;
                }
                if (n5 == 0 && typedValue292.internalMethod06691()) {
                    MoveNodeGenerator.internalMethod07018(new DescendPathStep(typedValue296, new PathNode(n6, n2 - 1, n7)), typedValue292, list);
                }
                MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n6, n2, n7), BasicMovementPathStep.InternalType0152.internalField0983), typedValue292, list);
                MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n6, n2 - 1, n7), BasicMovementPathStep.InternalType0152.internalField0983), typedValue292, list);
                MoveNodeGenerator.internalMethod07018(new WalkMoveNode(typedValue296, new PathNode(n6, n2, n7), true), typedValue292, list);
                MoveNodeGenerator.internalMethod07018(new WalkMoveNode(typedValue296, new PathNode(n6, n2 - 1, n7), true), typedValue292, list);
            }
            MoveNodeGenerator.internalMethod07018(new ParkourMoveNode(typedValue296, new PathNode(n3 + nArray[0] * 2, n2, n + nArray[1] * 2)), typedValue292, list);
            MoveNodeGenerator.internalMethod07018(new ParkourMoveNode(typedValue296, new PathNode(n3 + nArray[0] * 3, n2, n + nArray[1] * 3)), typedValue292, list);
        }
        for (int[] nArray : internalField0041) {
            MoveNodeGenerator.internalMethod07018(new DiagonalMoveNode(typedValue296, new PathNode(n3 + nArray[0], n2, n + nArray[1])), typedValue292, list);
        }
        if (typedValue292.internalMethod06691()) {
            MoveNodeGenerator.internalMethod07018(new BlockBreakTransition(typedValue296, new PathNode(n3, n2 - 1, n)), typedValue292, list);
        }
        if (typedValue292.internalMethod07759(n3, n2, n)) {
            MoveNodeGenerator.internalMethod07018(new TraversalTransition(typedValue296, new PathNode(n3, n2 + 1, n), TraversalTransition.InternalType0062.internalField0690), typedValue292, list);
        }
    }

    private static void internalMethod04019(PathNode typedValue296, PathfindBlockView typedValue292, List<AbstractPathStep> list, boolean bl) {
        int n;
        int n2;
        int n3 = typedValue296.internalMethod02945();
        boolean bl2 = MoveNodeGenerator.internalMethod07018(new TraversalTransition(typedValue296, new PathNode(n3, (n2 = typedValue296.internalMethod02949()) + 1, n = typedValue296.internalMethod07945()), TraversalTransition.InternalType0062.internalField0690), typedValue292, list);
        if (!bl2) {
            for (int[] nArray : internalField0040) {
                MoveNodeGenerator.internalMethod07018(new TraversalTransition(typedValue296, new PathNode(n3 + nArray[0], n2 + 1, n + nArray[1]), TraversalTransition.InternalType0062.internalField1279), typedValue292, list);
            }
        }
        if (!bl) {
            MoveNodeGenerator.internalMethod07018(new TraversalTransition(typedValue296, new PathNode(n3, n2 - 1, n), TraversalTransition.InternalType0062.internalField0689), typedValue292, list);
            for (int[] nArray : internalField0040) {
                MoveNodeGenerator.internalMethod07018(new WalkMoveNode(typedValue296, new PathNode(n3 + nArray[0], n2, n + nArray[1])), typedValue292, list);
                MoveNodeGenerator.internalMethod07018(new DescendMoveNode(typedValue296, new PathNode(n3 + nArray[0], n2 - 1, n + nArray[1])), typedValue292, list);
            }
        }
    }

    private static void internalMethod01096(PathNode typedValue296, PathfindBlockView typedValue292, List<AbstractPathStep> list, boolean bl) {
        int n = typedValue296.internalMethod02945();
        int n2 = typedValue296.internalMethod02949();
        int n3 = typedValue296.internalMethod07945();
        for (int[] nArray : internalField0040) {
            int n4 = n + nArray[0];
            int n5 = n3 + nArray[1];
            MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n4, n2, n5), BasicMovementPathStep.InternalType0152.internalField0076), typedValue292, list);
            if (!bl) {
                MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n4, n2, n5), BasicMovementPathStep.InternalType0152.internalField0982), typedValue292, list);
            }
            MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n4, n2 + 1, n5), BasicMovementPathStep.InternalType0152.internalField0981), typedValue292, list);
        }
        for (int[] nArray : internalField0041) {
            MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n + nArray[0], n2, n3 + nArray[1]), BasicMovementPathStep.InternalType0152.internalField0076), typedValue292, list);
        }
        MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n, n2 + 1, n3), BasicMovementPathStep.InternalType0152.internalField0077), typedValue292, list);
        MoveNodeGenerator.internalMethod07018(new BasicMovementPathStep(typedValue296, new PathNode(n, n2 - 1, n3), BasicMovementPathStep.InternalType0152.internalField0984), typedValue292, list);
    }

    private static boolean internalMethod07018(AbstractPathStep typedValue304, PathfindBlockView typedValue292, List<AbstractPathStep> list) {
        boolean bl;
        if (!typedValue304.internalMethod04946(typedValue292)) {
            return false;
        }
        PathNode typedValue296 = typedValue304.internalMethod02540();
        typedValue304.internalMethod05093(typedValue292.internalMethod02775(typedValue296.internalMethod02945(), typedValue296.internalMethod02949(), typedValue296.internalMethod07945()));
        boolean bl2 = bl = typedValue304 instanceof WalkMoveNode || typedValue304 instanceof DiagonalMoveNode || typedValue304 instanceof AscendMoveNode || typedValue304 instanceof DescendMoveNode;
        if (bl && typedValue292.internalMethod07797(typedValue296.internalMethod02945(), typedValue296.internalMethod02949(), typedValue296.internalMethod07945())) {
            PathNode typedValue297 = typedValue304.internalMethod01873();
            double d = Math.max(1.0, Math.hypot(typedValue296.internalMethod02945() - typedValue297.internalMethod02945(), typedValue296.internalMethod07945() - typedValue297.internalMethod07945()));
            typedValue304.internalMethod05093((typedValue292.internalMethod07797(typedValue296.internalMethod02945(), typedValue296.internalMethod02949() + 1, typedValue296.internalMethod07945()) ? 5.0 : 0.8) * d);
        }
        list.add(typedValue304);
        return true;
    }
}

