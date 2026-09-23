package moscow.rockstar.mixin.minecraft.render.entity.equipment;



import rockstar.client.module.*;
import rockstar.client.esp.*;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.GlowRenderLayers;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.equipment.EquipmentRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import pyrock.utility.render.ColorRGBA;
import rockstar.modules.player.FreeCameraModule;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.RockstarClient;
import rockstar.client.esp.EspManager;
import rockstar.client.esp.GlowEspFeature;
import rockstar.client.MinecraftClientAccess;
import rockstar.client.module.Module;

@Mixin(value={EquipmentRenderer.class})
public abstract class EquipmentRendererMixin {
    private static final String ROCKSTAR$RENDER = "render(Lnet/minecraft/client/render/entity/equipment/EquipmentModel$LayerType;Lnet/minecraft/registry/RegistryKey;Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/item/ItemStack;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;ILnet/minecraft/util/Identifier;II)V";

    @WrapOperation(method={ROCKSTAR$RENDER}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/RenderLayers;armorCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;")})
    private RenderLayer rockstar$glowArmorLayer(Identifier identifier, Operation<RenderLayer> operation) {
        BeautifullyModule typedValue318 = RockstarClient.getInstance().getModuleManager().getModule(BeautifullyModule.class);
        if (typedValue318.isEnabled() && typedValue318.internalMethod05320().isSelected() && !typedValue318.internalMethod01947().internalMethod02884() && RockstarClient.internalField0410 == MinecraftClient.getInstance().player) {
            return net.minecraft.client.render.RenderLayers.itemEntityTranslucentCull((Identifier)identifier);
        }
        FreeCameraModule typedValue262 = RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class);
        if (typedValue262.internalMethod09314() && RockstarClient.internalField0410 == MinecraftClient.getInstance().player) {
            return net.minecraft.client.render.RenderLayers.itemEntityTranslucentCull((Identifier)identifier);
        }
        if (!GlowEspFeature.internalField0277) {
            return (RenderLayer)operation.call(new Object[]{identifier});
        }
        Entity entity = GlowEspFeature.internalField0410;
        if (entity == null) {
            return (RenderLayer)operation.call(new Object[]{identifier});
        }
        GlowEspFeature typedValue094 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class);
        if (typedValue094 == null || !typedValue094.internalMethod06209(entity)) {
            return (RenderLayer)operation.call(new Object[]{identifier});
        }
        if (typedValue094.internalMethod03008(entity) == null) {
            return (RenderLayer)operation.call(new Object[]{identifier});
        }
        return GlowRenderLayers.get(identifier);
    }

    @ModifyArg(
        method={ROCKSTAR$RENDER},
        at=@At(
            value="INVOKE",
            target="Lnet/minecraft/client/render/command/RenderCommandQueue;submitModel(Lnet/minecraft/client/model/Model;Ljava/lang/Object;Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/RenderLayer;IIILnet/minecraft/client/texture/Sprite;ILnet/minecraft/client/render/command/ModelCommandRenderer$CrumblingOverlayCommand;)V",
            ordinal=0
        ),
        index=6
    )
    private int rockstar$glowArmorColor(int n3) {
        ColorRGBA colorRGBA;
        MinecraftClientAccess internalValue0002;
        Object object;
        if (GlowEspFeature.internalField0277 && (object = GlowEspFeature.internalField0410) != null && (internalValue0002 = EspManager.internalMethod06726().internalMethod05464(GlowEspFeature.class)) != null && ((GlowEspFeature)internalValue0002).internalMethod06209((Entity)object) && (colorRGBA = ((GlowEspFeature)internalValue0002).internalMethod03008((Entity)object)) != null) {
            n3 = colorRGBA.getRGB();
        }
        if (((Module)(object = RockstarClient.getInstance().getModuleManager().getModule(BeautifullyModule.class))).isEnabled() && ((BeautifullyModule)object).internalMethod05320().isSelected() && !((BeautifullyModule)object).internalMethod01947().internalMethod02884() && RockstarClient.internalField0410 == MinecraftClient.getInstance().player) {
            n3 = ColorRGBA.applyOpacity(n3, ((BeautifullyModule)object).internalMethod01947().internalMethod02881()).getRGB();
        }
        if (((FreeCameraModule)(internalValue0002 = RockstarClient.getInstance().getModuleManager().getModule(FreeCameraModule.class))).internalMethod09314() && RockstarClient.internalField0410 == MinecraftClient.getInstance().player) {
            n3 = ColorRGBA.applyOpacity(n3, ((FreeCameraModule)internalValue0002).internalMethod00300()).getRGB();
        }
        return n3;
    }
}
