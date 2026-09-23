package moscow.rockstar.mixin.minecraft.client.gui.overlay;


import rockstar.client.animation.*;
import java.util.List;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.ChatHud;
import net.minecraft.client.gui.hud.ChatHudLine;
import net.minecraft.client.gui.hud.MessageIndicator;
import net.minecraft.network.message.MessageSignatureData;
import net.minecraft.text.Text;
import net.minecraft.util.math.MathHelper;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.client.animation.Easing;
import rockstar.client.render.chat.ChatAnimationRenderState;

@Mixin(value={ChatHud.class})
public abstract class ChatHudMixin {
    @Unique
    private static final float ROCKSTAR_FADE_TICKS = 4.0f;
    @Unique
    private static final float ROCKSTAR_SHIFT = 12.0f;
    @Unique
    private static final float ROCKSTAR_BAR_HEIGHT = 16.0f;
    @Shadow
    @Final
    private List<ChatHudLine.Visible> visibleMessages;
    @Shadow
    private int scrolledLines;
    @Unique
    private static boolean rockstar$animate;
    @Unique
    private static float rockstar$tick;
    @Unique
    private static boolean rockstar$focused;
    @Unique
    private static boolean rockstar$wasFocused;
    @Unique
    private static long rockstar$openStart;
    @Unique
    private static long rockstar$closeStart;
    @Unique
    private static int rockstar$focusedLines;
    @Unique
    private static float rockstar$lineOpacity;
    @Unique
    private static float rockstar$lineAlpha;
    @Unique
    private static float rockstar$lineShift;
    @Unique
    private static int rockstar$linesBefore;
    @Unique
    private static float rockstar$slideLines;
    @Unique
    private static long rockstar$slideStart;
    @Unique
    private static boolean rockstar$shifted;

    @Shadow
    public abstract int getLineHeight();

    @Shadow
    public abstract double getChatScale();

