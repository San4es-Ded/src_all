package moscow.rockstar.mixin.minecraft.client;









import rockstar.client.ui.*;
import rockstar.client.render.*;
import rockstar.client.notification.*;
import rockstar.client.i18n.*;
import rockstar.client.internal.ui.*;
import rockstar.client.internal.script.*;
import rockstar.client.internal.game.*;
import rockstar.client.internal.core.*;
import moscow.rockstar.mixin.accessors.ItemCooldownEntryAccessor;
import moscow.rockstar.mixin.accessors.ItemCooldownManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import pyrock.events.game.GameTickEvent;
import rockstar.client.render.PostProcessRenderer;
import rockstar.client.render.compat.LegacyShaderProgram;
import rockstar.modules.player.NoDelayModule;
import rockstar.client.internal.script.LocalConfigStore;
import rockstar.client.i18n.LanguageManager;
import rockstar.client.internal.script.McpInputController;
import rockstar.client.internal.core.ClientLifecycleBridge;
import rockstar.client.RockstarClient;
import rockstar.client.internal.core.MenuCategoryTab;
import rockstar.client.internal.game.SprintResetManager;
import rockstar.client.internal.ui.DeferredScreenTracker;
import rockstar.client.internal.core.AnimationAtlas;
import rockstar.client.internal.core.SpriteAnimationPlayer;
import rockstar.client.ui.UiNode;
import rockstar.client.notification.ItemNotification;

@Mixin(value={MinecraftClient.class})
public class MinecraftClientMixin {
    @Shadow
    private int itemUseCooldown;
    @Unique
    private long rockstar$lastUseCooldownAlertMs;
    @Unique
    private Item rockstar$lastUseCooldownAlertItem;

