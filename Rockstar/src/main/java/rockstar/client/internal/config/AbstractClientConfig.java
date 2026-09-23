package rockstar.client.internal.config;




import rockstar.client.internal.script.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import java.io.File;
import lombok.Generated;
import rockstar.client.internal.script.ConfigManager;
import rockstar.client.internal.core.ConfigFileAnnotation;

public abstract class AbstractClientConfig {
    public final ConfigFileAnnotation internalField0162 = this.getClass().getAnnotation(ConfigFileAnnotation.class);
    public final File internalField0148 = new File(ConfigManager.internalField0148, this.internalField0162.internalMethod03654() + "." + this.internalField0162.internalMethod00190());

    public abstract void internalMethod07509();

    public abstract void internalMethod07512();

    @Generated
    public ConfigFileAnnotation internalMethod04830() {
        return this.internalField0162;
    }

    @Generated
    public File internalMethod05023() {
        return this.internalField0148;
    }
}

