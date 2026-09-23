 package su.sacura.util.impl.render.other;
 
 import net.minecraft.entity.LivingEntity;
 
 public class FriendRenderContext {
   public static final ThreadLocal<LivingEntity> CURRENT = new ThreadLocal<>();
 }


