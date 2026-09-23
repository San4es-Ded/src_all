package moscow.rockstar.mixin.minecraft.client.gui.overlay;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.BlindnessEffectFogModifier;
import net.minecraft.client.render.fog.DarknessEffectFogModifier;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;
import pyrock.utility.render.ColorRGBA;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.modules.visual.CustomFogModule;
import rockstar.modules.visual.RemovalsModule;

@Mixin(FogRenderer.class)
public class FogRendererMixin {
    @Redirect(
        method = {
            "getFogColor(Lnet/minecraft/client/render/Camera;FLnet/minecraft/client/world/ClientWorld;IF)Lorg/joml/Vector4f;",
            "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;"
        },
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/fog/FogModifier;shouldApply(Lnet/minecraft/block/enums/CameraSubmersionType;Lnet/minecraft/entity/Entity;)Z"
        )
    )
    private boolean rockstar$filterRemovedStatusFog(FogModifier modifier, CameraSubmersionType submersionType, Entity cameraEntity) {
        if (!modifier.shouldApply(submersionType, cameraEntity)) {
            return false;
        }
        RemovalsModule removals = RockstarClient.getInstance().getModuleManager().getModule(RemovalsModule.class);
        if (!removals.isEnabled()) {
            return true;
        }
        if (modifier instanceof BlindnessEffectFogModifier) {
            return !removals.internalMethod09435().isSelected();
        }
        if (modifier instanceof DarknessEffectFogModifier) {
            return !removals.internalMethod09454().isSelected();
        }
        return true;
    }

    @ModifyReturnValue(method = "getFogColor", at = @At("RETURN"))
    private Vector4f rockstar$modifyFogColor(Vector4f original, Camera camera) {
        CustomFogModule module = RockstarClient.getInstance().getModuleManager().getModule(CustomFogModule.class);
        if (!module.internalMethod00459(camera)) {
            return original;
        }
        ColorRGBA color = module.internalMethod02354().internalMethod04496()
            ? ThemeColors.internalMethod02531().withAlpha(module.internalMethod00043().internalMethod08576() / 100.0F * 255.0F)
            : module.internalMethod02396().internalMethod05620();
        return new Vector4f(
            color.getRed() / 255.0F,
            color.getGreen() / 255.0F,
            color.getBlue() / 255.0F,
            color.getAlpha() / 255.0F
        );
    }

    @ModifyArgs(
        method = "applyFog(Lnet/minecraft/client/render/Camera;ILnet/minecraft/client/render/RenderTickCounter;FLnet/minecraft/client/world/ClientWorld;)Lorg/joml/Vector4f;",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/render/fog/FogRenderer;applyFog(Ljava/nio/ByteBuffer;ILorg/joml/Vector4f;FFFFFF)V"
        )
    )
    private void rockstar$modifyFogDistances(
        Args args,
        Camera camera,
        int viewDistance,
        RenderTickCounter renderTickCounter,
        float skyDarkness,
        ClientWorld world
    ) {
        CustomFogModule module = RockstarClient.getInstance().getModuleManager().getModule(CustomFogModule.class);
        if (!module.internalMethod00459(camera)) {
            return;
        }
        float farPlane = viewDistance * 16.0F;
        float start = Math.clamp(module.internalMethod02398().internalMethod06919(), -8.0F, farPlane);
        float end = Math.clamp(module.internalMethod02398().internalMethod07967(), 0.0F, farPlane);
        args.set(3, start);
        args.set(4, end);
        args.set(5, start);
        args.set(6, end);
    }
}
