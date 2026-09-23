package moscow.rockstar.mixin.minecraft.world;

import net.minecraft.client.render.LightmapTextureManager;
import net.minecraft.entity.LivingEntity;
import org.joml.Vector3f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.modules.visual.AmbienceModule;
import rockstar.modules.visual.RemovalsModule;
import rockstar.client.RockstarClient;

@Mixin(value={LightmapTextureManager.class})
public class MixinLightmapTextureManager {
    @Shadow
    private boolean dirty;
    @Unique
    private boolean rockstar$nightWasActive;

    @Inject(method={"update"}, at={@At(value="HEAD")})
    private void rockstar$forceNightModeRefresh(float f, CallbackInfo callbackInfo) {
        boolean bl;
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        boolean bl2 = bl = typedValue317 != null && typedValue317.internalMethod09245();
        if (bl || this.rockstar$nightWasActive) {
            this.dirty = true;
        }
        this.rockstar$nightWasActive = bl;
    }

    @Inject(method={"getDarkness"}, at={@At(value="HEAD")}, cancellable=true)
    private void onGetDarkness(LivingEntity entity, float factor, float tickProgress, CallbackInfoReturnable<Float> callbackInfoReturnable) {
        RemovalsModule typedValue322 = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (typedValue322.isEnabled() && typedValue322.internalMethod09454().isSelected()) {
            callbackInfoReturnable.setReturnValue(Float.valueOf(0.0f));
        }
    }

    @ModifyArg(
        method={"update"},
        at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/buffers/Std140Builder;putFloat(F)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal=3),
        index=0
    )
    private float rockstar$encodeNightStrength(float vanillaNightVision) {
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        return typedValue317 != null && typedValue317.internalMethod09245() && typedValue317.internalMethod06258() > 0.0F
            ? -typedValue317.internalMethod06258()
            : vanillaNightVision;
    }

    @ModifyArg(
        method={"update"},
        at=@At(value="INVOKE", target="Lcom/mojang/blaze3d/buffers/Std140Builder;putVec3(Lorg/joml/Vector3fc;)Lcom/mojang/blaze3d/buffers/Std140Builder;", ordinal=1),
        index=0
    )
    private org.joml.Vector3fc rockstar$encodeNightTint(org.joml.Vector3fc vanillaAmbientColor) {
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        return typedValue317 != null && typedValue317.internalMethod09245() && typedValue317.internalMethod06258() > 0.0F
            ? new Vector3f(typedValue317.internalMethod07168())
            : vanillaAmbientColor;
    }
}
