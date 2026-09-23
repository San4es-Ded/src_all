package rockstar.client.internal.script;






import rockstar.client.rotation.*;
import rockstar.client.event.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.util.Optional;
import org.jetbrains.annotations.Nullable;
import pyrock.events.player.ClientPlayerTickEvent;
import rockstar.client.event.EventListener;
import rockstar.client.internal.rotation.NewtonCoreManager;
import rockstar.client.internal.script.NewtonEventDispatcher;
import rockstar.client.internal.core.NewtonTask;

public final class NewtonTaskManager {
    @Nullable
    private NewtonTask internalField0495;
    private final EventListener<ClientPlayerTickEvent> internalField0157 = clientPlayerTickEvent -> {
        if (this.internalField0495 == null) {
            return;
        }
        try {
            NewtonTask typedValue308 = this.internalField0495;
            if (!typedValue308.internalMethod04087()) {
                return;
            }
            if (this.internalField0495 == typedValue308) {
                this.internalField0495 = null;
            }
            if (typedValue308.internalMethod08147()) {
                NewtonEventDispatcher.internalMethod06127(typedValue308.internalMethod01129());
            } else {
                String string = typedValue308.internalMethod09095();
                NewtonEventDispatcher.internalMethod04499(typedValue308.internalMethod01129(), string == null ? "\u043f\u0440\u0435\u0440\u0432\u0430\u043d" : string);
            }
        }
        catch (Throwable throwable) {
            throwable.printStackTrace();
            this.internalMethod03476();
        }
    };

    public void internalMethod03441(NewtonTask typedValue308) {
        this.internalMethod03476();
        this.internalField0495 = typedValue308;
        NewtonEventDispatcher.internalMethod07616(typedValue308.internalMethod01129());
    }

    public void internalMethod03476() {
        if (this.internalField0495 == null) {
            return;
        }
        NewtonTask typedValue308 = this.internalField0495;
        this.internalField0495 = null;
        try {
            typedValue308.internalMethod04086();
        }
        catch (Throwable throwable) {
            // empty catch block
        }
        NewtonEventDispatcher.internalMethod04499(typedValue308.internalMethod01129(), "\u043e\u0442\u043c\u0435\u043d\u0451\u043d");
    }

    public Optional<NewtonTask> internalMethod03684() {
        return Optional.ofNullable(this.internalField0495);
    }

    public boolean internalMethod03477() {
        return this.internalField0495 != null;
    }

    public static NewtonTaskManager internalMethod03333(NewtonCoreManager typedValue289) {
        NewtonTaskManager typedValue309 = new NewtonTaskManager();
        typedValue289.internalMethod05035().internalMethod00647(typedValue309);
        return typedValue309;
    }
}

