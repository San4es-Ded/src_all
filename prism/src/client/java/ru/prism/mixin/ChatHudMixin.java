package ru.prism.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ru.prism.module.impl.render.Animations;
import ru.prism.module.impl.utils.ChatHelper;

import java.util.List;

@Mixin(ChatHud.class)
public abstract class ChatHudMixin {

    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;

    @Unique
    private boolean prism$chatAnimationEnabled() {
        Animations animations = Animations.get();
        return animations != null && animations.isEnabled() && animations.chat.getValue();
    }

    @Inject(
            method = "render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;IIIZZ)V",
            at = @At("HEAD")
    )
    private void prism$updateChatAnimations(DrawContext context, TextRenderer textRenderer, int ticks, int mouseX, int mouseY, boolean focused, boolean chatOpen, CallbackInfo ci) {
        if (prism$chatAnimationEnabled()) {
            Animations.get().updateChatMessages();
        }
    }

    @Inject(
            method = "addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V",
            at = @At("TAIL")
    )
    private void prism$addChatAnimation(Text message, MessageSignatureData signature, MessageIndicator indicator, CallbackInfo ci) {
        if (!prism$chatAnimationEnabled() || visibleMessages.isEmpty()) {
            return;
        }
        int width = ChatHud.getWidth(MinecraftClient.getInstance().options.getChatWidth().getValue());
        Animations.get().addChatMessage(visibleMessages.get(0).content(), width);
    }

    @Inject(method = "clear(Z)V", at = @At("HEAD"))
    private void prism$clearChatAnimations(boolean clearHistory, CallbackInfo ci) {
        Animations animations = Animations.get();
        if (animations != null) {
            animations.clearChatMessages();
        }
    }

    @Inject(method = "clear(Z)V", at = @At("HEAD"), cancellable = true)
    private void prism$saveChatHistory(boolean clearHistory, CallbackInfo ci) {
        if (!clearHistory) return;
        ChatHelper chatHelper = ChatHelper.get();
        if (chatHelper != null && chatHelper.isEnabled() && chatHelper.saveHistory.getValue()) {
            ci.cancel();
        }
    }
}
