package pyrock.classes;








import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.inventory.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.command.*;
import java.util.List;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.internal.core.NewtonGlobalState;
import rockstar.client.internal.game.RegionSelection;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.game.BlockSuggestionProvider;
import rockstar.client.internal.core.PathExecutor;
import rockstar.client.internal.game.PathNode;
import rockstar.client.internal.game.BlockPositionGoal;
import rockstar.client.internal.game.RadiusGoal;
import rockstar.client.internal.game.ColumnGoal;
import rockstar.client.internal.game.HeightLevelGoal;
import rockstar.client.internal.game.PathGoal;
import rockstar.client.internal.rotation.AbstractPathStep;
import rockstar.client.internal.script.ElytraFlightTask;
import rockstar.client.internal.game.ExcavationTask;
import rockstar.client.internal.inventory.AreaFillTask;
import rockstar.client.internal.rotation.GotoPathTask;
import rockstar.client.internal.game.BlockQuarryTask;
import rockstar.client.internal.core.NewtonTask;

public class PyNewton {
    public boolean ready() {
        return NewtonCoreManager.internalMethod00010();
    }

    public boolean active() {
        return this.ready() && NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03477();
    }

    public boolean goTo(int n, int n2, int n3) {
        return this.start(new GotoPathTask(new BlockPositionGoal(new BlockPos(n, n2, n3))));
    }

    public boolean goToNear(int n, int n2, int n3, int n4) {
        return this.start(new GotoPathTask(new RadiusGoal(new BlockPos(n, n2, n3), Math.max(0, n4))));
    }

    public boolean goToXZ(int n, int n2) {
        return this.start(new GotoPathTask(new ColumnGoal(n, n2)));
    }

    public boolean goToY(int n) {
        return this.start(new GotoPathTask(new HeightLevelGoal(n)));
    }

    public boolean flyTo(int n, int n2, int n3, boolean bl) {
        return this.start(new ElytraFlightTask(n, n2, n3, bl));
    }

    public boolean mine(String string) {
        Block block = BlockSuggestionProvider.internalMethod05431(string);
        if (block == null) {
            return false;
        }
        return this.start(new BlockQuarryTask(block));
    }

    public boolean excavate(int n, int n2, int n3, int n4, int n5, int n6, @Nullable String string) {
        Block block = null;
        if (string != null && !string.isEmpty() && (block = BlockSuggestionProvider.internalMethod05431(string)) == null) {
            return false;
        }
        return this.start(new ExcavationTask(PyNewton.min(n, n2, n3, n4, n5, n6), PyNewton.max(n, n2, n3, n4, n5, n6), block));
    }

    public boolean fill(int n, int n2, int n3, int n4, int n5, int n6, String string) {
        Block block = BlockSuggestionProvider.internalMethod05431(string);
        if (block == null) {
            return false;
        }
        return this.start(new AreaFillTask(PyNewton.min(n, n2, n3, n4, n5, n6), PyNewton.max(n, n2, n3, n4, n5, n6), block));
    }

    public void cancel() {
        if (!this.ready()) {
            return;
        }
        NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03476();
        NewtonCoreManager.internalMethod00114().internalMethod00183().internalMethod01287();
    }

    public boolean pause() {
        NewtonTask typedValue308 = this.current();
        if (typedValue308 == null) {
            return false;
        }
        typedValue308.internalMethod04089();
        return true;
    }

    public boolean resume() {
        NewtonTask typedValue308 = this.current();
        if (typedValue308 == null) {
            return false;
        }
        typedValue308.internalMethod08146();
        return true;
    }

    public boolean paused() {
        NewtonTask typedValue308 = this.current();
        return typedValue308 != null && typedValue308.internalMethod04090();
    }

    @Nullable
    public String process() {
        NewtonTask typedValue308 = this.current();
        return typedValue308 == null ? null : typedValue308.internalMethod01129();
    }

    @Nullable
    public String status() {
        NewtonTask typedValue308 = this.current();
        return typedValue308 == null ? null : typedValue308.internalMethod05788();
    }

