package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.core.Westra;
import net.minecraft.class_2561;
import net.minecraft.class_412;
import net.minecraft.class_4185;
import net.minecraft.class_419;
import net.minecraft.class_437;
import net.minecraft.class_500;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_419.class})
public abstract class DisconnectedScreenMixin extends class_437 {
   @Unique
   private class_4185 buttonWidget;

   private DisconnectedScreenMixin(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"method_25426"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_419;method_48640()V",
         shift = Shift.BEFORE
      )}
   )
   private void init(CallbackInfo ci) {
      class_642 server = Westra.h().d().v().h().a();
      if (server != null) {
         this.buttonWidget = (class_4185)this.method_37063(
            class_4185.method_46430(
                  class_2561.method_43470("Переподключиться"),
                  btn -> class_412.method_36877(
                     new class_500((class_437)null), Interface.aM_, class_639.method_2950(server.field_3761), server, false, (class_9112)null
                  )
               )
               .method_46434(0, 0, 200, 20)
               .method_46431()
         );
      }
   }

   @Inject(
      method = {"method_48640"},
      at = {@At("TAIL")}
   )
   private void refreshWidgetPositions(CallbackInfo ci) {
      if (this.buttonWidget != null) {
         int x = this.field_22789 / 2 - 100;
         int maxY = this.method_25396()
            .stream()
            .filter(child -> child instanceof class_4185 && child != this.buttonWidget)
            .map(child2 -> ((class_4185)child2).method_46427())
            .max((v0, v1) -> v0.compareTo(v1))
            .orElse(this.field_22790 / 2);
         this.buttonWidget.method_48229(x, maxY + 24);
      }
   }
}
