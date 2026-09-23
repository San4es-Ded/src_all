package pyrock.events.window;



import rockstar.client.util.*;
import rockstar.client.event.*;
import lombok.Generated;
import pyrock.events.EventCancellable;
import rockstar.client.event.EventName;
import rockstar.client.util.TextUtils;
import rockstar.client.MinecraftClientAccess;

@EventName(internalMethod03601="key")
public class KeyEvent
extends EventCancellable
implements MinecraftClientAccess {
    private final int key;
    private final int scancode;
    private final int action;
    private final int mods;

    public KeyEvent(int n, int n2, int n3, int n4) {
        this.key = n;
        this.scancode = n2;
        this.action = n3;
        this.mods = n4;
    }

    public String getName() {
        return TextUtils.internalMethod04982(this.key);
    }

    public boolean isPress() {
        return this.action == 1;
    }

    public boolean isRelease() {
        return this.action == 0;
    }

    public boolean isRepeat() {
        return this.action == 2;
    }

    public boolean isShift() {
        return (this.mods & 1) != 0;
    }

    public boolean isCtrl() {
        return (this.mods & 2) != 0;
    }

    public boolean isAlt() {
        return (this.mods & 4) != 0;
    }

    public boolean isScreenOpen() {
        return KeyEvent.internalField0149.currentScreen != null;
    }

    @Generated
    public int getKey() {
        return this.key;
    }

    @Generated
    public int getScancode() {
        return this.scancode;
    }

    @Generated
    public int getAction() {
        return this.action;
    }

    @Generated
    public int getMods() {
        return this.mods;
    }
}

