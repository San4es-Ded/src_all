package rockstar.modules.combat;





import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import net.minecraft.entity.Entity;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.c2s.play.PlayerMoveC2SPacket.LookAndOnGround;
import net.minecraft.util.Hand;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.InternalAttackEvent;

@ModuleInfo(
   name = "Knockback Tweaks",
   category = ModuleCategory.COMBAT,
   internalMethod09633 = "modules.descriptions.knockback_tweaks"
)
public class KnockbackTweaksModule extends Module {
   BooleanSetting internalField0650;
   private boolean internalField0277;
   boolean internalField0276;
   private Entity internalField0410;
   private int internalField0227;
   boolean internalField1099;
   private final EventListener<InternalAttackEvent> internalField0157 = new EventListener<InternalAttackEvent>() {
      public void onEvent(InternalAttackEvent localValue1) {
         if (MinecraftClientAccess.internalField0149.player != null && MinecraftClientAccess.internalField0149.world != null && localValue1.getEntity() != null) {
            if (!localValue1.isCancelled()) {
               if (KnockbackTweaksModule.this.internalField0650.internalMethod04496()) {
                  if (MinecraftClientAccess.internalField0149.player.isSprinting()) {
                     MinecraftClientAccess.internalField0149
                        .player
                        .networkHandler
                        .sendPacket(new ClientCommandC2SPacket(MinecraftClientAccess.internalField0149.player, Mode.STOP_SPRINTING));
                  }

                  MinecraftClientAccess.internalField0149
                     .player
                     .networkHandler
                     .sendPacket(new ClientCommandC2SPacket(MinecraftClientAccess.internalField0149.player, Mode.START_SPRINTING));
                  if (MinecraftClientAccess.internalField0149.player.isSprinting()) {
                     KnockbackTweaksModule.this.internalField0276 = true;
                  }
               } else {
                  boolean localValue2 = MinecraftClientAccess.internalField0149.player.isSprinting();
                  KnockbackTweaksModule.this.internalMethod09902();
                  if (!KnockbackTweaksModule.this.internalField1099 && !localValue2 && KnockbackTweaksModule.this.internalMethod09901()) {
                     KnockbackTweaksModule.this.internalMethod01235(localValue1.getEntity());
                     localValue1.cancel();
                     return;
                  }

                  if (!MinecraftClientAccess.internalField0149.player.isSprinting()) {
                     return;
                  }
               }

               KnockbackTweaksModule.this.internalMethod03336(localValue1.getEntity());
            }
         }
      }

      @Override
      public int internalMethod07175() {
         return -100;
      }
   };

   public KnockbackTweaksModule() {
      this.internalMethod09900();
   }

   private void internalMethod09900() {
      this.internalField0650 = new BooleanSetting(this, "modules.settings.knockback_tweaks.fake_sprint");
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null) {
         if (!this.internalField0650.internalMethod04496()) {
            this.internalMethod09902();
            this.internalMethod09247();
         }

         if (this.internalField0277) {
            this.internalField0277 = false;
            Rotation localValue1 = this.internalMethod01629();
            internalField0149.player
               .networkHandler
               .sendPacket(
                  new LookAndOnGround(
                     localValue1.internalMethod00169(), localValue1.internalMethod00171(), internalField0149.player.isOnGround(), internalField0149.player.horizontalCollision
                  )
               );
         }

         if (this.internalField0276) {
            this.internalField0276 = false;
            internalField0149.player.networkHandler.sendPacket(new ClientCommandC2SPacket(internalField0149.player, Mode.START_SPRINTING));
         }
      }
   }

   @Override
   public void onDisable() {
      this.internalField0277 = false;
      this.internalField0276 = false;
      this.internalField0410 = null;
      this.internalField0227 = 0;
      this.internalField1099 = false;
   }

   void internalMethod03336(Entity localValue1) {
      Rotation localValue2 = this.internalMethod02527(localValue1);
      internalField0149.player
         .networkHandler
         .sendPacket(
            new LookAndOnGround(
               localValue2.internalMethod00169(), localValue2.internalMethod00171(), internalField0149.player.isOnGround(), internalField0149.player.horizontalCollision
            )
         );
      this.internalField0277 = true;
   }

   private Rotation internalMethod02527(Entity localValue1) {
      Vec3d localValue2 = localValue1.getEntityPos().subtract(internalField0149.player.getEntityPos());
      float localValue3 = MathHelper.wrapDegrees((float)Math.toDegrees(Math.atan2(localValue2.z, localValue2.x)) + 90.0F);
      return new Rotation(localValue3, this.internalMethod01629().internalMethod00171());
   }

   private Rotation internalMethod01629() {
      RotationManager localValue1 = RockstarClient.getInstance().internalMethod02368();
      return localValue1.internalMethod01525() ? localValue1.internalMethod07496() : localValue1.internalMethod09074();
   }

   void internalMethod09902() {
      internalField0149.options.sprintKey.setPressed(true);
      if (!internalField0149.player.isSprinting() && this.internalMethod09901()) {
         internalField0149.player.setSprinting(true);
      }
   }

   void internalMethod01235(Entity localValue1) {
      this.internalField0410 = localValue1;
      this.internalField0227 = 1;
   }

   private void internalMethod09247() {
      if (this.internalField0410 != null && internalField0149.interactionManager != null) {
         if (this.internalField0410.isRemoved() || !this.internalMethod09901()) {
            this.internalField0410 = null;
            this.internalField0227 = 0;
         } else if (this.internalField0227 > 0) {
            this.internalField0227--;
         } else {
            Entity localValue1 = this.internalField0410;
            this.internalField0410 = null;
            this.internalField1099 = true;

            try {
               internalField0149.interactionManager.attackEntity(internalField0149.player, localValue1);
               internalField0149.player.swingHand(Hand.MAIN_HAND);
            } finally {
               this.internalField1099 = false;
            }
         }
      }
   }

   boolean internalMethod09901() {
      return internalField0149.player.input.hasForwardMovement()
         && !internalField0149.player.horizontalCollision
         && !internalField0149.player.isSneaking()
         && !internalField0149.player.isTouchingWater()
         && !internalField0149.player.isSubmergedInWater()
         && (internalField0149.player.getHungerManager().getFoodLevel() > 6 || internalField0149.player.getAbilities().allowFlying);
   }
}
