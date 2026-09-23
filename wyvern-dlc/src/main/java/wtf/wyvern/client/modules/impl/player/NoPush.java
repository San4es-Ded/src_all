package wtf.wyvern.client.modules.impl.player;

import java.util.List;
import lombok.Generated;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.MultiBooleanSetting;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import wtf.wyvern.core.eventbus.EventTarget;
import wtf.wyvern.core.events.impl.server.EventPacket;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "NoPush",
   description = "Убирает отталкивание от обьектов",
   category = Category.PLAYER
)
@FastNative
public class NoPush extends Module {
   public static final NoPush INSTANCE = new NoPush();
   private final MultiBooleanSetting objects = MultiBooleanSetting.create("Объекты", List.of("Игроки", "Блоки", "Вода", "Удочки"));

   @EventTarget
   private void onPacket(EventPacket event) {
      if (event.isReceive() && mc.player != null && objects.isEnable("Урон")
              && event.getPacket() instanceof EntityVelocityUpdateS2CPacket packet
              && packet.getEntityId() == mc.player.getId()) {
         event.cancel();
      }
   }

   @Generated
   public boolean equals(Object o) {
      if (o == this) {
         return true;
      } else if (!(o instanceof NoPush)) {
         return false;
      } else {
         NoPush other = (NoPush)o;
         if (!other.canEqual(this)) {
            return false;
         } else if (!super.equals(o)) {
            return false;
         } else {
            Object this$objects = this.getObjects();
            Object other$objects = other.getObjects();
            if (this$objects == null) {
               if (other$objects != null) {
                  return false;
               }
            } else if (!this$objects.equals(other$objects)) {
               return false;
            }

            return true;
         }
      }
   }

   @Generated
   protected boolean canEqual(Object other) {
      return other instanceof NoPush;
   }

   @Generated
   public int hashCode() {
      int PRIME = 1;
      int result = super.hashCode();
      Object $objects = this.getObjects();
      result = result * 59 + ($objects == null ? 43 : $objects.hashCode());
      return result;
   }

   @Generated
   public MultiBooleanSetting getObjects() {
      return this.objects;
   }

   @Generated
   public String toString() {
      return "NoPush(objects=" + String.valueOf(this.getObjects()) + ")";
   }
}
