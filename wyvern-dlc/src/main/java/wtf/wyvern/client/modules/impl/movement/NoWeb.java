package wtf.wyvern.client.modules.impl.movement;

import wtf.wyvern.core.eventbus.EventTarget;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import wtf.wyvern.core.events.impl.player.EventUpdate;
import wtf.wyvern.client.modules.api.Category;
import wtf.wyvern.client.modules.api.Module;
import wtf.wyvern.client.modules.api.ModuleAnnotation;
import wtf.wyvern.client.modules.api.setting.impl.ModeSetting;
import wtf.wyvern.utility.game.player.MovingUtil;
import wtf.astroguard.J2C.FastNative;

@ModuleAnnotation(
   name = "NoWeb",
   category = Category.MOVEMENT,
   description = "Меняет замедление от паутины"
)
@FastNative
public class NoWeb extends Module {
   public static final NoWeb INSTANCE = new NoWeb();

   private final ModeSetting mode = new ModeSetting("Режим", "Grim", "Обычный");
   private int grimTicks;

   @EventTarget
   private void onUpdate(EventUpdate event) {
      if (mc.player == null || mc.world == null) {
         return;
      }

      if (!isInWeb()) {
         grimTicks = 0;
         return;
      }

      Vec3d velocity = mc.player.getVelocity();
      if (mode.is("Grim")) {
         // Preserve the normal cobweb state and add only a small bounded
         // horizontal pulse. Alternating the limit avoids a constant forced
         // speed while still making movement visibly faster than vanilla.
         grimTicks++;
         double horizontalSpeed = (grimTicks & 1) == 0 ? 0.072D : 0.066D;
         double y = velocity.y;
         if (mc.options.jumpKey.isPressed()) {
            y = Math.max(y, 0.045D);
         } else if (mc.options.sneakKey.isPressed()) {
            y = Math.min(y, -0.055D);
         }
         mc.player.setVelocity(velocity.x, y, velocity.z);
         if (MovingUtil.hasPlayerMovement()) {
            MovingUtil.strafe(horizontalSpeed);
         }
      } else {
         double y = 0.0D;
         if (mc.options.jumpKey.isPressed()) {
            y = 0.32D;
         } else if (mc.options.sneakKey.isPressed()) {
            y = -0.42D;
         }
         mc.player.setVelocity(velocity.x, y, velocity.z);
         if (MovingUtil.hasPlayerMovement()) {
            MovingUtil.strafe(0.35D);
         }
      }
   }

   @Override
   public void onDisable() {
      grimTicks = 0;
      super.onDisable();
   }

   private boolean isInWeb() {
      Box box = mc.player.getBoundingBox().expand(0.001D);
      for (BlockPos pos : BlockPos.iterate(
         MathHelper.floor(box.minX),
         MathHelper.floor(box.minY),
         MathHelper.floor(box.minZ),
         MathHelper.floor(box.maxX),
         MathHelper.floor(box.maxY),
         MathHelper.floor(box.maxZ))) {
         if (mc.world.getBlockState(pos).isOf(Blocks.COBWEB)) {
            return true;
         }
      }

      return false;
   }
}
