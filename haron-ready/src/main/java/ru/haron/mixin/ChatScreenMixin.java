package ru.haron.mixin;

import haron.animation.ChatInputAnimation;
import haron.events.ChatSendEvent;
import haron.events.EventDispatcher;
import haron.gui.core.GuiInput;
import haron.hud.core.HudManager;
import haron.module.ModuleManager;
import haron.modules.visuals.Animations;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={ChatScreen.class})
public abstract class ChatScreenMixin
extends Screen {
    @Shadow
    protected TextFieldWidget chatField;
    @Unique
    private boolean matrixPushed = false;
    @Unique
    private int originalChatFieldY = 0;

    protected ChatScreenMixin(Text TextVar) {
        super(TextVar);
    }

    @Unique
    private boolean isChatAnimationEnabled() {
        return ModuleManager.ANIMATIONS.k() && ModuleManager.ANIMATIONS.chatMessagesEnabled.get();
    }

    @Inject(method={"sendMessage"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendMessage(String str, boolean z, CallbackInfo callbackInfo) {
        ChatSendEvent chatSendEvent = new ChatSendEvent(str);
        EventDispatcher.EVENT_BUS.post((Object)chatSendEvent);
        if (chatSendEvent.c()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"init"}, at={@At(value="TAIL")})
    private void onInit(CallbackInfo callbackInfo) {
        HudManager.a().a(true);
        if (this.isChatAnimationEnabled()) {
            ChatInputAnimation chatInputSlideAnimationO = Animations.chatInputAnimation();
            chatInputSlideAnimationO.d();
            chatInputSlideAnimationO.a((long)ModuleManager.ANIMATIONS.chatDuration.get());
            this.originalChatFieldY = this.chatField.getY();
        }
    }

    @Inject(method={"removed"}, at={@At(value="HEAD")})
    private void onRemoved(CallbackInfo callbackInfo) {
        HudManager.a().a(false);
        if (HudManager.a().h() != null) {
            HudManager.a().h().settingsPopup().reset();
            HudManager.a().h().g();
        }
        GuiInput.j();
        Animations.chatInputAnimation().d();
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseClicked(double d, double d2, int i, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (HudManager.a().a(d, d2, i)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (HudManager.a().b(mouseX, mouseY, button)) {
            return true;
        }
        return super.mouseReleased(mouseX, mouseY, button);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        HudManager.a().a(mouseX, mouseY, deltaX, deltaY);
        if (HudManager.a().b(mouseX, mouseY)) {
            return true;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Inject(method={"mouseScrolled"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseScrolled(double d, double d2, double d3, double d4, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (HudManager.a().a(d, d2, d4)) {
            callbackInfoReturnable.setReturnValue(true);
        } else if (this.isChatAnimationEnabled() && Animations.chatInputAnimation().c()) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"keyPressed"}, at={@At(value="HEAD")}, cancellable=true)
    private void onKeyPressed(int i, int i2, int i3, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (HudManager.a().a(i, i3)) {
            callbackInfoReturnable.setReturnValue(true);
        } else if (this.isChatAnimationEnabled() && Animations.chatInputAnimation().c() && i != 256) {
            callbackInfoReturnable.setReturnValue(false);
        }
    }

    @Inject(method={"render"}, at={@At(value="HEAD")})
    private void onRenderHead(DrawContext DrawContextVar, int i, int i2, float f, CallbackInfo callbackInfo) {
        GuiInput.h();
        if (!this.isChatAnimationEnabled()) {
            this.matrixPushed = false;
            return;
        }
        ChatInputAnimation chatInputSlideAnimationO = Animations.chatInputAnimation();
        chatInputSlideAnimationO.a();
        float fB = chatInputSlideAnimationO.b();
        if (fB <= 0.0f) {
            this.matrixPushed = false;
            return;
        }
        this.chatField.setY(this.originalChatFieldY + (int)fB);
        DrawContextVar.getMatrices().push();
        DrawContextVar.getMatrices().translate(0.0f, fB, 0.0f);
        this.matrixPushed = true;
    }

    @Inject(method={"render"}, at={@At(value="TAIL")})
    private void onRender(DrawContext DrawContextVar, int i, int i2, float f, CallbackInfo callbackInfo) {
        MinecraftClient MinecraftClientVarGetInstance;
        if (this.matrixPushed) {
            DrawContextVar.getMatrices().pop();
            this.matrixPushed = false;
        }
        if ((MinecraftClientVarGetInstance = MinecraftClient.getInstance()).isWindowFocused()) {
            HudManager.a().a(i, i2);
            if (HudManager.a().h() != null) {
                HudManager.a().h().a((double)i * MinecraftClientVarGetInstance.getWindow().getScaleFactor() / 2.0, (double)i2 * MinecraftClientVarGetInstance.getWindow().getScaleFactor() / 2.0);
            }
        } else {
            HudManager.a().a(i, i2);
            if (HudManager.a().h() != null) {
                HudManager.a().h().g();
            }
        }
        GuiInput.i();
    }
}
