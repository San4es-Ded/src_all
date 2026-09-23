 package su.sacura.util.impl.render.other;
 
 public final class HitColorTintState {
   public static final ThreadLocal<Boolean> SHOULD_TINT = ThreadLocal.withInitial(() -> Boolean.valueOf(false));
 }


