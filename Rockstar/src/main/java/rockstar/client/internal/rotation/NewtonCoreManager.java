package rockstar.client.internal.rotation;






import rockstar.client.rotation.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.internal.command.*;
import rockstar.client.*;
import lombok.Generated;
import org.jetbrains.annotations.Nullable;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.client.internal.script.EventBus;
import rockstar.client.RockstarClient;
import rockstar.client.rotation.RotationManager;
import rockstar.client.internal.core.NewtonApiHolder;
import rockstar.client.internal.script.PathCommandService;
import rockstar.client.internal.script.MovementInputController;
import rockstar.client.internal.core.PathExecutor;
import rockstar.client.internal.script.NewtonTaskManager;
import rockstar.client.internal.script.PathRouteRenderer;
import rockstar.client.internal.script.TargetBlockRenderer;
import rockstar.client.internal.script.SelectionBoxRenderer;

public final class NewtonCoreManager {
    private static NewtonCoreManager internalField0774;
    private final MovementInputController internalField0778;
    private final NewtonTaskManager internalField0496;
    private final PathRouteRenderer internalField0497;
    private final TargetBlockRenderer internalField0507;
    private final SelectionBoxRenderer internalField0508;
    private final PathCommandService internalField0772;
    @Nullable
    private PathExecutor internalField0906;

    public static NewtonCoreManager internalMethod00114() {
        if (internalField0774 == null) {
            throw new IllegalStateException("NewtonCore not initialized yet");
        }
        return internalField0774;
    }

    public static boolean internalMethod00010() {
        return internalField0774 != null;
    }

    public void internalMethod01913(@Nullable PathExecutor typedValue294) {
        this.internalField0906 = typedValue294;
    }

    public NewtonCoreManager() {
        internalField0774 = this;
        this.internalField0778 = MovementInputController.internalMethod05429(this);
        this.internalField0496 = NewtonTaskManager.internalMethod03333(this);
        this.internalField0497 = PathRouteRenderer.internalMethod01422(this);
        this.internalField0507 = TargetBlockRenderer.internalMethod04562(this);
        this.internalField0508 = SelectionBoxRenderer.internalMethod05327(this);
        this.internalField0772 = new PathCommandService();
        NewtonApiHolder.internalMethod03916(this.internalField0772);
    }

    public EventBus internalMethod05035() {
        return RockstarClient.getInstance().internalMethod03317();
    }

    public RotationManager internalMethod03241() {
        return RockstarClient.getInstance().internalMethod02368();
    }

    public CommandRegistry internalMethod06865() {
        return RockstarClient.getInstance().internalMethod05348();
    }

    @Generated
    public MovementInputController internalMethod00183() {
        return this.internalField0778;
    }

    @Generated
    public NewtonTaskManager internalMethod06401() {
        return this.internalField0496;
    }

    @Generated
    public PathRouteRenderer internalMethod06402() {
        return this.internalField0497;
    }

    @Generated
    public TargetBlockRenderer internalMethod06456() {
        return this.internalField0507;
    }

    @Generated
    public SelectionBoxRenderer internalMethod06457() {
        return this.internalField0508;
    }

    @Generated
    public PathCommandService internalMethod00112() {
        return this.internalField0772;
    }

    @Nullable
    @Generated
    public PathExecutor internalMethod01484() {
        return this.internalField0906;
    }
}

