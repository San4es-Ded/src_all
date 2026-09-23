package rockstar.modules.other;






import rockstar.client.setting.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.internal.framework.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import lombok.Generated;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import pyrock.events.game.EntityDeathEvent;
import pyrock.events.network.ReceivePacketEvent;

@ModuleInfo(
   name = "Sounds",
   category = ModuleCategory.OTHER,
   internalMethod08049 = true,
   internalMethod09633 = "modules.descriptions.sounds"
)
public class SoundsModule extends Module {
   private SliderSetting internalField0383;
   private MultiSelectSetting internalField0675;
   private MultiSelectSetting.InternalType0091 internalField0245;
   private MultiSelectSetting.InternalType0091 internalField0244;
   private MultiSelectSetting.InternalType0091 internalField1075;
   private MultiSelectSetting.InternalType0091 internalField1074;
   private MultiSelectSetting.InternalType0091 internalField1073;
   private LivingEntity internalField0505;
   private final EventListener<ReceivePacketEvent> internalField0157 = localValue1 -> {
      if (localValue1.getPacket() instanceof GameMessageS2CPacket localValue2) {
         String localValue5 = localValue2.content().getString();
         if (localValue5.contains("\u0412\u044b \u0443\u0441\u043f\u0435\u0448\u043d\u043e \u043a\u0443\u043f\u0438\u043b\u0438")
            || localValue5.contains("\u043e\u0442\u043f\u0440\u0430\u0432\u043b\u0435\u043d\u043e \u0438\u0433\u0440\u043e\u043a\u0443")) {
            ClientSoundEvents.internalField1015.internalMethod03864(this.internalMethod01798());
         }
      }

      if (localValue1.getPacket() instanceof EntityStatusS2CPacket localValue4 && internalField0149.world != null) {
         Entity localValue7 = localValue4.getEntity(internalField0149.world);
         if (localValue7 != null && localValue4.getStatus() == 35 && internalField0149.player.distanceTo(localValue7) < 6.0F && this.internalField0244.isSelected()) {
            RandomSoundHelper.internalMethod08441();
         }
      }
   };
   private final EventListener<EntityDeathEvent> internalField0158 = localValue1 -> {
      if (internalField0149.player == localValue1.getEntity() && this.internalField1073.isSelected()) {
         RandomSoundHelper.internalMethod07535();
         this.internalField0505 = null;
      }
   };

   public SoundsModule() {
      this.internalMethod09466();
   }

   private void internalMethod09466() {
      this.internalField0383 = new SliderSetting(this, "modules.settings.sounds.volume")
         .internalMethod08673(5.0F)
         .internalMethod05900(10.0F)
         .internalMethod02732(100.0F)
         .internalMethod08074(80.0F)
         .internalMethod06240("%");
      this.internalField0675 = new MultiSelectSetting(this, "modules.settings.sounds.voice");
      this.internalField0245 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sounds.voice.kill");
      this.internalField0244 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sounds.voice.totem");
      this.internalField1075 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sounds.voice.start");
      this.internalField1074 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sounds.voice.leave");
      this.internalField1073 = new MultiSelectSetting.InternalType0091(this.internalField0675, "modules.settings.sounds.voice.death");
   }

   public float internalMethod01798() {
      return this.internalField0383.internalMethod08576() / 100.0F;
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && !internalField0149.player.isDead()) {
         if (this.internalField0505 != null) {
            if (internalField0149.player.distanceTo(this.internalField0505) > 6.0F) {
               this.internalField0505 = null;
               return;
            }

            if ((!this.internalField0505.isAlive() || !internalField0149.world.hasEntity(this.internalField0505)) && this.internalField0245.isSelected()) {
               RandomSoundHelper.internalMethod08440();
               this.internalField0505 = null;
            }
         }

         LivingEntity localValue2 = RockstarClient.getInstance().internalMethod04463().internalMethod04526() instanceof LivingEntity localValue3 ? localValue3 : null;
         if (localValue2 != null) {
            this.internalField0505 = localValue2;
         }
      } else {
         this.internalField0505 = null;
      }
   }

   @Generated
   public MultiSelectSetting internalMethod01679() {
      return this.internalField0675;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod07476() {
      return this.internalField0245;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod02854() {
      return this.internalField0244;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod08081() {
      return this.internalField1075;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod08121() {
      return this.internalField1074;
   }

   @Generated
   public MultiSelectSetting.InternalType0091 internalMethod08546() {
      return this.internalField1073;
   }

   @Generated
   public LivingEntity internalMethod06005() {
      return this.internalField0505;
   }

   @Generated
   public EventListener<ReceivePacketEvent> internalMethod06629() {
      return this.internalField0157;
   }

   @Generated
   public EventListener<EntityDeathEvent> internalMethod03990() {
      return this.internalField0158;
   }
}
