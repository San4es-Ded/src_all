package rockstar.client.internal.core;


import rockstar.client.*;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface ConfigFileAnnotation {
    public String internalMethod03654();

    public String internalMethod00190() default "rock";
}

