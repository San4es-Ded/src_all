package rockstar.modules.movement;


import rockstar.client.module.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import java.util.Map;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;

@ModuleInfo(
   name = "High Jump",
   category = ModuleCategory.MOVEMENT
)
public class HighJumpModule extends Module {
   private boolean internalField0277;
   private int internalField0227;

   @Override
   public void internalMethod08229() {
      if (this.internalField0277) {
         this.internalField0227++;
         double localValue24 = Math.min(this.internalField0227 / 12.0, 1.5);
         Vec3d localValue25 = internalField0149.player.getVelocity();
         internalField0149.player.setVelocity(localValue25.x, 1.0, localValue25.z);
         if (this.internalField0227 > 4) {
            this.internalField0277 = false;
         }
      } else {
         Vec3d localValue1 = internalField0149.player.getEntityPos();
         int localValue2 = MathHelper.floor(localValue1.x) >> 4;
         int localValue3 = MathHelper.floor(localValue1.z) >> 4;

         for (int localValue4 = localValue2 - 1; localValue4 <= localValue2 + 1; localValue4++) {
            for (int localValue5 = localValue3 - 1; localValue5 <= localValue3 + 1; localValue5++) {
               WorldChunk localValue6 = internalField0149.world.getChunkManager().getWorldChunk(localValue4, localValue5);
               if (localValue6 != null) {
                  Map localValue7 = localValue6.getBlockEntities();

                  for (BlockEntity localValue9 : (Iterable<BlockEntity>)(Iterable<?>)localValue7.values()) {
                     if (localValue9 instanceof ShulkerBoxBlockEntity localValue10) {
                        BlockPos localValue11 = localValue9.getPos();
                        double localValue12 = localValue1.x - (localValue11.getX() + 0.5);
                        double localValue14 = localValue1.z - (localValue11.getZ() + 0.5);
                        double localValue16 = Math.sqrt(localValue12 * localValue12 + localValue14 * localValue14);
                        double localValue18 = Math.abs(localValue1.y - (localValue11.getY() + 0.5));
                        double localValue20 = internalField0149.player.getVelocity().y > 1.0 ? 30.0 : 2.0;
                        if (localValue16 <= 1.5 && localValue18 <= localValue20 && internalField0149.player.fallDistance == 0.0F) {
                           float localValue22 = localValue10.getAnimationProgress(1.0F);
                           if (localValue22 > 0.0F && localValue22 != 1.0F) {
                              this.internalField0277 = true;
                              this.internalField0227 = 0;
                              Vec3d localValue23 = internalField0149.player.getVelocity();
                              internalField0149.player.setVelocity(localValue23.x, 1.0, localValue23.z);
                              internalField0149.setScreen(null);
                              return;
                           }
                        }
                     }
                  }
               }
            }
         }
      }
   }

   @Override
   public void onEnable() {
   }

   @Override
   public void onDisable() {
      this.internalField0277 = false;
      this.internalField0227 = 0;
   }
}
