package moscow.rockstar.mixin.minecraft.client.gui.screen;




import rockstar.client.module.*;
import rockstar.client.animation.*;
import rockstar.client.internal.command.*;
import rockstar.client.compat.RenderSystem;
import net.minecraft.client.gui.Click;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ChatInputSuggestor;
import net.minecraft.client.gui.screen.ChatScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.game.SendMessageEvent;
import pyrock.events.render.ChatRenderEvent;
import pyrock.events.window.ChatClickEvent;
import pyrock.events.window.ChatKeyPressEvent;
import pyrock.events.window.ChatReleaseEvent;
import pyrock.events.window.ChatScrollEvent;
import pyrock.utility.render.CustomDrawContext;
import rockstar.client.internal.command.CommandRegistry;
import rockstar.modules.visual.BeautifullyModule;
import rockstar.modules.visual.ViewModelModule;
import rockstar.client.module.ModuleManager;
import rockstar.client.RockstarClient;
import rockstar.client.animation.Easing;
import rockstar.client.MinecraftClientAccess;

@Mixin(value={ChatScreen.class})
public class ChatScreenMixin
extends Screen
implements MinecraftClientAccess {
    @Shadow
    protected TextFieldWidget chatField;
    @Shadow
    private ChatInputSuggestor chatInputSuggestor;
    @Unique
    private long rockstar$openTime;
    @Unique
    private boolean rockstar$barShifted;

    protected ChatScreenMixin(Text text) {
        super(text);
    }

    @Inject(method={"init"}, at={@At(value="HEAD")})
    private void rockstar$startOpenAnimation(CallbackInfo callbackInfo) {
        this.rockstar$openTime = System.currentTimeMillis();
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/hud/ChatHud;render(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/font/TextRenderer;IIIZZ)V", shift=At.Shift.AFTER)})
    private void rockstar$pushInputAnimation(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        this.rockstar$barShifted = false;
        if (!BeautifullyModule.internalMethod09646()) {
            return;
        }
        float f2 = (float)(System.currentTimeMillis() - this.rockstar$openTime) / 200.0f;
        if (f2 >= 1.0f || f2 < 0.0f) {
            return;
        }
        float f3 = Easing.internalField1822.ease(f2, 0.0f, 1.0f, 1.0f);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)f3);
        drawContext.getMatrices().pushMatrix();
        drawContext.getMatrices().translate(0.0f, (1.0f - f3) * 16.0f);
        this.rockstar$barShifted = true;
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/gui/screen/ChatInputSuggestor;render(Lnet/minecraft/client/gui/DrawContext;II)V", shift=At.Shift.AFTER)})
    private void rockstar$popInputAnimation(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        if (!this.rockstar$barShifted) {
            return;
        }
        this.rockstar$barShifted = false;
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        drawContext.getMatrices().popMatrix();
    }

    @Inject(method={"sendMessage(Ljava/lang/String;Z)V"}, at={@At(value="HEAD")}, cancellable=true)
    private void onSendMessage(String string, boolean bl, CallbackInfo callbackInfo) {
        CommandRegistry typedValue128 = RockstarClient.getInstance().internalMethod05348();
        String string2 = typedValue128.internalMethod03606();
        if (!string2.isEmpty() && string.startsWith(string2 + string2)) {
            SendMessageEvent sendMessageEvent = new SendMessageEvent(string.substring(string2.length()));
            RockstarClient.getInstance().internalMethod03317().internalMethod06883(sendMessageEvent);
            if (!sendMessageEvent.isCancelled()) {
                ChatScreenMixin.internalField0149.player.networkHandler.sendChatMessage(sendMessageEvent.getMessage());
            }
            ChatScreenMixin.internalField0149.inGameHud.getChatHud().addToMessageHistory(string);
            callbackInfo.cancel();
            return;
        }
        if (!string2.isEmpty() && string.startsWith(string2)) {
            typedValue128.internalMethod04610(string);
            ChatScreenMixin.internalField0149.inGameHud.getChatHud().addToMessageHistory(string);
            callbackInfo.cancel();
            return;
        }
        SendMessageEvent sendMessageEvent = new SendMessageEvent(string);
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(sendMessageEvent);
        if (sendMessageEvent.isCancelled()) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"keyPressed(Lnet/minecraft/client/input/KeyInput;)Z"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTab(KeyInput input, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        int n = input.key();
        int n2 = input.scancode();
        int n3 = input.modifiers();
        if (RockstarClient.getInstance().internalMethod01271() != null && RockstarClient.getInstance().internalMethod01271().internalMethod07289(n, n3)) {
            callbackInfoReturnable.setReturnValue(true);
            return;
        }
        if (n != 256 && RockstarClient.getInstance().internalMethod01271() != null && RockstarClient.getInstance().internalMethod01271().internalMethod04745(n, n2, n3)) {
            callbackInfoReturnable.setReturnValue(true);
            return;
        }
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ChatKeyPressEvent(n, n2, n3));
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    public void render(DrawContext drawContext, int n, int n2, float f, CallbackInfo callbackInfo) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ChatRenderEvent(CustomDrawContext.of(drawContext), f));
    }

    @Inject(method={"mouseClicked"}, at={@At(value="HEAD")})
    private void onMouseClick(Click click, boolean doubled, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ChatClickEvent((float)click.x(), (float)click.y(), click.button()));
    }

    @Inject(method={"mouseScrolled"}, at={@At(value="HEAD")}, cancellable=true)
    private void onMouseScroll(double d, double d2, double d3, double d4, CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        ViewModelModule typedValue323;
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ChatScrollEvent((float)d, (float)d2, (float)d3, (float)d4));
        ModuleManager typedValue148 = RockstarClient.getInstance().getModuleManager();
        ViewModelModule typedValue324 = typedValue323 = typedValue148 == null ? null : typedValue148.getModule(ViewModelModule.class);
        if (typedValue323 != null && typedValue323.internalMethod05071((float)d, (float)d2, (float)d4)) {
            callbackInfoReturnable.setReturnValue(true);
        }
    }

    public boolean mouseReleased(Click click) {
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new ChatReleaseEvent((float)click.x(), (float)click.y(), click.button()));
        return true;
    }
}