    @Inject(method={"onResolutionChanged"}, at={@At(value="RETURN")})
    public void onResolutionChanged(CallbackInfo callbackInfo) {
        UiNode.invalidateLayout();
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")})
    public void tick(CallbackInfo callbackInfo) {
        SprintResetManager.internalMethod03031();
        RockstarClient.getInstance().internalMethod03317().internalMethod06883(new GameTickEvent());
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/GameRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;Z)V", shift=At.Shift.BEFORE)})
    private void rockstar$beginCaptureFrame(boolean bl, CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod07189();
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/GameRenderer;render(Lnet/minecraft/client/render/RenderTickCounter;Z)V", shift=At.Shift.AFTER)})
    private void rockstar$applyRemainingPatches(boolean bl, CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod01672(2);
    }

    @Inject(method={"render"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/util/Window;swapBuffers(Lnet/minecraft/client/util/tracy/TracyFrameCapturer;)V", shift=At.Shift.BEFORE)})
    private void rockstar$finishCaptureFrame(boolean bl, CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod08006();
        McpInputController.internalMethod03757();
    }

    @Inject(method={"render"}, at={@At(value="RETURN")})
    private void rockstar$rotateShaderUniformBuffers(boolean bl, CallbackInfo callbackInfo) {
        LegacyShaderProgram.rotateUniformBuffers();
    }

    @Inject(method={"<init>"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;onResolutionChanged()V")})
    public void initializeClient(RunArgs runArgs, CallbackInfo callbackInfo) {
        ClientLifecycleBridge.internalMethod01636();
    }

    @Inject(method={"<init>"}, at={@At(value="RETURN")})
    public void endInitialize(RunArgs runArgs, CallbackInfo callbackInfo) {
        AnimationAtlas typedValue258 = AnimationAtlas.internalMethod02378(16, 16);
        typedValue258.internalMethod03950(RockstarClient.id("animations/combat.zip"));
        typedValue258.internalMethod03950(RockstarClient.id("animations/movement.zip"));
        typedValue258.internalMethod03950(RockstarClient.id("animations/visuals.zip"));
        typedValue258.internalMethod03950(RockstarClient.id("animations/player.zip"));
        typedValue258.internalMethod03950(RockstarClient.id("animations/other.zip"));
        typedValue258.internalMethod03950(RockstarClient.id("animations/search.zip"));
        typedValue258.internalMethod00146();
        AnimationAtlas typedValue259 = AnimationAtlas.internalMethod02378(12, 12);
        typedValue259.internalMethod03950(RockstarClient.id("animations/check_enable.zip"));
        typedValue259.internalMethod03950(RockstarClient.id("animations/check_disable.zip"));
        typedValue259.internalMethod00146();
        for (MenuCategoryTab typedValue206 : MenuCategoryTab.values()) {
            try {
                typedValue206.internalMethod03186(new SpriteAnimationPlayer(RockstarClient.id("animations/" + typedValue206.internalMethod02856().toLowerCase() + ".zip")));
            }
            catch (RuntimeException runtimeException) {
                // empty catch block
            }
        }
    }

    @Inject(method={"cleanUpAfterCrash"}, at={@At(value="HEAD")})
    private void saveConfigAfterCrash(CallbackInfo callbackInfo) {
        RockstarClient typedParameter001 = RockstarClient.internalField0240;
        if (typedParameter001.internalMethod06896()) {
            return;
        }
        LocalConfigStore typedValue132 = typedParameter001.internalMethod02152();
        if (typedValue132 == null) {
            return;
        }
        typedValue132.internalMethod07804();
    }

    @Inject(method={"stop"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/MinecraftClient;close()V", shift=At.Shift.AFTER)})
    public void shutdownClient(CallbackInfo callbackInfo) {
        PostProcessRenderer.internalMethod07187();
        ClientLifecycleBridge.internalMethod01639();
    }

    @Inject(method={"getWindowTitle"}, at={@At(value="HEAD")}, cancellable=true)
    public void changeWindowTitle(CallbackInfoReturnable<String> callbackInfoReturnable) {
        ClientLifecycleBridge.internalMethod06700(callbackInfoReturnable);
    }

    @Inject(method={"setScreen"}, at={@At(value="HEAD")}, cancellable=true)
    private void rockstar$silentPeekSuppress(Screen screen, CallbackInfo callbackInfo) {
        if (DeferredScreenTracker.internalMethod03735(screen)) {
            callbackInfo.cancel();
        }
    }

    @Inject(method={"doItemUse"}, at={@At(value="TAIL")})
    private void resetItemUseCooldown(CallbackInfo callbackInfo) {
        NoDelayModule typedValue291 = RockstarClient.getInstance().getModuleManager().getModule(NoDelayModule.class);
        if (typedValue291.isEnabled() && typedValue291.internalMethod02483().internalMethod04496()) {
            this.itemUseCooldown = typedValue291.internalMethod08871();
        }
    }

    @Inject(method={"doItemUse"}, at={@At(value="HEAD")})
    private void rockstar$alertItemUseCooldown(CallbackInfo callbackInfo) {
        MinecraftClient minecraftClient = (MinecraftClient)(Object)this;
        if (minecraftClient.player == null) {
            return;
        }
        ItemStack itemStack = minecraftClient.player.getMainHandStack();
        if (itemStack.isEmpty() || !minecraftClient.player.getItemCooldownManager().isCoolingDown(itemStack)) {
            itemStack = minecraftClient.player.getOffHandStack();
        }
        if (itemStack.isEmpty() || !minecraftClient.player.getItemCooldownManager().isCoolingDown(itemStack)) {
            return;
        }
        float f = this.rockstar$getRemainingCooldownSeconds(minecraftClient, itemStack);
        if (f <= 0.01f) {
            return;
        }
        long l = System.currentTimeMillis();
        Item item = itemStack.getItem();
        if (item == this.rockstar$lastUseCooldownAlertItem && l - this.rockstar$lastUseCooldownAlertMs < 600L) {
            return;
        }
        this.rockstar$lastUseCooldownAlertMs = l;
        this.rockstar$lastUseCooldownAlertItem = item;
        RockstarClient.getInstance().internalMethod02503().internalMethod02784(new ItemNotification(LanguageManager.internalMethod00160("alerts.cooldown", Float.valueOf(f)), itemStack));
    }

    @Unique
    private float rockstar$getRemainingCooldownSeconds(MinecraftClient minecraftClient, ItemStack itemStack) {
        ItemCooldownManagerAccessor itemCooldownManagerAccessor = (ItemCooldownManagerAccessor)(Object)minecraftClient.player.getItemCooldownManager();
        Identifier identifier = itemCooldownManagerAccessor.rockstar$getGroup(itemStack);
        Object object = itemCooldownManagerAccessor.rockstar$getEntries().get(identifier);
        if (object == null) {
            return 0.0f;
        }
        int n = ((ItemCooldownEntryAccessor)(Object)object).rockstar$getEndTick() - itemCooldownManagerAccessor.rockstar$getTick();
        return Math.max(0.0f, (float)n / 20.0f);
    }
}
