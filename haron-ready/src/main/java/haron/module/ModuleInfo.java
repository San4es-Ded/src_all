package haron.module;

import haron.module.ModuleCategory;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface ModuleInfo {
    public String b();

    public ModuleCategory c();

    public String a();
}

