package moscow.rockstar.mixin;




import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.internal.script.*;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.SkyRendering;
import net.minecraft.client.render.state.SkyRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.world.MoonPhase;
import org.joml.Vector4f;
import org.joml.Vector4fc;
import org.joml.Matrix4fStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import pyrock.utility.render.ColorRGBA;
import rockstar.modules.visual.AmbienceModule;
import rockstar.client.RockstarClient;
import rockstar.client.ui.ThemeColors;
import rockstar.client.render.ShaderPair;
import rockstar.client.internal.script.CustomSkyboxRenderer;

@Mixin(value={SkyRendering.class})
public class SkyRenderingMixin {
    @Inject(method={"updateRenderState"}, at={@At(value="TAIL")})
    private void rockstar$replaceSkyColor(ClientWorld world, float tickProgress, Camera camera, SkyRenderState state, CallbackInfo callbackInfo) {
        AmbienceModule typedValue317 = SkyRenderingMixin.rockstar$ambience();
        if (typedValue317 != null && typedValue317.isEnabled() && typedValue317.internalMethod06409().isSelected()) {
            state.skyColor = typedValue317.internalMethod00796().internalMethod04496()
                ? ThemeColors.internalMethod02531().getRGB()
                : typedValue317.internalMethod00850().internalMethod05620().getRGB();
        }
    }

