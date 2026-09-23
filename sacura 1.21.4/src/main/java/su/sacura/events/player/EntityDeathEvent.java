 package su.sacura.events.player;
 
 import net.minecraft.entity.Entity;
 import net.minecraft.entity.damage.DamageSource;
 
 public class EntityDeathEvent {
   private final Entity entity;
   
   private final DamageSource source;
   
   public Entity getEntity() {
     return this.entity;
   }
   
   public DamageSource getSource() {
     return this.source;
   }
   
   public EntityDeathEvent(Entity entity, DamageSource source) {
     this.entity = entity;
     this.source = source;
   }
 }


