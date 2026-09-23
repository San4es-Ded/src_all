package ru.haron.mixin;

import haron.module.ModuleManager;
import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.BackgroundRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.Fog;
import net.minecraft.client.render.FogShape;
import net.minecraft.client.world.ClientWorld;
import org.joml.Vector4f;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={BackgroundRenderer.class})
public class BackgroundRendererMixin {
    @Inject(method={"getFogColor"}, at={@At(value="RETURN")}, cancellable=true)
    private static void customizeFogColor(Camera CameraVar, float f, ClientWorld ClientWorldVar, int i, float f2, CallbackInfoReturnable<Vector4f> callbackInfoReturnable) {
        if (ModuleManager.WORLD_CUSTOMIZER.n()) {
            int iS = ModuleManager.WORLD_CUSTOMIZER.s();
            callbackInfoReturnable.setReturnValue(new Vector4f((float)(iS >> 16 & 0xFF) / 255.0f, (float)(iS >> 8 & 0xFF) / 255.0f, (float)(iS & 0xFF) / 255.0f, 1.0f));
        }
    }

    @Inject(method={"applyFog"}, at={@At(value="RETURN")}, cancellable=true)
    private static void onApplyFog(Camera CameraVar, BackgroundRenderer.FogType class_4596Var, Vector4f vector4f, float f, boolean z, float f2, CallbackInfoReturnable<Fog> callbackInfoReturnable) {
        CameraSubmersionType CameraSubmersionTypeVarGetSubmersionType;
        if (ModuleManager.NO_FLUID.k() && ((CameraSubmersionTypeVarGetSubmersionType = CameraVar.getSubmersionType()) == CameraSubmersionType.WATER || CameraSubmersionTypeVarGetSubmersionType == CameraSubmersionType.LAVA)) {
            callbackInfoReturnable.setReturnValue(Fog.DUMMY);
            return;
        }
        if (ModuleManager.WORLD_CUSTOMIZER.n()) {
            int color = ModuleManager.WORLD_CUSTOMIZER.s();
            float r = (float)(color >> 16 & 0xFF) / 255.0f;
            float g = (float)(color >> 8 & 0xFF) / 255.0f;
            float b = (float)(color & 0xFF) / 255.0f;
            callbackInfoReturnable.setReturnValue(new Fog(ModuleManager.WORLD_CUSTOMIZER.p(), ModuleManager.WORLD_CUSTOMIZER.q(), FogShape.SPHERE, r, g, b, 1.0f));
        }
    }
}

