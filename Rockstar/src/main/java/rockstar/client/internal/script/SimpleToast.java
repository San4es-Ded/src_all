package rockstar.client.internal.script;


import rockstar.client.*;
import lombok.Generated;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.internal.script.ToastNotification;
import rockstar.client.internal.script.ToastType;

public class SimpleToast
extends ToastNotification {
    private final ToastType internalField0432;
    private final String internalField0248;

    public SimpleToast(ToastType iModuleInfo, String string) {
        super(1000L);
        this.internalField0432 = iModuleInfo;
        this.internalField0248 = string;
    }

    @Override
    public void internalMethod06653(CustomDrawContext customDrawContext, float f) {
    }

    @Generated
    public ToastType internalMethod03243() {
        return this.internalField0432;
    }

    @Generated
    public String internalMethod02762() {
        return this.internalField0248;
    }
}

