package moscow.rockstar.mixin.minecraft.render.item;


import rockstar.client.esp.*;
import net.minecraft.client.render.item.ItemRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import rockstar.client.esp.FillEspFeature;
import rockstar.client.esp.FlameEspFeature;
import rockstar.client.esp.GlowEspFeature;

@Mixin(value={ItemRenderState.LayerRenderState.class})
public class ItemRenderStateMixin {
    @ModifyArg(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;submitItem(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/item/ItemDisplayContext;III[ILjava/util/List;Lnet/minecraft/client/render/RenderLayer;Lnet/minecraft/client/render/item/ItemRenderState$Glint;)V"), index=2)
    private int chams$modifyItemLight(int n) {
        if (GlowEspFeature.internalField0276 || FlameEspFeature.internalField0277 || FillEspFeature.internalField0277) {
            return 0xF000F0;
        }
        return n;
    }

    @ModifyArg(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/render/item/model/special/SpecialModelRenderer;render(Ljava/lang/Object;Lnet/minecraft/item/ItemDisplayContext;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;IIZI)V"), index=4)
    private int chams$modifySpecialModelLight(int n) {
        if (GlowEspFeature.internalField0276 || FlameEspFeature.internalField0277 || FillEspFeature.internalField0277) {
            return 0xF000F0;
        }
        return n;
    }
}
