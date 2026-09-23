package rockstar.modules.movement;









import rockstar.client.util.*;
import rockstar.client.setting.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.*;
import rockstar.modules.combat.AuraModule;
import rockstar.modules.player.GuiMoveModule;
import rockstar.client.module.Module;

import java.util.ArrayList;
import java.util.List;
import lombok.Generated;
import net.minecraft.block.Blocks;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.UpdateSelectedSlotC2SPacket;
import net.minecraft.network.packet.c2s.play.ClientCommandC2SPacket.Mode;
import net.minecraft.network.packet.s2c.play.PlayerPositionLookS2CPacket;
import net.minecraft.screen.slot.Slot;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult.Success;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult.Type;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.RaycastContext.FluidHandling;
import net.minecraft.world.RaycastContext.ShapeType;
import pyrock.events.network.ReceivePacketEvent;
import pyrock.events.player.ClientPlayerTickEndEvent;
import pyrock.events.player.EventOnMovePost;
import pyrock.events.player.InputEvent;

@ModuleInfo(
   name = "Speed",
   category = ModuleCategory.MOVEMENT,
   internalMethod09633 = "modules.descriptions.speed"
)
public class SpeedModule extends Module {
   private ModeSetting internalField0668;
   private ModeSetting.InternalType0088 internalField0237;
   private ModeSetting.InternalType0088 internalField0238;
   private ModeSetting.InternalType0088 internalField1066;
   private ModeSetting.InternalType0088 internalField1067;
   private ModeSetting.InternalType0088 internalField1068;
   private ModeSetting.InternalType0088 internalField1065;
   private ModeSetting.InternalType0088 internalField1480;
   private SliderSetting internalField0383;
   private SectionSetting internalField0667;
   private SliderSetting internalField0382;
   private SliderSetting internalField1142;
   private SliderSetting internalField1140;
   private SliderSetting internalField1141;
   private SliderSetting internalField1143;
   private SliderSetting internalField1529;
   private BooleanSetting internalField0650;
   private int internalField0228;
   private int internalField1053;
   private boolean internalField0277;
   private final AutoJumpManager internalField0528 = new AutoJumpManager();
   private final AutoElytraSwap internalField0532 = AutoElytraSwap.internalMethod04072();
   private final EventListener<EventOnMovePost> internalField0157 = localValue1 -> {
      if (this.internalField1065.isSelected() && internalField0149.player != null) {
         this.internalMethod04895(1.7F);
         if (this.internalField0228 > 3) {
            double localValue2 = 0.03;
            if (this.internalField0228 % 2 == 0) {
               internalField0149.player.addVelocity(0.0, 0.03F, 0.0);
               localValue2 = internalField0149.player.isOnGround() ? 0.085 : 0.03;
            }

            Vec3d localValue4 = this.internalMethod04180();
            internalField0149.player.addVelocity(localValue4.x * localValue2, 0.0, localValue4.z * localValue2);
         }

         this.internalField0228++;
      }
   };
   private final EventListener<InputEvent> internalField0158 = localValue1 -> {
      if (this.internalField1480.isSelected()) {
         this.internalField0528.internalMethod02345(localValue1);
      }

      if (this.internalField1065.isSelected()) {
         if (internalField0149.player == null) {
            return;
         }

         if (internalField0149.player.verticalCollision) {
            this.internalField1053++;
         } else {
            this.internalField1053 = 0;
         }

         if (this.internalField1053 >= 1) {
            internalField0149.player.jump();
         }
      }

      if (this.internalField1067.isSelected()) {
         LivingEntity localValue3 = RockstarClient.getInstance().internalMethod04463().internalMethod04526() instanceof LivingEntity localValue4 ? localValue4 : null;
         if (localValue3 == null) {
            return;
         }

         AuraModule localValue9 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
         Vec3d localValue5 = localValue3.getEntityPos()
            .add(localValue3.getEntityPos().subtract(new Vec3d(localValue3.lastX, localValue3.lastY, localValue3.lastZ)).multiply(this.internalField1529.internalMethod08576()));
         if ((
               GameUtils.internalMethod00611(localValue3, this.internalField1143.internalMethod08576())
                  || GameUtils.internalMethod02992(localValue3, EntityPositionTracker.internalMethod01255(localValue3), this.internalField1143.internalMethod08576())
            )
            && internalField0149.options.forwardKey.isPressed()
            && internalField0149.player.hurtTime <= 0
            && RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class).internalMethod06994().isEmpty()) {
            RotationManager localValue6 = RockstarClient.getInstance().internalMethod02368();
            float localValue7 = RotationUtils.internalMethod08495(localValue6.internalMethod09074().internalMethod00169(), localValue6.internalMethod08456().internalMethod00169());
            Vec3d localValue8 = internalField0149.player.getEntityPos();
            if (internalField0149.world
                  .raycast(new RaycastContext(internalField0149.player.getEntityPos(), localValue5, ShapeType.COLLIDER, FluidHandling.NONE, internalField0149.player))
                  .getType()
               != Type.MISS) {
               return;
            }

            internalField0149.options.sprintKey.setPressed(false);
            localValue1.setSprint(false);
            internalField0149.player.setSprinting(false);
         }
      }
   };
   private final EventListener<ClientPlayerTickEndEvent> internalField1028 = localValue1 -> {
      if (this.internalField1065.isSelected() && internalField0149.player != null && internalField0149.player.networkHandler != null) {
         if (this.internalField0228 % 2 == 0) {
            this.internalMethod04895(0.3F);
            internalField0149.player.networkHandler.sendPacket(new ClientCommandC2SPacket(internalField0149.player, Mode.START_FALL_FLYING));
         }
      }
   };
   private final EventListener<ReceivePacketEvent> internalField1029 = localValue1 -> {
      if (this.internalField1065.isSelected()) {
         if (localValue1.getPacket() instanceof PlayerPositionLookS2CPacket) {
            if (this.internalField0228 % 2 == 1) {
               this.internalField0228++;
            }

            this.internalMethod04895(1.0F);
         }
      }
   };

   public SpeedModule() {
      this.internalMethod09254();
   }

   private void internalMethod09254() {
      this.internalField0668 = new ModeSetting(this, "modules.settings.speed.mode");
      this.internalField0237 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.speed.vanilla");
      this.internalField0238 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.speed.spooky_elytra");
      this.internalField1066 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.speed.collision");
      this.internalField1067 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.speed.collision_target");
      this.internalField1068 = new ModeSetting.InternalType0088(this.internalField0668, "modules.settings.speed.ice");
      this.internalField1065 = new ModeSetting.InternalType0088(this.internalField0668, "ReallyWorld");
      this.internalField1480 = new ModeSetting.InternalType0088(this.internalField0668, "HolyWorld");
      this.internalField0383 = new SliderSetting(this, "modules.settings.speed.distance", () -> !this.internalField1066.isSelected())
         .internalMethod05900(0.05F)
         .internalMethod02732(2.0F)
         .internalMethod08673(0.05F)
         .internalMethod08074(0.3F);
      this.internalField0667 = new SectionSetting(this, "modules.settings.speed.speed_info", () -> !this.internalField1066.isSelected());
      this.internalField0382 = new SliderSetting(this, "modules.settings.speed.on_ground", () -> !this.internalField1066.isSelected())
         .internalMethod05900(0.5F)
         .internalMethod02732(3.0F)
         .internalMethod08673(0.05F)
         .internalMethod08074(1.1F);
      this.internalField1142 = new SliderSetting(this, "modules.settings.speed.on_jump", () -> !this.internalField1066.isSelected())
         .internalMethod05900(0.5F)
         .internalMethod02732(3.0F)
         .internalMethod08673(0.05F)
         .internalMethod08074(1.1F);
      this.internalField1140 = new SliderSetting(this, "modules.settings.speed.on_fall", () -> !this.internalField1066.isSelected())
         .internalMethod05900(0.5F)
         .internalMethod02732(3.0F)
         .internalMethod08673(0.05F)
         .internalMethod08074(1.1F);
      this.internalField1141 = new SliderSetting(this, "modules.settings.speed.power", () -> !this.internalField1067.isSelected())
         .internalMethod05900(0.01F)
         .internalMethod02732(0.1F)
         .internalMethod08673(0.01F)
         .internalMethod08074(0.06F);
      this.internalField1143 = new SliderSetting(this, "modules.settings.speed.boost_range", () -> !this.internalField1067.isSelected())
         .internalMethod05900(0.0F)
         .internalMethod02732(2.0F)
         .internalMethod08673(0.1F)
         .internalMethod08074(1.0F);
      this.internalField1529 = new SliderSetting(this, "\u041f\u0440\u0435\u0434\u0438\u043a\u0442", () -> !this.internalField1067.isSelected())
         .internalMethod05900(0.0F)
         .internalMethod02732(10.0F)
         .internalMethod08673(1.0F)
         .internalMethod08074(5.0F);
      this.internalField0650 = new BooleanSetting(
            this, "\u0421\u0432\u0430\u043f \u044d\u043b\u0438\u0442\u0440\u044b", () -> !this.internalField1480.isSelected()
         )
         .internalMethod06630();
   }

   @Override
   public void internalMethod08229() {
      if (this.internalField0277 && !this.internalField1065.isSelected()) {
         this.internalMethod09256();
      }

      if (this.internalField1480.isSelected()) {
         this.internalMethod09420();
      } else if (this.internalField0650.internalMethod04496()
         && internalField0149.player != null
         && InventoryUtils.internalMethod06826().internalMethod00210() == Items.ELYTRA) {
         this.internalField0532.internalMethod04912();
      }

      if (this.internalField1068.isSelected() && InventoryUtils.internalMethod06160().internalMethod00210() == Items.ICE) {
         RockstarClient.getInstance()
            .internalMethod02368()
            .internalMethod00418(
               new Rotation(internalField0149.player.getYaw(), 90.0F),
               RotationBehavior.internalField0114,
               180.0F,
               180.0F,
               180.0F,
               RotationPriority.internalField1009
            );
         BlockHitResult localValue1 = (BlockHitResult)MathUtils.internalMethod01143(10.0, internalField0149.player.getYaw(), 90.0F, internalField0149.player);
         ActionResult localValue2 = internalField0149.interactionManager.interactBlock(internalField0149.player, Hand.MAIN_HAND, localValue1);
         if (localValue2 instanceof Success) {
            internalField0149.player.swingHand(Hand.MAIN_HAND);
         }
      }

      if (this.internalField1066.isSelected()) {
         for (Entity localValue23 : internalField0149.world.getEntities()) {
            if (localValue23 instanceof LivingEntity localValue3
               && localValue3.isAlive()
               && !localValue3.isSpectator()
               && localValue3 != internalField0149.player
               && GameUtils.internalMethod00611(localValue3, this.internalField0383.internalMethod08576())) {
               float localValue4 = internalField0149.world
                  .getBlockState(
                     internalField0149.player
                        .getBlockPos()
                        .add(
                           (int)internalField0149.player.getVelocity().x,
                           (int)internalField0149.player.getVelocity().y,
                           (int)internalField0149.player.getVelocity().z
                        )
                  )
                  .getBlock()
                  .getSlipperiness();
               if (internalField0149.player.isOnGround()) {
                  float localValue10000 = localValue4 * 0.91F;
               } else {
                  float localValue41 = 0.51F;
               }

               float localValue6 = internalField0149.player.isOnGround() ? localValue4 : 0.57F;
               float localValue7 = internalField0149.player.isOnGround()
                  ? this.internalField0382.internalMethod08576()
                  : (internalField0149.player.fallDistance > 0.0F ? this.internalField1140.internalMethod08576() : this.internalField1142.internalMethod08576());
               internalField0149.player
                  .setVelocity(
                     internalField0149.player.getVelocity().x * localValue7, internalField0149.player.getVelocity().y, internalField0149.player.getVelocity().z * localValue7
                  );
               break;
            }
         }
      }

      if (this.internalField0237.isSelected()) {
         BlockPos localValue20 = internalField0149.player.getBlockPos().add(0, -1, 0);
         internalField0149.options.sneakKey.setPressed(false);
         RockstarClient.getInstance().internalMethod02368().internalMethod04698(new Rotation(internalField0149.player.getYaw(), 90.0F));
         if (internalField0149.player.isOnGround() && !internalField0149.options.jumpKey.isPressed()) {
            internalField0149.player.jump();
            Vec3d localValue24 = internalField0149.player.getVelocity();
            internalField0149.player.setVelocity(localValue24.x, localValue24.y - 0.085F, localValue24.z);
            BlockPos localValue27 = internalField0149.player.getBlockPos().add(0, 1, 0);
            BlockPos localValue31 = internalField0149.player.getBlockPos();
            BlockPos localValue5 = internalField0149.player.getBlockPos().add(0, -1, 0);
            BlockPos localValue34 = internalField0149.player.getBlockPos().add(0, -2, 0);
            internalField0149.world.setBlockState(localValue5, Blocks.BLUE_ICE.getDefaultState());
            Vec3d localValue36 = new Vec3d(localValue31.getX(), localValue31.getY(), localValue31.getZ());
            new BlockHitResult(localValue36, Direction.UP, localValue31, false);
            Vec3d localValue9 = new Vec3d(localValue5.getX(), localValue5.getY(), localValue5.getZ());
            BlockHitResult localValue10 = new BlockHitResult(localValue9, Direction.UP, localValue5, false);
            Vec3d localValue11 = new Vec3d(localValue34.getX(), localValue34.getY(), localValue34.getZ());
            new BlockHitResult(localValue11, Direction.UP, localValue34, false);
            internalField0149.player.networkHandler.sendPacket(new PlayerInteractBlockC2SPacket(Hand.OFF_HAND, localValue10, 0));
         }
      } else if (this.internalField0238.isSelected()) {
         SlotCollection localValue21 = InventorySlots.internalMethod02872();
         HotbarSlot localValue25 = (HotbarSlot)localValue21.internalMethod02510(Items.ELYTRA);
         if (localValue25 != null && internalField0149.player.fallDistance > 1.0F) {
            HotbarSlot localValue28 = InventoryUtils.internalMethod06160();
            internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(localValue25.internalMethod08745()));
            InventoryUtils.internalMethod01980(localValue25);
            internalField0149.interactionManager.interactItem(internalField0149.player, Hand.MAIN_HAND);
            ((Slot)internalField0149.player.currentScreenHandler.slots.get(6)).setStack(new ItemStack(Items.ELYTRA));
            if (internalField0149.player.isSprinting() && internalField0149.player.input.hasForwardMovement() && internalField0149.player.checkGliding()) {
               internalField0149.player.networkHandler.sendPacket(new ClientCommandC2SPacket(internalField0149.player, Mode.START_FALL_FLYING));
            }

            InventoryUtils.internalMethod01980(localValue28);
            internalField0149.player.networkHandler.sendPacket(new UpdateSelectedSlotC2SPacket(internalField0149.player.getInventory().getSelectedSlot()));
         }
      } else if (this.internalField1067.isSelected()) {
         LivingEntity localValue26 = RockstarClient.getInstance().internalMethod04463().internalMethod04526() instanceof LivingEntity localValue29 ? localValue29 : null;
         if (localValue26 == null) {
            return;
         }

         AuraModule localValue30 = RockstarClient.getInstance().getModuleManager().getModule(AuraModule.class);
         Vec3d localValue32 = localValue26.getEntityPos()
            .add(localValue26.getEntityPos().subtract(new Vec3d(localValue26.lastX, localValue26.lastY, localValue26.lastZ)).multiply(this.internalField1529.internalMethod08576()));
         if ((
               GameUtils.internalMethod00611(localValue26, this.internalField1143.internalMethod08576())
                  || GameUtils.internalMethod02992(localValue26, EntityPositionTracker.internalMethod01255(localValue26), this.internalField1143.internalMethod08576())
            )
            && internalField0149.options.forwardKey.isPressed()
            && internalField0149.player.hurtTime <= 0
            && RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class).internalMethod06994().isEmpty()) {
            RotationManager localValue33 = RockstarClient.getInstance().internalMethod02368();
            float localValue35 = RotationUtils.internalMethod08495(localValue33.internalMethod09074().internalMethod00169(), localValue33.internalMethod08456().internalMethod00169());
            Vec3d localValue37 = internalField0149.player.getEntityPos();
            if (internalField0149.world
                  .raycast(new RaycastContext(internalField0149.player.getEntityPos(), localValue32, ShapeType.COLLIDER, FluidHandling.NONE, internalField0149.player))
                  .getType()
               != Type.MISS) {
               return;
            }

            Vec3d localValue8 = localValue32.subtract(localValue37).normalize();
            float localValue38 = internalField0149.world
               .getBlockState(
                  BlockPos.ofFloored(
                     internalField0149.player
                        .getEntityPos()
                        .add(internalField0149.player.getVelocity().x, internalField0149.player.getVelocity().y, internalField0149.player.getVelocity().z)
                  )
               )
               .getBlock()
               .getSlipperiness();
            float localValue39 = internalField0149.player.isOnGround() ? localValue38 : 0.79F;
            float localValue40 = internalField0149.player.isOnGround() ? localValue38 : 0.99F;
            double localValue12 = internalField0149.player.getVelocity().y;
            float localValue14 = this.internalField1141.internalMethod08576() * 3.0F;
            double localValue15 = localValue8.x * localValue14 * localValue40 / localValue39;
            double localValue17 = localValue8.z * localValue14 * localValue40 / localValue39;
            internalField0149.player.setVelocity(internalField0149.player.getVelocity().x + localValue15, localValue12, internalField0149.player.getVelocity().z + localValue17);
         }
      }
   }

   private Vec3d internalMethod04180() {
      if (internalField0149.player.input == null) {
         return Vec3d.ZERO;
      } else {
         float localValue1 = rockstar.client.compat.InputCompat.forward(internalField0149.player.input);
         float localValue2 = rockstar.client.compat.InputCompat.sideways(internalField0149.player.input);
         if (localValue1 == 0.0F && localValue2 == 0.0F) {
            return Vec3d.ZERO;
         } else {
            double localValue3 = GameUtils.internalMethod01047(internalField0149.player.getYaw(), localValue1, localValue2);
            return new Vec3d(-Math.sin(localValue3), 0.0, Math.cos(localValue3));
         }
      }
   }

   private void internalMethod04895(float localValue1) {
      GameUtils.internalMethod03366(localValue1);
      this.internalField0277 = true;
   }

   private void internalMethod09256() {
      this.internalField0228 = 0;
      this.internalField1053 = 0;
      this.internalField0277 = false;
      GameUtils.internalMethod00468();
   }

   private void internalMethod09420() {
      if (internalField0149.player != null) {
         boolean localValue1 = !RockstarClient.getInstance().getModuleManager().getModule(GuiMoveModule.class).internalMethod06994().isEmpty();
         if (!this.internalField0532.internalMethod04911() && !localValue1 && this.internalField0532.internalMethod01075(500L)) {
            this.internalField0528.internalMethod01829();
         } else {
            this.internalField0528.internalMethod01834();
         }
      }
   }

   public static boolean internalMethod06513(ClientPlayerEntity localValue0) {
      SpeedModule localValue1 = RockstarClient.getInstance().getModuleManager().getModule(SpeedModule.class);
      return localValue1 != null && localValue1.isEnabled() && localValue1.internalField1480.isSelected() && localValue1.internalField0528.internalMethod07057(localValue0);
   }

   public static boolean internalMethod09255() {
      SpeedModule localValue0 = RockstarClient.getInstance().getModuleManager().getModule(SpeedModule.class);
      return localValue0 != null && localValue0.isEnabled() && localValue0.internalField1480.isSelected();
   }

   public static boolean internalMethod09257() {
      SpeedModule localValue0 = RockstarClient.getInstance().getModuleManager().getModule(SpeedModule.class);
      return localValue0 != null && localValue0.isEnabled() && localValue0.internalField1480.isSelected();
   }

   private void internalMethod09421() {
      this.internalField0528.internalMethod08583();
   }

   @Override
   public void onEnable() {
      this.internalMethod09421();
      if (this.internalField1480.isSelected() && this.internalField0650.internalMethod04496()) {
         this.internalField0532.internalMethod04910();
      }

      super.onEnable();
   }

   @Override
   public void onDisable() {
      this.internalMethod09256();
      this.internalMethod09421();
      if (this.internalField1480.isSelected() && this.internalField0650.internalMethod04496()) {
         this.internalField0532.internalMethod04912();
      }

      super.onDisable();
   }

   @Generated
   public ModeSetting.InternalType0088 internalMethod06446() {
      return this.internalField1067;
   }

   @Generated
   public SliderSetting internalMethod00142() {
      return this.internalField1529;
   }

   static class InternalType0244 {
      private final Vec3d internalField0283;

      @Generated
      public InternalType0244(Vec3d localValue1) {
         this.internalField0283 = localValue1;
      }
   }
}
