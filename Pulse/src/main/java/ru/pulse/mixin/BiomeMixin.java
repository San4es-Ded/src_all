package ru.pulse.mixin;

import net.minecraft.world.biome.Biome;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome.Precipitation;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pulse.module.ModuleRegistry;
import pulse.modules.visuals.Atmosphere;

@Mixin(Biome.class)
public class BiomeMixin {
   @Inject(require = 0, method = "getPrecipitation", at = @At("HEAD"), cancellable = true)
   private void onGetPrecipitation(BlockPos pos, int i, CallbackInfoReturnable<Precipitation> cir) {
      Atmosphere atmosphere = ModuleRegistry.ATMOSPHERE;
      if (atmosphere != null && atmosphere.k() && atmosphere.isWeatherCustom()) {
         String mode = atmosphere.getWeatherMode();
         if (mode.equals("Снег")) {
            cir.setReturnValue(Precipitation.SNOW);
         } else if (mode.equals("Дождь") || mode.equals("Гроза")) {
            cir.setReturnValue(Precipitation.RAIN);
         } else if (mode.equals("Ясно")) {
            cir.setReturnValue(Precipitation.NONE);
         }
      }
   }
}
