 package su.sacura.util.impl.render.builders;
 
 public abstract class AbstractBuilder<T> {
   public AbstractBuilder() {
     reset();
   }
   
   public final T build() {
     T instance = _build();
     reset();
     return instance;
   }
   
   protected abstract void reset();
   
   protected abstract T _build();
 }


