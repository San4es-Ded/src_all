package su.sacura.mixin.client.render.entity;

import java.awt.Color;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.render.entity.state.LivingEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import su.sacura.Sacura;
import su.sacura.features.modules.impl.render.HitColorModule;
import su.sacura.util.impl.render.other.FriendRenderContext;
import su.sacura.util.impl.render.other.HitColorTintState;
import su.sacura.util.impl.render.providers.ColorProvider;

@Mixin({LivingEntityRenderer.class})
public abstract class LivingEntityRendererMixin<S extends LivingEntityRenderState, M extends EntityModel<S>> {
    @Unique
    private static final ThreadLocal<Boolean> SV_SHOULD_TINT = HitColorTintState.SHOULD_TINT;

    @Inject(method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = {@At("RETURN")})
    private void simplevisuals$clearOwner(S state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        FriendRenderContext.CURRENT.remove();
        SV_SHOULD_TINT.set(Boolean.valueOf(false));
    }

    @Inject(method = {"render(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"}, at = {@At("HEAD")})
    private void simplevisuals$prepareTint(S state, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        HitColorModule module = (HitColorModule)Sacura.getInstance().getModuleManager().getModule(HitColorModule.class);
        if (module != null && module.enable && ((LivingEntityRenderState)state).hurt) {
            SV_SHOULD_TINT.set(Boolean.valueOf(true));
            ((LivingEntityRenderState)state).hurt = false;
        }
    }

    // Исправлено: getOpacity -> getMixColor (именно так метод называется в Yarn 1.21.4)
    @Overwrite
    public int getMixColor(S state) {
        HitColorModule module = (HitColorModule)Sacura.getInstance().getModuleManager().getModule(HitColorModule.class);
        if (module != null && module.enable && Boolean.TRUE.equals(SV_SHOULD_TINT.get())) {
            int baseColor;
            if (((Boolean)module.customColor.get()).booleanValue()) {
                baseColor = ((Integer)module.hitColor.get()).intValue();
            } else {
                baseColor = ColorProvider.getColorStyle(1.0F);
            }
            Color color = new Color(baseColor, true);
            int alpha = (int)(255.0F * ((Float)module.alpha.get()).floatValue());
            return (new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha)).getRGB();
        }
        return -1;
    }

    private static final Identifier SV_WHITE = Identifier.of("minecraft", "textures/misc/white.png");

    @Inject(method = {"getRenderLayer(Lnet/minecraft/client/render/entity/state/LivingEntityRenderState;ZZZ)Lnet/minecraft/client/render/RenderLayer;"}, at = {@At("HEAD")}, cancellable = true)
    private void simplevisuals$forceTranslucentLayer(S state, boolean showBody, boolean translucent, boolean showOutline, CallbackInfoReturnable<RenderLayer> cir) {
        HitColorModule module = (HitColorModule)Sacura.getInstance().getModuleManager().getModule(HitColorModule.class);
        if (module != null && module.enable && Boolean.TRUE.equals(SV_SHOULD_TINT.get()))
            cir.setReturnValue(RenderLayer.getEntityTranslucent(SV_WHITE));
    }
}