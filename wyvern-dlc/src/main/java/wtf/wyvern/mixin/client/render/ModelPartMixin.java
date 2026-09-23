package wtf.wyvern.mixin.client.render;

import java.util.List;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import wtf.wyvern.render.level.ChamsRenderer;

@Mixin(ModelPart.class)
public abstract class ModelPartMixin {
   @Shadow
   @Final
   private List<ModelPart.Cuboid> cuboids;

   /**
    * renderCuboids receives the fully-posed matrix entry (all parent transforms,
    * limb swing, head pitch, sneaking applied), so capturing here makes the chams
    * boxes follow every animation of the model exactly.
    */
   @Inject(method = "renderCuboids", at = @At("HEAD"))
   private void wyvern$captureChams(MatrixStack.Entry entry, VertexConsumer vertexConsumer, int light, int overlay, int color, CallbackInfo ci) {
      if (ChamsRenderer.isCapturing()) {
         for (ModelPart.Cuboid cuboid : this.cuboids) {
            ChamsRenderer.capture(entry, cuboid);
         }
      }
   }
}