    @Inject(method={"renderTopSky"}, at={@At(value="HEAD")}, cancellable=true)
    private void renderCustomSkybox(int color, CallbackInfo callbackInfo) {
        AmbienceModule typedValue317 = SkyRenderingMixin.rockstar$ambience();
        if (SkyRenderingMixin.rockstar$renderReplacementSky(typedValue317)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderEndSky"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$replaceEndSky(CallbackInfo callbackInfo) {
        AmbienceModule typedValue317 = SkyRenderingMixin.rockstar$ambience();
        if (SkyRenderingMixin.rockstar$renderReplacementSky(typedValue317)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderEndSky"}, at={@At(value="RETURN")})
    private void rockstar$renderShaderOverVanillaEndSky(CallbackInfo callbackInfo) {
        SkyRenderingMixin.rockstar$renderShaderOverVanillaSky();
    }

    @Inject(method={"renderGlowingSky"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideVanillaGlowForCustomSky(MatrixStack matrices, float solarAngle, int color, CallbackInfo callbackInfo) {
        if (SkyRenderingMixin.rockstar$hasReplacementSky()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderCelestialBodies"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideVanillaCelestialsForCustomSky(MatrixStack matrices, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase, float alpha, float starBrightness, CallbackInfo callbackInfo) {
        if (SkyRenderingMixin.rockstar$hasReplacementSky()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"renderCelestialBodies"}, at={@At(value="RETURN")})
    private void rockstar$renderShaderOverVanillaSky(MatrixStack matrices, float sunAngle, float moonAngle, float starAngle, MoonPhase moonPhase, float alpha, float starBrightness, CallbackInfo callbackInfo) {
        SkyRenderingMixin.rockstar$renderShaderOverVanillaSky();
    }

    @Inject(method={"renderSkyDark"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$hideVanillaSkyDarkForCustomSky(CallbackInfo callbackInfo) {
        if (SkyRenderingMixin.rockstar$hasReplacementSky()) {
            callbackInfo.cancel();
        }
    }

    @Unique
    private static AmbienceModule rockstar$ambience() {
        if (RockstarClient.getInstance().getModuleManager() == null) {
            return null;
        }
        return RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
    }

    @Unique
    private static ColorRGBA rockstar$skyTint(AmbienceModule typedValue317) {
        return typedValue317.internalMethod06409().isSelected() ? (typedValue317.internalMethod00796().internalMethod04496() ? ThemeColors.internalMethod02531() : typedValue317.internalMethod00850().internalMethod05620()) : ColorRGBA.WHITE;
    }

    @Unique
    private static boolean rockstar$hasReplacementSky() {
        AmbienceModule ambience = SkyRenderingMixin.rockstar$ambience();
        return ambience != null && ambience.internalMethod09413() && ambience.internalMethod09400();
    }

    @Unique
    private static boolean rockstar$renderReplacementSky(AmbienceModule ambience) {
        if (ambience == null || !ambience.internalMethod09413() || !ambience.internalMethod09400()) {
            return false;
        }
        SkyRenderingMixin.rockstar$withCurrentModelView(() -> {
            CustomSkyboxRenderer.internalMethod04631(ambience.internalMethod04216(), SkyRenderingMixin.rockstar$skyTint(ambience));
            if (ambience.internalMethod09402()) {
                SkyRenderingMixin.rockstar$renderShader(ambience);
            }
        });
        return true;
    }

    @Unique
    private static void rockstar$renderShaderOverVanillaSky() {
        AmbienceModule ambience = SkyRenderingMixin.rockstar$ambience();
        if (ambience != null && ambience.internalMethod09413() && !ambience.internalMethod09400() && ambience.internalMethod09402()) {
            SkyRenderingMixin.rockstar$withCurrentModelView(() -> SkyRenderingMixin.rockstar$renderShader(ambience));
        }
    }

    @Unique
    private static void rockstar$withCurrentModelView(Runnable draw) {
        Matrix4fStack modelView = RenderSystem.getModelViewStack();
        modelView.pushMatrix();
        modelView.set(com.mojang.blaze3d.systems.RenderSystem.getModelViewMatrix());
        try {
            draw.run();
        } finally {
            modelView.popMatrix();
        }
    }

    @Unique
    private static void rockstar$renderShader(AmbienceModule typedValue317) {
        float f = (float)(System.currentTimeMillis() % 100000000L) / 1000.0f;
        ShaderPair typedValue019 = typedValue317.internalMethod03395();
        if (typedValue019 != null) {
            CustomSkyboxRenderer.internalMethod01557(typedValue019, SkyRenderingMixin.rockstar$skyTint(typedValue317), f, typedValue317.internalMethod09226());
        } else {
            CustomSkyboxRenderer.internalMethod04527(typedValue317.internalMethod01702(), SkyRenderingMixin.rockstar$skyTint(typedValue317), f, typedValue317.internalMethod09226());
        }
    }

    @Inject(method={"close"}, at={@At(value="HEAD")})
    private void closeCustomSkybox(CallbackInfo callbackInfo) {
        CustomSkyboxRenderer.internalMethod01308();
    }

    @ModifyArg(
        method={"renderStars"},
        at=@At(value="INVOKE", target="Lnet/minecraft/client/gl/DynamicUniforms;write(Lorg/joml/Matrix4fc;Lorg/joml/Vector4fc;Lorg/joml/Vector3fc;Lorg/joml/Matrix4fc;)Lcom/mojang/blaze3d/buffers/GpuBufferSlice;"),
        index=1
    )
    private Vector4fc redirectStarColor(Vector4fc vanillaColor) {
        if (RockstarClient.getInstance().getModuleManager() == null) {
            return vanillaColor;
        }
        AmbienceModule typedValue317 = RockstarClient.getInstance().getModuleManager().getModule(AmbienceModule.class);
        if (typedValue317 == null) {
            return vanillaColor;
        }
        if (typedValue317.isEnabled() && typedValue317.internalMethod07969().isSelected()) {
            ColorRGBA colorRGBA = typedValue317.internalMethod00796().internalMethod04496()
                ? ThemeColors.internalMethod02531()
                : typedValue317.internalMethod08238().internalMethod05620();
            return new Vector4f(
                colorRGBA.getRed() / 255.0f,
                colorRGBA.getGreen() / 255.0f,
                colorRGBA.getBlue() / 255.0f,
                colorRGBA.getAlpha() / 255.0f
            );
        }
        return vanillaColor;
    }
}
