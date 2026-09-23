package ru.haron.mixin;

import haron.modules.visuals.CustomSwords;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.ItemModelManager;
import net.minecraft.client.render.item.ItemRenderState;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.CustomModelDataComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.ModelTransformationMode;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(value={ItemModelManager.class})
public abstract class ItemModelResolverMixin {
    @ModifyVariable(method={"update(Lnet/minecraft/client/render/item/ItemRenderState;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ModelTransformationMode;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;I)V"}, at=@At(value="HEAD"), argsOnly=true, ordinal=0, require=1)
    private ItemStack haron$replaceSwordModel(ItemStack stack, ItemRenderState renderState, ItemStack originalStack, ModelTransformationMode transformationMode, @Nullable World world, @Nullable LivingEntity entity, int seed) {
        CustomSwords module = CustomSwords.getInstance();
        if (module == null || !module.k() || !ItemModelResolverMixin.isSword(stack)) {
            return stack;
        }
        if (module.isSelfOnly()) {
            MinecraftClient mc = MinecraftClient.getInstance();
            if (entity != null && entity != mc.player) {
                return stack;
            }
        }
        ItemStack renderedStack = stack.copy();
        renderedStack.set(DataComponentTypes.CUSTOM_MODEL_DATA, new CustomModelDataComponent(List.of(), List.of(), List.of(module.getSelectedWeapon()), List.of()));
        return renderedStack;
    }

    private static boolean isSword(ItemStack stack) {
        return stack.isOf(Items.WOODEN_SWORD) || stack.isOf(Items.STONE_SWORD) || stack.isOf(Items.IRON_SWORD) || stack.isOf(Items.GOLDEN_SWORD) || stack.isOf(Items.DIAMOND_SWORD) || stack.isOf(Items.NETHERITE_SWORD);
    }
}
