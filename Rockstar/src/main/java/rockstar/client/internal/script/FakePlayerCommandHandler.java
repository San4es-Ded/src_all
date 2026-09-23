package rockstar.client.internal.script;







import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.event.*;
import rockstar.client.command.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import com.mojang.authlib.GameProfile;
import java.util.UUID;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.network.packet.s2c.play.EntityStatusS2CPacket;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.Vec3d;
import pyrock.events.game.AttackEvent;
import pyrock.events.player.ClientPlayerTickEvent;
import pyrock.events.window.KeyPressEvent;

public class FakePlayerCommandHandler implements MinecraftClientAccess {
   private FakePlayerEntity internalField0195;
   private float internalField0205;
   private float internalField0206;
   private final EventListener<AttackEvent> internalField0157 = localValue1 -> {
      if (this.internalField0195 != null && localValue1.getEntity() == this.internalField0195 && this.internalField0195.hurtTime == 0) {
         internalField0149.world
            .playSound(
               internalField0149.player,
               this.internalField0195.getX(),
               this.internalField0195.getY(),
               this.internalField0195.getZ(),
               SoundEvents.ENTITY_PLAYER_HURT,
               SoundCategory.PLAYERS,
               1.0F,
               1.0F
            );
         if (internalField0149.player.fallDistance > 0.0F) {
            internalField0149.world
               .playSound(
                  internalField0149.player,
                  this.internalField0195.getX(),
                  this.internalField0195.getY(),
                  this.internalField0195.getZ(),
                  SoundEvents.ENTITY_PLAYER_ATTACK_CRIT,
                  SoundCategory.PLAYERS,
                  1.0F,
                  1.0F
               );
         } else {
            internalField0149.world
               .playSound(
                  internalField0149.player,
                  this.internalField0195.getX(),
                  this.internalField0195.getY(),
                  this.internalField0195.getZ(),
                  SoundEvents.ENTITY_PLAYER_ATTACK_SWEEP,
                  SoundCategory.PLAYERS,
                  1.0F,
                  1.0F
               );
         }

         this.internalField0195.onDamaged(internalField0149.world.getDamageSources().generic());
         this.internalField0195.setHealth(this.internalField0195.getHealth() + this.internalField0195.getAbsorptionAmount() - 1.0F);
         if (this.internalField0195.isDead()) {
            this.internalField0195.setHealth(10.0F);
            new EntityStatusS2CPacket(this.internalField0195, (byte)35).apply(internalField0149.player.networkHandler);
         }
      }
   };
   private final EventListener<KeyPressEvent> internalField0158 = localValue1 -> {
      if (this.internalField0195 != null && internalField0149.currentScreen == null) {
         int localValue2 = localValue1.getKey();
         int localValue3 = localValue1.getAction();
         float localValue4 = 2.0F;
         if (localValue2 == 265) {
            this.internalField0205 = localValue3 != 1 && localValue3 != 2 ? 0.0F : localValue4;
         } else if (localValue2 == 264) {
            this.internalField0205 = localValue3 != 1 && localValue3 != 2 ? 0.0F : -localValue4;
         } else if (localValue2 == 263) {
            this.internalField0206 = localValue3 != 1 && localValue3 != 2 ? 0.0F : localValue4;
         } else if (localValue2 == 262) {
            this.internalField0206 = localValue3 != 1 && localValue3 != 2 ? 0.0F : -localValue4;
         }
      }
   };
   private final EventListener<ClientPlayerTickEvent> internalField1028 = localValue1 -> {
      if (this.internalField0195 != null && internalField0149.player != null) {
         if (internalField0149.currentScreen != null) {
            this.internalField0205 = 0.0F;
            this.internalField0206 = 0.0F;
            this.internalField0195.setSprinting(false);
         } else {
            if (this.internalField0205 == 0.0F && this.internalField0206 == 0.0F) {
               this.internalField0195.setSprinting(false);
               this.internalField0195.setVelocity(0.0, this.internalField0195.getVelocity().y, 0.0);
               this.internalField0195.limbAnimator.setSpeed(0.0F);
            } else {
               float localValue2 = internalField0149.player.getYaw();
               double localValue3 = 0.2;
               double localValue5 = this.internalField0206 * Math.cos(Math.toRadians(localValue2)) - this.internalField0205 * Math.sin(Math.toRadians(localValue2));
               double localValue7 = this.internalField0205 * Math.cos(Math.toRadians(localValue2)) + this.internalField0206 * Math.sin(Math.toRadians(localValue2));
               Vec3d localValue9 = new Vec3d(localValue5 * localValue3, this.internalField0195.getVelocity().y, localValue7 * localValue3);
               this.internalField0195.move(MovementType.SELF, localValue9);
               this.internalField0195.setSprinting(true);
            }
         }
      }
   };