    public boolean command(String string) {
        if (!this.ready() || string == null || string.isBlank()) {
            return false;
        }
        try {
            CommandRegistry typedValue128 = NewtonCoreManager.internalMethod00114().internalMethod06865();
            return typedValue128.internalMethod04610(typedValue128.internalMethod03606() + " newton " + string);
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    public int pathSteps() {
        PathExecutor typedValue294 = this.executor();
        return typedValue294 == null ? 0 : typedValue294.internalMethod00712().internalMethod02878().size();
    }

    public int pathStep() {
        PathExecutor typedValue294 = this.executor();
        return typedValue294 == null ? 0 : typedValue294.internalMethod03485();
    }

    public int @Nullable [] nextNode() {
        PathExecutor typedValue294 = this.executor();
        if (typedValue294 == null) {
            return null;
        }
        List<AbstractPathStep> list = typedValue294.internalMethod00712().internalMethod02878();
        int n = typedValue294.internalMethod03485();
        if (n >= list.size()) {
            return null;
        }
        PathNode typedValue296 = list.get(n).internalMethod02540();
        return new int[]{typedValue296.internalMethod02945(), typedValue296.internalMethod02949(), typedValue296.internalMethod07945()};
    }

    public double @Nullable [] goalPos() {
        double[] dArray;
        PathGoal typedValue302;
        PathExecutor typedValue294 = this.executor();
        PathGoal typedValue303 = typedValue302 = typedValue294 == null ? null : typedValue294.internalMethod00771();
        if (typedValue302 == null) {
            return null;
        }
        Vec3d vec3d = typedValue302.internalMethod07298();
        if (vec3d == null) {
            dArray = null;
        } else {
            double[] dArray2 = new double[3];
            dArray2[0] = vec3d.x;
            dArray2[1] = vec3d.y;
            dArray = dArray2;
            dArray2[2] = vec3d.z;
        }
        return dArray;
    }

    public void select(int n, int n2, int n3) {
        RegionSelection.internalMethod00889().internalMethod04566(new BlockPos(n, n2, n3));
    }

    public void selectClear() {
        RegionSelection.internalMethod00889().internalMethod05224();
    }

    public int @Nullable [] selection() {
        RegionSelection typedValue288 = RegionSelection.internalMethod00889();
        if (!typedValue288.internalMethod05225()) {
            return null;
        }
        BlockPos blockPos = typedValue288.internalMethod08346();
        BlockPos blockPos2 = typedValue288.internalMethod09055();
        return new int[]{blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ()};
    }

    public boolean excavateSelection(@Nullable String string) {
        RegionSelection typedValue288 = RegionSelection.internalMethod00889();
        if (!typedValue288.internalMethod05225()) {
            return false;
        }
        BlockPos blockPos = typedValue288.internalMethod08346();
        BlockPos blockPos2 = typedValue288.internalMethod09055();
        return this.excavate(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), string);
    }

    public boolean fillSelection(String string) {
        RegionSelection typedValue288 = RegionSelection.internalMethod00889();
        if (!typedValue288.internalMethod05225()) {
            return false;
        }
        BlockPos blockPos = typedValue288.internalMethod08346();
        BlockPos blockPos2 = typedValue288.internalMethod09055();
        return this.fill(blockPos.getX(), blockPos.getY(), blockPos.getZ(), blockPos2.getX(), blockPos2.getY(), blockPos2.getZ(), string);
    }

    public boolean safewalk() {
        return NewtonGlobalState.internalField0277;
    }

    public void setSafewalk(boolean bl) {
        NewtonGlobalState.internalField0277 = bl;
    }

    public boolean logging() {
        return NewtonGlobalState.internalField1099;
    }

    public void setLogging(boolean bl) {
        NewtonGlobalState.internalField1099 = bl;
    }

    private boolean start(NewtonTask typedValue308) {
        if (!this.ready()) {
            return false;
        }
        try {
            NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(typedValue308);
            return true;
        }
        catch (Throwable throwable) {
            return false;
        }
    }

    @Nullable
    private NewtonTask current() {
        return this.ready() ? (NewtonTask)NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03684().orElse(null) : null;
    }

    @Nullable
    private PathExecutor executor() {
        return this.ready() ? NewtonCoreManager.internalMethod00114().internalMethod01484() : null;
    }

    private static BlockPos min(int n, int n2, int n3, int n4, int n5, int n6) {
        return new BlockPos(Math.min(n, n4), Math.min(n2, n5), Math.min(n3, n6));
    }

    private static BlockPos max(int n, int n2, int n3, int n4, int n5, int n6) {
        return new BlockPos(Math.max(n, n4), Math.max(n2, n5), Math.max(n3, n6));
    }
}

