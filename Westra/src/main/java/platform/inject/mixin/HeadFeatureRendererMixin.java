package platform.inject.mixin;

import aethereal.core.EventManager;
import aethereal.core.IEvent;
import aethereal.event.HeadFeatureEvent;
import net.minecraft.class_10042;
import net.minecraft.class_10055;
import net.minecraft.class_1657;
import net.minecraft.class_310;
import net.minecraft.class_3882;
import net.minecraft.class_3883;
import net.minecraft.class_3887;
import net.minecraft.class_4587;
import net.minecraft.class_4597;
import net.minecraft.class_583;
import net.minecraft.class_976;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({class_976.class})
public abstract class HeadFeatureRendererMixin<S extends class_10042, M extends class_583<S> & class_3882> extends class_3887<S, M> {
   public HeadFeatureRendererMixin(class_3883<S, M> context) {
      super(context);
   }

   @Inject(
      method = {"method_17159(Lnet/minecraft/class_4587;Lnet/minecraft/class_4597;ILnet/minecraft/class_10042;FF)V"},
      at = {@At("HEAD")}
   )
   public void onRenderHead(class_4587 matrixStack, class_4597 vertexConsumerProvider, int i, S livingEntityRenderState, float f, float g, CallbackInfo ci) {
      if (livingEntityRenderState instanceof class_10055
         && class_310.method_1551().field_1687.method_8469(((class_10055)livingEntityRenderState).field_53528) instanceof class_1657 class_1657VarMethod_8469) {
         EventManager.a((IEvent)(new HeadFeatureEvent(matrixStack, vertexConsumerProvider, class_1657VarMethod_8469, (class_3882)this.method_17165())));
      }
   }
}
