package ru.haron.mixin;

import haron.module.ModuleManager;
import haron.modules.visuals.HitColor;
import haron.render.HurtRenderState;
import haron.util.ColorUtils;
import java.awt.Color;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(value={EquipmentRenderer.class})
public class EquipmentRendererMixin {
    @Unique
    private static final Identifier HIT_COLOR_TEXTURE = Identifier.of((String)"haron", (String)"textures/misc/white.png");

    @ModifyArg(method={"render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/util/Identifier;)V"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/model/Model;render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;III)V"), index=4)
    private int modifyArmorColor(int i) {
        HitColor hitColor = ModuleManager.HIT_COLOR;
        if (!(hitColor.o() && hitColor.p() && HurtRenderState.a())) {
            return i;
        }
        Color colorN = hitColor.n();
        return ColorUtils.a(colorN.getRed(), colorN.getGreen(), colorN.getBlue(), 255);
    }
}

