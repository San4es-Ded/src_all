package rockstar.client.rotation;




import rockstar.client.util.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.rotation.*;
import rockstar.modules.combat.*;
import rockstar.modules.movement.*;
import rockstar.modules.visual.*;
import rockstar.modules.player.*;
import rockstar.modules.other.*;

import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.player.InputEvent;
import pyrock.events.player.TraceEvent;
import pyrock.events.render.Render3DEvent;

public class RotationEventHandler implements MinecraftClientAccess {
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue0 -> {
      Rotation localValue1 = RockstarClient.getInstance().internalMethod02368().internalMethod09074() != null
         ? RockstarClient.getInstance().internalMethod02368().internalMethod09074()
         : new Rotation(0.0F, 0.0F);
      RockstarClient.getInstance().internalMethod02368().internalMethod01524();
      if (RockstarClient.getInstance().internalMethod02368().internalMethod09074() != null) {
         Rotation localValue2 = localValue1.internalMethod01178(RockstarClient.getInstance().internalMethod02368().internalMethod09074());
      }
   };
   private final EventListener<Render3DEvent> internalField0158 = localValue1 -> {
      RotationManager localValue2 = RockstarClient.getInstance().internalMethod02368();
      localValue2.internalMethod02727(localValue1.getTickDelta());
      this.internalMethod00786(localValue2);
   };
   private final EventListener<InputEvent> internalField1028 = localValue0 -> {
      AuraModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
      RotationManager localValue2 = RockstarClient.internalField0240.internalMethod02368();
      RotationRequest localValue3 = localValue2.internalMethod07551();
      LivingEntity localValue5 = RockstarClient.getInstance().internalMethod04463().internalMethod04526() instanceof LivingEntity localValue6 ? localValue6 : null;
      if (!localValue2.internalMethod01525() && localValue3 != null) {
         RotationBehavior localValue12 = localValue3.internalMethod01386();
         Rotation localValue7 = localValue2.internalMethod09074();
         if (localValue12.internalMethod08350()) {
            localValue0.setYaw(localValue7.internalMethod00169());
         }

         if (localValue12.internalMethod08352()) {
            localValue0.setYawSmooth(localValue7.internalMethod00169());
         }

         if (localValue12 == RotationBehavior.internalField1423 && localValue5 != null) {
            SpeedModule localValue8 = RockstarClient.getInstance().getModuleManager().getModule(SpeedModule.class);
            if (localValue8.isEnabled() && localValue8.internalMethod06446().isSelected()) {
               Vec3d localValue9 = localValue5.getEntityPos()
                  .add(localValue5.getEntityPos().subtract(new Vec3d(localValue5.lastX, localValue5.lastY, localValue5.lastZ)).multiply(localValue8.internalMethod00142().internalMethod08576()));
               localValue0.setYaw(localValue7.internalMethod00169(), RotationUtils.internalMethod05580(localValue9).internalMethod00169());
            } else {
               localValue0.setYaw(localValue7.internalMethod00169(), RotationUtils.internalMethod05580(localValue5.getEntityPos()).internalMethod00169());
            }

            if (internalField0149.player.isSwimming()) {
               if (localValue5.getY() > internalField0149.player.getY()) {
                  localValue0.setJump(true);
                  localValue0.setSneak(false);
               } else {
                  localValue0.setSneak(true);
                  localValue0.setJump(false);
               }
            }
         }
      }

      if (!localValue2.internalMethod01525()
         && localValue3 != null
         && localValue3.internalMethod01386() == RotationBehavior.internalField1423
         && localValue1.isEnabled()
         && localValue5 != null) {
         if (localValue1.internalMethod09280() && localValue1.internalMethod02517().internalMethod04496()) {
            double localValue13 = Math.toDegrees(Math.atan2(internalField0149.player.getZ() - localValue5.getZ(), internalField0149.player.getX() - localValue5.getX()));
            float localValue14 = localValue5.getYaw() + 180.0F;
            float localValue15 = MathHelper.wrapDegrees((float)(localValue13 - localValue14));
            float localValue10 = MathHelper.clamp(-localValue15 / 90.0F, -1.0F, 1.0F);
            float localValue11 = Math.abs(localValue15) < 15.0F ? 0.6F : 1.0F;
            localValue0.setForward(localValue11);
            localValue0.setStrafe(localValue10);
         }

         localValue0.setSprint(true);
      }

      if (localValue1.internalMethod01895().internalMethod07418() instanceof SpookyTimeRotation
         && localValue1.internalMethod09757()
         && localValue5 != null
         && !internalField0149.player.isOnGround()
         && PlayerFallSimulator.internalMethod02421(internalField0149.player).internalMethod04604(CombatUtils.internalMethod03105(localValue5), 2)
         && localValue1.internalMethod04187(localValue5)
         && internalField0149.player.fallDistance < 0.4F) {
         localValue0.setForward(0.0F);
         localValue0.setStrafe(0.0F);
      }
   };
   private final EventListener<ReceivePacketEvent> internalField1029 = localValue0 -> {
      if (localValue0.getPacket() instanceof PlayerPositionLookS2CPacket localValue2) {
         RotationManager localValue3 = RockstarClient.getInstance().internalMethod02368();
         Rotation localValue4 = localValue3.internalMethod09074();
         float localValue5 = localValue2.change().yaw();
         if (localValue4 != null) {
            localValue5 = localValue4.internalMethod00169() + MathHelper.wrapDegrees(localValue5 - localValue4.internalMethod00169());
         }

         localValue3.internalMethod01570(new Rotation(localValue5, localValue2.change().pitch()));
      }
   };
   private final EventListener<TraceEvent> internalField1030 = localValue0 -> {
      RotationManager localValue1 = RockstarClient.internalField0240.internalMethod02368();
      RotationRequest localValue2 = localValue1.internalMethod07551();
      if (!localValue1.internalMethod01525() && localValue2 != null) {
         localValue0.setYaw(localValue1.internalMethod09074().internalMethod00169());
         localValue0.setPitch(localValue1.internalMethod09074().internalMethod00171());
         localValue0.cancel();
      }
   };
   private final EventListener<ReceivePacketEvent> internalField1027 = localValue0 -> {};

   public RotationEventHandler() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
   }

   private void internalMethod00786(RotationManager localValue1) {
      if (internalField0149.player != null && !localValue1.internalMethod01525()) {
         RotationRequest localValue2 = localValue1.internalMethod07551();
         if (localValue2 != null && localValue2.internalMethod01386().internalMethod08363()) {
            Rotation localValue3 = localValue1.internalMethod08582();
            RotationManager.internalMethod02140(localValue3.internalMethod00169(), localValue3.internalMethod00171());
         }
      }
   }
}
