package rockstar.client.internal.script;







import rockstar.client.rotation.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.command.*;
import rockstar.client.*;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.block.Block;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.internal.game.PathCommandApi;
import rockstar.client.internal.core.NewtonTaskHandle;
import rockstar.client.internal.core.NewtonTaskListener;
import rockstar.client.internal.core.NewtonTaskEvent;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.game.BlockPositionGoal;
import rockstar.client.internal.game.PathGoal;
import rockstar.client.internal.script.ElytraFlightTask;
import rockstar.client.internal.rotation.GotoPathTask;
import rockstar.client.internal.game.BlockQuarryTask;
import rockstar.client.internal.core.NewtonTask;

public final class PathCommandService
implements PathCommandApi {
    private final List<NewtonTaskListener> internalField0416 = new CopyOnWriteArrayList<NewtonTaskListener>();

    @Override
    public void internalMethod02230(BlockPos blockPos) {
        this.internalMethod00530(new BlockPositionGoal(blockPos));
    }

    @Override
    public void internalMethod01013(BlockPos blockPos, boolean bl) {
        if (bl) {
            this.internalMethod03517(blockPos);
        } else {
            this.internalMethod02230(blockPos);
        }
    }

    @Override
    public void internalMethod00530(PathGoal typedValue302) {
        NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new GotoPathTask(typedValue302));
    }

    @Override
    public void internalMethod03517(BlockPos blockPos) {
        NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new ElytraFlightTask(blockPos));
    }

    @Override
    public void internalMethod02016(Identifier identifier) {
        Block block = (Block)Registries.BLOCK.get(identifier);
        NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03441(new BlockQuarryTask(block));
    }

    @Override
    public void internalMethod00136() {
        NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03476();
    }

    @Override
    public boolean internalMethod00137() {
        return NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03477();
    }

    @Override
    public Optional<NewtonTaskHandle> internalMethod06302() {
        return NewtonCoreManager.internalMethod00114().internalMethod06401().internalMethod03684().map(this::internalMethod02658);
    }

    @Override
    public boolean internalMethod06067(String string) {
        CommandRegistry typedValue128 = NewtonCoreManager.internalMethod00114().internalMethod06865();
        return typedValue128.internalMethod04610(typedValue128.internalMethod03606() + " " + string);
    }

    @Override
    public void internalMethod00456(NewtonTaskListener typedValue282) {
        this.internalField0416.add(typedValue282);
    }

    @Override
    public void internalMethod05471(NewtonTaskListener typedValue282) {
        this.internalField0416.remove(typedValue282);
    }

    public void internalMethod07208(NewtonTaskEvent typedValue283) {
        for (NewtonTaskListener typedValue282 : this.internalField0416) {
            try {
                typedValue282.internalMethod01502(typedValue283);
            }
            catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    private NewtonTaskHandle internalMethod02658(final NewtonTask typedValue308) {
        return new NewtonTaskHandle(){

            @Override
            public String internalMethod00915() {
                return typedValue308.internalMethod01129();
            }

            @Override
            public String internalMethod05621() {
                return typedValue308.internalMethod05788();
            }

            @Override
            public boolean internalMethod03344() {
                return typedValue308.internalMethod04090();
            }

            @Override
            public void internalMethod03343() {
                typedValue308.internalMethod04089();
            }

            @Override
            public void internalMethod03349() {
                typedValue308.internalMethod08146();
            }

            @Override
            public void internalMethod08765() {
                typedValue308.internalMethod04086();
            }
        };
    }
}