    @Inject(method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}, at={@At(value="HEAD")})
    private void rockstar$rememberLineCount(Text text, MessageSignatureData messageSignatureData, MessageIndicator messageIndicator, CallbackInfo callbackInfo) {
        rockstar$linesBefore = this.visibleMessages.size();
    }

    @Inject(method={"addMessage(Lnet/minecraft/text/Text;Lnet/minecraft/network/message/MessageSignatureData;Lnet/minecraft/client/gui/hud/MessageIndicator;)V"}, at={@At(value="TAIL")})
    private void rockstar$startSlide(Text text, MessageSignatureData messageSignatureData, MessageIndicator messageIndicator, CallbackInfo callbackInfo) {
        if (!BeautifullyModule.internalMethod09646() || this.scrolledLines > 0) {
            return;
        }
        int n = this.visibleMessages.size() - rockstar$linesBefore;
        if (n <= 0) {
            return;
        }
        rockstar$slideLines = Math.min(4, n);
        rockstar$slideStart = System.currentTimeMillis();
    }

    @Inject(method={"render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;IIIZZ)V"}, at={@At(value="HEAD")})
    private void rockstar$beginRender(DrawContext drawContext, TextRenderer textRenderer, int n, int n2, int n3, boolean bl, boolean bl2, CallbackInfo callbackInfo) {
        rockstar$animate = BeautifullyModule.internalMethod09646();
        rockstar$tick = (float)n + MinecraftClient.getInstance().getRenderTickCounter().getTickProgress(false);
        rockstar$focused = bl;
        rockstar$lineOpacity = 1.0f;
        rockstar$lineAlpha = 1.0f;
        rockstar$lineShift = 0.0f;
        ChatAnimationRenderState.reset();
        rockstar$shifted = false;
        if (bl != rockstar$wasFocused) {
            rockstar$wasFocused = bl;
            if (bl) {
                rockstar$openStart = System.currentTimeMillis();
            } else {
                rockstar$closeStart = System.currentTimeMillis();
            }
        }
        if (!rockstar$animate || rockstar$slideStart == Long.MIN_VALUE) {
            return;
        }
        float f = (float)(System.currentTimeMillis() - rockstar$slideStart) / 200.0f;
        if (f >= 1.0f || f < 0.0f) {
            return;
        }
        float f2 = 1.0f - Easing.internalField1822.ease(f, 0.0f, 1.0f, 1.0f);
        float f3 = f2 * rockstar$slideLines * this.getLineHeight() * (float)this.getChatScale();
        if (f3 <= 0.05f) {
            return;
        }
        drawContext.enableScissor(0, 0, drawContext.getScaledWindowWidth(), drawContext.getScaledWindowHeight() - 40);
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(0.0f, f3);
        rockstar$shifted = true;
    }

    @Inject(method={"render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;IIIZZ)V"}, at={@At(value="TAIL")})
    private void rockstar$endRender(DrawContext drawContext, TextRenderer textRenderer, int n, int n2, int n3, boolean bl, boolean bl2, CallbackInfo callbackInfo) {
        if (rockstar$shifted) {
            rockstar$shifted = false;
            drawContext.getMatrices().popMatrix();
            drawContext.disableScissor();
        }
        this.rockstar$drawClosingInput(drawContext);
    }

    @Redirect(method={"forEachVisibleLine"}, at=@At(value="INVOKE", target="Ljava/util/List;get(I)Ljava/lang/Object;", ordinal=0))
    private Object rockstar$captureLine(List<ChatHudLine.Visible> list, int n) {
        float f;
        ChatHudLine.Visible visible = list.get(n);
        rockstar$lineOpacity = 1.0f;
        rockstar$lineAlpha = 1.0f;
        rockstar$lineShift = 0.0f;
        if (!rockstar$animate || !(visible instanceof ChatHudLine.Visible)) {
            return visible;
        }
        ChatHudLine.Visible visible2 = visible;
        float f2 = ChatHudMixin.rockstar$life(rockstar$tick - (float)visible2.addedTime());
        if (rockstar$focused) {
            rockstar$lineAlpha = f = MathHelper.lerp((float)ChatHudMixin.rockstar$openProgress(), (float)f2, (float)1.0f);
        } else {
            rockstar$lineOpacity = f = Math.max(f2, ChatHudMixin.rockstar$closeFade());
        }
        rockstar$lineShift = (1.0f - f) * 12.0f;
        ChatAnimationRenderState.set(rockstar$lineAlpha, rockstar$lineShift);
        return visible;
    }

    @ModifyExpressionValue(method={"forEachVisibleLine"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/ChatHud$OpacityRule;calculate(Lnet/minecraft/client/gui/hud/ChatHudLine$Visible;)F"))
    private float rockstar$messageOpacity(float original) {
        return rockstar$animate ? rockstar$lineOpacity : original;
    }

    @Redirect(method={"forEachVisibleLine"}, at=@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/ChatHud;getVisibleLineCount()I"))
    private int rockstar$visibleLineCount(ChatHud chatHud) {
        int n = chatHud.getVisibleLineCount();
        if (rockstar$focused) {
            rockstar$focusedLines = n;
            return n;
        }
        if (!rockstar$animate || ChatHudMixin.rockstar$closeFade() <= 0.0f) {
            return n;
        }
        return Math.max(n, rockstar$focusedLines);
    }

    @Inject(method={"forEachVisibleLine"}, at={@At(value="RETURN")})
    private void rockstar$resetLineAnimation(CallbackInfoReturnable<Integer> callbackInfoReturnable) {
        rockstar$lineOpacity = 1.0f;
        rockstar$lineAlpha = 1.0f;
        rockstar$lineShift = 0.0f;
        ChatAnimationRenderState.reset();
    }

    @Unique
    private void rockstar$drawClosingInput(DrawContext drawContext) {
        if (!rockstar$animate || rockstar$focused) {
            return;
        }
        float f = ChatHudMixin.rockstar$closeFade();
        if (f <= 0.0f) {
            return;
        }
        int n = drawContext.getScaledWindowWidth();
        int n2 = drawContext.getScaledWindowHeight();
        int n3 = MinecraftClient.getInstance().options.getTextBackgroundColor(Integer.MIN_VALUE);
        int n4 = MathHelper.clamp((int)((int)((float)(n3 >>> 24) * f)), (int)0, (int)255);
        org.joml.Matrix3x2fStack matrixStack = drawContext.getMatrices();
        matrixStack.pushMatrix();
        matrixStack.translate(0.0f, (1.0f - f) * 16.0f);
        drawContext.fill(2, n2 - 14, n - 2, n2 - 2, n3 & 0xFFFFFF | n4 << 24);
        matrixStack.popMatrix();
    }

    @Unique
    private static boolean rockstar$lineAnimated() {
        return rockstar$lineShift != 0.0f || rockstar$lineAlpha < 1.0f;
    }

    @Unique
    private static float rockstar$life(float f) {
        float f2 = MathHelper.clamp((float)((200.0f - f) / 4.0f), (float)0.0f, (float)1.0f);
        return f2 * f2;
    }

    @Unique
    private static float rockstar$openProgress() {
        if (rockstar$openStart == Long.MIN_VALUE) {
            return 1.0f;
        }
        float f = (float)(System.currentTimeMillis() - rockstar$openStart) / 200.0f;
        if (f <= 0.0f) {
            return 0.0f;
        }
        if (f >= 1.0f) {
            return 1.0f;
        }
        return Easing.internalField1822.ease(f, 0.0f, 1.0f, 1.0f);
    }

    @Unique
    private static float rockstar$closeFade() {
        if (rockstar$closeStart == Long.MIN_VALUE) {
            return 0.0f;
        }
        float f = (float)(System.currentTimeMillis() - rockstar$closeStart) / 200.0f;
        if (f <= 0.0f) {
            return 1.0f;
        }
        if (f >= 1.0f) {
            return 0.0f;
        }
        return 1.0f - Easing.internalField1822.ease(f, 0.0f, 1.0f, 1.0f);
    }

    @Unique
    private static boolean rockstar$invisible(int n) {
        return (n & 0xFC000000) == 0;
    }

    @Unique
    private static int rockstar$fade(int n) {
        if (rockstar$lineAlpha >= 1.0f) {
            return n;
        }
        int n2 = MathHelper.clamp((int)((int)((float)(n >>> 24) * rockstar$lineAlpha)), (int)0, (int)255);
        return n & 0xFFFFFF | n2 << 24;
    }

    static {
        rockstar$openStart = Long.MIN_VALUE;
        rockstar$closeStart = Long.MIN_VALUE;
        rockstar$lineOpacity = 1.0f;
        rockstar$lineAlpha = 1.0f;
        rockstar$slideStart = Long.MIN_VALUE;
    }
}
