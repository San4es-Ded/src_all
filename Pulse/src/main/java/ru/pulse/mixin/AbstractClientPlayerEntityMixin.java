package ru.pulse.mixin;

import net.minecraft.util.Identifier;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.entity.player.SkinTextures;
import net.minecraft.util.AssetInfo.TextureAssetInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pulse.cosmetic.CapeResolver;

@Mixin(AbstractClientPlayerEntity.class)
public abstract class AbstractClientPlayerEntityMixin {
   @Inject(method = "getSkin", at = @At("RETURN"), cancellable = true, require = 0)
   private void pulse$useCustomCapeTexture(CallbackInfoReturnable<SkinTextures> cir) {
      AbstractClientPlayerEntity player = (AbstractClientPlayerEntity)(Object)this;
      if (CapeResolver.b(player)) {
         Identifier capeTex = CapeResolver.a(player);
         if (capeTex != null) {
            SkinTextures original = (SkinTextures)cir.getReturnValue();
            if (original != null) {
               TextureAssetInfo customCapeAsset = new TextureAssetInfo(capeTex, capeTex);
               cir.setReturnValue(new SkinTextures(original.body(), customCapeAsset, customCapeAsset, original.model(), original.secure()));
            }
         }
      }
   }
}
