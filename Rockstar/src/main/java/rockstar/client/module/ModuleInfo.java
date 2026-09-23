package rockstar.client.module;


import rockstar.client.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import rockstar.client.module.ModuleCategory;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    public String name();

    public ModuleCategory category();

    public int defaultKey() default -1;

    public boolean enabledByDefault() default false;

    public boolean internalMethod08049() default false;

    public boolean internalMethod08050() default false;

    public String internalMethod09633() default "modules.descriptions.no_description";

}
