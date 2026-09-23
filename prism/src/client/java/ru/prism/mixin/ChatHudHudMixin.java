package ru.prism.mixin;

import net.minecraft.client.font.Alignment;
import net.minecraft.client.font.DrawnTextConsumer;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import ru.prism.module.impl.render.Animations;

@Mixin(targets = "net.minecraft.client.gui.hud.ChatHud$Hud")
public class ChatHudHudMixin {

    @ModifyArg(
            method = "text",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/client/font/DrawnTextConsumer;text(Lnet/minecraft/client/font/Alignment;IILnet/minecraft/client/font/DrawnTextConsumer$Transformation;Lnet/minecraft/text/OrderedText;)V"
            ),
            index = 1
    )
    private int prism$slideChatMessage(Alignment alignment, int x, int y, DrawnTextConsumer.Transformation transformation, OrderedText text) {
        Animations animations = Animations.get();
        if (animations == null || !animations.isEnabled() || !animations.chat.getValue()) {
            return x;
        }
        return x + animations.chatMessageOffset(text);
    }
}
