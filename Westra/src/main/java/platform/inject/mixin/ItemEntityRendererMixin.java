package platform.inject.mixin;

import aethereal.core.Westra;
import aethereal.mixin.IItemEntityRenderState;
import aethereal.module.render.ItemPhysic;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.class_10039;
import net.minecraft.class_1542;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_7833;
import net.minecraft.class_916;
import org.joml.Quaternionf;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_916.class})
@Environment(EnvType.CLIENT)
public class ItemEntityRendererMixin {
   @Inject(
      method = {"method_62470(Lnet/minecraft/class_1542;Lnet/minecraft/class_10039;F)V"},
      at = {@At("TAIL")}
   )
   private void captureGround(class_1542 entity, class_10039 state, float tickDelta, CallbackInfo ci) {
      ((IItemEntityRenderState)state).setOnGround(entity.method_24828());
   }

   @Redirect(
      method = {"method_3996(Lnet/minecraft/class_10039;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4587;method_46416(FFF)V"
      )
   )
   private void translate(class_4587 matrices, float x, float y, float z) {
      if (Westra.h().d().t().ag().m()) {
         y = 0.0F;
      }

      matrices.method_46416(x, y, z);
   }

   @Redirect(
      method = {"method_3996(Lnet/minecraft/class_10039;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_4587;method_22907(Lorg/joml/Quaternionf;)V"
      )
   )
   private void cancelHover(class_4587 matrices, Quaternionf quaternion) {
      if (!Westra.h().d().t().ag().m()) {
         matrices.method_22907(quaternion);
      }
   }

   @Inject(
      method = {"method_3996(Lnet/minecraft/class_10039;Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;I)V"},
      at = {@At(
         value = "INVOKE",
         target = "Lnet/minecraft/class_916;method_56858(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_10428;Lnet/minecraft/class_5819;)V",
         shift = Shift.BEFORE
      )}
   )
   private void applyPhysics(class_10039 state, class_4587 matrices, class_4597 vertexConsumers, int light, CallbackInfo ci) {
      ItemPhysic itemPhysic = Westra.h().d().t().ag();
      if (itemPhysic.m()) {
         if (itemPhysic.q().c()) {
            matrices.method_22905(0.5F, 0.5F, 0.5F);
         }

         if (((IItemEntityRenderState)state).isOnGround()) {
            matrices.method_22907(class_7833.field_40714.rotationDegrees(90.0F));
         } else {
            float spin = class_1542.method_27314(state.field_53328, state.field_53435) * 300.0F;
            matrices.method_22907(class_7833.field_40714.rotationDegrees(spin));
         }
      }
   }
}
