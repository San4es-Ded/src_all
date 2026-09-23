package moscow.rockstar.mixin.minecraft.client.gui;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.gui.render.GuiRenderer;
import net.minecraft.client.gui.render.state.ItemGuiElementRenderState;
import net.minecraft.util.math.ColorHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import rockstar.client.internal.ui.ItemGuiAlphaCompat;

@Mixin(GuiRenderer.class)
public class GuiRendererMixin {
    @ModifyArg(
        method = "prepareItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/render/state/TexturedQuadGuiElementRenderState;<init>(Lcom/mojang/blaze3d/pipeline/RenderPipeline;Lnet/minecraft/client/texture/TextureSetup;Lorg/joml/Matrix3x2f;IIIIFFFFILnet/minecraft/client/gui/ScreenRect;Lnet/minecraft/client/gui/ScreenRect;)V"
        ),
        index = 11
    )
    private int rockstar$applyItemAlpha(int color, @Local(argsOnly = true) ItemGuiElementRenderState state) {
        return ColorHelper.withAlpha(ItemGuiAlphaCompat.get(state), color);
    }
}
