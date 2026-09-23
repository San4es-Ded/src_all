package ru.haron.mixin;

import haron.client.MinecraftClientAccess;
import haron.events.AttackTargetEvent;
import haron.events.WorldChangedEvent;
import haron.events.EventDispatcher;
import haron.gui.friends.FriendsTab;
import haron.render.FrameTimeTracker;
import java.lang.reflect.Method;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.RunArgs;
import net.minecraft.client.gui.screen.DownloadingTerrainScreen;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ru.haron.Haron;

@Mixin(value={MinecraftClient.class})
public class MinecraftClientMixin
implements MinecraftClientAccess {
    @Unique
    private static Method brandingTickMethod = null;
    @Unique
    private static boolean failedToGetMethod = false;

    @Inject(method={"joinWorld"}, at={@At(value="RETURN")})
    private void onWorldChange(ClientWorld ClientWorldVar, DownloadingTerrainScreen.WorldEntryReason class_9678Var, CallbackInfo callbackInfo) {
        EventDispatcher.EVENT_BUS.post((Object)new WorldChangedEvent());
    }

    @Inject(method={"<init>"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/render/debug/DebugRenderer;<init>(Lnet/minecraft/client/MinecraftClient;)V")})
    public void init(RunArgs RunArgsVar, CallbackInfo callbackInfo) {
        Haron.getInstance().init();
    }

    @Inject(method={"render"}, at={@At(value="FIELD", target="Lnet/minecraft/client/MinecraftClient;fpsCounter:I", opcode=181, ordinal=0)})
    private void render(boolean z, CallbackInfo callbackInfo) {
        if (!failedToGetMethod) {
            try {
                brandingTickMethod.invoke(null, new Object[0]);
            }
            catch (Throwable th) {
                failedToGetMethod = true;
            }
        }
        FrameTimeTracker.b();
    }

    @Inject(method={"tick"}, at={@At(value="HEAD")}, cancellable=true)
    private void onTick(CallbackInfo callbackInfo) {
        MinecraftClient client = (MinecraftClient)(Object)this;
        if (client.isWindowFocused() || System.currentTimeMillis() % 100L != 0L) {
            // empty if block
        }
    }

    @Inject(method={"isMultiplayerEnabled"}, at={@At(value="HEAD")}, cancellable=true)
    public void allowsMultiplayer(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(true);
    }

    @Inject(method={"getChatRestriction"}, at={@At(value="HEAD")}, cancellable=true)
    public void allowsChat(CallbackInfoReturnable<MinecraftClient.ChatRestriction> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(MinecraftClient.ChatRestriction.ENABLED);
    }

    @Inject(method={"isRealmsEnabled"}, at={@At(value="HEAD")}, cancellable=true)
    public void allowsRealms(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        callbackInfoReturnable.setReturnValue(false);
    }

    @Unique
    @Inject(method={"doAttack"}, at={@At(value="HEAD")}, cancellable=true)
    private void haron$friendAntiHit(CallbackInfoReturnable<Boolean> cir) {
        Entity target = null;
        if (MinecraftClientMixin.c.targetedEntity != null) {
            target = MinecraftClientMixin.c.targetedEntity;
        } else if (MinecraftClientMixin.c.crosshairTarget != null && MinecraftClientMixin.c.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            target = ((EntityHitResult)MinecraftClientMixin.c.crosshairTarget).getEntity();
        }
        if (target != null && !FriendsTab.canAttack(target)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method={"doAttack"}, at={@At(value="INVOKE", target="Lnet/minecraft/client/network/ClientPlayerInteractionManager;attackEntity(Lnet/minecraft/entity/player/PlayerEntity;Lnet/minecraft/entity/Entity;)V")})
    private void onAttack(CallbackInfoReturnable<Boolean> callbackInfoReturnable) {
        if (MinecraftClientMixin.c.targetedEntity != null) {
            EventDispatcher.EVENT_BUS.post((Object)new AttackTargetEvent(MinecraftClientMixin.c.targetedEntity));
        }
    }
}

