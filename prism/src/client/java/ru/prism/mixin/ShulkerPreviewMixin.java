package ru.prism.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.HandledScreen;
import net.minecraft.screen.slot.Slot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.module.impl.render.ShulkerPreview;
import ru.prism.module.impl.render.ShulkerPreviewRenderer;

@Mixin(HandledScreen.class)
public class ShulkerPreviewMixin {

    @Inject(method = "drawMouseoverTooltip", at = @At("HEAD"), cancellable = true)
    private void prism$shulkerPreview(DrawContext context, int mouseX, int mouseY, CallbackInfo ci) {
        ShulkerPreview module = ShulkerPreview.getInstance();
        if (module == null || !module.isEnabled() || !module.inventoryPreview.getValue()) return;

        Slot slot = ((HandledScreenAccessor) this).getFocusedSlot();
        if (slot == null || !slot.hasStack() || !ShulkerPreviewRenderer.isShulkerBox(slot.getStack())) return;

        ci.cancel();
    }
}
