package rockstar.client.internal.game;



import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.Optional;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import rockstar.client.internal.core.NewtonTaskHandle;
import rockstar.client.internal.core.NewtonTaskListener;
import rockstar.client.internal.game.PathGoal;

public interface PathCommandApi {
    public void internalMethod02230(BlockPos localValue1);

    public void internalMethod01013(BlockPos localValue1, boolean localValue2);

    public void internalMethod00530(PathGoal localValue1);

    public void internalMethod03517(BlockPos localValue1);

    public void internalMethod02016(Identifier localValue1);

    public void internalMethod00136();

    public boolean internalMethod00137();

    public Optional<NewtonTaskHandle> internalMethod06302();

    public boolean internalMethod06067(String localValue1);

    public void internalMethod00456(NewtonTaskListener localValue1);

    public void internalMethod05471(NewtonTaskListener localValue1);
}

