package ru.prism.mixin;

import java.awt.Color;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.module.impl.utils.MaceHelper;

@Mixin(DrawContext.class)
public abstract class DrawContextMixin {

    @Shadow
    public abstract void fill(int x1, int y1, int x2, int y2, int color);

    @Inject(method = "drawItem(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;III)V", at = @At("HEAD"))
    private void prism$maceHotbarHighlight(LivingEntity entity, ItemStack stack, int x, int y, int seed, CallbackInfo ci) {
        MaceHelper maceHelper = MaceHelper.get();
        if (maceHelper == null || !maceHelper.isEnabled() || !maceHelper.hotbarHighlight.getValue() || !MaceHelper.isMace(stack)) {
            return;
        }

        Color color = MaceHelper.getChargeColor();
        this.fill(x, y, x + 16, y + 16, new Color(color.getRed(), color.getGreen(), color.getBlue(), 120).getRGB());
    }
}
