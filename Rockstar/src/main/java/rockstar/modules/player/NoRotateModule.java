package rockstar.modules.player;



import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.EnumSet;
import java.util.Set;
import moscow.rockstar.mixin.accessors.PlayerPositionLookS2CPacketAccessor;
import moscow.rockstar.mixin.accessors.PlayerRotationS2CPacketAccessor;
import net.minecraft.entity.EntityPosition;
import net.minecraft.network.packet.s2c.play.LookAtS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRotationS2CPacket;
import net.minecraft.network.packet.s2c.play.PositionFlag;
import pyrock.events.network.ReceivePacketEvent;

@ModuleInfo(
   name = "No Rotate",
   category = ModuleCategory.PLAYER,
   internalMethod09633 = "modules.descriptions.no_rotate"
)
public class NoRotateModule extends Module {
   private final EventListener<ReceivePacketEvent> internalField0157 = new EventListener<ReceivePacketEvent>() {
      public void onEvent(ReceivePacketEvent localValue1) {
         if (MinecraftClientAccess.internalField0149.player != null && MinecraftClientAccess.internalField0149.world != null) {
            if (localValue1.getPacket() instanceof PlayerPositionLookS2CPacket localValue2) {
               NoRotateModule.this.internalMethod07413(localValue2);
            } else if (localValue1.getPacket() instanceof PlayerRotationS2CPacket localValue3) {
               NoRotateModule.this.internalMethod07107(localValue3);
            } else if (localValue1.getPacket() instanceof LookAtS2CPacket) {
               localValue1.cancel();
            }
         }
      }

      @Override
      public int internalMethod07175() {
         return 100;
      }
   };

   void internalMethod07413(PlayerPositionLookS2CPacket localValue1) {
      EntityPosition localValue2 = localValue1.change();
      PlayerPositionLookS2CPacketAccessor localValue3 = (PlayerPositionLookS2CPacketAccessor)(Object)localValue1;
      localValue3.setChange(new EntityPosition(localValue2.position(), localValue2.deltaMovement(), internalField0149.player.getYaw(), internalField0149.player.getPitch()));
      localValue3.setRelatives(this.internalMethod00139(localValue1.relatives()));
   }

   void internalMethod07107(PlayerRotationS2CPacket localValue1) {
      PlayerRotationS2CPacketAccessor localValue2 = (PlayerRotationS2CPacketAccessor)(Object)localValue1;
      localValue2.setYaw(internalField0149.player.getYaw());
      localValue2.setPitch(internalField0149.player.getPitch());
   }

   private Set<PositionFlag> internalMethod00139(Set<PositionFlag> localValue1) {
      EnumSet localValue2 = localValue1.isEmpty() ? EnumSet.noneOf(PositionFlag.class) : EnumSet.copyOf(localValue1);
      localValue2.remove(PositionFlag.Y_ROT);
      localValue2.remove(PositionFlag.X_ROT);
      return localValue2;
   }
}
