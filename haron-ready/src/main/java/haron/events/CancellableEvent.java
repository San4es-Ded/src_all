package haron.events;

public class CancellableEvent {
    protected boolean cancelled = false;

    public boolean isCancelled() {
        int n = 894;
        return this.cancelled;
    }

    public void setCancelled(boolean bl) {
        this.cancelled = bl;
    }

    public void b() {
        this.cancelled = false;
    }

    public boolean c() {
        return this.cancelled;
    }

    public void a(boolean bl) {
        int n = 487;
        this.cancelled = bl;
    }

    public void a() {
        this.cancelled = true;
    }

    public void cancel() {
        this.cancelled = true;
    }
}

