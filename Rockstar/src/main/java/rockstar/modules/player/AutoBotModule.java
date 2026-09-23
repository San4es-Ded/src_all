package rockstar.modules.player;







import rockstar.client.util.*;
import rockstar.client.rotation.*;
import rockstar.client.module.*;
import rockstar.client.internal.rotation.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import rockstar.client.*;
import rockstar.client.module.Module;

import net.minecraft.block.Blocks;
import net.minecraft.registry.Registries;
import net.minecraft.util.math.BlockPos;

@ModuleInfo(
   name = "Auto Bot",
   category = ModuleCategory.PLAYER
)
public class AutoBotModule extends Module {
   private final Stopwatch internalField0519 = new Stopwatch();
   private AutoBotModule.InternalType0380 internalField0510;

   public AutoBotModule() {
      this.internalField0510 = AutoBotModule.InternalType0380.internalField0510;
   }

   @Override
   public void onEnable() {
      this.internalField0510 = AutoBotModule.InternalType0380.internalField0510;
      RegionSelection.internalMethod00889().internalMethod05224();
   }

   @Override
   public void onDisable() {
      if (NewtonCoreManager.internalMethod00010()) {
         this.internalMethod02142().internalMethod00136();
      }

      RegionSelection.internalMethod00889().internalMethod05224();
   }

   @Override
   public void internalMethod08229() {
      if (internalField0149.player != null && internalField0149.world != null && NewtonCoreManager.internalMethod00010()) {
         if (this.internalField0519.internalMethod02365(200L)) {
            BlockPos localValue1 = internalField0149.player.getBlockPos();
            byte localValue2 = 10;

            for (int localValue3 = 0; localValue3 < localValue2 * 2; localValue3++) {
               for (int localValue4 = 0; localValue4 < localValue2 * 2; localValue4++) {
                  for (int localValue5 = 0; localValue5 < localValue2 * 2; localValue5++) {
                     BlockPos localValue6 = new BlockPos((localValue3 % 2 == 0 ? -localValue3 : localValue3) / 2, (localValue5 % 2 == 0 ? -localValue5 : localValue5) / 2, (localValue4 % 2 == 0 ? -localValue4 : localValue4) / 2);
                     BlockPos localValue7 = localValue1.add(localValue6);
                     if (this.internalMethod00750(localValue7)) {
                        return;
                     }
                  }
               }
            }

            this.internalField0519.internalMethod00701();
         }
      }
   }

   private boolean internalMethod00750(BlockPos localValue1) {
      BlockPos localValue2 = localValue1.up();
      if (internalField0149.world.getBlockState(localValue1).getBlock() == Blocks.OBSIDIAN
         && internalField0149.world.getBlockState(localValue1.east()).getBlock() == Blocks.OBSIDIAN
         && internalField0149.world.getBlockState(localValue1.north()).getBlock() == Blocks.OBSIDIAN
         && internalField0149.world.getBlockState(localValue1.south()).getBlock() == Blocks.OBSIDIAN
         && internalField0149.world.getBlockState(localValue1.west()).getBlock() == Blocks.OBSIDIAN
         && internalField0149.world.getBlockState(localValue2.east()).getBlock() == Blocks.AIR
         && internalField0149.world.getBlockState(localValue2.north()).getBlock() == Blocks.AIR
         && internalField0149.world.getBlockState(localValue2.south()).getBlock() == Blocks.AIR
         && internalField0149.world.getBlockState(localValue2.west()).getBlock() == Blocks.AIR) {
         if (localValue2.equals(internalField0149.player.getBlockPos())) {
            if (internalField0149.world.getBlockState(localValue2).getBlock() == Blocks.TORCH) {
               if (this.internalField0510 != AutoBotModule.InternalType0380.internalField1182) {
                  this.internalMethod00749(localValue2);
                  this.internalMethod00589("newton cleararea");
                  this.internalField0510 = AutoBotModule.InternalType0380.internalField1182;
                  this.internalField0519.internalMethod00701();
                  return true;
               }
            } else if (this.internalField0510 != AutoBotModule.InternalType0380.internalField1181) {
               this.internalMethod00749(localValue2);
               this.internalMethod00589("newton fill " + Registries.BLOCK.getId(Blocks.TORCH));
               this.internalField0510 = AutoBotModule.InternalType0380.internalField1181;
               this.internalField0519.internalMethod00701();
               return true;
            }
         } else if (this.internalField0510 != AutoBotModule.InternalType0380.internalField0509
               && this.internalField0510 != AutoBotModule.InternalType0380.internalField1181
            || this.internalField0510 == AutoBotModule.InternalType0380.internalField0509 && !this.internalMethod02142().internalMethod00137()) {
            this.internalMethod02142().internalMethod02230(localValue2);
            this.internalField0510 = AutoBotModule.InternalType0380.internalField0509;
            this.internalField0519.internalMethod00701();
            return true;
         }

         return false;
      } else {
         return false;
      }
   }

   private void internalMethod00749(BlockPos localValue1) {
      RegionSelection localValue2 = RegionSelection.internalMethod00889();
      localValue2.internalMethod05224();
      localValue2.internalMethod04567(localValue1);
      localValue2.internalMethod05870(localValue1);
   }

   private boolean internalMethod00589(String localValue1) {
      return this.internalMethod02142().internalMethod06067(localValue1);
   }

   private PathCommandApi internalMethod02142() {
      return NewtonApiHolder.internalMethod01856();
   }

   static enum InternalType0380 {
      internalField0510,
      internalField0509,
      internalField1181,
      internalField1182;
   }
}
