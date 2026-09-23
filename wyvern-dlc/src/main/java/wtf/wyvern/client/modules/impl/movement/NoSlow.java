package wtf.wyvern.client.modules.impl.movement;

import wtf.wyvern.core.eventbus.EventTarget;
import java.util.Objects;
import net.minecraft.network.packet.c2s.play.PlayerActionC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import wtf.wyvern.core.events.impl.player.EventSlowWalking;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.BooleanSetting;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.utility.game.player.PlayerIntersectionUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
        name = "NoSlow",
        category = Category.MOVEMENT,
        description = "Убирает замедление во время еды"
)
public final class NoSlow extends Module {
   public static final NoSlow INSTANCE = new NoSlow();
   private final ModeSetting mode = new ModeSetting("Мод", new String[0]);
   private final ModeSetting.Value grimNew;
   private final ModeSetting.Value hw;
   private final ModeSetting.Value lonyGrief;
   private final ModeSetting.Value jump;
   private BooleanSetting sprint;

   private NoSlow() {
      this.grimNew = new ModeSetting.Value(this.mode, "Grim tick");
      this.hw = (new ModeSetting.Value(this.mode, "Grim")).select();
      this.lonyGrief = new ModeSetting.Value(this.mode, "LonyGrief");
      this.jump = new ModeSetting.Value(this.mode, "Прыжок");
      ModeSetting.Value var10005 = this.hw;
      Objects.requireNonNull(var10005);
      this.sprint = new BooleanSetting("Спринт", true, var10005::isSelected);
   }

   @FastNative
   @EventTarget
   public void onItemUse(EventSlowWalking e) {
      if (this.grimNew.isSelected() && mc.player.getItemUseTime() % 2 == 0) {
         e.setCancelled(true);
      }

      if (this.lonyGrief.isSelected()) {
         if (mc.player.getActiveHand() == Hand.OFF_HAND) {
            if (mc.player.age % 2 == 0 && mc.player.getVehicle() == null) {
               e.setCancelled(true);
            }
         } else if (mc.player.getItemUseTime() > 0) {
            e.setCancelled(true);
         }
      }

      if (this.hw.isSelected()) {
         Hand hand = mc.player.getActiveHand();
         if (this.sprint.isEnabled()) {
            mc.player.setSprinting(mc.player.canSprint() && mc.player.isWalking() && !mc.player.isBlind() && !mc.player.isGliding() && (!mc.player.shouldSlowDown() || mc.player.isSubmergedInWater()));
         }

         PlayerIntersectionUtil.useItem(hand.equals(Hand.MAIN_HAND) ? Hand.OFF_HAND : Hand.MAIN_HAND);
         e.setCancelled(true);
      }

   }

   @FastNative
   @EventTarget
   public void update(EventUpdate tickEvent) {
      if (mc.player == null || !mc.player.isAlive()) {
         return;
      }

      if (this.lonyGrief.isSelected()) {
         this.sendDigging();
      }

      // The jump mode uses only vanilla movement packets. While an item is
      // being used, a normal jump removes the ground slowdown without the
      // repeated inventory/input spoofing that Grim and RW flag.
      if (this.jump.isSelected()
              && mc.player.isUsingItem()
              && mc.player.isOnGround()
              && !mc.player.hasVehicle()
              && !mc.player.isGliding()) {
         mc.player.jump();
      }

   }

   @FastNative
   private void sendDigging() {
      if (mc.getNetworkHandler() == null) {
         return;
      }

      if (mc.player.isUsingItem() && mc.player.getItemUseTime() == 0) {
         mc.getNetworkHandler().sendPacket(new PlayerActionC2SPacket(
                 PlayerActionC2SPacket.Action.RELEASE_USE_ITEM,
                 BlockPos.ORIGIN,
                 Direction.getFacing(mc.player.getX(), mc.player.getY(), mc.player.getZ()),
                 0
         ));
      }
   }
}
