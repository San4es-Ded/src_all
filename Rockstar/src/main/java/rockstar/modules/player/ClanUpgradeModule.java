package rockstar.modules.player;






import rockstar.client.util.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.inventory.*;
import rockstar.client.event.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.network.packet.c2s.play.PlayerInteractBlockC2SPacket;
import net.minecraft.network.packet.c2s.play.PlayerInteractItemC2SPacket;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import pyrock.events.player.ClientPlayerTickEvent;

@ModuleInfo(
   name = "Clan Upgrade",
   category = ModuleCategory.PLAYER
)
public class ClanUpgradeModule extends Module {
   private final Stopwatch internalField0519 = new Stopwatch();
   private final EventListener<ClientPlayerTickEvent> internalField0157 = localValue1 -> {
      SlotCollection localValue2 = InventorySlots.internalMethod07766()
         .internalMethod07591(InventorySlots.internalMethod02872())
         .internalMethod07591(InventorySlots.internalMethod03558());
      InventorySlot localValue3 = localValue2.internalMethod02510(Items.TORCH);
      InventorySlot localValue4 = localValue2.internalMethod02510(Items.REDSTONE);
      if (internalField0149.player.getMainHandStack().getItem() == Items.TORCH || localValue3 == null || localValue4 != null) {
         if (internalField0149.player.getMainHandStack().getItem() != Items.REDSTONE && localValue3 == null && localValue4 != null) {
            if (localValue4 instanceof HotbarSlot localValue12 && InventoryUtils.internalMethod06160().internalMethod00210() != Items.REDSTONE) {
               InventoryUtils.internalMethod01980(localValue12);
            }
         } else if (localValue3 != null && localValue4 != null) {
            if (localValue3 instanceof HotbarSlot localValue11 && InventoryUtils.internalMethod06160().internalMethod00210() != Items.TORCH) {
               InventoryUtils.internalMethod01980(localValue11);
            }
         } else if (localValue3 == null && localValue4 == null) {
            return;
         }
      } else if (localValue3 instanceof HotbarSlot localValue5 && InventoryUtils.internalMethod06160().internalMethod00210() != Items.TORCH) {
         InventoryUtils.internalMethod01980(localValue5);
      }

      BlockPos localValue13 = internalField0149.player.getBlockPos();
      RockstarClient.getInstance()
         .internalMethod02368()
         .internalMethod00418(
            new Rotation(internalField0149.player.getYaw(), 88.0F),
            RotationBehavior.internalField0114,
            180.0F,
            80.0F,
            80.0F,
            RotationPriority.internalField0122
         );
      if (internalField0149.player.getMainHandStack().getItem() == Items.TORCH && internalField0149.world.getBlockState(localValue13).getBlock() != Blocks.TORCH
         || internalField0149.player.getMainHandStack().getItem() == Items.REDSTONE
            && internalField0149.world.getBlockState(localValue13).getBlock() != Blocks.REDSTONE_WIRE
            && internalField0149.player.isOnGround()
            && this.internalField0519.internalMethod02365(50L)
            && internalField0149.world.getBlockState(localValue13.down()).isSolid()) {
         internalField0149.interactionManager
            .sendSequencedPacket(
               internalField0149.world,
               localValue1x -> new PlayerInteractBlockC2SPacket(
                  Hand.MAIN_HAND, new BlockHitResult(Vec3d.ofCenter(internalField0149.player.getBlockPos()), Direction.UP, localValue13.down(), false), localValue1x
               )
            );
         internalField0149.player.swingHand(Hand.MAIN_HAND);
         this.internalField0519.internalMethod00701();
      }

      if ((
            internalField0149.world.getBlockState(localValue13).getBlock() == Blocks.TORCH
               || internalField0149.world.getBlockState(localValue13).getBlock() == Blocks.REDSTONE_WIRE
         )
         && this.internalField0519.internalMethod02365(50L)) {
         Vec3d localValue6 = Vec3d.ofCenter(localValue13.down()).subtract(internalField0149.player.getEyePos());
         double localValue7 = Math.sqrt(localValue6.x * localValue6.x + localValue6.z * localValue6.z);
         float localValue9 = (float)Math.toDegrees(Math.atan2(localValue6.z, localValue6.x)) - 90.0F + MathUtils.internalMethod05368(-2.0, 2.0);
         float localValue10 = (float)(-Math.toDegrees(Math.atan2(localValue6.y, localValue7))) + MathUtils.internalMethod05368(-1.0, 1.0);
         internalField0149.player.networkHandler.sendPacket(new PlayerInteractItemC2SPacket(Hand.MAIN_HAND, 0, localValue9, localValue10));
         internalField0149.interactionManager.updateBlockBreakingProgress(localValue13, Direction.UP);
         internalField0149.player.swingHand(Hand.MAIN_HAND);
      }
   };
}
