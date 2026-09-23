package rockstar.modules.combat;




import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import lombok.Generated;
import moscow.rockstar.mixin.accessors.EntityVelocityUpdateAccessor;
import net.minecraft.network.packet.Packet;
import net.minecraft.network.packet.s2c.play.EntityVelocityUpdateS2CPacket;
import net.minecraft.network.packet.s2c.play.ExplosionS2CPacket;
import net.minecraft.util.math.Vec3d;
import pyrock.events.network.ReceivePacketEvent;

@ModuleInfo(
   name = "Velocity",
   category = ModuleCategory.COMBAT,
   internalMethod09633 = "modules.descriptions.velocity"
)
public class VelocityModule extends Module {
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private SliderSetting internalField0383;
   private SliderSetting internalField0382;
   private SliderSetting internalField1142;
   private SliderSetting internalField1140;
   private final VelocityModule.InternalType0412 internalField0289 = new VelocityModule.InternalType0412();
   private final EventListener<ReceivePacketEvent> internalField0157 = localValue1 -> {
      if (internalField0149.player != null && !internalField0149.player.isDead()) {
         Packet localValue2 = localValue1.getPacket();
         boolean localValue3 = localValue2 instanceof EntityVelocityUpdateS2CPacket localValue4 && localValue4.getEntityId() == internalField0149.player.getId();
         boolean localValue11 = localValue2 instanceof ExplosionS2CPacket;
         if (this.internalField0668.internalMethod06103(this.internalField1066)) {
            if (localValue2 instanceof EntityVelocityUpdateS2CPacket localValue13 && localValue13.getEntityId() == internalField0149.player.getId()) {
               this.internalField0289.internalMethod05072(this.internalField0289.internalMethod02826() + 1);
               int localValue15 = Math.max(1, (int)this.internalField1140.internalMethod08576());
               if (this.internalField0289.internalMethod02826() > localValue15) {
                  localValue1.cancel();
                  this.internalField0289.internalMethod05072(0);
               }
            }
         } else {
            if (localValue3 || localValue11) {
               if (this.internalField0668.internalMethod06103(this.internalField0237)
                  && localValue2 instanceof EntityVelocityUpdateS2CPacket localValue5
                  && localValue5.getEntityId() == internalField0149.player.getId()) {
                  localValue1.cancel();
               } else if (this.internalField0668.internalMethod06103(this.internalField1067)
                  && localValue2 instanceof EntityVelocityUpdateS2CPacket localValue6
                  && localValue6.getEntityId() == internalField0149.player.getId()) {
                  Vec3d localValue7 = localValue6.getVelocity();
                  Vec3d localValue8 = new Vec3d(
                     localValue7.x * this.internalField0383.internalMethod08576() / 100.0,
                     localValue7.y * this.internalField0382.internalMethod08576() / 100.0,
                     localValue7.z * this.internalField1142.internalMethod08576() / 100.0
                  );
                  EntityVelocityUpdateAccessor localValue10 = (EntityVelocityUpdateAccessor)(Object)localValue6;
                  localValue10.setVelocity(localValue8);
               }
            }

            if (this.internalField0238.isSelected()
               && localValue1.getPacket() instanceof EntityVelocityUpdateS2CPacket localValue12
               && localValue12.getEntityId() == internalField0149.player.getId()) {
               this.internalField0289.internalField0283 = localValue12.getVelocity();
               this.internalField0289.internalField0227 = 4 + internalField0149.player.getRandom().nextInt(4);
            }
         }
      }
   };

   public VelocityModule() {
      this.internalMethod09503();
   }

   private void internalMethod09503() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.velocity.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.velocity.default");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.velocity.compensation");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "Grim");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.velocity.modify");
      this.internalField0383 = new SliderSetting(
            this, "modules.settings.velocity.velocity_x", () -> !this.internalField0668.internalMethod06103(this.internalField1067)
         )
         .internalMethod06240("%")
         .internalMethod08074(50.0F)
         .internalMethod05900(0.0F)
         .internalMethod02732(100.0F)
         .internalMethod08673(1.0F);
      this.internalField0382 = new SliderSetting(
            this, "modules.settings.velocity.velocity_y", () -> !this.internalField0668.internalMethod06103(this.internalField1067)
         )
         .internalMethod06240("%")
         .internalMethod08074(50.0F)
         .internalMethod05900(0.0F)
         .internalMethod02732(100.0F)
         .internalMethod08673(1.0F);
      this.internalField1142 = new SliderSetting(
            this, "modules.settings.velocity.velocity_z", () -> !this.internalField0668.internalMethod06103(this.internalField1067)
         )
         .internalMethod06240("%")
         .internalMethod08074(50.0F)
         .internalMethod05900(0.0F)
         .internalMethod02732(100.0F)
         .internalMethod08673(1.0F);
      this.internalField1140 = new SliderSetting(
            this, "modules.settings.velocity.grim_hits_before_cancel", () -> !this.internalField0668.internalMethod06103(this.internalField1066)
         )
         .internalMethod05900(1.0F)
         .internalMethod02732(10.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(3.0F);
   }

   @Override
   public final void internalMethod08229() {
      if (internalField0149.player == null || internalField0149.player.isDead()) {
         this.internalField0289.internalMethod02824();
      } else if (this.internalField0238.isSelected() && this.internalField0289.internalField0227 > 0) {
         Vec3d localValue1 = internalField0149.player.getVelocity();
         double localValue2 = this.internalField0289.internalField0227 / 6.0;
         double localValue4 = 0.3 + (1.0 - localValue2) * 0.35;
         localValue4 += internalField0149.player.getRandom().nextDouble() * 0.05;
         Vec3d localValue6 = new Vec3d(-this.internalField0289.internalField0283.x * localValue4, 0.0, -this.internalField0289.internalField0283.z * localValue4);
         Vec3d localValue7 = localValue1.add(localValue6);
         internalField0149.player.setVelocity(localValue7);
         this.internalField0289.internalField0227--;
         super.internalMethod08229();
      }
   }

   @Override
   public void onDisable() {
      this.internalField0289.internalMethod02824();
   }

   static class InternalType0412 {
      Vec3d internalField0283 = Vec3d.ZERO;
      int internalField0227;
      private int internalField0228;

      void internalMethod02824() {
         this.internalField0283 = Vec3d.ZERO;
         this.internalField0227 = 0;
         this.internalField0228 = 0;
      }

      @Generated
      public Vec3d internalMethod01065() {
         return this.internalField0283;
      }

      @Generated
      public int internalMethod02823() {
         return this.internalField0227;
      }

      @Generated
      public int internalMethod02826() {
         return this.internalField0228;
      }

      @Generated
      public void internalMethod05913(Vec3d localValue1) {
         this.internalField0283 = localValue1;
      }

      @Generated
      public void internalMethod05037(int localValue1) {
         this.internalField0227 = localValue1;
      }

      @Generated
      public void internalMethod05072(int localValue1) {
         this.internalField0228 = localValue1;
      }
   }
}
