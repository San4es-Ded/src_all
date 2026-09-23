package wtf.wyvern.client.modules.impl.player;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.other.EventTick;
import wtf.wyvern.core.events.impl.player.EventAttack;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(name = "AntiThorns", category = Category.PLAYER, description = "Убирает отдачу от шипов на элитре")
@FastNative
public final class AntiThorns extends Module {
   public static final AntiThorns INSTANCE = new AntiThorns();
   private static final int THORNS_STATUS = 33;
   private static final int SUPPRESS_TICKS = 20;
   private int suppressVelocityTicks;
   private Vec3d glideVelocity;

   @EventTarget
   public void onAttack(EventAttack event) {
      if (mc.player != null && mc.player.isGliding()) {
         suppressVelocityTicks = SUPPRESS_TICKS;
         glideVelocity = mc.player.getVelocity();
      }
   }

   @EventTarget
   public void onPacket(EventPacket event) {
      if (!event.isReceive() || mc.player == null || mc.world == null) return;
      if (event.getPacket() instanceof EntityStatusS2CPacket packet) {
         if (packet.getStatus() == THORNS_STATUS && suppressVelocityTicks > 0 && mc.player.isGliding()) {
            suppressVelocityTicks = SUPPRESS_TICKS;
         }
      } else if (event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet
         && suppressVelocityTicks > 0 && mc.player.isGliding() && packet.getEntityId() == mc.player.getId()) {
         event.cancel();
         if (glideVelocity != null) mc.player.setVelocity(glideVelocity);
      }
   }

   @EventTarget
   public void onTick(EventTick event) {
      if (mc.player == null || !mc.player.isGliding()) {
         suppressVelocityTicks = 0;
         glideVelocity = null;
      } else if (suppressVelocityTicks > 0) {
         // Some servers apply the recoil through a local hurt update after the
         // velocity packet. Re-assert the pre-hit glide vector during that frame.
         if (mc.player.hurtTime > 0 && glideVelocity != null) {
            mc.player.setVelocity(glideVelocity);
         }
         --suppressVelocityTicks;
         if (suppressVelocityTicks == 0) glideVelocity = null;
      }
   }

   @Override
   public void onEnable() {
      suppressVelocityTicks = 0;
      glideVelocity = null;
      super.onEnable();
   }

   @Override
   public void onDisable() {
      suppressVelocityTicks = 0;
      glideVelocity = null;
      super.onDisable();
   }
}
