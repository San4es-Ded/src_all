package wtf.wyvern.client.modules.impl.combat;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.entity.player.PlayerEntity;
import wtf.wyvern.Wyvern;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "NoFriendDamage",
   category = Category.COMBAT,
   description = "Отключает возможность наносить урон друзьям"
)
@FastNative
public final class NoFriendDamage extends Module {
   public static final NoFriendDamage INSTANCE = new NoFriendDamage();

   private NoFriendDamage() {
   }

   @EventTarget
   public void onAttack(EventAttack event) {
      Aura aura = Aura.INSTANCE;
      if (event.getTarget() instanceof PlayerEntity player
            && Wyvern.getInstance().getFriendManager().isFriend(player.getGameProfile().getName())
            && !(aura.isEnabled() && aura.getTarget() == event.getTarget())) {
         event.setCancelled(true);
      }
   }
}
