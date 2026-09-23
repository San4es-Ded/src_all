package platform.inject.mixin;

import aethereal.core.Interface;
import aethereal.util.ServerUtil;
import net.minecraft.class_124;
import net.minecraft.class_2561;
import net.minecraft.class_2583;
import net.minecraft.class_2588;
import net.minecraft.class_412;
import net.minecraft.class_4185;
import net.minecraft.class_433;
import net.minecraft.class_437;
import net.minecraft.class_442;
import net.minecraft.class_639;
import net.minecraft.class_642;
import net.minecraft.class_9112;
import net.minecraft.class_4185.class_4241;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import platform.inject.accessors.ButtonWidgetAccessor;

@Mixin({class_433.class})
public abstract class GameMenuScreenMixin extends class_437 {
   @Unique
   private boolean reconnect = false;
   @Unique
   private boolean disconnect = false;

   private GameMenuScreenMixin(class_2561 title) {
      super(title);
   }

   @Inject(
      method = {"method_25426"},
      at = {@At("TAIL")}
   )
   private void init(CallbackInfo ci) {
      class_642 server = Interface.aM_.method_1558();
      this.method_25396()
         .stream()
         .filter(child -> child instanceof class_4185)
         .map(child2 -> (class_4185)child2)
         .filter(
            button -> button.method_25369().method_10851() instanceof class_2588 class_2588VarMethod_10851
               && class_2588VarMethod_10851.method_11022().equals("menu.disconnect")
         )
         .findFirst()
         .ifPresent(button2 -> {
            ButtonWidgetAccessor accessor = (ButtonWidgetAccessor)button2;
            class_4241 original = accessor.getOnPress();
            accessor.setOnPress(widget -> {
               if (ServerUtil.e() && !this.disconnect) {
                  widget.method_25355(button2.method_25369().method_27661().method_10862(class_2583.field_24360.method_10977(class_124.field_1061)));
                  this.disconnect = true;
               } else {
                  original.onPress(widget);
               }
            });
         });
      if (server != null) {
         int maxY = this.method_25396()
            .stream()
            .filter(child3 -> child3 instanceof class_4185)
            .map(child4 -> ((class_4185)child4).method_46427())
            .max((v0, v1) -> v0.compareTo(v1))
            .orElse(this.field_22790 / 2);
         this.method_37063(class_4185.method_46430(class_2561.method_43470("Переподключиться"), btn -> {
            if (ServerUtil.e() && !this.reconnect) {
               btn.method_25355(class_2561.method_43470("Переподключиться").method_10862(class_2583.field_24360.method_10977(class_124.field_1061)));
               this.reconnect = true;
            } else {
               try {
                  Interface.aM_.field_1687.method_8525();
                  class_412.method_36877(new class_442(), Interface.aM_, class_639.method_2950(server.field_3761), server, false, (class_9112)null);
               } catch (Exception var4) {
               }
            }
         }).method_46434(this.field_22789 / 2 - 100, maxY + 24, 200, 20).method_46431());
      }
   }
}
