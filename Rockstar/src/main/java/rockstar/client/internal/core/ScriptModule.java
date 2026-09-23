package rockstar.client.internal.core;



import rockstar.client.module.*;
import rockstar.client.*;
import lombok.Generated;
import rockstar.client.module.ModuleCategory;
import rockstar.client.module.Module;

public class ScriptModule
extends Module {
    private String internalField0248 = "";

    public ScriptModule(String string, ModuleCategory typedValue150, int n) {
        super(string, typedValue150, n);
    }

    @Generated
    public void internalMethod05500(String string) {
        this.internalField0248 = string;
    }

    @Override
    @Generated
    public String internalMethod05655() {
        return this.internalField0248;
    }
}