   public CommandNode internalMethod06472() {
      return CommandBuilder.internalMethod00593("fakeplayer")
         .internalMethod05325("fp")
         .internalMethod06148("commands.fakeplayer.description")
         .internalMethod01539("action", localValue0 -> {
            localValue0.internalMethod04818("add", "remove", "del");
            localValue0.internalMethod07138("add", "remove");
         })
         .internalMethod00262(this::internalMethod01312)
         .internalMethod04146();
   }

   private void internalMethod01312(ParsedCommand localValue1) {
      String localValue2 = (String)localValue1.internalMethod02266().getFirst();
      String localValue3 = localValue2.toLowerCase();
      switch (localValue3) {
         case "add":
            this.internalMethod01019();
            break;
         case "remove":
         case "del":
            this.internalMethod01023();
      }
   }

   public void internalMethod01019() {
      RockstarClient.getInstance().internalMethod03317().internalMethod00647(this);
      if (this.internalField0195 != null) {
         this.internalField0195.discard();
         this.internalField0195 = null;
      }

      this.internalField0195 = new FakePlayerEntity(
         internalField0149.world, new GameProfile(UUID.fromString("66123666-6666-6666-6666-666666666600"), "FakePlayer")
      );
      this.internalField0195.copyPositionAndRotation(internalField0149.player);
      this.internalField0195.setStackInHand(Hand.MAIN_HAND, internalField0149.player.getMainHandStack().copy());
      this.internalField0195.setStackInHand(Hand.OFF_HAND, internalField0149.player.getOffHandStack().copy());
      this.internalField0195.addStatusEffect(new StatusEffectInstance(StatusEffects.REGENERATION, 9999, 2));
      this.internalField0195.addStatusEffect(new StatusEffectInstance(StatusEffects.ABSORPTION, 9999, 4));
      this.internalField0195.addStatusEffect(new StatusEffectInstance(StatusEffects.RESISTANCE, 9999, 1));
      internalField0149.world.addEntity(this.internalField0195);
      RockstarClient.getInstance()
         .internalMethod02503()
         .internalMethod00599(
            NotificationType.internalField0704,
            LanguageManager.internalMethod07214("commands.fakeplayer.success"),
            LanguageManager.internalMethod07214("commands.fakeplayer.added")
         );
   }

   public void internalMethod01023() {
      if (this.internalField0195 == null) {
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod00599(
               NotificationType.internalField0705,
               LanguageManager.internalMethod07214("commands.fakeplayer.error"),
               LanguageManager.internalMethod07214("commands.fakeplayer.not_exists")
            );
      } else {
         this.internalField0195.discard();
         this.internalField0195 = null;
         RockstarClient.getInstance()
            .internalMethod02503()
            .internalMethod00599(
               NotificationType.internalField0704,
               LanguageManager.internalMethod07214("commands.fakeplayer.success"),
               LanguageManager.internalMethod07214("commands.fakeplayer.removed")
            );
         RockstarClient.getInstance().internalMethod03317().internalMethod07237(this);
      }
   }
}
