package ru.haron.mixin;

import haron.animation.TimedFloatAnimation;
import haron.module.ModuleManager;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.text.OrderedText;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={ChatHud.class})
public abstract class ChatHudMixin {
    @Shadow
    @Final
    private MinecraftClient client;
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;
    @Shadow
    private int scrolledLines;
    @Unique
    private Map<ChatHudLine.Visible, TimedFloatAnimation> messageAnimators;
    @Unique
    private int currentRenderIndex;
    @Unique
    private static final long FLAGS_TTL_MS = 250L;
    @Unique
    private long flagsTime = 0L;
    @Unique
    private boolean fChatAnim = false;

    @Shadow
    public abstract int getWidth();

    @Unique
    private Map<ChatHudLine.Visible, TimedFloatAnimation> getAnimators() {
        if (this.messageAnimators == null) {
            this.messageAnimators = new IdentityHashMap<ChatHudLine.Visible, TimedFloatAnimation>();
        }
        return this.messageAnimators;
    }

    @Unique
    private boolean isChatAnimationEnabled() {
        long now = System.currentTimeMillis();
        if (now - this.flagsTime > 250L) {
            this.flagsTime = now;
            this.fChatAnim = ModuleManager.ANIMATIONS.k() && ModuleManager.ANIMATIONS.chatMessagesEnabled.get();
        }
        return this.fChatAnim;
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderHead(DrawContext DrawContextVar, int i, int i2, int i3, boolean z, CallbackInfo callbackInfo) {
        if (this.isChatAnimationEnabled()) {
            Map<ChatHudLine.Visible, TimedFloatAnimation> animators = this.getAnimators();
            if (!animators.isEmpty()) {
                animators.values().forEach(v0 -> v0.a());
                animators.entrySet().removeIf(entry -> ((TimedFloatAnimation)entry.getValue()).d());
            }
            this.currentRenderIndex = 0;
        }
    }

    @Redirect(method={"render"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/DrawContext;drawTextWithShadow(Lnet/minecraft/client/font/TextRenderer;Lnet/minecraft/text/OrderedText;III)I"))
    private int redirectDrawTextWithShadow(DrawContext DrawContextVar, TextRenderer TextRendererVar, OrderedText OrderedTextVar, int i, int i2, int i3) {
        float fB = 0.0f;
        if (this.fChatAnim) {
            TimedFloatAnimation chatMessageSlideAnimation;
            int i4;
            if (this.messageAnimators != null && !this.messageAnimators.isEmpty() && (i4 = this.currentRenderIndex + this.scrolledLines) >= 0 && i4 < this.visibleMessages.size() && (chatMessageSlideAnimation = this.messageAnimators.get(this.visibleMessages.get(i4))) != null) {
                fB = chatMessageSlideAnimation.b();
            }
            ++this.currentRenderIndex;
        }
        return DrawContextVar.drawTextWithShadow(TextRendererVar, OrderedTextVar, i + (int)fB, i2, i3);
    }

    @Inject(method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}, at={@At(value="TAIL")})
    private void onAddMessage(CallbackInfo callbackInfo) {
        if (!this.isChatAnimationEnabled() || this.visibleMessages.isEmpty()) {
            return;
        }
        int iGetWidth = this.getWidth();
        long jFloatValue = (long)ModuleManager.ANIMATIONS.chatDuration.get();
        ChatHudLine.Visible class_7590Var = this.visibleMessages.get(0);
        Map<ChatHudLine.Visible, TimedFloatAnimation> animators = this.getAnimators();
        if (animators.containsKey(class_7590Var)) {
            return;
        }
        animators.put(class_7590Var, new TimedFloatAnimation(-iGetWidth, 0.0f, jFloatValue));
    }

    @Inject(method={"clear"}, at={@At(value="HEAD")})
    private void onClear(boolean z, CallbackInfo callbackInfo) {
        if (this.messageAnimators != null) {
            this.messageAnimators.clear();
        }
    }
}
