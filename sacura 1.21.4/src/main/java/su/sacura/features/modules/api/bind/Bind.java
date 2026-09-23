package su.sacura.features.modules.api.bind;

public class Bind {
    private int key;
    private BindMode mode;

    public Bind(int key, BindMode mode) {
        this.key = key;
        this.mode = mode;
    }

    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
    }

    public BindMode getMode() {
        return this.mode;
    }

    public void setMode(BindMode mode) {
        this.mode = mode;
    }

    public boolean isNone() {
        return this.key == -1;
    }

    public static enum BindMode {
        TOGGLE,
        HOLD;

    }
}
