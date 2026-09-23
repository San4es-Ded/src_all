package platform.inject.mixin;

import aethereal.core.Westra;
import net.minecraft.class_310;
import net.minecraft.class_412;
import net.minecraft.class_437;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_412.class})
public class ConnectScreenMixin {
   @Inject(
      method = {"method_36877(Lnet/minecraft/class_437;Lnet/minecraft/class_310;Lnet/minecraft/class_639;Lnet/minecraft/class_642;ZLnet/minecraft/class_9112;)V"},
      at = {@At("HEAD")}
   )
   private static void connect(
      class_437 screen, class_310 client, class_639 address, class_642 info, boolean quickPlay, @Nullable class_9112 cookieStorage, CallbackInfo ci
   ) {
      Westra.h().d().v().h().a(info);
   }
}
