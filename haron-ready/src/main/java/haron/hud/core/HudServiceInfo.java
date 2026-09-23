package haron.hud.core;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(value=RetentionPolicy.RUNTIME)
public @interface HudServiceInfo {
    public boolean enabledByDefault() default true;
}

