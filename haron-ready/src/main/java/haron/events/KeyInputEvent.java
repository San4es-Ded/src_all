package haron.events;

public class KeyInputEvent {
    private final int keyCode;
    private final int scanCode;
    private final int action;
    private final int modifiers;

    public int keyCode() {
        int n = 295;
        return this.keyCode;
    }

    public int scanCode() {
        return this.scanCode;
    }

    public int modifiers() {
        return this.modifiers;
    }

    public KeyInputEvent(int n, int n2, int n3, int n4) {
        this.keyCode = n;
        this.scanCode = n2;
        this.action = n3;
        this.modifiers = n4;
    }

    public int b() {
        return this.scanCode;
    }

    public int c() {
        return this.action;
    }

    public int d() {
        return this.modifiers;
    }

    public int a() {
        return this.keyCode;
    }

    public int action() {
        return this.action;
    }
}

