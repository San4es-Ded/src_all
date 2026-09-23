package rockstar.client.internal.core;


import rockstar.client.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.ApiStatus;
import rockstar.client.internal.core.SimpleChangeListener;
import rockstar.client.internal.core.ChangeListener;

public class ChangeListenerRegistry {
    private final List<ChangeListener> internalField0416 = new ArrayList<ChangeListener>();

    public synchronized void internalMethod01208(ChangeListener typedValue084) {
        this.internalField0416.add(typedValue084);
    }

    public synchronized void internalMethod01206(SimpleChangeListener typedValue083) {
        this.internalField0416.add(typedValue083);
    }

    public synchronized boolean internalMethod01209(ChangeListener typedValue084) {
        return this.internalField0416.remove(typedValue084);
    }

    public synchronized boolean internalMethod01207(SimpleChangeListener typedValue083) {
        return this.internalField0416.remove(typedValue083);
    }

    @ApiStatus.Internal
    public synchronized <T> void internalMethod07273(T t, T t2) {
        if (!Objects.equals(t, t2)) {
            for (ChangeListener typedValue084 : this.internalField0416) {
                typedValue084.onChange(t, t2);
            }
        }
    }
}

