package su.sacura.features.modules.api.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import su.sacura.features.modules.impl.Category;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ModuleAnnotations {
  String name();
  
  Category category();
  
  String desc() default "У данного модуля нету описания.";
}


